package org.tudalgo.algoutils.tutor.general.basic;

import org.tudalgo.algoutils.tutor.general.Environment;
import org.tudalgo.algoutils.tutor.general.assertions.CommentFactory;
import org.tudalgo.algoutils.tutor.general.assertions.Result;
import org.tudalgo.algoutils.tutor.general.assertions.basic.BasicCommentFactory;
import org.tudalgo.algoutils.tutor.general.stringify.Stringifier;
import org.tudalgo.algoutils.tutor.general.stringify.basic.ReflectionStringifier;

import java.util.Objects;

public class BasicEnvironment implements Environment {

    private final CommentFactory<Result<?, ?, ?, ?>> commentFactory = new BasicCommentFactory(this);

    private Stringifier stringifier = ReflectionStringifier.getInstance();

    @Override
    public CommentFactory<Result<?, ?, ?, ?>> getCommentFactory() {
        return commentFactory;
    }

    private static final BasicEnvironment INSTANCE = new BasicEnvironment();

    private BasicEnvironment() {
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

    public static final BasicEnvironment getInstance() {
        return INSTANCE;
    }
}
