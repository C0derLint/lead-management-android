package com.community.jboss.leadmanagement.main.contacts;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.community.jboss.leadmanagement.data.daos.ContactDao;
import com.community.jboss.leadmanagement.data.entities.Contact;
import com.community.jboss.leadmanagement.utils.DbUtil;

import java.util.List;

public class ContactsFragmentViewModel extends AndroidViewModel {
    private LiveData<List<Contact>> mContacts;

    public ContactsFragmentViewModel(@NonNull Application application) {
        super(application);

        final ContactDao dao = DbUtil.contactDao(getApplication());
        mContacts = dao.getLiveContacts();
    }

    public LiveData<List<Contact>> getContacts() {
        return mContacts;
    }

    public void deleteContact(Contact contact) {
        final ContactDao dao = DbUtil.contactDao(getApplication());
        dao.delete(contact);
    }
}
