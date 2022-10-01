package org.tudalgo.algoutils.tutor.general.test.basic;

import org.tudalgo.algoutils.tutor.general.Environment;
import org.tudalgo.algoutils.tutor.general.test.CommentFactory;
import org.tudalgo.algoutils.tutor.general.test.Context;
import org.tudalgo.algoutils.tutor.general.test.PreCommentSupplier;
import org.tudalgo.algoutils.tutor.general.test.Result;

import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

public final class BasicCommentFactory implements CommentFactory<Result<?, ?, ?, ?>> {

    private final Environment environment;

    public BasicCommentFactory(Environment environment) {
        this.environment = environment;
    }

    @Override
    public <TS extends Result<?, ?, ?, ?>> String comment(TS result, Context context, PreCommentSupplier<? super TS> commentSupplier) {
        var expected = result.expected();
        var actual = result.actual();
        var cause = result.cause();

        var stringifier = environment.getStringifier();
        // comment
        var comment = commentSupplier != null ? commentSupplier.getPreComment(result) : null;
        // subject
        var subject = context != null && context.subject() != null ? stringifier.stringify(context.subject()) : null;
        // properties
        var properties = context != null ? context.properties().stream().map(p -> {
            var key = stringifier.stringify(p.key());
            var value = stringifier.stringify(p.value());
            return String.format("%s = %s", key, value);
        }).filter(Objects::nonNull).collect(Collectors.joining(",")) : "";
        // trace
        var trace = (String) null;
        if (cause != null) {
            trace = stream(cause.getStackTrace()).limit(10).map(e -> "\t" + e).collect(Collectors.joining("\n"));
        }

        var sb = new StringBuilder();
        if (comment != null) {
            sb.append(comment);
        }
        if (subject != null) {
            sb.append(" @ ");
            sb.append(subject);
        }
        if (!properties.isEmpty()) {
            sb.append(" ");
            sb.append(String.format("{ %s }", properties));
        }
        if ((comment != null || subject != null || !properties.isEmpty()) && (expected != null || actual != null)) {
            sb.append(":\n");
        }
        if (expected != null) {
            sb.append("\t");
            sb.append("expected: ");
            var object = expected.string(stringifier);
            sb.append(String.format("%s", object));
            sb.append("\n");
        }
        System.out.println("actual: " + actual);
        if (actual != null) {

            sb.append("\t");
            sb.append(String.format("actual: %s", actual.string(stringifier)));
            sb.append("\n");
        }
        if (trace != null) {
            sb.append("\n");
            sb.append(trace);
        }
        return sb.toString().trim();
    }
}



