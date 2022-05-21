package it.polimi.ingsw.utilities;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Log {

    private static BufferedWriter writer = null;
    private static DateTimeFormatter dateTimeFormatter;
    private static int level;

    private Log() {
    }

    public static synchronized void createClientInstance() throws IOException, IllegalAccessException {
        if (writer != null)
            throw new IllegalAccessException();
        createInstance(false);
    }

    public static synchronized void createServerInstance() throws IOException, IllegalAccessException {
        if (writer != null)
            throw new IllegalAccessException();
        createInstance(true);
    }

    private static synchronized void createInstance(boolean server) throws IOException {
        dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        level = Level.WARNING;
        File file = new File(Paths.get(
                new File(Log.class.getProtectionDomain().getCodeSource().getLocation().getFile()).getParent(),
                (server ? "server" : "client") + ".log"
        ).toString());
        if (!file.exists() && !file.createNewFile())
            throw new IOException();
        writer = Files.newBufferedWriter(
                file.toPath(),
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING,
                StandardOpenOption.WRITE);
    }

    public static synchronized void setLevel(int level) {
        Log.level = level;
    }

    public static synchronized void debug(String message) {
        if (writer == null)
            return;
        if (level <= Level.DEBUG) {
            try {
                writer.write(format("DEBUG", message));
                writer.flush();
            } catch (IOException ignored) {
            }
        }
    }

    public static synchronized void info(String message) {
        if (writer == null)
            return;
        if (level <= Level.INFO) {
            try {
                writer.write(format("INFO", message));
                writer.flush();
            } catch (IOException ignored) {
            }
        }
    }

    public static synchronized void warning(String message) {
        if (writer == null)
            return;
        if (level <= Level.WARNING) {
            try {
                writer.write(format("WARNING", message));
                writer.flush();
            } catch (IOException ignored) {
            }
        }
    }

    public static void error(String message, Exception exception) {
        printError(message + exception.getMessage() + "\n" + getStackTrace(exception.getStackTrace()));
    }

    public static void error(Exception exception) {
        printError(exception.getMessage() + "\n" + getStackTrace(exception.getStackTrace()));
    }

    public static void error(String message) {
        printError(message);
    }

    private static synchronized void printError(String message) {
        if (writer == null)
            return;
        if (level <= Level.ERROR) {
            try {
                writer.write(format("ERROR", message));
                writer.flush();
            } catch (IOException ignored) {
            }
        }
    }

    private static String getStackTrace(StackTraceElement[] stackTrace) {
        StringBuilder trace = new StringBuilder();
        for (StackTraceElement element : stackTrace)
            trace.append("    " + element.toString() + "\n");
        return trace.toString();
    }

    private static synchronized String format(String type, String message) {
        return dateTimeFormatter.format(LocalDateTime.now()) + " | " + type + " | " + message + "\n";
    }

    public static class Level {
        public static final int DEBUG = 0;
        public static final int INFO = 1;
        public static final int WARNING = 2;
        public static final int ERROR = 3;

        private Level() {
        }
    }
}
