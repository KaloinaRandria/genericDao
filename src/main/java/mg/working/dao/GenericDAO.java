package mg.working.dao;

import mg.working.annotations.PrimaryKey;
import mg.working.annotations.SearchWithCriteria;
import mg.working.utility.DaoUtility;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.ListIterator;

public class GenericDAO {
    public void save(Connection connection, Object object) throws SQLException {
    boolean localConnection = false;

    try {
        // Vérifier si une connexion doit être créée localement
        if (connection == null) {
            connection = Connect.dbConnect();
            localConnection = true;
            connection.setAutoCommit(false); // Désactiver le commit automatique
        }

        // Générer la requête SQL
        String query = "INSERT INTO " + DaoUtility.getTableName(object) +
                       DaoUtility.getListColumns(object) + // Cette méthode exclut "id"
                       " VALUES " + DaoUtility.generateBaraingo(object);
        System.out.println("Requête générée : " + query);

        // Récupérer les valeurs des attributs à insérer
        Object[] attributeValues = DaoUtility.getAttributeValues(object);
        PreparedStatement preparedStatement = connection.prepareStatement(query);

        // Assignation des placeholders
        int paramIndex = 1;
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.getAnnotation(PrimaryKey.class) == null) {
                field.setAccessible(true);
                Object value = field.get(object);
                System.out.println("Assignation paramètre #" + paramIndex + ": " + field.getName() + " = " + value);
                preparedStatement.setObject(paramIndex++, value);
            }
        }

        // Exécuter la requête
        preparedStatement.executeUpdate();
        preparedStatement.close();

        // Commit si connexion locale
        if (localConnection) {
            connection.commit();
        }
    } catch (Exception e) {
        // Si une exception survient, rollback si la connexion était locale
        if (localConnection && connection != null) {
            connection.rollback();
        }
        throw new RuntimeException(e);
    } finally {
        // Fermez la connexion si elle a été créée localement
        if (localConnection && connection != null) {
            connection.close();
        }
    }
}

    public List<?> findAll(Connection connection , Object object) throws SQLException {
        boolean check = false;
        List<Object> objects;
        try {
            if (connection == null){
                connection = Connect.dbConnect();
                check = true;
            }
            String query = "SELECT * FROM " + DaoUtility.getTableName(object);
            List<Object[]> data = DaoUtility.traitementDonnees(connection , query);
            objects = DaoUtility.instanceClassWithConstructor(object.getClass().getName(),data);
        }catch (Exception e){
            throw new RuntimeException(e);
        } finally {
            if (check){
                connection.close();
            }
        }
        return objects;
    }

    public List<?> findCriteria(Connection connection, Object object) throws SQLException {
        boolean check = false;
        List<Object> objects = null;
        try {
            if (connection == null) {
                connection = Connect.dbConnect();
                check = true;
            }
            StringBuilder query = new StringBuilder("SELECT * FROM " + DaoUtility.getTableName(object) + " WHERE 1=1");
            Field[] fields = object.getClass().getDeclaredFields();
            String[] columns = DaoUtility.getAllColumn(object);
            for (int i = 0; i < fields.length; i++) {
                    fields[i].setAccessible(true);
                    if (fields[i].get(object) != null) {
                        query.append(" AND ").append(columns[i]).append(" = '").append(fields[i].get(object).toString()).append("'");
                    }
                    fields[i].setAccessible(false);
            }
            List<Object[]> data = DaoUtility.traitementDonnees(connection, query.toString());
            objects = DaoUtility.instanceClassWithConstructor(object.getClass().getName(),data);
        }catch (Exception e){
            throw new RuntimeException(e);
        } finally {
            if (check){
                connection.close();
            }
        }
        return objects;
    }

    public List<?> findIntervalle(Connection connection ,Object object, Object value1 , Object value2) throws SQLException {
        boolean check = false;
        List<Object> objects = null;
        try {
            if(connection == null) {
                connection = Connect.dbConnect();
                check = true;
            }
            Field[] fields = object.getClass().getDeclaredFields();
            String[] columns = DaoUtility.getAllColumn(object);
            PreparedStatement preparedStatement;
            String query;
            if (value1 != null && value2 != null) {
                query = ("SELECT * FROM " + DaoUtility.getTableName(object) + " WHERE ");
                for (int i = 0; i < fields.length; i++) {
                    if (fields[i].getAnnotation(SearchWithCriteria.class) != null) {
                        query += columns[i] + " BETWEEN "+ value1 +" AND " + value2;
                    }
                }
                System.out.println(query);
                List<Object[]> data = DaoUtility.traitementDonnees(connection, query.toString());
                objects = DaoUtility.instanceClassWithConstructor(object.getClass().getName(),data);
            } else if (value1 != null) {
                query = ("SELECT * FROM " + DaoUtility.getTableName(object) + " WHERE ");
                for (int i = 0; i < fields.length; i++) {
                    if (fields[i].getAnnotation(SearchWithCriteria.class) != null) {
                        query += columns[i] + " >= " + value1;
                    }
                }
                System.out.println(query);
                List<Object[]> data = DaoUtility.traitementDonnees(connection, query.toString());
                objects = DaoUtility.instanceClassWithConstructor(object.getClass().getName(),data);
            } else if (value2 != null) {
                query = ("SELECT * FROM " + DaoUtility.getTableName(object) + " WHERE ");
                for (int i = 0; i < fields.length; i++) {
                    if (fields[i].getAnnotation(SearchWithCriteria.class) != null) {
                        query += columns[i] + " <= " + value2;
                    }
                }
                System.out.println(query);
                List<Object[]> data = DaoUtility.traitementDonnees(connection, query.toString());
                objects = DaoUtility.instanceClassWithConstructor(object.getClass().getName(),data);
            }
            connection.commit();
        } catch (Exception e){
            throw new RuntimeException(e);
        } finally {
            if (check) {
                connection.close();
            }
        }
        return objects;
    }

    public List<?> pagination(Connection connection , Object object, int debut, int limite) throws SQLException {
        boolean check = false;
        List<Object> objects = null;
        try {
            if (connection == null) {
                connection = Connect.dbConnect();
                check = true;
            }
            String query = "SELECT * FROM " + DaoUtility.getTableName(object) ;
            String dbName = connection.getCatalog();
            if (dbName.equals("oracle")) {
                query +=" ROMNUM <=" + limite + " OFFSET " + debut;
            }
            query += " OFFSET " + debut + "LIMIT " + limite;
            List<Object[]> data = DaoUtility.traitementDonnees(connection, query);
            objects = DaoUtility.instanceClassWithConstructor(object.getClass().getName(),data);
        }catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (check) {
                connection.close();
            }
        }
        return objects;
    }
}