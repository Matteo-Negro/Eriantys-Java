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

    public static void createClientInstance() throws IOException, IllegalAccessException {
        if (writer != null)
            throw new IllegalAccessException();
        createInstance(false);
    }

    public static void createServerInstance() throws IOException, IllegalAccessException {
        if (writer != null)
            throw new IllegalAccessException();
        createInstance(true);
    }

    private static void createInstance(boolean server) throws IOException {
        File file = new File(Paths.get(new File(Log.class.getProtectionDomain().getCodeSource().getLocation().getFile())
                .getParent(), server ? "server.log" : "client.log").toString());
        if (!file.exists() && !file.createNewFile())
            throw new IOException();
        writer = Files.newBufferedWriter(file.toPath(), StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
        dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        level = Level.WARNING;
    }

    public static void setLevel(int level) {
        Log.level = level;
    }

    public static void debug(String message) {
        if (level <= Level.DEBUG) {
            try {
                writer.write(format("DEBUG", message));
                writer.flush();
            } catch (IOException ignored) {
            }
        }
    }

    public static void info(String message) {
        if (level <= Level.INFO) {
            try {
                writer.write(format("INFO", message));
                writer.flush();
            } catch (IOException ignored) {
            }
        }
    }

    public static void warning(String message) {
        if (level <= Level.WARNING) {
            try {
                writer.write(format("WARNING", message));
                writer.flush();
            } catch (IOException ignored) {
            }
        }
    }

    public static void error(String message) {
        if (level <= Level.ERROR) {
            try {
                writer.write(format("ERROR", message));
                writer.flush();
            } catch (IOException ignored) {
            }
        }
    }

    private static String format(String type, String message) {
        return dateTimeFormatter.format(LocalDateTime.now()) +
                " | " +
                type +
                " | " +
                message.replace("|", "-") +
                "\n";
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
