package com.example.curiosity;

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

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class ScanPet extends AppCompatActivity {

    ImageButton back_button;
    ImageButton settings_button;

    //TextView mText;
    NfcAdapter mAdapter;
    PendingIntent mPendingIntent;
    IntentFilter mFilters[];
    String mTechLists[][];



        @Override
        protected void onCreate (Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_scan_pet);



            //mText = (TextView) findViewById(R.id.text);

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
            System.out.println(getNdefMessages(intent));



            back_button = (ImageButton) findViewById(R.id.back_button);
            settings_button = (ImageButton) findViewById(R.id.settings_button);

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
        //mText = (TextView) findViewById(R.id.text);
        //mText.setText(getNdefMessages(intent));
        //System.out.println(getNdefMessages(intent));
    }


}