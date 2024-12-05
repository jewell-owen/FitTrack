package com.example.fittrack;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.fittrack.adapter.ExerciseAdapter;
import com.example.fittrack.adapter.FavoriteExerciseAdapter;
import com.example.fittrack.workout.SelectCustomExerciseFragment;
import com.example.fittrack.workout.SelectLibraryExerciseFragment;
import com.example.fittrack.workout.ViewExerciseFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener, FavoriteExerciseAdapter.OnExerciseSelectedListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();

    private String mParam1;
    private String mParam2;

    private ImageButton logoutButton;

    private TextView profileUserEmailTv;
    private TextView profileFriendIdTv;

    private ImageButton editGoalBtn;
    private ImageButton saveGoalBtn;
    private TextView profileGoalTv;
    private EditText profileGoalEt;

    private TextView favoriteExerciseTextView;

    private Button favoriteCustomExerciseBtn;
    private Button favoriteLibraryExerciseBtn;

    private RecyclerView myFavoriteExerciseRecyclerView;

    private Query mQueryExercises;
    private FavoriteExerciseAdapter mAdapterExercises;

    private String favoriteId = "";

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        logoutButton = view.findViewById(R.id.profile_logout_btn);
        profileUserEmailTv = view.findViewById(R.id.profile_email_address_tv);
        profileFriendIdTv = view.findViewById(R.id.profile_friend_id_tv);
        profileGoalTv = view.findViewById(R.id.profile_goal_tv);
        profileGoalEt = view.findViewById(R.id.profile_goal_et);
        editGoalBtn = view.findViewById(R.id.profile_edit_btn);
        saveGoalBtn = view.findViewById(R.id.profile_save_btn);
        favoriteExerciseTextView = view.findViewById(R.id.profile_favorite_exercise_tv);
        favoriteCustomExerciseBtn = view.findViewById(R.id.profile_favorite_exercise_library_btn);
        favoriteLibraryExerciseBtn = view.findViewById(R.id.profile_favorite_exercise_custom_btn);
        myFavoriteExerciseRecyclerView = view.findViewById(R.id.profile_favorite_exercises_recycler);


        MainActivity mainActivity = (MainActivity) getActivity();

       db.collection("users").document(user.getUid()).get()
               .addOnSuccessListener(documentSnapshot -> {
                   if (documentSnapshot.exists()) {
                       // If the document exists, get the workout name and assign it to the TextView
                       String goal = documentSnapshot.getString("goal");
                       if (goal != null) {
                           profileGoalTv.setText(goal);

                       } else {
                           profileGoalTv.setText("Set a Goal! Writing a goal down significantly increases the likelihood of achieving it!");
                       }
                   } else {
                       // If the document does not exist, set the TextView to "None"
                       profileGoalTv.setText("Set a Goal! Writing a goal down significantly increases the likelihood of achieving it!");
                   }
               })
               .addOnFailureListener(e -> {
                   Log.e("Firestore", "Error fetching planned workout", e);
                   // Handle failure by setting a fallback value
                   profileGoalTv.setText("Set a Goal! Writing a goal down significantly increases the likelihood of achieving it!");
               });

        db.collection("users").document(user.getUid()).collection("favorite")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            // Assuming we take the first favorite exercise
                            DocumentSnapshot firstFavorite = queryDocumentSnapshots.getDocuments().get(0);
                            String exerciseName = firstFavorite.getString("name"); // Adjust the key based on Firestore structure
                            if (exerciseName != null) {
                                favoriteExerciseTextView.setVisibility(View.INVISIBLE);
                                myFavoriteExerciseRecyclerView.setVisibility(View.VISIBLE);
                                favoriteId = firstFavorite.getId();
                            } else {
                                favoriteExerciseTextView.setText("Select A Favorite Exercise");
                                favoriteExerciseTextView.setVisibility(View.VISIBLE);
                                myFavoriteExerciseRecyclerView.setVisibility(View.INVISIBLE);
                            }
                        } else {
                            favoriteExerciseTextView.setText("Select A Favorite Exercise");
                            favoriteExerciseTextView.setVisibility(View.VISIBLE);
                            myFavoriteExerciseRecyclerView.setVisibility(View.INVISIBLE);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Firestore", "Error fetching favorite exercises", e);
                        favoriteExerciseTextView.setText("Select A Favorite Exercise!");
                        favoriteExerciseTextView.setVisibility(View.VISIBLE);
                        myFavoriteExerciseRecyclerView.setVisibility(View.INVISIBLE);
                    }
                });



        myFavoriteExerciseRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        myFavoriteExerciseRecyclerView.setAdapter(mAdapterExercises);

        profileUserEmailTv.setText(user.getEmail());
        profileFriendIdTv.setText(user.getUid());

        mQueryExercises =  db.collection("users").document(user.getUid()).collection("favorite");
        initSavedWorkoutExercisesRecyclerView();


        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.auth.signOut();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
                mainActivity.finish();

            }
        });


        editGoalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveGoalBtn.setVisibility(View.VISIBLE);
                editGoalBtn.setVisibility(View.GONE);
                profileGoalEt.setVisibility(View.VISIBLE);
                profileGoalTv.setVisibility(View.INVISIBLE);

            }
        });

        saveGoalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editGoalBtn.setVisibility(View.VISIBLE);
                saveGoalBtn.setVisibility(View.GONE);
                profileGoalTv.setText(profileGoalEt.getText().toString());
                String newGoal = profileGoalEt.getText().toString();
                // Reference to the workout document in Firestore
                DocumentReference workoutRef = db.collection("users")
                        .document(user.getUid());

                // Create a map with the updated name
                Map<String, Object> updates = new HashMap<>();
                updates.put("goal", newGoal);

                // Update the "goal" field in Firestore
                workoutRef.update(updates)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Close the fragment/activity upon successful update
                                profileGoalTv.setVisibility(View.VISIBLE);
                                profileGoalEt.setVisibility(View.INVISIBLE);
                                profileGoalEt.setText("");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Log or handle the failure
                            }
                        });
            }
        });


        favoriteLibraryExerciseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectLibraryExerciseFragment exerciseSelect = new SelectLibraryExerciseFragment();
                Bundle args = new Bundle();
                args.putString("id", favoriteId);
                args.putString("workoutType", "favoriteExercise");
                exerciseSelect.setArguments(args);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, exerciseSelect)
                        .addToBackStack(null)
                        .commit();
            }
        });

        favoriteCustomExerciseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectCustomExerciseFragment customExercise = new SelectCustomExerciseFragment();
                Bundle args = new Bundle();
                args.putString("id", favoriteId);
                args.putString("workoutType", "favoriteExercise");
                customExercise.setArguments(args);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, customExercise)
                        .addToBackStack(null)
                        .commit();
            }
        });




        return view;
    }

    private void initSavedWorkoutExercisesRecyclerView() {
        if (mQueryExercises == null) {
            Log.w("TAG", "No query, not initializing RecyclerView");
        }

        mAdapterExercises = new FavoriteExerciseAdapter(mQueryExercises, this) {

            @Override
            protected void onDataChanged() {
                // Show/hide content if the query returns empty.
                if (myFavoriteExerciseRecyclerView.getVisibility() == View.VISIBLE){
                    //Handle update
                }

            }

            @Override
            protected void onError(FirebaseFirestoreException e) {
            }
        };

        //myWorkoutsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        myFavoriteExerciseRecyclerView.setAdapter(mAdapterExercises);
    }

    public void onExerciseMoreInfo(DocumentSnapshot exercise) {
        Bundle bundle = new Bundle();
        bundle.putString("name", exercise.getString("name"));
        bundle.putString("type", exercise.getString("type"));
        bundle.putString("muscle", exercise.getString("muscle"));
        bundle.putString("equipment", exercise.getString("equipment"));
        bundle.putString("difficulty", exercise.getString("difficulty"));
        bundle.putString("instructions", exercise.getString("instructions"));
        ViewExerciseFragment viewExerciseFragment = new ViewExerciseFragment();
        viewExerciseFragment.setArguments(bundle);
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.flFragment, viewExerciseFragment)
                .addToBackStack(null)
                .commit();

    }

}