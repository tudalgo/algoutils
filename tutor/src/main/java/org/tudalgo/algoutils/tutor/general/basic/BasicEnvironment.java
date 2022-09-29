package org.tudalgo.algoutils.tutor.general.basic;

import org.tudalgo.algoutils.tutor.general.Environment;
import org.tudalgo.algoutils.tutor.general.stringify.Stringifier;
import org.tudalgo.algoutils.tutor.general.stringify.basic.ReflectionStringifier;
import org.tudalgo.algoutils.tutor.general.test.CommentFactory;
import org.tudalgo.algoutils.tutor.general.test.Result;
import org.tudalgo.algoutils.tutor.general.test.basic.BasicCommentFactory;

import java.util.Objects;

public class BasicEnvironment implements Environment {

    private final CommentFactory<Result<?, ?>> commentFactory = new BasicCommentFactory(this);

    private Stringifier stringifier = ReflectionStringifier.getInstance().orElse(Objects::toString);

    @Override
    public CommentFactory<Result<?, ?>> getCommentFactory() {
        return commentFactory;
    }

    @Override
    public void setStringifier(Stringifier stringifier) {
        Objects.requireNonNull(stringifier, "stringifier must not be null");
        this.stringifier = stringifier;
    }

    @Override
    public Stringifier getStringifier() {
        return stringifier;
    }
}
