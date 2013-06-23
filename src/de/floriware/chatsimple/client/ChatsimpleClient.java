package de.floriware.chatsimple.client;

import java.net.*;
import java.io.*;

import de.floriware.chatsimple.ServerInfo;
import de.floriware.chatsimple.databundles.DataBundle;

public class ChatsimpleClient
{
	protected ServerInfo server;
	protected IConnectionHandler handler;
	protected Socket socket;
	protected PrintWriter writer;
	protected BufferedReader reader;
	protected ConnectionListener listener;
	
	public ChatsimpleClient(ServerInfo server, IConnectionHandler handler)
	{
		this.server = server;
		this.handler = handler;
	}
	
	public boolean connect()
	{
		try
		{
			socket = new Socket(server.getHost(),server.getPort());
			writer = new PrintWriter(socket.getOutputStream(),false);
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		}
		catch (ConnectException e)
		{
			return false;
		}
		catch (Exception e)
		{
			handler.handleException(e);
			return false;
		}
		
		listener = new ConnectionListener(server,reader,handler);
		listener.start();
		
		return socket.isConnected();
	}
	
	public void disconnect()
	{
		if(writer != null)
		{
			writer.close();
		}
		try
		{
			if(reader != null)
			{
				reader.close();
			}
			if(socket != null)
			{
				socket.close();
			}
		}
		catch (Exception e)
		{
			handler.handleException(e);
		}
	}
	
	public boolean isConnected()
	{
		if(socket != null)
		{
			return socket.isConnected();
		}
		return false;
	}
	
	public void send(DataBundle data)
	{
		data.delimiter = server.delimiter;
		send(data.getData());
	}
	
	public void send(String data)
	{
		writer.println(data);
		writer.flush();
	}
	
	public ConnectionListener getListener()
	{
		return listener;
	}
}
