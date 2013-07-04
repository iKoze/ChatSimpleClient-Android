package de.floriware.android.chatsimple;

import android.app.Activity;
import android.content.Intent;
import de.floriware.android.chatsimple.activities.ChatActivity;
import de.floriware.android.chatsimple.activities.ConnectActivity;
import de.floriware.android.chatsimple.services.ChatService;

public class Manager
{
	private static Manager instance = null;
	private ChatService chatservice = null;
	private ConnectActivity connectactivity = null;
	private ChatActivity chatactivity = null;
	private Worker worker = null;
	private boolean connected = false;
	
	private Manager()
	{
		worker = new Worker();
	}
	
	public static Manager getInstance()
	{
		if(instance == null)
		{
			instance = new Manager();
		}
		return instance;
	}
	
	public Worker getWorker()
	{
		return worker;
	}
	
	public void setConnected(boolean connected)
	{
		this.connected = connected; 
	}
	
	public boolean setChatService(ChatService service)
	{
		if(chatservice == null)
		{
			chatservice = service;
			return true;
		}
		return false;
	}
	
	public ChatService getChatService()
	{
		return chatservice;
	}
	
	public void setConnectActivity(ConnectActivity activity)
	{
		endActivity(connectactivity);
		if(connected)
		{
			Intent intent = new Intent(activity, ChatActivity.class);
			activity.startActivity(intent);
		}
		connectactivity = activity;
	}
	
	public ConnectActivity getConnectActivity()
	{
		return connectactivity;
	}
	
	public void setChatActivity(ChatActivity activity)
	{
		endActivity(chatactivity);
		endActivity(connectactivity);
		connectactivity = null;
		chatactivity = activity;
		chatactivity.appendText(worker.getChatHistory());
	}
	
	public ChatActivity getChatActivity()
	{
		return chatactivity;
	}
	
	public void removeChatActivity()
	{
		chatactivity.finish();
		chatactivity = null;
	}
	
	private void endActivity(Activity a)
	{
		if(a != null)
		{
			a.finish();
		}
	}
}
