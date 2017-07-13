package com.example.jordan.trivialibrary.LetteredView;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import Animations.ViewAnimations.MAnimator;
import com.example.jordan.basicslibrary.Utilities.Utils.MHelper;
import com.google.android.flexbox.FlexboxLayout;

import static android.view.View.VISIBLE;

/**
 * Created by Jordan on 7/9/2017.
 */

public class LetteredViewManager {

    ILetterViewManager manager ;

    public LetteredViewManager(ILetterViewManager manager) {
        this.manager = manager ;
    }

    // runs when a letter choice is clicked , fades it out and checks if it is correct
    public static void addSelectedLetter(ViewGroup layout, View childview ){
        TextView letterChoiceTV = (TextView) childview ;

        // can only be clicked once gets reset back to clickable when clicked on from answer box
        letterChoiceTV.setClickable( false );
        // checking if it is visible so that it cant be clicked more then once just to make sure double click does not happen
        if(letterChoiceTV.getVisibility() == VISIBLE) {
            String selectedLetter = letterChoiceTV.getText().toString();
            int totalLetters = layout.getChildCount();

            // checks for soonest empty string
            for (int i = 0; i < totalLetters; i++) {
                TextView selectedLetterTV = (TextView) layout.getChildAt(i);

                String answerLetter = selectedLetterTV.getText().toString();

                // sets empty text view to selected letter and ends loop
                if (answerLetter.trim().length() < 1) {

                    selectedLetterTV.setText(MHelper.capitalize(selectedLetter));
                    selectedLetterTV.setTag(letterChoiceTV.getTag());
                    MAnimator.toggleFade(letterChoiceTV);
                    break;
                }
            }
        }
    }

    public int getTextColor( String selectedLetter, String correctLetter){
        if(manager.isHelpOn()){
            return compareString(selectedLetter , correctLetter ) ? manager.getCorrectColor() : manager.getIncorrectColor() ;
        }else {
            return Color.BLACK ;
        }
    }

    public static boolean compareString(String one , String two){
        return one.toLowerCase().trim().equals(two.toLowerCase());
    }

    public int calculateChoicesLOWidth(){
        int boxWidth = manager.getChoicesTVWidth() + manager.getChoicesBoxHorizontalMargin() ;

        return ( (manager.getLetters().length + 1) * boxWidth  ) / 2 ;
    }
    public int calculateChoicesBoxHeight(){
        // we times by 3 because margin top, bottom and between the boxes
        return (manager.getChoicesTVHeight() * 2) + manager.getChoicesBoxVerticalMargin() * 2;
    }


    //    // only resizes  the boxes if they have gone off of the screen
    public int calculateChoicesBoxWidth(int layoutWidth){
        int marg =manager.getChoicesBoxHorizontalMargin() ;
        int length = manager.getLetters().length ;

        int totalWidth = (( manager.getChoicesTVWidth() +   marg )* manager.getLetters().length) / 2 ;
        int resized = ( layoutWidth / length    ) - marg ;

        return totalWidth > layoutWidth ? resized : manager.getChoicesTVWidth();
    }

    public int calculateSelectedBoxWidth( int layoutWidth) {
        String[] letters = manager.getLetters() ;
        int selectedBoxMaxLines = manager.getMaxLinesOfTVS();
        int selectedBoxMargin = manager.getSelectedBoxMargin();
        int selectedBoxMaxWidth = manager.getSelectedBoxMaxWidth() ;
        int minBoxesOnLine = manager.getMinBoxesOnLine() ;
        int width = 0;
        int availableSpace;



        // checking if num is even and adjusting available space because with flexboxlayout it will wrap odd to next line instead of shrinking other views (idk why but this fixes the problem)
        availableSpace = MHelper.isNumEven(letters.length) ? layoutWidth * selectedBoxMaxLines : layoutWidth * selectedBoxMaxLines - (layoutWidth * selectedBoxMaxLines / letters.length);

        int letterWidth = (availableSpace / letters.length) - selectedBoxMargin;

        // if width is bigger then max it gets shrunk it max width , if there is space available to make it max width it is made max width
        boolean isBigger = letterWidth > selectedBoxMaxWidth && selectedBoxMaxWidth != 0;
        boolean spaceIsAvailable = letterWidth < selectedBoxMaxWidth && (selectedBoxMaxWidth + selectedBoxMargin) * letters.length < availableSpace;

        width = isBigger || spaceIsAvailable ? selectedBoxMaxWidth : letterWidth ;

        // prevents line with only 1 letter box by expanding with of each to meet min of 3 for second line, the more
        if((width + selectedBoxMargin ) * letters.length > layoutWidth && (width + selectedBoxMargin ) * letters.length < layoutWidth + width * minBoxesOnLine ){
            // use this one to shrink layout width to force boxes to next line
//            selectedLO.getLayoutParams().width = layoutWidth - width * minBoxesOnLine ;

            // use this one to increase box size to force to next line
            return (layoutWidth + width * minBoxesOnLine )    /  letters.length ;
        }else {

            return width;
        }
    }



    public int calculateSelectedBoxHeight(int layoutWidth){
        // calculating height based on width ;
        double multiplier = manager.getSelectedTVHeightMultiplier() ;
        int maxHeight = manager.getSelectedTVMaxHeight() ;

        int resizedHeight = ((Double) Math.ceil( calculateSelectedBoxWidth(layoutWidth) * multiplier )).intValue() ;

        // if height is bigger then max height we return max height
        if(resizedHeight > maxHeight && maxHeight != 0){
            return  maxHeight ;
        }
        else {
            return resizedHeight ;
        }
    }



    interface ILetterViewManager{

        int getCorrectColor();
        int getIncorrectColor();
        FlexboxLayout getSelectedLO() ;
        FlexboxLayout getChoicesLO() ;
        int getChoicesBoxHorizontalMargin();
        int getChoicesBoxVerticalMargin() ;
        int getChoicesTVWidth() ;
        int getChoicesTVHeight();
        int getChoicesTVVertMargin();

        int getSelectedBoxMargin();
        int getMinBoxesOnLine();
        int getMaxLinesOfTVS();
        int getSelectedBoxMaxWidth();
        int getSelectedTVMaxHeight();
        double getSelectedTVHeightMultiplier();

        String[] getLetters();
        String getAnswer();
        boolean isHelpOn();
    }
}
