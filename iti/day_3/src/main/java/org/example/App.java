package org.example;

import java.io.IOException;

public class App
{
    public static void main(String[] args) throws IOException {

        System.out.println("press any key to print longer and first string (Said, Gamal)");
        System.in.read();
        String string1 = "Said";
        String string2 = "Gamal";
        String longer = StringUtils.betterString(string1, string2, (s1, s2) -> s1.length() > s2.length());
        String first = StringUtils.betterString(string1, string2, (s1, s2) -> true);
        System.out.println("longer: " + longer);
        System.out.println("first: " + first);
        System.out.println("*******************************************************************************************");

        System.out.println("press any key to check string to have letters only");
        System.in.read();
        String alpha = "Abcd";
        String alphaNum = "A1b2c3";
        SingleTest<String> isAlphaOnly = (str) -> str.chars().allMatch(Character::isLetter);
        boolean isLetter1 = StringUtils.testString(alpha, isAlphaOnly);
        boolean isLetter2 = StringUtils.testString(alphaNum, isAlphaOnly);
        System.out.println(alpha + "->isAlphaOnly: " + isLetter1);
        System.out.println(alphaNum + "->isAlphaOnly: " + isLetter2);
    }
}
