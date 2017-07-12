package com.example.jordan.trivialibrary;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import fragments.IAdapterDelegates;

/**
 * Created by Jordan on 7/6/2017.
 */

public class PickLevelDelegate implements IAdapterDelegates {

    private Context context ;

    public PickLevelDelegate(Context context  ) {
        this.context = context ;
    }

    @Override
    public void onBindViewHolder(ArrayList itemsList, RecyclerView.ViewHolder holder, int position) {
        MHolder mHolder = (MHolder) holder ;
        String title = context.getResources().getString(R.string.listItemTitle ) + " "   + String.valueOf(position + 1  ) ;
        mHolder.itemTV.setText(title);
    }

    @Override
    public RecyclerView.ViewHolder createViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from( parent.getContext()).inflate(R.layout.card_pick_level , parent , false );

        return new MHolder(v);
    }

    @Override
    public void update(Object o) {}

    public class MHolder extends RecyclerView.ViewHolder {
        CardView layout;
        TextView itemTV ;

        public MHolder(View itemView) {
            super(itemView);
            layout = (CardView) itemView.findViewById(  R.id.cardLO  )  ;
            itemTV = (TextView) itemView.findViewById(  R.id.itemTV   ) ;
        }
    }

}
