package swetipi.presentpal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class GiftRecipientHelper extends SQLiteOpenHelper 
{
	private static final String DATABASE_NAME = "presentpal.db";
	private static final int SCHEMA_VERSION = 1;
	
	private static final String CREATE_DATABASE_SCHEMA = "CREATE TABLE recipients (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT);";
	private static final String GET_ALL_FROM_RECIPIENT_DB = "SELECT _id, name FROM recipients ORDER BY ";
	private static final String GET_BY_ID_FROM_RECIPIENT_DB = "SELECT _id, name FROM recipients WHERE _ID = ?";
	
	public GiftRecipientHelper(Context context)
	{
		super(context, DATABASE_NAME, null, SCHEMA_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase database)
	{
		database.execSQL(CREATE_DATABASE_SCHEMA);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion)
	{
		// nothing
	}
	
	public Cursor getAll(String orderBy)
	{
		return getReadableDatabase().rawQuery(GET_ALL_FROM_RECIPIENT_DB + orderBy, null);
	}
	
	public Cursor getById(String id)
	{
		String[] selectionArgs = {id};
		
		return getReadableDatabase().rawQuery(GET_BY_ID_FROM_RECIPIENT_DB, selectionArgs);
	}
	
	public void insert(String name)
	{
		ContentValues contentValues = new ContentValues();
		contentValues.put("name", name);
		
		getWritableDatabase().insert("recipients", null, contentValues);
	}
	
	public void update(String id, String name)
	{
		String[] whereArgs = {id};
		ContentValues contentValues = new ContentValues();
		contentValues.put("name", name);
		
		getWritableDatabase().update("recipients", contentValues, "_ID=?", whereArgs);
	}
	
	public String getName(Cursor cursor)
	{
		return cursor.getString(1);
	}
}
