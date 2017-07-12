package com.example.jordan.trivialibrary.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jordan.trivialibrary.IActivity;
import com.example.jordan.trivialibrary.fragments.TriviaFragment;

import Animations.ViewPagerAnimations.RotateUpTransformer;
import fragments.ListPager.ListPagerAdapter;
import fragments.ListPager.ListPagerFragment;

/**
 * Created by Jordan on 7/12/2017.
 */

public class TriviaItemsPager extends ListPagerFragment{
    IActivity iActivity ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        System.out.println("first");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        iActivity = (IActivity) getActivity();

        setAdapter( new ListPagerAdapter(getChildFragmentManager(), iActivity.getList(), new TriviaFragment()   ));

        setupViewPager( getCurrentPosition() );

        setPageTransformer(     new RotateUpTransformer()   );
    }

}
