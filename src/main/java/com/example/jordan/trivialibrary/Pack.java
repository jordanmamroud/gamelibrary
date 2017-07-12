package com.example.jordan.trivialibrary;

/**
 * Created by Jordan on 7/10/2017.
 */

public class Pack {
    private int size ;
    private int position ;
    private int progress ;
    private int startIndex ;
    private int endIndex ;

    public Pack(int size, int position, int startIndex, int endIndex) {
        this.size = size;
        this.position = position ;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public int getEndIndex() {
        return endIndex;
    }

    public void setEndIndex(int endIndex) {
        this.endIndex = endIndex;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }


    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
}
