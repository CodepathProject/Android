package com.codepath.project.android.activities;



import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.codepath.project.android.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;


public class PlotActivity  extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plot);
        GraphView graph = (GraphView) findViewById(R.id.graph);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6)
        });
        series.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        series.setColor(getResources().getColor(R.color.white));
        graph.addSeries(series);
        graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.NONE);
        graph.getGridLabelRenderer().setHighlightZeroLines(true);
        graph.getGridLabelRenderer().setHorizontalLabelsVisible(false);
        graph.getGridLabelRenderer().setVerticalLabelsVisible(false);
    }
}