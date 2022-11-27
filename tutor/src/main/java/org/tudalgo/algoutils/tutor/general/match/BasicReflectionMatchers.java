package org.tudalgo.algoutils.tutor.general.match;

import org.tudalgo.algoutils.tutor.general.reflections.*;

import java.util.List;
import java.util.Objects;

public class BasicReflectionMatchers {

    private BasicReflectionMatchers() {
    }

    public static <T extends WithType> Matcher<T> sameType(TypeLink link) {
        return Matcher.of(l -> Objects.equals(l.type(), link), link);
    }

    /**
     * <p>A matcher matching an object if the object has the same types as given.</p>
     *
     * @param types the types
     * @return the matcher
     */
    public static <T extends WithTypeList> Matcher<T> sameTypes(TypeLink... types) {
        var parameterList = List.of(types);
        return Matcher.of(l -> l.typeList().equals(parameterList));
    }

    /**
     * <p>A matcher matching an object if the object has at least the given modifiers.</p>
     *
     * @param modifiers the modifiers
     * @param <T>       the type of object to match
     * @return the matcher
     */
    public static <T extends WithModifiers> Matcher<T> hasModifiers(Modifier... modifiers) {
        return Matcher.of(l -> {
            for (var modifier : modifiers) {
                if (!modifier.is(l.modifiers())) {
                    return false;
                }
            }
            return true;
        });
    }
}
