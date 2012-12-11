package modelhelpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class GiftHelper extends DatabaseHelper
{
	private static final String TABLE_NAME = "gifts";
	private static final String GET_ALL_FROM_GIFT_DB = "SELECT _id, name, price, quantity_purchased, total_quantity, recipient_id FROM gifts ORDER BY %s";
	private static final String GET_BY_RECIPIENT_ID_FROM_GIFT_DB = "SELECT _id, name, price, quantity_purchased, total_quantity, recipient_id FROM gifts WHERE recipient_id = %s;";
	private static final String GET_BY_ID_FROM_GIFT_DB = "SELECT _id, name, price, quantity_purchased, total_quantity, recipient_id FROM gifts WHERE _ID = ?;";
	
	public GiftHelper(Context context)
	{
		super(context);
	}
	
	public Cursor getAll(String orderBy)
	{
		return getReadableDatabase().rawQuery(String.format(GET_ALL_FROM_GIFT_DB, orderBy), null);
	}
	
	public Cursor getById(String id)
	{
		String[] selectionArgs = {id};
		
		return getReadableDatabase().rawQuery(GET_BY_ID_FROM_GIFT_DB, selectionArgs);
	}
	
	public Cursor getByRecipientId(String recipientId)
	{
		return getReadableDatabase().rawQuery(String.format(GET_BY_RECIPIENT_ID_FROM_GIFT_DB, recipientId), null);
	}
	
	public long insert(String name, double price, int total_quantity, String recipientId)
	{
		ContentValues contentValues = new ContentValues();
		contentValues.put("name", name);
		contentValues.put("price", price);
		contentValues.put("quantity_purchased", 0);
		contentValues.put("total_quantity", total_quantity);
		contentValues.put("recipient_id", recipientId);
		
		return getWritableDatabase().insert(TABLE_NAME, null, contentValues);
	}
	
	public void incrementQuantityPurchased(String id, Cursor giftCursor)
	{
		String[] whereArgs = {id};
		ContentValues contentValues = new ContentValues();
		contentValues.put("quantity_purchased", getQuantityPurchased(giftCursor) + 1);
		
		getWritableDatabase().update(TABLE_NAME, contentValues, "_ID=?", whereArgs);
	}
	
	public void update(String id, String name, double price, int quantity_purchased, int total_quantity, String recipientId)
	{
		String[] whereArgs = {id};
		ContentValues contentValues = new ContentValues();
		contentValues.put("name", name);
		contentValues.put("price", price);
		contentValues.put("quantity_purchased", quantity_purchased);
		contentValues.put("total_quantity", total_quantity);
		contentValues.put("recipient_id", recipientId);
		
		getWritableDatabase().update(TABLE_NAME, contentValues, "_ID=?", whereArgs);
	}
	
	public void removeById(String id)
	{
		String[] whereArgs = {id};
		
		getReadableDatabase().delete(TABLE_NAME, "_ID=?", whereArgs);
	}
	
	public void removeByRecipientId(String recipientId)
	{
		Cursor cursor = getByRecipientId(recipientId);
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())
		{
			removeById(getId(cursor));
		}
	}
	
	public String getId(Cursor cursor)
	{
		return cursor.getString(0);
	}
	
	public String getName(Cursor cursor)
	{
		return cursor.getString(1);
	}
	
	public double getPrice(Cursor cursor)
	{
		return cursor.getDouble(2);
	}
	
	public int getQuantityPurchased(Cursor cursor)
	{
		return cursor.getInt(3);
	}
	
	public int getTotalQuantity(Cursor cursor)
	{
		return cursor.getInt(4);
	}
	
	public String getRecipientId(Cursor cursor)
	{
		return cursor.getString(5);
	}
}