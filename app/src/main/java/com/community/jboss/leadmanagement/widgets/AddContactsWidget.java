package com.community.jboss.leadmanagement.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.community.jboss.leadmanagement.R;
import com.community.jboss.leadmanagement.main.contacts.editcontact.EditContactActivity;

public class AddContactsWidget extends AppWidgetProvider {


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.add_contacts_widget);

            Intent intent = new Intent(context, EditContactActivity.class);

            views.setOnClickPendingIntent(R.id.widget_layout, PendingIntent.getActivity(context, 0, intent, 0));
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }


    public static PendingIntent getPendingIntent(Context context){
        Intent intent = new Intent(context, EditContactActivity.class);
        return  PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}

