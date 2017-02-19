package com.example.servicemanager;

import static com.example.servicemanager.Constants.*;
import static com.example.servicemanager.Utils.*;
import java.util.Calendar;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.TextView;

public class CustomerInfo extends ActionBarActivity {
  // Declare Variables
  TextView name;
  TextView address;
  TextView mobile;
  TextView sellingDate;
  TextView serviceCount;
  TextView service2;
  TextView service3;
  TextView service4;
  String displayName;
  String displayAddress;
  String displayMobile;
  String displaySellingDate;
  String displayServiceCount;
  Customer customer;

  private Calendar calendar;
  private int year, month, day;

  private DBHelper smDevDb = new DBHelper(this);

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.singleitemview);
    // Retrieve data from MainActivity on item click event
    Intent i = getIntent();
    customer = (Customer) i.getParcelableExtra(CUSTOMER);
    displayName = customer.getName();
    displayAddress = customer.getAddress();
    displayMobile = String.valueOf(customer.getContactNo());
    displaySellingDate = customer.getSellingDate();
    displayServiceCount = String.valueOf(customer.getTotalServiceCount());

    // Locate the TextViews in singleitemview.xml
    name = (TextView) findViewById(R.id.displayName);
    address = (TextView) findViewById(R.id.displayAddress);
    mobile = (TextView) findViewById(R.id.displayMobile);
    sellingDate = (TextView) findViewById(R.id.displaySellingDate);
    serviceCount = (TextView) findViewById(R.id.displayServiceCount);

    // Load the results into the TextViews
    name.setText(displayName);
    address.setText(displayAddress);
    mobile.setText(displayMobile);
    sellingDate.setText(displaySellingDate);
    serviceCount.setText(displayServiceCount);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.single_item_view, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.btnUpdateCustomer:
        updateCustomer();
        return true;

      case R.id.btnResetServices:
        resetServiceCount();
        return true;

      case R.id.btnDeleteCustomer:
        deleteCustomer();
        return true;

      case R.id.btnServiceDone:
        updateServiceCount();
        return true;

      case R.id.btnServiceHistory:
        displayServiceHistory();
        return true;

      default:
        return super.onOptionsItemSelected(item);
    }
  }

  public void displayServiceHistory() {
    Intent intent =
        new Intent(getApplicationContext(), com.example.servicemanager.ServiceHistory.class);
    intent.putExtra(CUSTOMER, customer);
    startActivity(intent);
  }

  public void updateServiceCount() {

    sellingDate = (TextView) findViewById(R.id.editTextSellingDate);
    calendar = Calendar.getInstance();
    year = calendar.get(Calendar.YEAR);
    month = calendar.get(Calendar.MONTH);
    day = calendar.get(Calendar.DAY_OF_MONTH);

    customer.setTotalServiceCount(customer.getTotalServiceCount() + 1);
    setDate();
  }

  public void resetServiceCount() {

    new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert)
        .setTitle("Reset Services").setMessage("Are you sure you want to Reset Services?")
        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            customer.setTotalServiceCount(0);
            customer.setLastServiceDate(getDateString(Calendar.getInstance()));
            resetServices(customer);
          }

        }).setNegativeButton("No", null).show();
  }

  public void resetServices(Customer customer) {
    smDevDb.deleteServices(customer);
    updateServiceDetail();
  }

  private void updateServiceDetail() {
    smDevDb.updateCustomerData(customer);
    Intent intent =
        new Intent(getApplicationContext(), com.example.servicemanager.CustomerInfo.class);
    intent.putExtra(CUSTOMER, customer);
    startActivity(intent);
  }

  public void updateCustomer() {
    Intent intent =
        new Intent(getApplicationContext(), com.example.servicemanager.NewCustomer.class);
    intent.putExtra(CUSTOMER, customer);
    startActivity(intent);
  }

  public void deleteCustomer() {
    new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert)
        .setTitle("Delete Customer").setMessage("Are you sure you want to delete Customer?")
        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            smDevDb.deleteCustomer(customer);
            launchSearchCustomer();
          }
        }).setNegativeButton("No", null).show();
  }
  
  public void launchSearchCustomer() {
    Intent intent = new Intent(CustomerInfo.this, SearchCustomer.class);
    intent.putExtra(CALLED_FROM, SEARCH_CUSTOMER);
    startActivity(intent);
  }

  @Override
  public void onBackPressed() {
    launchSearchCustomer();
    super.onBackPressed();
  }

  @SuppressWarnings("deprecation")
  public void setDate() {
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

  private DatePickerDialog.OnDateSetListener myDateListener =
      new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
          // TODO Auto-generated method stub
          // arg1 = year
          // arg2 = month
          // arg3 = day
          showDate(arg1, arg2, arg3);
        }
      };

  private void showDate(int year, int month, int day) {
    if (customer == null) {
      customer = new Customer();
    }
    String date = composeDate(day, month, year);
    customer.setLastServiceDate(date);

    serviceDone();
  }

  public void serviceDone() {
    smDevDb.insertService(customer);
    updateServiceDetail();
  }
}
