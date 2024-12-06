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

public class WeightGraph extends AppCompatActivity {

    private EditText weight;
    private Button b_addWeight;
    private GraphView graph;
    int count;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weightgraph);


        Random random = new Random();               //this is for a test
        graph = (GraphView) findViewById(R.id.graph);
        b_addWeight = (Button) findViewById(R.id.b_addWeight);
        weight = (EditText) findViewById(R.id.weight);
        graph.setTitle("Weight Graph");


//        for (int i = 1; i < 9; i++) {
//            PointsGraphSeries<DataPoint> series = new PointsGraphSeries<>(new DataPoint[]{
//                    new DataPoint(i, random.nextInt(2) + 135),
//            });
//            graph.addSeries(series);
//        }
//        graph.addSeries(series);
    }

    public void onButton(View view) {

        String hold = (String) weight.getText().toString();

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
