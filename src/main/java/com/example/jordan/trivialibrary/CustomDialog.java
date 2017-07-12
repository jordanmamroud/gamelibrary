package com.example.jordan.trivialibrary;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.zooming_entrances.ZoomInAnimator;


/**
 * Created by A7MED-Freelancer on 03-Jul-17.
 */

public class CustomDialog extends Dialog {

    TextView word;
    ImageView img;
    TextView next;
    ZoomInAnimator zoomInAnimator ;
    int fadeViewDelay ;
    int fadeViewDuration = 300 ;

    // defaulting to animation of fading in / out
    int animationStyle = R.style.DialogFadeAnimation;

    public CustomDialog(@NonNull final Context context , int dialogStyle) {
        super(context,dialogStyle);

        setContentView(R.layout.custom_dialog);
        fadeViewDelay = getContext().getResources().getInteger(R.integer.defaultFadeDuration);

        word = (TextView) findViewById(R.id.word);
        img = (ImageView) findViewById(R.id.img);
        next = (TextView) findViewById(R.id.nxt);
        word.setText("red bull");
    }

    public void setOnNextBtnClick(View.OnClickListener onNextBtnClick){
        next.setOnClickListener(onNextBtnClick);
    }

    public void setWindowAnimations(int animationStyle){
        this.animationStyle = animationStyle ;
    }

    public void setImage(int imageId) {
        img.setVisibility(View.INVISIBLE);
        img.setImageResource(   imageId    );
        zoomInAnimator = new ZoomInAnimator();
        zoomInAnimator.prepare(img);

        deleyViewFade();
    }

    // slightly delaying view animation makes it work smoother with window animation
    public void deleyViewFade(){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                img.setVisibility(View.VISIBLE);
                zoomInAnimator.animate();

            }
        }, 100    );
    }

    public void showDimmedWindow(float dimamount){
        Window window = getWindow();
        if(window != null){
            // makes background for dialog dim
            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND); // This flag is required to set otherwise the setDimAmount method will not show any effect
            window.setDimAmount(dimamount); //0 for no dim to 1 for full dim
        }

        show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getWindow() !=null) {

            getWindow().getAttributes().windowAnimations =  animationStyle;
        }
    }
}
