package com.example.hannybuns.memorygame6.activity;
import android.animation.ObjectAnimator;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.graphics.Color;

import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.hannybuns.memorygame6.object.Card;
import com.example.hannybuns.memorygame6.adapter.CardListAdapterWithCache;
import com.example.hannybuns.memorygame6.R;
import com.example.hannybuns.memorygame6.object.Player;
import com.example.hannybuns.memorygame6.service.DatabaseHelper;
import com.example.hannybuns.memorygame6.service.MotionService;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import tyrantgit.explosionfield.ExplosionField;

import static java.lang.Math.abs;


public class GameActivity extends AppCompatActivity {
     ExplosionField mExplosionField;

    int DURATION_GAME = 15 * 1000;
    int TIME_TO_EXIT = (int) (1.5 * 1000);
    Random rand = new Random();
    Integer[] ALL_IMG = {R.drawable.im1, R.drawable.im2, R.drawable.im3, R.drawable.im4,
            R.drawable.im5, R.drawable.im6, R.drawable.im7, R.drawable.im8,
            R.drawable.im9, R.drawable.im10};

    ArrayList<Card> cards = new ArrayList<Card>();
    GridView gvcards = null;
    CardListAdapterWithCache adaptercards;

    ArrayList<Integer> img_in_game = new ArrayList<>();
    int level, firstIndex = -1, secondIndex = -1, numOfCouples = 0;

    Card firstCard, secondCard;

    int playerId;
    DatabaseHelper mydb;

    TextView textClock,textWin;
    long MillisecondTime, StartTime, TimeBuff, UpdateTime = 0L;
    int Seconds, Minutes, MilliSeconds;
    Handler handler = new Handler();

    public MotionService.SensorServiceBinder binder;
    private boolean isServiceBound = false;
    Boolean pauseGame=false, win=true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        mydb = new DatabaseHelper(this);
        mExplosionField = ExplosionField.attach2Window(this);

        Intent i = getIntent();
        playerId = i.getIntExtra("player", -1);

        textClock = (TextView) findViewById(R.id.textClock);
        textWin = (TextView) findViewById(R.id.textWin);
        gvcards = (GridView) findViewById(R.id.grid_cards);
        StartTime = SystemClock.uptimeMillis();
        level = getIntent().getIntExtra("level", 2);

        resetGame(level);
    }

    public void resetGame(int level) {
//        start = true;
        TimeBuff = DURATION_GAME * ((level / 2) - 1);
//        TimeBuff = DURATION_GAME;
        for (int i = 0; i < level * 2; i++)
            img_in_game.add(ALL_IMG[i / 2]);
        for (int i = 0; i < level * 2; i++) {
            int temp = rand.nextInt(img_in_game.size());
            Card c = new Card(img_in_game.get(temp));
            cards.add(c);
            img_in_game.remove(temp);
        }
        resetBoard();
    }

    public void resetBoard() {
        adaptercards = new CardListAdapterWithCache(this, cards);
        gvcards.setAdapter(adaptercards);
        handler.postDelayed(runnable, 0);
        gvcards.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                if (!pauseGame) {
                    ImageView position_iv = (ImageView) gvcards.getChildAt(position).findViewById(R.id.image);
                    setCardImageView(position_iv, position);
                    gameLogic(position);
                }
            }
        });
    }

    public void setCardImageView(ImageView iv, int i) {
        cards.get(i).setCard_iv(iv);
    }

    public void gameLogic(int i) {
        if (!cards.get(i).isDone() && !gameOver()) {
            if (firstIndex == -1) {
                firstIndex = i;
                firstCard = cards.get(firstIndex);
                firstFlipIt(firstCard.getCard_iv());
                firstCard.getCard_iv().setImageResource(firstCard.getIv_front());
                secondFlipIt(firstCard.getCard_iv());
            } else if (i != firstIndex) {
                if (secondIndex == -1) {
                    secondIndex = i;
                    secondCard = cards.get(secondIndex);
                    firstFlipIt(secondCard.getCard_iv());
                    secondCard.getCard_iv().setImageResource(secondCard.getIv_front());
                    secondFlipIt(secondCard.getCard_iv());
                    if (firstCard.getIv_front() == secondCard.getIv_front()) {
                        numOfCouples += 2;
                        firstCard.setDone(true);
                        secondCard.setDone(true);
                    }
                } else {
                    if (firstCard.getIv_front() != secondCard.getIv_front()) {
                        firstCard.getCard_iv().setImageResource(firstCard.getIv_back());
                        secondCard.getCard_iv().setImageResource(firstCard.getIv_back());
                    }
                    firstIndex = secondIndex = -1;
                    gameLogic(i);
                }
            }
        }
    }

    private boolean gameOver() {
        if (numOfCouples == cards.size() || UpdateTime < 0)
            return true;
        return false;
    }

    public void endGame(int points) {
        handler.removeCallbacks(runnable);
        Cursor res = mydb.getALLData();

        while (res.moveToNext()) {
            if (res.getInt(0) == playerId) {
                mydb.updateData(playerId, points);
            }
        }

    }

    public void goToScreenX(){
        Intent startNewGame = new Intent(this, LevelActivity.class);
        startActivity(startNewGame);
        finish();
    }

    public static void setAdapterInsertAnimation(final View aCard, int row, int height) {
        final int ANIMATION_DURATION = 650;
        final int BASE_DELAY = 50;

        TranslateAnimation translationAnimation = new TranslateAnimation(0,0, height,0);

        AlphaAnimation alphaAnimation = new AlphaAnimation(0f, 1f);

        final AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(translationAnimation);
        animationSet.addAnimation(alphaAnimation);
        animationSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animationSet.setFillAfter(true);
        animationSet.setFillBefore(true);
        animationSet.setDuration(ANIMATION_DURATION + row * BASE_DELAY);

        aCard.setAnimation(animationSet);
    }

    private void firstFlipIt(final View viewToFlip) {
        ObjectAnimator flip = ObjectAnimator.ofFloat(viewToFlip, "rotationY", 0f, 90f);
        flip.setDuration(1000);
        flip.start();
    }

    private void secondFlipIt(final View viewToFlip) {
        ObjectAnimator flip = ObjectAnimator.ofFloat(viewToFlip, "rotationY", 90f, 180f);
        flip.setDuration(1000);
        flip.start();
    }

    private void PauseGame() {
        for (Card card : cards) {
            if (card.isDone() | card == firstCard | card == secondCard) {
                if (pauseGame)
                    card.getCard_iv().setImageResource(card.getIv_back());
                if (!pauseGame)
                    card.getCard_iv().setImageResource(card.getIv_front());
            }
        }
    }

    public Runnable runnable = new Runnable() {
        public void run() {
            MillisecondTime = SystemClock.uptimeMillis() - StartTime;
            UpdateTime = TimeBuff - MillisecondTime;
            Seconds = (int) (UpdateTime / 1000);
            Minutes = Seconds / 60;
            Seconds = Seconds % 60;
            MilliSeconds = (int) (UpdateTime % 1000);
            if (!gameOver()) {
                textClock.setText("" + Minutes + ":"
                        + String.format("%02d", Seconds) + ":"
                        + String.format("%03d", MilliSeconds));
                handler.postDelayed(this, 0);
            } else {
                if (UpdateTime < 0) {
                    textClock.setText("END TIME!");
                    textClock.setTextColor(Color.RED);
                }
                if (numOfCouples == cards.size()) {
                    textClock.setText(""+Minutes + ":" +
                            String.format("%02d", Seconds) + ":" +
                            String.format("%03d", MilliSeconds));
                    textClock.setTextColor(Color.GREEN);
                }
                win=false;
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        endGame(Seconds);
                    }
                }, TIME_TO_EXIT);
            }
            if(gameOver()){
                if (numOfCouples == cards.size())
                    endAnimation(true);
                else
                    endAnimation(false);
            }

        }
    };

    private void endAnimation(boolean win) {
        Handler hendler = new Handler();
        if (!win) {
            mExplosionField.explode((View) gvcards);
        } else {
            textWin.setText("YOU WON!!!!!!");
            textWin.setTextColor(Color.GREEN);
            textClock.setText("");
            hendler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mExplosionField.explode(textWin);
                }
            }, 800);
        }
        hendler.postDelayed(new Runnable() {
            @Override
            public void run() {
                goToScreenX();
            }
        }, 1500);
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {

            binder = (MotionService.SensorServiceBinder) service;
            isServiceBound = true;
            notifyBoundService(MotionService.SensorServiceBinder.START_LISTENING);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isServiceBound = false;

        }
        void notifyBoundService(String massageFromActivity) {
            if (isServiceBound && binder != null) {
                binder.notifyService(massageFromActivity);
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        bindService(new Intent(this, MotionService.class), serviceConnection, Context.BIND_AUTO_CREATE);
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(mMessageReceiver,
                        new IntentFilter(MotionService.MOTION_CHANGE));
    }
    @Override
    protected void onResume() {
        super.onResume();
        bindService(new Intent(this, MotionService.class), serviceConnection, Context.BIND_AUTO_CREATE);
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(mMessageReceiver,
                        new IntentFilter("MOTION_CHANGE"));
    }
    @Override
    protected void onPause() {
        super.onPause();
        unbindService(serviceConnection);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);

    }
    @Override
    protected void onStop() {
        super.onStop();
    }

    // Handling the received Intents for the "my-integer" event
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            pauseGame = intent.getBooleanExtra(MotionService.VALIDITY, false);
            PauseGame();
        }
    };


}







