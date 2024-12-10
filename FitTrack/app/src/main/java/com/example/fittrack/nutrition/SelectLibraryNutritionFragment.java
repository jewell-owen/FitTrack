package com.example.fittrack.nutrition;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;


import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.example.fittrack.BuildConfig;
import com.example.fittrack.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_library_nutrition, container, false);

        // Initialize views
        foodSearchEditText = view.findViewById(R.id.nutrition_select_et);
        foodSearchButton = view.findViewById(R.id.nutrition_select_search_btn);
        foodLinearLayout = view.findViewById(R.id.nutrition_select_card_ll);
        nutritionBackButton = view.findViewById(R.id.nutrition_select_back_btn);

        // Set click listener for the search button
        foodSearchButton.setOnClickListener(v -> {
            String query = foodSearchEditText.getText().toString().trim();
            if (!TextUtils.isEmpty(query)) {
                fetchFoodData(query);
            }
        });

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
                connection.setConnectTimeout(5000); // 5 seconds timeout
                connection.setReadTimeout(5000);

                // Get response
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = connection.getInputStream();
                    String response = convertStreamToString(inputStream);
                    parseAndDisplayFoodData(response);
                } else {
                    Log.e("Nutritionix", "Error: " + responseCode);
                }
                connection.disconnect();
            } catch (MalformedURLException e) {
                Log.e("Nutritionix", "Invalid URL", e);
            } catch (IOException e) {
                Log.e("Nutritionix", "Connection error", e);
            }
        });
    }

    private String convertStreamToString(InputStream inputStream) throws IOException {
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

            for (int i = 0; i < commonFoods.length(); i++) {
                JSONObject food = commonFoods.getJSONObject(i);
                String foodName = food.getString("food_name");
                String brandName = food.optString("brand_name", "Generic");

                // Update UI on the main thread
                getActivity().runOnUiThread(() -> {
                    CardView cardView = createFoodCard(foodName, brandName);
                    foodLinearLayout.addView(cardView);
                });
            }
        } catch (JSONException e) {
            Log.e("Nutritionix", "JSON Parsing error", e);
        }
    }

    private CardView createFoodCard(String foodName, String calories) {
        // Inflate the CardView layout
        LayoutInflater inflater = LayoutInflater.from(getContext());
        CardView cardView = (CardView) inflater.inflate(R.layout.card_food_select, foodLinearLayout, false);

        // Set values to the views in the CardView
        TextView nutritionNameTv = cardView.findViewById(R.id.nutrition_select_card_nutrition_name_tv);
        TextView caloriesTv = cardView.findViewById(R.id.food_name_calories_tv);
        ImageButton moreInfoBtn = cardView.findViewById(R.id.nutrition_select_card_more_info_btn);
        ImageButton selectNutritionBtn = cardView.findViewById(R.id.nutrition_select_card_select_nutrition_btn);

        // Populate the card views
        nutritionNameTv.setText(foodName);
        caloriesTv.setText(String.format("Calories: %s kcal", calories));

        // Set actions for buttons (example handlers)
        moreInfoBtn.setOnClickListener(v -> {
            // Handle "More Info" button click
            Log.d("FoodCard", "More Info clicked for: " + foodName);
            // Implement navigation to detailed view or a dialog
        });

        selectNutritionBtn.setOnClickListener(v -> {
            // Handle "Select" button click
            Log.d("FoodCard", "Food Selected: " + foodName);
            // Save the selection or navigate to the next step
        });

        return cardView;
    }


    @Override
    public void onClick(View v) {
        // Handle additional click events if needed
    }
}

