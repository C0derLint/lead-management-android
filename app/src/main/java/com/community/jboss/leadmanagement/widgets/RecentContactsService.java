package com.community.jboss.leadmanagement.widgets;


import android.content.Intent;
import android.widget.RemoteViewsService;

public class RecentContactsService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RecentContactsFactory(this.getApplicationContext());
    }
}