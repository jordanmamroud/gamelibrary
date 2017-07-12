package com.example.jordan.trivialibrary;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Created by Jordan on 7/1/2017.
 */

public class TriviaHomeActivity extends AppCompatActivity {

    private ImageView logoIMG ;
    private Button mainBTN1 , mainBTN2 , mainBTN3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        instantiateView();
        setupCallbacks();
//        setupCallbacks();
    }

    public void instantiateView(){
        logoIMG = (ImageView) findViewById(R.id.logoIMG);
        mainBTN1 =(Button) findViewById(R.id.mainBTN1);
        mainBTN2 = (Button) findViewById(R.id.mainBTN2);
        mainBTN3 = (Button) findViewById(R.id.mainBTN3);
    }


    public void setupCallbacks(){
        mainBTN1.setOnClickListener( v-> startGame()    );
        mainBTN2.setOnClickListener( v-> showHighScores()   );
        mainBTN3.setOnClickListener(null);
    }

    public void setLogoIMG(int res){
        logoIMG.setImageResource( res   );
    }

    public void startGame(){

    }

    public void showHighScores(){}


}
