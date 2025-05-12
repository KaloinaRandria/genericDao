package mg.working.utility;

import mg.working.annotations.Column;
import mg.working.annotations.PrimaryKey;
import mg.working.annotations.Table;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DaoUtility {
    public static String getClassName(Object object) {
        return object.getClass().getName();
    }

    public static Field[] getAllAttribute(Object object) {
        return object.getClass().getDeclaredFields();
    }

    public static Class[] attributeType(Object[] methodArguments) {
        Class[] attributes = new Class[methodArguments.length];
        for (int i = 0; i < attributes.length; i++) {
            attributes[i] = methodArguments[i].getClass();
            System.out.println("Argument Type = " + attributes[i].getSimpleName());
        }
        return attributes;
    }

    public static Object executeMethod(Object object , String methodName , Object[] methodArguments) {
        try {
            Method method = object.getClass().getMethod(methodName,attributeType(methodArguments));
            return method.invoke(object,methodArguments);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String getTableName(Object object) {
        if (object.getClass().isAnnotationPresent(Table.class)) {
            Table table = object.getClass().getAnnotation(Table.class);
            if (!table.name().equals("")) {
                return table.name();
            }
        }
        return object.getClass().getSimpleName();
    }
    public static String[] getAllColumn(Object object) {
        Field[] fields = object.getClass().getDeclaredFields();
        String[] valiny = new String[fields.length];
        for (int i = 0; i < fields.length; i++) {
            valiny[i] = fields[i].getName();
            if (fields[i].isAnnotationPresent(Column.class)) {
                Column column = fields[i].getAnnotation(Column.class);
                if (!column.name().equals("")) {
                    valiny[i] = column.name();
                }
            }
        }
        return valiny;
    }

    public static String getListColumns(Object object) {
        Field[] fields = object.getClass().getDeclaredFields();
        StringBuilder valiny = new StringBuilder("(");
        for (int i = 0; i < fields.length; i++) {
            if (fields[i].getAnnotation(PrimaryKey.class) == null) {
                String columnName = fields[i].getName();
                if (fields[i].isAnnotationPresent(Column.class)) {
                    Column column = fields[i].getAnnotation(Column.class);
                    if (!column.name().equals("")) {
                        columnName = column.name();
                    }
                }
                valiny.append(columnName);
                if (i != fields.length - 1) valiny.append(",");
            }
        }
        // Retirer la dernière virgule s'il reste une
        if (valiny.charAt(valiny.length() - 1) == ',') {
            valiny.deleteCharAt(valiny.length() - 1);
        }
        valiny.append(")");
        return valiny.toString();
    }

public static Object[] getAttributeValues(Object object) {
    Field[] fields = object.getClass().getDeclaredFields();
    List<Object> attributeValues = new ArrayList<>();
    for (Field field : fields) {
        try {
            // Ignorer les champs annotés avec @PrimaryKey
            if (field.getAnnotation(PrimaryKey.class) == null) {
                field.setAccessible(true); // Rendre accessible les champs privés
                attributeValues.add(field.get(object)); // Ajouter la valeur
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    return attributeValues.toArray();
}
public static String generateBaraingo(Object object) {
    StringBuilder placeholders = new StringBuilder("(");
    Field[] fields = object.getClass().getDeclaredFields();
    for (Field field : fields) {
        if (field.getAnnotation(PrimaryKey.class) == null) {
            placeholders.append("?,");
        }
    }
    // Supprimer la dernière virgule
    if (placeholders.charAt(placeholders.length() - 1) == ',') {
        placeholders.deleteCharAt(placeholders.length() - 1);
    }
    placeholders.append(")");
    return placeholders.toString();
}

    public static List<Object[]> traitementDonnees(Connection connection , String query) throws SQLException {
        List<Object[]> valiny = new ArrayList<>();
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
        while (resultSet.next()) {
            Object[] temp = new Object[resultSetMetaData.getColumnCount()];
            int count = 0;
            for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
                if (resultSet.getObject(i) != null) {
                    if (resultSet.getObject(i) instanceof BigDecimal) {
                        temp[count] = ((BigDecimal) resultSet.getObject(i)).intValue();
                    } else {
                        temp[count] = resultSet.getObject(i);
                    }
                } else {
                    temp[count] = 0.0;
                }
                count ++;
            }

            valiny.add(temp);
        }
        return valiny;
    }

   public static Class[] classList(Object[] champs) {
        Class[] valiny = new Class[champs.length];
        for (int i = 0; i < champs.length; i++) {
            valiny[i] = champs[i].getClass();
        }
        return valiny;
    }

   public static List<Object> instanceClassWithConstructor(String className, List<Object[]> data) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        List<Object> valiny = new ArrayList<>();
        if (!data.isEmpty()) {
            Class[] classList = classList(data.get(0));
            Class getClass = Class.forName(className);

            Constructor constructor = getClass.getConstructor(classList);
            for (int i = 0; i < data.size(); i++) {
                valiny.add(constructor.newInstance(data.get(i)));
            }
        }
        return valiny;
   }
}