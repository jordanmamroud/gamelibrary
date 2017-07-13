package com.example.jordan.trivialibrary.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;


import com.example.jordan.basicslibrary.Utilities.Utils.MHelper;
import com.example.jordan.trivialibrary.IActivity;
import com.example.jordan.trivialibrary.LetteredView.LetteredView;
import com.example.jordan.trivialibrary.R;
import com.example.jordan.trivialibrary.TriviaActivity;
import com.example.jordan.trivialibrary.TriviaItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Observable;
import java.util.Observer;

import fragments.ListPager.IPagerItemFragment;


/**
 * Created by Jordan on 7/1/2017.
 */

public class TriviaFragment extends Fragment implements IPagerItemFragment , Observer {

    public static int DIFFICULTY_EASY = 0;
    public static int DIFFICULTY_MEDIUM = 1;
    public static int DIFFICULTY_Hard = 2;


    private LinearLayout imageContainerLO;
    private View v;
    private ImageView itemIMG;
    private LetteredView mLetteredView;
    private TriviaItem triviaItem;
    private IActivity iActivity;

    @Override
    public Fragment newInstance(Object object, int position) {
        System.out.println("created");
        TriviaFragment triviaFragment = new TriviaFragment();
        triviaFragment.setItem(object, position);
        return triviaFragment;
    }

    @Override
    public void setItem(Object object, int position) {
        this.triviaItem = (TriviaItem) object;
        this.triviaItem.setPosition(position);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_trivia, container, false);
        iActivity = (IActivity) getActivity();

        if (savedInstanceState != null) {
            triviaItem = (TriviaItem) savedInstanceState.getSerializable("TRIVIA_ITEM");
        }

        instantiateView();
        setupCallbacks();
        setupLayout();
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        new LoadData().execute();
    }

    public void instantiateView() {
        mLetteredView = (LetteredView) v.findViewById(R.id.mLetteredView);
        mLetteredView.setAnswer(triviaItem.getAnswer());
        mLetteredView.setHelpOn(iActivity.getHelpStatus());
        itemIMG = (ImageView) v.findViewById(R.id.itemIMG);
        imageContainerLO = (LinearLayout) v.findViewById(R.id.imageContainerLO);
    };

    public void setupCallbacks() {
        mLetteredView.setOnAnswerCorrect(   this :: onAnswerCorrect   );
    }

    public void onAnswerCorrect(){
        iActivity.showCorrectDialog( triviaItem.getAnswer());
        iActivity.saveCompletedItemAttempt(triviaItem);
    }

    public void setupLayout() {
        if (iActivity.getBackgroundId() > 0) {
            imageContainerLO.setBackgroundResource( iActivity.getBackgroundId()  );
        }
        itemIMG.setImageResource(MHelper.getImageId(getContext(), triviaItem.getAnswer()   ));
    }

    @Override
    public void update(Observable o, Object arg) {
        if (v != null) {
            mLetteredView.setHelpOn(iActivity.getHelpStatus());
//            mLetteredView.setSomeAnswers(3);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("TRIVIA_ITEM", triviaItem);
    }

    // async adding views and animating
    public class LoadData extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {
        }

        protected Void doInBackground(Void... params) {
            iActivity.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    ArrayList<String> shuffledList = new ArrayList<String>(Arrays.asList(mLetteredView.getLetters()));
                    Collections.shuffle(shuffledList);
                    for (int i = 0; i < shuffledList.size(); i++) {
                          mLetteredView.addLetterViews(shuffledList.get(i), i);
                    }

                    // doing this in background because makes for smoother transition.
                    iActivity.animateBottomBar( true );
                }
            });

            return null;
        }

        protected void onPostExecute(Void result) {

        }
    }
}
