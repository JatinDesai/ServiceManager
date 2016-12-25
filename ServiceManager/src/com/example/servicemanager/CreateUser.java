package com.example.servicemanager;

import android.support.v7.app.ActionBarActivity;

import static com.example.servicemanager.Constants.PASSWORD;
import static com.example.servicemanager.Constants.USER_NAME;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CreateUser extends ActionBarActivity {
	private DBHelper smDevDb;

	Button registerBtn;
	EditText userName, password;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_user);

		smDevDb = new DBHelper(this);

		registerBtn = (Button) findViewById(R.id.btnRegister);
		userName = (EditText) findViewById(R.id.etUserName);
		password = (EditText) findViewById(R.id.etPassword);
		registerBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				insertCredentials();
				login();
			}
		});
	}

	public void login() {
			Intent in = new Intent(getApplicationContext(), com.example.servicemanager.MainActivity.class);
			startActivity(in);
	}

	public void insertCredentials() {
		smDevDb.updateGlobalParam(USER_NAME, userName.getText().toString(), null);
		smDevDb.updateGlobalParam(PASSWORD, password.getText().toString(), null);
	}
}
