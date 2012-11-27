package databaseobjecthelpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class GiftRecipientJoinHelper extends SQLiteOpenHelper
{
	private static final String DATABASE_NAME = "presentpal.db";
	private static final int SCHEMA_VERSION = 1;
	
	private static final String CREATE_DATABASE_SCHEMA = "CREATE TABLE gifts_recipients (gift_id INTEGER, recipient_id INTEGER);";
	private static final String GET_BY_GIFT_ID_FROM_GIFT_RECIPIENT_DB = "SELECT gift_id, recipient_id FROM gifts_recipients WHERE gift_id = ?";
	private static final String GET_BY_RECIPIENT_ID_FROM_GIFT_RECIPIENT_DB = "SELECT gift_id, recipient_id FROM gifts_recipients WHERE recipient_id = ?";
	
	public GiftRecipientJoinHelper(Context context)
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
	
	public Cursor getByGiftId(String giftId)
	{
		String[] selectionArgs = {giftId};
		
		return getReadableDatabase().rawQuery(GET_BY_GIFT_ID_FROM_GIFT_RECIPIENT_DB, selectionArgs);
	}
	
	public Cursor getByRecipientId(String recipientId)
	{
		String[] selectionArgs = {recipientId};
		
		return getReadableDatabase().rawQuery(GET_BY_RECIPIENT_ID_FROM_GIFT_RECIPIENT_DB, selectionArgs);
	}
	
	public void insert(String giftId, String recipientId)
	{
		ContentValues contentValues = new ContentValues();
		contentValues.put("gift_id", giftId);
		contentValues.put("recipient_id", recipientId);
		
		getWritableDatabase().insert("gifts_recipients", null, contentValues);
	}
	
	public String getGiftId(Cursor cursor)
	{
		return cursor.getString(0);
	}
	
	public double getRecipientId(Cursor cursor)
	{
		return cursor.getDouble(1);
	}

}