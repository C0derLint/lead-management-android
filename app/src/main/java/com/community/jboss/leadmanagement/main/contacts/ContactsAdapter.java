package com.community.jboss.leadmanagement.main.contacts;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.graphics.Color;

import static com.community.jboss.leadmanagement.SettingsActivity.PREF_DARK_THEME;
import static com.community.jboss.leadmanagement.main.contacts.editcontact.EditContactActivity.bytesToBitmap;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.community.jboss.leadmanagement.CustomDialogBox;
import com.community.jboss.leadmanagement.PermissionManager;
import com.community.jboss.leadmanagement.R;
import com.community.jboss.leadmanagement.SettingsActivity;
import com.community.jboss.leadmanagement.data.daos.ContactNumberDao;
import com.community.jboss.leadmanagement.data.entities.Contact;
import com.community.jboss.leadmanagement.main.contacts.editcontact.EditContactActivity;
import com.community.jboss.leadmanagement.utils.DbUtil;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> implements Filterable {
    private List<Contact> mContacts;
    private ContactsAdapter mAdapter;
    public AdapterListener mListener;
    private List<Contact> spareData;

    public ContactsAdapter(AdapterListener listener) {
        mListener = listener;
        mAdapter = this;
        mContacts = new ArrayList<>();
        spareData = new ArrayList<>();
    }

    @Override
    public ContactsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_cell, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Contact contact = mContacts.get(position);
        holder.bind(contact);
    }

    @Override
    public int getItemCount() {
        return mContacts.size();
    }

    public void replaceData(List<Contact> contacts) {
        mContacts = contacts;
        spareData = contacts;
        notifyDataSetChanged();
    }

    private Context mContext;
    @Override
    public Filter getFilter() {
        mContacts = spareData;
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String data = constraint.toString();
                if(data.isEmpty()){
                    mContacts = spareData;
                }
                List<Contact> filteredList = new ArrayList<>();
                final ContactNumberDao dao = DbUtil.contactNumberDao(mContext);

                for(Contact contact: mContacts){
                    if(contact.getName().toLowerCase().contains(data.toLowerCase())){
                        filteredList.add(contact);
                    }
                    else if (dao.getContactNumbers(contact.getId()).get(0).getNumber().contains(data)) {
                        filteredList.add(contact);
                    }
                }

                FilterResults results = new FilterResults();
                results.values = filteredList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mContacts = (ArrayList<Contact>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface AdapterListener {
        void onContactDeleted(Contact contact);
    }

    final class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener {
        @BindView(R.id.contact_name)
        TextView name;
        @BindView(R.id.contact_number)
        TextView number;
        @BindView(R.id.contact_avatar)
        CircularImageView picture;
        @BindView(R.id.contact_delete)
        ImageButton deleteButton;


        private Contact mContact;
        private Context mContext;
        private PermissionManager permManager;
        private SharedPreferences mPref;

        ViewHolder(View v) {
            super(v);

            mContext = v.getContext();
            mPref = PreferenceManager.getDefaultSharedPreferences(mContext);
            permManager = new PermissionManager(mContext,(Activity) mContext);
            ButterKnife.bind(this, v);

            v.setOnClickListener(this);
            v.setOnLongClickListener(this);

            deleteButton.setOnClickListener(v1 -> {
                CustomDialogBox dialogBox = new CustomDialogBox();
                dialogBox.showAlert((Activity) mContext,mContact,mAdapter);
                deleteButton.setVisibility(View.INVISIBLE);
            });
        }

        void bind(Contact contact) {
            mContact = contact;

            // TODO add contact avatar
            name.setText(contact.getName());
            number.setText(getNumber());
            Glide.with(mContext).load(bytesToBitmap(contact.getImage())).into(picture);
        }

        /**
         * TODO:
         * This really sucks but it'll do until we decide to make
         * database transactions go into the background thread,
         * or find out how to embed the contact number into the
         * contact object itself
         */
        private String getNumber() {
            final ContactNumberDao dao = DbUtil.contactNumberDao(mContext);
            return dao.getContactNumbers(mContact.getId()).get(0).getNumber();
        }

        @Override
        public void onClick(View view) {
            final Context context = view.getContext();

            Dialog detailDialog;
            detailDialog = new Dialog(context);

            TextView txtClose;
            TextView popupName;
            TextView contactNum;
            TextView mail;
            TextView location;
            TextView notes;
            TextView notes_hint;
            Button btnEdit;
            Button btnCall;
            Button btnMsg;
            LinearLayout layout;
            ImageView image;

            ImageView helper_email;
            ImageView helper_phone;
            ImageView helper_adress;
            ImageView helper_notes;

            detailDialog.setContentView(R.layout.popup_detail);
            txtClose = detailDialog.findViewById(R.id.txt_close);
            btnEdit = detailDialog.findViewById(R.id.btn_edit);
            popupName = detailDialog.findViewById(R.id.popup_name);
            contactNum = detailDialog.findViewById(R.id.txt_num);
            btnCall = detailDialog.findViewById(R.id.btn_call);
            btnMsg = detailDialog.findViewById(R.id.btn_msg);
            mail = detailDialog.findViewById(R.id.popupMail);
            layout = detailDialog.findViewById(R.id.popupLayout);
            image = detailDialog.findViewById(R.id.details_image);
            location = detailDialog.findViewById(R.id.details_adress);
            notes = detailDialog.findViewById(R.id.details_note);
            notes_hint = detailDialog.findViewById(R.id.details_note_hint);

            helper_email = detailDialog.findViewById(R.id.popup_helper_email);
            helper_phone = detailDialog.findViewById(R.id.popup_helper_phone);
            helper_adress = detailDialog.findViewById(R.id.popup_helper_adress);
            helper_notes = detailDialog.findViewById(R.id.popup_helper_note);

            if(mPref.getBoolean(PREF_DARK_THEME,false)){
                layout.setBackgroundColor(Color.parseColor("#303030"));
                popupName.setTextColor(Color.WHITE);
                contactNum.setTextColor(Color.WHITE);
                mail.setTextColor(Color.WHITE);
                location.setTextColor(Color.WHITE);
                notes.setTextColor(Color.WHITE);
                notes_hint.setTextColor(Color.WHITE);


                txtClose.setBackground(mContext.getResources().getDrawable(R.drawable.ic_close_white));
                helper_email.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_email_white));
                helper_phone.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_phone_white));
                helper_adress.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_location_white));
                helper_notes.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_notes_white));
            }

            popupName.setText(name.getText());
            contactNum.setText(number.getText());
            mail.setText(mContact.getMail());
            notes.setText(mContact.getNotes());
            Glide.with(context).load(bytesToBitmap(mContact.getImage())).apply(new RequestOptions().circleCrop()).into(image);

            txtClose.setOnClickListener(view1 -> detailDialog.dismiss());

            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final Intent intent = new Intent(context, EditContactActivity.class);
                    intent.putExtra(EditContactActivity.INTENT_EXTRA_CONTACT_NUM, number.getText().toString());
                    context.startActivity(intent);
                }
            });


            btnCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(permManager.permissionStatus(Manifest.permission.CALL_PHONE)) {
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:" + number.getText().toString()));
                        context.startActivity(intent);
                    }else{
                        permManager.requestPermission(58,Manifest.permission.CALL_PHONE);
                    }
                }
            });

            btnMsg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("sms:"
                            + number.getText().toString())));
                }
            });

            detailDialog.show();



        }

        @Override
        public boolean onLongClick(View view) {
            final int newVisibility = deleteButton.getVisibility() == View.VISIBLE
                    ? View.GONE
                    : View.VISIBLE;
            deleteButton.setVisibility(newVisibility);
            if(mPref.getBoolean(SettingsActivity.PREF_DARK_THEME,false)){
                deleteButton.setBackgroundColor(Color.parseColor("#303030"));
                deleteButton.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_close_white));
            }
            return true;
        }
    }

    public int getDataSize(){
        return mContacts.size();
    }
}

