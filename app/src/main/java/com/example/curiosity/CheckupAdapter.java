package com.example.curiosity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class CheckupAdapter extends FirestoreRecyclerAdapter<Checkup, CheckupAdapter.CheckupHolder> {

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public CheckupAdapter(@NonNull FirestoreRecyclerOptions<Checkup> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull CheckupHolder checkupHolder, int i, @NonNull Checkup checkup) {
        checkupHolder.checkupName.setText(checkup.getCheckupName());
        checkupHolder.clinicName.setText(checkup.getClinicName());
        checkupHolder.date.setText(String.valueOf(checkup.getDate()));
        checkupHolder.summary.setText(checkup.getSummary());


    }

    @NonNull
    @Override
    public CheckupHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.checkup_layout,parent,false);
        return new CheckupHolder(v);
    }

    class CheckupHolder extends RecyclerView.ViewHolder{
        TextView checkupName;
        TextView clinicName;
        TextView date;
        TextView summary;

        public CheckupHolder(@NonNull View itemView) {
            super(itemView);

            checkupName=itemView.findViewById(R.id.checkupName);
            clinicName=itemView.findViewById(R.id.clinicName);
            date=itemView.findViewById(R.id.date);
            summary=itemView.findViewById(R.id.summary);
        }
    }
}
