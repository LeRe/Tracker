package ru.ijava.tracker.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;

import ru.ijava.tracker.R;
import ru.ijava.tracker.activitys.MenuActivity;

/**
 * Created by rele on 7/10/17.
 */

public class DetailSelection extends Fragment {
    MenuActivity menuActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_selection, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        menuActivity = (MenuActivity) getActivity();

        Button buttonMapShow = (Button) getView().findViewById(R.id.button_map_show);

        buttonMapShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkRadioButtons();
                menuActivity.startMapActivity();
            }
        });
    }

    private void checkRadioButtons() {
        RadioButton rbLastPosition = (RadioButton) getView().findViewById(R.id.rb_last_position);
        RadioButton rbAllPositions = (RadioButton) getView().findViewById(R.id.rb_all_position);

        if (rbLastPosition.isChecked())
        {
            menuActivity.loadRequestedPositions(menuActivity.POSITIONS_LAST);
        }
        else if (rbAllPositions.isChecked())
        {
            menuActivity.loadRequestedPositions(menuActivity.POSITIONS_ALL);
        }

    }
}
