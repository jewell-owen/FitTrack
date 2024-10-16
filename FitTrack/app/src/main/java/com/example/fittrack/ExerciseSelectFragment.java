package com.example.fittrack;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;

public class ExerciseSelectFragment extends Fragment implements View.OnClickListener{

    private ImageButton exerciseSelectBackButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_exercise_select, container, false);

        exerciseSelectBackButton = view.findViewById(R.id.exercise_select_back_btn);

        exerciseSelectBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        return view;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
    }
}
