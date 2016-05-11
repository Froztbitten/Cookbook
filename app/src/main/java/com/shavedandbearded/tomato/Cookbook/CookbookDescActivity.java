package com.shavedandbearded.tomato.Cookbook;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.shavedandbearded.tomato.AppContentProvider;
import com.shavedandbearded.tomato.DB;
import com.shavedandbearded.tomato.R;
import com.shavedandbearded.tomato.SettingsActivity;

public class CookbookDescActivity extends AppCompatActivity
{
    Long rowid;
    String recipeName;
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cookbook_desc);

        rowid = getIntent().getLongExtra("rowid", 0);
        ContentResolver contentResolver = getContentResolver();

        Cursor cursor = contentResolver.query(AppContentProvider.CONTENT_URI2.buildUpon().appendPath(Long.toString(rowid)).build(),
                new String[]{"name", "instructions"}, null, null, null);
        if(!cursor.moveToFirst())
            this.setTitle("WTF");
        else
        {
            recipeName = cursor.getString(0);
            ((TextView) findViewById(R.id.recipe_Name)).setText(recipeName);
            ((TextView) findViewById(R.id.instructions)).setText(cursor.getString(1));
        }
        cursor.close();

        cursor = contentResolver.query(AppContentProvider.CONTENT_URI3,
                new String[]{"recipeName", "ingName", "quantity", "label"},
                "recipeName = '" + recipeName + "'", null, null);

        if(!cursor.moveToFirst())
        {
            this.setTitle("PLEASE");
        }
        do
        {
            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.ingredients_row);

            LinearLayout rows = new LinearLayout(CookbookDescActivity.this);
            rows.setOrientation(LinearLayout.HORIZONTAL);
            rows.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));

            ViewSwitcher viewSwitcher = new ViewSwitcher(CookbookDescActivity.this);
            viewSwitcher.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            viewSwitcher.setId(i);

            EditText quantity = new EditText(CookbookDescActivity.this);
            quantity.setLayoutParams(new android.widget.LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            quantity.setId(i + 100);
            quantity.setText(Float.toString(cursor.getFloat(2)));
            viewSwitcher.addView(quantity);
            rows.addView(viewSwitcher);

            i++;
            linearLayout.addView(rows);
        } while(cursor.moveToNext());
        cursor.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_recipe, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if (id == R.id.action_settings)
        {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        else if (id == R.id.action_add_to_SL)
        {
            Toast.makeText(this, recipeName, Toast.LENGTH_SHORT).show();
            ContentResolver contentResolver = getContentResolver();
            Cursor cursor = contentResolver.query(AppContentProvider.CONTENT_URI3,
                    new String[]{"ingName", "quantity", "label"},
                    "recipeName = '" + recipeName + "'", null, null);
            if(!cursor.moveToFirst())
                this.setTitle("PLEASE");
            do
            {
                ContentValues values = new ContentValues();
                values.put("name", cursor.getString(0));
                values.put("quantity", cursor.getFloat(1));
                values.put("label", cursor.getString(2));
                Uri newRowId = getContentResolver().insert(AppContentProvider.CONTENT_URI, values);
            } while (cursor.moveToNext());
            cursor.close();
        }
        else if (id == R.id.action_delete)
        {
            String recipeName = "";
            ContentResolver cr = getContentResolver();
            Cursor c = cr.query(AppContentProvider.CONTENT_URI2.buildUpon().appendPath(Long.toString(rowid)).build(),
                    new String[]{"name"}, null, null, null);
            if(!c.moveToFirst())
                this.setTitle("WTF");
            else
            {
                Toast.makeText(this, c.getString(0), Toast.LENGTH_SHORT).show();
                recipeName = c.getString(0);
            }
            c.close();
            cr.delete(AppContentProvider.CONTENT_URI2, "_id = " + rowid, null);
            cr.delete(AppContentProvider.CONTENT_URI3, "recipeName = '" + recipeName + "'", null);
            finish();
        }
        else if (id == R.id.action_edit)
        {

        }
        return super.onOptionsItemSelected(item);
    }
}
