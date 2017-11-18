package net.zomis.fight.v2;

import net.zomis.fight.GuavaExt;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class StatsFight {

    public static <T> IndexResults fightEvently(List<T> aiList, int count, StatsPerform<List<T>> perform, StatsExtract<List<T>> extract) {
        List<List<T>> subsets = GuavaExt.processSubsets(aiList, 2);
        // subsets.stream().flatMap(i -> IntStream.range(0, 1000).map());
        for (List<T> pair : subsets) {
            for (int i = 1; i <= count; i++) {
                extract.perform(perform, pair, i);
            }
        }
        return extract.finishResults();
    }

    public static <T> IndexResults performAll(Stream<T> stream, StatsPerform<T> performer, StatsExtract<T> extract) {
        AtomicInteger count = new AtomicInteger();
        stream.forEach(t -> extract.perform(performer, t, count.incrementAndGet()));
        return extract.finishResults();
    }

}
