package com.shavedandbearded.tomato.Cookbook;

import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.shavedandbearded.tomato.AppContentProvider;
import com.shavedandbearded.tomato.DB;
import com.shavedandbearded.tomato.R;
import com.shavedandbearded.tomato.SettingsActivity;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class CookbookActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemClickListener
{
    private SimpleCursorAdapter adapter;

    SQLiteDatabase mydb;
    static final String SERVER = "http://192.168.1.5:8080/";
    static final String TAG = "Cookbook Activity";
    JSONObject jsonObject = new JSONObject();
    JSONObject testJsonObject = new JSONObject();
    ArrayList<String> listBuffer = new ArrayList<String>();
    ArrayList<String> myArrayList = new ArrayList<String>();
    private ArrayAdapter<String> myAdapter;
    ListView listAll;

    int numRecipes = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cookbook);

        adapter = new SimpleCursorAdapter(this, R.layout.list_item, null, new String[]{"name"},
                new int[]{R.id.txtName}, 0);

        ListView listAll = (ListView) findViewById(R.id.listAll);
        listAll.setAdapter(adapter);
        listAll.setOnItemClickListener(this);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v("DATABASE RECORDS", "onResume is called");
        DB.getInstance(this).getWritableDatabase(new DB.OnDBReadyListener() {
            @Override
            public void onReady(SQLiteDatabase db) {
                CookbookActivity.this.mydb = db;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cookbook, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        else if(id == R.id.action_add) {
            startActivity(new Intent(this, RecipeActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args)
    {
        return new CursorLoader(this, AppContentProvider.CONTENT_URI2, new String[]{"_id", "name"}, null,
                null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data)
    {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader)
    {
        adapter.swapCursor(null);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        Intent intent = new Intent(CookbookActivity.this, CookbookDescActivity.class);
        intent.putExtra("rowid", id);
        startActivity(intent);
    }
}
