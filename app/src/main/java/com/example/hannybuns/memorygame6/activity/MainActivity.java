package com.example.hannybuns.memorygame6.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hannybuns.memorygame6.R;
import com.example.hannybuns.memorygame6.object.Player;
import com.example.hannybuns.memorygame6.service.DatabaseHelper;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper mydb;

    private Button playButton ;
    private Button recordsListButton ;
    private Button recordsMapButton ;
    private Button buttonCalander ;
    private TextView dateView;
    private TextView nameView;
    private TextView editName;

    private int d,m,y;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mydb = new DatabaseHelper(this);
        findView();
        playButtonOnClick();
        recordsListButtonOnClick();
        recordsMapButtonOnClick();
        buttonCalanderOnClick();
    }


    private void findView() {
        playButton = (Button)findViewById(R.id.playButton);
        recordsListButton = (Button)findViewById(R.id.recordsTable);
        recordsMapButton = (Button)findViewById(R.id.recordsMap);
        buttonCalander = (Button)findViewById(R.id.buttonCalander);
        dateView = (TextView)findViewById(R.id.viewDate);
        nameView = (TextView)findViewById(R.id.viewName);
        editName= (TextView)findViewById(R.id.editName);
    }

    private void buttonCalanderOnClick() {
        buttonCalander.setOnClickListener(new View.OnClickListener() {
            Calendar calendar = Calendar.getInstance();

            @Override
            public void onClick(View view) {
                d = calendar.get(Calendar.DAY_OF_MONTH);
                m = calendar.get(Calendar.MONTH);
                y = calendar.get(Calendar.YEAR);

                DatePickerDialog pickerDialog = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        dateView.setText("Date:"+i2+"/"+i1+"/"+i);
                        y=i;m=i1;d=i2;
                    }
                }, y, m, d);
                Toast.makeText(MainActivity.this, d+"/"+m+"/"+y, Toast.LENGTH_LONG).show();
                pickerDialog.show();
            }
        });
    }
    private void playButtonOnClick() {
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Player player = new Player(editName.getText().toString(),
                        d+"."+m+"."+y, 0);
                player.setId(mydb.insertData(player));
                if (player.getId() !=- 1) {

                    Toast.makeText(getApplicationContext(), "DONE",
                            Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(MainActivity.this, LevelActivity.class);
                    intent.putExtra("day", d);
                    intent.putExtra("month", m);
                    intent.putExtra("year", y);
                    intent.putExtra("name", editName.getText());
                    intent.putExtra("player", player.getId());

                    Toast.makeText(MainActivity.this, nameView.getText(), Toast.LENGTH_LONG);
                    startActivity(intent);
                }

                }

        });
    }
    private void recordsListButtonOnClick() {
        recordsListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)  {
                Intent intent = new Intent(MainActivity.this , RecordsActivity.class);
                intent.putExtra("fragment_type", "LIST");
                startActivity(intent);
            }
        });
    }
    private void recordsMapButtonOnClick() {
        recordsMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)  {
                Intent intent = new Intent(MainActivity.this , RecordsActivity.class);
                intent.putExtra("fragment_type", "MAP");
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(MainActivity.this, nameView.getText().toString(), Toast.LENGTH_LONG).show();
    }

}
