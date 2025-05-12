package mg.working.utility;

import mg.working.dao.Connect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class IdGenerator {
    Connection connection = null;
    public String generateId(String prefix , String sequenceName){
        String toReturn = "";
        try {
            if (connection == null) {
                this.connection = Connect.dbConnect();
            }
            String query = "SELECT nextval(?)";
            PreparedStatement preparedStatement = this.connection.prepareStatement(query);
            preparedStatement.setString(1,sequenceName);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                toReturn = prefix + String.format("%04d", resultSet.getInt(1));
            } else {
                throw new SQLException("Impossible de recuperer la prochaine valeur de la sequence");
            }
            resultSet.close();
            preparedStatement.close();
            this.connection.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return toReturn;
    }
}
