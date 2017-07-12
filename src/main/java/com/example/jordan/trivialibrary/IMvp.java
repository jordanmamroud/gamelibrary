package com.example.jordan.trivialibrary;

import com.example.jordan.trivialibrary.TriviaItem;

import java.util.ArrayList;

/**
 * Created by Jordan on 7/10/2017.
 */

public interface IMvp {

    interface View {
        int getSelectedPack();
        ArrayList<String> getCompletedItems();
        void saveItem(String item, String points);
        void setPointsView(String points);
    };

    interface Presenter{
        ArrayList getListOfPacks();
    };

    interface Model{
        void loadData();
        TriviaItem getItem(int position);
        ArrayList<? extends TriviaItem> getAllItems();
        ArrayList<TriviaItem> getItemsInRange(int startIndex , int endIndex);

    };

}
