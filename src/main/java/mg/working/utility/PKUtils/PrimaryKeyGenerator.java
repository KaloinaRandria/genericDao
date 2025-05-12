package mg.working.utility.PKUtils;

import mg.working.dao.Connect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PrimaryKeyGenerator {

    public static String generatePrimaryKey(Object obj) {
        try {
            PrimaryKeyDetails pkDetails = AnnotationUtils.getPrimaryKeyDetails(obj);

            if (!pkDetails.isAutoIncrement()) {
                try (Connection connection = Connect.dbConnect()) {
                    String query = "SELECT nextval(?)";
                    try (PreparedStatement ps = connection.prepareStatement(query)) {
                        ps.setString(1, pkDetails.getSequence());
                        try (ResultSet resultSet = ps.executeQuery()) {
                            if (resultSet.next()) {
                                String sequenceValue = resultSet.getString(1);
                                return pkDetails.getPrefix() +
                                       fillWithZeros(pkDetails.getLength(), pkDetails.getPrefix().length(), sequenceValue);
                            } else {
                                throw new RuntimeException("La séquence n'a retourné aucune valeur !");
                            }
                        }
                    }
                }
            }

            // Cas des clés auto-incrémentées (si applicable)
            return "default";
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la génération de la clé primaire : " + e.getMessage(), e);
        }
    }

    private static String fillWithZeros(int totalLength, int prefixLength, String value) {
        StringBuilder sb = new StringBuilder();
        int numberOfZeros = totalLength - prefixLength - value.length();
        for (int i = 0; i < numberOfZeros; i++) {
            sb.append("0");
        }
        return sb.append(value).toString();
    }
}