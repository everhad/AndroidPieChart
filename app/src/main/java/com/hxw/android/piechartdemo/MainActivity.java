package com.hxw.android.piechartdemo;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private static Random random = new Random();
    private PieChart pieChart1, pieChart2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pieChart1 = (PieChart) findViewById(R.id.pie_chart1);
        pieChart2 = (PieChart) findViewById(R.id.pie_chart2);
        pieChart2.setAnimMode(PieChart.ANIM_MODE_MULTI_SECTION);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshPieChart();
            }
        });
        refreshPieChart();
    }

    private void refreshPieChart() {
        ArrayList<Map.Entry<Double, Integer>> pieData = new ArrayList<>();
        pieData.add(new AbstractMap.SimpleEntry<Double, Integer>(random.nextInt(5000) + 0d, 0xFF71dabe));
        pieData.add(new AbstractMap.SimpleEntry<Double, Integer>(random.nextInt(5000) + 0d, 0xFFff6656));
        pieData.add(new AbstractMap.SimpleEntry<Double, Integer>(random.nextInt(5000) + 0d, 0xFF7bbef8));
        pieData.add(new AbstractMap.SimpleEntry<Double, Integer>(random.nextInt(5000) + 0d, 0xFF6b96f5));
        pieChart1.setPieData(pieData);
        pieChart2.setPieData(pieData);
    }
}
