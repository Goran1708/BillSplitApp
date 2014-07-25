package com.example.BillSplitApp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Accounting extends Activity implements View.OnClickListener {

    Integer amountOfDays;
    String personName;
    static int peopleListSize;
    static int arrayElementPosition;
    static List<Integer> daysInApartment = new ArrayList<Integer>();
    static List<String> peopleList = new ArrayList();
    Button addPerson;
    TextView addPersonName;
    ListView listOfPeople;
    ArrayAdapterItem peopleArrayAdapter;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.accounting);
        initializeVariables();
        listOfPeople.setAdapter(peopleArrayAdapter);
        grabExtras();
    }

    private void grabExtras(){
        if(getIntent().getExtras() != null) {
            amountOfDays = getIntent().getExtras().getInt("key");
            daysInApartment.set(arrayElementPosition, amountOfDays);
        }
    }

    private void initializeVariables() {
        listOfPeople = ((ListView)findViewById(R.id.lVPeople));
        peopleArrayAdapter = new ArrayAdapterItem
                (this, android.R.layout.simple_list_item_1, peopleList);
        addPersonName = ((TextView)findViewById(R.id.eTPersonName));
        addPerson = ((Button)findViewById(R.id.bAddPerson));
        addPerson.setOnClickListener(this);

        listOfPeople.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
                Intent localIntent = new Intent(Accounting.this, Parameters.class);
                arrayElementPosition = i;
                localIntent.putExtra("arrayKey", (Integer)daysInApartment.get(i));
                localIntent.putExtra("personName", peopleList.get(i));
                Accounting.this.startActivity(localIntent);
            }
        });

        listOfPeople.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long id)
            {
                final int position = i;
                arrayElementPosition = i;
                AlertDialog alertDialog = new AlertDialog.Builder(Accounting.this).create();
                alertDialog.setTitle("Delete person");
                alertDialog.setMessage("Do you want to delete " + peopleList.get(arrayElementPosition) + "?");

                alertDialog.setButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        peopleList.remove(position);
                        listOfPeople.setAdapter(peopleArrayAdapter);
                        peopleListSize = peopleListSize - 1;
                    }
                });
                alertDialog.setButton2("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog.show();
                return false;
            }
        });
    }

    @Override
    public void onClick(View view) {
        personName = addPersonName.getText().toString();
        peopleList.add(personName);
        listOfPeople.setAdapter(peopleArrayAdapter);
        daysInApartment.add(peopleListSize, Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH));
        peopleListSize = 1 + peopleListSize;
    }

    @Override
    public void onPause() {
        super.onPause();
        finish();
    }
}