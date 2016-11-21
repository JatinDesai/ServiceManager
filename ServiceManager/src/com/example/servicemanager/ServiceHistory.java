package com.example.servicemanager;

import static com.example.servicemanager.Constants.*;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TableRow.LayoutParams;

public class ServiceHistory extends ActionBarActivity {

	private DBHelper mydb = new DBHelper(this);
	Customer customer = null;
	List<String> serviceNos = new ArrayList<String>();
	List<String> serviceDates = new ArrayList<String>();
	TableLayout tl;
	TableRow tr;
	TextView serviceNo, serviceDate;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_service_history);
		tl = (TableLayout) findViewById(R.id.maintable);

		Intent i = getIntent();
		customer = (Customer) i.getParcelableExtra(CUSTOMER);
		List<List<String>> services = mydb.getServiceHistory(customer.getId());
		serviceNos = services.get(0);
		serviceDates = services.get(1);
		
		addHeaders();
		addData();
	}

	/** This function add the headers to the table **/
	@SuppressWarnings("deprecation")
	public void addHeaders() {

		/** Create a TableRow dynamically **/
		tr = new TableRow(this);
		tr.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

		/** Creating a TextView to add to the row **/
		TextView serviceNo = new TextView(this);
		serviceNo.setText(SERVICE_NO);
		serviceNo.setTextColor(Color.BLACK);
		serviceNo.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
		serviceNo.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		serviceNo.setPadding(5, 5, 5, 0);
		tr.addView(serviceNo); // Adding textView to tablerow.

		/** Creating another textview **/
		TextView serviceDate = new TextView(this);
		serviceDate.setText(SERVICE_DATE);
		serviceDate.setTextColor(Color.BLACK);
		serviceDate.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		serviceDate.setPadding(5, 5, 5, 0);
		serviceDate.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
		tr.addView(serviceDate); // Adding textView to tablerow.

		// Add the TableRow to the TableLayout
		tl.addView(tr, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

		// we are adding two textviews for the divider because we have two
		// columns
		tr = new TableRow(this);
		tr.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

		/** Creating another textview **/
		TextView divider = new TextView(this);
		divider.setText("-------------");
		divider.setTextColor(Color.BLUE);
		divider.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		divider.setPadding(5, 0, 0, 0);
		divider.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
		tr.addView(divider); // Adding textView to tablerow.

		TextView divider2 = new TextView(this);
		divider2.setText("---------------");
		divider2.setTextColor(Color.BLUE);
		divider2.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		divider2.setPadding(5, 0, 0, 0);
		divider2.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
		tr.addView(divider2); // Adding textView to tablerow.

		// Add the TableRow to the TableLayout
		tl.addView(tr, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
	}

	/** This function add the data to the table **/
	@SuppressWarnings("deprecation")
	public void addData() {

		for (int i = 0; i < serviceNos.size(); i++) {
			/** Create a TableRow dynamically **/
			tr = new TableRow(this);
			tr.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

			/** Creating a TextView to add to the row **/
			serviceNo = new TextView(this);
			serviceNo.setText(serviceNos.get(i));
			serviceNo.setTextColor(Color.GRAY);
			serviceNo.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
			serviceNo.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			serviceNo.setPadding(50, 5, 5, 5);
			tr.addView(serviceNo); // Adding textView to tablerow.

			/** Creating another textview **/
			serviceDate = new TextView(this);
			serviceDate.setText(serviceDates.get(i));
			serviceDate.setTextColor(Color.GRAY);
			serviceDate.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			serviceDate.setPadding(15, 5, 5, 5);
			serviceDate.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
			tr.addView(serviceDate); // Adding textView to tablerow.

			// Add the TableRow to the TableLayout
			tl.addView(tr, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		}
	}
}