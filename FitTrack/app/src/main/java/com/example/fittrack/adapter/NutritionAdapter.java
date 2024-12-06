package com.example.fittrack.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fittrack.R;
import com.example.fittrack.model.FoodItem;

import java.util.List;

public class NutritionAdapter extends RecyclerView.Adapter<NutritionAdapter.ViewHolder> {

    private Context context;
    private List<FoodItem> foodList;

    public NutritionAdapter(Context context, List<FoodItem> foodList) {
        this.context = context;
        this.foodList = foodList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the card_food_select layout
        View view = LayoutInflater.from(context).inflate(R.layout.card_food_select, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FoodItem food = foodList.get(position);

        // Set the food name and calories
        holder.foodNameTv.setText(food.getFoodName());
        holder.caloriesTv.setText(context.getString(R.string.calories_format, food.getCalories()));

        // Set up button actions
        holder.moreInfoBtn.setOnClickListener(v -> {
            // Handle more info button click, e.g., open detailed view
        });

        holder.selectFoodBtn.setOnClickListener(v -> {
            // Handle select food button click, e.g., add food to a list
        });
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView foodNameTv, caloriesTv;
        ImageButton moreInfoBtn, selectFoodBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Bind views using the updated IDs from card_food_select.xml
            foodNameTv = itemView.findViewById(R.id.nutrition_select_card_nutrition_name_tv);
            caloriesTv = itemView.findViewById(R.id.food_name_calories_tv);
            moreInfoBtn = itemView.findViewById(R.id.nutrition_select_card_more_info_btn);
            selectFoodBtn = itemView.findViewById(R.id.nutrition_select_card_select_nutrition_btn);
        }
    }
}
