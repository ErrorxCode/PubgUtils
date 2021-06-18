package com.java.android.libraries;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.pubg.utils.OneTimeDialogInterface;
import com.pubg.utils.OneTimeEventInterface;
import com.pubg.utils.PubgUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener{

    PubgUtils utils;
    boolean Return;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button pak = findViewById(R.id.pak);
        Button sav = findViewById(R.id.sav);
        Button firewall = findViewById(R.id.firewall);
        Button clear = findViewById(R.id.clear);
        Button oneTime = findViewById(R.id.oneTime);

        utils = new PubgUtils(this,PubgUtils.GLOBAL);

        pak.setOnClickListener(this);
        pak.setOnLongClickListener(this);
        sav.setOnLongClickListener(this);
        sav.setOnClickListener(this);
        firewall.setOnLongClickListener(this);
        firewall.setOnClickListener(this);
        clear.setOnLongClickListener(this);
        clear.setOnClickListener(this);
        oneTime.setOnLongClickListener(this);
        oneTime.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String file = Environment.getExternalStorageDirectory().getPath() + "/Files.zip";
        switch (v.getId()){
            case R.id.pak:
                utils.copyPak(file);
                break;
            case R.id.sav:
                utils.copySav(file);
                break;
            case R.id.clear:
                utils.clearCaches();
                break;
            case R.id.firewall:
                utils.activateFirewall();
                break;
            case R.id.oneTime:
                utils.oneTimeDialog(new OneTimeDialogInterface() {
                @Override
                public boolean OneTimeDialog(AlertDialog.Builder builder) {
                    builder.setPositiveButton("ok",null).setNeutralButton("later", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Return =  true;
                        }
                    }).create().show();
                    return Return;
                }
            });
        }
    }

    @Override
    public boolean onLongClick(View v) {
        String file = "Files.zip";
        switch (v.getId()){
            case R.id.pak:
                utils.deletePak(file);
                break;
            case R.id.sav:
                utils.deleteSav(file);
                break;
            case R.id.clear:
                utils.clearData();
                break;
            case R.id.firewall:
                utils.deactivateFirewall();
                break;
            case R.id.oneTime:
                utils.oneTimeEvent(new OneTimeEventInterface() {
                @Override
                public boolean OneTimeEvent() {
                    Toast.makeText(MainActivity.this, "Event occurred", Toast.LENGTH_SHORT).show();
                    return true;
                }
            });
        }
        return true;
    }
}