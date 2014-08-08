package com.example.BillSplitApp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Parameters extends Activity {

	Integer days;
	String name;
	private BillCheckBoxAdapter listAdapter;
	private BillItems[] billItems;
	ArrayList<BillItems> billList = new ArrayList<BillItems>();
	ArrayList<String> checked = new ArrayList<String>();
	LinkedHashMap<String, List<BillItems>> peopleMapBillItems = new LinkedHashMap<String, List<BillItems>>();
	EditText amountOfDays;
	TextView currentValue, personName;
	Button saveData;
	ListView billCheckBox;

	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.parameters);
		grabExtras();
		if (getIntent().getStringExtra("mapBillItems") != null) {
			String str=  getIntent().getStringExtra("mapBillItems");
			Gson gson = new Gson();
			Type entityType = new TypeToken< LinkedHashMap<String, List<BillItems>>>(){}.getType();
			peopleMapBillItems = gson.fromJson(str, entityType); 
			System.out.println("Its working 0");
		}
		initializeVariables();
		setExtras();
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("UseValueOf")
	private void initializeVariables() {
		amountOfDays = ((EditText) findViewById(R.id.eTDaysInMonth));
		currentValue = ((TextView) findViewById(R.id.tVCurrentValue));
		personName = ((TextView) findViewById(R.id.tVPersonName));
		saveData = ((Button) findViewById(R.id.bSaveData));
		saveData.setOnClickListener(new View.OnClickListener() // Save button
																// opens dialog
		{
			public void onClick(View paramAnonymousView) {
				AlertDialog alertDialog = new AlertDialog.Builder(
						Parameters.this).create();
				alertDialog.setTitle("Save data");
				String str = amountOfDays.getText().toString(); // duplicate 1
				if (str.isEmpty()) {
					alertDialog.setMessage("Do you want to save empty value?");
				} else {
					alertDialog.setMessage("Do you want to save " + str + "?");
				}
				alertDialog.setButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								String str = amountOfDays.getText().toString(); // duplicate
																				// 2
								Calendar c = Calendar.getInstance();
								Integer monthMaxDays = new Integer(
										c.getActualMaximum(Calendar.DAY_OF_MONTH));
								if (!(str.equals(""))) {
									if (Integer.parseInt(str) <= monthMaxDays
											&& Integer.parseInt(str) >= 0) {
										Intent localIntent = new Intent(
												Parameters.this,
												Accounting.class);
										localIntent
												.putParcelableArrayListExtra(
														"billListBoolean",
														billList);
										localIntent.putExtra("key",
												Integer.parseInt(str));

										startActivity(localIntent);
									} else {
										Toast.makeText(
												getBaseContext(),
												"Enter amount of days between 0 and "
														+ monthMaxDays,
												Toast.LENGTH_SHORT).show();
									}
								} else {
									Toast.makeText(getBaseContext(),
											"You didn't enter any value",
											Toast.LENGTH_SHORT).show();
								}
							}
						});
				alertDialog.setButton2("No",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.cancel();
							}
						});
				alertDialog.show();
			}
		});

		billCheckBox = ((ListView) findViewById(R.id.lVBillCheckBox));
		billCheckBox
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View item,
							int position, long id) {
						BillItems planet = listAdapter.getItem(position);
						planet.toggleChecked();
						SelectViewHolder viewHolder = (SelectViewHolder) item
								.getTag();
						viewHolder.getCheckBox().setChecked(planet.isChecked());
					}
				});

		billItems = (BillItems[]) getLastNonConfigurationInstance();

		billList.add(new BillItems("Rent"));
		billList.add(new BillItems("TV"));
		billList.add(new BillItems("Electricity/Gas/Heat"));
		billList.add(new BillItems("Water/Sewer/Garbage"));
		billList.add(new BillItems("Internet"));
		billList.add(new BillItems("Phone"));
		billList.add(new BillItems("Groceries"));
		billList.add(new BillItems("Metro"));
		billList.add(new BillItems("Car"));
		billList.add(new BillItems("Car insurance"));
		billList.add(new BillItems("Car gas"));
		billList.add(new BillItems("Entertainment"));
		billList.add(new BillItems("Parking"));

		List<BillItems> valueList = new ArrayList<BillItems>();
		System.out.println(name);
		System.out.println(peopleMapBillItems.keySet());
		if (peopleMapBillItems.get(name) != null) {
			System.out.println("Its working 1");
			if (peopleMapBillItems.containsKey(name)) {
				System.out.println("Its working 2");
				for (List<BillItems> billItemValues : peopleMapBillItems
						.values()) {
					for (BillItems value : billItemValues) {
						valueList.add(value);
					}
				}
			}
		}
		for (int i = 0; i < valueList.size(); i++) {
			System.out.println(valueList.get(i).isChecked());
		}

		if (!(valueList.isEmpty())) {
			for (int i = 0; i < billList.size(); i++) {
				billList.set(i, valueList.get(i));
				System.out.println(billList.get(i).isChecked());
			}
		}

		// Set our custom array adapter as the ListView's adapter.
		listAdapter = new BillCheckBoxAdapter(this, billList);
		billCheckBox.setAdapter(listAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 1, Menu.NONE, "Bills");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 1:
			for (int i = 0; i < checked.size(); i++) {
				Log.d("pos : ", "" + checked.get(i));
			}
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void grabExtras() {
		days = getIntent().getExtras().getInt("arrayKey");
		name = getIntent().getExtras().getString("personName");
	}
	
	private void setExtras() {
		currentValue.setText("Your current value is " + days);
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

	public Object onRetainNonConfigurationInstance() {
		return billItems;
	}
}