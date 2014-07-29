package com.example.BillSplitApp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Accounting extends Activity implements View.OnClickListener {

	Integer amountOfDays;
	static float billAmount;
	String personName;
	static int peopleListSize;
	static int arrayElementPosition;
	static List<Integer> daysInApartment = new ArrayList<Integer>();
	static List<String> peopleList = new ArrayList<String>();
	Button addPerson, calculate, addBill;
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

	private void grabExtras() {
		if (getIntent().getExtras() != null) {
			amountOfDays = getIntent().getExtras().getInt("key");
			daysInApartment.set(arrayElementPosition, amountOfDays);
		}
	}

	private void initializeVariables() {
		listOfPeople = ((ListView) findViewById(R.id.lVPeople));
		peopleArrayAdapter = new ArrayAdapterItem(this,
				android.R.layout.simple_list_item_1, peopleList);
		addPersonName = ((TextView) findViewById(R.id.eTPersonName));
		addPerson = ((Button) findViewById(R.id.bAddPerson));
		calculate = ((Button) findViewById(R.id.bCalculate));
		addBill = ((Button) findViewById(R.id.bAddBill));
		addPerson.setOnClickListener(this);
		calculate.setOnClickListener(this);
		addBill.setOnClickListener(this);

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

	@SuppressWarnings("deprecation")
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.bAddPerson:
			personName = addPersonName.getText().toString();
			peopleList.add(personName);
			listOfPeople.setAdapter(peopleArrayAdapter);
			daysInApartment.add(peopleListSize, Calendar.getInstance()
					.getActualMaximum(Calendar.DAY_OF_MONTH));
			peopleListSize = 1 + peopleListSize;
			break;
		case R.id.bAddBill:
			AlertDialog alertDialog = new AlertDialog.Builder(Accounting.this)
					.create();
			alertDialog.setTitle("Add bill");
			final EditText input = new EditText(this);
			input.setText("0.0", TextView.BufferType.EDITABLE);
			input.setRawInputType(InputType.TYPE_CLASS_NUMBER
					| InputType.TYPE_NUMBER_FLAG_DECIMAL);
			alertDialog.setView(input);
			alertDialog.setButton("Save bill amount",
					new DialogInterface.OnClickListener() {
						public void onClick(final DialogInterface dialog,
								final int which) {
							billAmount = Float.parseFloat(input.getText()
									.toString());
							dialog.cancel();
						}
					});
			alertDialog.show();
			break;
		case R.id.bCalculate:
			if (billAmount >= 0 && !(daysInApartment.isEmpty())) {
				final Dialog dialog = new Dialog(Accounting.this);
				dialog.setContentView(R.layout.calculatedialog);
				dialog.setTitle("Final bill calculation");
				dialog.setCancelable(true);

				// set up text
				TextView text = (TextView) dialog
						.findViewById(R.id.tVIndividualBill);
				float coeficient = 0;
				float amountPersonPays;
				for (int i = 0; i < daysInApartment.size(); i++) {
					coeficient = coeficient + daysInApartment.get(i);
				}

				for (int i = 0; i < peopleList.size(); i++) {
					amountPersonPays = (daysInApartment.get(i).intValue()
							/ coeficient * billAmount);
					
					if(!(text.getText().equals("false"))) {
					text.setText(text.getText() + "\n" + peopleList.get(i)
							+ " has to pay " + amountPersonPays);
					} else {
						text.setText(peopleList.get(i)
								+ " has to pay " + amountPersonPays);
					}
				}

				// set up button
				Button button = (Button) dialog.findViewById(R.id.bCancel);
				button.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.cancel();
					}
				});
				dialog.show();
			} else {
				Toast.makeText(getBaseContext(),
						"You didn't enter bill amount or people",
						Toast.LENGTH_SHORT).show();
			}
			break;
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		finish();
	}
}