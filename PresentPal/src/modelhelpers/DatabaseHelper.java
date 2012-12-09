package modelhelpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public abstract class DatabaseHelper extends SQLiteOpenHelper 
{
	private static final String DATABASE_NAME = "presentpal.db";
	private static final int SCHEMA_VERSION = 1;
	
	private static final String CREATE_RECIPIENT_TABLE = "CREATE TABLE recipients (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT);";
	private static final String CREATE_GIFT_TABLE = "CREATE TABLE gifts (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, price REAL, quantity_purchased INTEGER, total_quantity INTEGER, recipient_id INTEGER);";
	
	public DatabaseHelper(Context context)
	{
		super(context, DATABASE_NAME, null, SCHEMA_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) 
	{
		database.execSQL(CREATE_RECIPIENT_TABLE);
		database.execSQL(CREATE_GIFT_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
	{
		// nothing
	}

}
