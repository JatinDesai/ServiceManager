package com.example.servicemanager;

import static com.example.servicemanager.Constants.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

public class AlarmService extends IntentService {

  private static final int NOTIFICATION_ID = 1;
  private NotificationManager notificationManager;
  private PendingIntent pendingIntent;

  public AlarmService() {
    super("AlarmService");
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    return super.onStartCommand(intent, flags, startId);
  }

  @Override
  protected void onHandleIntent(Intent intent) {
    DBHelper mydb = new DBHelper(this);

    List<Customer> customers = new ArrayList<Customer>();
    try {
      customers = mydb.getDueServices();
    } catch (ParseException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    if (customers.size() > 0) {

      // don't notify if they've played in last 24 hr
      Context context = this.getApplicationContext();
      notificationManager =
          (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
      Intent mIntent =
          new Intent(getApplicationContext(), com.example.servicemanager.SearchCustomer.class);
      mIntent.putExtra(CALLED_FROM, DUE_SERVICES);
      Bundle bundle = new Bundle();
      bundle.putString("test", "test");
      mIntent.putExtras(bundle);
      pendingIntent =
          PendingIntent.getActivity(context, 0, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);

      Resources res = this.getResources();
      NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

      builder.setContentIntent(pendingIntent).setSmallIcon(R.drawable.ic_launcher)
          .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.ic_launcher))
          .setTicker(res.getString(R.string.due_services)).setAutoCancel(true)
          .setContentTitle(res.getString(R.string.due_services))
          .setContentText(res.getString(R.string.due_services));

      notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
      notificationManager.notify(NOTIFICATION_ID, builder.build());
    }
  }
}
