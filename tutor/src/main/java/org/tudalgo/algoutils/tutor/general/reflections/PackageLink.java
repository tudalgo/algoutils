package org.tudalgo.algoutils.tutor.general.reflections;

import org.tudalgo.algoutils.tutor.general.match.Matcher;

import java.util.Collection;
import java.util.List;

import static org.tudalgo.algoutils.tutor.general.match.MatchingUtils.firstMatch;
import static org.tudalgo.algoutils.tutor.general.match.MatchingUtils.matches;

public interface PackageLink extends Link, WithName {

    Collection<? extends TypeLink> getTypes();

    default List<? extends TypeLink> getTypes(Matcher<? super TypeLink> matcher) {
        return matches(getTypes(), matcher);
    }

    default TypeLink getType(Matcher<? super TypeLink> matcher) {
        return firstMatch(getTypes(), matcher);
    }

    @Override
    default Kind kind() {
        return Kind.PACKAGE;
    }
}
