package com.cobeffectiveness;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class CobEffectiveness {
    public static final String MOD_ID = "cobblemon_effectiveness";
    public static final String NAMESPACE = "cobeffectiveness";
    public static final Logger LOGGER = LogManager.getLogger("CobEffectiveness");

    private CobEffectiveness() {}

    public static void initCommon() {
        logInfo("Mod initialized");
    }

    public static void logInfo(String msg) {
        LOGGER.info(addPrefix(msg));
    }

    public static void logInfo(String msg, Object... args) {
        LOGGER.info(addPrefix(msg), args);
    }

    public static void logWarn(String msg) {
        LOGGER.warn(addPrefix(msg));
    }

    public static void logWarn(String msg, Object... args) {
        LOGGER.warn(addPrefix(msg), args);
    }

    public static void logDebug(String msg) {
        LOGGER.debug(addPrefix(msg));
    }

    public static void logError(String msg) {
        LOGGER.error(addPrefix(msg));
    }

    public static void logError(String msg, Object... args) {
        LOGGER.error(addPrefix(msg), args);
    }

    public static void logError(String msg, Throwable t) {
        LOGGER.error(addPrefix(msg), t);
    }

    private static String addPrefix(String msg) {
        return "[CobEffectiveness] ".concat(msg);

    }
}