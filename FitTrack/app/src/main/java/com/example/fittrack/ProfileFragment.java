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

import java.util.HashMap;
import java.util.Map;

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

    private Button favoriteCustomExerciseBtn;
    private Button favoriteLibraryExerciseBtn;

    private RecyclerView myFavoriteExerciseRecyclerView;

    private Query mQueryExercises;
    private FavoriteExerciseAdapter mAdapterExercises;

    private String favoriteId = "";

    public ProfileFragment() {
        // Required empty public constructor
    }

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
        favoriteLibraryExerciseBtn = view.findViewById(R.id.profile_favorite_exercise_library_btn);
        favoriteCustomExerciseBtn = view.findViewById(R.id.profile_favorite_exercise_custom_btn);
        myFavoriteExerciseRecyclerView = view.findViewById(R.id.profile_favorite_exercises_recycler);

        MainActivity mainActivity = (MainActivity) getActivity();

        myFavoriteExerciseRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));


        // Check if the favorite exercise query is set
        db.collection("users").document(user.getUid()).collection("favorite")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        // Log the retrieved favorite exercises
                        for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                            Log.d("Firestore", "Favorite Exercise: " + doc.getData());
                            favoriteId = doc.getId();
                            break;

                        }
                        // Set the query to get all favorite exercises
                        mQueryExercises = db.collection("users").document(user.getUid()).collection("favorite");
                        initSavedWorkoutExercisesRecyclerView();
                        myFavoriteExerciseRecyclerView.setAdapter(mAdapterExercises);
                    } else {
                        Map<String, Object> newWorkout = new HashMap<>();
                        newWorkout.put("name", "Select a favorite exercise");
                        final String[] newWorkoutID = {""};
                        db.collection("users").document(user.getUid()).collection("loggedWorkouts")
                                .add(newWorkout)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        newWorkoutID[0] = documentReference.getId();
                                        mQueryExercises = db.collection("users").document(user.getUid()).collection("favorite");
                                        favoriteId = newWorkoutID[0];
                                        initSavedWorkoutExercisesRecyclerView();
                                        myFavoriteExerciseRecyclerView.setAdapter(mAdapterExercises);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                    }
                                });
                    }
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Error fetching favorite exercises", e));

        profileUserEmailTv.setText(user.getEmail());
        profileFriendIdTv.setText(user.getUid());

        logoutButton.setOnClickListener(view1 -> {
            mainActivity.auth.signOut();
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
            mainActivity.finish();
        });

        editGoalBtn.setOnClickListener(view1 -> {
            saveGoalBtn.setVisibility(View.VISIBLE);
            editGoalBtn.setVisibility(View.GONE);
            profileGoalEt.setVisibility(View.VISIBLE);
            profileGoalTv.setVisibility(View.INVISIBLE);
        });

        saveGoalBtn.setOnClickListener(view1 -> {
            editGoalBtn.setVisibility(View.VISIBLE);
            saveGoalBtn.setVisibility(View.GONE);
            profileGoalTv.setText(profileGoalEt.getText().toString());
            String newGoal = profileGoalEt.getText().toString();
            // Reference to the workout document in Firestore
            DocumentReference workoutRef = db.collection("users").document(user.getUid());

            // Create a map with the updated name
            Map<String, Object> updates = new HashMap<>();
            updates.put("goal", newGoal);

            // Update the "goal" field in Firestore
            workoutRef.update(updates)
                    .addOnSuccessListener(aVoid -> {
                        profileGoalTv.setVisibility(View.VISIBLE);
                        profileGoalEt.setVisibility(View.INVISIBLE);
                        profileGoalEt.setText("");
                    })
                    .addOnFailureListener(e -> Log.e("Firestore", "Error updating goal", e));
        });

        favoriteLibraryExerciseBtn.setOnClickListener(view1 -> {
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
        });

        favoriteCustomExerciseBtn.setOnClickListener(view1 -> {
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
        });

        return view;
    }

    private void initSavedWorkoutExercisesRecyclerView() {
        if (mQueryExercises == null) {
            Log.w("TAG", "No query, not initializing RecyclerView");
            return;
        }

        mAdapterExercises = new FavoriteExerciseAdapter(mQueryExercises, this) {
            @Override
            protected void onDataChanged() {
                Log.d("RecyclerView", "Adapter item count: " + getItemCount());
                if (getItemCount() == 0) {
                    myFavoriteExerciseRecyclerView.setVisibility(View.GONE);
                    Log.d("RecyclerView", "No items to display");
                } else {
                    myFavoriteExerciseRecyclerView.setVisibility(View.VISIBLE);
                    Log.d("RecyclerView", "Data loaded successfully");
                }
            }

            @Override
            protected void onError(FirebaseFirestoreException e) {
                Log.e("RecyclerView", "Error loading data", e);
            }
        };

        myFavoriteExerciseRecyclerView.setAdapter(mAdapterExercises);
    }

    @Override
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