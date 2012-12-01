package swetipi.presentpal;

import modelhelpers.RecipientHelper;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class RecipientListMain extends ListActivity 
{
	private Cursor cursor = null;
	private RecipientListAdapter adapter = null;
	private RecipientHelper helper = null;
	
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipient_list);
        
        helper = new RecipientHelper(this);
        cursor = helper.getAll("name");
        
        ListView list = getListView();
        adapter = new RecipientListAdapter(cursor);
        list.setAdapter(adapter);
    }
	
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		
		helper.close();
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        getMenuInflater().inflate(R.menu.activity_recipient_list, menu);
        
        return super.onCreateOptionsMenu(menu);
    }
    
    @SuppressWarnings("deprecation")
	@Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
    	int itemId = item.getItemId();
    	if (itemId == R.id.menu_add_recipient)
    	{
    		Dialog dialog = DialogFactory.createtDialog(this, cursor, helper, DialogType.ADD_RECIPIENT);
    		dialog.show();
    	}
    	
    	return super.onOptionsItemSelected(item);
    }
    
    class RecipientListAdapter extends CursorAdapter
    {
    	public RecipientListAdapter(Cursor cursor) 
    	{
			super(RecipientListMain.this, cursor);
		}
    	
    	@Override
    	public void bindView(View row, Context context, Cursor cursor)
    	{
    		RecipientHolder holder = (RecipientHolder)row.getTag();
    		
    		holder.populateFrom(cursor, helper);
    	}
    	
    	@Override
    	public View newView(Context context, Cursor cursor, ViewGroup parent)
    	{
    		LayoutInflater inflater = getLayoutInflater();
    		View row = inflater.inflate(R.layout.recipient_row, parent, false);
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
			name = (TextView)row.findViewById(R.id.textview_recipient_name);
			quantity = (TextView)row.findViewById(R.id.textview_gift_quantity);
			price = (TextView)row.findViewById(R.id.textview_gift_price);
		}
    	
    	void populateFrom(Cursor cursor, RecipientHelper helper)
    	{
    		name.setText(helper.getName(cursor));
    		quantity.setText(String.format("quantity: "));
    		price.setText(String.format("cost: "));
    	}
    }
    
}
