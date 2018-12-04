package com.community.jboss.leadmanagement.main.contacts.importcontact;


import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.community.jboss.leadmanagement.R;
import com.community.jboss.leadmanagement.data.daos.ContactDao;
import com.community.jboss.leadmanagement.data.daos.ContactNumberDao;
import com.community.jboss.leadmanagement.data.entities.Contact;
import com.community.jboss.leadmanagement.data.entities.ContactNumber;
import com.community.jboss.leadmanagement.utils.DbUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.community.jboss.leadmanagement.SettingsActivity.PREF_DARK_THEME;

public class ImportContactActivity extends AppCompatActivity {

    @BindView(R.id.importContactRecycler)
    RecyclerView recyclerView;

    ContactDao contactDao;
    ContactNumberDao numberDao;
    private ImportsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean useDarkTheme = preferences.getBoolean(PREF_DARK_THEME, false);

        if(useDarkTheme) {
            setTheme(R.style.AppTheme_Dark);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_contact);
        setTitle(R.string.import_contacts);
        ButterKnife.bind(this);
        contactDao = DbUtil.contactDao(getApplication());
        numberDao = DbUtil.contactNumberDao(getApplication());

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        adapter = new ImportsAdapter(getContacts());
        recyclerView.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.import_contact_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id==R.id.finishImport){
            List<ImportContact> contacts = adapter.getContactsToImport();
            if(contacts.size()==0){
                onBackPressed();
            }else{
                for(ImportContact contact:contacts){
                    String contactUUID = UUID.randomUUID().toString();
                    contactDao.insert(new Contact(contactUUID,contact.getName()));
                    numberDao.insert(new ContactNumber(contact.getNumber(),contactUUID));
                }
                onBackPressed();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    public List<ImportContact> getContacts(){
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection    = new String[] {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER};

        Cursor people = getContentResolver().query(uri, projection, null, null, null);

        if(people == null){
            return new ArrayList<>();
        }

        int indexName = people.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
        int indexNumber = people.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

        List<ImportContact> contacts = new ArrayList<>();

        if(people.moveToFirst()) {
            do {
                String name   = people.getString(indexName);
                String number = people.getString(indexNumber)
                        .replace("(","")
                        .replace(")","")
                        .replace(" ","")
                        .replace("-","");
                if(numberDao.getContactNumber(number)==null) {
                    ImportContact contact = new ImportContact(name, number);
                    contacts.add(contact);
                }
            } while (people.moveToNext());
        }
        people.close();
        return contacts;
    }


}

