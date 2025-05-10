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
        boolean check = false;
        try {
            if (connection == null) {
                connection = Connect.dbConnect();
                check = true;
            }
            String query = "INSERT INTO "+ DaoUtility.getTableName(object)  + DaoUtility.getListColumns(object) + " VALUES";
            query = query + DaoUtility.generateBaraingo(object);
            System.out.println(query);
            Object[] attributeValues = DaoUtility.getAttributeValues(object);
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            Field[] fields = object.getClass().getDeclaredFields();
            System.out.println(fields.length);
            for (int i = 0; i < attributeValues.length; i++) {
                if (fields[i].getAnnotation(PrimaryKey.class) == null) {
//                    System.out.println(fields[i].getName());
                    preparedStatement.setObject(i+1,attributeValues[i]);
                }
            }
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.commit();
        }catch (Exception e){
            throw new RuntimeException(e);
        }finally {
            if(check) {
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
