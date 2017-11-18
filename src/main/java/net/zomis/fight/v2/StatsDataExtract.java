package net.zomis.fight.v2;

/**
 * Interface to use when something has happened and you want to extract data.
 * @param <T>
 * @param <U>
 */
@FunctionalInterface
public interface StatsDataExtract<Q, T, U> {

    void extract(StatsStore<Q> store, T primary, U secondary);

}
