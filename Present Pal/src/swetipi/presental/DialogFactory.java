package swetipi.presental;

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
import android.widget.Button;
import android.widget.EditText;

public class DialogFactory
{
	private static Button cancelButton;
	
	public static Dialog createtDialog(final Context context, final Cursor cursor, final DatabaseHelper helper, DialogType type)
	{
		final Dialog dialog = new Dialog(context);
		dialog.setCancelable(true);
		LayoutInflater inflater = ((Activity)context).getLayoutInflater();
		
		switch(type)
		{
			case ADD_RECIPIENT:				
				dialog.setContentView(inflater.inflate(R.layout.dialog_add_recipient, null));
				dialog.setTitle("Add New Recipient");
				
				Button addRecipientButton = (Button)dialog.findViewById(R.id.button_add_recipient);
				addRecipientButton.setOnClickListener(new OnClickListener()
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
				
				Button addGiftButton = (Button)dialog.findViewById(R.id.button_add_gift);
				addGiftButton.setOnClickListener(new OnClickListener()
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
			default:
				return null;
		}
	}
	
}
