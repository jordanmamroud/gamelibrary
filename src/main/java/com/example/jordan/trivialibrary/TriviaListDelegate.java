package com.example.jordan.trivialibrary;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.jordan.basicslibrary.Utilities.Utils.MHelper;

import java.util.ArrayList;

import fragments.IAdapterDelegates;

/**
 * Created by Jordan on 7/7/2017.
 */

public class TriviaListDelegate implements IAdapterDelegates {

    Context mContext ;
    TriviaItem item ;
    ArrayList<String> completedList ;

    public TriviaListDelegate(Context context, ArrayList<String> completedList) {
        this.mContext = context ;
        this.completedList = completedList ;
    }

    @Override
    public void onBindViewHolder(ArrayList itemsList, RecyclerView.ViewHolder holder, int position) {
        TViewHolder mHolder = (TViewHolder) holder ;
        mHolder.thumbnail.setScaleType(ImageView.ScaleType.FIT_XY);
        mHolder.completedIMG.setVisibility(View.GONE);
        item = (TriviaItem) itemsList.get(position) ;

        if(completedList.indexOf( item.getAnswer().toLowerCase().trim() ) != -1){
            mHolder.completedIMG.setVisibility(View.VISIBLE);
        }

        // sets up smooth image caching and loading
        int imageId = MHelper.getImageId(mContext, item.getAnswer()) ;

        Glide.with(mContext).load(  imageId   )
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into((ImageView) mHolder.thumbnail);
    }

    @Override
    public RecyclerView.ViewHolder createViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_selection, parent , false);
        return new TViewHolder(v);
    }

    @Override
    public void update(Object o) {

    }

    public class TViewHolder extends RecyclerView.ViewHolder {

        CardView card ;
        ImageView thumbnail ;
        ImageView completedIMG;
        public TViewHolder(View itemView) {
            super(itemView);
            completedIMG = (ImageView) itemView.findViewById(R.id.completedIMG);
            card = (CardView) itemView.findViewById(R.id.cardLO);
            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail) ;
        }

    }

}
