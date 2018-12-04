package com.community.jboss.leadmanagement.widgets;

import android.content.Context;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.community.jboss.leadmanagement.R;
import com.community.jboss.leadmanagement.data.entities.Contact;
import com.community.jboss.leadmanagement.utils.DbUtil;

import java.util.List;

import static com.community.jboss.leadmanagement.main.contacts.editcontact.EditContactActivity.bytesToBitmap;

public class RecentContactsFactory implements RemoteViewsService.RemoteViewsFactory {
    private List<Contact> data;

    private Context context;

    public RecentContactsFactory(Context context) {
        this.context = context;

        getData();
    }

    @Override
    public void onCreate() {}

    @Override
    public void onDestroy() {}

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews widgetCell =new RemoteViews(context.getPackageName(),
                R.layout.widget_cell);

        widgetCell.setTextViewText(android.R.id.text1, data.get(position).getName());
        widgetCell.setImageViewBitmap(android.R.id.icon, bytesToBitmap(data.get(position).getImage()));

        return widgetCell;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void onDataSetChanged() {
        getData();
    }

    public void getData(){
        this.data = DbUtil.contactDao(context).getContacts();
    }
}
