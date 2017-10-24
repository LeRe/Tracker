package ru.ijava.tracker.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.List;

import ru.ijava.tracker.model.PrimaryDevice;
import ru.ijava.tracker.services.AbstractTask;

/**
 * Created by rele on 8/14/17.
 */
public class StatusServices extends Fragment {
    private static final String STRING_FRAGMENT_NAME = "Service status.";
    private static final String STRING_STATUS_ON = "On";
    private static final String STRING_STATUS_OFF = "Off";
    private static final String STRING_GLOBAL_DELEMITER =       "==============";
    private static final String STRING_GLOBAL_DELEMITER_SHORT = "========";
    private static final String STRING_DELEMITER = ": ";

    private StringBuilder strStatus;

    public StatusServices() {
        PrimaryDevice primaryDevice;
        try {
            primaryDevice = PrimaryDevice.getInstance();
        } catch (Exception e) {
            e.printStackTrace();
            primaryDevice = PrimaryDevice.getInstance(getActivity());
        }

        List<AbstractTask> abstractTasksList = primaryDevice.getAbstractTaskList();

        strStatus = new StringBuilder();
        strStatus.append("\r\n");
        strStatus.append("\r\n");
        strStatus.append(STRING_GLOBAL_DELEMITER);
        strStatus.append("\r\n");
        strStatus.append(STRING_FRAGMENT_NAME);
        strStatus.append("\r\n");
        strStatus.append(STRING_GLOBAL_DELEMITER_SHORT);
        strStatus.append("\r\n");
        strStatus.append("\r\n");

        if(abstractTasksList != null) {
            for (AbstractTask abstractTask : abstractTasksList) {
                strStatus.append(abstractTask.getServiceName());
                strStatus.append(STRING_DELEMITER);
                if(abstractTask.isRunning()) {
                    strStatus.append(STRING_STATUS_ON);
                }
                else {
                    strStatus.append(STRING_STATUS_OFF);
                }
                strStatus.append("\r\n");
            }
        }

        strStatus.append(STRING_GLOBAL_DELEMITER);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Context context = getActivity();
        FrameLayout frameLayout = new FrameLayout(context);
        TextView textView = new TextView(context);
        textView.setText(strStatus);
        frameLayout.addView(textView);

        return frameLayout;
    }
}
