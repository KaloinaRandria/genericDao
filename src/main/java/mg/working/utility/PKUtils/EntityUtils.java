package mg.working.utility.PKUtils;

import java.lang.reflect.Field;

public class EntityUtils {

    public static void assignPrimaryKey(Object obj) {
        try {
            PrimaryKeyDetails pkDetails = AnnotationUtils.getPrimaryKeyDetails(obj);
            String primaryKey = PrimaryKeyGenerator.generatePrimaryKey(obj);

            Field pkField = pkDetails.getField();
            pkField.setAccessible(true); // Accéder au champ privé
            pkField.set(obj, primaryKey); // Affecter l'ID directement au champ
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l'affectation de la clé primaire : " + e.getMessage(), e);
        }
    }
}