package com.example.servicemanager;

import static com.example.servicemanager.Constants.*;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {
	private DBHelper smDevDb;

	Button loginBtn;
	EditText userName, password;
	TextView forgotPwLink;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity);

		smDevDb = new DBHelper(this);

		loginBtn = (Button) findViewById(R.id.btnLogin);
		userName = (EditText) findViewById(R.id.etUserName);
		String user = smDevDb.getGlogbalParm(USER_NAME);
		if (user.isEmpty()) {
			createUser();
		} else {
			userName.setText((CharSequence)user);
		}
		userName.setEnabled(false);
		password = (EditText) findViewById(R.id.etPassword);
		forgotPwLink = (TextView) findViewById(R.id.tvForgotPw);
		loginBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				login();
			}
		});

		forgotPwLink.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO send password to registered email id and display message
				login(); // temporory to test the link
			}
		});
	}

	public void createUser() {
		Intent in = new Intent(getApplicationContext(), com.example.servicemanager.CreateUser.class);
		startActivity(in);
	}

	public void login() {
		if (validateLogin()) {
			Intent in = new Intent(getApplicationContext(), com.example.servicemanager.MainActivity.class);
			startActivity(in);
		} else {
			Toast.makeText(getApplicationContext(), "Wrong Credentials", Toast.LENGTH_SHORT).show();
		}
	}

	public boolean validateLogin() {
		if (password.getText().toString().equals(smDevDb.getGlogbalParm(PASSWORD))) {
			return true;
		}
		return false;
	}
}