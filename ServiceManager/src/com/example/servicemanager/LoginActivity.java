package com.example.servicemanager;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class LoginActivity extends Activity  {
   Button loginBtn;
   EditText userName,password;
   TextView forgotPwLink;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.login_activity);

      loginBtn = (Button)findViewById(R.id.btnLogin);
      forgotPwLink = (TextView)findViewById(R.id.tvForgotPw);
      loginBtn.setOnClickListener(new View.OnClickListener() {
    	  @Override
          public void onClick(View v) {
              login();
          }
      });

      forgotPwLink.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              //TODO send password to registered email id and display message
        	  login(); //temporory to test the link
          }
      });
   }
   
   public void login() {
	 if (validateLogin()) {
       Intent in = new Intent(getApplicationContext(), com.example.servicemanager.MainActivity.class);
       startActivity(in);
     } else {
       Toast.makeText(getApplicationContext(), "Wrong Credentials",Toast.LENGTH_SHORT).show();
     }
   }
   
   public boolean validateLogin() {
     boolean valid = false;

     userName = (EditText)findViewById(R.id.etUserName);
	 password = (EditText)findViewById(R.id.etPassword);

	 if (userName.getText().toString().equals("admin") &&
	   password.getText().toString().equals("admin")) {      	
	   valid = true;
     } else {
       valid = false;
     }

     return valid;
   }
}