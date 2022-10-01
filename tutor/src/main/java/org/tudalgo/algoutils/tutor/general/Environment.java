package org.tudalgo.algoutils.tutor.general;

import org.tudalgo.algoutils.tutor.general.stringify.Stringifier;
import org.tudalgo.algoutils.tutor.general.test.CommentFactory;
import org.tudalgo.algoutils.tutor.general.test.Result;

public interface Environment {

    CommentFactory<Result<?, ?, ?, ?>> getCommentFactory();

    Stringifier getStringifier();

    void setStringifier(Stringifier stringifier);
}
