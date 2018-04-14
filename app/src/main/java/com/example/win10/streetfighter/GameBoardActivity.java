package com.example.win10.streetfighter;


import android.content.pm.ActivityInfo;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.Vector;

public class GameBoardActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_game_board);

        //get all the details from the intent, and start the countdown timer
        Bundle userData = getIntent().getExtras();
        if (userData == null)
            return;

        final String name = userData.getString("nameMessage");
        TextView nameTextView = findViewById(R.id.gameBoardTextView);
        nameTextView.setText(name);

        final TextView timerTextView = findViewById(R.id.timerTextView);
        int milliseconds = userData.getInt("milliseconds");
        countDownTimer = new CountDownTimer(milliseconds, 1000) {

            public void onTick(long millisUntilFinished) {
                timerTextView.setText(getString(R.string.time) + millisUntilFinished / 1000);
            }

            public void onFinish() {
                Toast.makeText(GameBoardActivity.this, "GAME OVER", Toast.LENGTH_SHORT).show();
                enableCards(false);
                handler.postDelayed(new Runnable() {
                    public void run() {
                        finish();
                    }
                }, DELAYMILLIS);
            }
        }.start();

        int row = userData.getInt("row");
        int col = userData.getInt("col");

        //start the matrix of cards, and shuffle the cards, each card will have a listener
        GridLayout gridLayout = findViewById(R.id.gameBoardGrid);
        gridLayout.setColumnCount(row);
        gridLayout.setRowCount(col);

        Vector<Integer> picVec = new Vector<>();

        for (int i = 0; i < row * col; i++) {
            picVec.add(i, picArr[i]);
        }
        Collections.shuffle(picVec);
        for (int i = 0; i < picVec.size(); i++) {
            Card bt = new Card(this, picVec.get(i));
            cardVec.add(i, bt);
            gridLayout.addView(cardVec.get(i));
            cardVec.get(i).setOnClickListener(new MyOnClickListener(cardVec.get(i)));

        }

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
            countDownTimer.cancel();
            Toast.makeText(GameBoardActivity.this, "YOU WIN!", Toast.LENGTH_SHORT).show();
            enableCards(false);
            handler.postDelayed(new Runnable() {
                public void run() {
                    finish();
                }
            }, DELAYMILLIS);
        }
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
}


