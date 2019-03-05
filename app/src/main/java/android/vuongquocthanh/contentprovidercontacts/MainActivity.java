package android.vuongquocthanh.contentprovidercontacts;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private Button btShowContact;
    private List<ContactsModel> listContact = new ArrayList<>();
    private ContactsAdapter adapter;
    private int PERMISSIONS_REQUEST_READ_CONTACTS =100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerview);
        btShowContact = findViewById(R.id.btShowContacts);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        btShowContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check the SDK version and whether the permission is already granted or not.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
                    fetchContacts();
                }
            }
        });
    }

    private void fetchContacts(){
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection = {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER};
        String selection = null;
        String[] selectionArgs = null;
        String sortOder = null;

        ContentResolver resolver  = getContentResolver();
        Cursor cursor = resolver.query(uri, projection, selection, selectionArgs, sortOder);

        while (cursor.moveToNext()){
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            ContactsModel contactsModel = new ContactsModel(name, number);
            listContact.add(contactsModel);
            Log.d("CONTACTS", name+number);
        }

        adapter = new ContactsAdapter(listContact);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
