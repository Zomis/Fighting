package net.zomis.fight.statextract;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Created by Simon on 3/12/2015.
 */
public class Extractor {

    private final Object target;
    private final Map<Class<?>, ClassExtractor> classExtractorMap = new HashMap<>();
    private final List<InstancePoster> posters = Collections.synchronizedList(new ArrayList<>());

    private Extractor(Object target) {
        this.target = target;
    }

    @Deprecated
    public ExtractResults collect() {
        return new ExtractResults(classExtractorMap.entrySet().stream().collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue().finish())));
    }

    public IndexableResults collectIndexable() {
        return new IndexableResults(posters);
    }

    public static Extractor extractor(Object target) {
        Extractor extractor = new Extractor(target);
        for (Field field : target.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            final Object fieldValue;
            try {
                fieldValue = field.get(target);
                Objects.requireNonNull(fieldValue, "Field cannot be null: " + field.getName());
                if (field.getType() == ToIntFunction.class) {
                    extractor.addExtractor(field.getName(), genericType(field, 0),
                            () -> Collectors.summarizingInt((ToIntFunction) fieldValue));
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return extractor;
    }

    private void addExtractor(String name, Class<?> aClass, Supplier<Collector<?, ?, ?>> fieldValue) {
        extractFor(aClass).addCollector(name, fieldValue);
    }

    private ClassExtractor extractFor(Class<?> aClass) {
        classExtractorMap.putIfAbsent(aClass, new ClassExtractor());
        return classExtractorMap.get(aClass);
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

    public <T> void addPreHandler(Class<T> clazz, BiConsumer<Poster, ? super T> preHandler) {
        extractFor(clazz).addPreHandler(preHandler);
    }

    public InstancePoster postPrimary() {
        InstancePoster poster = new InstancePoster(classExtractorMap);
        posters.add(poster);
        return poster;
    }

}
