package net.zomis.fight.statextract;

import org.junit.Test;

import java.util.function.Function;
import java.util.function.ToIntFunction;

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

    public static class Example {
        ToIntFunction<String> length = str -> str.length();
        ToIntFunction<String> numA = str -> (int) str.chars().filter(ch -> ch == 97).count();
        ToIntFunction<Character> charValue = ch -> ch.charValue();
    }

    @Test
    public void indexExample() {
        Extractor extractor = Extractor.extractor(new IndexExample());
        extractor.post("teat");
        extractor.post("a_a_");
        extractor.post("tca");
        extractor.post("abc");
        extractor.post("aaa");
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

        ExtractResults data = extractor.collect();
        System.out.println(data.getData());
    }

}
