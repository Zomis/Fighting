package net.zomis.fight.statextract.types;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by Simon on 3/20/2015.
 */
public class TypeHelp {

    public static Class<?> genericType(Field field, int i) {
        Type genericFieldType = field.getGenericType();
        if (!(genericFieldType instanceof ParameterizedType)) {
            throw new IllegalArgumentException("Cannot deserialize a Map without generics types");
        }
        ParameterizedType aType = (ParameterizedType) genericFieldType;
        Type[] fieldArgTypes = aType.getActualTypeArguments();
        return (Class<?>) fieldArgTypes[i];
    }

}
