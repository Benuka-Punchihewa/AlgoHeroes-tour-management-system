package com.example.madfinalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class HotelOwnerRegistration extends AppCompatActivity {

    //Creating object to access firebase
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://mad-project-754dc-default-rtdb.firebaseio.com/");
    int year;
    int month;
    int day;
    DatePickerDialog.OnDateSetListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_owner_registration);


        ImageView backImage = (ImageView) findViewById(R.id.backImageHO);
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            //This onClick listener called when user taps on custom back button
            public void onClick(View view) {
                HotelOwnerRegistration.super.onBackPressed();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this);//Creating the dialog object

        //Getting the id's
        EditText firstNameHotelOwner = findViewById(R.id.firstNameHotelOwner);
        EditText lastNameHotelOwner = findViewById(R.id.lastNameHotelOwner);
        EditText emailHotelOwner = findViewById(R.id.emailHotelOwner);
        EditText contactHotelOwner = findViewById(R.id.contactHotelOwner);
        Button birthDayHotelOwner = findViewById(R.id.birthdayHotelOwner);
        EditText passwordHotelOwner = findViewById(R.id.passwordHotelOwner);
        EditText reEnterPasswordHotelOwner = findViewById(R.id.reEnterPasswordHotelOwner);
        Button signUpHotelOwner = findViewById(R.id.signUpHotelOwner);

        //Creating calender and select date listener for birthday
        Calendar calendar = Calendar.getInstance();
        birthDayHotelOwner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(HotelOwnerRegistration.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,listener,year,month,day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });
        listener = new DatePickerDialog.OnDateSetListener() {
               @Override
               public void onDateSet(DatePicker datePicker, int year, int month, int dateOdMonth) {
                   month = month + 1;
                   String date = dateOdMonth + "/" + month + "/" + year;
                   birthDayHotelOwner.setText(date);
               }
           };

        //setting up a onclick listener for the signup button
        signUpHotelOwner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Getting values and turn it to Strings
                String firstNameText = firstNameHotelOwner.getText().toString().trim();
                String lastNameText = lastNameHotelOwner.getText().toString().trim();
                String emailText = emailHotelOwner.getText().toString().trim();
                String contactText = contactHotelOwner.getText().toString().trim();
                String birthdayText = birthDayHotelOwner.getText().toString().trim();
                String passwordText = passwordHotelOwner.getText().toString().trim();
                String reEnterPasswordText = reEnterPasswordHotelOwner.getText().toString().trim();


                String email = encodeUserEmail(emailText);//Encoding the Email

                if (firstNameText.isEmpty() || lastNameText.isEmpty() || email.isEmpty() || contactText.isEmpty() || birthdayText.isEmpty() || passwordText.isEmpty() || reEnterPasswordText.isEmpty()) { //Send a Toast if the fields are empty
                    //Toast toast = Toast.makeText(HotelOwnerRegistration.this, "Please fill all the fields", Toast.LENGTH_LONG);
                    //toast.show();

                    //Telling user that they must fill the all fields
                    builder.setTitle("Alert!!").setMessage("Please fill all the fields")
                            .setCancelable(true)
                            .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            })
                            .show();

                } else if (passwordText.length() < 6) { //Check weather password have more than 6 characters
                    //Toast toast = Toast.makeText(HotelOwnerRegistration.this,"Passwords must be greater than 6 character's", Toast.LENGTH_LONG);
                    //toast.show();

                    //Telling user that password must be greater than 6 character's
                    builder.setTitle("Alert!!").setMessage("Passwords must be greater than 6 character's")
                            .setCancelable(true)
                            .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            })
                            .show();

                } else if (!passwordText.equals(reEnterPasswordText)) { // Check weather passwords are matching
                    //Toast toast = Toast.makeText(HotelOwnerRegistration.this,"Passwords does not matched", Toast.LENGTH_LONG);
                    //toast.show();

                    //Telling user that password does not match with each other
                    builder.setTitle("Alert!!").setMessage("Passwords does not matched")
                            .setCancelable(true)
                            .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            })
                            .show();

                } else { //Submitting the data to the firebase

                    databaseReference.child("Hotel Owner").addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            //Checking weather the email is already taken
                            if (snapshot.hasChild(email)) {
                                //Toast.makeText(HotelOwnerRegistration.this, "Email is already taken", Toast.LENGTH_SHORT).show();

                                //Telling user that email is already taken
                                builder.setTitle("Alert!!").setMessage("Email is already taken")
                                        .setCancelable(true)
                                        .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.cancel();
                                            }
                                        })
                                        .show();

                            } else {
                                databaseReference.child("Tour Guide").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                        if (snapshot.hasChild(email)) {
                                            //Toast.makeText(HotelOwnerRegistration.this, "Email is already taken", Toast.LENGTH_SHORT).show();

                                            //Telling user that email is already taken
                                            builder.setTitle("Alert!!").setMessage("Email is already taken")
                                                    .setCancelable(true)
                                                    .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                            dialogInterface.cancel();
                                                        }
                                                    })
                                                    .show();

                                        } else {
                                            //Checking weather the email is already taken
                                            databaseReference.child("Traveler").addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                    //Checking weather the email is already taken
                                                    if (snapshot.hasChild(email)) {
                                                        //Toast.makeText(HotelOwnerRegistration.this, "Email is already taken", Toast.LENGTH_SHORT).show();

                                                        //Telling user that email is already taken
                                                        builder.setTitle("Alert!!").setMessage("Email is already taken")
                                                                .setCancelable(true)
                                                                .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                                        dialogInterface.cancel();
                                                                    }
                                                                })
                                                                .show();

                                                    } else {

                                                        //Sending data to the data base
//                                                        databaseReference.child("HotelOwner").child(email).child("Email").setValue(email);
//                                                        databaseReference.child("HotelOwner").child(email).child("First Name").setValue(firstNameText);
//                                                        databaseReference.child("HotelOwner").child(email).child("Last Name").setValue(lastNameText);
//                                                        databaseReference.child("HotelOwner").child(email).child("Contact Number").setValue(contactText);
//                                                        databaseReference.child("HotelOwner").child(email).child("Password").setValue(passwordText);

                                                        Users hotelOwner = new Users(firstNameText, lastNameText, email, contactText, birthdayText, passwordText);
                                                        databaseReference.child("Hotel Owner").child(email).setValue(hotelOwner);


                                                        //Sendin toast to user to tell signUp is successfully
                                                        Toast.makeText(HotelOwnerRegistration.this, "Successfully SignUp as a Hotel Owner", Toast.LENGTH_SHORT).show();
                                                        Intent intent2 = new Intent(HotelOwnerRegistration.this, SignIn.class); //Redirect user to the login page
                                                        startActivity(intent2);
                                                        finish();

                                                    }

                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) { //If te database connection lost sending a Toast massage to th user
                                                    Toast.makeText(HotelOwnerRegistration.this, "Lost the connection with the Database", Toast.LENGTH_SHORT).show();

                                                }
                                            });

                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) { //If te database connection lost sending a Toast massage to th user
                                        Toast.makeText(HotelOwnerRegistration.this, "Lost the connection with the Database", Toast.LENGTH_SHORT).show();

                                    }
                                });
                            }



                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) { //If te database connection lost sending a Toast massage to th user
                            Toast.makeText(HotelOwnerRegistration.this, "Lost the connection with the Database", Toast.LENGTH_SHORT).show();

                        }
                    });

                }
            }
        });

    }

    static String encodeUserEmail(String email) {
        return email.replace(".", ",");
    }
}

