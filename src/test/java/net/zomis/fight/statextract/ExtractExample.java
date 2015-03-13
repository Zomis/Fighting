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
        ToIntFunction<Character> charValue = ch -> ch.charValue();
    }

    @Test
    public void test() {
        Extractor extractor = Extractor.extractor(new Example());
        extractor.addPreHandler(String.class, (extr, str) -> str.chars().mapToObj(i -> (char) i).forEach(ch -> extr.post(ch)));
        extractor.post("test");
        extractor.post("Hello World");
        extractor.post("yet another message");
        extractor.post("a very long message that perhaps contains a bunch of a's");
        ExtractResults data = extractor.collect();
        System.out.println(data.getData());
    }


}
