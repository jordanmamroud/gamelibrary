package com.example.jordan.trivialibrary.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.example.jordan.basicslibrary.Utilities.EventListeners.MOnItemSelected;
import com.example.jordan.trivialibrary.IActivity;
import com.example.jordan.trivialibrary.PickLevelDelegate;

import fragments.Lists.MListAdapter;
import fragments.Lists.MListFragment;

/**
 * Created by Jordan on 7/12/2017.
 */

public class TriviaPacksFragment extends MListFragment {

    IActivity iActivity ;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        iActivity = (IActivity) getActivity();
        MListAdapter adapter =new MListAdapter(getContext() , iActivity.getPacksList(), new PickLevelDelegate(getContext()) );
        setAdapter(adapter);
        setupLayout();
    }

    @Override
    public void onItemSelected(View v, int position) {
        iActivity.openGridViewFragment( position );
    }
}
