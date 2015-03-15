package net.zomis.fight.statextract;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Collection of CollectorInfo for one specific class. The #add method will only retrieve objects of the relevant class.
 */
public class ClassExtractor {

    private final List<CollectorInfo> collectors = new ArrayList<>();
    private final List<BiConsumer<Poster, Object>> preHandlers = new ArrayList<>();

    void add(Poster extractor, Object object) {
        preHandlers.forEach(consumer -> consumer.accept(extractor, object));
        for (CollectorInfo collector : collectors) {
            collector.accumulate(object);
        }
    }

    public void addCollector(String name, Supplier<Collector<?, ?, ?>> fieldValue) {
        collectors.add(new CollectorInfo(name, fieldValue));
    }

    public Map<String, Object> finish() {
        return collectors.stream()
                .collect(Collectors.toMap(CollectorInfo::getName, coll -> coll.finish()));
    }

    ClassExtractor copy() {
        ClassExtractor copy = new ClassExtractor();
        for (CollectorInfo collectorInfo : collectors) {
            copy.collectors.add(collectorInfo.copy());
            copy.preHandlers.addAll(preHandlers);
        }
        return copy;
    }

    void addPreHandler(BiConsumer<Poster, ?> preHandler) {
        preHandlers.add((BiConsumer<Poster, Object>) preHandler);
    }

}
