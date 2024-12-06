package com.example.step;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.util.Random;

public class StepGraph extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);


        Random random = new Random();               //this is for a test
        GraphView graph = (GraphView) findViewById(R.id.graph);
        for (int i = 1; i < 9; i++) {
            PointsGraphSeries<DataPoint> series = new PointsGraphSeries<>(new DataPoint[]{
                    new DataPoint(i, 6000 + random.nextInt(4000)),
            });
            graph.addSeries(series);
        }

        /**
         * could also plot each point with respect to the previous point, tho that may noy work

         * new DataPoint(0, 6000 + random.nextInt(4000)),
         * new DataPoint(1, 6000 + random.nextInt(4000)),
         * graph.add()

         * new DataPoint(1, 6000 + random.nextInt(4000)),
         * new DataPoint(2, 6000 + random.nextInt(4000)),
         * graph.add()
         * ...
         */
//        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{
//                new DataPoint(0, 6000 + random.nextInt(4000)),
//                new DataPoint(1, 6000 + random.nextInt(4000)),
//                new DataPoint(2, 6000 + random.nextInt(4000)),
//                new DataPoint(3, 6000 + random.nextInt(4000)),
//                new DataPoint(4, 6000 + random.nextInt(4000)),
//                new DataPoint(5, 6000 + random.nextInt(4000)),
//                new DataPoint(6, 6000 + random.nextInt(4000)),
//                new DataPoint(7, 6000 + random.nextInt(4000)),
//                new DataPoint(8, 6000 + random.nextInt(4000)),
//
//        });
//        graph.addSeries(series);
    }
}
