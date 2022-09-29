package org.tudalgo.algoutils.tutor.general.test.basic;

import org.tudalgo.algoutils.tutor.general.Environment;
import org.tudalgo.algoutils.tutor.general.test.CommentFactory;
import org.tudalgo.algoutils.tutor.general.test.Context;
import org.tudalgo.algoutils.tutor.general.test.PreCommentSupplier;
import org.tudalgo.algoutils.tutor.general.test.Result;

import java.util.Collections;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.tudalgo.algoutils.tutor.general.Utils.none;

public final class BasicCommentFactory implements CommentFactory<Result<?, ?>> {

    private final Environment environment;

    public BasicCommentFactory(Environment environment) {
        this.environment = environment;
    }

    @Override
    public <TS extends Result<?, ?>> String comment(TS result, Context context, PreCommentSupplier<? super TS> preCommentSupplier) {
        var preComment = preCommentSupplier != null ? preCommentSupplier.getPreComment(result) : null;
        var subjectString = context != null && context.subject() != null ? environment.getStringifier().stringify(context.subject()) : null;
        var properties = context != null ? context.properties().stream().map(p -> {
            var string = environment.getStringifier().stringify(p.value());
            return string != null ? String.format("%s = %s", p.key(), string) : null;
        }).filter(Objects::nonNull).collect(Collectors.toList()) : Collections.<String>emptyList();
        var propertyString = properties.isEmpty() ? null : String.join(", ", properties);
        var expectationString = result.expected() != none() ? environment.getStringifier().stringify(result.expected()) : null;
        var actualString = result.expected() != none() ? environment.getStringifier().stringify(result.actual()) : null;
        var sb = new StringBuilder();
        if (preComment != null) {
            sb.append(preComment);
        }
        if (subjectString != null) {
            sb.append(" @ ");
            sb.append(subjectString);
        }
        if (!properties.isEmpty()) {
            sb.append(" ");
            sb.append(String.format("{ %s }", propertyString));
        }
        if ((preComment != null || subjectString != null || !properties.isEmpty()) && (expectationString != null || actualString != null)) {
            sb.append(":");
        }
        if (expectationString != null) {
            sb.append(" ");
            sb.append(String.format("expected: <%s>", expectationString));
        }
        if (actualString != null) {
            sb.append(" ");
            sb.append(String.format("actual: <%s>", actualString));
        }
        return sb.toString().trim();
    }
}



