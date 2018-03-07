package paulosalvatore.com.br.ocean_codelab_gps_07_03_18;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.List;

public class ListaActivity extends AppCompatActivity {

	private ListView lvLista;
	private DatabaseManager db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lista);

		lvLista = findViewById(R.id.lvLista);

		DatabaseHelper helper = new DatabaseHelper(this.getApplicationContext());
		DatabaseManager.initializeInstance(helper);
		db = DatabaseManager.getInstance();

		List<Posicao> posicoes = db.obterPosicoes();

		lvLista.setAdapter(
				new ListaAdapter(
						this,
						R.layout.lista_layout,
						posicoes
				)
		);
	}
}
