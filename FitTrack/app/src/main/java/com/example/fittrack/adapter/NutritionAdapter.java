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

/**
 * Adapter for displaying a list of food items in the nutrition section of the app.
 * This adapter is used to bind data from Firebase to a RecyclerView for displaying food items.
 */
public class NutritionAdapter extends FirestoreAdapter<NutritionAdapter.ViewHolder> {

    /**
     * Interface for handling food item selection.
     */
    public interface onFoodSelectedListener {

        /**
         * Called when a food item is selected.
         *
         * @param snapshot The DocumentSnapshot representing the selected food item.
         */
        void onGoToFood(DocumentSnapshot snapshot);

    }

    private NutritionAdapter.onFoodSelectedListener mListener;

    /**
     * Constructs a NutritionAdapter with the specified query and listener.
     *
     * @param query    The query used to fetch food items from Firebase.
     * @param listener The listener that handles food item selection events.
     */
    public NutritionAdapter(Query query, NutritionAdapter.onFoodSelectedListener listener) {
        super(query);
        mListener = listener;
    }

    /**
     * Creates a new ViewHolder for the RecyclerView.
     *
     * @param parent   The parent view group to attach the view holder to.
     * @param viewType The view type of the new view holder.
     * @return A new ViewHolder instance.
     */
    @NonNull
    @Override
    public NutritionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new NutritionAdapter.ViewHolder(inflater.inflate(R.layout.card_nutrition_item_text, parent, false));
    }

    /**
     * Binds the data from the given position to the provided ViewHolder.
     *
     * @param holder   The ViewHolder to bind data to.
     * @param position The position of the item in the data set.
     */
    @Override
    public void onBindViewHolder(@NonNull NutritionAdapter.ViewHolder holder, int position) {
        holder.bind(getSnapshot(position), mListener);
    }

    /**
     * ViewHolder for a single item in the nutrition list.
     */
    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView nameView;

        /**
         * Constructs a new ViewHolder.
         *
         * @param itemView The view representing a single item in the RecyclerView.
         */
        public ViewHolder(View itemView) {
            super(itemView);

            nameView = itemView.findViewById(R.id.card_nutrition_name_tv);


        }

        /**
         * Binds the food item data to the view holder.
         *
         * @param snapshot The snapshot of the food item to bind.
         * @param listener The listener for food item selection.
         */
        public void bind(final DocumentSnapshot snapshot,
                         final NutritionAdapter.onFoodSelectedListener listener) {

            FoodItem foodItem = snapshot.toObject(FoodItem.class);
            Resources resources = itemView.getResources();

            nameView.setText(foodItem.getFoodName());


        }

    }

}


