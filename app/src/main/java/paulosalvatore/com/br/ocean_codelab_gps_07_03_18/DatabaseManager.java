package paulosalvatore.com.br.ocean_codelab_gps_07_03_18;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by paulo on 06/03/2018.
 */

public class DatabaseManager {
	private static DatabaseManager instance;
	private static DatabaseHelper mDatabaseHelper;
	private boolean conexaoAberta = false;
	private SQLiteDatabase db;

	public static synchronized void initializeInstance(DatabaseHelper helper) {
		if (instance == null) {
			instance = new DatabaseManager();
			mDatabaseHelper = helper;
		}
	}

	public static synchronized DatabaseManager getInstance() {
		if (instance == null) {
			throw new IllegalStateException("Declare 'initializeInstance(...)' primeiro.");
		}

		return instance;
	}

	public void openDb() {
		if (!conexaoAberta) {
			db = mDatabaseHelper.getWritableDatabase();
			conexaoAberta = true;
		}
	}

	public void closeDb() {
		if (conexaoAberta) {
			db.close();
			conexaoAberta = false;
		}
	}

	protected void close(Cursor cursor) {
		if (cursor != null) {
			cursor.close();
		}
	}

	public List<Posicao> obterPosicoes() {
		List<Posicao> posicoes = new ArrayList<Posicao>();

		openDb();

		String sql = "SELECT * FROM posicoes";
		Cursor cursor = db.rawQuery(sql, null);

		if (cursor.moveToFirst()) {
			do {
				int id = cursor.getInt(cursor.getColumnIndex("id"));
				double latitude = cursor.getDouble(cursor.getColumnIndex("latitude"));
				double longitude = cursor.getDouble(cursor.getColumnIndex("longitude"));
				String data_hora = cursor.getString(cursor.getColumnIndex("data_hora"));

				Posicao posicao = new Posicao(
						id,
						latitude,
						longitude,
						data_hora
				);

				posicoes.add(posicao);

			} while (cursor.moveToNext());
		}

		return posicoes;
	}

	public void inserirPosicao(Posicao posicao) {
		openDb();

		ContentValues values = new ContentValues();
		values.put("latitude", posicao.getLatitude());
		values.put("longitude", posicao.getLongitude());
		values.put("data_hora", posicao.getDataHora());

		db.insert("posicoes", null, values);

		closeDb();
	}

	public void atualizarPosicao(Posicao posicao) {
		String sql = "UPDATE posicoes SET latitude = '" + posicao.getLatitude() + "'," +
				"longitude = '" + posicao.getLongitude() + "'," +
				"data_hora = '" + posicao.getDataHora() + "' " +
				"WHERE (id = '" + posicao.getId() + "')";

		openDb();

		db.execSQL(sql);

		closeDb();
	}

	public void removerPosicao(Posicao posicao) {
		String sql = "DELETE FROM posicoes WHERE (id = '" + posicao.getId() + "')";

		openDb();

		db.execSQL(sql);

		closeDb();
	}
}
