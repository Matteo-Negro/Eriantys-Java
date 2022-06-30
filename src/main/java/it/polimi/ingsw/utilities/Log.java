package it.polimi.ingsw.utilities;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Main logging class. Writes all the data into a file named "client.log" or "server.log" according to the instance
 * running and appends the logs to the already existing ones.
 */
public class Log {

    private static BufferedWriter writer = null;
    private static DateTimeFormatter dateTimeFormatter;
    private static Level level;

    private Log() {
    }

    /**
     * Creates a new instance for the client.
     *
     * @throws IOException            if an I/O exception occurs while creating the instance.
     * @throws IllegalAccessException if the instance already exists.
     */
    public static synchronized void createClientInstance() throws IOException, IllegalAccessException {
        if (writer != null)
            throw new IllegalAccessException();
        createInstance(false);
    }

    /**
     * Creates a new instance for the server.
     *
     * @throws IOException            if an I/O exception occurs while creating the instance.
     * @throws IllegalAccessException if the instance already exists.
     */
    public static synchronized void createServerInstance() throws IOException, IllegalAccessException {
        if (writer != null)
            throw new IllegalAccessException();
        createInstance(true);
    }

    /**
     * Internal method for generating the instance.
     *
     * @param server true if it's a server instance, false otherwise.
     * @throws IOException if an I/O exception occurs while creating the instance.
     */
    private static synchronized void createInstance(boolean server) throws IOException {

        dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        level = Level.WARNING;

        File file;
        try {
            file = new File(Paths.get(
                    new File(Log.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParent(),
                    String.format("%s - %s.log",
                            DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss").format(LocalDateTime.now()),
                            server ? "server" : "client"
                    )
            ).toString());
        } catch (URISyntaxException e) {
            throw new IOException(e);
        }

        if (!file.exists() && !file.createNewFile())
            throw new IOException();

        writer = Files.newBufferedWriter(
                file.toPath(),
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND);
    }

    /**
     * Returns whether the log can be print or not according to the required severity level.
     *
     * @param level The level of the request.
     * @return true if it can be print.
     */
    private static synchronized boolean checkLevel(Level level) {
        return Log.level.severity < level.severity;
    }

    /**
     * Sets the logging level (default is WARNING).
     *
     * @param level The desired logging level.
     */
    public static synchronized void setLevel(Level level) {
        Log.level = level;
    }

    /**
     * Writes a debug log.
     *
     * @param message The message to print.
     */
    public static synchronized void debug(String message) {
        if (writer == null)
            return;
        if (checkLevel(Level.DEBUG)) {
            try {
                writer.write(format(Level.DEBUG, message));
                writer.flush();
            } catch (IOException ignored) {
            }
        }
    }

    /**
     * Writes an info log.
     *
     * @param message The message to print.
     */
    public static synchronized void info(String message) {
        if (writer == null)
            return;
        if (checkLevel(Level.INFO)) {
            try {
                writer.write(format(Level.INFO, message));
                writer.flush();
            } catch (IOException ignored) {
            }
        }
    }

    /**
     * Writes a warning log.
     *
     * @param message   The programmer's message to print.
     * @param exception The corresponding exception to print.
     */
    public static void warning(String message, Exception exception) {
        printWarning(String.format("%s: %s%n%s", message, exception.getMessage(), getStackTrace(exception.getStackTrace())));
    }

    /**
     * Writes a debug log.
     *
     * @param exception The exception to print.
     */
    public static void warning(Exception exception) {
        printWarning(String.format("%s%n%s", exception.getMessage(), getStackTrace(exception.getStackTrace())));
    }

    /**
     * Writes a debug log.
     *
     * @param message The message to print.
     */
    public static void warning(String message) {
        printWarning(message);
    }

    /**
     * Actually writes the generated warning log.
     *
     * @param message The generated message to print.
     */
    private static synchronized void printWarning(String message) {
        if (writer == null)
            return;
        if (checkLevel(Level.WARNING)) {
            try {
                writer.write(format(Level.WARNING, message));
                writer.flush();
            } catch (IOException ignored) {
            }
        }
    }

    /**
     * Writes an error log.
     *
     * @param message   The programmer's message to print.
     * @param exception The corresponding exception to print.
     */
    public static void error(String message, Exception exception) {
        printError(String.format("%s: %s%n%s", message, exception.getMessage(), getStackTrace(exception.getStackTrace())));
    }

    /**
     * Writes an error log.
     *
     * @param exception The exception to print.
     */
    public static void error(Exception exception) {
        printError(String.format("%s%n%s", exception.getMessage(), getStackTrace(exception.getStackTrace())));
    }

    /**
     * Writes an error log.
     *
     * @param message The message to print.
     */
    public static void error(String message) {
        printError(message);
    }

    /**
     * Actually writes the generated error log.
     *
     * @param message The generated message to print.
     */
    private static synchronized void printError(String message) {
        if (writer == null)
            return;
        if (checkLevel(Level.ERROR)) {
            try {
                writer.write(format(Level.ERROR, message));
                writer.flush();
            } catch (IOException ignored) {
            }
        }
    }

    /**
     * Returns a string representation of the stack trace.
     *
     * @param stackTrace The stack trace.
     * @return The generated string.
     */
    private static String getStackTrace(StackTraceElement[] stackTrace) {
        StringBuilder trace = new StringBuilder();
        for (StackTraceElement element : stackTrace)
            trace.append(String.format("    %s%n", element.toString()));
        return trace.substring(0, trace.toString().length() - 1);
    }

    /**
     * Formats the log in order to have always the same aspect.
     *
     * @param level   The severity of the message.
     * @param message The message to print.
     * @return The generated string to write.
     */
    private static synchronized String format(Level level, String message) {
        return String.format("%s | %s | %s%n", dateTimeFormatter.format(LocalDateTime.now()), level.name(), message);
    }

    /**
     * Enumeration with all the possibile log levels (or severities).
     */
    public enum Level {

        /**
         * Debug log level.
         */
        DEBUG(0),

        /**
         * Info log level.
         */
        INFO(1),

        /**
         * Warning log level.
         */
        WARNING(2),

        /**
         * Error log level.
         */
        ERROR(3);

        /**
         * The corresponding severity.
         */
        final int severity;

        /**
         * Internal constructor. It assigns the corresponding severity level to each level.
         *
         * @param severity
         */
        Level(int severity) {
            this.severity = severity;
        }
    }
}
