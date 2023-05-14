package com.kolosya.zavodsimulator.factory;

import com.kolosya.zavodsimulator.Shutdownable;

import java.io.IOException;
import java.util.logging.*;

public class Debug implements Shutdownable {
    private static Debug instance;
    private final Logger logger = Logger.getLogger("com.kolosya.zavodsimulator.factory");

    public Debug(String loggerFilePath, boolean useStdOut) throws IOException {
        if (instance == null) {
            instance = this;
        }

        logger.addHandler(new FileHandler(loggerFilePath));

        /*if (useStdOut) {
            logger.addHandler(new ConsoleHandler());
        }*/

        logger.setLevel(Level.ALL);
    }

    public static Debug getInstance() {
        return instance;
    }

    public void log(String msg) {
        synchronized (logger) {
            logger.info(msg + "\n");
        }
    }

    public void warn(String msg) {
        synchronized (logger) {
            logger.warning(msg + "\n");
        }
    }

    @Override
    public void shutdown() {
        log("Simulation finished. Exit...");
        for (var handler : logger.getHandlers()) {
            handler.close();
        }
    }
}
