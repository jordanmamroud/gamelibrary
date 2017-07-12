package com.example.jordan.trivialibrary;

/**
 * Created by Jordan on 5/4/2017.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Movie;
import android.util.AttributeSet;
import android.view.View;

import java.io.InputStream;

public class GIFView extends View {

    public Movie mMovie;
    public long movieStart;
    private int gifRes ;

    public GIFView(Context context) {
        super(context);

    }

    public GIFView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public GIFView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }

    private void initializeView() {
//R.drawable.loader - our animated GIF
        InputStream is = getContext().getResources().openRawResource(gifRes);
        mMovie = Movie.decodeStream(is);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.TRANSPARENT);
        super.onDraw(canvas);
        long now = android.os.SystemClock.uptimeMillis();
        if (movieStart == 0) {
            movieStart = now;
        }
        if (mMovie != null) {
            int relTime = (int) ((now - movieStart) % mMovie.duration());
            mMovie.setTime(relTime);
            mMovie.draw(canvas, getWidth() - mMovie.width(), getHeight() - mMovie.height());
            this.invalidate();
        }
    }
    private int gifId;

    public void setGIFResource(int resId) {
        this.gifId = resId;
        initializeView();
    }

    public int getGIFResource() {
        return this.gifId;
    }
}