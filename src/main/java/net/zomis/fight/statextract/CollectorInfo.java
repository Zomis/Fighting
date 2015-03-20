package net.zomis.fight.statextract;

import net.zomis.fight.statextract.types.StatCollector;

import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * Created by Simon on 3/13/2015.
 */
public class CollectorInfo {

    private final String name;
    private Object collectingObject;
    private final Collector<Object, Object, Object> collector;
    private final Supplier<Collector<?, ?, ?>> supplier;
    private final StatCollector statCollector;

    public CollectorInfo(String name, StatCollector statCollector, Supplier<Collector<?, ?, ?>> supplier) {
        this.name = name;
        this.statCollector = statCollector;
        this.supplier = supplier;
        Collector<?, ?, ?> collector = supplier.get();
        this.collector = (Collector<Object, Object, Object>) collector;
        this.collectingObject = collector.supplier().get();
    }

    public void accumulate(Object object) {
        this.collector.accumulator().accept(collectingObject, object);
    }

    CollectorInfo combine(CollectorInfo other) {
        CollectorInfo copy = copy();
        copy.collectingObject = copy.collector.combiner().apply(copy.collectingObject, other.collectingObject);
        copy.collectingObject = copy.collector.combiner().apply(copy.collectingObject, this.collectingObject);
        return copy;
    }

    public String getName() {
        return name;
    }

    public Object finish() {
        return collector.finisher().apply(collectingObject);
    }

    public CollectorInfo copy() {
        return new CollectorInfo(name, statCollector, supplier);
    }

    public Object getIndexValue() {
        return statCollector.getIndexValue(finish());
    }
}
