package com.example.jordan.trivialibrary;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.TextView;


import Animations.ViewAnimations.MAnimator;
import com.example.jordan.basicslibrary.Utilities.Utils.MHelper;
import com.example.jordan.trivialibrary.fragments.TriviaFragment;
import com.example.jordan.trivialibrary.fragments.TriviaGridFragment;
import com.example.jordan.trivialibrary.fragments.TriviaItemsPager;
import com.example.jordan.trivialibrary.fragments.TriviaPacksFragment;

import java.util.ArrayList;

public class TriviaActivity extends AppCompatActivity implements IActivity, IMvp.View

{

    public final static String  ITEM_PAGER_FRAG_TAG = "triviaFragment" ;
    public final static String  GRID_VIEW_FRAG_TAG = "gridViewFragment" ;
    public final static String  LIST_FRAG_TAG = "listFragment";
    private String CURRENT_FRAG = "";

    private static String PREF_HELP_ON_KEY = "switchkey";

    private Toolbar bottomBar ;
    private FrameLayout mContent ;
    private CustomDialog customDialog ;
    private SwitchCompat helpSwitch ;
    private TextView headerTV ;
    private TriviaActivityPresenter presenter ;

    private boolean isHelpOn =  false ;
    private int selectedPack ;

    private TriviaItemsPager listPagerFragment = new TriviaItemsPager();
    private TriviaGridFragment gridViewFragment = new TriviaGridFragment();
    private TriviaPacksFragment listFragment = new TriviaPacksFragment();


    private int difficultyLevel = TriviaFragment.DIFFICULTY_EASY ;
    private int totalPoints = 0 ;
    private SQLSaver sqlSaver;

    public void setPresenter(TriviaActivityPresenter presenter){
        this.presenter = presenter ;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        sqlSaver = new SQLSaver(getApplicationContext());
        if(savedInstanceState != null){
            selectedPack = savedInstanceState.getInt("selectedPack");
        }
        instantiateView();
        setupCallbacks();
        setupView();
    }

    public void instantiateView(){
        headerTV = (TextView) findViewById(R.id.headerTV);
        mContent = (FrameLayout) findViewById(R.id.mContent) ;
        bottomBar = (Toolbar) findViewById(R.id.bottomBar) ;
        customDialog = new CustomDialog(TriviaActivity.this , R.style.FullScreenDialog  );
        helpSwitch = (  SwitchCompat    ) findViewById(R.id.helpSwitch);
    }

    public void setupCallbacks(){
        helpSwitch.setOnClickListener( this :: toggleHelpStatus );
        customDialog.setOnNextBtnClick( v-> moveNext() );
    }

    public void setupView(){
        // setting help switch based on shared preference
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        isHelpOn = sharedPrefs.getBoolean(PREF_HELP_ON_KEY, true);
        helpSwitch.setChecked(isHelpOn);
        headerTV.setText( String.valueOf(   presenter.countPoints()    ));
    }

    public void openTriviaFragment(int position){
        listPagerFragment.setCurrentPosition(position);
        openFragment(listPagerFragment , ITEM_PAGER_FRAG_TAG , GRID_VIEW_FRAG_TAG );
    }

    public void openListFragment(){
        openFragment(listFragment , LIST_FRAG_TAG , null);
    }

    public void openGridViewFragment(int position){
        selectedPack = position ;
        openFragment(gridViewFragment, GRID_VIEW_FRAG_TAG , LIST_FRAG_TAG);
    }

    public void openFragment(Fragment fragment , String tag , String backstack){
        CURRENT_FRAG = tag ;
        FragmentTransaction transaction = MHelper.setupFragTransaction(getSupportFragmentManager(), R.anim.fade_in, R.anim.fade_out, backstack);
        transaction.replace(    R.id.mContent, fragment , tag  ) ;
        transaction.commit();
    }

    public void animateBottomBar(boolean in ){
        if( in ) MAnimator.slideInUp(bottomBar);
        else MAnimator.slideOutDown(    bottomBar   );
    }

    // turns on any assistance inside of triviafragment such as letter coloring
    public void toggleHelpStatus(View v ){
        this.isHelpOn  = ((CompoundButton) v).isChecked();
        setHelpStatusPref(isHelpOn);
        listPagerFragment.getViewPager().getAdapter().notifyDataSetChanged();
    }

    public void setHelpStatusPref(boolean isHelpOn) {
        this.isHelpOn = isHelpOn;
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(  TriviaActivity.PREF_HELP_ON_KEY, isHelpOn );
        editor.apply();
    }

    @Override
    public int getSelectedPack() {
        return selectedPack;
    }

    @Override
    public ArrayList<Pack> getPacksList() {
        return presenter.getListOfPacks();
    }

    // methods for Ilistpager from listpagerfragment and list fragment
    @Override
    public ArrayList getList() {
        return presenter.getCurrentItems();
    }


    public void moveNext() {
        listPagerFragment.moveNext();
        if(customDialog.isShowing()){
            customDialog.dismiss();
        }
    }

    public void movePrevious(){
        listPagerFragment.movePrevious();
    }

    // methods for trivia fragment
    public void showCorrectDialog(String imageId){
        customDialog.showDimmedWindow(.9f);
        customDialog.setImage(  MHelper.getImageId(getApplicationContext() , imageId    ) );
    }

    public boolean getHelpStatus() {
        return isHelpOn;
    }


    // must be overriden for backgrounds to be set
    public int getBackgroundId() {return 0; }

    // right now using margin inside of fragment instead of change frame layout params because it slows down performance, may change later.
    public void changeContentParams(){
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) mContent.getLayoutParams() ;
        params.bottomMargin = bottomBar.getLayoutParams().height ;
        bottomBar.requestLayout();
    }

    public int getDifficultyLevel() {
        return difficultyLevel;
    }


    @Override
    public void saveCompletedItemAttempt(TriviaItem triviaItem) {
        presenter.addnewCompletedItem(triviaItem.getAnswer());
    }

    @Override
    public ArrayList<String> getCompletedItems() {
        return sqlSaver.getAllItems();
    }

    @Override
    public void setPointsView(String additionalPoints) {
        int prev = Integer.valueOf(headerTV.getText().toString());
        int added = Integer.valueOf(additionalPoints);
        headerTV.setText( String.valueOf(   prev + added ));
    }

    @Override
    public void saveItem(String item, String points) {
        sqlSaver.addItem(item, points);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("selectedPack" , selectedPack );
    }

    @Override
    public void onBackPressed() {
        animateBottomBar( false );
        super.onBackPressed();
    }

}
