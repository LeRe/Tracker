package ru.ijava.tracker.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import ru.ijava.tracker.R;
import ru.ijava.tracker.db.DBHelper;

/**
 * Created by rele on 7/6/17.
 */

public class DBActivity extends AppCompatActivity {
    DBHelper sqliteDB;
    TextView dbStatisticsTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db);

        Button buttonLoadData = (Button) findViewById(R.id.button_load_db_data);
        Button buttonClearData = (Button) findViewById(R.id.button_clear_db);
        dbStatisticsTextView = (TextView) findViewById(R.id.textView_db_statistics);

        sqliteDB = new DBHelper(this);

        buttonLoadData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqliteDB.loadExampleData();
                showDBStatistics();
            }
        });

        buttonClearData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqliteDB.clearDataBase();
                showDBStatistics();
            }
        });

        showDBStatistics();
    }

    private void showDBStatistics() {
        dbStatisticsTextView.setText(sqliteDB.getStatistics());
    }
}
