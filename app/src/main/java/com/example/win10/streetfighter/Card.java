package com.example.win10.streetfighter;

import android.content.Context;

public class Card extends android.support.v7.widget.AppCompatImageButton {
    private boolean isFlipped = false;
    private int pic = 0;
    private boolean isMatch = false;


    public Card(Context context, int pic) {
        super(context);
        this.pic = pic;
        super.setImageResource(R.drawable.quastionmark);
    }

    public void flip() {
        isFlipped = !isFlipped;
        if (!isFlipped)
            setImageResource(R.drawable.quastionmark);
        else
            setImageResource(pic);
    }

    public boolean getIsFlipped() {
        return isFlipped;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Card card = (Card) o;

        return pic == card.pic;
    }

    public void setIsMatch(boolean isMatch) {
        this.isMatch = isMatch;
    }

    public boolean getIsMatch() {
        return isMatch;
    }


}