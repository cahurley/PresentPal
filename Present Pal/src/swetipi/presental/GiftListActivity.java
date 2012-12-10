package swetipi.presental;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import modelhelpers.GiftHelper;
import ZXingAssets.IntentIntegrator;
import ZXingAssets.IntentResult;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class GiftListActivity extends ListActivity  
{
	private Cursor giftCursor = null;
	private GiftListAdapter adapter = null;
	private GiftHelper giftHelper = null;
	static ArrayList<String> productInformation = null;
	
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift_list);
        
        giftHelper = new GiftHelper(this);
        giftCursor = giftHelper.getByRecipientId(getIntent().getStringExtra(RecipientListActivity.RECIPIENT_ID_EXTRA));
        
        ListView list = getListView();
        adapter = new GiftListAdapter(giftCursor);
        list.setAdapter(adapter);
        
        list.setOnItemLongClickListener(new OnItemLongClickListener()
        {
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) 
			{	
				giftCursor.moveToPosition(position);
				
				AlertDialog.Builder builder = new AlertDialog.Builder(GiftListActivity.this);
				builder.setCancelable(true);
				builder.setMessage(String.format("Are you sure you want to delete %s from your list?", giftHelper.getName(giftCursor)));
				
				builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() 
				{
					@SuppressWarnings("deprecation")
					public void onClick(DialogInterface dialog, int which) 
					{
						giftHelper.removeById(giftHelper.getId(giftCursor));
						giftCursor.requery();
						
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
    }
	
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		
		giftHelper.close();
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        getMenuInflater().inflate(R.menu.activity_gift_list, menu);
        
        return super.onCreateOptionsMenu(menu);
    }
    
	@Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
    	int itemId = item.getItemId();
    	if (itemId == R.id.menu_add_gift_manually)
    	{
    		Dialog dialog = DialogFactory.createtDialog(this, giftCursor, giftHelper, DialogType.ADD_GIFT_MANUALLY);
    		dialog.show();
    	}
    	else if (itemId == R.id.menu_add_gift_barcode)
    	{
    		// This code was retrieved from adamzwakk.com/create-a-basic-android-barcode-scanner
    		IntentIntegrator.initiateScan(this);
    	}
    	
    	return super.onOptionsItemSelected(item);
    }
	
	// This code was retrieved from adamzwakk.com/create-a-basic-android-barcode-scanner
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent)
	{
		ArrayList<String> productInformation;
		switch (requestCode)
		{
			case IntentIntegrator.REQUEST_CODE:
				if (resultCode != RESULT_CANCELED)
				{
					IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
					if (scanResult != null)
					{
						String upc = scanResult.getContents();
						ProductInformationTask task = (new ProductInformationTask(this));
						task.execute(upc);
						
						try 
						{
							productInformation = task.get();
							Log.v("", String.format("name %s, price %.2f", productInformation.get(0), Double.parseDouble(productInformation.get(1))));
						}
						catch (InterruptedException e) 
						{
							// nothing
						}
						catch (ExecutionException e) 
						{
							// nothing
						}
					}
				}
				break;
		}
	}
	
    class GiftListAdapter extends CursorAdapter
    {
    	public GiftListAdapter(Cursor cursor) 
    	{
			super(GiftListActivity.this, cursor);
		}
    	
    	@Override
    	public void bindView(View row, Context context, Cursor cursor)
    	{
    		GiftHolder holder = (GiftHolder)row.getTag();
    		
    		holder.populateFrom(cursor, giftHelper);
    	}
    	
    	@Override
    	public View newView(Context context, Cursor cursor, ViewGroup parent)
    	{
    		LayoutInflater inflater = getLayoutInflater();
    		View row = inflater.inflate(R.layout.list_row, parent, false);
    		GiftHolder holder = new GiftHolder(row);
    		
    		row.setTag(holder);
    		
    		return row;
    	}
    }
    
    static class GiftHolder
    {
    	private TextView name = null;
    	private TextView quantity = null;
    	private TextView price = null;
    	
    	public GiftHolder(View row) 
    	{
			name = (TextView)row.findViewById(R.id.textview_name);
			quantity = (TextView)row.findViewById(R.id.textview_gift_quantity);
			price = (TextView)row.findViewById(R.id.textview_gift_price);
		}
    	
    	void populateFrom(Cursor cursor, GiftHelper helper)
    	{
    		double giftPrice = helper.getPrice(cursor);
    		int quantityPurchased = helper.getQuantityPurchased(cursor);
    		int totalQuantity = helper.getTotalQuantity(cursor);
    		
    		name.setText(helper.getName(cursor));
    		quantity.setText(String.format("quantity: %d / %d", quantityPurchased, totalQuantity));
    		price.setText(String.format("price: %.2f / %.2f", giftPrice * quantityPurchased, giftPrice * totalQuantity));
    	}
    }
	
}
