package mg.working.utility;

import mg.working.annotations.PrimaryKey;

import java.lang.reflect.Field;

public class AnnotationProcessor {

    public static void processPrimaryKey(Object object) {
        Class<?> clazz = object.getClass();
        IdGenerator idGenerator = new IdGenerator();

        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(PrimaryKey.class)) {
                // Récupérer l'annotation
                PrimaryKey primaryKey = field.getAnnotation(PrimaryKey.class);

                // Lire les valeurs de l'annotation
                String prefix = primaryKey.prefix();
                String sequenceName = primaryKey.sequenceName();

                // Générer l'ID avec la méthode de IdGenerator
                String generatedId = idGenerator.generateId(prefix, sequenceName);

                // Rendre le champ accessible et lui affecter l'ID généré
                try {
                    field.setAccessible(true);
                    field.set(object, generatedId);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Erreur lors de l'accès au champ annoté", e);
                }
            }
        }
    }
}