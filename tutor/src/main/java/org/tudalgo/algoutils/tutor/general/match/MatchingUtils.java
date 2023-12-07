package org.tudalgo.algoutils.tutor.general.match;

import java.util.Collection;
import java.util.List;

import static java.util.Comparator.reverseOrder;

/**
 * Utility class for matching objects.
 */
public class MatchingUtils {

    /**
     * <p>Returns a list of all objects in the given collection matched by the given matcher sorted by match scores in descending order.</p>
     *
     * @param objects the collection of objects
     * @param matcher the matcher
     * @param <T>     the type of objects
     * @return the list of matched objects
     */
    public static <T> List<T> matches(Collection<T> objects, Matcher<? super T> matcher) {
        return objects.stream().map(matcher::match).filter(Match::matched).sorted(reverseOrder()).map(Match::object).toList();
    }

    /**
     * <p>Returns the object in the given collection matched by the given matcher with the highest match score.</p>
     *
     * <p>If there is not such an object, null is returned.</p>
     *
     * @param objects the collection of objects
     * @param matcher the matcher
     * @param <T>     the type of objects
     * @return the matched object or null
     */
    public static <T> T firstMatch(Collection<T> objects, Matcher<? super T> matcher) {
        return matches(objects, matcher).stream().findFirst().orElse(null);
    }


    /**
     * Calculates the similarity (a number within 0 and 1) between two strings.
     *
     * @param s1 the first string used for the calculation of the similarity
     * @param s2 the second string used for the calculation  of the similarity
     * @return the calculated similarity (a number within 0 and 1) between two strings.
     * @author Ruben Deisenroth
     * @see <a href="http://rosettacode.org/wiki/Levenshtein_distance#Java">Levenshtein distance -
     * Java</a>
     */
    public static int editDistance(String s1, String s2) {
        s1 = s1.toLowerCase();
        s2 = s2.toLowerCase();

        final int[] costs = new int[s2.length() + 1];
        for (int i = 0; i <= s1.length(); i++) {
            int lastValue = i;
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0) {
                    costs[j] = j;
                } else {
                    if (j > 0) {
                        int newValue = costs[j - 1];
                        if (s1.charAt(i - 1) != s2.charAt(j - 1)) {
                            newValue = Math.min(Math.min(newValue, lastValue), costs[j]) + 1;
                        }
                        costs[j - 1] = lastValue;
                        lastValue = newValue;
                    }
                }
            }
            if (i > 0) {
                costs[s2.length()] = lastValue;
            }
        }
        return costs[s2.length()];
    }

    /**
     * Calculates the similarity (a number within 0 and 1) between two strings.
     *
     * @param s1 the first string used for the calculation of the similarity
     * @param s2 the second string used for the calculation  of the similarity
     * @return the similarity
     * @author Ruben Deisenroth
     */
    public static double similarity(final String s1, final String s2) {
        String longer = s1;
        String shorter = s2;
        if (s1.length() < s2.length()) {
            longer = s2;
            shorter = s1;
        }
        final int longerLength = longer.length();
        if (longerLength == 0) {
            return 1.0;
            /* both strings are zero length */
        }
        /*
         * // If you have Apache Commons Text, you can use it to calculate the edit
         * distance: LevenshteinDistance levenshteinDistance = new
         * LevenshteinDistance(); return (longerLength -
         * levenshteinDistance.apply(longer, shorter)) / (double) longerLength;
         */
        return (longerLength - editDistance(longer, shorter)) / (double) longerLength;
    }
}
