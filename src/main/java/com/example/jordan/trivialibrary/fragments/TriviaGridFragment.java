package com.example.jordan.trivialibrary.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.example.jordan.trivialibrary.IActivity;
import com.example.jordan.trivialibrary.TriviaListDelegate;

import fragments.IAdapterDelegates;
import fragments.Lists.GridView.GridViewAdapter;
import fragments.Lists.GridView.GridViewFragment;

/**
 * Created by Jordan on 7/12/2017.
 */

public class TriviaGridFragment extends GridViewFragment {

    private IActivity iActivity ;
    private int itemsOnLine = 2 ;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        iActivity = (IActivity) getActivity();
        iActivity.animateBottomBar( false    );
        IAdapterDelegates delegate = new TriviaListDelegate(getContext(), iActivity.getCompletedItems());

        setGridViewAdapter( new GridViewAdapter( getContext()  ,  iActivity.getList() , delegate    ));

        setupLayout(    itemsOnLine );

    }

    @Override
    public void onItemClicked(View v, int pos) {
        iActivity.openTriviaFragment(pos    );
    }

}
