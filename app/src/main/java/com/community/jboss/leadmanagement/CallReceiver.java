package com.community.jboss.leadmanagement;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.community.jboss.leadmanagement.main.MainActivity;
import com.community.jboss.leadmanagement.main.contacts.editcontact.EditContactActivity;

public class CallReceiver extends BroadcastReceiver {

    private Context mContext;
    private String CHANNEL_ID = "LEAD_MANAGEMENT_ID";
    private int ID = 54321;

    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;


        TelephonyManager tm = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);

        if(tm == null) return;

        tm.listen(new PhoneStateListener(){
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                super.onCallStateChanged(state, incomingNumber);
                switch(state){
                    case TelephonyManager.CALL_STATE_IDLE:
                        hideNotification();
                        break;
                    case TelephonyManager.CALL_STATE_OFFHOOK:
                        showNotification(incomingNumber);
                        break;
                }
                System.out.println("incomingNumber : "+incomingNumber);
            }
        },PhoneStateListener.LISTEN_CALL_STATE);

    }

    private void hideNotification() {
        final NotificationManager manager =
                (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        if (manager != null) {
            manager.cancel(ID);
        }

        Intent callRecorderService = new Intent(mContext, CallRecorderService.class);
        mContext.stopService(callRecorderService);
    }


    public void showNotification(String number) {

        // Call Recording Service Intent
        Intent notifyIntent = new Intent("com.aykuttasil.callrecord.CallRecord");
        notifyIntent.setClass(mContext, CallRecorderService.class);

        PendingIntent notifyPendingIntent = PendingIntent.getService(
                mContext, 0, notifyIntent, 0
        );

        NotificationCompat.Builder mBuilder =
                new NotificationCompat
                        .Builder(mContext.getApplicationContext(), CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_record)
                        .setContentTitle("Lead Management")
                        .setContentText("Would you like to record this conversation?")
                        .setPriority(Notification.PRIORITY_MAX)
                        .addAction(R.drawable.ic_record_round, "Record now",
                                notifyPendingIntent);


        // Contact Details Intent
        Intent contentIntent = new Intent(mContext, EditContactActivity.class);
        PendingIntent contentPendingIntent = PendingIntent.getActivity(mContext, 0, contentIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        final NotificationCompat.Builder notification = new NotificationCompat.Builder(mContext)
                .setSmallIcon(R.drawable.ic_phone)
                .setContentTitle("Call in Progress")
                .setTicker("Lead Management")
                .setContentIntent(contentPendingIntent)
                .setContentText("Number: " + number)
                .setChannelId(CHANNEL_ID);

        NotificationManager mNotificationManager =
                (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        if (mNotificationManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                        "This is a notification channel for displaying recording option.",
                        NotificationManager.IMPORTANCE_DEFAULT);
                mNotificationManager.createNotificationChannel(channel);
            }

            mNotificationManager.notify(ID, mBuilder.build());

        }
    }
}
