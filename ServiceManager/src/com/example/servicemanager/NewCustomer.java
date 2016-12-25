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
  private DBHelper smDevDb;

  TextView name;
  TextView address;
  TextView contactNo;
  TextView emailId;
  TextView productName;
  TextView productModelNo;
  TextView productPrice;
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
    emailId = (TextView) findViewById(R.id.editTextEmailId);
    contactNo = (TextView) findViewById(R.id.editTextContactNo);
    productName = (TextView) findViewById(R.id.editTextProductName);
    productModelNo = (TextView) findViewById(R.id.editTextProductModelNo);
    productPrice = (TextView) findViewById(R.id.editTextProductPrice);
    sellingDate = (TextView) findViewById(R.id.editTextSellingDate);
    calendar = Calendar.getInstance();
    year = calendar.get(Calendar.YEAR);
    month = calendar.get(Calendar.MONTH);
    day = calendar.get(Calendar.DAY_OF_MONTH);
    showDate(year, month, day);

    smDevDb = new DBHelper(this);

    Intent i = getIntent();
    customer = (Customer) i.getParcelableExtra(CUSTOMER);
    if (customer != null) {
      int id = customer.getId();

      if (id > 0) {
        // means this is the view part not the add contact part.
        String nam = customer.getName();
        String add = customer.getAddress();
        String conNo = customer.getContactNo();
        String email = customer.getEmailId();
        String proName = customer.getProductName();
        String modelNo = customer.getProductModelNo();
        int pPrice = customer.getProductPrice();
        String sellingDat = customer.getSellingDate();

        name.setText((CharSequence) nam);
        address.setText((CharSequence) add);
        contactNo.setText((CharSequence) String.valueOf(conNo));
        emailId.setText((CharSequence) email);
        productName.setText((CharSequence) proName);
        productModelNo.setText((CharSequence) modelNo);
        productPrice.setText((CharSequence) String.valueOf(pPrice));
        sellingDate.setText((CharSequence) sellingDat);

        final Button button = (Button) findViewById(R.id.btnSave);
        button.setOnClickListener(new View.OnClickListener() {
          public void onClick(View v) {
            customer.setName(name.getText().toString());
            customer.setAddress(address.getText().toString());
            customer.setContactNo(contactNo.getText().toString());
            customer.setEmailId(emailId.getText().toString());
            customer.setProductName(productName.getText().toString());
            customer.setProductModelNo(productModelNo.getText().toString());
            customer.setProductPrice(Integer.valueOf(productPrice.getText().toString()));
            customer.setSellingDate(sellingDate.getText().toString());
            if (customer.getTotalServiceCount() == 0) {
              customer.setLastServiceDate(sellingDate.getText().toString());
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
        smDevDb.updateCustomerData(customer);
      } else {
        Customer customer = new Customer();

        customer.setName(name.getText().toString());
        customer.setAddress(address.getText().toString());
        customer.setContactNo(contactNo.getText().toString());
        customer.setEmailId(emailId.getText().toString());
        customer.setProductName(productName.getText().toString());
        customer.setProductModelNo(productModelNo.getText().toString());
        customer.setProductPrice(Integer.valueOf(productPrice.getText().toString()));
        customer.setSellingDate(sellingDate.getText().toString());
        customer.setLastServiceDate(sellingDate.getText().toString());
        // customer.setNextServiceDate(sellingDate.getText().toString()); // TODO need to check from
        // default data
        customer.setTotalServiceCount(0);

        smDevDb.insertCustomer(customer);
      }

      Intent intent = new Intent(getApplicationContext(), MainActivity.class);
      startActivity(intent);
    }
  }

  // TODO should have generic validation method
  public boolean validateCustomer() {
    String nameV = name.getText().toString();
    String addressV = address.getText().toString();
    String mobileV = contactNo.getText().toString();
    String modelV = productModelNo.getText().toString();
    String priceV = productPrice.getText().toString();
    boolean validationResult = false;
    if (nameV.length() == 0) {
      name.setError("Name is required!");
    } else {
      if (addressV.length() == 0) {
        address.setError("Address is required!");
      } else {
        if (mobileV.length() != 10) {
          contactNo.setError("Invalid Mobile No!");
        } else {
          if (modelV.length() == 0) {
            productModelNo.setError("Model No is required!");
          } else {
            if (priceV.length() == 0) {
              productPrice.setError("Price is required!");
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

  private DatePickerDialog.OnDateSetListener myDateListener =
      new DatePickerDialog.OnDateSetListener() {
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
