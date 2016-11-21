package com.example.servicemanager;

import static com.example.servicemanager.Constants.*;
import static com.example.servicemanager.Utils.*;
import java.util.Calendar;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

public class NewCustomer extends ActionBarActivity // implements TextWatcher
{
	private DBHelper mydb;

	TextView name;
	TextView address;
	TextView mobile;
	TextView model;
	TextView price;
	TextView sellingDate;
	Customer customer = new Customer();

	private Calendar calendar;
	private int year, month, day;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_customer);
		name = (TextView) findViewById(R.id.editTextName);
		address = (TextView) findViewById(R.id.editTextAddress);
		mobile = (TextView) findViewById(R.id.editTextMobile);
		model = (TextView) findViewById(R.id.editTextModel);
		price = (TextView) findViewById(R.id.editTextPrice);

		sellingDate = (TextView) findViewById(R.id.editTextDate);
		calendar = Calendar.getInstance();
		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH);
		day = calendar.get(Calendar.DAY_OF_MONTH);
		showDate(year, month, day);

		mydb = new DBHelper(this);

		Intent i = getIntent();
		customer = (Customer) i.getParcelableExtra(CUSTOMER);
		if (customer != null) {
			int id = customer.getId();

			if (id > 0) {
				// means this is the view part not the add contact part.
				String nam = customer.getName();
				String add = customer.getAddress();
				String mobil = customer.getMobile();
				String mode = customer.getModel();
				int pric = customer.getPrice();
				String sellingDat = customer.getSellingDate();

				name.setText((CharSequence) nam);
				address.setText((CharSequence) add);
				mobile.setText((CharSequence) String.valueOf(mobil));
				model.setText((CharSequence) mode);
				price.setText((CharSequence) String.valueOf(pric));
				sellingDate.setText((CharSequence) sellingDat);

				final Button button = (Button) findViewById(R.id.btnSave);
				button.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						customer.setName(name.getText().toString());
						customer.setAddress(address.getText().toString());
						customer.setMobile(mobile.getText().toString());
						customer.setModel(model.getText().toString());
						customer.setPrice(Integer.valueOf(price.getText().toString()));
						customer.setSellingDate(sellingDate.getText().toString());
						if (customer.getServiceCount() == 0) {
							customer.setLastService(sellingDate.getText().toString());
						}
						insertUpdateCustomer(v);
					}
				});
			}
		}
	}

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu)
	// {
	// getMenuInflater().inflate(R.menu.display, menu);
	// return true;
	// }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void insertUpdateCustomer(View view) {
		boolean validationResult = validateCustomer();
		if (validationResult) {
			if (customer != null && customer.getId() != 0) {
				mydb.updateCustomerData(customer);
			} else {
				Customer customer = new Customer();
				customer.setName(name.getText().toString());
				customer.setAddress(address.getText().toString());
				customer.setMobile(mobile.getText().toString());
				customer.setModel(model.getText().toString());
				customer.setPrice(Integer.valueOf(price.getText().toString()));
				customer.setSellingDate(sellingDate.getText().toString());
				customer.setLastService(sellingDate.getText().toString());
				customer.setServiceCount(0);

				mydb.insertCustomer(customer);
			}

			Intent intent = new Intent(getApplicationContext(), MainActivity.class);
			startActivity(intent);
		}
	}

	public boolean validateCustomer() {
		String nameV = name.getText().toString();
		String addressV = address.getText().toString();
		String mobileV = mobile.getText().toString();
		String modelV = model.getText().toString();
		String priceV = price.getText().toString();
		boolean validationResult = false;
		if (nameV.length() == 0) {
			name.setError("Name is required!");
		} else {
			if (addressV.length() == 0) {
				address.setError("Address is required!");
			} else {
				if (mobileV.length() != 10) {
					mobile.setError("Invalid Mobile No!");
				} else {
					if (modelV.length() == 0) {
						model.setError("Model No is required!");
					} else {
						if (priceV.length() == 0) {
							price.setError("Price is required!");
						} else {
							validationResult = true;
						}
					}
				}
			}
		}
		return validationResult;
	}

	@SuppressWarnings("deprecation")
	public void setDate(View view) {
		showDialog(999);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		if (id == 999) {
			return new DatePickerDialog(this, myDateListener, year, month, day);
		}
		return null;
	}

	private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
			// arg1 = year
			// arg2 = month
			// arg3 = day
			showDate(arg1, arg2, arg3);
		}
	};

	private void showDate(int year, int month, int day) {
		String date = composeDate(day, month, year);
		sellingDate.setText(date);
	}

	@Override
	public void onBackPressed() {
		Intent MainActivityIntent = new Intent(NewCustomer.this, MainActivity.class);
		startActivity(MainActivityIntent);
		super.onBackPressed();
	}
}
