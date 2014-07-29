package com.example.BillSplitApp;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Menu extends ListActivity {

    String[] activityNames = { "Accounting", "Exit" };

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setListAdapter(new ArrayAdapter<Object>(this,
                android.R.layout.simple_list_item_1, this.activityNames));
    }

    @Override
    protected void onListItemClick(ListView l, View view, int i, long id) {
        super.onListItemClick(l, view, i, id);
        String str = this.activityNames[i];
        try {
            startActivity(new Intent(this, Class.forName
                    ("com.example.BillSplitApp." + str)));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}