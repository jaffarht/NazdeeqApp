package com.example.nazdeeqapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nazdeeqapp.Model.CustomerModel;
import com.example.nazdeeqapp.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class CustomerHistoryAdapter extends FirestoreRecyclerAdapter<CustomerModel,CustomerHistoryAdapter.PostsViewHolders> {

    public CustomerHistoryAdapter(@NonNull FirestoreRecyclerOptions<CustomerModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull PostsViewHolders holder, int position, @NonNull CustomerModel model) {
        holder.Date.setText(model.getDate());
        holder.destinationName.setText(model.getDestinationName());
        holder.sourceName.setText(model.getSourceName());
        holder.Time.setText(model.getTime());

        holder.Price.setText(model.getPrice()+ "");
        holder.driverName.setText(model.getDriverName());
        holder.carModel.setText(model.getDriverCarModel());
        holder.carNumber.setText(model.getDriverCarNumber());

    }

    @NonNull
    @Override
    public PostsViewHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.customerhistory_item_single, parent, false);
        return new CustomerHistoryAdapter.PostsViewHolders(view);
    }

    public class PostsViewHolders extends RecyclerView.ViewHolder{

        private TextView Date;
        private TextView Time;
        private TextView sourceName;
        private TextView destinationName;
        private TextView Price;
        private TextView driverName;
        private TextView carModel;
        private TextView carNumber;

        public PostsViewHolders(@NonNull View itemView) {
            super(itemView);

            Date = itemView.findViewById(R.id.chDate);
            destinationName = itemView.findViewById(R.id.chdestination);
            sourceName = itemView.findViewById(R.id.chsource);
            Time = itemView.findViewById(R.id.chTime);
            Price = itemView.findViewById(R.id.chPrice);
            driverName = itemView.findViewById(R.id.chdrivername);
            carModel = itemView.findViewById(R.id.chdrivercar);
            carNumber = itemView.findViewById(R.id.chcarnumber);
        }
    }
}
