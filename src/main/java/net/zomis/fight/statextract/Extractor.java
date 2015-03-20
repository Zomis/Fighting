package net.zomis.fight.statextract;

import net.zomis.fight.statextract.types.StatCollector;
import net.zomis.fight.statextract.types.StatCollectors;

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

    public IndexableResults collectIndexable() {
        return new IndexableResults(posters);
    }

    public static Extractor extractor(Object target) {
        Extractor extractor = new Extractor(target);
        StatCollectors collectorTypes = new StatCollectors();
        for (Field field : target.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            final Object fieldValue;
            try {
                fieldValue = field.get(target);
                Objects.requireNonNull(fieldValue, "Field cannot be null: " + field.getName());
                StatCollector statCollector = collectorTypes.get(field.getType());
                if (statCollector != null) {
                    extractor.addExtractor(field.getName(), statCollector.postedType(field), statCollector, () -> statCollector.createCollector(fieldValue));
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return extractor;
    }

    private void addExtractor(String name, Class<?> aClass, StatCollector statCollector, Supplier<Collector<?, ?, ?>> fieldValue) {
        extractFor(aClass).addCollector(name, statCollector, fieldValue);
    }

    private ClassExtractor extractFor(Class<?> aClass) {
        classExtractorMap.putIfAbsent(aClass, new ClassExtractor());
        return classExtractorMap.get(aClass);
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
