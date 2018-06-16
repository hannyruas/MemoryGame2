package com.example.hannybuns.memorygame6.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hannybuns.memorygame6.R;
import com.example.hannybuns.memorygame6.activity.GameActivity;
import com.example.hannybuns.memorygame6.object.Player;
import com.google.android.gms.maps.model.LatLng;

import java.util.Calendar;

public class LevelActivity extends AppCompatActivity {

    private Button easyButton;
    private Button mediumButton;
    private Button hardButton;

    String level="level";
    final int EASY = 6;
    final int MEDYUM = 8;
    final int HARD = 10;

    private TextView textView;
    private  Bundle bundle;
    private ImageView imageView;
    private int d,m;


    int playerId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);

        Intent i = getIntent();
        playerId = i.getIntExtra("player" ,-1);

        easyButton =(Button) findViewById(R.id.easyButton);
        easyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                easyGame();
            }
        });

        mediumButton =(Button) findViewById(R.id.mediumButton);
        mediumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediumGame();
            }
        });

        hardButton =(Button) findViewById(R.id.hardButton);
        hardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hardGame();
            }
        });

        textView = (TextView)findViewById(R.id.happyBirthdayView);
        imageView = (ImageView) findViewById(R.id.happyBirthdayImageView);
        bundle = getIntent().getExtras();
        setHappyBirthdayText();
    }
    public void easyGame(){
        Intent easyGame = new Intent(this, GameActivity.class);
        easyGame.putExtra(level, EASY);
        easyGame.putExtra("player", playerId);
        startActivity(easyGame);
    }
    public void mediumGame(){
        Intent mediumGame = new Intent(this, GameActivity.class);
        mediumGame.putExtra(level, MEDYUM);
        mediumGame.putExtra("player", playerId);
        startActivity(mediumGame);
    }
    public void hardGame(){
        Intent hardGame = new Intent(this, GameActivity.class);
        hardGame.putExtra(level, HARD);
        hardGame.putExtra("player", playerId);
        startActivity(hardGame);
    }


    private void setHappyBirthdayText() {
        if(Birthday()){
            imageView.setVisibility(ImageView.VISIBLE);
            textView.setText(bundle.get("name") + " Happy Birthday!!");
        }
        else
            textView.setText("Hello "+ bundle.get("name"));
    }
    private boolean Birthday() {
        Calendar calendar = Calendar.getInstance();
        d = calendar.get(Calendar.DAY_OF_MONTH);
        m = calendar.get(Calendar.MONTH);
        if((int)bundle.get("day")!= d )
            return false;
        else if ((int)bundle.get("month") != m )
            return false;
        return true;
    }
}
