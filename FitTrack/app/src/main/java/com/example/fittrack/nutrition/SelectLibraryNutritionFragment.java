package com.example.fittrack.nutrition;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fittrack.BuildConfig;
import com.example.fittrack.R;
//import com.example.fittrack.adapter.NutritionAdapter;
import com.example.fittrack.model.FoodItem;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.OutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SelectLibraryNutritionFragment extends Fragment implements View.OnClickListener {

    private EditText foodSearchEditText;
    private ImageButton foodSearchButton;
    private ImageButton nutritionBackButton;
    private LinearLayout foodLinearLayout;
    private final Executor executor = Executors.newSingleThreadExecutor(); // For background threading
    private ScrollView foodSelectScrollView;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();

    private String search = "";
    private String filter = "common_name"; // Default filter is "common_name"

    private Button commonNameFilterButton;
    private Button brandFilterButton;

    private boolean useBrandedFoods = false; // Default to false for common foods

    private RecyclerView breakfastRecyclerView, lunchRecyclerView, dinnerRecyclerView, otherRecyclerView;
    private TextView breakfastTotalCaloriesTextView, lunchTotalCaloriesTextView, dinnerTotalCaloriesTextView, otherTotalCaloriesTextView;
    private Button selectNutritionBtn;
    private String mealType = "";  // Tracks the selected meal type

    private String date;
    private String meal;

    // List to hold food items for each meal
    private List<String> breakfastFoodList = new ArrayList<>();
    private List<String> lunchFoodList = new ArrayList<>();
    private List<String> dinnerFoodList = new ArrayList<>();
    private List<String> otherFoodList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_library_nutrition, container, false);

//        breakfastRecyclerView = view.findViewById(R.id.breakfast_recycler);
//        lunchRecyclerView = view.findViewById(R.id.lunch_recycler);
//        dinnerRecyclerView = view.findViewById(R.id.dinner_recycler);
//        otherRecyclerView = view.findViewById(R.id.other_recycler);

        // Initialize views
        foodSearchEditText = view.findViewById(R.id.nutrition_select_et);
        foodSearchButton = view.findViewById(R.id.nutrition_select_search_btn);
        foodLinearLayout = view.findViewById(R.id.nutrition_select_card_ll);
        nutritionBackButton = view.findViewById(R.id.nutrition_select_back_btn);

        commonNameFilterButton = view.findViewById(R.id.nutrition_select_common_name_filter_btn);
        brandFilterButton = view.findViewById(R.id.nutrition_select_brand_filter_btn);

        if (getArguments() != null) {
            date = getArguments().getString("date");
            meal = getArguments().getString("meal");
        }


        // Set click listeners for the buttons
        commonNameFilterButton.setOnClickListener(v -> {
            filter = "common_name"; // Switch filter to "common_name"
            useBrandedFoods = false; // Set to false for common foods
            updateFilterButtonStyle(commonNameFilterButton, brandFilterButton);
        });

        brandFilterButton.setOnClickListener(v -> {
            filter = "brand"; // Switch filter to "brand"
            useBrandedFoods = true; // Set to true for branded foods
            updateFilterButtonStyle(brandFilterButton, commonNameFilterButton);
        });


        // Set click listener for the search button
        foodSearchButton.setOnClickListener(v -> {
            String query = foodSearchEditText.getText().toString().trim();
            if (!TextUtils.isEmpty(query)) {
                fetchFoodData(query);
            }
        });

//        RecyclerView breakfastRecyclerView = view.findViewById(R.id.breakfast_recycler);
//        RecyclerView lunchRecyclerView = findViewById(R.id.lunch_recycler);
//        RecyclerView dinnerRecyclerView = findViewById(R.id.dinner_recycler);
//        RecyclerView otherRecyclerView = findViewById(R.id.other_recycler);

        nutritionBackButton.setOnClickListener(v -> getActivity().getSupportFragmentManager().popBackStack());

        return view;
    }



    private void fetchFoodData(String query) {
        executor.execute(() -> {
            String appId = BuildConfig.API_ID_NUTRITION;
            String appKey = BuildConfig.API_KEY_NUTRITION;
            String baseUrl = "https://trackapi.nutritionix.com/v2/search/instant";
            String urlString = baseUrl + "?query=" + query;

            try {
                // Build URL and open connection
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("x-app-id", appId);
                connection.setRequestProperty("x-app-key", appKey);
                if(useBrandedFoods) {
                    connection.setRequestProperty("use_branded_foods", "true");
                } else {
                    connection.setRequestProperty("use_branded_foods", "false");
                }
                connection.setConnectTimeout(5000); // 5 seconds timeout
                connection.setReadTimeout(5000);




                // Get response
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = connection.getInputStream();
                    String response = convertStreamToString(inputStream);
                    parseAndDisplayFoodData(response);
                    Log.d("Nutritionix", "Response JSON: " + response);
                } else {
                    Log.e("Nutritionix", "Error: " + responseCode);
                }
                connection.disconnect();
            } catch (Exception e) {
                Log.e("Nutritionix", "Connection error", e);
            }
        });
    }

    private String convertStreamToString(InputStream inputStream) throws Exception {
        StringBuilder result = new StringBuilder();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            result.append(new String(buffer, 0, length));
        }
        return result.toString();
    }

    private void parseAndDisplayFoodData(String jsonResponse) {
        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray commonFoods = jsonObject.getJSONArray("common");

            // Clear previous results on the main thread
            getActivity().runOnUiThread(() -> foodLinearLayout.removeAllViews());

//            for (int i = 0; i < commonFoods.length(); i++) {
                JSONObject food = commonFoods.getJSONObject(0);
                String foodName = food.getString("food_name");

                // Fetch detailed nutritional info for each food item
                executor.execute(() -> fetchDetailedFoodData(foodName));
//            }
        } catch (JSONException e) {
            Log.e("Nutritionix", "JSON Parsing error", e);
        }
    }

    private void fetchDetailedFoodData(String foodName) {
        String appId = BuildConfig.API_ID_NUTRITION;
        String appKey = BuildConfig.API_KEY_NUTRITION;
        String urlString = "https://trackapi.nutritionix.com/v2/natural/nutrients";

        try {
            // Build URL and open connection
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("x-app-id", appId);
            connection.setRequestProperty("x-app-key", appKey);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setConnectTimeout(5000); // 5 seconds timeout
            connection.setReadTimeout(5000);

            // Create request body
            String requestBody = new JSONObject().put("query", foodName).toString();

            // Write request body
            connection.setDoOutput(true);
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(requestBody.getBytes());
            outputStream.flush();
            outputStream.close();

            // Get response
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                String response = convertStreamToString(inputStream);
                displayDetailedFoodData(response);
                Log.d("Nutritionix", "Detailed Response JSON: " + response);

            } else {
                Log.e("Nutritionix", "Error: " + responseCode);
            }
            connection.disconnect();
        } catch (Exception e) {
            Log.e("Nutritionix", "Connection error", e);
        }
    }

    private void displayDetailedFoodData(String jsonResponse) {
        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray foods = jsonObject.getJSONArray("foods");

            for (int i = 0; i < 1/*foods.length()*/; i++) {
                JSONObject food = foods.getJSONObject(i);
                String foodName = food.getString("food_name");
                double calories = (double) food.get("nf_calories");

                // Update UI on the main thread
                getActivity().runOnUiThread(() -> {
                    CardView cardView = createFoodCard(foodName, calories);
                    foodLinearLayout.addView(cardView);
                });
            }
        } catch (JSONException e) {
            Log.e("Nutritionix", "JSON Parsing error", e);
        }
    }

    private CardView createFoodCard(String foodName, double calories) {

        LayoutInflater inflater = LayoutInflater.from(getContext());
        CardView cardView = (CardView) inflater.inflate(R.layout.card_food_select, foodLinearLayout, false);

        TextView nutritionNameTv = cardView.findViewById(R.id.nutrition_select_card_nutrition_name_tv);
        TextView caloriesTv = cardView.findViewById(R.id.food_name_calories_tv);
        ImageButton moreInfoBtn = cardView.findViewById(R.id.nutrition_select_card_more_info_btn);
        ImageButton selectNutritionBtn = cardView.findViewById(R.id.nutrition_select_card_select_nutrition_btn);

        nutritionNameTv.setText(foodName);
        String tempCalories = "Calories: " + String.valueOf(calories);
        caloriesTv.setText(tempCalories);

        // Set actions for buttons (example handlers)
        moreInfoBtn.setOnClickListener(v -> {
            // Handle "More Info" button click
            Log.d("FoodCard", "More Info clicked for: " + foodName);
            // Implement navigation to detailed view or a dialog
        });

        selectNutritionBtn.setOnClickListener(v -> {
            String selectedFoodName = foodName; // Food name fetched from the API response
            Double selectedCalories = calories; // Calories from the API response

            // Log the selected food (for debugging purposes)
            Log.d("FoodCard", "Food Selected: " + selectedFoodName);

            updateMealData(selectedFoodName, selectedCalories);

            // Add the selected food to Firebase
            addToFirebase(selectedFoodName, selectedCalories);

            // Example action: Add selected food to Firebase Firestore
//            if (user != null) {
//                db.collection("users")
//                        .document(user.getUid())
//                        .collection("selectedFoods")
//                        .add(new FoodItem(selectedFoodName, selectedCalories)) // Assuming a FoodItem model
//                        .addOnSuccessListener(documentReference -> {
//                            Log.d("Firestore", "Food added with ID: " + documentReference.getId());
//                            // Provide user feedback
//                        })
//                        .addOnFailureListener(e -> {
//                            Log.e("Firestore", "Error adding food", e);
//                        });
//            }
        });

        return cardView;
    }

    private void updateMealData(String foodName, double calories) {
        // Add the food item to the RecyclerView
        if (mealType.equals("Breakfast")) {
            addItemToRecyclerView(breakfastRecyclerView, foodName);
            updateTotalCalories(breakfastTotalCaloriesTextView, calories);
        } else if (mealType.equals("Lunch")) {
            addItemToRecyclerView(lunchRecyclerView, foodName);
            updateTotalCalories(lunchTotalCaloriesTextView, calories);
        } else if (mealType.equals("Dinner")) {
            addItemToRecyclerView(dinnerRecyclerView, foodName);
            updateTotalCalories(dinnerTotalCaloriesTextView, calories);
        } else if (mealType.equals("Other")) {
            addItemToRecyclerView(otherRecyclerView, foodName);
            updateTotalCalories(otherTotalCaloriesTextView, calories);
        }
    }

    private void addItemToRecyclerView(RecyclerView recyclerView, String foodName) {
        // Assuming you have a list of food items for each meal type
//        foodItemsList.add(foodName);
//        recyclerView.getAdapter().notifyDataSetChanged();  // Update the RecyclerView with new food item
    }

    private void updateTotalCalories(TextView totalCaloriesTextView, double calories) {
        // Convert calories to integer and update the total
//        double totalCalories = String.valueOf(totalCaloriesTextView.getText().toString())
//        totalCalories += Integer.parseInt(calories);
//        totalCaloriesTextView.setText(String.valueOf(totalCalories));  // Update total calories
    }

    private void addToFirebase(String foodName, double calories) {
        // Store the food and calories in Firebase under the user's meal collection for the selected date
        CollectionReference mealRef = db.collection("users")
                .document(user.getUid())
                .collection(date)
                .document(date)
                .collection(meal);

        Map<String, Object> mealData = new HashMap<>();
        mealData.put("foodName", foodName);
        mealData.put("calories", calories);
        mealData.put("meal", meal);  // "Breakfast", "Lunch", "Dinner", "Other"

        mealRef.add(mealData)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        getActivity().getSupportFragmentManager().popBackStack();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }


    private void updateFilterButtonStyle(Button selectedButton, Button unselectedButton) {
        selectedButton.setBackground(getResources().getDrawable(R.drawable.button_background_secondary));
        unselectedButton.setBackground(getResources().getDrawable(R.drawable.button_background));
    }

    @Override
    public void onClick(View v) {
        // Handle additional click events if needed
    }
}

