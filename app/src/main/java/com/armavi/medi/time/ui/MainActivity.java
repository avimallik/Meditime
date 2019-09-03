package com.armavi.medi.time.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.armavi.medi.time.Language;
import com.armavi.medi.time.R;

public class MainActivity extends Language {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadLocale();
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(Color.parseColor("#F44336"));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the main_menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {
            case R.id.english:
                //your action
                changeLang("en");
                Intent iEn = getApplicationContext().getPackageManager()
                        .getLaunchIntentForPackage(getApplicationContext().getPackageName() );

                iEn.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK );
                startActivity(iEn);

                break;
            case R.id.bangla:
                //your action
                changeLang("bn");
                Intent iBn = getApplicationContext().getPackageManager()
                        .getLaunchIntentForPackage(getApplicationContext().getPackageName() );

                iBn.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK );
                startActivity(iBn);

                break;
            case R.id.about:
                LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
                View promptView = layoutInflater.inflate(R.layout.about_dialog, null);
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                alertDialogBuilder.setView(promptView);

                final ImageView gitHubSocio = (ImageView) promptView.findViewById(R.id.githubSocio);
                final ImageView facebookSocio = (ImageView) promptView.findViewById(R.id.facebookSocio);
                final ImageView linkedInSocio = (ImageView) promptView.findViewById(R.id.linkedinSocio);

                gitHubSocio.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Uri uri = Uri.parse("https://github.com/avimallik"); // missing 'http://' will cause crashed
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }
                });

                facebookSocio.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Uri uri = Uri.parse("https://www.facebook.com/prolog.fortron"); // missing 'http://' will cause crashed
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }
                });

                linkedInSocio.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Uri uri = Uri.parse("https://www.linkedin.com/in/arunav-mallik-avi-31a34b152/"); // missing 'http://' will cause crashed
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }
                });

                alertDialogBuilder.setCancelable(false)
                        .setPositiveButton(getResources().getString(R.string.ok_dio), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });

                // create an alert dialog
                AlertDialog alert = alertDialogBuilder.create();
                alert.show();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

}
