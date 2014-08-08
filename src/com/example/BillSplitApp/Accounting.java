package com.example.BillSplitApp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import com.google.gson.Gson;

public class Accounting extends Activity implements View.OnClickListener {

	Integer amountOfDays;
	static float billAmount;
	String personName;
	static int peopleListSize;
	static int arrayElementPosition;
	static List<Integer> daysInApartment = new ArrayList<Integer>();
	static List<String> peopleList = new ArrayList<String>();
	static LinkedHashMap<String, ArrayList<BillItems>> peopleMapBillItems = new LinkedHashMap<String, ArrayList<BillItems>>();
	static LinkedHashMap<String, Float> mapOfBillValues = new LinkedHashMap<String, Float>();
	static ArrayList<BillItems> listOfBills = new ArrayList<BillItems>();
	Button addPerson, calculate, addBill;
	TextView addPersonName;
	ListView listOfPeople;
	PeopleArrayAdapter peopleArrayAdapter;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.accounting);
		initializeVariables();
		listOfPeople.setAdapter(peopleArrayAdapter);
		grabExtras();
	}

	private void initializeVariables() {
		listOfPeople = ((ListView) findViewById(R.id.lVPeople));
		peopleArrayAdapter = new PeopleArrayAdapter(this,
				android.R.layout.simple_list_item_1, peopleList);
		addPersonName = ((TextView) findViewById(R.id.eTPersonName));
		addPerson = ((Button) findViewById(R.id.bAddPerson));
		calculate = ((Button) findViewById(R.id.bCalculate));
		addBill = ((Button) findViewById(R.id.bAddBill));
		addPerson.setOnClickListener(this);
		calculate.setOnClickListener(this);
		addBill.setOnClickListener(this);

		if (mapOfBillValues.isEmpty()) {
			String[] billArray = getResources().getStringArray(
					R.array.planets_array);
			int billArrayLength = billArray.length;
			for (int i = 0; i < billArrayLength; i++) {
				mapOfBillValues.put(billArray[i], 0.0f);
			}
		}

		listOfPeople
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					public void onItemClick(AdapterView<?> adapterView,
							View view, int i, long id) {
						Intent localIntent = new Intent(Accounting.this,
								Parameters.class);
						arrayElementPosition = i;
						localIntent.putExtra("arrayKey",
								(Integer) daysInApartment.get(i));
						localIntent.putExtra("personName", peopleList.get(i));
						if(peopleMapBillItems.get(peopleList
												.get(i)) != null) {
							if(peopleMapBillItems.containsKey(peopleList
												.get(i))) {
								Gson gson = new Gson();
								String list = gson.toJson(peopleMapBillItems);
								localIntent.putExtra("mapBillItems", list);
							}
						}
						Accounting.this.startActivity(localIntent);
					}
				});

		listOfPeople
				.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
					@SuppressWarnings("deprecation")
					public boolean onItemLongClick(AdapterView<?> adapterView,
							View view, int i, long id) {
						final int position = i;
						arrayElementPosition = i;
						AlertDialog alertDialog = new AlertDialog.Builder(
								Accounting.this).create();
						alertDialog.setTitle("Delete person");
						alertDialog.setMessage("Do you want to delete "
								+ peopleList.get(arrayElementPosition) + "?");

						alertDialog.setButton("Yes",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										peopleMapBillItems.remove(peopleList
												.get(position));
										peopleList.remove(position);
										daysInApartment.remove(position);
										listOfPeople
												.setAdapter(peopleArrayAdapter);
										peopleListSize = peopleListSize - 1;
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
						return false;
					}
				});
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.bAddPerson:
			personName = addPersonName.getText().toString();
			peopleList.add(personName);
			peopleMapBillItems.put(personName, null);
			listOfPeople.setAdapter(peopleArrayAdapter);
			daysInApartment.add(peopleListSize, Calendar.getInstance()
					.getActualMaximum(Calendar.DAY_OF_MONTH));
			peopleListSize = 1 + peopleListSize;
			break;
		case R.id.bAddBill:
			/*
			 * AlertDialog alertDialog = new
			 * AlertDialog.Builder(Accounting.this) .create();
			 * alertDialog.setTitle("Add bill"); final EditText input = new
			 * EditText(this); input.setText("0.0",
			 * TextView.BufferType.EDITABLE);
			 * input.setRawInputType(InputType.TYPE_CLASS_NUMBER |
			 * InputType.TYPE_NUMBER_FLAG_DECIMAL); alertDialog.setView(input);
			 * alertDialog.setButton2("Save bill amount", new
			 * DialogInterface.OnClickListener() { public void onClick(final
			 * DialogInterface dialog, final int which) { billAmount =
			 * Float.parseFloat(input.getText() .toString()); dialog.cancel(); }
			 * }); alertDialog.setButton("Exit", new
			 * DialogInterface.OnClickListener() { public void onClick(final
			 * DialogInterface dialog, final int which) { dialog.cancel(); } });
			 * alertDialog.show(); break;
			 */

			final Dialog billDialog = new Dialog(Accounting.this);
			// tell the Dialog to use the dialog.xml as it's layout description
			billDialog.setContentView(R.layout.bill_dialog);
			billDialog.setTitle("Add bill");
			final Spinner spinner = (Spinner) billDialog
					.findViewById(R.id.spinner1);
			ArrayAdapter<CharSequence> adapter = ArrayAdapter
					.createFromResource(this, R.array.planets_array,
							android.R.layout.simple_spinner_item);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner.setAdapter(adapter);

			final EditText inputBillAmount = (EditText) billDialog
					.findViewById(R.id.eTBillAmount);
			inputBillAmount.setText("0.0", TextView.BufferType.EDITABLE);

			// / Button to show Bills and their values
			Button showBill = (Button) billDialog.findViewById(R.id.bShowBill);
			showBill.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					final Dialog showBillDialog = new Dialog(Accounting.this);
					showBillDialog.setContentView(R.layout.calculatedialog);
					showBillDialog.setTitle("Final bill calculation");
					showBillDialog.setCancelable(true);

					TextView text = (TextView) showBillDialog
							.findViewById(R.id.tVIndividualBill);
					for (HashMap.Entry<String, Float> entry : mapOfBillValues
							.entrySet()) {
						if (!(text.getText().equals("false"))) {
							text.setText(text.getText() + "\n" + entry.getKey()
									+ " : " + entry.getValue());
						} else {
							text.setText(entry.getKey() + " : "
									+ entry.getValue());
						}
					}

					Button button = (Button) showBillDialog
							.findViewById(R.id.bCancel);
					button.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							showBillDialog.cancel();
						}
					});
					showBillDialog.show();
				}
			});

			// /Saves Bill value
			Button billSave = (Button) billDialog.findViewById(R.id.bSaveBill);
			billSave.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mapOfBillValues.put(spinner.getSelectedItem().toString(),
							Float.parseFloat(inputBillAmount.getText()
									.toString()));
					billAmount = 0;
					for (HashMap.Entry<String, Float> entry : mapOfBillValues
							.entrySet()) {
						billAmount = billAmount + entry.getValue().floatValue();
					}
				}
			});

			// /Exit Bill dialog
			Button exitBill = (Button) billDialog.findViewById(R.id.bExitBill);
			exitBill.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					billDialog.cancel();
				}
			});
			billDialog.show();
			break;
		case R.id.bCalculate:
			if (billAmount >= 0 && !(daysInApartment.isEmpty())) {
				final Dialog dialogCalc = new Dialog(Accounting.this);
				dialogCalc.setContentView(R.layout.calculatedialog);
				dialogCalc.setTitle("Final bill calculation");
				dialogCalc.setCancelable(true);

				// set up text
				TextView text = (TextView) dialogCalc
						.findViewById(R.id.tVIndividualBill);
				float coeficient = 0;
				float amountPersonPays;
				for (int i = 0; i < daysInApartment.size(); i++) {
					coeficient = coeficient + daysInApartment.get(i);
				}

				for (int i = 0; i < peopleList.size(); i++) {
					amountPersonPays = (daysInApartment.get(i).intValue()
							/ coeficient * billAmount);

					if (!(text.getText().equals("false"))) {
						text.setText(text.getText() + "\n" + peopleList.get(i)
								+ " has to pay " + amountPersonPays);
					} else {
						text.setText(peopleList.get(i) + " has to pay "
								+ amountPersonPays);
					}
				}

				// set up button
				Button button = (Button) dialogCalc.findViewById(R.id.bCancel);
				button.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialogCalc.cancel();
					}
				});
				dialogCalc.show();
			} else {
				Toast.makeText(getBaseContext(),
						"You didn't enter bill amount or people",
						Toast.LENGTH_SHORT).show();
			}
			break;
		}
	}

	private void grabExtras() {
		if (getIntent().getExtras() != null) {
			amountOfDays = getIntent().getExtras().getInt("key");
			daysInApartment.set(arrayElementPosition, amountOfDays);
			listOfBills = getIntent().getParcelableArrayListExtra(
					"billListBoolean");
			peopleMapBillItems.put(peopleList.get(arrayElementPosition), listOfBills);
			for (int i = 0; i < listOfBills.size(); i++) {
				System.out.println(listOfBills.get(i).getName() + "  "
						+ listOfBills.get(i).isChecked());
			}
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		finish();
	}
}