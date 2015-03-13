package net.zomis.fight.statextract;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
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
        if (extract == null) {
            throw new RuntimeException("Unable to post " + object + " of class "
                    + object.getClass() + ": No extract object available");
        }
        extract.add(this, object);
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
        extractFor(aClass).addCollector(name, fieldValue);
    }

    private Extract extractFor(Class<?> aClass) {
        extractors.putIfAbsent(aClass, new Extract());
        return extractors.get(aClass);
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

    public <T> void addPreHandler(Class<T> clazz, BiConsumer<Extractor, ? super T> preHandler) {
        extractFor(clazz).addPreHandler(preHandler);
    }

}
