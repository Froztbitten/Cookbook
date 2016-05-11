package com.shavedandbearded.tomato.Cookbook;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.shavedandbearded.tomato.AppContentProvider;
import com.shavedandbearded.tomato.DB;
import com.shavedandbearded.tomato.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecipeActivity extends AppCompatActivity
{
    int i = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        Button add = (Button) findViewById(R.id.button_add);
        add.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                LinearLayout rL = (LinearLayout) findViewById(R.id.ingredients_list);

                LinearLayout horizontal = new LinearLayout(RecipeActivity.this);
                horizontal.setOrientation(LinearLayout.HORIZONTAL);
                horizontal.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));

                EditText name = new EditText(RecipeActivity.this);
                name.setLayoutParams(new android.widget.LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                name.setId(i);
                name.setHint("ingredient");
                horizontal.addView(name);

                EditText quantity = new EditText(RecipeActivity.this);
                quantity.setLayoutParams(new android.widget.LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                quantity.setId(i + 100);
                quantity.setInputType(InputType.TYPE_CLASS_NUMBER);
                quantity.setHint("qty");
                horizontal.addView(quantity);

                Spinner label = new Spinner(RecipeActivity.this);
                List<String> labels = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.us_measurements_array)));
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(RecipeActivity.this, android.R.layout.simple_spinner_item, labels);
                label.setId(i + 200);
                label.setAdapter(adapter);
                horizontal.addView(label);

                i++;
                rL.addView(horizontal);
            }
        });

        Button save = (Button) findViewById(R.id.button_save);
        save.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String recipeName = ((EditText) findViewById(R.id.edit_recipe_name)).getText().toString();
                String instructions = ((EditText) findViewById(R.id.edit_instructions)).getText().toString();
                if(recipeName.isEmpty())
                    Toast.makeText(RecipeActivity.this, "Please enter a recipe name", Toast.LENGTH_SHORT).show();
                else if(i == 0)
                {
                    Toast.makeText(RecipeActivity.this, "Please add an ingredient", Toast.LENGTH_SHORT).show();
                }
                else if(instructions.isEmpty())
                    Toast.makeText(RecipeActivity.this, "Please enter instructions", Toast.LENGTH_SHORT).show();
                else
                {
                    ContentValues values = new ContentValues();

                    values.put("name", recipeName);
                    values.put("instructions", instructions);
                    getContentResolver().insert(AppContentProvider.CONTENT_URI2, values);

                    for(int j = 0; j < i; j++)
                    {
                        values = new ContentValues();
                        values.put("recipeName", recipeName);
                        values.put("ingName", ((EditText) findViewById(j)).getText().toString());
                        values.put("quantity", ((EditText) findViewById(100 + j)).getText().toString());
                        values.put("label", ((Spinner) findViewById(200 + j)).getSelectedItem().toString());
                        getContentResolver().insert(AppContentProvider.CONTENT_URI3, values);
                    }
                    RecipeActivity.this.finish();
                }
            }
        });
    }
}
