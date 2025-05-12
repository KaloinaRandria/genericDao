package mg.working.utility;

import mg.working.dao.Connect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class IdGenerator {
    public String generateId(String prefix, String sequenceName) {
    String toReturn = "";
    String query = "SELECT nextval(?)";

    try (Connection connection = Connect.dbConnect();
         PreparedStatement preparedStatement = connection.prepareStatement(query)) {

        preparedStatement.setString(1, sequenceName);
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                toReturn = prefix + String.format("%04d", resultSet.getInt(1));
            } else {
                throw new SQLException("Impossible de récupérer la prochaine valeur de la séquence");
            }
        }
    } catch (SQLException e) {
        throw new RuntimeException("Erreur SQL lors de la génération de l'ID", e);
    }
    return toReturn;
}
}