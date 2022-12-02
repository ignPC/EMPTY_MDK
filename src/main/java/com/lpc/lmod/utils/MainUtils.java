package com.lpc.lmod.utils;

public class MainUtils {
    /**
     * Utilities class.
     */
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static String toBlack(String string){
        string = ANSI_BLACK + "\nLMOD: "  + string + ANSI_RESET;
        return string;
    }

    public static String toResponse(String string){
        string = ANSI_PURPLE + "\nRESPONSE: "  + string + ANSI_RESET;
        return string;
    }

    public static String toRed(String string){
        string = ANSI_RED + string + ANSI_RESET;
        return string;
    }


    public static class StringSimilarity {
        /**
         * Calculates the similarity (a number within 0 and 1) between two strings.
         */
        public static double similarity(String s1, String s2) {
            String longer = s1, shorter = s2;
            if (s1.length() < s2.length()) {
                longer = s2;
                shorter = s1;
            }
            int longerLength = longer.length();
            if (longerLength == 0) {
                return 1.0;
            }
            return (longerLength - editDistance(longer, shorter)) / (double) longerLength;
        }

        public static int editDistance(String s1, String s2) {
            s1 = s1.toLowerCase();
            s2 = s2.toLowerCase();

            int[] costs = new int[s2.length() + 1];
            for (int i = 0; i <= s1.length(); i++) {
                int lastValue = i;
                for (int j = 0; j <= s2.length(); j++) {
                    if (i == 0)
                        costs[j] = j;
                    else {
                        if (j > 0) {
                            int newValue = costs[j - 1];
                            if (s1.charAt(i - 1) != s2.charAt(j - 1))
                                newValue = Math.min(Math.min(newValue, lastValue),
                                        costs[j]) + 1;
                            costs[j - 1] = lastValue;
                            lastValue = newValue;
                        }
                    }
                }
                if (i > 0)
                    costs[s2.length()] = lastValue;
            }
            return costs[s2.length()];
        }
    }
}
