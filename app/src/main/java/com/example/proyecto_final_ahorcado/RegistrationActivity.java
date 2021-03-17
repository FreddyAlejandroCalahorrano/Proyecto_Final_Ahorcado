package com.example.proyecto_final_ahorcado;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.proyecto_final_ahorcado.model.Jugador;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



public class RegistrationActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    final String TAG = "RegistrationActivity";
    EditText nomJ, coor;
    String palabra, ID;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mAuth = FirebaseAuth.getInstance();

        Button registerButton = findViewById(R.id.buttonRegister);
        final EditText usernameInput = findViewById(R.id.usernameInput);
        final EditText passwordInput = findViewById(R.id.passwordInput);
        nomJ = findViewById(R.id.nameInput);
        coor = findViewById(R.id.usernameInput);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signUp(usernameInput.getText().toString(), passwordInput.getText().toString());
            }
        });
    }
    private void signUp(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            Toast.makeText(RegistrationActivity.this, "Authentication Success." + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                            inicializarFirebase();
                            inicializacionUsuario();
                            finish();

                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegistrationActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void inicializacionUsuario(){
        palabra = coor.getText().toString();
        ID = palabra.replaceAll(".com", "");
        String nombre = nomJ.getText().toString();
        String puntaje = "0";
        if (nombre.equals("")){
            validacion();
        }
        else {
            Jugador j = new Jugador();
            j.setUid(ID);
            j.setNombre(nombre);
            j.setPuntaje(puntaje);
            databaseReference.child("Jugador").child(j.getUid()).setValue(j);
            limpiarCajas();
        }
    }
    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }
    private void validacion() {
        String nombre = nomJ.getText().toString();
        if (nombre.equals("")){
            nomJ.setError("Required");
        }
    }
    private void limpiarCajas() {
        nomJ.setText("");
    }
}