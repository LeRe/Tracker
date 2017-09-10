package ru.ijava.tracker.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import ru.ijava.tracker.R;
import ru.ijava.tracker.db.DBHelper;
import ru.ijava.tracker.model.Device;
import ru.ijava.tracker.model.PrimaryDevice;

/**
 * Created by rele on 7/6/17.
 */

public class DBFragment extends Fragment {
    DBHelper sqliteDB;
    TextView dbStatisticsTextView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_db, container, false);

        Button buttonDumpData = (Button) view.findViewById(R.id.button_dump_db);
        Button buttonClearData = (Button) view.findViewById(R.id.button_clear_db);
        Button buttonLoadData = (Button) view.findViewById(R.id.button_load_db_data);

        dbStatisticsTextView = (TextView) view.findViewById(R.id.textView_db_statistics);

        sqliteDB = new DBHelper(getActivity());

        buttonDumpData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqliteDB.dumpTablesToLog();
            }
        });

        buttonClearData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqliteDB.clearDataBase();
                showDBStatistics();
            }
        });

        buttonLoadData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Device device = null;
                try {
                    device = PrimaryDevice.getInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                    device = PrimaryDevice.getInstance(getActivity());
                }

                sqliteDB.loadExampleData(device);
                showDBStatistics();
            }
        });

        showDBStatistics();

        return view;
    }

    private void showDBStatistics() {
        dbStatisticsTextView.setText(sqliteDB.getStatistics());
    }
}
