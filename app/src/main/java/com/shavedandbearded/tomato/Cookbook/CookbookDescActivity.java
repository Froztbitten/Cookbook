package com.shavedandbearded.tomato.Cookbook;

import android.app.ActionBar;
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

import org.w3c.dom.Text;

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

            ViewSwitcher viewSwitcher = new ViewSwitcher(CookbookDescActivity.this);
            viewSwitcher.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            viewSwitcher.setId(i);

            LinearLayout rows1 = new LinearLayout(CookbookDescActivity.this);
            rows1.setOrientation(LinearLayout.HORIZONTAL);
            rows1.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));

            TextView quantity1 = new TextView(CookbookDescActivity.this);
            quantity1.setLayoutParams(new android.widget.LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            quantity1.setId(i + 100);
            quantity1.setText(Float.toString(cursor.getFloat(2)));
            rows1.addView(quantity1);

            TextView label1 = new TextView(CookbookDescActivity.this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(20, 0, 0, 0);
            label1.setLayoutParams(layoutParams);
            label1.setId(i + 200);
            label1.setText(cursor.getString(3));
            rows1.addView(label1);

            TextView name1 = new TextView(CookbookDescActivity.this);
            layoutParams = new LinearLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(20, 0, 0, 0);
            name1.setLayoutParams(layoutParams);
            name1.setId(i + 300);
            name1.setText(cursor.getString(1));
            rows1.addView(name1);

            viewSwitcher.addView(rows1);

            LinearLayout rows2 = new LinearLayout(CookbookDescActivity.this);
            rows2.setOrientation(LinearLayout.HORIZONTAL);
            rows2.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));

            EditText quantity2 = new EditText(CookbookDescActivity.this);
            quantity2.setLayoutParams(new android.widget.LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            quantity2.setId(i + 400);
            quantity2.setText(Float.toString(cursor.getFloat(2)));
            rows2.addView(quantity2);

            EditText label2 = new EditText(CookbookDescActivity.this);
            layoutParams = new LinearLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(20, 0, 0, 0);
            label2.setLayoutParams(layoutParams);
            label2.setId(i + 500);
            label2.setText(cursor.getString(3));
            rows2.addView(label2);

            EditText name2 = new EditText(CookbookDescActivity.this);
            layoutParams = new LinearLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(20, 0, 0, 0);
            name2.setLayoutParams(layoutParams);
            name2.setId(i + 600);
            name2.setText(cursor.getString(1));
            rows2.addView(name2);

            viewSwitcher.addView(rows2);

            linearLayout.addView(viewSwitcher);
            i++;
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
            for(int j = 0; j < i; j++)
            {
                ViewSwitcher viewSwitcher = (ViewSwitcher) (CookbookDescActivity.this).findViewById(j);
                viewSwitcher.getNextView();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
