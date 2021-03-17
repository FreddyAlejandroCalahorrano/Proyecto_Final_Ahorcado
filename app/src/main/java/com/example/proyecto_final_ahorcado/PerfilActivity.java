package com.example.proyecto_final_ahorcado;


import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.proyecto_final_ahorcado.model.Jugador;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;


public class PerfilActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    EditText nomJ;
    String ID, nomJBD, ScoreR;
    String email;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();

        email = user.getEmail();
        ID = email.replaceAll(".com", "");

        TextView welcomeText = findViewById(R.id.welcomeText);
        welcomeText.setText("Ingresaste : " + email);
        FirebaseDatabase fdb = FirebaseDatabase.getInstance();

        //apuntamos al nodo que queremos leer
        DatabaseReference myRef = fdb.getReference("Jugador/"+ ID);
        myRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot){

                //leeremos un objeto de tipo Estudiante
                GenericTypeIndicator<Jugador> t = new GenericTypeIndicator<Jugador>() {};
                Jugador player = dataSnapshot.getValue(t);
                nomJBD = player.getNombre();
                EditText nombre = findViewById(R.id.username);
                nombre.setText(nomJBD);
                Log.d("nombre",nomJBD);
                ScoreR = player.getPuntaje();
                EditText puntaje = findViewById(R.id.userpuntaje);
                puntaje.setText(ScoreR);
                Log.d("Puntaje",ScoreR);

            }
            @Override
            public void onCancelled(DatabaseError error){
                Log.e("ERROR FIREBASE",error.getMessage());
            }

        });

        inicializarFirebase();
        Button editpntjButton = findViewById(R.id.btneditNom);
        editpntjButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nomJ = findViewById(R.id.username);
                String nombre = nomJ.getText().toString();
                if (nombre.equals("")){
                    validacion();
                }
                else{
                    Jugador j = new Jugador();
                    j.setUid(ID);
                    j.setNombre(nombre);
                    j.setPuntaje(ScoreR);
                    databaseReference.child("Jugador").child(j.getUid()).setValue(j);
                    ShowAlertActualizar();
                }
            }
        });
        Button elimUsrarioButton = findViewById(R.id.btnelmPunt);
        elimUsrarioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowAlertEliminae();
            }
        });

    }
    protected void ShowAlertActualizar(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alerta!!!");
        builder.setMessage("Nombre Actualizado.");
        builder.setPositiveButton("Aceptar", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    protected void ShowAlertEliminae(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("!!!ALERTA!!!");
        builder.setMessage("¿Seguro Quiere eliminar su cuenta?");

        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ////////////Eliminar Datos del Uusario//////////////
                Jugador j = new Jugador();
                j.setUid(ID);
                j.setNombre(nomJBD);
                j.setPuntaje(ScoreR);
                databaseReference.child("Jugador").child(j.getUid()).removeValue();
                ////////////////////////////////////////////////////////
                FirebaseUser user = mAuth.getCurrentUser();
                user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            mAuth.signOut();
                            Intent intent=new Intent(PerfilActivity.this,LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
            }
        });
        builder.setNegativeButton("Cancelar", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    protected void ShowElim(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Exito!!");
        builder.setMessage("Usuario Elimninado");
        builder.setPositiveButton("Aceptar", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }
    private void validacion() {
        String nombre = nomJ.getText().toString();
        if (nombre.equals("")){
            nomJ.setError("Nombre Requerido");
        }
    }
}