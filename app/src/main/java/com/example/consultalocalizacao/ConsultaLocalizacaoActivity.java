package com.example.consultalocalizacao;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class ConsultaLocalizacaoActivity extends AppCompatActivity {

    private EditText txtLatitude;
    private EditText txtLongetude;
    private FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta_localizacao);

        txtLatitude = findViewById(R.id.edtLatitude);
        txtLongetude = findViewById(R.id.edtLongetude);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
    }

    public void chamarGoogleMaps(View view) {
        try {
            if (txtLatitude.getText().toString().isEmpty()) {
                Toast.makeText(this, "Preencha o campo Latitude para continuar.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (txtLongetude.getText().toString().isEmpty()) {
                Toast.makeText(this, "Preencha o campo Longetude para continuar.", Toast.LENGTH_SHORT).show();
                return;
            }

            double latitude = Double.parseDouble(txtLatitude.getText().toString());
            double longitude = Double.parseDouble(txtLongetude.getText().toString());

            String strUri = "http://maps.google.com/maps?q=loc:" + latitude + "," + longitude;
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(strUri));
            intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
            startActivity(intent);
        } catch (Exception ex) {
            Toast.makeText(this, "Verifique se o Google Maps está instalado em seu dispositivo", Toast.LENGTH_SHORT).show();
        }
    }

    public void obterLocalizacaoAtual(View view) {
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                fusedLocationProviderClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, null).addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location != null) {
                            txtLatitude.setText(String.valueOf(location.getLatitude()));
                            txtLongetude.setText(String.valueOf(location.getLongitude()));
                        }
                    }
                });
            }
        } catch (Exception ex) {
            Toast.makeText(this, "Não foi possivel obter a localização atual.", Toast.LENGTH_SHORT).show();
        }
    }
}