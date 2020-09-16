package com.example.curiosity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class VaccinationAdapter extends FirestoreRecyclerAdapter<Vaccination, VaccinationAdapter.VaccinationHolder> {

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public VaccinationAdapter(@NonNull FirestoreRecyclerOptions<Vaccination> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull VaccinationHolder vaccinationHolder, int i, @NonNull Vaccination vaccination) {
        vaccinationHolder.vaccineName.setText(vaccination.getVaccineName());
        vaccinationHolder.clinicName.setText(vaccination.getClinicName());
        vaccinationHolder.date.setText(String.valueOf(vaccination.getDate()));


    }

    @NonNull
    @Override
    public VaccinationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.record_layout,parent,false);
        return new VaccinationHolder(v);
    }

    class VaccinationHolder extends RecyclerView.ViewHolder{
        TextView vaccineName;
        TextView clinicName;
        TextView date;

        public VaccinationHolder(@NonNull View itemView) {
            super(itemView);

            vaccineName=itemView.findViewById(R.id.vaccineName);
            clinicName=itemView.findViewById(R.id.clinicName);
            date=itemView.findViewById(R.id.date);
        }
    }
}
