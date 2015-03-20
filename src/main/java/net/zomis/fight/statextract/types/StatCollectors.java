package net.zomis.fight.statextract.types;

import java.util.HashMap;
import java.util.Map;
import java.util.function.ToIntFunction;

/**
 * Created by Simon on 3/20/2015.
 */
public class StatCollectors {

    private Map<Class<?>, StatCollector> collectorTypes = new HashMap<>();

    public StatCollectors() {
        this.collectorTypes.put(ToIntFunction.class, new ToIntFunctionSumCollector());
    }

    public StatCollector get(Class<?> type) {
        return collectorTypes.get(type);
    }
}
