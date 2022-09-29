package org.tudalgo.algoutils.tutor.general;

import org.tudalgo.algoutils.tutor.general.stringify.Stringifier;
import org.tudalgo.algoutils.tutor.general.test.CommentFactory;
import org.tudalgo.algoutils.tutor.general.test.Result;
import org.tudalgo.algoutils.tutor.general.test.Test;

public interface Environment {

    CommentFactory<Result<?, ?>> getCommentFactory();

    void setStringifier(Stringifier stringifier);

    Stringifier getStringifier();
}
