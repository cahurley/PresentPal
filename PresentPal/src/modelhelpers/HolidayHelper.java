package modelhelpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class HolidayHelper extends SQLiteOpenHelper
{
	private static final String DATABASE_NAME = "presentpal.db";
	private static final int SCHEMA_VERSION = 1;
	
	private static final String CREATE_DATABASE_SCHEMA = "CREATE TABLE holidays (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, date REAL);";
	private static final String GET_ALL_FROM_HOLIDAY_DB = "SELECT _id, name, date FROM holidays ORDER BY ";
	private static final String GET_BY_ID_FROM_HOLIDAY_DB = "SELECT _id, name, date FROM holidays WHERE _ID = ?";
	
	public HolidayHelper(Context context)
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
		return getReadableDatabase().rawQuery(GET_ALL_FROM_HOLIDAY_DB + orderBy, null);
	}
	
	public Cursor getById(String id)
	{
		String[] selectionArgs = {id};
		
		return getReadableDatabase().rawQuery(GET_BY_ID_FROM_HOLIDAY_DB, selectionArgs);
	}
	
	public void insert(String name, long date)
	{
		ContentValues contentValues = new ContentValues();
		contentValues.put("name", name);
		contentValues.put("date", date);
		
		getWritableDatabase().insert("gifts", null, contentValues);
	}
	
	public void update(String id, String name, long date)
	{
		String[] whereArgs = {id};
		ContentValues contentValues = new ContentValues();
		contentValues.put("name", name);
		contentValues.put("date", date);
		
		getWritableDatabase().update("gifts", contentValues, "_ID=?", whereArgs);
	}
	
	public String getName(Cursor cursor)
	{
		return cursor.getString(1);
	}
	
	public double getPrice(Cursor cursor)
	{
		return cursor.getDouble(2);
	}
	
	public int getQuantity(Cursor cursor)
	{
		return cursor.getInt(3);
	}
}