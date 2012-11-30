package swetipi.presentpal;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.LayoutInflater;

public class DialogFactory
{
	public static Dialog createtDialog(Context context, DialogType type)
	{
		final AlertDialog.Builder builder = new AlertDialog.Builder(context);
		LayoutInflater inflater = ((Activity)context).getLayoutInflater();
		
		switch(type)
		{
			case ADD_RECIPIENT:
				builder.setView(inflater.inflate(R.layout.dialog_add_recipient, null));
				builder.setTitle("Add New Recipient");

				builder.setPositiveButton(R.string.add_recipient, new DialogInterface.OnClickListener()
				{	
					public void onClick(DialogInterface dialog, int which) 
					{
						// add name to database
					}
				});
						
				builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener()
				{	
					public void onClick(DialogInterface dialog, int which) 
					{
						dialog.cancel();
					}
				});
				
				return builder.create();
			default:
				return null;
		}
	}
	
}
