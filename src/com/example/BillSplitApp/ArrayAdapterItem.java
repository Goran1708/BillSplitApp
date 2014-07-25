package com.example.BillSplitApp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;

public class ArrayAdapterItem extends ArrayAdapter<String> {

    Context context;
    List<String> objects;
    int textViewResourceId;

    public ArrayAdapterItem(Context context, int paramInt, List<String> paramList) {
        super(context, paramInt, paramList);
        this.context = context;
        this.textViewResourceId = paramInt;
        this.objects = paramList;
    }

    @Override
    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup) {
        if (paramView == null) {
            paramView = ((Activity)this.context).getLayoutInflater().
                    inflate(this.textViewResourceId, paramViewGroup, false);
        }
        String str = (String)this.objects.get(paramInt);
        ((TextView)paramView.findViewById(android.R.id.text1)).setText(str);
        return paramView;
    }
}