package net.zomis.fight.statextract;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Simon on 3/12/2015.
 */
public class ExtractResults {

    private final Map<Class<?>, Map<String, Object>> data;

    public ExtractResults(Map<Class<?>, Map<String, Object>> data) {
        this.data = data;
    }

    public Map<Class<?>, Map<String, Object>> getData() {
        return new HashMap<>(data);
    }

}
