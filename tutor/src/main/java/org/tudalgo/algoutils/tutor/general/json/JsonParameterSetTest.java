package org.tudalgo.algoutils.tutor.general.json;

import org.junit.jupiter.params.provider.ArgumentsSource;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A parameterized test that uses a JSON file as input and apply custom converters to the JSON nodes.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ArgumentsSource(JsonArgumentSetProvider.class)
public @interface JsonParameterSetTest {

    /**
     * The path to the JSON file containing the test data.
     * <p>it is recommended to replicate the java path structure in the resources.For example let's assume the
     * following structure:</p>
     * <pre>
     *     src/graderPrivate/java/h13/controller/scene/game/GameControllerTest.java
     *     src/graderPrivate/resources/h13/controller/scene/game/GameControllerDummyState.json
     * </pre>
     * <p>Then a test case could look like this:</p>
     * <pre>{@code
     *     @ParameterizedTest
     *     @JsonParameterSetTest(value = "GameControllerDummyState.json", customConverters = "customConverters")
     *     public void testHandleKeyboardInputsEscapeLose(final JsonParameterSet params)
     *     throws InterruptedException, IOException {
     *         testHandleKeyboardInputsEscape(params, true);
     *     }
     * }</pre>
     *
     * @return the path to the JSON file containing the test data.
     */
    String value();

    /**
     * The name of the field in the test class that contains a map of custom converters.
     * <p>For example let's assume we have a Field called customConverters in the test class:</p>
     * <pre>{@code
     *     @SuppressWarnings("unused")
     *     public final static Map<String, Function<JsonNode, ?>> customConverters = Map.ofEntries(
     *         Map.entry("GAME_BOUNDS", JsonConverter::toBounds),
     *         Map.entry("enemies", JsonConverter::toIDEnemyList),
     *         Map.entry("bullets", JsonConverter::toIDBulletList),
     *         Map.entry("player", JsonConverter::toIDPlayer),
     *         Map.entry("bulletOwners", JsonConverter::toIntMap),
     *         Map.entry("hits", JsonConverter::toIntMap),
     *         Map.entry("damaged", jsonNode -> JsonConverter.toMap(jsonNode, Integer::parseInt, JsonNode::asBoolean))
     *     );
     *    }</pre>
     * <p>Then a test case could look like this:</p>
     * <pre>{@code
     *        @ParameterizedTest
     *        @JsonParameterSetTest(value = "GameControllerDummyState.json", customConverters = "customConverters")
     *        public void testHandleKeyboardInputsEscapeLose(final JsonParameterSet params)
     *        throws InterruptedException, IOException {
     *            testHandleKeyboardInputsEscape(params, true);
     *        }
     *    }</pre>
     *
     * @return the name of the field in the test class that contains a map of custom converters.
     */
    String customConverters() default "";

}
