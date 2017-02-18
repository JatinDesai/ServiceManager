package com.example.servicemanager;

import android.support.v7.app.ActionBarActivity;

import static com.example.servicemanager.Constants.CUSTOMER;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class ManageProducts extends ActionBarActivity {
  DBHelper smDevDb;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_manage_products);

    smDevDb = new DBHelper(this);
    ArrayAdapter<String> adapter =
        new ArrayAdapter<String>(this, R.layout.activity_listview, smDevDb.getProducts());

    ListView listView = (ListView) findViewById(R.id.product_list);
    listView.setAdapter(adapter);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.manage_products, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.btnAddProduct:
        addProduct();
        return true;

      default:
        return super.onOptionsItemSelected(item);
    }
  }

  private void addProduct() {
    Intent intent = new Intent(getApplicationContext(), NewProduct.class);
    startActivity(intent);
  }
}
