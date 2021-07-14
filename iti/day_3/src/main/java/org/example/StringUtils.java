package org.example;

public class StringUtils {
    public static String betterString(String string1, String string2, BiTest<String> biTest) {
        if (biTest.test(string1, string2))
            return string1;
        else return string2;
    }

    public static boolean testString(String string, SingleTest<String> test) {
        return test.test(string);
    }
}
