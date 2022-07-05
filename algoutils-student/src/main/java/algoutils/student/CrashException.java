package algoutils.student;

/**
 * An Exception that is intended to be thrown by default when a method is called that is not implemented.
 *
 * @author Ruben Deisenroth
 */
public class CrashException extends RuntimeException {
    /**
     * Constructs a new {@code CrashException} with the specified detail message.
     *
     * @param message the detail message.
     */
    public CrashException(String message) {
        super(message);
    }
}
