package net.zomis.fight.statextract;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Simon on 3/14/2015.
 */
public class InstancePoster implements Poster {

    private final Object key;
    private final Map<Class<?>, ClassExtractor> extractors;

    public InstancePoster(Object key, Map<Class<?>, ClassExtractor> extractors) {
        this.key = key;
        this.extractors = new HashMap<>();
        for (Map.Entry<Class<?>, ClassExtractor> ee : extractors.entrySet()) {
            this.extractors.put(ee.getKey(), ee.getValue().copy());
        }
    }

    public ExtractResults collect() {
        return new ExtractResults(extractors.entrySet().stream().collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue().finish())));
    }

    @Override
    public Poster post(Object object) {
        ClassExtractor extract = extractors.get(object.getClass());
        if (extract == null) {
            throw new RuntimeException("Unable to post " + object + " of class "
                    + object.getClass() + ": No extract object available");
        }
        extract.add(this, object);
        return this;
    }

    public InstancePoster combine(InstancePoster other) {

        throw new UnsupportedOperationException();
    }

}
