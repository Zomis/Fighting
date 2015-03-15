package net.zomis.fight.statextract;

import org.junit.Test;

import java.util.IntSummaryStatistics;
import java.util.function.ToIntFunction;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

/**
 * Created by Simon on 3/12/2015.
 */
public class ExtractTest {

    public static class Example {
        ToIntFunction<String> length = str -> str.length();
        ToIntFunction<String> numA = str -> (int) str.chars().filter(ch -> ch == 'a').count();
        ToIntFunction<Character> charValue = ch -> ch.charValue();
    }

    @Test(expected = StackOverflowError.class)
    public void stackOverflow() {
        Extractor extractor = Extractor.extractor(new Example());
        extractor.addPreHandler(String.class, (extr, str) -> str.chars().mapToObj(i -> (char) i).forEach(ch -> extr.post(ch)));
        extractor.addPreHandler(Character.class, (extr, ch) -> extr.post("--" + ch));
        extractor.post("test");
    }

    @Test
    public void finishTwice() {
        Collector<String, Object, IntSummaryStatistics> len = (Collector<String, Object, IntSummaryStatistics>) Collectors.summarizingInt((String str) -> str.length());
        Object obj = len.supplier().get();
        len.accumulator().accept(obj, "test");
        len.accumulator().accept(obj, "42");
        IntSummaryStatistics sum = len.finisher().apply(obj);
        assertEquals(2, sum.getCount());
        assertEquals(2, sum.getMin());
        assertEquals(4, sum.getMax());
        assertEquals(6, sum.getSum());

        IntSummaryStatistics sum2 = len.finisher().apply(obj);
        assertEquals(2, sum2.getCount());
        assertEquals(2, sum2.getMin());
        assertEquals(4, sum2.getMax());
        assertEquals(6, sum2.getSum());
    }

    @Test
    public void test() {
        Extractor extractor = Extractor.extractor(new Example());
        // This below
        extractor.addPreHandler(String.class, (extr, str) -> str.chars().mapToObj(i -> (char) i).forEach(ch -> extr.post(ch)));
        InstancePoster poster = extractor.postPrimary();
        poster.post("test");
        poster.post("Hello World");
        poster.post("yet another message");
        poster.post("a very long message that perhaps contains a bunch of a's");
        ExtractResults data = extractor.collect();
        System.out.println(data.getData());
        IntSummaryStatistics lengths = (IntSummaryStatistics) data.getData().get(String.class).get("length");
        assertEquals(4, lengths.getCount());
        assertEquals(90, lengths.getSum());
        assertEquals(4, lengths.getMin());
        assertEquals(56, lengths.getMax());

        IntSummaryStatistics numA = (IntSummaryStatistics) data.getData().get(String.class).get("numA");
        assertEquals(4, numA.getCount());
        assertEquals(9, numA.getSum());
        assertEquals(0, numA.getMin());
        assertEquals(7, numA.getMax());

        IntSummaryStatistics charValues = (IntSummaryStatistics) data.getData().get(Character.class).get("charValue");
        assertEquals(90, charValues.getCount());
        assertEquals(8580, charValues.getSum());
        assertEquals(32, charValues.getMin());
        assertEquals(121, charValues.getMax());


    }

}
