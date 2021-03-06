package net.zomis.fight.statextract;

import org.junit.jupiter.api.Test;

import java.util.IntSummaryStatistics;
import java.util.Map;
import java.util.function.Function;
import java.util.function.ToIntFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by Simon on 3/12/2015.
 */
public class IndexTest {

    public static class IndexExample {
        Function<String, Integer> sizeIndex = str -> str.length();
        Function<String, Character> firstLetter = str -> str.charAt(0);

        ToIntFunction<String> length = str -> str.length();
        ToIntFunction<String> numA = str -> (int) str.chars().filter(ch -> ch == 97).count();
    }

    @Test
    public void indexed() {
        Extractor extractor = Extractor.extractor(new IndexExample());
        extractor.postPrimary().post("teat");
        extractor.postPrimary().post("aaaa");
        extractor.postPrimary().post("af");
        extractor.postPrimary().post("tt");

        IndexableResults results = extractor.collectIndexable();
        Map<Object, Object> data = results.indexBy("length");
        System.out.println("INDEXED ----");
        System.out.println(data);
    }

    @Test
    public void unindexed() {
        Extractor extractor = Extractor.extractor(new IndexExample());
        extractor.postPrimary().post("teat");
        extractor.postPrimary().post("a_a_");
        extractor.postPrimary().post("tca");
        extractor.postPrimary().post("abc");
        extractor.postPrimary().post("aaa");
/*
Unindexed: { length = 4+4+3+3+3, numA = 1+2+1+1+3 }
size=3: { length = 3+3+3, numA = 1+1+3 }
size=4: { length = 4+4, numA = 1+2 }
firstLetter=t: { length = 4+3, numA = 1+1 }
firstLetter=a: { length = 4+3+3, numA = 2+1+3 }
size=3,firstLetter=a: { length = 3+3, numA = 1+3 }
size=3,firstLetter=t: { length = 3, numA = 1 }
size=4,firstLetter=a: { length = 4, numA = 2 }
size=4,firstLetter=t: { length = 4, numA = 1 }
*/

        IndexableResults results = extractor.collectIndexable();
        for (ExtractResults ee : results.getResults()) {
            System.out.println(ee.getData());
        }
        Map<Class<?>, Map<String, Object>> data = results.unindexed().getData();
        System.out.println(data);
        Map<String, Object> voidData = data.get(void.class);
        IntSummaryStatistics numA = (IntSummaryStatistics) voidData.get("numA");
        IntSummaryStatistics length = (IntSummaryStatistics) voidData.get("length");
        assertEquals(5, numA.getCount());
        assertEquals(8, numA.getSum());
        assertEquals(1, numA.getMin());
        assertEquals(3, numA.getMax());

        assertEquals(5, length.getCount());
        assertEquals(17, length.getSum());
        assertEquals(3, length.getMin());
        assertEquals(4, length.getMax());
    }

}
