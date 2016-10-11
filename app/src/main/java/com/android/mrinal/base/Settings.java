package com.android.mrinal.base;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.onesignal.OneSignal;

public class Settings extends AppCompatActivity {
    String id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent email = new Intent(Intent.ACTION_SEND);
                email.setType("plain/text");
                email.putExtra(android.content.Intent.EXTRA_SUBJECT, "About the Base Converter app");
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{"me@mrinalraj.com"});
                startActivity(Intent.createChooser(email, "Choose app to send the e-mail !"));
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Switch notification = (Switch)findViewById(R.id.notification);
        Switch advanced = (Switch)findViewById(R.id.advanced);
        Switch floating = (Switch)findViewById(R.id.floating_point);

        final SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        boolean tgpref = preferences.getBoolean("tgpref", true);  //default is true
        if (tgpref = true) //if (tgpref) may be enough, not sure
        {
            notification.setChecked(true);
        }
        else
        {
            notification.setChecked(false);
        }

        notification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("tgpref", true); // value to store
                    editor.commit();
                    OneSignal.setSubscription(true);
                    Toast.makeText(Settings.this, "Subscribed for push", Toast.LENGTH_SHORT).show();
                } else {
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("tgpref", false); // value to store
                    editor.commit();
                    OneSignal.setSubscription(false);
                    Toast.makeText(Settings.this, "Unsubscribed for push", Toast.LENGTH_SHORT).show();
                }
            }
        });

        OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
            @Override
            public void idsAvailable(String userId, String registrationId) {
                id = userId;
            }
        });

        TextView userid=(TextView)findViewById(R.id.userid);
        userid.setText(id);

    }

}
