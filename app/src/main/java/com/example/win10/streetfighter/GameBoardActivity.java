package com.example.win10.streetfighter;


import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.Vector;

import tyrantgit.explosionfield.ExplosionField;

public class GameBoardActivity extends AppCompatActivity implements SensorService.SensorServiceListener {

    private final static int DELAYMILLIS = 2000;
    private static int counter = 0;
    private static Card card1 = null;
    private static Card card2 = null;
    private static int[] picArr = {R.drawable.bison, R.drawable.bison, R.drawable.cammy, R.drawable.cammy,
            R.drawable.chunli, R.drawable.chunli, R.drawable.dhalsim, R.drawable.dhalsim,
            R.drawable.feilong, R.drawable.feilong, R.drawable.guile, R.drawable.guile,
            R.drawable.ken, R.drawable.ken, R.drawable.ryu, R.drawable.ryu,
            R.drawable.sagat, R.drawable.sagat, R.drawable.vega, R.drawable.vega};
    private Vector<Card> cardVec = new Vector<>();
    private static Handler handler = new Handler();
    private CountDownTimer countDownTimer;
    private ImageView shoryukenImageView;
    private GridLayout gameGrid;
    private float[] startValues;
    private static final int DELTA=7;
    private static boolean gameIsOver;
    Location lastKnownLocation = null;
    long timeLeft;
    private String name;
    private int row;
    private int col;
    private DBAssistant DBAssistant;
    final String TAG = "GameBoardActivity";




    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            if (iBinder instanceof SensorService.SensorServiceBinder) {
                SensorService.SensorServiceBinder sensorServiceBinder = (SensorService.SensorServiceBinder) iBinder;
                sensorServiceBinder.setListener(GameBoardActivity.this);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            unbindService(serviceConnection);

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_game_board);

        bindService(new Intent(this ,SensorService.class), serviceConnection, Context.BIND_AUTO_CREATE);

        DBAssistant = new DBAssistant(this);

        gameIsOver = false;

        shoryukenImageView = (ImageView) findViewById(R.id.ryuImageView);
        shoryukenImageView.setVisibility(View.GONE);

        //get all the details from the intent, and start the countdown timer
        Bundle userData = getIntent().getExtras();
        if (userData == null)
            return;

        name = userData.getString("nameMessage");
        TextView nameTextView = findViewById(R.id.gameBoardTextView);
        nameTextView.setText(name);

        final TextView timerTextView = findViewById(R.id.timerTextView);
        int milliseconds = userData.getInt("milliseconds");
        countDownTimer = new CountDownTimer(milliseconds, 1000) {

            public void onTick(long millisUntilFinished) {
                timeLeft = millisUntilFinished;
                timerTextView.setText(getString(R.string.time) + millisUntilFinished / 1000);
            }

            public void onFinish() {
                ExplosionField explosionField = explosionField = ExplosionField.attach2Window(GameBoardActivity.this);

                explosionField.explode(gameGrid);

                Toast.makeText(GameBoardActivity.this, "GAME OVER", Toast.LENGTH_SHORT).show();
                enableCards(false);


                handler.postDelayed(new Runnable() {
                    public void run() {

                        finish();
                    }
                }, DELAYMILLIS);
            }
        }.start();

        row = userData.getInt("row");
        col = userData.getInt("col");

        //start the matrix of cards, and shuffle the cards, each card will have a listener
        gameGrid = findViewById(R.id.gameBoardGrid);
        gameGrid.setColumnCount(row);
        gameGrid.setRowCount(col);

        Vector<Integer> picVec = new Vector<>();

        for (int i = 0; i < row * col; i++) {
            picVec.add(i, picArr[i]);
        }
        Collections.shuffle(picVec);
        for (int i = 0; i < picVec.size(); i++) {
            Card bt = new Card(this, picVec.get(i));
            cardVec.add(i, bt);
            gameGrid.addView(cardVec.get(i));
            cardVec.get(i).setOnClickListener(new MyOnClickListener(cardVec.get(i)));

        }

    }

    @Override
    public void onSensorChanged(float[] values) {
        if(startValues==null) {
            startValues= new float[3];
            startValues[0] = values[0];
            startValues[1] = values[1];
            startValues[2] = values[2];
        }
        if(gameIsOver)
            return;

        float x=Math.abs(values[0]);
        float y=Math.abs(values[1]);
        float z=Math.abs(values[2]);
        float sx=Math.abs(startValues[0]);
        float sy=Math.abs(startValues[1]);
        float sz=Math.abs(startValues[2]);

        if(x > sx + DELTA || y > sy + DELTA || z > sz + DELTA)
        {
           addCardsToGame();

        }
    }

    private void addCardsToGame(){
        boolean cardsAdded = false;
        Card card = null;
        int counter = 0;
        for(int i = 0; i < cardVec.size() && counter < 2; i++) {
            if(cardVec.get(i).getIsMatch() && counter == 0){
                card = cardVec.get(i);
                card.setEnabled(true);
                card.setClickable(true);
                card.setIsMatch(false);
                card.flip();
                counter++;
                cardsAdded = true;
            }
            else if(counter > 0 && cardVec.get(i).getIsMatch()){
                if(cardVec.get(i).equals(card)){
                    cardVec.get(i).setIsMatch(false);
                    cardVec.get(i).flip();
                    cardVec.get(i).setEnabled(true);
                    cardVec.get(i).setClickable(true);
                    counter++;
                }

            }

        }
        if(cardsAdded)
            Toast.makeText(this, "punished! you moved your phone", Toast.LENGTH_SHORT).show();


    }

    // a class that associate the card who pressed to the field 'Card card' of the class for
    //checking after if the specified cards who pressed are identify or not
    class MyOnClickListener implements View.OnClickListener {
        private Card card;

        public MyOnClickListener(Card card) {
            this.card = card;
        }

        @Override
        public void onClick(View view) {
            if (!card.getIsFlipped()) {
                counter++;
                card.flip();
            }
            calculate(card);

        }

        // check if cards are identify or not
        private void calculate(Card card) {
            if (counter % 2 != 0) {
                card1 = card;
            } else
                card2 = card;
            if (card1 != null && card2 != null) {
                enableCards(false);

                if (card1.getIsFlipped() && card2.getIsFlipped()) {
                    //cards are equals, so lock them and then clear to be null for next pair of cards
                    if (card1.equals(card2)) {
                        card1.setIsMatch(true);
                        card2.setIsMatch(true);
                        card1.setEnabled(false);
                        card2.setEnabled(false);
                        card1.setClickable(false);
                        card2.setClickable(false);
                        card1 = null;
                        card2 = null;
                        checkGameOver();
                        enableCards(true);
                        //go back after a second, flip again and clear the cards to be null
                    } else {
                        handler.postDelayed(new Runnable() {

                            @Override
                            public void run() {
                                card1.flip();
                                card2.flip();
                                card1 = null;
                                card2 = null;
                                enableCards(true);

                            }
                        }, DELAYMILLIS / 2);

                    }
                }
            }
        }

        //check if the game is over
        private void checkGameOver() {
            for (int i = 0; i < cardVec.size(); i++) {
                if (!cardVec.get(i).getIsMatch())
                    return;
            }
            gameIsOver = true;

            long points = (timeLeft/100) * (row*col/2);
            getLocation();
            DBAssistant.addData(name,points,lastKnownLocation.getLatitude(),lastKnownLocation.getLongitude());

            countDownTimer.cancel();
            Toast.makeText(GameBoardActivity.this, "YOU WIN!", Toast.LENGTH_SHORT).show();
            enableCards(false);
            animationStart();
            handler.postDelayed(new Runnable() {
                public void run() {
                    finish();
                }
            }, DELAYMILLIS);
        }
    }

    private void animationStart() {
        shoryukenImageView.setVisibility(View.VISIBLE);
        shoryukenImageView.setImageResource(R.drawable.shoryuken);
        AnimationDrawable animationDrawable = (AnimationDrawable) shoryukenImageView.getDrawable();
        animationDrawable.start();
    }


    //lock or not lock the cards
    private void enableCards(boolean enable) {
        for (int i = 0; i < cardVec.size(); i++) {
            if (!cardVec.get(i).getIsMatch()) {
                cardVec.get(i).setEnabled(enable);
                cardVec.get(i).setClickable(enable);
            }

        }
    }

    // if user pressed back button, cancel the timer and finish activity
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        countDownTimer.cancel();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
        countDownTimer.cancel();
    }


    private void getLocation() {
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        String locationProvider = LocationManager.GPS_PROVIDER;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG,"No GPS - Turn on");
        }try{
            lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
            lastKnownLocation.getLongitude();
        }catch (Exception e){
            lastKnownLocation = new Location("default location");
            lastKnownLocation.setLatitude(32.113819);
            lastKnownLocation.setLongitude(34.817794);
        }
    }

}


