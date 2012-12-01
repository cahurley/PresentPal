package swetipi.presentpal;

import modelhelpers.RecipientHelper;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class DialogFactory
{
	// TODO: make an interface so I can pass in a generic Helper
	public static Dialog createtDialog(Context context, final Cursor cursor, final RecipientHelper helper, DialogType type)
	{
		final Dialog dialog = new Dialog(context);
		dialog.setCancelable(true);
		LayoutInflater inflater = ((Activity)context).getLayoutInflater();
		
		switch(type)
		{
			case ADD_RECIPIENT:
				dialog.setContentView(inflater.inflate(R.layout.dialog_add_recipient, null));
				dialog.setTitle("Add New Recipient");
				
				Button positiveButton = (Button)dialog.findViewById(R.id.button_add_recipient);
				positiveButton.setOnClickListener(new OnClickListener()
				{
					public void onClick(View v) 
					{
						String recipientName = ((EditText)dialog.findViewById(R.id.edittext_recipient_name)).getText().toString();
						helper.insert(recipientName);
						
						cursor.requery();
						
						dialog.dismiss();
					}
				});
						
				Button negativeButton = (Button)dialog.findViewById(R.id.button_cancel_add_recipient);
				negativeButton.setOnClickListener(new OnClickListener()
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
