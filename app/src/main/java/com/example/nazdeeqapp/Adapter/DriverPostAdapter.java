package com.example.nazdeeqapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nazdeeqapp.Model.PostsModel;
import com.example.nazdeeqapp.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class DriverPostAdapter extends FirestoreRecyclerAdapter<PostsModel,DriverPostAdapter.PostsViewHolders> {

    public DriverPostAdapter(@NonNull FirestoreRecyclerOptions<PostsModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull PostsViewHolders holder, int position, @NonNull PostsModel model) {
        holder.SeatsAvailable.setText(model.getSeatsAvailable() + "");
        holder.Date.setText(model.getDate());
        holder.destinationName.setText(model.getDestinationName());
        holder.sourceName.setText(model.getSourceName());
        holder.Time.setText(model.getTime());
    }

    @NonNull
    @Override
    public PostsViewHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.driverpost_item_single, parent, false);
        return new PostsViewHolders(view);
    }

    public void deleteItem(int position) {
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    public class PostsViewHolders extends RecyclerView.ViewHolder{

        private TextView Date;
        private TextView SeatsAvailable;
        private TextView Time;
        private TextView sourceName;
        private TextView destinationName;

        public PostsViewHolders(@NonNull View itemView) {
            super(itemView);

            Date = itemView.findViewById(R.id.dpDate);
            destinationName = itemView.findViewById(R.id.dpdestination);
            sourceName = itemView.findViewById(R.id.dpsource);
            Time = itemView.findViewById(R.id.dpTime);
            SeatsAvailable = itemView.findViewById(R.id.dpSeats);
        }
    }
}
