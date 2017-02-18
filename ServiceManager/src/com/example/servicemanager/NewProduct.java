package com.example.servicemanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class NewProduct extends ActionBarActivity {
  private DBHelper smDevDb;

  TextView name;

  @Override
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_new_product);
    name = (TextView) findViewById(R.id.editTextProductName);

    smDevDb = new DBHelper(this);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    if (id == R.id.action_settings) {
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  public void insertUpdateProduct(View view) {
    boolean validationResult = validateProduct();
    if (validationResult) {
      smDevDb.insertProduct(name.getText().toString());
      Intent intent = new Intent(getApplicationContext(), ManageProducts.class);
      startActivity(intent);
    }
  }

  public boolean validateProduct() {
    String nameV = name.getText().toString();
    boolean validationResult = false;
    if (nameV.length() == 0) {
      name.setError("Name is required!");
    } else {
      if (smDevDb.isProductExists(nameV)) {
        name.setError("Product already exists.");
      } else {
              validationResult = true;
      }
    }
    return validationResult;
  }
}
