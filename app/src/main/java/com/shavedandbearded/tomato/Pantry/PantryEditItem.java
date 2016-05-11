package com.shavedandbearded.tomato.Pantry;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.shavedandbearded.tomato.AppContentProvider;
import com.shavedandbearded.tomato.R;

public class PantryEditItem  extends AppCompatActivity {
    Long rowid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantry_edit_item);
        if (getIntent().hasExtra("rowid")) {
            rowid = getIntent().getLongExtra("rowid", 0);
            Log.v("VARIABLE: ", "rowid =" + rowid);
            ContentResolver cr = getContentResolver();

            Cursor c = cr.query(AppContentProvider.CONTENT_URI1.buildUpon().appendPath(Long.toString(rowid)).build(),
                    new String[]{"name"}, null, null, null);
            c.moveToFirst();
            ((EditText) findViewById(R.id.itemNameEditText1)).setText(c.getString(0));
            Log.v("ContentProvider Info", c.getString(0));
        }
    }

    public void updateButton1(View view){
        Log.v("CALLED: ", "updateButton1");

        ContentResolver cr = getContentResolver();
        ContentValues values = new ContentValues();

        values.put("name", ((EditText) findViewById(R.id.itemNameEditText1)).getText().toString());
        cr.update(AppContentProvider.CONTENT_URI1.buildUpon().appendPath(Long.toString(rowid)).build(), values, null, null);
        finish(); //closes the EditItemActivity and returns to ShoppingListActivity
    }

    public void deleteItem1(View view) {
        if (rowid != null) {
            try {
                ContentResolver cr = getContentResolver();
                cr.delete(AppContentProvider.CONTENT_URI1, "_id = " + rowid, null);
                finish();
            } catch (SQLException e) {
                Toast.makeText(this, "Error deleting record.", Toast.LENGTH_LONG).show();
            }
        }
        else {
            finish();
        }
    }
}
