package com.example.fittrack;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NutritionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NutritionFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Button dateBtn;
    private ImageButton previousDayBtn, nextDayBtn;

    private Calendar selectedDate;

    public NutritionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NutritionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NutritionFragment newInstance(String param1, String param2) {
        NutritionFragment fragment = new NutritionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_nutrition, container, false);

        // Initialize the selected date with today's date
        selectedDate = Calendar.getInstance();

        // Get the buttons
        dateBtn = rootView.findViewById(R.id.plan_workouts_date_btn);
        previousDayBtn = rootView.findViewById(R.id.plan_workouts_previous_day_btn);
        nextDayBtn = rootView.findViewById(R.id.plan_workouts_next_day_btn);

        // Set the click listener for the date button
        dateBtn.setOnClickListener(v -> openDatePicker());

        // Set the click listener for the previous day button
        previousDayBtn.setOnClickListener(v -> changeDate(-1));

        // Set the click listener for the next day button
        nextDayBtn.setOnClickListener(v -> changeDate(1));

        // Update the date button with the current date
        updateDateButton();

        return rootView;
    }

    // Method to open the DatePickerDialog
    private void openDatePicker() {
        int year = selectedDate.get(Calendar.YEAR);
        int month = selectedDate.get(Calendar.MONTH);
        int day = selectedDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
                (view, year1, month1, dayOfMonth) -> {
                    selectedDate.set(year1, month1, dayOfMonth);
                    updateDateButton();
                },
                year, month, day);

        datePickerDialog.show();
    }

    // Method to change the date by a given number of days (positive for next day, negative for previous day)
    private void changeDate(int dayOffset) {
        selectedDate.add(Calendar.DAY_OF_MONTH, dayOffset);
        updateDateButton();
    }

    // Method to update the date displayed on the button
    private void updateDateButton() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());
        String formattedDate = sdf.format(selectedDate.getTime());

        // If the selected date is today, show "Today" instead of the date
        Calendar today = Calendar.getInstance();
        if (selectedDate.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                selectedDate.get(Calendar.MONTH) == today.get(Calendar.MONTH) &&
                selectedDate.get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH)) {
            dateBtn.setText("Today");
        } else {
            dateBtn.setText(formattedDate);
        }
    }
}