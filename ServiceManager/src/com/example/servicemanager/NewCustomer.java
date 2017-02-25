package com.example.servicemanager;

import static com.example.servicemanager.Constants.*;
import static com.example.servicemanager.Utils.*;
import java.util.Calendar;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class NewCustomer extends ActionBarActivity // implements TextWatcher
{
  private DBHelper smDevDb = new DBHelper(this);

  TextView name;
  TextView address;
  TextView contactNo;
  TextView emailId;
  TextView productName;
  TextView productModelNo;
  TextView productPrice;
  TextView sellingDate;
  TextView serviceDuration;
  TextView maxServices;
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
    serviceDuration = (TextView) findViewById(R.id.editTextServiceDuration);
    maxServices = (TextView) findViewById(R.id.editTextMaxServices);
    calendar = Calendar.getInstance();
    year = calendar.get(Calendar.YEAR);
    month = calendar.get(Calendar.MONTH);
    day = calendar.get(Calendar.DAY_OF_MONTH);
    showDate(year, month, day);
    serviceDuration.setText((CharSequence) smDevDb.getGlobalParam(SERVICE_DURATION));
    maxServices.setText((CharSequence) smDevDb.getGlobalParam(MAX_SERVICES));
  

    sellingDate.setFocusable(false);
    sellingDate.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        setDate(view);
      }
    });

    Intent i = getIntent();
    customer = (Customer) i.getParcelableExtra(CUSTOMER);
    if (customer != null) {
      int id = customer.getId();

      if (id > 0) {
        // means this is the view part not the add contact part.
        String currentName = customer.getName();
        String currentAddress = customer.getAddress();
        String currentContactNo = customer.getContactNo();
        String currentEmailId = customer.getEmailId();
        String currentProductName = customer.getProductName();
        String currentProductModelNo = customer.getProductModelNo();
        int currentProductPrice = customer.getProductPrice();
        String currentSellingDate = customer.getSellingDate();
        int currentServiceDuration = customer.getServiceDuration();
        int currentMaxServices = customer.getMaxServices();

        name.setText((CharSequence) currentName);
        address.setText((CharSequence) currentAddress);
        contactNo.setText((CharSequence) String.valueOf(currentContactNo));
        emailId.setText((CharSequence) currentEmailId);
        productName.setText((CharSequence) currentProductName);
        productModelNo.setText((CharSequence) currentProductModelNo);
        productPrice.setText((CharSequence) String.valueOf(currentProductPrice));
        sellingDate.setText((CharSequence) currentSellingDate);
        serviceDuration.setText((CharSequence) String.valueOf(currentServiceDuration));
        maxServices.setText((CharSequence) String.valueOf(currentMaxServices));

        final Button button = (Button) findViewById(R.id.btnSave);
        button.setOnClickListener(new View.OnClickListener() {
          public void onClick(View v) {
            updateCustomerObject(customer);
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

  private void updateCustomerObject(Customer customer) {
    customer.setName(name.getText().toString());
    customer.setAddress(address.getText().toString());
    customer.setContactNo(contactNo.getText().toString());
    customer.setEmailId(emailId.getText().toString());
    customer.setProductName(productName.getText().toString());
    customer.setProductModelNo(productModelNo.getText().toString());
    customer.setProductPrice(Integer.valueOf(productPrice.getText().toString()));
    customer.setSellingDate(sellingDate.getText().toString());
    customer.setServiceDuration(Integer.valueOf(serviceDuration.getText().toString()));
    customer.setMaxServices(Integer.valueOf(maxServices.getText().toString()));
  }
  public void insertUpdateCustomer(View view) {
    boolean validationResult = validateCustomer();
    if (validationResult) {
      if (customer != null && customer.getId() != 0) {
        smDevDb.updateCustomerData(customer);
      } else {
        Customer customer = new Customer();
        updateCustomerObject(customer);
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
    String addressV = address.getText().toString();    String contact = contactNo.getText().toString();
    String eId = emailId.getText().toString();
    String productNameV = productName.getText().toString();
    String modelV = productModelNo.getText().toString();
    String priceV = productPrice.getText().toString();
    String sellingDateV = sellingDate.getText().toString();
    String serviceDurationV = productPrice.getText().toString();
    String maxServicesV = productPrice.getText().toString();
    boolean validationResult = false;

    if (nameV.length() == 0) {
      name.setError("Name is required!");
    } else if (addressV.length() == 0) {
      address.setError("Address is required!");
    } else if (contact.length() == 0 || !Patterns.PHONE.matcher(contact).matches()) {
      contactNo.setError("Invalid Mobile No!");
    } else if (eId.length() != 0 && !Patterns.EMAIL_ADDRESS.matcher(eId).matches()) {
      emailId.setError("Invalid email Id!");
    } else if (productNameV.length() == 0) {
      productName.setError("Product Name is required!");
    } else if (modelV.length() == 0) {
      productModelNo.setError("Model No is required!");
    } else if (priceV.length() == 0) {
      productPrice.setError("Price is required!");
    } else if (sellingDateV.length() == 0) {
      sellingDate.setError("Selling Date is required!");
    } else if (serviceDurationV.length() == 0) {
      serviceDuration.setError("Service Duration is required!");
    } else if (maxServicesV.length() == 0) {
      maxServices.setError("Max Services is required!");
    } else {
      validationResult = true;
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
