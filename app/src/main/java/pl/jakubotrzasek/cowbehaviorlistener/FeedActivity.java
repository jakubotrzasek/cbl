package pl.jakubotrzasek.cowbehaviorlistener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import java.util.ArrayList;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 */
public class FeedActivity extends Activity {

    private final static String TAG = "feed_Avtivity";
    private Intent iMainActivity;
    CowData cd = new CowData();
    ArrayList<CheckBox> cBoxes = new ArrayList<CheckBox>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent iFeedActivity = getIntent();
        iMainActivity = new Intent(getApplicationContext(), MainActivity.class);

        setContentView(R.layout.activity_feed);

        this.setCheckBoxName(R.id.cow0CheckBox, iFeedActivity.getStringExtra("cow0"));
        this.setCheckBoxName(R.id.cow1CheckBox, iFeedActivity.getStringExtra("cow1"));
        this.setCheckBoxName(R.id.cow2CheckBox, iFeedActivity.getStringExtra("cow2"));

        this.checkAction();
        this.buttonsActions();
    }

    private void setCheckBoxName(int id, String cowName) {
        CheckBox cb = (CheckBox) findViewById(id);
        if (cowName != null && cowName != "") {
            Log.d(TAG, cowName);
            cb.setText(cd.getCowName(cowName));

        } else {
            cb.setVisibility(View.INVISIBLE);
        }
    }


    private void buttonsActions() {
        Button button;
        try {

            button = (Button) findViewById(R.id.cancelButton);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    startActivity(iMainActivity);
                }
            });
        } catch (Exception e) {
            e.toString();
            Log.e(TAG, e.toString());
        }
        try {
            button = (Button) findViewById(R.id.saveButton);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String chosenCow = "";
                    CheckBox c;
                    c = (CheckBox) findViewById(R.id.cow0CheckBox);
                    if (c.isChecked()) {
                        chosenCow = c.getText().toString();
                    }
                    c = (CheckBox) findViewById(R.id.cow1CheckBox);
                    if (c.isChecked()) {
                        chosenCow = c.getText().toString();
                    }
                    c = (CheckBox) findViewById(R.id.cow2CheckBox);
                    if (c.isChecked()) {
                        chosenCow = c.getText().toString();
                    }
                    if (chosenCow != "") {
                        cd.saveCowData(chosenCow, ((EditText) findViewById(R.id.milkLiters)).getText().toString());
                    }
                    startActivity(iMainActivity);
                }
            });
        } catch (Exception e) {
            e.toString();
            Log.e(TAG, e.toString());
        }
    }

    public void checkAction() {
        CheckBox c;
        c = (CheckBox) findViewById(R.id.cow0CheckBox);
        c.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    CheckBox c;
                    c = (CheckBox) findViewById(R.id.cow1CheckBox);
                    c.setChecked(false);
                    c = (CheckBox) findViewById(R.id.cow2CheckBox);
                    c.setChecked(false);
                }

            }
        });
        c = (CheckBox) findViewById(R.id.cow1CheckBox);
        c.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    CheckBox c;
                    c = (CheckBox) findViewById(R.id.cow0CheckBox);
                    c.setChecked(false);
                    c = (CheckBox) findViewById(R.id.cow2CheckBox);
                    c.setChecked(false);
                }

            }
        });
        c = (CheckBox) findViewById(R.id.cow2CheckBox);
        c.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    CheckBox c;
                    c = (CheckBox) findViewById(R.id.cow0CheckBox);
                    c.setChecked(false);
                    c = (CheckBox) findViewById(R.id.cow1CheckBox);
                    c.setChecked(false);
                }

            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }


}
