package ru.ijava.tracker.model;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by levchenko on 19.10.2017.
 */

public class LogSystem {
    private static LogSystem instance;

    public enum DebugLevel {DEBUG, INFO}
    public enum OutputDirection {LOGI, FILE, All}

    private final DebugLevel currentDebugLevel = DebugLevel.DEBUG;
    private final OutputDirection defaultOutputDirection = OutputDirection.All;

    private static final String DELIMITER = "     ";

    private static final String TAG = "RELE";

    private LogSystem() {}

    public static LogSystem getInstance() {
        if(instance == null) {
            instance = new LogSystem();
        }

        return instance;
    }

    public void save(String message, DebugLevel debugLevel, OutputDirection outputDirection) {
        if(message == null) {
            message = "nothing to say...";
        }

        if(debugLevel == null) {
            debugLevel = currentDebugLevel;
        }

        if(outputDirection == null) {
            outputDirection = defaultOutputDirection;
        }

        String fullMessage = generateFullMessage(message, debugLevel);

        if(outputDirection == OutputDirection.LOGI || outputDirection == OutputDirection.All) {
            outputLogI(fullMessage);
        }
        if(outputDirection == OutputDirection.FILE || outputDirection == OutputDirection.All) {
            outputFile(fullMessage);
        }
    }

    private String generateFullMessage(String message, DebugLevel debugLevel) {
        StringBuilder fullMessage = new StringBuilder();

        fullMessage.append(new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime()));
        fullMessage.append(DELIMITER);

        fullMessage.append(debugLevel);
        fullMessage.append(DELIMITER);

        fullMessage.append(message);

        return fullMessage.toString();
    }

    private void outputLogI(String message) {
        Log.i(TAG, message);
    }

    private void outputFile(String message) {
        Log.i(TAG, "file write...");
    }
}
