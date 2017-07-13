package com.example.jordan.trivialibrary.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jordan.trivialibrary.IActivity;

import Animations.ViewPagerAnimations.RotateUpTransformer;
import fragments.ListPager.ListPagerAdapter;
import fragments.ListPager.ListPagerFragment;

/**
 * Created by Jordan on 7/12/2017.
 */

public class TriviaItemsPager extends ListPagerFragment {

    IActivity iActivity ;
    int offScreenLimit = 1 ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        iActivity = (IActivity) getActivity();

//        setupLayout(offScreenLimit);


        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setAdapter( new ListPagerAdapter(getChildFragmentManager(), iActivity.getList(), new TriviaFragment()   ));
        setPageTransformer(new RotateUpTransformer());
        setOffScreenLimit(1);

        // must be called last
        setupLayout();


    }
    //


//        setPageTransformer(     new RotateUpTransformer()   );
//
//        iActivity = (IActivity) getActivity();
//
//        setAdapter( new ListPagerAdapter(getChildFragmentManager(), iActivity.getList(), new TriviaFragment()   ));
//
////        setupLayout(offScreenLimit);
//
//        setPageTransformer(     new RotateUpTransformer()   );


}
