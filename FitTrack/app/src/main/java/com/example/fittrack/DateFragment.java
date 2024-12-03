//package com.example.fittrack;
//
//import android.app.DatePickerDialog;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.TextView;
//
//import androidx.fragment.app.Fragment;
//
//import java.util.Calendar;
//
//public class DateFragment extends Fragment {
//
//    private TextView selectedDateTextView;
//    private Button selectDateButton;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View rootView = inflater.inflate(R.layout.fragment_date, container, false);
//
//        selectedDateTextView = rootView.findViewById(R.id.selected_date_tv);
//        selectDateButton = rootView.findViewById(R.id.select_date_btn);
//
//        // Initialize the current date
//        Calendar calendar = Calendar.getInstance();
//        int year = calendar.get(Calendar.YEAR);
//        int month = calendar.get(Calendar.MONTH);
//        int day = calendar.get(Calendar.DAY_OF_MONTH);
//
//        // Set the default date as today
//        String defaultDate = day + "/" + (month + 1) + "/" + year;
//        selectedDateTextView.setText(defaultDate);
//
//        // Set OnClickListener to show DatePickerDialog
//        selectDateButton.setOnClickListener(v -> {
//            DatePickerDialog datePickerDialog = new DatePickerDialog(
//                    getActivity(),
//                    (view, year1, month1, dayOfMonth) -> {
//                        // Display the selected date in TextView
//                        String selectedDate = dayOfMonth + "/" + (month1 + 1) + "/" + year1;
//                        selectedDateTextView.setText(selectedDate);
//
//                        // Optionally, call a method to update data based on the selected date
//                        updateDataForSelectedDate(selectedDate);
//                    },
//                    year, month, day);
//
//            // Show the DatePickerDialog
//            datePickerDialog.show();
//        });
//
//        return rootView;
//    }
//
//    // Method to update data based on the selected date
//    private void updateDataForSelectedDate(String selectedDate) {
//        // You can replace this with your logic to update the data based on the selected date
//        // For example, updating calories or meals for the chosen date
//    }
//
//}
