package com.example.jordan.trivialibrary.LetteredView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import Animations.ViewAnimations.MAnimator;
import com.example.jordan.basicslibrary.Utilities.Utils.MHelper;
import com.example.jordan.trivialibrary.R;
import com.google.android.flexbox.FlexboxLayout;


/**
 * Created by Jordan on 7/1/2017.
 */

public class LetteredView extends LinearLayout implements LetteredViewManager.ILetterViewManager{

    private Context context ;
    private FlexboxLayout selectedLO;
    private FlexboxLayout choicesLO;

    private OnAnswerCorrect mOnAnswerCorrect ;
    private OnAnswerIncorrect mOnAnswerIncorrect ;

    // default values
    private String answer = "";
    private String[] letters ;
    private boolean isHelpOn = false ;
    private int colorCorrect = Color.GREEN ;
    private int colorIncorrect = Color.RED ;

    // default values for all letter choice options
    private int choicesBoxColor = Color.WHITE ;
    private int choicesTVHeight = 100 ;
    private int choicesTVWidth = 30 ;
    private int choicesBoxHorizontalMargin = 10 ;
    private int choicesBoxVerticalMargin =    10  ;

    // default value for all selected letters
    private int selectedBoxColor = Color.WHITE ;

    // height gets calculated based on width this is just default value
    private int selectedTVHeight = 50 ;

    private int selectedTVWidth = 100 ;

    //if height based on width is bigger then max height if max height is set , set to 0 means no max height
    private int selectedBoxMaxHeight =  0;

    // when calculating width this will be used to change height to a percent of new width to avoid looking weird;
    private double selectedBoxHeightMultipler = 1.7 ;
    private int selectedBoxMinWidth = 120;
    private int selectedBoxMaxWidth = 200 ;
    private int selectedBoxMargin = 5;
    private int selectedBoxMaxLines  = 2;

    // this controls the minumum amount of boxes that can be on a new line , if increased it increase box size when used to put more boxes on a new line ;
    private int minBoxesOnLine = 2;

    //default values for both layouts
    private int spaceBetweenLayouts = 50 ;
    private int bottomMargin = 0 ;

    boolean init = false ;

    LetteredViewManager manager ;

    private int layoutWidth ;

    public LetteredView(Context context, String answer) {
        super(context);
        init(context);

        // answer must bet set it creates a textview for each letter in the answer ;
        setAnswer(answer);
    }

    public LetteredView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // xml customizable attributes
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.LetteredView);

        choicesTVHeight =(int) attributes.getDimension( R.styleable.LetteredView_choicesBoxHeight, choicesTVHeight);
        choicesTVWidth = (int)attributes.getDimension(R.styleable.LetteredView_choicesBoxWidth , choicesTVWidth);
        choicesBoxHorizontalMargin = (int) attributes.getDimension(R.styleable.LetteredView_choicesBoxHorizontalMargin , choicesBoxHorizontalMargin);
        choicesBoxVerticalMargin =(int) attributes.getDimension(R.styleable.LetteredView_choicesBoxVerticalMargin, choicesBoxVerticalMargin);
        choicesBoxColor = attributes.getColor(R.styleable.LetteredView_choicesBoxColor , choicesBoxColor );

        selectedTVHeight =(int) attributes.getDimension(R.styleable.LetteredView_selectedBoxHeight, selectedTVHeight);
        selectedBoxMaxHeight = (int) attributes.getDimension(R.styleable.LetteredView_selectedBoxMaxHeight , selectedBoxMaxHeight);
        selectedBoxMaxLines = attributes.getInt(R.styleable.LetteredView_selectedBoxMaxLines  , 2);

        // doing max width instead of regular width so that the size does not go to big with small letters
        selectedBoxMaxWidth = (int)attributes.getDimension( R.styleable.LetteredView_selectedBoxMaxWidth, selectedBoxMaxWidth);
        selectedBoxMargin = (int)attributes.getDimension( R.styleable.LetteredView_selectedBoxMargin, selectedBoxMargin) ;
        selectedBoxColor = attributes.getColor(R.styleable.LetteredView_selectedBoxColor , selectedBoxColor ) ;

        colorCorrect = attributes.getColor(R.styleable.LetteredView_correctHelpColor , colorCorrect);
        colorIncorrect = attributes.getColor(R.styleable.LetteredView_incorrectHelpColor , colorIncorrect);
        spaceBetweenLayouts = (int) attributes.getDimension(R.styleable.LetteredView_spaceBetweenLayouts , spaceBetweenLayouts);
        bottomMargin = (int) attributes.getDimension(R.styleable.LetteredView_bottomMargin , bottomMargin) ;

        attributes.recycle();
        init(context);

    }

    public LetteredView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context ){
        this.context = context;
        manager = new LetteredViewManager(this);
        layoutWidth = MHelper.getDeviceWidth(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.cv_lettered, this);
        selectedLO = (FlexboxLayout) findViewById(R.id.selectedLettersLO ) ;
        choicesLO = (FlexboxLayout) findViewById(R.id.allLettersLO);

//        System.out.println("?" + layoutWidth);
//        setupMeasurements(layoutWidth);
//        updateParams();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        layoutWidth = getMeasuredWidth() ;
//         checking to make sure that views are not being added multiple times because on layout gets called muliple times
//        setupMeasurements(layoutWidth);

    }

    public void setAnswer(String answer) {
        this.answer = answer;
        letters = getLetters();
    }

    public void addLetterViews(String letter , int pos){
        // sets up measurements in time for adding of textviews , doing before we must worry about view lifecycle.

        if(choicesLO.getChildCount() < letters.length) {
            if (pos == 0){
                setupMeasurements(layoutWidth);
            }
            createChoiceTV(letter, pos);
            createSelectedTV(pos);
            // updating params after all views have been added .
            if (pos == letters.length -1){
                updateParams();
            }
        }

    }

    public void setupMeasurements(int layoutWidth){
        this.selectedTVWidth = manager.calculateSelectedBoxWidth(  layoutWidth   ) ;
        this.selectedTVHeight = manager.calculateSelectedBoxHeight( layoutWidth  ) ;
        this.choicesTVWidth = manager.calculateChoicesBoxWidth(layoutWidth);
    }

    public void updateParams(){
        RelativeLayout.LayoutParams params =  (RelativeLayout.LayoutParams) choicesLO.getLayoutParams();
        params.height = manager.calculateChoicesBoxHeight();
        params.topMargin = spaceBetweenLayouts ;
        params.bottomMargin = bottomMargin ;

        // must be done after calculating box width
        choicesLO.getLayoutParams().width = manager.calculateChoicesLOWidth() ;
        choicesLO.setLayoutParams(params);
    }

    public void createChoiceTV(String l , int pos){
        FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(choicesTVWidth , choicesTVHeight);
        params.setMargins(choicesBoxHorizontalMargin / 2 , choicesBoxVerticalMargin / 2 , choicesBoxHorizontalMargin / 2 , choicesBoxVerticalMargin / 2);
        TextView textView = createTextView(params , pos, Color.WHITE, R.drawable.bg_rounded , choicesBoxColor   );

        String letter = MHelper.capitalize(l);

        textView.setText(letter);
        textView.setOnClickListener(v -> onLetterChoicesSelected(selectedLO , textView));
        choicesLO.addView(textView);
    }

    public void createSelectedTV(int position ){
        FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(selectedTVWidth , selectedTVHeight);
        // dividing by 2 because margin is applied to both sides
        params.setMargins( selectedBoxMargin /2  , selectedBoxMargin, selectedBoxMargin /2 , selectedBoxMargin    );
        TextView textView = createTextView(params, position, Color.BLACK, R.drawable.bg_rounded, selectedBoxColor);
        textView.setText("");
        textView.setOnClickListener(v -> onAnswerBoxSelected(   textView, position  ));

        selectedLO.addView(textView);
    }

    public void setSomeAnswers(int amount){
        int a = 0 ;
        for(int i = 0; i < 100 ; i++ ){
            int rand = MHelper.getRandomNum( letters.length );
            TextView randView = (TextView) choicesLO.getChildAt(rand);

            if(randView.getVisibility() == VISIBLE){
                onLetterChoicesSelected(choicesLO, randView);
                a   += 1    ;
                if(a == amount){
                    break;
                }
            }
        }
    }

    public void onAnswerBoxSelected(View view, int pos){
        TextView textView = (TextView) view ;
        textView.setClickable(false);
        if(textView.getText().toString().trim().length() > 0) {
            textView.setText("");
            View selectedLetterChoice = choicesLO.findViewWithTag(textView.getTag());
            MAnimator.toggleFade(  selectedLetterChoice  );
            selectedLetterChoice.setClickable(true);
        }
        textView.setClickable(true);
    }

    // runs when a letter choice is clicked , fades it out and checks if it is correct
    public void onLetterChoicesSelected(ViewGroup layout,  View childview ){
        LetteredViewManager.addSelectedLetter(    layout, childview   );

        // checking answer
        if(checkIfLetterIsCorrect()){
            if( mOnAnswerCorrect    != null) mOnAnswerCorrect.onAnswerCorrect();
        }else {
            if( mOnAnswerIncorrect !=null    )  mOnAnswerIncorrect.onAnswerIncorrect();
        }

    }

    public TextView createTextView(ViewGroup.LayoutParams params, Object tag, int textColor, int bgres, int bgcolor ){
//        TextView textView = new TextView(context);
        TextView textView = (TextView) LayoutInflater.from(context).inflate( R.layout.tv_letter, this,  false);
        textView.setTag(tag);
        textView.getBackground().setColorFilter(bgcolor, PorterDuff.Mode.SRC_ATOP);
        textView.setLayoutParams(params);
        return textView ;
    }

    // checks selected letters and gives help if help is on plus sets event for on correct or incorrect
    public boolean checkIfLetterIsCorrect(){
        String answerTry = "" ;

        //creating answer try string of all selected letters  and giving help if on
        for(int i = 0; i < selectedLO.getChildCount(); i ++){
            changeTextColorByHelpStatus(selectedLO ,  i );
            answerTry = answerTry +  ((TextView)selectedLO.getChildAt(i)).getText().toString() ;
        }

        return MHelper.compareString(answerTry , answer );
    }

    public void setHelpOn(boolean helpOn){
        this.isHelpOn = helpOn ;
        for(int i = 0; i < selectedLO.getChildCount(); i ++){
            changeTextColorByHelpStatus(selectedLO ,  i );
        }
    }

    public void changeTextColorByHelpStatus(ViewGroup layout , int pos){
        TextView letterTV = (TextView) layout.getChildAt(pos);
        String letter = letterTV.getText().toString();
        int color = manager.getTextColor( letter, letters[pos] );
        letterTV.setTextColor( color );
    }


    //methods used by manager all override methods

    public int getChoicesTVHeight() {return choicesTVHeight;}

    public int getChoicesTVVertMargin() {return choicesBoxVerticalMargin; }

    public int getChoicesTVWidth() {return choicesTVWidth;}

    public int getChoicesBoxHorizontalMargin() {return choicesBoxHorizontalMargin; }

    public int getChoicesBoxVerticalMargin() {return choicesBoxVerticalMargin ;}

    public String getAnswer() {return answer;}

    public int getCorrectColor() { return colorCorrect ; }

    public int getIncorrectColor() { return colorIncorrect ;}

    public int getSelectedBoxMargin() { return selectedBoxMargin; }

    public int getMinBoxesOnLine() { return minBoxesOnLine; }

    public int getMaxLinesOfTVS() { return selectedBoxMaxLines;}

    public int getSelectedBoxMaxWidth() { return selectedBoxMaxWidth; }

    public int getSelectedTVMaxHeight() {return selectedBoxMaxHeight; }

    public double getSelectedTVHeightMultiplier() {return selectedBoxHeightMultipler; }

    public boolean isHelpOn(){
        return isHelpOn;
    }

    // end methods used by manager

    public FlexboxLayout getChoicesLO(){ return  choicesLO; }

    public FlexboxLayout getSelectedLO(){return selectedLO ;}

    public String[] getLetters(){
        return answer.split("(?!^)");
    }

    public void setColorCorrect(int colorCorrect) {
        this.colorCorrect = colorCorrect;
    }

    public void setColorIncorrect(int colorIncorrect) {
        this.colorIncorrect = colorIncorrect;
    }

    public void setHelpStatus(boolean helpStatus){
        this.isHelpOn = helpStatus;
    }

    public void setOnAnswerCorrect(OnAnswerCorrect mOnAnswerCorrect){
        this.mOnAnswerCorrect = mOnAnswerCorrect ;
    }

    public void setOnAnswerIncorrect(OnAnswerIncorrect mOnAnswerIncorrect){
        this.mOnAnswerIncorrect = mOnAnswerIncorrect ;
    }

    public interface OnAnswerCorrect {
        void onAnswerCorrect();
    }

    public interface OnAnswerIncorrect{
        void onAnswerIncorrect();
    }

}

// realization this could be done using flexbox and re adjustin the width of the layout but can re do later if needed
// aligning textviews in columns of 2 ;
//    private  RelativeLayout.LayoutParams getChoicesBoxParams(RelativeLayout layout, int num){
//
//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(choicesTVWidth, choicesTVHeight);
//
//        if( MHelper.isNumEven(num) ){
//
//            // doing if num is even
//            params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
//
//
//            if(num > 0){
//                params.addRule(RelativeLayout.RIGHT_OF, layout.findViewWithTag((Integer)num -1).getId()   );
//            }else {
//                params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//            }
//
//            // controlling horizontal margin by setting margin right and controls space between top and bottom views by setting margin bottom
//            params.setMargins(0 , 0 , choicesBoxHorizontalMargin, choicesBoxVerticalMargin);
//
//        }else {
//            // doing if num is odd
//            if(num > 1){
//                params.addRule(RelativeLayout.RIGHT_OF, layout.findViewWithTag((Integer)    num - 2  ).getId()   );
//                params.addRule( RelativeLayout.ALIGN_PARENT_BOTTOM    );
//            }else {
//                params.addRule( RelativeLayout.ALIGN_PARENT_BOTTOM    );
//            }
//            // // controlling horizontal margin by setting margin right  and setting top and bottom to 0 to let top views control vertical margin between
//            params.setMargins(0 ,   0 , choicesBoxHorizontalMargin, 0 );
//        }
//
//        return params;
//    }












//
//
//    private Context context ;
//    private LetteredViewManager logic ;
//
//    private FlexboxLayout selectedLO;
//    private FlexboxLayout choicesLO;
//
//    private OnAnswerCorrect mOnAnswerCorrect ;
//    private OnAnswerIncorrect mOnAnswerIncorrect ;
//
//    // default values
//    private String answer = "";
//    private String[] letters ;
//    private boolean isHelpOn = false ;
//    private int colorCorrect = Color.GREEN ;
//    private int colorIncorrect = Color.RED ;
//
//
//    // default values for all letter choice options
//    private int choicesBoxColor = Color.WHITE ;
//    private int choicesTVHeight = 100 ;
//    private int choicesTVWidth = 30 ;
//    private int choicesBoxHorizontalMargin = 10 ;
//    private int choicesBoxVerticalMargin =    10  ;
//
//
//    // default value for all selected letters
//    private int selectedBoxColor = Color.WHITE ;
//
//    // height gets calculated based on width this is just default value
//    private int selectedTVHeight = 50 ;
//
//    private int selectedTVWidth = 100 ;
//
//    //if height based on width is bigger then max height if max height is set , set to 0 means no max height
//    private int selectedBoxMaxHeight =  0;
//
//    // when calculating width this will be used to change height to a percent of new width to avoid looking weird;
//    private double selectedBoxHeightMultipler = 1.7 ;
//    private int selectedBoxMinWidth = 120;
//    private int selectedBoxMaxWidth = 200 ;
//    private int selectedBoxMargin = 5;
//    private int selectedBoxMaxLines  = 2;
//
//    // this controls the minumum amount of boxes that can be on a new line , if increased it increase box size when used to put more boxes on a new line ;
//    private int minBoxesOnLine = 2;
//
//    //default values for both layouts
//    private int boxElevation = 0 ;
//    private int spaceBetweenLayouts = 50 ;
//    private int bottomMargin = 0 ;
//
//    boolean init = false ;
//
//    private int layoutWidth ;
//
//    public LetteredView(Context context, String answer) {
//        super(context);
//        init(context);
//
//        // answer must bet set it creates a textview for each letter in the answer ;
//        setAnswer(answer);
//    }
//
//    public LetteredView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//
//        // xml customizable attributes
//        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.LetteredView);
//
//        choicesTVHeight =(int) attributes.getDimension( R.styleable.LetteredView_choicesBoxHeight, choicesTVHeight);
//        choicesTVWidth = (int)attributes.getDimension(R.styleable.LetteredView_choicesBoxWidth , choicesTVWidth);
//        choicesBoxHorizontalMargin = (int) attributes.getDimension(R.styleable.LetteredView_choicesBoxHorizontalMargin , choicesBoxHorizontalMargin);
//
//        choicesBoxVerticalMargin =(int) attributes.getDimension(R.styleable.LetteredView_choicesBoxVerticalMargin, choicesBoxVerticalMargin);
//        choicesBoxColor = attributes.getColor(R.styleable.LetteredView_choicesBoxColor , choicesBoxColor );
//
//        selectedTVHeight =(int) attributes.getDimension(R.styleable.LetteredView_selectedBoxHeight, selectedTVHeight);
//
//        selectedBoxMaxHeight = (int) attributes.getDimension(R.styleable.LetteredView_selectedBoxMaxHeight , selectedBoxMaxHeight);
//
//        selectedBoxMaxLines = attributes.getInt(R.styleable.LetteredView_selectedBoxMaxLines  , 2);
//
//        // doing max width instead of regular width so that the size does not go to big with small letters
//
//        selectedBoxMaxWidth = (int)attributes.getDimension( R.styleable.LetteredView_selectedBoxMaxWidth, selectedBoxMaxWidth);
//
//        selectedBoxMargin = (int)attributes.getDimension( R.styleable.LetteredView_selectedBoxMargin, selectedBoxMargin) ;
//
//
//        selectedBoxColor = attributes.getColor(R.styleable.LetteredView_selectedBoxColor , selectedBoxColor ) ;
//
//        boxElevation = (int) attributes.getDimension(   R.styleable.LetteredView_boxElevation , boxElevation   );
//        colorCorrect = attributes.getColor(R.styleable.LetteredView_correctHelpColor , colorCorrect);
//        colorIncorrect = attributes.getColor(R.styleable.LetteredView_incorrectHelpColor , colorIncorrect);
//        spaceBetweenLayouts = (int) attributes.getDimension(R.styleable.LetteredView_spaceBetweenLayouts , spaceBetweenLayouts);
//        bottomMargin = (int) attributes.getDimension(R.styleable.LetteredView_bottomMargin , bottomMargin) ;
//
//        attributes.recycle();
//        init(context);
//
//
//    }
//
//
//
//    public LetteredView(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        init(context);
//    }
//
//    public void init(Context context ){
//        this.context = context;
//        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        inflater.inflate(R.layout.cv_lettered, this);
//        layoutWidth = MHelper.getDeviceWidth(context);
//
//        selectedLO = (FlexboxLayout) findViewById(R.id.selectedLettersLO ) ;
//        choicesLO = (FlexboxLayout) findViewById(R.id.allLettersLO);
//    }
//
//
//    @Override
//    protected void onLayout(boolean changed, int l, int t, int r, int b) {
//        super.onLayout(changed, l, t, r, b);
//        layoutWidth = getMeasuredWidth() ;
////        setupLayout();
//        // checking to make sure that views are not being added multiple times because on layout gets called muliple times
//        if(selectedLO.getChildCount() < letters.length || changed   ) {
//            setupLayout();
//
//        }
//    }
//
//    public void setupLayout(){
////
//        setupMeasurements(layoutWidth);
//        setupSelectedBoxMeasurements(layoutWidth);
//
//    }
//
//    public void setAnswer(String answer) {
//        this.answer = answer;
//        letters = getLetters();
//    }
//
//    public void addLetterViews(String letter , int pos){
//        createChoiceTV(letter , pos);
//        createSelectedTV(pos);
//    }
//
//    public void setupMeasurements(int layoutWidth){
//        RelativeLayout.LayoutParams params =  (RelativeLayout.LayoutParams) choicesLO.getLayoutParams();
//        params.height = calculateChoicesBoxHeight();
//        params.topMargin = spaceBetweenLayouts ;
//        params.bottomMargin = bottomMargin ;
//
//        this.choicesTVWidth = calculateChoicesBoxWidth(layoutWidth);
//
//        // must be done after calculating box width
//
//        choicesLO.getLayoutParams().width = calculateChoicesLOWidth() ;
//
//    }
//
//    public void setupSelectedBoxMeasurements(int layoutWidth){
//        this.selectedTVWidth = calculateSelectedBoxWidth(  layoutWidth   ) ;
//        this.selectedTVHeight = calculateSelectedBoxHeight( layoutWidth  ) ;
//    }
//
//    public void createChoiceTV(String l , int pos){
//        FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(choicesTVWidth , choicesTVHeight);
//        params.setMargins(choicesBoxHorizontalMargin / 2 , choicesBoxVerticalMargin / 2 , choicesBoxHorizontalMargin / 2 , choicesBoxVerticalMargin / 2);
//        TextView textView = createTextView(params , pos, Color.WHITE, R.drawable.bg_rounded , choicesBoxColor   );
//
//        String letter = MHelper.capitalize(l);
//
//        textView.setText(letter);
//        textView.setOnClickListener(v -> onLetterChoicesSelected(selectedLO , textView));
//        choicesLO.addView(textView);
//    }
//
//    public void createSelectedTV(int position ){
//        FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(selectedTVWidth , selectedTVHeight);
//        // dividing by 2 because margin is applied to both sides
//        params.setMargins( selectedBoxMargin /2  , selectedBoxMargin, selectedBoxMargin /2 , selectedBoxMargin    );
//        TextView textView = createTextView(params, position, Color.BLACK, R.drawable.bg_rounded, selectedBoxColor);
//        textView.setText("");
//        textView.setOnClickListener(v -> onAnswerBoxSelected(   textView, position  ));
//        selectedLO.addView(textView);
//    }
//
//    public void setSomeAnswers(int amount){
//
//        int a = 0 ;
//
//        for(int i = 0; i < 100 ; i++ ){
//            int rand = MHelper.getRandomNum( letters.length );
//            TextView randView = (TextView) choicesLO.getChildAt(rand);
//
//            if(randView.getVisibility() == VISIBLE){
//                onLetterChoicesSelected(choicesLO, randView);
//                a   += 1    ;
//                if(a == amount){
//                    break;
//                }
//            }
//        }
//
//    }
//
//    public void onAnswerBoxSelected(View view, int pos){
//        TextView textView = (TextView) view ;
//        textView.setClickable(false);
//        if(textView.getText().toString().trim().length() > 0) {
//            textView.setText("");
//            View selectedLetterChoice = choicesLO.findViewWithTag(textView.getTag());
//            MAnimator.toggleFade(  selectedLetterChoice  );
//            selectedLetterChoice.setClickable(true);
//        }
//        textView.setClickable(true);
//    }
//
//    // runs when a letter choice is clicked , fades it out and checks if it is correct
//    public void onLetterChoicesSelected(ViewGroup layout,  View childview ){
//        LetteredViewManager.addSelectedLetter(    layout, childview   );
////        TextView letterChoiceTV = (TextView) childview ;
////
////        // can only be clicked once gets reset back to clickable when clicked on from answer box
////        letterChoiceTV.setClickable( false );
////        // checking if it is visible so that it cant be clicked more then once just to make sure double click does not happen
////        if(letterChoiceTV.getVisibility() == VISIBLE) {
////            String selectedLetter = letterChoiceTV.getText().toString();
////            int totalLetters = layout.getChildCount();
////
////            // checks for soonest empty string
////            for (int i = 0; i < totalLetters; i++) {
////                TextView selectedLetterTV = (TextView) layout.getChildAt(i);
////
////                String answerLetter = selectedLetterTV.getText().toString();
////
////                // sets empty text view to selected letter and ends loop
////                if (answerLetter.trim().length() < 1) {
////
////                    selectedLetterTV.setText(MHelper.capitalize(selectedLetter));
////                    selectedLetterTV.setTag(letterChoiceTV.getTag());
////                    MAnimator.toggleFade(letterChoiceTV);
////                    break;
////                }
////            }
//
//        // checking answer
//        checkIfLetterIsCorrect(isHelpOn());
//    }
//
//
//    public TextView createTextView(ViewGroup.LayoutParams params, Object tag, int textColor, int bgres, int bgcolor ){
////        TextView textView = new TextView(context);
//        TextView textView = (TextView) LayoutInflater.from(context).inflate( R.layout.tv_letter, this,  false);
//        textView.setTag(tag);
//        textView.getBackground().setColorFilter(bgcolor, PorterDuff.Mode.SRC_ATOP);
//        textView.setLayoutParams(params);
//        return textView ;
//    }
//
//
//    // checks selected letters and gives help if help is on plus sets event for on correct or incorrect
//    public void checkIfLetterIsCorrect(boolean isHelpOn ){
//
//        String answerTry = "" ;
//
//        //creating answer try string of all selected letters  and giving help if on
//
//        for(int i = 0; i < selectedLO.getChildCount(); i ++){
//            TextView selectedLettersTV = (TextView) selectedLO.getChildAt(i);
//            String letter = selectedLettersTV.getText().toString();
//            String correctLetter =  letters[i] ;
//            LetteredViewManager.isCorrect(selectedLettersTV, isHelpOn ,colorCorrect, colorIncorrect, letter, correctLetter   );
//            answerTry = answerTry + letter;
//        }
//
//        runEvent(answerTry);
//    }
//
//    public void runEvent(String answerTry){
//        // runs event for on correct and incorrect if they are not null
//        if(answerTry.toLowerCase().trim().equals(answer.toLowerCase().trim())){
//            if( mOnAnswerCorrect    != null) mOnAnswerCorrect.onAnswerCorrect();
//
//        }else {
//            if( mOnAnswerIncorrect !=null    )  mOnAnswerIncorrect.onAnswerIncorrect();
//        }
//    }
//
//
//    public void setHelpOn(boolean helpOn){
//        this.isHelpOn = helpOn ;
//        for(int i = 0; i < selectedLO.getChildCount(); i ++){
//            TextView selectedLettersTV = (TextView) selectedLO.getChildAt(i);
//            String letter = selectedLettersTV.getText().toString();
//            String correct = letters[i];
//            LetteredViewManager.isCorrect(selectedLettersTV, helpOn, colorCorrect, colorIncorrect, letter, correct) ;
//        }
//    }
//
//    // sets color of text based on whether or not selected letter is in correct position
//    public void setLetterHelp(boolean helpon, TextView view, String selectedLetter, int position){
//        if(helpon){
////            int color = LetteredViewManager.isCorrect(selectedLetter , letters[position]) ? colorCorrect : colorIncorrect  ;
////            view.setTextColor(color);
//        }else {
//            view.setTextColor(Color.BLACK);
//        }
//    }
//
//    public int calculateChoicesLOWidth(){
//        int boxWidth = choicesTVWidth + choicesBoxHorizontalMargin ;
//        int totalWidth = ( (letters.length + 1) * boxWidth  ) / 2 ;
//        return totalWidth ;
//    }
//
//    // calculates width for boxes that hold selected letters
//
//    public int calculateSelectedBoxWidth( int layoutWidth) {
//
//        int width = 0;
//        int availableSpace;
//
//        // checking if num is even and adjusting available space because with flexboxlayout it will wrap odd to next line instead of shrinking other views (idk why but this fixes the problem)
//        availableSpace = MHelper.isNumEven(letters.length) ? layoutWidth * selectedBoxMaxLines : layoutWidth * selectedBoxMaxLines - (layoutWidth * selectedBoxMaxLines / letters.length);
//
//        int letterWidth = (availableSpace / letters.length) - selectedBoxMargin;
//
//        // if width is bigger then max it gets shrunk it max width , if there is space available to make it max width it is made max width
//        boolean isBigger = letterWidth > selectedBoxMaxWidth && selectedBoxMaxWidth != 0;
//        boolean spaceIsAvailable = letterWidth < selectedBoxMaxWidth && (selectedBoxMaxWidth + selectedBoxMargin) * letters.length < availableSpace;
//
//        width = isBigger || spaceIsAvailable ? selectedBoxMaxWidth : letterWidth ;
//
//        // prevents line with only 1 letter box by expanding with of each to meet min of 3 for second line, the more
//        if((width + selectedBoxMargin ) * letters.length > layoutWidth && (width + selectedBoxMargin ) * letters.length < layoutWidth + width * minBoxesOnLine ){
//            // use this one to shrink layout width to force boxes to next line
////            selectedLO.getLayoutParams().width = layoutWidth - width * minBoxesOnLine ;
//
//            // use this one to increase box size to force to next line
//            return (layoutWidth + width * minBoxesOnLine )    /  letters.length ;
//        }
//
//        return width ;
//    }
//
//    // calculates width for boxes that hold selected letters
//
//    public int calculateSelectedBoxHeight(int layoutWidth){
//        // calculating height based on width ;
//        int height = ((Double) Math.ceil( calculateSelectedBoxWidth(layoutWidth) * selectedBoxHeightMultipler )).intValue() ;
//        // if height is bigger then max height we return max height
//        if(height > selectedBoxMaxHeight && selectedBoxMaxHeight != 0){
//            return  selectedBoxMaxHeight ;
//        }
//        else {
//            return height ;
//        }
//    }
//
//    // only resizes  the boxes if they have gone off of the screen
//    public int calculateChoicesBoxWidth(int layoutWidth){
//        int totalWidth = ((choicesTVWidth + choicesBoxHorizontalMargin)* letters.length) / 2 ;
//        int resized = ( layoutWidth / letters.length) - choicesBoxHorizontalMargin ;
//
//        return totalWidth > layoutWidth ? resized : choicesTVWidth;
//    }
//
//    public int calculateChoicesBoxHeight(){
//        // we times by 3 because margin top, bottom and between the boxes
//        return (choicesTVHeight * 2) + choicesBoxVerticalMargin * 2;
//    }
//
//    public FlexboxLayout getChoicesLO(){ return  choicesLO; }
//
//    public FlexboxLayout getSelectedLO(){return selectedLO ;}
//
//    public String[] getLetters(){
//        return answer.split("(?!^)");
//    }
//
//    public void setColorCorrect(int colorCorrect) {
//        this.colorCorrect = colorCorrect;
//    }
//
//    public void setColorIncorrect(int colorIncorrect) {
//        this.colorIncorrect = colorIncorrect;
//    }
//
//    public void setHelpStatus(boolean helpStatus){
//        this.isHelpOn = helpStatus;
//    }
//
//    public void setOnAnswerCorrect(OnAnswerCorrect mOnAnswerCorrect){
//        this.mOnAnswerCorrect = mOnAnswerCorrect ;
//    }
//
//    public void setOnAnswerIncorrect(OnAnswerIncorrect mOnAnswerIncorrect){
//        this.mOnAnswerIncorrect = mOnAnswerIncorrect ;
//    }
//
//    public boolean isHelpOn(){
//        return isHelpOn;
//    }
//
//interface OnAnswerCorrect {
//    void onAnswerCorrect();
//}
//
//interface OnAnswerIncorrect{
//    void onAnswerIncorrect();
//}
//
//}
//
//// realization this could be done using flexbox and re adjustin the width of the layout but can re do later if needed
//// aligning textviews in columns of 2 ;
////    private  RelativeLayout.LayoutParams getChoicesBoxParams(RelativeLayout layout, int num){
////
////        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(choicesTVWidth, choicesTVHeight);
////
////        if( MHelper.isNumEven(num) ){
////
////            // doing if num is even
////            params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
////
////
////            if(num > 0){
////                params.addRule(RelativeLayout.RIGHT_OF, layout.findViewWithTag((Integer)num -1).getId()   );
////            }else {
////                params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
////            }
////
////            // controlling horizontal margin by setting margin right and controls space between top and bottom views by setting margin bottom
////            params.setMargins(0 , 0 , choicesBoxHorizontalMargin, choicesBoxVerticalMargin);
////
////        }else {
////            // doing if num is odd
////            if(num > 1){
////                params.addRule(RelativeLayout.RIGHT_OF, layout.findViewWithTag((Integer)    num - 2  ).getId()   );
////                params.addRule( RelativeLayout.ALIGN_PARENT_BOTTOM    );
////            }else {
////                params.addRule( RelativeLayout.ALIGN_PARENT_BOTTOM    );
////            }
////            // // controlling horizontal margin by setting margin right  and setting top and bottom to 0 to let top views control vertical margin between
////            params.setMargins(0 ,   0 , choicesBoxHorizontalMargin, 0 );
////        }
////
////        return params;
////    }
//
//
//
//
//
//
//
//
//
//
//












