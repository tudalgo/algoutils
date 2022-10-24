package org.tudalgo.algoutils.tutor.general.assertions.expected;

import org.tudalgo.algoutils.tutor.general.assertions.actual.Actual;
import org.tudalgo.algoutils.tutor.general.stringify.Stringifier;

import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.joining;

/**
 * <p>A type representing no expected behavior.</p>
 *
 * @author Dustin Glaser
 */
public final class Nothing implements Actual, Expected {

    private static final Nothing FAILED = new Nothing(null, false);
    private static final Nothing SUCCESSFUL = new Nothing(null, true);


    private final Object behavior;
    private final boolean successful;

    // no instantiation allowed
    private Nothing(Object behavior, boolean successful) {
        this.behavior = behavior;
        this.successful = successful;
    }

    @Override
    public Object behavior() {
        return behavior;
    }

    @Override
    public boolean successful() {
        return successful;
    }

    @Override
    public boolean display() {
        return behavior != null;
    }

    @Override
    public String string(Stringifier stringifier) {
        return Actual.super.string(stringifier);
    }

    public static Nothing nothing() {
        return FAILED;
    }

    public static Nothing text() {
        return FAILED;
    }

    public static Nothing text(Object text) {
        return new Nothing(text, false);
    }

    public static Nothing items(
        String prefixSingular,
        String prefixPlural,
        List<?> items,
        String suffixSingular,
        String suffixPlural
    ) {
        if (items.size() == 0) {
            return new Nothing("no " + prefixPlural + " " + suffixPlural, false);
        } else if (items.size() == 1) {
            var item = Objects.toString(items.get(0));
            return new Nothing(prefixSingular + " " + item + " " + suffixSingular, false);
        }
        var firstItems = items.stream().limit(items.size() - 1).map(Objects::toString).collect(joining(", "));
        var lastItem = Objects.toString(items.get(items.size() - 1));
        return new Nothing(prefixPlural + " " + firstItems + " and " + lastItem + " " + suffixPlural, false);
    }

    public static Nothing items(
        String prefixSingular,
        String prefixPlural,
        List<?> items
    ) {
        return items(prefixSingular, prefixPlural, items, "", "");
    }

    public static Nothing items(
        List<?> items,
        String suffixSingular,
        String suffixPlural
    ) {
        return items("", "", items, suffixSingular, suffixPlural);
    }

    public static Nothing successBehavior() {
        return SUCCESSFUL;
    }

    public static Nothing successBehavior(Object behavior) {
        return new Nothing(behavior, true);
    }
}
