package org.tudalgo.algoutils.tutor.general.reflections;

import org.junit.jupiter.api.Test;
import org.tudalgo.algoutils.tutor.general.match.BasicStringMatchers;
import org.tudalgo.algoutils.tutor.general.match.MatcherFactories;

import static org.tudalgo.algoutils.tutor.general.assertions.Assertions3.assertTypeExists;

public class BasicTypeLinkTest {

    @Test
    void testImportsAll() throws Exception {
        var p = BasicPackageLink.of("org.tudalgo.algoutils.tutor.general.reflections");
        MatcherFactories.StringMatcherFactory matcher = BasicStringMatchers::identical;
        var clazz = assertTypeExists(
            p,
            matcher.matcher(BasicTypeLink.class.getSimpleName())
        );
        clazz.imports(true).stream().map(WithName::name).forEach(System.out::println);
    }

}
