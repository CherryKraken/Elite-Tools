package com.connorboyle.elitetools;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.connorboyle.elitetools.R;
import com.connorboyle.elitetools.models.System;

/**
 * Created by Connor Boyle on 22-Feb-18.
 */

public class SystemInfoActivity extends AppCompatActivity {

    System system;

    TextView txtSysEddbId;
    TextView txtSysEdsmId;
    TextView txtSysCoords;
    TextView txtSysPermit;
    TextView txtSysUpdated;
    TextView txtSysSimbad;
    TextView txtSysPopulation;
    TextView txtSysAllegiance;
    TextView txtSysSecurity;
    TextView txtSysReserves;
    TextView txtSysEconomy;
    TextView txtSysGovernment;
    TextView txtSysState;
    TextView txtSysPower;
    TextView txtSysPowerState;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.system_info_layout);
        Toolbar tb = (Toolbar) findViewById(R.id.toolbar3);
        setSupportActionBar(tb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        this.system = (System) getIntent().getSerializableExtra("systemInfo");
        this.setTitle(this.system.name);

        txtSysEddbId = (TextView) findViewById(R.id.txtSysEddbId);
        txtSysEdsmId = (TextView) findViewById(R.id.txtSysEdsmId);
        txtSysCoords = (TextView) findViewById(R.id.txtSysCoords);
        txtSysPermit = (TextView) findViewById(R.id.txtSysPermit);
        txtSysUpdated = (TextView) findViewById(R.id.txtSysUpdated);
        txtSysSimbad = (TextView) findViewById(R.id.txtSysSimbad);
        txtSysPopulation = (TextView) findViewById(R.id.txtSysPopulation);
        txtSysAllegiance = (TextView) findViewById(R.id.txtSysAllegiance);
        txtSysSecurity = (TextView) findViewById(R.id.txtSysSecurity);
        txtSysReserves = (TextView) findViewById(R.id.txtSysReserves);
        txtSysEconomy = (TextView) findViewById(R.id.txtSysEconomy);
        txtSysGovernment = (TextView) findViewById(R.id.txtSysGovernment);
        txtSysState = (TextView) findViewById(R.id.txtSysState);
        txtSysPower = (TextView) findViewById(R.id.txtSysPower);
        txtSysPowerState = (TextView) findViewById(R.id.txtSysPowerState);

        txtSysEddbId.setText("" + system.eddbId);
        txtSysEdsmId.setText("" + system.edsmId);
        txtSysCoords.setText(String.format("x: %.2f   y: %.2f   z: %.2f", system.x, system.y, system.z));
        txtSysPermit.setText(system.needsPermit ? "Yes" : "No");
        txtSysUpdated.setText(system.updatedAt);
        txtSysSimbad.setText(system.simbadRef);
        txtSysPopulation.setText("" + system.population);
        txtSysAllegiance.setText(system.allegiance);
        txtSysSecurity.setText(system.security);
        txtSysReserves.setText(system.reserves);
        txtSysEconomy.setText(system.primaryEconomy);
        txtSysGovernment.setText(system.government);
        txtSysState.setText(system.state);
        txtSysPower.setText(system.powerPlayLeader);
        txtSysPowerState.setText(system.powerPlayState);

        this.getLayoutInflater();
    }
}
