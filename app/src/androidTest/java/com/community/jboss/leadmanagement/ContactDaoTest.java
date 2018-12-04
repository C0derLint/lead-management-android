package com.community.jboss.leadmanagement;

import android.arch.persistence.room.Room;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.community.jboss.leadmanagement.data.LeadDatabase;
import com.community.jboss.leadmanagement.data.entities.Contact;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

@RunWith(AndroidJUnit4.class)
public class ContactDaoTest {
    private LeadDatabase mDatabase;

    @Before
    public void initDatabase() {
        mDatabase = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                LeadDatabase.class).build();
    }

    @Test
    public void insertContactSavesData() {
        final Contact jane = new Contact("Jane");
        final Contact tom = new Contact("Tom");
        final Contact maria = new Contact("Maria");

        mDatabase.getContactDao().insert(jane);
        mDatabase.getContactDao().insert(tom);
        mDatabase.getContactDao().insert(maria);

        final List<Contact> contacts = mDatabase.getContactDao().getLiveContacts().getValue();
        assert (contacts != null);
        assert (!contacts.isEmpty());
        assert (contacts.size() == 3);
        assert (contacts.contains(jane));
        assert (contacts.contains(tom));
        assert (contacts.contains(maria));
    }
}
