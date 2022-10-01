package org.tudalgo.algoutils.tutor.general;

import org.tudalgo.algoutils.tutor.general.stringify.Stringifier;
import org.tudalgo.algoutils.tutor.general.assertions.CommentFactory;
import org.tudalgo.algoutils.tutor.general.assertions.Result;

public interface Environment {

    CommentFactory<Result<?, ?, ?, ?>> getCommentFactory();

    Stringifier getStringifier();

    void setStringifier(Stringifier stringifier);
}
