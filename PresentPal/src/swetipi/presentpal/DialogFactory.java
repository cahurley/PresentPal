package swetipi.presentpal;

import modelhelpers.RecipientHelper;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class DialogFactory
{
	public static Dialog createtDialog(final Context context, DialogType type)
	{
		final Dialog dialog = new Dialog(context);
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
						RecipientHelper helper = new RecipientHelper(context);
						
						String recipientName = ((EditText)dialog.findViewById(R.id.edittext_recipient_name)).getText().toString();
						helper.insert(recipientName);
						
						dialog.dismiss();
						helper.close();
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
