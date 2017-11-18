package net.zomis.fight.v2;

import net.zomis.fight.ext.FNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;


public class IndexResults {

    private static final Logger logger = LoggerFactory.getLogger(IndexResults.class);

    private final Map<Object, IndexResults> children;
    private final Map<String, Object> values;
    private final Map<String, Collector<?, ?, ?>> coll;
    private final int depth;

    IndexResults(int depth) {
        this.depth = depth;
        this.values = new ConcurrentHashMap<>();
        this.coll = new ConcurrentHashMap<>();
        this.children = new ConcurrentHashMap<>();
    }

    public Map<String, Object> getValues() {
        return new HashMap<>(values);
    }

    public int getDepth() {
        return depth;
    }

    public Map<Object, IndexResults> getChildren() {
        return new HashMap<>(children);
    }

    void finish() {
        for (Entry<String, Collector<?, ?, ?>> ee : coll.entrySet()) {
            Collector<?, ?, ?> collector = ee.getValue();
            @SuppressWarnings("unchecked")
            Function<Object, Object> finisher = (Function<Object, Object>) collector.finisher();

            Object value = values.get(ee.getKey());
            Object newValue = finisher.apply(value);
            values.put(ee.getKey(), newValue);
        }
        this.children.values().forEach(IndexResults::finish);
    }

    void addRecursive(String valueName, Collector<Object, ?, ?> collector, List<Object> currentIndexes, List<Object> objects) {
        this.addRecursive(0, valueName, collector, currentIndexes, objects);
    }

    private void addRecursive(int indexIndex, String valueName,
             Collector<Object, ?, ?> collector, List<Object> currentIndexes, List<Object> objects) {
        logger.debug("Add recursive {} value {} indexes {} objects {}", indexIndex, valueName, currentIndexes, objects);
        Collector<Object, Object, Object> objectCollector = (Collector<Object, Object, Object>) collector;
        Object handler = values.computeIfAbsent(valueName, key -> objectCollector.supplier().get());
        coll.put(valueName, objectCollector);
        BiConsumer<Object, Object> accum = objectCollector.accumulator();
        objects.forEach(value -> accum.accept(handler, value));

        if (indexIndex == currentIndexes.size()) {
            return;
        }

        Object nextIndex = currentIndexes.get(indexIndex);
        this.children.computeIfAbsent(nextIndex, s -> new IndexResults(depth + 1));
        IndexResults next = this.children.get(nextIndex);
        next.addRecursive(indexIndex + 1, valueName, collector, currentIndexes, objects);
    }

    public String toMultiline() {
        StringBuilder str = new StringBuilder();
        for (Entry<String, Object> valueSet : this.values.entrySet()) {
            indent(str);
            str.append(valueSet.getKey());
            str.append(": ");
            str.append(valueSet.getValue());
            str.append(System.lineSeparator());
        }
        for (Entry<Object, IndexResults> indexes : this.children.entrySet()) {
            indent(str);
            str.append(indexes.getKey());
            str.append(System.lineSeparator());
            str.append(indexes.getValue().toMultiline());
        }
        return str.toString();
    }

    private void indent(StringBuilder str) {
        for (int i = 0; i < this.depth; i++) {
            str.append("  ");
        }
    }

}
