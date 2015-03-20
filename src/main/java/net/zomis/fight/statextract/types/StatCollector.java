package net.zomis.fight.statextract.types;

import java.lang.reflect.Field;
import java.util.stream.Collector;

/**
 * Created by Simon on 3/20/2015.
 */
public interface StatCollector {

    Collector<?,?,?> createCollector(Object fieldValue);

    Object getIndexValue(Object finish);

    Class<?> postedType(Field field);

}
