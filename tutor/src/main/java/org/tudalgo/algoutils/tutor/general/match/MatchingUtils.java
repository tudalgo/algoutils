package org.tudalgo.algoutils.tutor.general.match;

import java.util.Collection;
import java.util.List;

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
        return objects.stream().map(matcher::match).filter(Match::matched).sorted().map(Match::object).toList();
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
}
