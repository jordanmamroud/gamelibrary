package com.example.jordan.trivialibrary;

import com.example.jordan.basicslibrary.Utilities.Interfaces.IDrawableImage;

import java.io.Serializable;

/**
 * Created by Jordan on 7/2/2017.
 */

public class TriviaItem implements Serializable , IDrawableImage{
    private String answer ;
    private int position ;

    public TriviaItem() {
    }

    public TriviaItem(String answer) {
        this.answer = answer;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public String getDrawableName() {
        return answer.toLowerCase().trim();
    }

    public interface ITriviaItem{
        String getAnswer();
    }


}
