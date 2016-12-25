package com.example.servicemanager;


import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {

  Intent intent;
  PendingIntent pendingIntent;

  @Override
  public void onReceive(Context context, Intent intent) {
    Intent service1 = new Intent(context, AlarmService.class);
    context.startService(service1);

  }

}
