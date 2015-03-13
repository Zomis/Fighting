package net.zomis.fight.statextract;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Created by Simon on 3/13/2015.
 */
public class Extract {

    private final List<CollectorInfo> collectors = new ArrayList<>();

    public void add(Object object) {
        for (CollectorInfo collector : collectors) {
            collector.accumulate(object);
        }
    }

    public void addCollector(String name, Collector<?, ?, ?> fieldValue) {
        collectors.add(new CollectorInfo(name, fieldValue));
    }

    public Map<String, Object> finish() {
        return collectors.stream().
                collect(Collectors.toMap(CollectorInfo::getName, coll -> coll.finish()));
    }

}
