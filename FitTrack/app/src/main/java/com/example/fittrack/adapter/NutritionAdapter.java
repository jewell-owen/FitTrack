package com.example.fittrack.adapter;

import android.content.res.Resources;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fittrack.R;
import com.example.fittrack.model.FoodItem;
import com.example.fittrack.model.LogWorkout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

public class NutritionAdapter extends FirestoreAdapter<NutritionAdapter.ViewHolder> {

    public interface onFoodSelectedListener {

        void onGoToFood(DocumentSnapshot snapshot);

    }

    private NutritionAdapter.onFoodSelectedListener mListener;

    public NutritionAdapter(Query query, NutritionAdapter.onFoodSelectedListener listener) {
        super(query);
        mListener = listener;
    }

    @NonNull
    @Override
    public NutritionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new NutritionAdapter.ViewHolder(inflater.inflate(R.layout.card_nutrition_item_text, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NutritionAdapter.ViewHolder holder, int position) {
        holder.bind(getSnapshot(position), mListener);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView nameView;

        public ViewHolder(View itemView) {
            super(itemView);

            nameView = itemView.findViewById(R.id.card_nutrition_name_tv);


        }

        public void bind(final DocumentSnapshot snapshot,
                         final NutritionAdapter.onFoodSelectedListener listener) {

            FoodItem foodItem = snapshot.toObject(FoodItem.class);
            Resources resources = itemView.getResources();


            nameView.setText(foodItem.getFoodName());






        }

    }

}


