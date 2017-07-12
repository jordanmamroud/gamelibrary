package com.example.jordan.trivialibrary;

import android.content.Context;

import com.example.jordan.basicslibrary.Utilities.Utils.MHelper;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by Jordan on 7/10/2017.
 */

public class TriviaActivityPresenter implements IMvp.Presenter {

    private IMvp.View vm ;
    private IMvp.Model model ;

    private ArrayList<Pack> packList = new ArrayList<>();
    private ArrayList<TriviaItem> currentItems = new ArrayList<>() ;
    private int scoringMulitple = 1;
    int totalPoints = 0 ;

    public TriviaActivityPresenter(IMvp.View ref  , IMvp.Model model ) {
        vm = new WeakReference<IMvp.View>(ref).get();
        this.model = model ;
        vm = ref ;
        createPacks();
    }

    public void getPackItems(){
        Pack pack = packList.get(vm.getSelectedPack());
        currentItems.clear();
        for( int i = pack.getStartIndex() ; i < pack.getEndIndex() ; i++){
            currentItems.add( model.getItem(i) );
        }
    }

    public int countPoints(){
        int totalPoints = 0 ;
        for(String item : vm.getCompletedItems()){
            String[] letters = MHelper.splitString(item);

            totalPoints += letters.length * scoringMulitple ;
        }
        return totalPoints ;
    }

    public void addnewCompletedItem(String answer){
        if( vm.getCompletedItems().indexOf(answer.trim().trim()) == -1){
            String points = String.valueOf((MHelper.splitString(answer)).length *   scoringMulitple);
            vm.setPointsView(points);
            vm.saveItem(answer, points );
        }
    }

    public void createPacks(){
        int allSize = model.getAllItems().size();
        int numOfPacks = allSize / 10 ;
        for( int i = 0 ; i < numOfPacks ; i++){
            int startIndex = i *10 ;
            int endIndex = (i * 10) + numOfPacks ;
            Pack pack = new Pack(numOfPacks , i , startIndex , endIndex );
            packList.add(pack);
        }
    }

    public ArrayList<TriviaItem> getCurrentItems(){
        getPackItems();
        return currentItems ;
    }

    @Override
    public ArrayList getListOfPacks() {

        return packList;
    }



}
