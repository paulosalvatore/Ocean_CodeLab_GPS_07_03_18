package paulosalvatore.com.br.ocean_codelab_gps_07_03_18;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by paulo on 06/03/2018.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
	private Context context;

	public DatabaseHelper (Context context) {
		super(context, "codelab_db", null, 1);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		try {
			db.execSQL("CREATE TABLE posicoes (id integer PRIMARY KEY, latitude double, longitude double, data_hora varchar(255));");
		}
		catch (Exception e) {
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int i, int i1) {
	}
}
