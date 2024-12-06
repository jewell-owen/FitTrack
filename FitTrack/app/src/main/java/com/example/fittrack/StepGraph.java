package com.example.fittrack;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.util.Random;

public class StepGraph extends AppCompatActivity {
    private EditText steps;
    private Button b_addSteps;
    private GraphView graph;
    int count;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        count = 0;

        Random random = new Random();               //this is for a test
        graph = (GraphView) findViewById(R.id.graph);
        b_addSteps = (Button) findViewById(R.id.b_addSteps);
        steps = (EditText) findViewById(R.id.steps);
//        for (int i = 1; i < 9; i++) {
//            PointsGraphSeries<DataPoint> series = new PointsGraphSeries<>(new DataPoint[]{
//                    new DataPoint(i, 6000 + random.nextInt(4000)),
//            });
//            graph.addSeries(series);
//        }
    }
        public void onButton (View view){

            String hold = (String) steps.getText().toString();
            int t = Integer.parseInt(hold);

            if (count == 7) {
                graph.removeAllSeries();
                count = 0;
            }

            PointsGraphSeries<DataPoint> series = new PointsGraphSeries<>(new DataPoint[]{
                    new DataPoint(count +1, t),
            });
            graph.addSeries(series);
            count ++;
        }

}
