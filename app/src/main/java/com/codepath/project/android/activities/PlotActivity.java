package com.codepath.project.android.activities;



import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.codepath.project.android.R;
import com.codepath.project.android.helpers.Constants;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Random;


public class PlotActivity  extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plot);
        GraphView graph = (GraphView) findViewById(R.id.graph);

        String productName = getIntent().getStringExtra(Constants.PRODUCT_NAME);

        TextView tvProductName = (TextView)findViewById(R.id.product_name);
        tvProductName.setText(productName);

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(getDataPoint());
        series.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        series.setColor(getResources().getColor(R.color.white));
        graph.addSeries(series);
        graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.NONE);
        graph.getGridLabelRenderer().setHighlightZeroLines(true);
        graph.getGridLabelRenderer().setHorizontalLabelsVisible(false);
        graph.getGridLabelRenderer().setVerticalLabelsVisible(false);
    }

    private static final int DATA_SIZE = 100;

    private DataPoint[] getDataPoint(){
        DataPoint[] dataPoints = new DataPoint[DATA_SIZE];

        for(int i = 0; i < DATA_SIZE; i++){
            DataPoint d = new DataPoint(i, getRandom(i));
            dataPoints[i] = d;
        }
        return dataPoints;
    }

    private int getRandom(int n){
        int max = n + 1 + n/8;
        int min = n + 1 - n/8;

        Random ran = new Random();
        return ran.nextInt(max) + min;
    }
}