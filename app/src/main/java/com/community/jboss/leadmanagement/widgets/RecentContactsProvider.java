package com.community.jboss.leadmanagement.widgets;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.community.jboss.leadmanagement.R;

public class RecentContactsProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context ctxt, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            Intent svcIntent = new Intent(ctxt, RecentContactsService.class);

            svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            svcIntent.setData(Uri.parse(svcIntent.toUri(Intent.URI_INTENT_SCHEME)));

            RemoteViews widget = new RemoteViews(ctxt.getPackageName(),
                    R.layout.recent_contacts_widget);

            widget.setRemoteAdapter(appWidgetId, R.id.words,
                    svcIntent);

            appWidgetManager.updateAppWidget(appWidgetId, widget);
        }

        super.onUpdate(ctxt, appWidgetManager, appWidgetIds);
    }
}