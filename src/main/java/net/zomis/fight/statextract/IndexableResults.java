package net.zomis.fight.statextract;

import java.util.*;
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

    public Map<Object, Object> indexBy(String... fields) {
        Map<Object, Object> results = new HashMap<>();
        Map<String, CollectorInfo> temp = new HashMap<>();
        BiFunction<CollectorInfo, CollectorInfo, CollectorInfo> remapCollectors = (a, b) -> a.combine(b);
        for (Map<String, CollectorInfo> instance : collectors) {
            Map<Object, Object> current = results;
            for (int index = 0; index < fields.length; index++) {
                String indexKey = fields[index];
                // if index is not last, put into a map of value-for-field, more-values
                // if index is last, put the real actual values into the map
                CollectorInfo indexCollector = instance.get(indexKey);
                Object indexValue = indexCollector.getIndexValue();

                if (index < fields.length - 1) {
                    throw new UnsupportedOperationException();
                } else { // final part
                    mergeEndResults(current, indexValue, instance);
                }
            }
        }
        finish(results);
        return results;
    }

    private void finish(Map<Object, Object> results) {
        for (Map.Entry<Object, Object> ee : results.entrySet()) {
            if (ee.getValue() instanceof FinalResults) {
                FinalResults fin = (FinalResults) ee.getValue();
                ee.setValue(fin.finish());
            } else {
                finish((Map<Object, Object>) ee.getValue());
            }
        }
    }

    private void mergeEndResults(Map<Object, Object> current, Object indexValue, Map<String, CollectorInfo> instance) {
        if (current.get(indexValue) == null) {
            current.put(indexValue, new FinalResults());
        }
        FinalResults fin = (FinalResults) current.get(indexValue);
        fin.merge(instance);
    }

    public static class FinalResults {
        private static final BiFunction<CollectorInfo, CollectorInfo, CollectorInfo> remapCollectors = (a, b) -> a.combine(b);

        private final Map<String, CollectorInfo> temp = new HashMap<>();

        public void merge(Map<String, CollectorInfo> instance) {
            for (Map.Entry<String, CollectorInfo> ee : instance.entrySet()) {
                temp.merge(ee.getKey(), ee.getValue(), remapCollectors);
            }
        }

        public Map<String, Object> finish() {
            Map<String, Object> results = new HashMap<>();
            for (Map.Entry<String, CollectorInfo> ee : temp.entrySet()) {
                results.put(ee.getKey(), ee.getValue().finish());
            }
            return results;
        }
    }


    public List<ExtractResults> getResults() {
        return posters.stream().map(po -> po.collect()).collect(Collectors.toList());
    }

}
