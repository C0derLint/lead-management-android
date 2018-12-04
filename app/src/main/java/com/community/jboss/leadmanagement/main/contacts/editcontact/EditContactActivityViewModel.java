package com.community.jboss.leadmanagement.main.contacts.editcontact;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.support.annotation.NonNull;

import com.community.jboss.leadmanagement.data.daos.ContactDao;
import com.community.jboss.leadmanagement.data.daos.ContactNumberDao;
import com.community.jboss.leadmanagement.data.entities.Contact;
import com.community.jboss.leadmanagement.data.entities.ContactNumber;
import com.community.jboss.leadmanagement.utils.DbUtil;

import java.util.ArrayList;
import java.util.List;

public class EditContactActivityViewModel extends AndroidViewModel {
    private MutableLiveData<Contact> mContact;
    private MutableLiveData<List<ContactNumber>> mContactNumbers;
    private boolean mIsNewContact;

    public EditContactActivityViewModel(@NonNull Application application) {
        super(application);

        mContact = new MutableLiveData<>();
        mContactNumbers = new MutableLiveData<>();
    }

    public boolean isNewContact() {
        return mIsNewContact;
    }

    public LiveData<Contact> getContact() {
        return mContact;
    }

    public void setContact(String contactId) {
        final Context context = getApplication();

        final ContactDao contactDao = DbUtil.contactDao(context);
        Contact contact = contactDao.getContact(contactId);
        if (contact == null) {
            contact = new Contact(null);
            mIsNewContact = true;
        }
        mContact.setValue(contact);

        final ContactNumberDao contactNumberDao = DbUtil.contactNumberDao(context);
        List<ContactNumber> contactNumbers = contactNumberDao.getContactNumbers(contactId);
        if (contactNumbers == null) {
            contactNumbers = new ArrayList<>();
        }
        mContactNumbers.setValue(contactNumbers);
    }

    public LiveData<List<ContactNumber>> getContactNumbers() {
        return mContactNumbers;
    }

    public ContactNumber getContactNumberByNumber(String number){
        final ContactNumberDao contactNumberDao = DbUtil.contactNumberDao(getApplication());
        return contactNumberDao.getContactNumber(number);
    }

    public void saveContact(String name) {
        final ContactDao dao = DbUtil.contactDao(getApplication());

        Contact contact = mContact.getValue();
        if (contact == null) {
            contact = new Contact(name);
        } else {
            contact.setName(name);
        }

        if (mIsNewContact) {
            dao.insert(contact);
        } else {
            dao.update(contact);
        }
    }

    public void saveContactNumber(String number) {
        final ContactNumberDao dao = DbUtil.contactNumberDao(getApplication());
        final String contactId = mContact.getValue().getId();
        final List<ContactNumber> contactNumbers = mContactNumbers.getValue();

        ContactNumber contactNumber;
        if (contactNumbers == null || contactNumbers.isEmpty()) {
            contactNumber = new ContactNumber(number, contactId);
        } else {
            contactNumber = mContactNumbers.getValue().get(0);
            contactNumber.setNumber(number);
        }

        if (mIsNewContact) {
            dao.insert(contactNumber);
        } else {
            dao.update(contactNumber);
        }
    }

    public void saveData(String email, String location, String query, byte[] image, String notes) {
        final ContactDao dao = DbUtil.contactDao(getApplication());
        Contact contact = mContact.getValue();
        if(contact!=null){
            contact.setMail(email);
            contact.setLocation(location);
            contact.setQuery(query);
            contact.setImage(image);
            contact.setNotes(notes);
        }
        dao.update(contact);
    }
}
