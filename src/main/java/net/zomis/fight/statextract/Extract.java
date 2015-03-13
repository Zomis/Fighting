package net.zomis.fight.statextract;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Created by Simon on 3/13/2015.
 */
public class Extract {

    private final List<CollectorInfo> collectors = new ArrayList<>();
    private final List<BiConsumer<Extractor, Object>> preHandlers = new ArrayList<>();

    void add(Extractor extractor, Object object) {
        preHandlers.forEach(consumer -> consumer.accept(extractor, object));
        for (CollectorInfo collector : collectors) {
            collector.accumulate(object);
        }
    }

    public void addCollector(String name, Collector<?, ?, ?> fieldValue) {
        collectors.add(new CollectorInfo(name, fieldValue));
    }

    public Map<String, Object> finish() {
        return collectors.stream()
                .collect(Collectors.toMap(CollectorInfo::getName, coll -> coll.finish()));
    }

    void addPreHandler(BiConsumer<Extractor, ?> preHandler) {
        preHandlers.add((BiConsumer<Extractor, Object>) preHandler);
    }

}
