package mg.working.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.InputStream;

public class Connect {
    public static Connection dbConnect() {
        Connection connection = null;
        Properties prop = new Properties();

        try (InputStream input = Connect.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (input == null) {
                throw new IOException("Fichier db.properties introuvable dans le classpath");
            }
            prop.load(input);
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement du fichier db.properties : " + e.getMessage());
        }

        try {
            // Obtenir les informations de connexion
            String url = prop.getProperty("db.url");
            String user = prop.getProperty("db.username");
            String password = prop.getProperty("db.password");

            // Vérifier si les valeurs sont nulles
            if (url == null || user == null || password == null) {
                throw new IllegalArgumentException("Les paramètres de connexion (URL, utilisateur ou mot de passe) sont absents dans le fichier db.properties");
            }

            // Charger le driver JDBC
            Class.forName("org.postgresql.Driver");

            // Initialiser la connexion
            connection = DriverManager.getConnection(url, user, password);
            connection.setAutoCommit(false); // Désactiver l'auto-commit

            System.out.println("Connexion établie avec la base de données.");

        } catch (ClassNotFoundException e) {
            System.err.println("Driver JDBC introuvable : " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Erreur de connexion à la base de données : " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Erreur inattendue : " + e.getMessage());
        }

        return connection;
    }
}