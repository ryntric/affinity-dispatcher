package io.github.ryntric;

final class Utils {
    private static final Runtime RUNTIME = Runtime.getRuntime();
    private static final Integer AVAILABLE_PROCESSORS = RUNTIME.availableProcessors();

    private Utils() {
    }

    public static int getProcessorCount() {
        return AVAILABLE_PROCESSORS > 1 ? AVAILABLE_PROCESSORS - 1 : AVAILABLE_PROCESSORS;
    }

    public static int getAvailableProcessorsCount() {
        return AVAILABLE_PROCESSORS;
    }

}
