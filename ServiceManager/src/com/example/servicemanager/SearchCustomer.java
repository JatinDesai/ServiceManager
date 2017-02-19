package com.example.servicemanager;

import static com.example.servicemanager.Constants.*;
import android.text.Editable;
import android.text.TextWatcher;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import com.example.servicemanager.Customer;
import com.example.servicemanager.DBHelper;
import com.example.servicemanager.ListViewAdapter;
import com.example.servicemanager.LoginActivity;
import com.example.servicemanager.SearchCustomer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;

public class SearchCustomer extends ActionBarActivity {

  ListView list;
  ListViewAdapter adapter;
  EditText editSearch;
  ArrayList<String> name = new ArrayList<String>();
  ArrayList<String> address = new ArrayList<String>();
  ArrayList<String> mobile = new ArrayList<String>();
  ArrayList<Customer> arraylist = new ArrayList<Customer>();

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.listview_main);

    Intent intent = getIntent();

    DBHelper mydb = new DBHelper(this);
    List<Customer> customers = new ArrayList<Customer>();

    // Locate the EditText in listview_main.xml
    editSearch = (EditText) findViewById(R.id.search);

    final String calledFrom = intent.getStringExtra(CALLED_FROM);

    editSearch.setHint("Search Customer Information");
    if (calledFrom.equals(SEARCH_CUSTOMER)) {
      customers = mydb.getAllCustomers();
    } else if (calledFrom.equals(DUE_SERVICES)) {
      try {
        customers = mydb.getDueServices();
      } catch (ParseException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    } else if (calledFrom.equals(UPCOMING_SERVICES)) {
      try {
        // TODO need to write logic
        customers = mydb.getUpcomingServices();
      } catch (ParseException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }

    Iterator itr = customers.listIterator();
    while (itr.hasNext()) {
      Customer customer = (Customer) itr.next();
      name.add(customer.getName());
      address.add(customer.getAddress());
      mobile.add(String.valueOf(customer.getContactNo()));
    }

    // Locate the ListView in listview_main.xml
    list = (ListView) findViewById(R.id.listview);

    // Pass results to ListViewAdapter Class
    adapter = new ListViewAdapter(this, customers);

    // Binds the Adapter to the ListView
    list.setAdapter(adapter);

    // Capture Text in EditText
    editSearch.addTextChangedListener(new TextWatcher() {

      @Override
      public void afterTextChanged(Editable arg0) {
        // TODO Auto-generated method stub
        String text = editSearch.getText().toString().toLowerCase(Locale.getDefault());
        adapter.filter(text, calledFrom);
      }

      @Override
      public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        // TODO Auto-generated method stub
      }

      @Override
      public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        // TODO Auto-generated method stub
      }
    });
  }

  /*// Not using options menu in this tutorial
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }*/

  @Override
  public void onBackPressed() {
    Intent MainActivityIntent = new Intent(SearchCustomer.this, MainActivity.class);
    startActivity(MainActivityIntent);
    super.onBackPressed();
  }
}
