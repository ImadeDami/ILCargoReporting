package com.interlog.ilcargoreporting;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final android.support.v4.app.Fragment first = new DcStock();
        final android.support.v4.app.Fragment second = new WcStock();

        findViewById(R.id.dcFragment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.layout, first);
                //fragmentTransaction.detach(first); // new 30 01 2020
                //fragmentTransaction.attach(first); // new 30 01 2020
                fragmentTransaction.commit();

            }
        });
        findViewById(R.id.wcFragment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.layout, second);
                // fragmentTransaction.detach(second); // new 30 01 2020
                // fragmentTransaction.attach(second); // new 30 01 2020
                fragmentTransaction.commit();
            }
        });
    }
    }

