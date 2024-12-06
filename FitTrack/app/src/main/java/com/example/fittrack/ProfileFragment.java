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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends Fragment implements FavoriteExerciseAdapter.OnExerciseSelectedListener {

    private static final String TAG = "ProfileFragment";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();

    private ImageButton logoutButton;
    private TextView profileUserEmailTv, profileFriendIdTv, profileGoalTv;
    private EditText profileGoalEt;
    private ImageButton editGoalBtn, saveGoalBtn;
    private Button favoriteCustomExerciseBtn, favoriteLibraryExerciseBtn;
    private RecyclerView myFavoriteExerciseRecyclerView;

    private Query mQueryExercises;
    private FavoriteExerciseAdapter mAdapterExercises;
    private String favoriteId = "";

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        initializeUI(view);
        setupRecyclerView();
        fetchFavoriteExercises();

        return view;
    }

    private void initializeUI(View view) {
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

        logoutButton.setOnClickListener(v -> handleLogout());
        editGoalBtn.setOnClickListener(v -> enableGoalEditing());
        saveGoalBtn.setOnClickListener(v -> saveGoalToFirestore());
        favoriteLibraryExerciseBtn.setOnClickListener(v -> openLibraryExerciseSelector());
        favoriteCustomExerciseBtn.setOnClickListener(v -> openCustomExerciseSelector());

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



        if (user != null) {
            profileUserEmailTv.setText(user.getEmail());
            profileFriendIdTv.setText(user.getUid());
        }
    }

    private void setupRecyclerView() {
        myFavoriteExerciseRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mAdapterExercises = new FavoriteExerciseAdapter(mQueryExercises, this) {
            @Override
            protected void onDataChanged() {
                if (getItemCount() == 0) {
                    myFavoriteExerciseRecyclerView.setVisibility(View.GONE);
                } else {
                    myFavoriteExerciseRecyclerView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            protected void onError(FirebaseFirestoreException e) {
                Log.e(TAG, "Error loading data", e);
            }
        };
        myFavoriteExerciseRecyclerView.setAdapter(mAdapterExercises);
    }

    private void fetchFavoriteExercises() {
        db.collection("users").document(user.getUid()).collection("favorite")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                            favoriteId = doc.getId();
                            break;
                        }
                        mQueryExercises = db.collection("users").document(user.getUid()).collection("favorite");
                        mAdapterExercises.setQuery(mQueryExercises);
                    } else {
                        createPlaceholderFavorite();
                    }
                })
                .addOnFailureListener(e -> Log.e(TAG, "Error fetching favorite exercises", e));
    }

    private void createPlaceholderFavorite() {
        Map<String, Object> placeholder = new HashMap<>();
        placeholder.put("name", "Select a favorite exercise");

        db.collection("users").document(user.getUid()).collection("favorite")
                .add(placeholder)
                .addOnSuccessListener(documentReference -> {
                    favoriteId = documentReference.getId();
                    mQueryExercises = db.collection("users").document(user.getUid()).collection("favorite");
                    mAdapterExercises.setQuery(mQueryExercises);
                })
                .addOnFailureListener(e -> Log.e(TAG, "Error creating placeholder favorite", e));
    }

    private void enableGoalEditing() {
        saveGoalBtn.setVisibility(View.VISIBLE);
        editGoalBtn.setVisibility(View.GONE);
        profileGoalEt.setVisibility(View.VISIBLE);
        profileGoalTv.setVisibility(View.INVISIBLE);
    }

    private void saveGoalToFirestore() {
        String newGoal = profileGoalEt.getText().toString();

        if (user != null) {
            DocumentReference userRef = db.collection("users").document(user.getUid());
            userRef.update("goal", newGoal)
                    .addOnSuccessListener(aVoid -> {
                        profileGoalTv.setText(newGoal);
                        profileGoalTv.setVisibility(View.VISIBLE);
                        profileGoalEt.setVisibility(View.INVISIBLE);
                        editGoalBtn.setVisibility(View.VISIBLE);
                        saveGoalBtn.setVisibility(View.GONE);
                    })
                    .addOnFailureListener(e -> Log.e(TAG, "Error updating goal", e));
        }
    }

    private void handleLogout() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);
        if (getActivity() != null) {
            getActivity().finish();
        }
    }

    private void openLibraryExerciseSelector() {
        SelectLibraryExerciseFragment fragment = new SelectLibraryExerciseFragment();
        Bundle args = new Bundle();
        args.putString("id", favoriteId);
        args.putString("workoutType", "favoriteExercise");
        fragment.setArguments(args);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.flFragment, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void openCustomExerciseSelector() {
        SelectCustomExerciseFragment fragment = new SelectCustomExerciseFragment();
        Bundle args = new Bundle();
        args.putString("id", favoriteId);
        args.putString("workoutType", "favoriteExercise");
        fragment.setArguments(args);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.flFragment, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onExerciseMoreInfo(DocumentSnapshot exercise) {
        ViewExerciseFragment fragment = new ViewExerciseFragment();
        Bundle args = new Bundle();
        args.putString("name", exercise.getString("name"));
        args.putString("type", exercise.getString("type"));
        args.putString("muscle", exercise.getString("muscle"));
        args.putString("equipment", exercise.getString("equipment"));
        args.putString("difficulty", exercise.getString("difficulty"));
        args.putString("instructions", exercise.getString("instructions"));
        fragment.setArguments(args);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.flFragment, fragment)
                .addToBackStack(null)
                .commit();
    }
}
