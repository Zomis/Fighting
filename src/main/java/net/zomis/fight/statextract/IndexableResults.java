package net.zomis.fight.statextract;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * Created by Simon on 3/14/2015.
 */
public class IndexableResults {

    private final List<InstancePoster> posters;
    private final List<Map<String, CollectorInfo>> collectors;

    public IndexableResults(List<InstancePoster> posters) {
        this.posters = posters;
        this.collectors = new ArrayList<>();
        for (InstancePoster poster : posters) {
            Map<String, CollectorInfo> data = new HashMap<>();
            for (Map.Entry<Class<?>, ClassExtractor> ee : poster.extractors.entrySet()) {
                for (CollectorInfo coll : ee.getValue().collectors) {
                    data.put(coll.getName(), coll);
                }
            }
            collectors.add(data);
        }
    }

    public ExtractResults unindexed() {
        Map<String, Object> results = new HashMap<>();
        Map<String, CollectorInfo> temp = new HashMap<>();
        BiFunction<CollectorInfo, CollectorInfo, CollectorInfo> remapCollectors = (a, b) -> a.combine(b);
        for (Map<String, CollectorInfo> instance : collectors) {
            for (Map.Entry<String, CollectorInfo> ee : instance.entrySet()) {
                temp.merge(ee.getKey(), ee.getValue(), remapCollectors);
            }
        }
        for (Map.Entry<String, CollectorInfo> ee : temp.entrySet()) {
            results.put(ee.getKey(), ee.getValue().finish());
        }
        return new ExtractResults(null, results);
    }

    public ExtractResults indexBy(String... fields) {
        return null;
    }

    public List<ExtractResults> getResults() {
        return posters.stream().map(po -> po.collect()).collect(Collectors.toList());
    }

}
