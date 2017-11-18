package net.zomis.fight.v2;

import java.util.function.Function;

public interface StatsStore<Q> {

    void save(String name, Object key, Object value);

    Q getCurrent();
    <T> T get(String key);
    <T> void set(String key, T value);
    <T> void index(String name, Function<T, Object> savedKeyToIndex);

}
