package net.zomis.fight.statextract;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.function.ToIntFunction;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Created by Simon on 3/12/2015.
 */
public class Extractor {

    private final Object target;
    private final Map<Class<?>, Extract> extractors = new HashMap<>();

    private Extractor(Object target) {
        this.target = target;
    }

    public ExtractResults collect() {
        return new ExtractResults(extractors.entrySet().stream().collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue().finish())));
    }

    public void post(Object object) {
        Extract extract = extractors.get(object.getClass());
        extract.add(object);
    }

    public static Extractor extractor(Object target) {
        Extractor extractor = new Extractor(target);
        for (Field field : target.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            Object fieldValue = null;
            try {
                fieldValue = field.get(target);
                if (field.getType() == ToIntFunction.class) {
                    extractor.addExtractor(field.getName(), genericType(field, 0),
                            Collectors.summarizingInt((ToIntFunction) fieldValue));
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return extractor;
    }

    private void addExtractor(String name, Class<?> aClass, Collector<?, ?, ?> fieldValue) {
        extractors.putIfAbsent(aClass, new Extract());
        extractors.get(aClass).addCollector(name, fieldValue);
    }

    private static Class<?> genericType(Field field, int i) {
        Type genericFieldType = field.getGenericType();
        if (!(genericFieldType instanceof ParameterizedType)) {
            throw new IllegalArgumentException("Cannot deserialize a Map without generics types");
        }
        ParameterizedType aType = (ParameterizedType) genericFieldType;
        Type[] fieldArgTypes = aType.getActualTypeArguments();
        return (Class<?>) fieldArgTypes[i];
    }

}