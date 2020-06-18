package com.example.nazdeeqapp.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nazdeeqapp.Model.PostsModel;
import com.example.nazdeeqapp.R;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.firebase.ui.firestore.paging.LoadingState;
import com.google.firebase.firestore.DocumentSnapshot;

import org.w3c.dom.Text;


public class FirestoreAdapter extends FirestorePagingAdapter<PostsModel,FirestoreAdapter.PostsViewHolder> {

    private OnListItemClick onListItemClick;


    public FirestoreAdapter(@NonNull FirestorePagingOptions<PostsModel> options, OnListItemClick onListItemClick) {
        super(options);
        this.onListItemClick = onListItemClick;
    }

    @Override
    protected void onBindViewHolder(@NonNull PostsViewHolder holder, int position, @NonNull PostsModel model) {


        holder.SeatsAvailable.setText(model.getSeatsAvailable() + "");
        holder.Date.setText(model.getDate());
        holder.destinationName.setText(model.getDestinationName());
        holder.sourceName.setText(model.getSourceName());
        holder.Time.setText(model.getTime());
        holder.price.setText(model.getPrice() + "");
    }

    @NonNull
    @Override
    public PostsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item_single, parent, false);

        return new PostsViewHolder(view);
    }

    @Override
    protected void onLoadingStateChanged(@NonNull LoadingState state) {
        super.onLoadingStateChanged(state);
        switch (state)
        {
            case LOADING_INITIAL:
                Log.d("PAGING_LOG", "Loading Initial Data : " + getItemCount());
                break;
            case LOADING_MORE:
                Log.d("PAGING_LOG", "Loading Next Page : " );
                break;
            case FINISHED:
                Log.d("PAGING_LOG", "All Data Loaded : ");
                break;

            case ERROR:
                Log.d("PAGING_LOG", "Error : ");
                break;

            case LOADED:
                Log.d("PAGING_LOG", "Total Items Loaded : " + getItemCount());
                break;
        }
    }

    public class PostsViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView Date;
        private TextView SeatsAvailable;
        private TextView Time;
        private TextView sourceName;
        private TextView destinationName;
        private TextView price;
        //ADD LATER
        private TextView name;
        private TextView destinationAddress;
        private TextView UserID;


        public PostsViewHolder(@NonNull View itemView) {
            super(itemView);

            Date = itemView.findViewById(R.id.dpDate);
            destinationName = itemView.findViewById(R.id.dpdestination);
            sourceName = itemView.findViewById(R.id.dpsource);
            Time = itemView.findViewById(R.id.dpTime);
            SeatsAvailable = itemView.findViewById(R.id.dpSeats);
            price = itemView.findViewById(R.id.dpPrice);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            onListItemClick.OnItemClick(getItem(getAdapterPosition()),getAdapterPosition());

        }
    }

    public interface OnListItemClick
    {
        void OnItemClick(DocumentSnapshot snapshot,int position);

    }
}
