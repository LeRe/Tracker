package ru.ijava.tracker.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import ru.ijava.tracker.R;
import ru.ijava.tracker.activitys.MapActivity;

/**
 * Created by rele on 7/10/17.
 */

public class DetailSelection extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_detail_selection, container, false);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Button buttonMapShow = (Button) getView().findViewById(R.id.button_map_show);

        buttonMapShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MapActivity.class);
                //intent.putExtra(MapActivity.DEVICE_KEY, device);
                startActivity(intent);

            }
        });
    }
}
