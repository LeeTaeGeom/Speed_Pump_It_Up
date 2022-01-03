package com.studyandroid.game;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class RankActivity extends AppCompatActivity {

    ListView listView;
    DBHelper db;
    Button back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);

        listView= findViewById(R.id.listview1);
        db = new DBHelper(this);
        back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Cursor cursor = db.getAllData();
        Log.v("count",cursor.getCount()+"? ");

        int i =0;
        String[] rank = new String[cursor.getCount()];
        while(cursor.moveToNext()){
            if(cursor.getString(2)==null){
                rank[i++]=i+"등        "+cursor.getString(0)+"    "+cursor.getString(2)+"초 ";

            }
            else{
                rank[i++]=i+"등        "+cursor.getString(0)+"    "+cursor.getFloat(2)+"초 ";

            }
        }
        
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,rank);
        
        listView.setAdapter(adapter);

    }
}