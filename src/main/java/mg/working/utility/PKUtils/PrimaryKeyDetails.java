package mg.working.utility.PKUtils;

import java.lang.reflect.Field;

public class PrimaryKeyDetails {
    private final Field field;
    private final String prefix;
    private final boolean autoIncrement;
    private final String sequence;
    private final int length;

    public PrimaryKeyDetails(Field field, String prefix, boolean autoIncrement, String sequence, int length) {
        this.field = field;
        this.prefix = prefix;
        this.autoIncrement = autoIncrement;
        this.sequence = sequence;
        this.length = length;
    }

    public Field getField() {
        return field;
    }

    public String getPrefix() {
        return prefix;
    }

    public boolean isAutoIncrement() {
        return autoIncrement;
    }

    public String getSequence() {
        return sequence;
    }

    public int getLength() {
        return length;
    }
}