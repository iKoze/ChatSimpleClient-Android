package de.floriware.chatsimple.databundles;

import de.floriware.utils.StringJoin;

public class Clients extends DataBundle
{
	public String client_delimiter;
	protected String [] clients;
	
	protected void init()
	{
		type = Type.CLIENTS;
		client_delimiter = ",";
	}
	
	public Clients()
	{
		super();
	}
	
	public Clients(String data)
	{
		super(data);
	}
	
	public Clients(String [] clients)
	{
		init();
		setClients(clients);
	}
	
	public String getData()
	{
		return type.toString() + delimiter + StringJoin.join(client_delimiter, clients);
	}
	
	public void setClients(String [] clients)
	{
		this.clients = clients;
	}
	
	public void setClients(String delimiter, String clients)
	{
		this.clients = clients.split(delimiter);
	}
	
	public String [] getClients()
	{
		return clients;
	}
	
	public String getClients(String delimiter)
	{
		return StringJoin.join(delimiter, clients);
	}
	
	public boolean parse(String data)
	{
		try
		{
			clients = data.split(delimiter, 2)[1].split(client_delimiter);
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			return false;
		}
		return true;
	}
}
