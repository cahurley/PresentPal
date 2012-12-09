package model;

public class Holiday 
{
	private final String id;
	private String name;
	private long date;
	
	Holiday(String id)
	{
		this.id = id;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public long getDate()
	{
		return date;
	}
	
	public void setDate(long date)
	{
		this.date = date;
	}
}
