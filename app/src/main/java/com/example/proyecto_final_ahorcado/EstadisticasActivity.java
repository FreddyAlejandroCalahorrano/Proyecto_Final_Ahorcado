package com.example.proyecto_final_ahorcado;


import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.proyecto_final_ahorcado.model.Jugador;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EstadisticasActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    private List<Jugador> listGamer = new ArrayList<Jugador>();
    ArrayAdapter<Jugador> arrayAdapterJugador;

    ListView listV_jugadores;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estadisticas);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        listV_jugadores = findViewById(R.id.listajugadores);

        inicializarFirebase();
        listarDatos();
    }
    private void listarDatos() {
        databaseReference.child("Jugador").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listGamer.clear();
                for (DataSnapshot objSnaptshot : dataSnapshot.getChildren()){
                    Jugador j = objSnaptshot.getValue(Jugador.class);
                    listGamer.add(j);
                    arrayAdapterJugador = new ArrayAdapter<Jugador>(EstadisticasActivity.this, android.R.layout.simple_list_item_1, listGamer);
                    listV_jugadores.setAdapter(arrayAdapterJugador);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }
    public void Regresar(View view) {
        Intent intent=new Intent(this,HubActivity.class);
        startActivity(intent);
    }
}