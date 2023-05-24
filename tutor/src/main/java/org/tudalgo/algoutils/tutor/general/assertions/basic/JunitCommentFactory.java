package org.tudalgo.algoutils.tutor.general.assertions.basic;

import org.tudalgo.algoutils.student.CrashException;
import org.tudalgo.algoutils.tutor.general.Environment;
import org.tudalgo.algoutils.tutor.general.assertions.CommentFactory;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.assertions.PreCommentSupplier;
import org.tudalgo.algoutils.tutor.general.assertions.Result;

import java.util.stream.Collectors;

import static java.lang.String.format;

/**
 * <p>An implementation of a comment factory for JUnit runs.</p>
 *
 * @author Ruben Deisenroth
 */
public final class JunitCommentFactory implements CommentFactory<Result<?, ?, ?, ?>> {

    private final Environment environment;

    /**
     * <p>Constructs a new comment factory with the given environment.</p>
     *
     * @param environment the environment
     */
    public JunitCommentFactory(Environment environment) {
        this.environment = environment;
    }

    @Override
    public <TS extends Result<?, ?, ?, ?>> String comment(TS result, Context context, PreCommentSupplier<? super TS> commentSupplier) {
        if (result.cause() instanceof CrashException) {
            return "not implemented";
        }
        var stringifier = environment.getStringifier();
        var builder = new StringBuilder();

        // comment
        var comment = commentSupplier != null ? commentSupplier.getPreComment(result) : null;
        if (comment != null) {
            builder.append(comment).append("\n");
        }
        // subject
        var subject = context != null && context.subject() != null ? stringifier.stringify(context.subject()) : null;
        if (subject != null) {
            builder.append(format(" @ %s%n", subject));
        } else {
            builder.append(format("%s = ", "context"));
        }
        // properties
        var properties = context != null ? properties(context, 0) : "{}";


        if (!properties.isEmpty()) {
            builder.append(properties);
        }
        if ((comment != null || subject != null || !properties.isEmpty()) && ((result.expected() != null && result.expected().display()) || (result.actual() != null && result.actual().display()))) {
            builder.append("\n");
        }

        return builder.toString().trim();
    }

    private String properties(Context context, int depth) {
        var stringifier = environment.getStringifier();
        var sb = new StringBuilder();
        sb.append("{\n");
        var indent = " ".repeat(4 * (depth + 1));
        sb.append(context.properties().stream().map(p -> {
            var key = stringifier.stringify(p.key());
            var value = p.value() instanceof Context c ? properties(c, depth + 1) : stringifier.stringify(p.value());
            return format("%s%s = %s", indent, key, value);
        }).collect(Collectors.joining(",\n")));
        sb.append("\n").append(" ".repeat(4 * depth)).append("}");
        return sb.toString();
    }
}



