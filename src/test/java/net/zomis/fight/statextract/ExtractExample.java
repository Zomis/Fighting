package net.zomis.fight.statextract;

import org.junit.Test;

import java.util.function.ToIntFunction;

/**
 * Created by Simon on 3/12/2015.
 */
public class ExtractExample {

    public static class Example {
        ToIntFunction<String> length = str -> str.length();
        ToIntFunction<String> numA = str -> (int) str.chars().filter(ch -> ch == 97).count();
    }

    @Test
    public void test() {
        Extractor extractor = Extractor.extractor(new Example());
        extractor.post("test");
        extractor.post("Hello World");
        extractor.post("yet another message");
        extractor.post("a very long message that perhaps contains a bunch of a's");
        ExtractResults data = extractor.collect();
        System.out.println(data.getData());
    }


}
