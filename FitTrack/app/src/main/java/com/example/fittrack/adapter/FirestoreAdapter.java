package com.example.fittrack.adapter;


import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * RecyclerView adapter for displaying the results of a Firestore {@link Query}.
 *
 * This class listens to changes in a Firestore query and updates the RecyclerView accordingly.
 * It trades off efficiency for simplicity. For example, the result of
 * {@link DocumentSnapshot#toObject(Class)} is not cached, meaning the same object may be
 * deserialized multiple times.
 *
 * For a more efficient implementation, consider using FirebaseUI's Firestore RecyclerView Adapter:
 * https://github.com/firebase/FirebaseUI-Android/tree/master/firestore
 *
 * @param <VH> The type of {@link RecyclerView.ViewHolder} used by this adapter.
 */
public abstract class FirestoreAdapter<VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH> implements EventListener<QuerySnapshot>{

    private static final String TAG = "Firestore Adapter";

    /** The Firestore query being listened to. */
    private Query mQuery;

    /** The Firestore listener registration for the query. */
    private ListenerRegistration mRegistration;

    /** The list of snapshots representing the Firestore query results. */
    private ArrayList<DocumentSnapshot> mSnapshots = new ArrayList<>();

    /**
     * Constructs a FirestoreAdapter.
     *
     * @param query The Firestore query whose results will be displayed by this adapter.
     */
    public FirestoreAdapter(Query query) {
        mQuery = query;
    }

    /**
     * Starts listening to the Firestore query. Updates to the query will be reflected in the adapter.
     */
    public void startListening() {
        if (mQuery != null && mRegistration == null) {
            mRegistration = mQuery.addSnapshotListener(this);
        }
    }

    /**
     * Stops listening to the Firestore query. The adapter will no longer update in response to query changes.
     */
    public void stopListening() {
        if (mRegistration != null) {
            mRegistration.remove();
            mRegistration = null;
        }

        mSnapshots.clear();
        notifyDataSetChanged();
    }

    /**
     * Sets a new Firestore query for the adapter and begins listening to it.
     *
     * @param query The new Firestore query.
     */
    public void setQuery(Query query) {
        // Stop listening
        stopListening();

        // Clear existing data
        mSnapshots.clear();
        notifyDataSetChanged();

        // Listen to new query
        mQuery = query;
        startListening();
    }

    @Override
    public int getItemCount() {
        return mSnapshots.size();
    }

    /**
     * Retrieves a snapshot at the specified position.
     *
     * @param index The position of the snapshot.
     * @return The Firestore {@link DocumentSnapshot} at the specified position.
     */
    protected DocumentSnapshot getSnapshot(int index) {
        return mSnapshots.get(index);
    }

    /**
     * Called when there is an error with the Firestore query.
     *
     * @param e The Firestore exception.
     */
    protected void onError(FirebaseFirestoreException e) {};

    /**
     * Called when the Firestore query data changes.
     */
    protected void onDataChanged() {}

    @Override
    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
// Handle errors
        if (e != null) {
            Log.w(TAG, "onEvent:error", e); return;
        }
// Dispatch the event
        for (DocumentChange change : documentSnapshots.getDocumentChanges()) {
// Snapshot of the changed document
            DocumentSnapshot snapshot = change.getDocument();
            switch (change.getType()) { case ADDED:
                onDocumentAdded(change);
                break;
                case MODIFIED:
                    onDocumentModified(change);
                    break;
                case REMOVED:
                    onDocumentRemoved(change);
                    break;
            }
        }
        onDataChanged();
    }

    /**
     * Handles the addition of a document to the Firestore query.
     *
     * @param change The document change event.
     */
    protected void onDocumentAdded(DocumentChange change) {
        mSnapshots.add(change.getNewIndex(), change.getDocument());
        notifyItemInserted(change.getNewIndex());
    }

    /**
     * Handles the modification of a document in the Firestore query.
     *
     * @param change The document change event.
     */
    protected void onDocumentModified(DocumentChange change) {
        if (change.getOldIndex() == change.getNewIndex()) {
// Item changed but remained in same position
            mSnapshots.set(change.getOldIndex(), change.getDocument());
            notifyItemChanged(change.getOldIndex());
        } else {
// Item changed and changed position
            mSnapshots.remove(change.getOldIndex());
            mSnapshots.add(change.getNewIndex(), change.getDocument());
            notifyItemMoved(change.getOldIndex(), change.getNewIndex());
        }
    }

    /**
     * Handles the removal of a document from the Firestore query.
     *
     * @param change The document change event.
     */
    protected void onDocumentRemoved(DocumentChange change) {
        mSnapshots.remove(change.getOldIndex());
        notifyItemRemoved(change.getOldIndex());
    }

}