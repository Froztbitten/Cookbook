package com.shavedandbearded.tomato.ShoppingList;

import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.shavedandbearded.tomato.AppContentProvider;
import com.shavedandbearded.tomato.DB;
import com.shavedandbearded.tomato.R;
import com.shavedandbearded.tomato.SettingsActivity;

import java.util.ArrayList;
import java.util.List;

public class ShoppingListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    SQLiteDatabase mydb;
    long currentRow;
    private SimpleCursorAdapter adapt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        adapt = new SimpleCursorAdapter(this, R.layout.list_item, null, new String[]{"name"},
                new int[] {R.id.txtName},0);

        ListView listAll = (ListView) findViewById(R.id.listAll);
        listAll.setAdapter(adapt);
        listAll.setOnItemClickListener(this);

        getLoaderManager().initLoader(1, null, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v("DATABASE RECORDS", "onResume is called");
        DB.getInstance(this).getWritableDatabase(new DB.OnDBReadyListener() {
            @Override
            public void onReady(SQLiteDatabase db) {
                ShoppingListActivity.this.mydb = db;
            }
        });
    }

    public void addItem(View view){
        if(mydb == null){
            Toast.makeText(this, "Try again in a few seconds", Toast.LENGTH_SHORT).show();
        }
        else{
            ContentResolver cr = getContentResolver();
            ContentValues values = new ContentValues();
            values.put("name", ((EditText) findViewById(R.id.addItemShoppingList)).getText().toString());
            Uri newRowId = getContentResolver().insert(AppContentProvider.CONTENT_URI, values);
            Toast.makeText(this, "Added " + ((EditText) findViewById(R.id.addItemShoppingList)).getText().toString(), Toast.LENGTH_SHORT).show();
            ((EditText) findViewById(R.id.addItemShoppingList)).setText("");
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, AppContentProvider.CONTENT_URI,
                new String[]{"_id", "name"}, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapt.swapCursor(data);
    }

    //     called when activity is being destroyed to prevent memory leak
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapt.swapCursor(null);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.v("CALLED", "onItemClick");
        //intent open edit_item including id (row id)
        Intent intent = new Intent(ShoppingListActivity.this, EditItemActivity.class);
        intent.putExtra("rowid", id);
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Log.v("CALLED", "onItemLongClick");
        return false;
    }
}
