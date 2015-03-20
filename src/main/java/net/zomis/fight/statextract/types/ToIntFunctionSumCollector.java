package net.zomis.fight.statextract.types;

import java.lang.reflect.Field;
import java.util.IntSummaryStatistics;
import java.util.function.ToIntFunction;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Created by Simon on 3/20/2015.
 */
public class ToIntFunctionSumCollector implements StatCollector {

    @Override
    public Collector<?, ?, ?> createCollector(Object fieldValue) {
        return Collectors.summarizingInt((ToIntFunction) fieldValue);
    }

    @Override
    public Object getIndexValue(Object finish) {
        IntSummaryStatistics statistics = (IntSummaryStatistics) finish;
        return statistics.getSum();
    }

    @Override
    public Class<?> postedType(Field field) {
        return TypeHelp.genericType(field, 0);
    }

}
