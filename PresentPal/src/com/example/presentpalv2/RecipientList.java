package com.example.presentpalv2;

import modelhelpers.GiftHelper;
import modelhelpers.RecipientHelper;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class RecipientList extends ListActivity 
{
	public static String RECIPIENT_ID_EXTRA = "swetipi.presentpal.recipient_id";
	
	private Cursor recipientCursor = null;
	private RecipientListAdapter adapter = null;
	private RecipientHelper recipientHelper = null;
	private GiftHelper giftHelper = null;
	
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipient_list);
        
        recipientHelper = new RecipientHelper(this);
        recipientCursor = recipientHelper.getAll("name");
        giftHelper = new GiftHelper(this);
        
        ListView list = getListView();
        adapter = new RecipientListAdapter(recipientCursor);
        list.setAdapter(adapter);
        
        list.setOnItemLongClickListener(new OnItemLongClickListener()
        {
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) 
			{	
				recipientCursor.moveToPosition(position);
				
				AlertDialog.Builder builder = new AlertDialog.Builder(RecipientList.this);
				builder.setCancelable(true);
				builder.setMessage(String.format("Are you sure you want to delete %s from your list?", recipientHelper.getName(recipientCursor)));
				
				builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() 
				{
					@SuppressWarnings("deprecation")
					public void onClick(DialogInterface dialog, int which) 
					{
						giftHelper.removeByRecipientId(recipientHelper.getId(recipientCursor));
						recipientHelper.removeById(recipientHelper.getId(recipientCursor));
						recipientCursor.requery();
						
						dialog.dismiss();
					}
				});
				
				builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() 
				{
					public void onClick(DialogInterface dialog, int which) 
					{
						dialog.dismiss();
					}
				});
				
				builder.create().show();
				
				return true;
			}	

		});
        
        list.setOnItemClickListener(new OnItemClickListener() 
        {	
        	public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
        	{
        		recipientCursor.moveToPosition(position);
        		
        		Intent intent = new Intent(RecipientList.this, GiftListActivity.class);
        		intent.putExtra(RECIPIENT_ID_EXTRA, String.valueOf(recipientHelper.getId(recipientCursor)));
        		startActivity(intent);
			}
		});
    }
	
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		
		recipientHelper.close();
		giftHelper.close();
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		
		recipientCursor.requery();
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        getMenuInflater().inflate(R.menu.activity_recipient_list, menu);
        
        return super.onCreateOptionsMenu(menu);
    }
    
	@Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
    	int itemId = item.getItemId();
    	if (itemId == R.id.menu_add_recipient)
    	{
    		Dialog dialog = DialogFactory.createtDialog(this, recipientCursor, recipientHelper, DialogType.ADD_RECIPIENT);
    		dialog.show();
    	}
    	
    	return super.onOptionsItemSelected(item);
    }
    
    class RecipientListAdapter extends CursorAdapter
    {
    	public RecipientListAdapter(Cursor cursor) 
    	{
			super(RecipientList.this, cursor);
		}
    	
    	@Override
    	public void bindView(View row, Context context, Cursor cursor)
    	{
    		RecipientHolder holder = (RecipientHolder)row.getTag();
    		
    		holder.populateFrom(cursor, recipientHelper, giftHelper);
    	}
    	
    	@Override
    	public View newView(Context context, Cursor cursor, ViewGroup parent)
    	{
    		LayoutInflater inflater = getLayoutInflater();
    		View row = inflater.inflate(R.layout.list_row, parent, false);
    		RecipientHolder holder = new RecipientHolder(row);
    		
    		row.setTag(holder);
    		
    		return row;
    	}
    }
    
    static class RecipientHolder
    {
    	private TextView name = null;
    	private TextView quantity = null;
    	private TextView price = null;
    	
    	public RecipientHolder(View row) 
    	{
			name = (TextView)row.findViewById(R.id.textview_name);
			quantity = (TextView)row.findViewById(R.id.textview_gift_quantity);
			price = (TextView)row.findViewById(R.id.textview_gift_price);
		}
    	
    	void populateFrom(Cursor recipientCursor, RecipientHelper recipientHelper, GiftHelper giftHelper)
    	{
    		int giftQuantityPurchased = 0;
    		int totalQuantity = 0;
    		double giftPricePurchased = 0.00;
    		double totalPrice = 0.00;
    		
    		Cursor giftCursor = giftHelper.getByRecipientId(recipientHelper.getId(recipientCursor));
    		if (giftCursor.getCount() > 0)
    		{
    			int giftsPurchased;
    			int totalGifts;
    			double giftPrice;
    			for (giftCursor.moveToFirst(); !giftCursor.isAfterLast(); giftCursor.moveToNext())
    			{
    				giftsPurchased = giftHelper.getQuantityPurchased(giftCursor);
    				totalGifts = giftHelper.getTotalQuantity(giftCursor);
    				giftPrice = giftHelper.getPrice(giftCursor);
    				
    				giftQuantityPurchased += giftsPurchased;
    				totalQuantity += totalGifts;
    				giftPricePurchased += (giftPrice * giftsPurchased);
    				totalPrice += (giftPrice * totalGifts);
    			}
    		}
    			
    		name.setText(recipientHelper.getName(recipientCursor));
    		quantity.setText(String.format("gifts: %d / %d", giftQuantityPurchased, totalQuantity));
    		price.setText(String.format("cost: $%.2f / $%.2f", giftPricePurchased, totalPrice));
    	}
    }
    
}
