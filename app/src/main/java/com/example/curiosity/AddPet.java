package com.example.curiosity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AddPet extends AppCompatActivity {

    ImageButton back_button;
    ImageButton settings_button;

    FirebaseAuth fAuth; //to get user id
    FirebaseFirestore fStore; //for data retrieval
    String userid;

    TextView petname;
    TextView pettype;
    TextView petbreed;
    TextView dob;
    TextView trackerno;
    TextView straystatus;

    Button addpetButton;

    String numberofpets;

    View petbreedview;
    ImageView petProfilePicture;

    private FirebaseFirestore db;
    public Uri imageUri;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    String petid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addpet);

        back_button = (ImageButton) findViewById(R.id.back_button);
        settings_button = (ImageButton) findViewById(R.id.settings_button);
        petname = findViewById(R.id.petName);
        pettype = findViewById(R.id.petType);
        petbreedview = findViewById(R.id.petbreedview);
        petbreed = findViewById(R.id.petBreed);
        dob = findViewById(R.id.petDOB);
        petProfilePicture = findViewById(R.id.petProfilePicture);
        addpetButton = findViewById(R.id.addpetButton);
        trackerno = findViewById(R.id.trackerNumber);
        straystatus = findViewById(R.id.petStatus);
        storage = FirebaseStorage.getInstance();
        storageReference= storage.getReference();

        petid = UUID.randomUUID().toString();


        String ScannedID = getIntent().getStringExtra("petID");
        //getting docreference
        userid = fAuth.getCurrentUser().getUid();
        DocumentReference documentReference = fStore.collection("Users").document(userid);


        String[] DOGBREEDS = new String[]{
                "Affenpinscher", "Afghan Hound", "Aidi", "Airedale Terrier", "Akbash", "Akita", "Alano Español", "Alapaha Blue Blood Bulldog", "Alaskan husky", "Alaskan Klee Kai", "Alaskan Malamute", "Alopekis", "Alpine Dachsbracke", "American Bulldog", "American Bully", "American Cocker Spaniel", "American English Coonhound", "American Eskimo Dog", "American Foxhound", "American Hairless Terrier", "American Pit Bull Terrier", "American Staffordshire Terrier", "American Water Spaniel", "Anatolian Shepherd Dog", "Andalusian Hound", "Anglo-Français de Petite Vénerie", "Appenzeller Sennenhund", "Ariegeois", "Armant", "Armenian Gampr dog", "Artois Hound", "Australian Cattle Dog", "Australian Kelpie", "Australian Shepherd", "Australian Stumpy Tail Cattle Dog", "Australian Terrier", "Austrian Black and Tan Hound", "Austrian Pinscher", "Azawakh", "Bakharwal dog", "Banjara Hound", "Barbado da Terceira", "Barbet", "Basenji", "Basque Shepherd Dog", "Basset Artésien Normand", "Basset Bleu de Gascogne", "Basset Fauve de Bretagne", "Basset Hound", "Bavarian Mountain Hound", "Beagle", "Beagle-Harrier", "Belgian Shepherd", "Bearded Collie", "Beauceron", "Bedlington Terrier", "Bergamasco Shepherd", "Berger Picard", "Bernese Mountain Dog", "Bhotia", "Bichon Frisé", "Billy", "Black and Tan Coonhound", "Black Norwegian Elkhound", "Black Russian Terrier", "Black Mouth Cur", "Bloodhound", "Blue Lacy", "Blue Picardy Spaniel", "Bluetick Coonhound", "Boerboel", "Bohemian Shepherd", "Bolognese", "Border Collie", "Border Terrier", "Borzoi", "Bosnian Coarse-haired Hound", "Boston Terrier", "Bouvier des Ardennes", "Bouvier des Flandres", "Boxer", "Boykin Spaniel", "Bracco Italiano", "Braque d'Auvergne", "Braque de l'Ariège", "Braque du Bourbonnais", "Braque Francais", "Braque Saint-Germain", "Briard", "Briquet Griffon Vendéen", "Brittany", "Broholmer", "Bruno Jura Hound", "Brussels Griffon", "Bucovina Shepherd Dog", "Bull Arab", "Bull Terrier", "Bulldog", "Bullmastiff", "Bully Kutta", "Burgos Pointer", "Cairn Terrier", "Campeiro Bulldog", "Canaan Dog", "Canadian Eskimo Dog", "Cane Corso", "Cane di Oropa", "Cane Paratore", "Cantabrian Water Dog", "Can de Chira", "Cão da Serra de Aires", "Cão de Castro Laboreiro", "Cão de Gado Transmontano", "Cão Fila de São Miguel", "Cardigan Welsh Corgi", "Carea Castellano Manchego", "Carea Leonés", "Carolina Dog", "Carpathian Shepherd Dog", "Catahoula Leopard Dog", "Catalan Sheepdog", "Caucasian Shepherd Dog", "Cavalier King Charles Spaniel", "Central Asian Shepherd Dog", "Cesky Fousek", "Cesky Terrier", "Chesapeake Bay Retriever", "Chien Français Blanc et Noir", "Chien Français Blanc et Orange", "Chien Français Tricolore", "Chihuahua", "Chilean Terrier", "Chinese Chongqing Dog", "Chinese Crested Dog", "Chinook", "Chippiparai", "Chongqing dog", "Chow Chow", "Cimarrón Uruguayo", "Cirneco dell'Etna", "Clumber Spaniel", "Combai", "Colombian fino hound", "Coton de Tulear", "Cretan Hound", "Croatian Sheepdog", "Curly-Coated Retriever", "Cursinu", "Czechoslovakian Wolfdog", "Dachshund", "Dalmatian", "Dandie Dinmont Terrier", "Danish-Swedish Farmdog", "Denmark Feist", "Dingo", "Doberman Pinscher", "Dogo Argentino", "Dogo Guatemalteco", "Dogo Sardesco", "Dogue Brasileiro", "Dogue de Bordeaux", "Drentse Patrijshond", "Drever", "Dunker", "Dutch Shepherd", "Dutch Smoushond", "East Siberian Laika", "East European Shepherd", "English Cocker Spaniel", "English Foxhound", "English Mastiff", "English Setter", "English Shepherd", "English Springer Spaniel", "English Toy Terrier (Black & Tan", "Entlebucher Mountain Dog", "Estonian Hound", "Estrela Mountain Dog", "Eurasier", "Field Spaniel", "Fila Brasileiro", "Finnish Hound", "Finnish Lapphund", "Finnish Spitz", "Flat-Coated Retriever", "French Bulldog", "French Spaniel", "Galgo Español", "Galician Shepherd Dog", "Garafian Shepherd", "Gascon Saintongeois", "Georgian Shepherd", "German Hound", "German Longhaired Pointer", "German Pinscher", "German Roughhaired Pointer", "German Shepherd Dog", "German Shorthaired Pointer", "German Spaniel", "German Spitz", "German Wirehaired Pointer", "Giant Schnauzer", "Glen of Imaal Terrier", "Golden Retriever", "Gończy Polski", "Gordon Setter", "Gos Rater Valencià", "Grand Anglo-Français Blanc et Noir", "Grand Anglo-Français Blanc et Orange", "Grand Anglo-Français Tricolore", "Grand Basset Griffon Vendéen", "Grand Bleu de Gascogne", "Grand Griffon Vendéen", "Great Dane", "Greater Swiss Mountain Dog", "Greek Harehound", "Greek Shepherd", "Greenland Dog", "Greyhound", "Griffon Bleu de Gascogne", "Griffon Fauve de Bretagne", "Griffon Nivernais", "Gull Dong", "Gull Terrier", "Hällefors Elkhound", "Hamiltonstövare", "Hanover Hound", "Harrier", "Havanese", "Hierran Wolfdog", "Hokkaido", "Hortaya borzaya", "Hovawart", "Huntaway", "Hygen Hound", "Ibizan Hound", "Icelandic Sheepdog", "Indian pariah dog", "Indian Spitz", "Irish Red and White Setter", "Irish Setter", "Irish Terrier", "Irish Water Spaniel", "Irish Wolfhound", "Istrian Coarse-haired Hound", "Istrian Shorthaired Hound", "Italian Greyhound", "Jack Russell Terrier", "Jagdterrier", "Japanese Chin", "Japanese Spitz", "Japanese Terrier", "Jindo", "Jonangi", "Kai Ken", "Kaikadi", "Kangal Shepherd Dog", "Kanni", "Karakachan dog", "Karelian Bear Dog", "Kars", "Karst Shepherd", "Keeshond", "Kerry Beagle", "Kerry Blue Terrier", "King Charles Spaniel", "King Shepherd", "Kintamani", "Kishu", "Kokoni", "Komondor", "Kooikerhondje", "Koolie", "Koyun dog", "Kromfohrländer", "Kuchi", "Kuvasz", "Labrador Retriever", "Lagotto Romagnolo", "Lakeland Terrier", "Lancashire Heeler", "Landseer", "Lapponian Herder", "Large Münsterländer", "Leonberger", "Levriero Sardo", "Lhasa Apso", "Lithuanian Hound", "Löwchen", "Lupo Italiano", "Mackenzie River husky", "Magyar agár", "Mahratta Greyhound", "Maltese", "Manchester Terrier", "Maremmano-Abruzzese Sheepdog", "McNab dog", "Miniature American Shepherd", "Miniature Bull Terrier", "Miniature Fox Terrier", "Miniature Pinscher", "Miniature Schnauzer", "Molossus of Epirus", "Montenegrin Mountain Hound", "Mountain Cur", "Mountain Feist", "Mucuchies", "Mudhol Hound", "Mudi", "Neapolitan Mastiff", "New Guinea singing dog", "New Zealand Heading Dog", "Newfoundland", "Norfolk Terrier", "Norrbottenspets", "Northern Inuit Dog", "Norwegian Buhund", "Norwegian Elkhound", "Norwegian Lundehund", "Norwich Terrier", "Nova Scotia Duck Tolling Retriever", "Old Croatian Sighthound", "Old Danish Pointer", "Old English Sheepdog", "Old English Terrier", "Olde English Bulldogge", "Original Fila Brasileiro", "Otterhound", "Pachon Navarro", "Pampas Deerhound", "Paisley Terrier", "Papillon", "Parson Russell Terrier", "Pastore della Lessinia e del Lagorai", "Patagonian Sheepdog", "Patterdale Terrier", "Pekingese", "Pembroke Welsh Corgi", "Perro Majorero", "Perro de Pastor Mallorquin", "Perro de Presa Canario[20", "Perro de Presa Mallorquin", "Peruvian Inca Orchid", "Petit Basset Griffon Vendéen", "Petit Bleu de Gascogne", "Phalène", "Pharaoh Hound", "Phu Quoc Ridgeback", "Picardy Spaniel", "Plummer Terrier", "Plott Hound", "Podenco Canario", "Podenco Valenciano", "Pointer", "Poitevin", "Polish Greyhound", "Polish Hound", "Polish Lowland Sheepdog", "Polish Tatra Sheepdog", "Pomeranian", "Pont-Audemer Spaniel", "Poodle", "Porcelaine", "Portuguese Podengo", "Portuguese Pointer", "Portuguese Water Dog", "Posavac Hound", "Pražský Krysařík", "Pshdar dog", "Pudelpointer", "Pug", "Puli", "Pumi", "Pungsan dog", "Pyrenean Mastiff", "Pyrenean Mountain Dog", "Pyrenean Sheepdog", "Rafeiro do Alentejo", "Rajapalayam", "Rampur Greyhound", "Rat Terrier", "Ratonero Bodeguero Andaluz", "Ratonero Mallorquin", "Ratonero Murciano de Huerta", "Ratonero Valenciano", "Redbone Coonhound", "Rhodesian Ridgeback", "Romanian Mioritic Shepherd Dog", "Romanian Raven Shepherd Dog", "Rottweiler", "Rough Collie", "Russian Spaniel", "Russian Toy", "Russo-European Laika", "Russell Terrier", "Saarloos Wolfdog", "Sabueso Español", "Saint Bernard", "Saint Hubert Jura Hound", "Saint-Usuge Spaniel", "Saluki", "Samoyed", "Sapsali", "Sarabi dog", "Šarplaninac", "Schapendoes", "Schillerstövare", "Schipperke", "Schweizer Laufhund", "Schweizerischer Niederlaufhund", "Scottish Deerhound", "Scottish Terrier", "Sealyham Terrier", "Segugio dell'Appennino", "Segugio Italiano", "Segugio Maremmano", "Seppala Siberian Sleddog", "Serbian Hound", "Serbian Tricolour Hound", "Serrano Bulldog", "Shar Pei", "Shetland Sheepdog", "Shiba Inu", "Shih Tzu", "Shikoku", "Shiloh Shepherd", "Siberian Husky", "Silken Windhound", "Silky Terrier", "Sinhala Hound", "Skye Terrier", "Sloughi", "Slovakian Wirehaired Pointer", "Slovenský Cuvac", "Slovenský Kopov", "Smalandstövare", "Small Greek domestic dog", "Small Münsterländer", "Smooth Collie", "Smooth Fox Terrier", "Soft-Coated Wheaten Terrier", "South Russian Ovcharka", "Spanish Mastiff", "Spanish Water Dog", "Spinone Italiano", "Sporting Lucas Terrier", "Sardinian Shepherd Dog", "Stabyhoun", "Staffordshire Bull Terrier", "Standard Schnauzer", "Stephens Stock", "Styrian Coarse-haired Hound", "Sussex Spaniel", "Swedish Elkhound", "Swedish Lapphund", "Swedish Vallhund", "Swedish White Elkhound", "Taigan", "Taiwan Dog", "Tamaskan Dog", "Teddy Roosevelt Terrier", "Telomian", "Tenterfield Terrier", "Terrier Brasileiro", "Thai Bangkaew Dog", "Thai Ridgeback", "Tibetan Mastiff", "Tibetan Spaniel", "Tibetan Terrier", "Tornjak", "Tosa", "Toy Fox Terrier", "Toy Manchester Terrier", "Transylvanian Hound", "Treeing Cur", "Treeing Feist", "Treeing Tennessee Brindle", "Treeing Walker Coonhound", "Trigg Hound", "Tyrolean Hound", "Vikhan", "Villano de Las Encartaciones", "Villanuco de Las Encartaciones", "Vizsla", "Volpino Italiano", "Weimaraner", "Welsh Sheepdog", "Welsh Springer Spaniel", "Welsh Terrier", "West Highland White Terrier", "West Siberian Laika", "Westphalian Dachsbracke", "Wetterhoun", "Whippet", "White Shepherd", "White Swiss Shepherd Dog", "Wire Fox Terrier", "Wirehaired Pointing Griffon", "Wirehaired Vizsla", "Xiasi Dog", "Xoloitzcuintli", "Yakutian Laika", "Yorkshire Terrier"
        };

        String[] CATBREEDS = new String[]{
                "Abyssinian", "Aegean", "American Bobtail", "American Curl", "American Ringtail", "American Shorthair", "American Wirehair", "Aphrodite Giant", "Arabian Mau", "Asian cat", "Asian Semi-longhair", "Australian Mist", "Balinese", "Bambino", "Bengal", "Birman", "Bombay", "Brazilian Shorthair", "British Longhair", "British Shorthair", "Burmese", "Burmilla", "California Spangled", "Chantilly-Tiffany", "Chartreux", "Chausie", "Colorpoint Shorthair", "Cornish Rex", "Cymric", "Cyprus", "Devon Rex", "Don Sphynx", "Dragon Li", "Dwelf", "Egyptian Mau", "European Shorthair", "Exotic Shorthair", "Foldex", "German Rex", "Havana Brown", "Highlander", "Himalayan", "Japanese Bobtail", "Javanese", "Kanaani", "Khao Manee", "Kinkalow", "Korat", "Korean Bobtail", "Korn Ja", "Kurilian Bobtail", "Lambkin", "LaPerm", "Lykoi", "Maine Coon", "Manx", "Mekong Bobtail", "Minskin", "Napoleon", "Munchkin", "Nebelung", "Norwegian Forest Cat", "Ocicat", "Ojos Azules", "Oregon Rex", "Oriental Bicolor", "Oriental Longhair", "Oriental Shorthair", "Persian (modern)", "Persian (traditional)", "Peterbald", "Pixie-bob", "Ragamuffin", "Ragdoll", "Raas", "Russian Blue", "Russian White", "Sam Sawet", "Savannah", "Scottish Fold", "Selkirk Rex", "Serengeti", "Serrade Petit", "Siamese", "Siberian", "Singapura", "Snowshoe", "Sokoke", "Somali", "Sphynx", "Suphalak", "Thai", "Thai Lilac", "Tonkinese", "Toyger", "Turkish Angora", "Turkish Van", "Turkish Vankedisi", "Ukrainian Levkoy", "Wila Krungthep", "York Chocolate"
        };

        //Pet Type Dropdown menu setup
        String[] PETTYPES = new String[]{"Cat", "Dog"};

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        getApplicationContext(),
                        R.layout.dropdown_menu_popup_item,
                        PETTYPES);
        AutoCompleteTextView editTextFilledExposedDropdown =
                findViewById(R.id.petType);
        editTextFilledExposedDropdown.setAdapter(adapter);


        if (petbreedview.getVisibility() == View.VISIBLE) {
            editTextFilledExposedDropdown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    petbreedview.animate()
                            .translationY(0)
                            .alpha(0.0f)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    petbreedview.setVisibility(View.GONE);
                                }
                            });
                }
            });
        } else {


            //Pet Breed View setup
            editTextFilledExposedDropdown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view1, int position, long id) {
                    petbreed.setText("");

                    // Prepare the View for the animation
                    petbreedview.setVisibility(View.VISIBLE);
                    petbreedview.setAlpha(0.0f);

                    // Start the animation
                    petbreedview.animate()
                            .alpha(1.0f)
                            .setListener(null);


                    String petTypeString = pettype.getText().toString();

                    switch (petTypeString) {
                        case "Cat": {
                            //Pet Breed Dropdown menu setup
                            ArrayAdapter<String> adapter1 =
                                    new ArrayAdapter<>(
                                            getApplicationContext(),
                                            R.layout.dropdown_menu_popup_item,
                                            CATBREEDS);
                            AutoCompleteTextView editTextFilledExposedDropdown1 =
                                    findViewById(R.id.petBreed);
                            editTextFilledExposedDropdown1.setAdapter(adapter1);
                            break;
                        }
                        case "Dog": {
                            //Pet Breed Dropdown menu setup
                            ArrayAdapter<String> adapter1 =
                                    new ArrayAdapter<>(
                                            getApplicationContext(),
                                            R.layout.dropdown_menu_popup_item,
                                            DOGBREEDS);
                            AutoCompleteTextView editTextFilledExposedDropdown1 =
                                    findViewById(R.id.petBreed);
                            editTextFilledExposedDropdown1.setAdapter(adapter1);
                            break;
                        }
                    }

                }
            });

        }

        //Pet Type Dropdown menu setup
        String[] PETORSTRAY = new String[]{"Pet", "Stray"};

        ArrayAdapter<String> adapter1 =
                new ArrayAdapter<>(
                        getApplicationContext(),
                        R.layout.dropdown_menu_popup_item,
                        PETORSTRAY);
        AutoCompleteTextView editTextFilledExposedDropdown1 =
                findViewById(R.id.petStatus);
        editTextFilledExposedDropdown1.setAdapter(adapter1);

        //Calendar
        //MaterialDatePicker
        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Select a date");
        final MaterialDatePicker materialDatePicker = builder.build();
        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDatePicker.show(getSupportFragmentManager(), "DATE_PICKER");
            }
        });

        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                dob.setText(materialDatePicker.getHeaderText());
            }
        });


                fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        //getting docreference
        userid = fAuth.getCurrentUser().getUid();
        DocumentReference documentReference = fStore.collection("Users").document(userid);

        //pulling data from db
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                //getting number of pets from the db and setting it to variable
                numberofpets = documentSnapshot.getString("Number of Pets");
            }
        });


        addpetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //update number of pets
                Map<String, String> userMap =new HashMap<>();
                int nop = Integer.parseInt(numberofpets) + 1;
                userMap.put("Number of Pets", Integer.toString(nop));
                DocumentReference documentReference = fStore.collection("Users").document(userid);
                documentReference.set(userMap, SetOptions.merge());

                //add to pet collection
                Map<String, String> petMap =new HashMap<>();
                petMap.put("Pet ID",petid);

                String uniqueId = UUID.randomUUID().toString();
                petMap.put("Pet ID",ScannedID);

                petMap.put("Pet Name",petname.getText().toString());
                petMap.put("Pet Type",pettype.getText().toString());
                petMap.put("Pet Breed",petbreed.getText().toString());
                petMap.put("Pet DOB",dob.getText().toString());
                petMap.put("Tracker Number",trackerno.getText().toString());
                petMap.put("Stray Status",straystatus.getText().toString());
                petMap.put("Status","Found");

                documentReference = fStore.collection("Users")
                                .document(userid)
                                .collection("Pets")
                                .document(""+petid);
                                .document(""+ScannedID);
                documentReference.set(petMap, SetOptions.merge());

                //updating petdocnames
                documentReference = fStore.collection("Users")
                        .document(userid)
                        .collection("Pets")
                        .document("PetDocNames");
                Map<String, String> petDocName =new HashMap<>();
                petDocName.put("PetDocName"+nop,petid);
                petDocName.put("PetDocName"+nop,ScannedID);
                documentReference.set(petDocName, SetOptions.merge());

                Intent intent=new Intent(AddPet.this,Pet.class);
                startActivity(intent);
            }
        });



        //setting profile picture
        petProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePicture();
            }
        });


        //on press settings button
        settings_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddPet.this, Settings.class);
                startActivity(intent);
            }
        });

        //on press go back
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddPet.this, Pet.class);
                startActivity(intent);
            }
        });


    }

    private void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 &&resultCode==RESULT_OK && data!=null && data.getData()!= null){
            imageUri = data.getData();
            petProfilePicture.setImageURI(imageUri);
            uploadPicture();
        }

    }

    private void uploadPicture() {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Uploading Image...");
        pd.show();

        final String randomKey = UUID.randomUUID().toString();

        StorageReference riversRef = storageReference.child("images/Pets/"+petid+".jpg");

        riversRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        pd.dismiss();
                        Snackbar.make(findViewById(android.R.id.content), "Image Uploaded.", Snackbar.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        pd.dismiss();
                        Toast.makeText(getApplicationContext(), "Failed To Upload", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                        double progressPercent = (100.00 * taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                        pd.setMessage("Percentage: " + (int) progressPercent + "%");
                    }
                });

    }
}