package mg.working.dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Connect {
    private static final String DEFAULT_CONFIG_PATH = "src/main/resources/db.properties"; // Chemin par défaut

    public static Connection dbConnect() {
        return dbConnect(DEFAULT_CONFIG_PATH); // Utilise le chemin par défaut si aucun n'est spécifié
    }

    public static Connection dbConnect(String configPath) {
        Connection connection = null;
        Properties prop = new Properties();

        try (FileInputStream fis = new FileInputStream(configPath)) {
            // Charger le fichier .properties
            prop.load(fis);
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement du fichier " + configPath + " : " + e.getMessage());
            return null; // Retourne une connexion nulle en cas de problème
        }

        try {
            // Obtenir les informations de connexion
            String url = prop.getProperty("db.url");
            String user = prop.getProperty("db.username");
            String password = prop.getProperty("db.password");

            // Vérifier si les valeurs sont nulles
            if (url == null || user == null || password == null) {
                throw new IllegalArgumentException("Les paramètres de connexion (URL, utilisateur ou mot de passe) sont absents dans le fichier " + configPath);
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