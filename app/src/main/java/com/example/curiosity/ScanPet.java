package com.example.curiosity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;

import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLOutput;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class ScanPet extends AppCompatActivity {

    FirebaseAuth fAuth; //to get user id------------------------
    FirebaseFirestore fStore; //for data retrieval--------------
    String userid;

    private FirebaseFirestore db;


    ImageButton back_button;
    ImageButton settings_button;

    TextView petname;
    TextView pettype;
    TextView petbreed;
    TextView petdob;

    TextView petId;
    NfcAdapter mAdapter;
    PendingIntent mPendingIntent;
    IntentFilter mFilters[];
    String mTechLists[][];


        @Override
        protected void onCreate (Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_scan_pet);

            back_button = (ImageButton) findViewById(R.id.back_button);
            settings_button = (ImageButton) findViewById(R.id.settings_button);
            petname = findViewById(R.id.petName);
            pettype = findViewById(R.id.petType);
            petbreed = findViewById(R.id.petBreed);
            petdob = findViewById(R.id.petDOB);
            petId = (TextView) findViewById(R.id.ScannedID);

            fAuth = FirebaseAuth.getInstance();
            fStore = FirebaseFirestore.getInstance();

            userid = fAuth.getCurrentUser().getUid();



            mAdapter = NfcAdapter.getDefaultAdapter(this);
            mPendingIntent = PendingIntent.getActivity(this, 0,
                    new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

            IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
            try{
                ndef.addDataType("text/plain");
            }catch(IntentFilter.MalformedMimeTypeException e){
                throw new RuntimeException("fail", e);
            }

            IntentFilter nt = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
            mFilters = new IntentFilter[]{
                    ndef, nt
            };

            mTechLists = new String[][]{
                    new String[]{
                            Ndef.class.getName()
                    }
            };
            Intent intent = getIntent();
            String id = getNdefMessages(intent);
            //if
            //System.out.println(id);
            petId = (TextView) findViewById(R.id.ScannedID);
            petId.setText(id);

                DocumentReference documentReference = fStore.collection("Users").document(userid).collection("Pets")
                        .document(id);
            System.out.println(documentReference);
                documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
//                        petname.setText(documentSnapshot.getString("name"));
//                        pettype.setText(documentSnapshot.getString("Pet Type"));
//                        petbreed.setText(documentSnapshot.getString("Pet Breed"));
//
//                        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
//                        String strDate = dateFormat.format(documentSnapshot.getDate("DOB"));
//                        petdob.setText(strDate);
                        System.out.println(documentSnapshot.getString("Pet Name"));

                    }
                });


////////////////////////////////////////////////////////////////
            //on press settings button
            settings_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ScanPet.this, Settings.class);
                    startActivity(intent);
                }
            });

            //on press go back
            back_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ScanPet.this, Dashboard.class);
                    startActivity(intent);
                }
            });
        }


    public String getNdefMessages(Intent intent){
        NdefMessage[] msgs = null;
        String action = intent.getAction();
        String fin = null;
        if(NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)||
                NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)){
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            if(rawMsgs != null){
                msgs = new NdefMessage[rawMsgs.length];
                for(int i=0; i<rawMsgs.length; i++){
                    msgs[i] = (NdefMessage) rawMsgs[i];
                }

                NdefMessage ndefMessage = (NdefMessage) msgs[0];
                String temp = new String(ndefMessage.getRecords()[0].getPayload());
                fin = temp;

            }else{
                byte[] empty = new byte[]{};
                NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN, empty, empty, empty);
                NdefMessage msg = new NdefMessage(new NdefRecord[]{record});
                msgs = new NdefMessage[]{msg};
                System.out.println("EMPTY");
            }

        }
        if(msgs==null)
            return "No Tag discovered!";
        else
            return fin;
    }

    @Override
    public void onNewIntent(Intent intent){
            super.onNewIntent(intent);
        Log.i("Foreground dispatch", "Discovered tag with intent:" + intent);
        petId = (TextView) findViewById(R.id.ScannedID);
        petId.setText(getNdefMessages(intent));
        //System.out.println(getNdefMessages(intent));
    }




}