package com.example.fittrack;

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
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class StepGraph extends AppCompatActivity {
    private EditText steps;
    private Button b_addSteps, b_menu, b_weight;
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
        setContentView(R.layout.activity_graph);
        count = 0;

        Random random = new Random();               //this is for a test
        graph = (GraphView) findViewById(R.id.graph);
        b_addSteps = (Button) findViewById(R.id.b_addSteps);
        b_weight = (Button) findViewById(R.id.b_weight);
        b_menu = (Button) findViewById(R.id.b_menu);
        steps = (EditText) findViewById(R.id.steps);

        graph.setTitle("Step Graph");
        graph.getViewport().setScalable(true);  // activate horizontal zooming and scrolling
        graph.getViewport().setScrollable(true);  // activate horizontal scrolling
        graph.getViewport().setScalableY(true);  // activate horizontal and vertical zooming and scrolling
        graph.getViewport().setScrollableY(true);  // activate vertical scrolling


        db.collection("users").document(user.getUid()).collection("steps")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        QuerySnapshot querySnapshot = task.getResult();

                        if (!querySnapshot.isEmpty()) {
                            int documentCount = querySnapshot.size();
                            size = documentCount;
                            count = size;

                            for (DocumentSnapshot document : querySnapshot) {
                                //get steps from database
                                if (snapshot == null) {
                                    snapshot = document;
                                }
                                temp = (String) document.get("steps").toString();
                                Log.d("TEST", temp );
                                point = Integer.parseInt(temp);
//                                point = 13;

                                if (count == 9) {
                                    count = 0;
                                    graph.removeAllSeries();
                                }
                                PointsGraphSeries<DataPoint> series = new PointsGraphSeries<>(new DataPoint[]{
                                        new DataPoint(count, point),
                                });
                                graph.addSeries(series);
                                if(temp != null) {
                                    count++;
                                }

                            }
                        } else {
                            Log.w("Firestore", "No logged steps found.");
                        }
                    } else {
                        Log.e("Firestore", "Error fetching logged steps", task.getException());
                    }
                });

    }
        public void onButton (View view){

            String hold = (String) steps.getText().toString();
            int t = Integer.parseInt(hold);

            if (count == 7) {
                graph.removeAllSeries();
                count = 0;
            }

            PointsGraphSeries<DataPoint> series = new PointsGraphSeries<>(new DataPoint[]{
                    new DataPoint(count, t),
            });
            graph.addSeries(series);
            DocumentReference exerciseRef = db.collection("users")
                    .document(user.getUid())
                    .collection("steps")
                    .document(snapshot.getId());
            Map<String, Object> updates = new HashMap<>();
            updates.put("steps",  t);
            db.collection("users").document(user.getUid()).collection("steps")
                    .add(updates);

            count ++;
        }

    public void onMenu(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    public void onWeight(View view){
        Intent intent;
        intent = new Intent(this, WeightGraph.class);
        startActivity(intent);

    }

}
