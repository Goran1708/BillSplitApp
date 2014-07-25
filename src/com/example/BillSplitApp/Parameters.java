package com.example.BillSplitApp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;

public class Parameters extends Activity {

    Integer days;
    String name;
    EditText amountOfDays;
    TextView currentValue, personName;
    Button saveData;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.parameters);
        initializeVariables();
        grabExtras();
    }

    private void initializeVariables() {
        amountOfDays = ((EditText)findViewById(R.id.eTDaysInMonth));
        currentValue = ((TextView)findViewById(R.id.tVCurrentValue));
        personName = ((TextView)findViewById(R.id.tVPersonName));
        saveData = ((Button)findViewById(R.id.bSaveData));
        saveData.setOnClickListener(new View.OnClickListener() //Save button opens dialog
        {
            public void onClick(View paramAnonymousView)
            {
                AlertDialog alertDialog = new AlertDialog.Builder(Parameters.this).create();
                alertDialog.setTitle("Save data");
                String str = amountOfDays.getText().toString(); //duplicate 1
                if (str.isEmpty()) {
                    alertDialog.setMessage("Do you want to save empty value?");
                } else {
                    alertDialog.setMessage("Do you want to save " + str + "?");
                }
                alertDialog.setButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String str = amountOfDays.getText().toString(); //duplicate 2
                        Calendar c = Calendar.getInstance();
                        Integer monthMaxDays = new Integer(c.getActualMaximum(Calendar.DAY_OF_MONTH));

                        if(Integer.parseInt(str) <= monthMaxDays && Integer.parseInt(str) > 0) {
                            Intent localIntent = new Intent(Parameters.this, Accounting.class);
                            localIntent.putExtra("key", Integer.parseInt(str));
                            startActivity(localIntent);
                        }
                    }
                });
                alertDialog.setButton2("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog.show();
            }
        });
    }

    private void grabExtras(){
        days = getIntent().getExtras().getInt("arrayKey");
        currentValue.setText("Your current value is " + days);
        name = getIntent().getExtras().getString("personName");
        personName.setText("Enter amount of days for " + name);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent localIntent = new Intent(Parameters.this, Accounting.class);
        startActivity(localIntent);
    }
}
