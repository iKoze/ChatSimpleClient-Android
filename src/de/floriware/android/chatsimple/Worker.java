package de.floriware.android.chatsimple;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import de.floriware.android.chatsimple.activities.ChatActivity;
import de.floriware.android.chatsimple.activities.ConnectActivity;
import de.floriware.android.chatsimple.activities.IChatActivity;
import de.floriware.android.chatsimple.activities.IConnectActivity;
import de.floriware.android.chatsimple.tasks.ConnectTask;
import de.floriware.chatsimple.ServerInfo;
import de.floriware.chatsimple.client.simplified.ISimplifiedConnectionHandler;
import de.floriware.chatsimple.client.simplified.SimplifiedChatsimpleClient;
import de.floriware.utils.string.StringJoin;

public class Worker implements IChatActivity, IConnectActivity, ISimplifiedConnectionHandler
{
	private SimplifiedChatsimpleClient client = null;
	private String chat_history = "";
	
	public Worker()
	{
		client = new SimplifiedChatsimpleClient(this);
	}
	
	public String getChatHistory()
	{
		return chat_history;
	}
	
	public SimplifiedChatsimpleClient getClient()
	{
		return client;
	}
	
	private void chatUpdate(String message)
	{
		chat_history += message;
		ChatActivity a = Manager.getInstance().getChatActivity();
		if(a != null)
		{
			a.appendText(message);
			a.scrollDown();
		}
	}
	
	private void logout()
	{
		client.logout();
		client.disconnect();
	}
	
	@SuppressLint("SimpleDateFormat")
	public String timestamp()
	{
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		return "[" + sdf.format(cal.getTime()) + "]";
	}
	
	@Override
	public void gotDisconnected() {
		Manager.getInstance().removeChatActivity();
		Manager.getInstance().setConnected(false);
		client.disconnect();
	}

	@Override
	public void handleException(Exception e)
	{
		e.printStackTrace();
	}

	@Override
	public void incomingChatMessage(String sender, String chatmessage)
	{
		String line = timestamp()+" "+sender+": "+chatmessage+"\n";
		chatUpdate(line);
	}

	@Override
	public void incomingError(String sender, String message)
	{
		ConnectActivity a = Manager.getInstance().getConnectActivity();
		if(a != null)
		{
			a.showToast(message);
			return;
		}
		String line = timestamp()+" ERROR "+sender+": "+message+"\n";
		chatUpdate(line);
	}

	@Override
	public void incomingNofification(String sender, String message)
	{
		String line = timestamp()+" NOTIFY "+sender+": "+message+"\n";
		chatUpdate(line);
		client.requestUserList();
	}

	@Override
	public void incomingOK()
	{
		
	}

	@Override
	public void incomingUserList(String[] arg0)
	{
		String line = timestamp()+" Online Users: "+StringJoin.join(", ",arg0)+"\n";
		chatUpdate(line);
	}

	@Override
	public void incomingWhisperMessage(String sender, String[] receiver, String chatmessage)
	{
		String line = timestamp()+" "+sender+" -> "+StringJoin.join(", ", receiver)+": "+chatmessage+"\n";
		chatUpdate(line);
	}

	@Override
	public void e_connect(ConnectActivity a) {
		ServerInfo sinfo = new ServerInfo(a.et_srv_name.getText().toString(), a.et_usr_name.getText().toString(), a.et_srv_pass.getText().toString());
		String delim = a.et_proto_se.getText().toString();
		sinfo.delimiter = delim.equalsIgnoreCase("") ? "::" : delim;
		client.setServerInfo(sinfo);
		
		if(client.isConnected())
		{
			client.disconnect();
		}
		
		SharedPreferences settings = a.getPreferences(Context.MODE_PRIVATE);
    	SharedPreferences.Editor editor = settings.edit();
        editor.putString("et_srv_name", a.et_srv_name.getText().toString());
        editor.putString("et_usr_name", a.et_usr_name.getText().toString());
        editor.putString("et_srv_pass", a.et_srv_pass.getText().toString());
        editor.putString("et_proto_se", a.et_proto_se.getText().toString());
        editor.commit();
        
		new ConnectTask(a).execute(client);
	}

	@Override
	public void e_loggedin(ConnectActivity a) {
		Intent intent = new Intent(a, ChatActivity.class);
		a.startActivity(intent);
		Manager.getInstance().setConnected(true);
	}
	
	@Override
	public void e_send(ChatActivity a)
	{
		String s = a.et_message.getText().toString();
		if(s.startsWith("/"))
		{
			s = s.replaceFirst("/", "");
			if(s.startsWith("list"))
			{
				client.requestUserList();
			}
			else if(s.startsWith("tell "))
			{
				s = s.replaceFirst("tell ", "");
				String [] data = s.split(" ",2);
				try
				{
					String [] receiver = data[0].split(",");
					client.sendTellMessage(receiver, data[1]);
				}
				catch(ArrayIndexOutOfBoundsException e)
				{
					chatUpdate("Usage: /tell user1,user2 your message\n");
				}
			}
			else
			{
				chatUpdate("Unknown Command: "+s+"\n");
			}
		}
		else
		{
			client.sendChatMessage(s);
		}
		a.et_message.setText("");
	}
	
	@Override
	public void e_disconnect(ChatActivity activity)
	{
		logout();
		Manager.getInstance().removeChatActivity();
		Manager.getInstance().setConnected(false);
		Manager.getInstance().newConnectActivity(activity);
	}
	
	@Override
	public void e_exit(ChatActivity activity)
	{
		logout();
		Manager.getInstance().removeChatActivity();
		Manager.getInstance().setConnected(false);
		System.exit(0);
	}
	
	@Override
	public void e_exit(ConnectActivity activity)
	{
		System.exit(0);
	}
}
