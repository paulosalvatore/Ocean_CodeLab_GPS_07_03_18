package paulosalvatore.com.br.ocean_codelab_gps_07_03_18;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by paulo on 07/03/2018.
 */

class ListaAdapter extends ArrayAdapter<Posicao> {
	private final Context context;
	private List<Posicao> posicoes;

	public ListaAdapter(Context context, int resource, List<Posicao> posicoes) {
		super(context, resource, posicoes);
		this.context = context;
		this.posicoes = posicoes;
	}

	@Override
	public View getView(int i, View view, ViewGroup viewGroup) {
		View row = view;

		if (row == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.lista_layout, viewGroup, false);
		}

		final Posicao item = posicoes.get(i);

		if (item != null) {
			TextView campoId = row.findViewById(R.id.campoId);
			TextView campoLatitude = row.findViewById(R.id.campoLatitude);
			TextView campoLongitude = row.findViewById(R.id.campoLongitude);
			TextView campoDataHora = row.findViewById(R.id.campoDataHora);

			campoId.setText("ID: " + item.getId());
			campoLatitude.setText("Lat: " + item.getLatitude());
			campoLongitude.setText("Lon: " + item.getLongitude());
			campoDataHora.setText("Data/Hora: " + item.getDataHora());
		}

		return row;
	}
}
