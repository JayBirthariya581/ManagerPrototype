package com.manage.app;

import androidx.recyclerview.widget.DiffUtil;

import java.util.ArrayList;

public class DiffUtilCallBack extends DiffUtil.Callback {

    private ArrayList<ModelService> oldList;
    private ArrayList<ModelService> newList;


    public DiffUtilCallBack(ArrayList<ModelService> oldList, ArrayList<ModelService> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldItemPosition==newItemPosition;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition) == newList.get(newItemPosition);
    }
}
