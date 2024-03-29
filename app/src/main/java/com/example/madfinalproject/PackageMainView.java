package com.example.madfinalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PackageMainView extends AppCompatActivity {

    String uuid;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://mad-project-754dc-default-rtdb.firebaseio.com/");

    TextView actionBar;
    ImageView imageBack;
    TextView packageName1, numberOfGuest1, roomFeatures, roomTypes, fee, description1, rating, refundable,numRooms;
    Button packageEditBtn, packageDeleteBtn;
    DatabaseReference packageRef;
    Package pkg = new Package();


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_main_view);



        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            uuid = extras.getString("uuid");

        }
//Toast.makeText(PackageMainView.this,"id : "+uuid,Toast.LENGTH_SHORT).show();


        //action bar
        actionBar = findViewById(R.id.actionBar);
        actionBar.setText("Package Details");

        imageBack       = findViewById(R.id.imageBack);

        //back button
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                {PackageMainView.super.onBackPressed();}
            }
        });


        packageName1    = findViewById(R.id.packageName1);
        numberOfGuest1  = findViewById(R.id.numberOfGuest1);
        roomFeatures    = findViewById(R.id.roomFeatures);
        roomTypes       = findViewById(R.id.roomTypes);
        fee             = findViewById(R.id.fee);
        description1    = findViewById(R.id.description1);
        rating          = findViewById(R.id.rating);
        refundable      = findViewById(R.id.refundable);
        numRooms      = findViewById(R.id.numRooms);

        packageEditBtn      = findViewById(R.id.packageEditBtn);
        packageDeleteBtn    = findViewById(R.id.packageDeleteBtn);

        //package database Ref
        packageRef = databaseReference.child("Packages").child(uuid);

        //getting the packages from the database
        
        packageRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                
                if (snapshot.exists()){
                    
                    pkg = snapshot.getValue(Package.class);
                    assert pkg != null;
                    //adapter.notifyDataSetChanged();

                    packageName1.setText(pkg.getName());
                    numberOfGuest1.setText(pkg.getNuGuest());
                    fee.setText(pkg.getFee());
                    description1.setText(pkg.getDescription());
                    rating.setText("8.4");
                    refundable.setText("No");
                    numRooms.setText(pkg.getnRooms());

                    StringBuilder roomFeatures1 = new StringBuilder();

                    for (int j = 0; j < pkg.getRoomFeatures().size(); j++) {
                        roomFeatures1.append(pkg.getRoomFeatures().get(j));
                        if (j != pkg.getRoomFeatures().size() - 1) {
                            roomFeatures1.append(", ");
                        }
                    }

                    roomFeatures.setText(roomFeatures1.toString());

                    StringBuilder roomTypes1 = new StringBuilder();

                    for (int k = 0; k < pkg.getRoomTypes().size(); k++) {
                        roomTypes1.append(pkg.getRoomTypes().get(k));
                        if (k != pkg.getRoomTypes().size() - 1) {
                            roomTypes1.append(", ");
                        }
                    }
                    roomTypes.setText(roomTypes1.toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(PackageMainView.this, error.toString(),Toast.LENGTH_SHORT).show();
            }
        });

        //edit hotels functionality
        packageEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PackageMainView.this, EditPackage.class);
                intent.putExtra("pkgObj", pkg);
                startActivity(intent);
            }
        });


        //delete packages functionality
        packageDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(PackageMainView.this)
                        .setTitle("Warning")
                        .setMessage("This package will be permanently deleted! Are you sure that you want to continue?")
                        .setCancelable(true)
                        .setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        })
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                deletePackage();
                            }
                        })
                        .show();
            }
        });



    }



    private void deletePackage(){

        packageRef.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {

            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(PackageMainView.this, "Successfully Deleted the Package", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PackageMainView.this, AllPackages.class);
                startActivity(intent);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PackageMainView.this, e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });



}
}