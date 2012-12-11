package swetipi.presental;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

public class ProductInformationTask extends AsyncTask<String, Void, ArrayList<String>>
{
	private static final String GOOGLE_SEARCH_API_URL = "https://www.googleapis.com/shopping/search/v1/public/products?country=US&key=%s&restrictBy=gtin:%s,condition:new";
	private static final String API_KEY = "AIzaSyC4jiDhCBYFhA9UrxvEu09owV8_QmSZLcw";
	
	private Context context;
	private ProgressBar progressBar;
	
	public ProductInformationTask(Context context)
	{
		this.context = context;
	}
	
	@Override
	protected void onPreExecute()
	{
		progressBar = new ProgressBar(context);
		progressBar.setIndeterminate(true);
		progressBar.setVisibility(View.VISIBLE);
		super.onPreExecute();
	}
	
	@Override
	protected ArrayList<String> doInBackground(String... productURP)
	{
		URL url;
		String result = null;
		
		try
		{
			url = new URL(String.format(GOOGLE_SEARCH_API_URL, API_KEY, productURP[0]));
			HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
			
			InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
			result = readStream(inputStream);
			Log.v("", result);
			return parseJsonResult(result);
		}
		catch (MalformedURLException e) 
		{
			e.printStackTrace();
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		catch (JSONException e) 
		{
			Log.v("", result);
			e.printStackTrace();
		}
		
		return null;
	}
	
	@Override
	protected void onPostExecute(ArrayList<String> result) 
	{
		progressBar.setVisibility(View.GONE);
		super.onPostExecute(result);
	}

	private String readStream(InputStream inputStream) throws IOException
	{
		BufferedReader reader = null;
		
		try
		{
			reader = new BufferedReader(new InputStreamReader(inputStream));
			
			StringBuilder builder = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null)
			{
				builder.append(line);
			}
			
			return builder.toString();
		}
		catch (IOException ioException)
		{
			return null;
		}
		finally
		{
			if (reader !=null)
			{
				reader.close();
			}
		}
	}
	
	private ArrayList<String> parseJsonResult(String jsonString) throws JSONException
	{
		ArrayList<String> productInformation = new ArrayList<String>();
		
		JSONObject json = new JSONObject(jsonString);
		JSONArray products = json.getJSONArray("items");
		
		JSONObject productJson;
		JSONArray productInventory;
		for (int i = 0; i < products.length(); i++)
		{
			productJson = products.getJSONObject(i).getJSONObject("product");
			productInformation.add(productJson.getString("title"));
			
			productInventory = productJson.getJSONArray("inventories");
			productInformation.add(productInventory.getJSONObject(0).getString("price"));
		}
		
		return productInformation;
	}
	
}
