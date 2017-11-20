package com.example.android.myauth;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonRegister;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewSignin;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonRegister = (Button)findViewById(R.id.buttonRegister);
        editTextEmail = (EditText)findViewById(R.id.editTextEmail);
        editTextPassword = (EditText)findViewById(R.id.editTextPassword);
        textViewSignin = (TextView)findViewById(R.id.textViewSignin);
        buttonRegister.setOnClickListener(this);
        textViewSignin.setOnClickListener(this);
        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser()!=null)
        {
            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
        }
    }

    private void registerUser()
    {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(this,"Please Enter email",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this,"Please Enter Password",Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.setMessage("Registering user...");
        progressDialog.show();
        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(MainActivity.this,"User Registered",Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            finish();
                            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                        }
                        else
                        {
                            Toast.makeText(MainActivity.this,"User Not Registered",Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                });
    }

    @Override
    public void onClick(View view)
    {
        if(view == buttonRegister)
        {
            ConnectivityManager cm =
                    (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null &&
                    activeNetwork.isConnectedOrConnecting();
            if(isConnected)
            registerUser();
            else
                Toast.makeText(MainActivity.this,"Internet Access Required",Toast.LENGTH_SHORT).show();

        }
        if(view == textViewSignin)
        {
            //will open login activity here
            finish();
            startActivity(new Intent(this,LoginActivity.class));
        }
    }
}
