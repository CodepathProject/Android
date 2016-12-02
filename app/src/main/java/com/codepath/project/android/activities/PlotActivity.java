package com.codepath.project.android.activities;



import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.project.android.R;
import com.codepath.project.android.data.TestData;
import com.codepath.project.android.helpers.Constants;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.DecimalFormat;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;


public class PlotActivity  extends AppCompatActivity {

    @BindView(R.id.graph)
    GraphView graph;
    @BindView(R.id.product_name)
    TextView tvProductName;

    @BindView(R.id.percent_change)
    TextView tvPercentChange;
    @BindView(R.id.current_price)
    TextView tvCurrentPrice;
    @BindView(R.id.lowest_price)
    TextView tvLowestPrice;
    @BindView(R.id.highest_price)
    TextView tvHighestPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plot);
        ButterKnife.bind(this);
        String productName = getIntent().getStringExtra(Constants.PRODUCT_NAME);
        String productPrice = getIntent().getStringExtra(Constants.PRODUCT_PRICE);
        tvProductName.setText(productName);
        tvCurrentPrice.setText(productPrice);
        tvHighestPrice.setText(TestData.getMaxMinPrice(productPrice,"MAX"));
        tvLowestPrice.setText(TestData.getMaxMinPrice(productPrice,"MIN"));

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(TestData.getDataPoint());
        series.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        series.setColor(getResources().getColor(R.color.white));
        series.setThickness(10);
        series.setDataPointsRadius(2);
        graph.addSeries(series);
        graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.NONE);
        graph.getGridLabelRenderer().setHighlightZeroLines(true);
        graph.getGridLabelRenderer().setHorizontalLabelsVisible(true);
        graph.getGridLabelRenderer().setHorizontalLabelsColor(getResources().getColor(R.color.action_gray));
        graph.getGridLabelRenderer().setVerticalLabelsVisible(false);

        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
        staticLabelsFormatter.setHorizontalLabels(new String[] {"Jan", "Mar","May","Jul","Oct","Dec"});
        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
    }
}