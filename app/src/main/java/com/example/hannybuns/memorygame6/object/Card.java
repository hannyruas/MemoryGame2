package com.example.hannybuns.memorygame6.object;
import android.widget.ImageView;

import com.example.hannybuns.memorygame6.R;

public class Card {
    private int iv_front;
    private int iv_back= R.drawable.back;
    private ImageView card_iv;
    private boolean done=false;

    public Card(int iv_front){
        setIv_front(iv_front);
    }

    public void setIv_front(int iv_front) {
        this.iv_front = iv_front;
    }
    public void setIv_back(int iv_back) {
        this.iv_back = iv_back;
    }
    public void setCard_iv(ImageView card_iv) {
        this.card_iv = card_iv;
    }
    public void setDone(boolean done) {
        this.done = done;
    }

    public int getIv_front() {
        return iv_front;
    }
    public int getIv_back() {
        return iv_back;
    }
    public ImageView getCard_iv() {
        return card_iv;
    }
    public boolean isDone() {
        return done;
    }

}
