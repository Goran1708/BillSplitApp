package com.example.BillSplitApp;

import android.app.Activity;
import android.os.Bundle;

public class Exit extends Activity {

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        finish();
        System.exit(0);
    }
}