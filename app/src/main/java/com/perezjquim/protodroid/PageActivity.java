package com.perezjquim.protodroid;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.perezjquim.protodroid.db.DatabaseManager;
import com.perezjquim.protodroid.view.ActionCardView;

import static com.perezjquim.UIHelper.askBinary;
import static com.perezjquim.UIHelper.toast;

public class PageActivity extends AppCompatActivity
{
    private int id;
    private String name;

    private static final int COLUMN_ID = 0;
    private static final int COLUMN_TYPE = 1;
    private static final int COLUMN_LABEL = 2;
    private static final int COLUMN_CONFIG = 3;
    private static final int COLUMN_PAGE_ID = 4;
    private static final int COLUMN_PAGE_DESTINATION_ID = 5;


    private static final String[] types = { "Button" , "Checkbox" , "Switch" };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page);
        initPage();
        listElements();
    }

    private void initPage()
    {
        Intent i = getIntent();
        id = i.getIntExtra("id",-1);
        name = i.getStringExtra("name");
        TextView title = findViewById(R.id.title);
        title.setText(name);
    }

    private void listElements()
    {
        Cursor elements = DatabaseManager.getElements(id);
        LinearLayout elementListView = findViewById(R.id.elementList);
        while(elements.moveToNext())
        {
            final int id = elements.getInt(COLUMN_ID);
            final String name = elements.getString(COLUMN_LABEL);

            final ActionCardView[] card = new ActionCardView[1];
            card[0] = new ActionCardView(this, name,
                    null,
                    (v)->updateElement(id),
                    (v)->deleteElement(elementListView,card[0],id));
            elementListView.addView(card[0]);
        }
    }

    public void addElement(View view)
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Create a new element");

        LinearLayout form = new LinearLayout(this);
        form.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        form.setOrientation(LinearLayout.VERTICAL);

        TextView lblLabel = new TextView(this);
        lblLabel.setText("Label:");
        form.addView(lblLabel);

        EditText fldLabel = new EditText(this);
        form.addView(fldLabel);

        TextView lblType = new TextView(this);
        lblType.setText("Type:");
        form.addView(lblType);

        Spinner spTypes = new Spinner(this);
        spTypes.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, types));
        spTypes.setSelection(0);
        form.addView(spTypes);

        alertDialog.setView(form);
        alertDialog.setPositiveButton("Confirm",
                (dialog,which) ->
                {
                    System.out.println(spTypes.getSelectedItemPosition());
                    DatabaseManager.insertElement(spTypes.getSelectedItemPosition(),""+fldLabel.getText(),"",id);
                    toast(this,"Element created!");
                    Intent i = new Intent(this,PageActivity.class);
                    i.putExtra("id",id);
                    i.putExtra("name",name);
                    startActivity(i);
                    this.finish();
                });
        alertDialog.setNegativeButton("Cancel",
                (dialog, which) -> dialog.cancel());
        alertDialog.show();
    }

    private void updateElement(int elementID)
    {
        Cursor previous = DatabaseManager.getIndividualElement(elementID);

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Create a new element");

        LinearLayout form = new LinearLayout(this);
        form.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        form.setOrientation(LinearLayout.VERTICAL);

        TextView lblLabel = new TextView(this);
        lblLabel.setText("Label:");
        form.addView(lblLabel);

        EditText fldLabel = new EditText(this);
        fldLabel.setText(previous.getString(COLUMN_LABEL));;
        form.addView(fldLabel);

        TextView lblType = new TextView(this);
        lblType.setText("Type:");
        form.addView(lblType);

        Spinner spTypes = new Spinner(this);
        spTypes.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, types));
        spTypes.setSelection(previous.getInt(COLUMN_TYPE));
        form.addView(spTypes);

        alertDialog.setView(form);
        alertDialog.setPositiveButton("Confirm",
                (dialog,which) ->
                {
                    DatabaseManager.updateElement(spTypes.getSelectedItemPosition(),""+fldLabel.getText(),"",elementID);
                    toast(this,"Element updated!");
                    Intent i = new Intent(this,PageActivity.class);
                    i.putExtra("id",id);
                    i.putExtra("name",name);
                    startActivity(i);
                    this.finish();
                });
        alertDialog.setNegativeButton("Cancel",
                (dialog, which) -> dialog.cancel());
        alertDialog.show();
    }

    private void deleteElement(LinearLayout list, ActionCardView card, int elementID)
    {
        askBinary(this,"Are you sure you want to delete this element?",null,()->
        {
            list.removeView(card);
            DatabaseManager.deleteElement(id);
            toast(this,"Element deleted!");
        });
    }

}
