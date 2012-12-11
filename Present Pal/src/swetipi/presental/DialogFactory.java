package swetipi.presental;

import java.util.ArrayList;

import modelhelpers.DatabaseHelper;
import modelhelpers.GiftHelper;
import modelhelpers.RecipientHelper;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class DialogFactory
{
	private static ArrayList<String> productTitles;
	private static ArrayList<String> productPrices;
	
	public static Dialog createtDialog(final Context context, final Cursor cursor, final DatabaseHelper helper, DialogType type)
	{
		Button submitButton;
		Button cancelButton;
		
		final Dialog dialog = new Dialog(context);
		dialog.setCancelable(true);
		LayoutInflater inflater = ((Activity)context).getLayoutInflater();
		
		switch(type)
		{
			case ADD_RECIPIENT:				
				dialog.setContentView(inflater.inflate(R.layout.dialog_add_recipient, null));
				dialog.setTitle("Add New Recipient");
				
				submitButton = (Button)dialog.findViewById(R.id.button_add_recipient);
				submitButton.setOnClickListener(new OnClickListener()
				{
					public void onClick(View v) 
					{
						String recipientName = ((EditText)dialog.findViewById(R.id.edittext_recipient_name)).getText().toString();
						((RecipientHelper)helper).insert(recipientName);
						
						cursor.requery();
						
						dialog.dismiss();
					}
				});
						
				cancelButton = (Button)dialog.findViewById(R.id.button_cancel_add_recipient);
				cancelButton.setOnClickListener(new OnClickListener()
				{	
					public void onClick(View v) 
					{
						dialog.cancel();
					}
				});

				return dialog;
			case ADD_GIFT_MANUALLY:
				dialog.setContentView(inflater.inflate(R.layout.dialog_add_gift_manual, null));
				dialog.setTitle("Add New Gift");
				
				submitButton = (Button)dialog.findViewById(R.id.button_add_gift);
				submitButton.setOnClickListener(new OnClickListener()
				{	
					@Override
					public void onClick(View v) 
					{
						String giftName = ((EditText)dialog.findViewById(R.id.edittext_gift_name)).getText().toString();
						double giftPrice = Double.parseDouble(((EditText)dialog.findViewById(R.id.edittext_gift_price)).getText().toString());
						int giftQuantity = Integer.parseInt(((EditText)dialog.findViewById(R.id.edittext_gift_quantity)).getText().toString());
						
						String recipientId = ((Activity)context).getIntent().getStringExtra(RecipientListActivity.RECIPIENT_ID_EXTRA);
						((GiftHelper)helper).insert(giftName, giftPrice, giftQuantity, recipientId);
						
						cursor.requery();
						
						dialog.dismiss();
					}
				});
				
				cancelButton = (Button)dialog.findViewById(R.id.button_cancel_add_gift);
				cancelButton.setOnClickListener(new OnClickListener()
				{	
					public void onClick(View v) 
					{
						dialog.cancel();
					}
				});
				
				return dialog;
			case ADD_GIFT_BARCODE:
				dialog.setContentView(inflater.inflate(R.layout.dialog_add_gift_barcode, null));
				dialog.setTitle("Add New Gift");
				
				((TextView)dialog.findViewById(R.id.textview_gift_name)).setText(productTitles.get(0));
				
				final Spinner spinner = (Spinner)dialog.findViewById(R.id.spinner_product_prices);
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, productPrices);
				adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				spinner.setAdapter(adapter);
				
				spinner.setOnItemSelectedListener(new OnItemSelectedListener()
				{
					@Override
					public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
					{
						((TextView)dialog.findViewById(R.id.textview_gift_name)).setText(productTitles.get(position));
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {}
					
				});
				
				submitButton = (Button)dialog.findViewById(R.id.button_add_gift);
				submitButton.setOnClickListener(new OnClickListener()
				{	
					@Override
					public void onClick(View v) 
					{
						String giftName = ((TextView)dialog.findViewById(R.id.textview_gift_name)).getText().toString();
						double giftPrice = Double.parseDouble(productPrices.get(spinner.getSelectedItemPosition()));
						int giftQuantity = Integer.parseInt(((EditText)dialog.findViewById(R.id.edittext_gift_quantity)).getText().toString());
						
						String recipientId = ((Activity)context).getIntent().getStringExtra(RecipientListActivity.RECIPIENT_ID_EXTRA);
						((GiftHelper)helper).insert(giftName, giftPrice, giftQuantity, recipientId);
						
						cursor.requery();
						
						dialog.dismiss();
					}
				});
				
				cancelButton = (Button)dialog.findViewById(R.id.button_cancel_add_gift);
				cancelButton.setOnClickListener(new OnClickListener()
				{	
					public void onClick(View v) 
					{
						dialog.cancel();
					}
				});
				
				return dialog;
			default:
				return null;
		}
	}
	
	public static void setProductInformation(ArrayList<String> productInformation)
	{
		productTitles = new ArrayList<String>();
		productPrices = new ArrayList<String>();
		
		for (int i = 0; i < productInformation.size(); i+=2)
		{
			productTitles.add(productInformation.get(i));
			productPrices.add(productInformation.get(i + 1));
		}
	}
	
}
