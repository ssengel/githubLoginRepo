package com.example.ssengel.loginapp01.Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ssengel.loginapp01.Constant.ServerURL;
import com.example.ssengel.loginapp01.Model.BLEScannerImpl;
import com.example.ssengel.loginapp01.Model.User;
import com.example.ssengel.loginapp01.R;

public class MainActivity extends AppCompatActivity {

    private String userId= LoginActivity.USER.getId();
    private String userName= LoginActivity.USER.getName();
    private String userLastName= LoginActivity.USER.getLastName();
    private ListView listViewDevices;
    private Button btnStopScan;
    private Button btnLowScan;
    private Button btnFastScan;
    private TextView txtNearestDevice;
    private BluetoothManager btManager;
    private BLEScannerImpl mBLEScannerImpl;

    private void initVars(){
        btManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBLEScannerImpl = new BLEScannerImpl(btManager,this);
    }

    private void initViews(){
        listViewDevices = (ListView) findViewById(R.id.lstRssi);
        btnStopScan = (Button) findViewById(R.id.btnScanStop);
        btnLowScan = (Button) findViewById(R.id.btnLowScan);
        btnFastScan = (Button) findViewById(R.id.btnFastScan);
        txtNearestDevice = (TextView) findViewById(R.id.txtNearestDevice);
    }

    private void initListeners(){
        btnLowScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBLEScannerImpl.scanLeDevice("LOW");
            }
        });
        btnFastScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBLEScannerImpl.scanLeDevice("FAST");
            }
        });
        btnStopScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBLEScannerImpl.scanLeDevice("STOP");
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)

    private void checkPermissions(){
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "BLE Not Supported",
                    Toast.LENGTH_SHORT).show();
            finish();
        }

        if (btManager.getAdapter() != null && !btManager.getAdapter().isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent,1);
        }

        if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("This app needs location access");
            builder.setMessage("Please grant location access so this app can detect peripherals.");
            builder.setPositiveButton(android.R.string.ok, null);
            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                }
            });
            builder.show();
        }
    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //izin kontrolleri
        initVars();
        checkPermissions();


        //degisken ve view atamalari
        initViews();
        initListeners();
        mBLEScannerImpl.updatelistRssi(ServerURL.BEACONFRAMES,LoginActivity.USER.getId());

    }
}