package com.hxw.android.piechartdemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import static com.hxw.android.piechartdemo.R.*;

public class MainActivity extends Activity {
    private static Random random = new Random();
    private PieChart pieChart1, pieChart2, pieChart3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_main);
        pieChart1 = (PieChart) findViewById(id.pie_chart1);
        pieChart2 = (PieChart) findViewById(id.pie_chart2);
        pieChart3 = (PieChart) findViewById(id.pie_chart3);

        pieChart1.setAnimMode(PieChart.ANIM_MODE_SINGLE_SECTION);
        pieChart2.setAnimMode(PieChart.ANIM_MODE_MULTI_SECTION);
        pieChart3.setAnimMode(PieChart.ANIM_MODE_PROCEED);

        findViewById(id.root_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshPieChart();
            }
        });
    }

    private void refreshPieChart() {
        ArrayList<Map.Entry<Double, Integer>> pieData = new ArrayList<>();
        pieData.add(new AbstractMap.SimpleEntry<Double, Integer>(300d, 0xFF71dabe));
        pieData.add(new AbstractMap.SimpleEntry<Double, Integer>(600d, 0xFFff6656));
        pieData.add(new AbstractMap.SimpleEntry<Double, Integer>(1200d, 0xFF7bbef8));
        pieData.add(new AbstractMap.SimpleEntry<Double, Integer>(400d, 0xFF6b96f5));
        pieChart1.setPieData(pieData);
        pieChart2.setPieData(pieData);
        pieChart3.setPieData(pieData);
    }
}
