package ru.ijava.tracker.model;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

/**
 * Created by levchenko on 19.10.2017.
 */

public class LogSystem {
    private Context context;
    private static LogSystem instance;

    public enum DebugLevel {ERROR, DEBUG, INFO}
    public enum OutputDirection {LOGI, FILE, All}

    private final DebugLevel currentDebugLevel = DebugLevel.INFO;
    private final OutputDirection defaultOutputDirection = OutputDirection.All;

    private static final String DELIMITER = "     ";

    private static final String TAG = "RELE";

    private String logFileName = "tracker.log";

    private LogSystem() {}

    public static LogSystem getInstance(Context context) {
        if(instance == null) {
            instance = new LogSystem();
        }
        instance.setContext(context);
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

        fullMessage.append(new SimpleDateFormat("dd-MM-yyyy HH:mm.ss").format(Calendar.getInstance().getTime()));
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
        if(context != null) {
            String state = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                FileOutputStream outputStream;
                try {

                    String fullFileName = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
                            + "/" + logFileName;

                    File file = new File(fullFileName);

                    outputStream = new FileOutputStream(file, true);
                    outputStream.write(message.getBytes());
                    outputStream.write(System.getProperty("line.separator").getBytes());
                    outputStream.flush();
                    outputStream.close();
                } catch (Exception e) {
                    this.save("Take Exception. In LogSystem. LogSystem can't write to file",
                            DebugLevel.DEBUG, OutputDirection.LOGI);
                    e.printStackTrace();
                }
            }
            else {
                this.save("ERROR!!! I am LogSystem. I can't write to DIRECTORY_DOWNLOADS, please check permission",
                        DebugLevel.DEBUG, OutputDirection.LOGI);
            }
        }
        else {
            this.save("ERROR!!! I am LogSystem. I got empty context. I can't write to file",
                    DebugLevel.DEBUG, OutputDirection.LOGI);
        }
    }

    private void setContext(Context context) {
        if(context != null) {
            this.context = context;
        }
        else {
            this.save("ERROR!!! I am LogSystem. I got empty context",
                    DebugLevel.DEBUG, OutputDirection.LOGI);
        }
    }
}
