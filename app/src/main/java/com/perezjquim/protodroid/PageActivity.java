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
import com.perezjquim.protodroid.db.Element;
import com.perezjquim.protodroid.db.Page;
import com.perezjquim.protodroid.view.ActionCardView;

import java.util.ArrayList;
import java.util.Arrays;

import static com.perezjquim.UIHelper.askBinary;
import static com.perezjquim.UIHelper.toast;

public class PageActivity extends AppCompatActivity
{
    private int page_id;
    private String page_name;
    private int project_id;

    private static final int TYPE_NONE = 0;

    private static final String TITLE = " (Elements)";

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
        project_id = i.getIntExtra("project_id",-1);
        page_id = i.getIntExtra("page_id",-1);
        page_name = i.getStringExtra("page_name");
        TextView title = findViewById(R.id.title);
        title.setText(page_name + TITLE);
    }

    private void listElements()
    {
        Cursor elements = DatabaseManager.getElements(page_id);
        LinearLayout elementListView = findViewById(R.id.elementList);
        while(elements.moveToNext())
        {
            final int id = elements.getInt(Element.ID.ordinal());
            final String name = elements.getString(Element.LABEL.ordinal());

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

        fillForm(alertDialog,null);

        alertDialog.show();
    }

    private void updateElement(int element_id)
    {
        Cursor element = DatabaseManager.getIndividualElement(element_id);

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Edit an element");

        fillForm(alertDialog,element);

        alertDialog.show();
    }

    private void deleteElement(LinearLayout list, ActionCardView card, int element_id)
    {
        askBinary(this,"Are you sure you want to delete this element?",null,()->
        {
            list.removeView(card);
            DatabaseManager.deleteElement(element_id);
            toast(this,"Element deleted!");
        });
    }

    private void fillForm(AlertDialog.Builder alertDialog, Cursor element)
    {
        LinearLayout form = new LinearLayout(this);
        form.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        form.setOrientation(LinearLayout.VERTICAL);

        TextView lblLabel = new TextView(this);
        lblLabel.setText("Label:");
        form.addView(lblLabel);

        EditText fldLabel = new EditText(this);
        if(element != null) fldLabel.setText(element.getString(Element.LABEL.ordinal()));
        form.addView(fldLabel);

        TextView lblType = new TextView(this);
        lblType.setText("Type:");
        form.addView(lblType);

        String[] types = Arrays.toString(Element.Types.values()).replaceAll("^.|.$", "").split(", ");
        Spinner spTypes = new Spinner(this);
        spTypes.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, types));
        if(element != null) spTypes.setSelection(element.getInt(Element.TYPE.ordinal()));
        form.addView(spTypes);

        TextView lblLink = new TextView(this);
        lblLink.setText("Link:");
        form.addView(lblLink);

        Spinner spLink = new Spinner(this);
        ArrayList<String> page_names = new ArrayList<>();
        ArrayList<Integer> page_ids = new ArrayList<>();
        page_names.add("(none)");
        Cursor pages = DatabaseManager.getPages(project_id);
        while(pages.moveToNext())
        {
            int page_id = pages.getInt(Page.ID.ordinal());
            if(this.page_id != page_id)
            {
                page_ids.add(pages.getInt(Page.ID.ordinal()));
                page_names.add(pages.getString(Page.NAME.ordinal()));
            }
        }
        spLink.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, page_names));
        if(element != null)
        {
            int page_destination_id = element.getInt(Element.PAGE_DESTINATION_ID.ordinal());
            if(page_destination_id != -1)
                spLink.setSelection(page_ids.indexOf(page_destination_id) + 1);
            else
                spLink.setSelection(0);
        }

        form.addView(spLink);
        alertDialog.setView(form);

        alertDialog.setPositiveButton("Confirm",
                (dialog,which) ->
                {
                    int _page_destination_id = spLink.getSelectedItemPosition() != TYPE_NONE ?
                            page_ids.get(spLink.getSelectedItemPosition() - 1) :
                            -1;

                    if(element == null)
                    {
                        DatabaseManager.insertElement(
                                spTypes.getSelectedItemPosition(),
                                fldLabel.getText()+"",
                                page_id,
                                _page_destination_id);
                        toast(this,"Element created!");
                    }
                    else
                    {
                        DatabaseManager.updateElement(
                                spTypes.getSelectedItemPosition(),
                                fldLabel.getText()+"",
                                element.getInt(Element.ID.ordinal()),
                                _page_destination_id);
                        toast(this,"Element updated!");
                    }
                    refreshActivity();
                });

        alertDialog.setNegativeButton("Cancel",
                (dialog, which) -> dialog.cancel());
    }

    private void refreshActivity()
    {
        Intent i = new Intent(this,PageActivity.class);
        i.putExtra("project_id",project_id);
        i.putExtra("page_id",page_id);
        i.putExtra("page_name",page_name);
        startActivity(i);
        this.finish();
    }

}
