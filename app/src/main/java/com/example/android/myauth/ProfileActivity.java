package com.example.android.myauth;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;
    private Button buttonLogout;
    private TextView textViewUserEmail;
    private EditText editTextName;
    private EditText editTextEmailA;
    private Button buttonSave;
    private DatabaseReference databaseReference;// with this refernce we store data to firebase database
    private EditText editTextNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser()==null)
        {
            finish();
            startActivity(new Intent(this,LoginActivity.class));
        }


        FirebaseUser user = firebaseAuth.getCurrentUser();

        databaseReference = FirebaseDatabase.getInstance().getReference();
        buttonLogout = (Button)findViewById(R.id.buttonLogout);
        textViewUserEmail = (TextView)findViewById(R.id.textViewUserEmail);
        textViewUserEmail.setText("Welcome "+user.getEmail());
        editTextName = (EditText)findViewById(R.id.editTextName);
        editTextEmailA = (EditText)findViewById(R.id.editTextEmailAddress);
        editTextNumber = (EditText)findViewById(R.id.editTextNumber);
        buttonSave = (Button)findViewById(R.id.buttonSaveInfo);
        buttonLogout.setOnClickListener(this);
        buttonSave.setOnClickListener(this);
    }

    public void saveUserInfo()
    {
        String name = editTextName.getText().toString().trim();
        String email = editTextEmailA.getText().toString().trim();
        String num = editTextNumber.getText().toString().trim();

        UserInfo userInfo = new UserInfo(name,email,num);
        FirebaseUser user = firebaseAuth.getCurrentUser();
        databaseReference.child(user.getUid()).setValue(userInfo);
        Toast.makeText(this,"Information Saved...",Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onClick(View view) {
        if(view ==  buttonLogout)
        {
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        if(view == buttonSave)
            saveUserInfo();

    }
}
