package paulosalvatore.com.br.ocean_codelab_gps_07_03_18;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener {

	private static final int PERMISSAO_LOCALIZACAO = 1;
	private Location ultimaLocalizacao;
	private GoogleMap mapa;
	private DatabaseManager db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		DatabaseHelper helper = new DatabaseHelper(this.getApplicationContext());
		DatabaseManager.initializeInstance(helper);
		db = DatabaseManager.getInstance();

		inicializarLocalizacao();
	}

	private void inicializarLocalizacao() {
		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(
					this,
					new String[]{
							Manifest.permission.ACCESS_COARSE_LOCATION,
							Manifest.permission.ACCESS_FINE_LOCATION
					},
					PERMISSAO_LOCALIZACAO
			);

			return;
		}

		LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

		try {
			//Criteria criteria = new Criteria();
			//criteria.setAccuracy(Criteria.ACCURACY_LOW);

			//String provider = locationManager.getBestProvider(criteria, true);

			List<String> providers = locationManager.getProviders(true);
			Location bestLocation = null;
			for (String provider : providers) {
				Location l = locationManager.getLastKnownLocation(provider);
				if (l == null) {
					continue;
				}
				if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
					// Found best last known location: %s", l);
					bestLocation = l;
				}
			}

			atualizarPosicao(bestLocation);

			inicializarMapa();
		}
		catch (Exception e) {
			Log.e("inicializarLocalizacao", e.toString());
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		switch (requestCode) {
			case PERMISSAO_LOCALIZACAO:
				if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					inicializarLocalizacao();
				}

				break;
		}
	}

	private void inicializarMapa() {
		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragMap);
		mapFragment.getMapAsync(this);
	}

	@Override
	public void onLocationChanged(Location location) {
		Toast.makeText(this, "Localização alterada.", Toast.LENGTH_SHORT).show();

		atualizarPosicao(location);
	}

	private void atualizarPosicao(Location location) {
		ultimaLocalizacao = location;

		Posicao posicao = new Posicao(
				ultimaLocalizacao.getLatitude(),
				ultimaLocalizacao.getLongitude(),
				Calendar.getInstance().getTime().toString()
		);

		db.inserirPosicao(posicao);

		LatLng latLng = new LatLng(
				ultimaLocalizacao.getLatitude(),
				ultimaLocalizacao.getLongitude()
		);

		mapa.animateCamera(
				CameraUpdateFactory.newLatLngZoom(
						latLng,
						14
				)
		);
	}

	@Override
	public void onStatusChanged(String s, int i, Bundle bundle) {
		Toast.makeText(this, "Iniciando busca da localização.", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onProviderEnabled(String s) {
		Toast.makeText(this, "GPS Habilitado.", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onProviderDisabled(String s) {
		Toast.makeText(this, "GPS Desabilitado.", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onMapReady(GoogleMap googleMap) {
		mapa = googleMap;

		if (ultimaLocalizacao == null) {
			return;
		}

		LatLng posicao = new LatLng(
				ultimaLocalizacao.getLatitude(),
				ultimaLocalizacao.getLongitude()
		);

		mapa.addMarker(new MarkerOptions().position(posicao).title("Minha posição."));
		mapa.moveCamera(
				CameraUpdateFactory.newLatLngZoom(
						posicao,
						14
				)
		);
	}

	public void abrirTelaLista(View view) {
		Intent intent = new Intent(this, ListaActivity.class);
		startActivity(intent);
	}
}
