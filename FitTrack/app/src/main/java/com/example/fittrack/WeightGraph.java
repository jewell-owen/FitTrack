package com.example.fittrack;

import static java.security.AccessController.getContext;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.model.ObjectValue;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class WeightGraph extends AppCompatActivity {

    private EditText weight;
    private Button b_addWeight, b_menu, b_step;
    private GraphView graph;
    int count;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();

    private int size;
    private String temp;
    private int point;
    DocumentSnapshot snapshot;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weightgraph);


        Random random = new Random();               //this is for a test
        graph = (GraphView) findViewById(R.id.graph);
        b_addWeight = (Button) findViewById(R.id.b_addWeight);
        b_step = (Button) findViewById(R.id.b_step);
        b_menu = (Button) findViewById(R.id.b_menu);
        weight = (EditText) findViewById(R.id.weight);
        graph.setTitle("Weight Graph");
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(10);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(300);

        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setXAxisBoundsManual(true);

        graph.getViewport().setScalable(true);  // activate horizontal zooming and scrolling
        graph.getViewport().setScrollable(true);  // activate horizontal scrolling
//        graph.getViewport().setScalableY(true);  // activate horizontal and vertical zooming and scrolling
//        graph.getViewport().setScrollableY(true);  // activate vertical scrolling

        db.collection("users").document(user.getUid()).collection("weight")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        QuerySnapshot querySnapshot = task.getResult();

                        if (!querySnapshot.isEmpty()) {
                            int documentCount = querySnapshot.size();
                            size = documentCount;
//                            count = size;

                            for (DocumentSnapshot document : querySnapshot) {
                                //get weight from database
                                if (snapshot == null) {
                                    snapshot = document;
                                }
                                temp = (String) document.get("weight").toString();
                                Log.d("TEST", temp );
                                point = Integer.parseInt(temp);


                                PointsGraphSeries<DataPoint> series = new PointsGraphSeries<>(new DataPoint[]{
                                        new DataPoint(count, point),
                                });
                                graph.addSeries(series);
                                if(temp != null) {
                                    count++;
                                }

                            }
                        } else {
                            Log.w("Firestore", "No logged weights found.");
                        }
                    } else {
                        Log.e("Firestore", "Error fetching logged weights", task.getException());
                    }
                });


    }

    public void onButton(View view) {

        String hold = (String) weight.getText().toString();


        int t = Integer.parseInt(hold);
        if (count == 9) {
            graph.removeAllSeries();
            count = 0;
        }

        PointsGraphSeries<DataPoint> series = new PointsGraphSeries<>(new DataPoint[]{
                new DataPoint(count , t),
        });

        DocumentReference exerciseRef = db.collection("users")
                .document(user.getUid())
                .collection("weight")
                .document(snapshot.getId());
        Map<String, Object> updates = new HashMap<>();
        updates.put("weight",  t);
        db.collection("users").document(user.getUid()).collection("weight")
                .add(updates);
        graph.addSeries(series);
        count ++;
    }

    public void onMenu(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    public void onStep(View view){
        Intent intent;
        intent = new Intent(this, StepGraph.class);
        startActivity(intent);

    }
}
