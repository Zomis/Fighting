package net.zomis.fight.v2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class StatsExtract<Q> implements StatsInterface, StatsStore {

    private static final Logger logger = LoggerFactory.getLogger(StatsExtract.class);

    /**
     * <pre>Map< tag name, Map< key, List< values >>></pre>
     **/
    private final Map<String, Map<Object, List<Object>>> values = new HashMap<>();

    private final Map<String, Function> knownIndexes = new HashMap<>();
    private final Map<String, List<StatsDataExtract<Q, Object, Object>>> extractorsTuple = new HashMap<>();
    private Q current;
    private final Map<String, Collector<Object, ?, ?>> collectors = new HashMap<>();
    private IndexResults results = new IndexResults(0);
    private String[] indexes;

    public <T> StatsExtract<Q> value(String name, Class<T> valueType, Collector<T, ?, ?> collector) {
        if (this.collectors.containsKey(name)) {
            throw new IllegalStateException("Collector for '" + name + "' already exists");
        }
        this.collectors.put(name, (Collector<Object, ?, ?>) collector);
        return this;
    }

    public <T, U> StatsExtract<Q> dataTuple(String tag, Class<T> tClass, Class<U> uClass,
            StatsDataExtract<Q, T, U> extract) {
        this.extractorsTuple.putIfAbsent(tag, new ArrayList<>());
        extractorsTuple.get(tag).add((StatsDataExtract<Q, Object, Object>) extract);
        return this;
    }

    public StatsExtract<Q> indexes(String... indexes) {
        this.indexes = Arrays.copyOf(indexes, indexes.length);
        return this;
    }

    public <T> StatsExtract<Q> data(String tag, Class<T> tClass, StatsDataExtract<Q, T, Object> extract) {
        this.extractorsTuple.putIfAbsent(tag, new ArrayList<>());
        extractorsTuple.get(tag).add((StatsDataExtract<Q, Object, Object>) extract);
        return this;
    }

    @Override
    public <T, U> void postTuple(String tag, T primary, U secondary) {
        logger.debug("Post tuple '{}' primary: {} secondary: {}", tag, primary, secondary);
        this.extractorsTuple.getOrDefault(tag, Collections.emptyList()).forEach(s -> s.extract(this, primary, secondary));
    }

    @Override
    public <T> void post(String tag, T data) {
        logger.debug("Post {} data {}", tag, data);
        this.extractorsTuple.getOrDefault(tag, Collections.emptyList()).forEach(s -> s.extract(this, data, null));
    }

    private void finishCurrent() {
        if (knownIndexes.size() != indexes.length) {
            throw new IllegalStateException("Missing indexes: " + Arrays.stream(this.indexes)
                .filter(indexName -> !knownIndexes.containsKey(indexName)).collect(Collectors.toList()));
        }

        for (Map.Entry<String, Map<Object, List<Object>>> valueSet : values.entrySet()) {
            String valueName = valueSet.getKey();
            Map<Object, List<Object>> groupedValues = valueSet.getValue();
            for (Map.Entry<Object, List<Object>> groupedValue : groupedValues.entrySet()) {
                Object key = groupedValue.getKey();
                List<Object> objects = groupedValue.getValue();

                List<Object> currentIndexes = getIndexesFor(knownIndexes, key);
                Collector<Object, ?, ?> collector = collectors.get(valueName);
                this.results.addRecursive(valueName, collector, currentIndexes, objects);
                objects.clear();
            }
        }
    }

    private List<Object> getIndexesFor(Map<String, Function> knownIndexes, Object key) {
        return Arrays.stream(this.indexes)
            .map(knownIndexes::get)
            .map(fnc -> fnc.apply(key))
            .collect(Collectors.toList());
    }

    @Override
    public void index(String name, Function savedKeyToIndex) {
        this.knownIndexes.put(name, savedKeyToIndex);
    }

    public static <Q> StatsExtract<Q> create() {
        return new StatsExtract<>();
    }

    public IndexResults getResults() {
        return results;
    }

    @Override
    public void save(String name, Object key, Object value) {
        values.computeIfAbsent(name, s -> new HashMap<>());
        values.get(name).computeIfAbsent(key, s -> new ArrayList<>());
        values.get(name).get(key).add(value);
    }

    public void perform(StatsPerform<Q> perform, Q data, int count) {
        this.current = data;
        perform.perform(this, data, count);
        finishCurrent();
    }

    @Override
    public Q getCurrent() {
        return current;
    }

    @Override
    public Object get(String key) {
        throw new UnsupportedOperationException("Not implemented yet. Meant as a hashmap to store things between posts");
    }

    @Override
    public void set(String key, Object value) {
        throw new UnsupportedOperationException("Not implemented yet. Meant as a hashmap to store things between posts");
    }

    public IndexResults finishResults() {
        results.finish();
        return results;
    }

}
