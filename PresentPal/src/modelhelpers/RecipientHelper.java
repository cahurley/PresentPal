package modelhelpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class RecipientHelper extends DatabaseHelper
{
	private static final String TABLE_NAME = "recipients";
	private static final String GET_ALL_FROM_RECIPIENT_DB = "SELECT _id, name FROM recipients ORDER BY %s";
	private static final String GET_BY_ID_FROM_RECIPIENT_DB = "SELECT _id, name FROM recipients WHERE _ID = ?";
	
	public RecipientHelper(Context context)
	{
		super(context);
	}
	
	public Cursor getAll(String orderBy)
	{
		return getReadableDatabase().rawQuery(String.format(GET_ALL_FROM_RECIPIENT_DB, orderBy), null);
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
	
	public void removeById(String id)
	{
		String[] whereArgs = {id};
		
		getReadableDatabase().delete(TABLE_NAME, "_ID=?", whereArgs);
	}
	
	public String getId(Cursor cursor)
	{
		return cursor.getString(0);
	}
	
	public String getName(Cursor cursor)
	{
		return cursor.getString(1);
	}
}
