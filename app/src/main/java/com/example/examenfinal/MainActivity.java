package com.example.examenfinal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor> {
    private ListView listView_contacts;
    private SimpleCursorAdapter adapter;
    private Button button_notificationActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView_contacts = (ListView)findViewById(R.id.listview_contacts);
        button_notificationActivity = (Button)findViewById(R.id.button_notifActivity);
        button_notificationActivity.setOnClickListener(this);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{
                    Manifest.permission.READ_CONTACTS},0);
        }

        //#region SimpleCursorAdapter
        String[] COLUMNS_TO_BE_BOUND = new String[]{
                ContactsContract.CommonDataKinds.Phone._ID,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER
        };

        int[] LAYOUT_ITEMS_TO_FILL = new int[]{
                R.id.textview_id,
                R.id.textview_contact_name,
                R.id.textview_number,
        };

        adapter = new SimpleCursorAdapter(this,
                R.layout.contact_list_row,
                null,
                COLUMNS_TO_BE_BOUND,
                LAYOUT_ITEMS_TO_FILL,
                0);

        listView_contacts.setAdapter(adapter);
        LoaderManager.getInstance(this).initLoader(1, null, this);
        //#endregion
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_notifActivity:
                Intent intent = new Intent(this, NotificationActivity.class);
                startActivity(intent);
                break;
        }
    }

    //#region Overrides pour SimpleCursorAdapter
    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String[] Projection =
                {
                        ContactsContract.CommonDataKinds.Phone._ID,
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                        ContactsContract.CommonDataKinds.Phone.NUMBER
                };

        String selection = null;
        String[] selectionArgs = null;
        String orderBy = ContactsContract.Contacts._ID;

        CursorLoader cursorLoader = new CursorLoader(
                MainActivity.this,
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                Projection,
                selection,
                selectionArgs,
                orderBy);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        adapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
    //#endregion
}