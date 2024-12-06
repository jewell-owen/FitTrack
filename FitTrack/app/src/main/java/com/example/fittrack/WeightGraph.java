package com.example.step;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.util.Random;

public class WeightGraph extends AppCompatActivity {

    private GraphView graph;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);


        Random random = new Random();               //this is for a test
        graph = (GraphView) findViewById(R.id.graph);
        graph.setTitle("Weight Graph");

        for (int i = 1; i < 9; i++) {
            PointsGraphSeries<DataPoint> series = new PointsGraphSeries<>(new DataPoint[]{
                    new DataPoint(i, random.nextInt(2) + 135),
            });
            graph.addSeries(series);
        }
//        graph.addSeries(series);
    }



}
