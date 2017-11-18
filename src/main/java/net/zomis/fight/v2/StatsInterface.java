package net.zomis.fight.v2;

public interface StatsInterface {

    <T, U> void postTuple(String tag, T primary, U secondary);
    <T> void post(String tag, T data);

}
