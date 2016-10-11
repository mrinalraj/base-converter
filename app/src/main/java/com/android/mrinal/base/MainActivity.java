package com.android.mrinal.base;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import com.onesignal.OneSignal;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Activity context;
    ProgressDialog pd;
    int newVersionCode;
    String newVersionName;
    String newChangelogs;

    private static boolean activityStarted;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OneSignal.startInit(this).init();
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar=getSupportActionBar();
        actionBar.setElevation(0);

        OneSignal.enableNotificationsWhenActive(true);


        context=this;

        if (   activityStarted
                && getIntent() != null
                && (getIntent().getFlags() & Intent.FLAG_ACTIVITY_REORDER_TO_FRONT) != 0) {
            finish();
            return;
        }

        activityStarted = true;


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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final EditText binary=(EditText)findViewById(R.id.binary);
        final EditText decimal=(EditText)findViewById(R.id.decimal);
        final EditText octal=(EditText)findViewById(R.id.octal);
        final EditText hex=(EditText)findViewById(R.id.hex);
        binary.setFilters(new InputFilter[] {new InputFilter.LengthFilter(64)});
        decimal.setFilters(new InputFilter[] {new InputFilter.LengthFilter(19)});
        octal.setFilters(new InputFilter[] {new InputFilter.LengthFilter(22)});
        hex.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});

        binary.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String b = binary.getText().toString().trim();
                try {
                    if (binary.hasFocus() && b.length() != 0) {
                        long bin = Long.parseLong(b, 2);
                        decimal.setText(Long.toString(bin));
                        octal.setText(Long.toOctalString(bin));
                        hex.setText(Long.toHexString(bin));
                    } else if (binary.hasFocus() && b.length() == 0) {
                        decimal.setText("");
                        octal.setText("");
                        hex.setText("");
                    }
                } catch (NumberFormatException er) {
                    numberLimit();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        decimal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String d = decimal.getText().toString().trim();
                try {
                    if (decimal.hasFocus() && d.length() != 0) {
                        long dec = Long.parseLong(d);
                        binary.setText(Long.toBinaryString(dec));
                        octal.setText(Long.toOctalString(dec));
                        hex.setText(Long.toHexString(dec));
                    } else if (decimal.hasFocus() && d.length() == 0) {
                        binary.setText("");
                        octal.setText("");
                        hex.setText("");
                    }
                }
                catch (NumberFormatException er){
                    numberLimit();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        octal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String o = octal.getText().toString().trim();
                try {
                    if (octal.hasFocus() && o.length() != 0) {
                        long oct = Long.parseLong(o, 8);
                        binary.setText(Long.toBinaryString(oct));
                        decimal.setText(Long.toString(oct));
                        hex.setText(Long.toHexString(oct));
                    } else if (octal.hasFocus() && o.length() == 0) {
                        binary.setText("");
                        decimal.setText("");
                        hex.setText("");
                    }
                } catch (NumberFormatException er) {
                    numberLimit();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        hex.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String h = hex.getText().toString().trim();
                try {
                    if (hex.hasFocus() && h.length() != 0) {
                        long hexa = Long.parseLong(h, 16);
                        binary.setText(Long.toBinaryString(hexa));
                        decimal.setText(Long.toString(hexa));
                        octal.setText(Long.toOctalString(hexa));
                    } else if (hex.hasFocus() && h.length() == 0) {
                        binary.setText("");
                        decimal.setText("");
                        octal.setText("");
                    }
                } catch (NumberFormatException er) {
                    numberLimit();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.changelogs) {
            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
            alertDialog.setTitle("Changelogs v2.1.1");
            alertDialog.setMessage(" • Completely redisgned UI \n\n • Added conversion for any bases \n\n • Major UI bug fixes");

            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            alertDialog.setButton("Check Update", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(MainActivity.this, "Checking for updates !", Toast.LENGTH_SHORT).show();
                    BackTask bt=new BackTask();
                    bt.execute("http://mrinalraj.com/VERSION/version_code.txt");

                        String versionName = BuildConfig.VERSION_NAME;
                        if (checkUpdate()==true) {
                            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                            alertDialog.setTitle("New version available");
                            alertDialog.setMessage("Download It");

                            alertDialog.setButton("Download", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Uri uri = Uri.parse("http://mrinalraj.com/BaseConverter.apk");
                                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                    startActivity(intent);
                                }
                            });
                        }


                }
            });

            alertDialog.show();
            return true;
        }
        if (id==R.id.share){
            Intent email=new Intent(Intent.ACTION_SEND);
            email.setType("plain/text");
            email.putExtra(Intent.EXTRA_TEXT, "download this cool new app www.mrinalraj.com/BaseConvereter.apk");
            startActivity(Intent.createChooser(email, "Choose app"));
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_primary) {

        } else if (id == R.id.nav_advance) {

        } else if (id == R.id.nav_about_app) {

        } else if (id == R.id.nav_faq) {

        } else if (id == R.id.nav_settings) {
            Intent intent= new Intent(MainActivity.this, Settings.class);
            MainActivity.this.startActivity(intent);
        } else if (id == R.id.nav_feedback) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void numberLimit(){
        Toast.makeText(MainActivity.this,"Number limit reached.", Toast.LENGTH_LONG).show();
    }

    public String getId(int n){
        final String[] idUser = new String[2];
        OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
            @Override
            public void idsAvailable(String userId, String registrationId) {
                idUser[0] = userId;
                if (registrationId != null)
                    idUser[1] = registrationId;
                else
                    idUser[1] = "Could not subscribe for push";

            }
        });
        return idUser[n];
    }

    private class BackTask extends AsyncTask<String,Integer,Void>{
        String text="";
        protected void onPreExecute(){
            super.onPreExecute();
            //display progress dialog
            pd = new ProgressDialog(context);
            pd.setTitle("Update");
            pd.setMessage("Checking for Update");
            pd.setCancelable(true);
            pd.setIndeterminate(false);
            pd.show();

        }



        protected Void doInBackground(String...params){
            URL url;
            try {
                //create url object to point to the file location on internet
                url = new URL(params[0]);
                //make a request to server
                HttpURLConnection con=(HttpURLConnection)url.openConnection();
                //get InputStream instance
                InputStream is=con.getInputStream();
                //create BufferedReader object
                BufferedReader br=new BufferedReader(new InputStreamReader(is));
                String line;
                int count=0;
                //read content of the file line by line
                while((line=br.readLine())!=null){
                    newVersionCode = Integer.parseInt(line);
                }

                br.close();

            }catch (Exception e) {
                e.printStackTrace();
                //close dialog if error occurs
                Toast.makeText(MainActivity.this, "Could not check for update", Toast.LENGTH_SHORT).show();
                if(pd!=null) pd.dismiss();
            }

            return null;

        }


        protected void onPostExecute(Void result){
            //close dialog
            if(pd!=null) {
                pd.dismiss();
            }

        }

    }

    public Boolean checkUpdate(){
        int versionCode = BuildConfig.VERSION_CODE;
        if (newVersionCode>versionCode){
            return true;
        }
        else{
            return false;
        }
    }
}
