package org.tudalgo.algoutils.tutor.general.assertions.basic;

import org.tudalgo.algoutils.tutor.general.Environment;
import org.tudalgo.algoutils.tutor.general.assertions.CommentFactory;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.assertions.PreCommentSupplier;
import org.tudalgo.algoutils.tutor.general.assertions.Result;

import java.util.Objects;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.util.Arrays.stream;

/**
 * <p>A basic implementation of a comment factory.</p>
 *
 * @author Dustin Glaser
 */
@SuppressWarnings("ClassCanBeRecord")
public final class BasicCommentFactory implements CommentFactory<Result<?, ?, ?, ?>> {

    private static final int TRACE_LINES = 3;

    private final Environment environment;

    /**
     * <p>Constructs a new comment factory with the given environment.</p>
     *
     * @param environment the environment
     */
    public BasicCommentFactory(Environment environment) {
        this.environment = environment;
    }

    @Override
    public <TS extends Result<?, ?, ?, ?>> String comment(TS result, Context context, PreCommentSupplier<? super TS> commentSupplier) {
        var stringifier = environment.getStringifier();
        var builder = new StringBuilder();
        var trace = result.cause() != null ? stream(result.cause().getStackTrace()).limit(TRACE_LINES).map(e -> "\t" + e).collect(Collectors.joining("\n")) : null;

        // comment
        var comment = commentSupplier != null ? commentSupplier.getPreComment(result) : null;
        if (comment != null) {
            builder.append(comment);
        }

        // subject
        var subject = context != null && context.subject() != null ? stringifier.stringify(context.subject()) : null;
        if (subject != null) {
            builder.append(" @ ");
            builder.append(subject);
        }
        // properties
        var properties = context != null ? context.properties().stream().map(p -> {
            var key = stringifier.stringify(p.key());
            var value = stringifier.stringify(p.value());
            return format("%s = %s", key, value);
        }).filter(Objects::nonNull).collect(Collectors.joining(",")) : "";
        if (!properties.isEmpty()) {
            builder.append(" ");
            builder.append(format("{ %s }", properties));
        }
        if ((comment != null || subject != null || !properties.isEmpty()) && (result.expected().display() || result.actual().display())) {
            builder.append(":\n");
        }
        // expected
        if (result.expected().display()) {
            builder.append("\t");
            builder.append(format("expected: %s", result.expected().string(stringifier)));
            builder.append("\n");
        }
        // actual
        if (result.actual().display()) {
            builder.append("\t");
            builder.append(format("actual: %s", result.actual().string(stringifier)));
            builder.append("\n");
        }
        // trace
        if (trace != null) {
            builder.append("\n");
            builder.append(trace);
        }
        return builder.toString().trim();
    }
}



