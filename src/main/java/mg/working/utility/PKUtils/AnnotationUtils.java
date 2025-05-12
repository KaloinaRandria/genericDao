package mg.working.utility.PKUtils;

import mg.working.annotations.PrimaryKey;

import java.lang.reflect.Field;

public class AnnotationUtils {

    public static PrimaryKeyDetails getPrimaryKeyDetails(Object obj) {
        Class<?> clazz = obj.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(PrimaryKey.class)) {
                PrimaryKey annotation = field.getAnnotation(PrimaryKey.class);
                return new PrimaryKeyDetails(
                    field,
                    annotation.prefix(),
                    annotation.autoIncrement(),
                    annotation.sequenceName(),
                    annotation.length()
                );
            }
        }
        throw new RuntimeException("Aucun champ annoté avec @PrimaryKey trouvé dans " + clazz.getName());
    }
}