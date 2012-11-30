package swetipi.presentpal;

import android.app.Dialog;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class RecipientListMain extends ListActivity 
{
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipient_list);
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
    		Dialog dialog = DialogFactory.createtDialog(this, DialogType.ADD_RECIPIENT);
    		dialog.show();
    	}
    	
    	return super.onOptionsItemSelected(item);
    }
}
