package atlanteshellsing.aegis.logging;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.*;

public class AEGISLogger {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final java.util.logging.Logger AEGIS_LOGGER = Logger.getLogger("AEGIS LOG");

    public enum LogColor {
        CYAN("\u001B[36m"),
        GREEN("\u001B[32m"),
        YELLOW("\u001B[33m"),
        RED("\u001B[31m"),
        RESET("\u001B[0m");

        final String code;

        LogColor(String code) {
            this.code = code;
        }
    }

    public enum AEGISLogKey {
        AEGIS_MAIN, AEGIS_TOOL
    }

    public enum AEGISLogLevel {
        FINE(Level.FINE, LogColor.CYAN), INFO(Level.INFO, LogColor.GREEN), WARNING(Level.WARNING, LogColor.YELLOW), SEVERE(Level.SEVERE, LogColor.RED);

        final LogColor color;
        final Level level;

        AEGISLogLevel(Level level, LogColor color) {
            this.level = level;
            this.color = color;
        }
    }

    static {
        AEGIS_LOGGER.setUseParentHandlers(false);

        ConsoleHandler handler = new ConsoleHandler();
        handler.setFormatter(new SimpleFormatter() {
            @Override
            public synchronized String format(LogRecord logRec) {
                StringBuilder builder = new StringBuilder();

                String timestamp = LocalDateTime.now().format(FORMATTER);
                AEGISLogKey key = AEGISLogKey.AEGIS_MAIN;
                AEGISLogLevel aegisLevel = null;

                Object[] params = logRec.getParameters();
                if (params != null) {
                    if (params.length > 0 && params[0] instanceof AEGISLogKey logKey) {
                        key = logKey;
                    }
                    if (params.length > 1 && params[1] instanceof AEGISLogLevel logLevel) {
                        aegisLevel = logLevel;
                    }
                }

                String color = aegisLevel != null ? aegisLevel.color.code : LogColor.RESET.code;

                builder.append(String.format("%s[%s] [%s] [%s] %s%s%n",
                        color,
                        timestamp,
                        key.name(),
                        logRec.getLevel().getName(),
                        logRec.getMessage(),
                        LogColor.RESET.code
                ));

                //print throwables
                if(logRec.getThrown() != null) {
                    Throwable thrown = logRec.getThrown();
                    printThrowable(builder, thrown);
                }

                return builder.toString();
            }

            private void printThrowable(StringBuilder builder, Throwable thrown) {

                  String grey = "\u001B[90m";
                  String red = "\u001B[31m";

                // Print Exception Header In Red
                builder.append(red)
                        .append("Caused by: ")
                        .append(thrown.toString())
                        .append(LogColor.RESET.code)
                        .append(System.lineSeparator());

                // Print Stack Trace In Grey
                for(StackTraceElement element : thrown.getStackTrace()) {
                    builder.append(grey)
                            .append("\tat ")
                            .append(element.toString())
                            .append(LogColor.RESET.code)
                            .append(System.lineSeparator());
                }

                //Handle Nested Exceptions
                Throwable cause = thrown.getCause();
                if(cause != null)
                    printThrowable(builder, cause);
            }
        });

        handler.setLevel(Level.ALL);
        AEGIS_LOGGER.addHandler(handler);
        AEGIS_LOGGER.setLevel(Level.ALL);
    }

    public static void log(AEGISLogKey key, AEGISLogLevel level, String message) {
        LogRecord logRec = new LogRecord(level.level, message);
        logRec.setParameters(new Object[]{key, level});
        AEGIS_LOGGER.log(logRec);
    }

    public static void log(AEGISLogKey key, AEGISLogLevel level, String message, Exception e) {
        LogRecord logRec = new LogRecord(level.level, message);
        logRec.setParameters(new Object[]{key, level});
        logRec.setThrown(e);
        AEGIS_LOGGER.log(logRec);
    }
}
