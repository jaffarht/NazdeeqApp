package com.example.nazdeeqapp.Adapter;

import android.util.Log;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class CustomerOngoingAdapter extends FirestoreRecyclerAdapter<CustomerModel,CustomerOngoingAdapter.PostsViewHolders> {
    String post;
    private FirebaseFirestore firebaseFirestore;
    public CustomerOngoingAdapter(@NonNull FirestoreRecyclerOptions<CustomerModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull CustomerOngoingAdapter.PostsViewHolders holder, int position, @NonNull CustomerModel model) {
        holder.Date.setText(model.getDate());
        holder.destinationName.setText(model.getDestinationName());
        holder.sourceName.setText(model.getSourceName());
        holder.Time.setText(model.getTime());

        holder.Price.setText(model.getPrice()+ "");
        holder.driverName.setText(model.getDriverName());
        holder.carModel.setText(model.getDriverCarModel());
        holder.carNumber.setText(model.getDriverCarNumber());
        post = model.getPostID();
        Log.d("CustomerOngoingAdapter", "onBindViewHolder: "+post);



    }

    @NonNull
    @Override
    public PostsViewHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.customerongoing_item_single, parent, false);
        return new CustomerOngoingAdapter.PostsViewHolders(view);

    }


    public void increaseSeat()
    {
        firebaseFirestore = FirebaseFirestore.getInstance();
        DocumentReference documentReference = firebaseFirestore.collection("Posts").document(post);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot snapshot) {
                Long seatBack = snapshot.getLong("seatsAvailable");

                    Long seatAvailable = seatBack + 1;
                    Log.d("CustomerOngoingAdapter", "onSuccess: "+ seatAvailable);
                    updateSeat(seatAvailable);

                //seatAvailable = seatBack + 1;
            }
        });

    }

    public void updateSeat(long seatAvailable)
    {
        Log.d("CustomerOngoingAdapter", "postID: "+ post + " \nSeat:  "+ seatAvailable);
        DocumentReference documentReference = firebaseFirestore.collection("Posts").document(post);
        documentReference.update("seatsAvailable",seatAvailable);
    }




    public void deleteItem(int position) {
        getSnapshots().getSnapshot(position).getReference().delete();
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


