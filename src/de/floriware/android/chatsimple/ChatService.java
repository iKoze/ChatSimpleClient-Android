package de.floriware.android.chatsimple;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import de.floriware.chatsimple.client.simplified.ISimplifiedConnectionHandler;
import de.floriware.chatsimple.client.simplified.SimplifiedChatsimpleClient;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

public class ChatService extends Service implements ISimplifiedConnectionHandler
{
	public static ChatService CHATSERVICE;
	public SimplifiedChatsimpleClient client;
	
	@Override
	public void onCreate()
	{
		if(CHATSERVICE == null)
		{
			CHATSERVICE = this;
		}
	}

	@Override
	public void incomingOK() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void incomingChatMessage(String sender, String chatmessage) {
		//ca.tv_messages.append(timestamp()+" "+sender+": "+chatmessage+"\n");
		ChatActivity.CHATACTIVITY.appendText(timestamp()+" "+sender+": "+chatmessage+"\n");
	}

	@Override
	public void incomingNofification(String sender, String message) {
		ChatActivity.CHATACTIVITY.appendText(timestamp()+" NOTIFY "+sender+": "+message+"\n");
		CHATSERVICE.client.requestUserList();
		
	}

	@Override
	public void incomingError(String sender, String message) {
		ChatActivity.CHATACTIVITY.appendText(timestamp()+" ERROR "+sender+": "+message+"\n");
		Toast.makeText(this, sender + " Error: " + message, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void incomingUserList(String[] userlist) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void gotDisconnected() {
		Toast.makeText(this, "got disconnected!", Toast.LENGTH_LONG).show();
		
	}

	@Override
	public void handleException(Exception e) {
		e.printStackTrace();
		
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@SuppressLint("SimpleDateFormat")
	public String timestamp()
	{
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		return "[" + sdf.format(cal.getTime()) + "]";
		
	}

}
