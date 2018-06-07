package com.perezjquim.protodroid;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.SearchView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.perezjquim.protodroid.db.DatabaseManager;
import com.perezjquim.protodroid.db.Element;
import com.perezjquim.protodroid.db.Page;

import java.util.EnumSet;

public class PreviewActivity extends AppCompatActivity
{
    private int page_id;
    private String page_name;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        initPage();
        drawElements();
    }

    private void initPage()
    {
        Intent i = getIntent();
        page_id = i.getIntExtra("page_id",-1);
        page_name = i.getStringExtra("page_name");
        TextView title = findViewById(R.id.title);
        title.setText(page_name);
    }

    private void drawElements()
    {
        Cursor elements = DatabaseManager.getElements(page_id);
        LinearLayout screen = findViewById(R.id.screen);
        while(elements.moveToNext())
        {
            final int type = elements.getInt(Element.TYPE.ordinal());
            final String label = elements.getString(Element.LABEL.ordinal());
            final int page_destination_id = elements.isNull(Element.PAGE_DESTINATION_ID.ordinal()) ? -1 :
                    elements.getInt(Element.PAGE_DESTINATION_ID.ordinal());
            final String config = elements.getString(Element.CONFIG.ordinal());

            switch(Element.Types.values()[type])
            {
                case BUTTON:
                    Button b = new Button(this);
                    b.setText(label);
                    if(page_destination_id != -1)
                    {
                        b.setOnClickListener((v)->
                                jumpToPage(page_destination_id));
                    }
                    screen.addView(b);
                    break;

                case CHECKBOX:
                    CheckBox c = new CheckBox(this);
                    c.setText(label);
                    if(page_destination_id != -1)
                    {
                        c.setOnClickListener((v)->
                                jumpToPage(page_destination_id));
                    }
                    screen.addView(c);
                    break;

                case SWITCH:
                    Switch s = new Switch(this);
                    s.setText(label);
                    if(page_destination_id != -1)
                    {
                        s.setOnClickListener((v)->
                                jumpToPage(page_destination_id));
                    }
                    screen.addView(s);
                    break;

                case FIELD:
                    EditText e = new EditText(this);
                    e.setHint(label);
                    screen.addView(e);
                    break;

                case NUMBER_PICKER:
                    TextView lblNumberPicker = new TextView(this);
                    lblNumberPicker.setText(label);
                    NumberPicker p = new NumberPicker(this);
                    screen.addView(lblNumberPicker);
                    screen.addView(p);
                    break;

                case DATE_PICKER:
                    TextView lblDatePicker = new TextView(this);
                    lblDatePicker.setText(label);
                    DatePicker d = new DatePicker(this);
                    screen.addView(lblDatePicker);
                    screen.addView(d);
                    break;

                case TIME_PICKER:
                    TextView lblTimePicker = new TextView(this);
                    lblTimePicker.setText(label);
                    TimePicker t = new TimePicker(this);
                    screen.addView(lblTimePicker);
                    screen.addView(t);
                    break;

                case SEARCH_BAR:
                    SearchView bar = new SearchView(this);
                    bar.setQueryHint(label);
                    screen.addView(bar);
                    break;

                case RATING_BAR:
                    TextView lblRating = new TextView(this);
                    lblRating.setText(label);
                    RatingBar r = new RatingBar(this);
                    screen.addView(lblRating);
                    screen.addView(r);
                    break;

                case SEEK_BAR:
                    TextView lblSeek = new TextView(this);
                    lblSeek.setText(label);
                    SeekBar seekBar = new SeekBar(this);
                    screen.addView(lblSeek);
                    screen.addView(seekBar);
                    break;

                case RADIO_BUTTON:
                    RadioButton btnRadio = new RadioButton(this);
                    btnRadio.setText(label);
                    screen.addView(btnRadio);
                    break;

                default:
                    break;
            }
        }
    }

    private void jumpToPage(int page_destination_id)
    {
        Intent i = new Intent(this,PreviewActivity.class);
        i.putExtra("page_id",page_destination_id);
        i.putExtra("page_name",DatabaseManager
                .getIndividualPage(page_destination_id)
                .getString(Page.NAME.ordinal()));
        startActivity(i);
    }
}
