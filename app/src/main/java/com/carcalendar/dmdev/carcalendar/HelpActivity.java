package com.carcalendar.dmdev.carcalendar;

import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import org.w3c.dom.Text;

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        TextView helpTxt = (TextView) findViewById(R.id.txt_help);
        helpTxt.setText(getString(R.string.act_help_text));

        ActionBar actionBar = getSupportActionBar();

        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }else{
            throw new NullPointerException("Action bar was not retrieved properly!");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO activity started from different task
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button (API < 15)
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
