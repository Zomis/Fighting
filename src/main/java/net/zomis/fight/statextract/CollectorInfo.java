package net.zomis.fight.statextract;

import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * Created by Simon on 3/13/2015.
 */
public class CollectorInfo {

    private final String name;
    private final Object collectingObject;
    private final Collector<Object, Object, Object> collector;
    private final Supplier<Collector<?, ?, ?>> collectorSupplier;

    public CollectorInfo(String name, Supplier<Collector<?, ?, ?>> supplier) {
        this.name = name;
        this.collectorSupplier = supplier;
        Collector<?, ?, ?> collector = supplier.get();
        this.collector = (Collector<Object, Object, Object>) collector;
        this.collectingObject = collector.supplier().get();
    }

    public void accumulate(Object object) {
        this.collector.accumulator().accept(collectingObject, object);
    }

    public String getName() {
        return name;
    }

    public Object finish() {
        return collector.finisher().apply(collectingObject);
    }

    public CollectorInfo copy() {
        return new CollectorInfo(name, collectorSupplier);
    }
}
