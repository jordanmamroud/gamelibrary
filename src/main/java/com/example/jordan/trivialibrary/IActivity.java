package com.example.jordan.trivialibrary;

import java.util.ArrayList;

/**
 * Created by Jordan on 7/10/2017.
 */

public interface IActivity {

    boolean getHelpStatus();
    int getBackgroundId ();
    void runOnUiThread(Runnable runnable);
    void showCorrectDialog(String imageId);
    void animateBottomBar(boolean in);
    void openTriviaFragment(int position);
    void openGridViewFragment(int position);
    int getDifficultyLevel();

    ArrayList getList();
    ArrayList<String> getCompletedItems();
    ArrayList<Pack> getPacksList();
    void saveCompletedItemAttempt(TriviaItem triviaItem);

}
