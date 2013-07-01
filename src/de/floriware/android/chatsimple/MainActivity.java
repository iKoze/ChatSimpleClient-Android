package de.floriware.android.chatsimple;

import de.floriware.chatsimple.ServerInfo;
import de.floriware.chatsimple.client.simplified.SimplifiedChatsimpleClient;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	private EditText et_srv_name, et_usr_name, et_srv_pass, et_proto_se;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		et_srv_name = (EditText) findViewById(R.id.et_srv_name);
		et_usr_name = (EditText) findViewById(R.id.et_usr_name);
		et_srv_pass = (EditText) findViewById(R.id.et_srv_pass);
		et_proto_se = (EditText) findViewById(R.id.et_proto_se);
		
		if(ChatService.CHATSERVICE == null)
		{
			Intent intent = new Intent(this, ChatService.class);
			startService(intent);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void connect(View v)
	{
		ServerInfo sinfo = new ServerInfo(et_srv_name.getText().toString(), et_usr_name.getText().toString(), et_srv_pass.getText().toString());
		String delim = et_proto_se.getText().toString();
		sinfo.delimiter = delim.equalsIgnoreCase("") ? "::" : delim;
		ChatService.CHATSERVICE.client = new SimplifiedChatsimpleClient(sinfo, ChatService.CHATSERVICE);
		SimplifiedChatsimpleClient client = ChatService.CHATSERVICE.client;
		new ConnectTask().execute(client);
		int i = 0;
		while(!client.isConnected() && i < 100)
		{
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			i++;
		}
		if(client.isConnected())
		{
			if(client.login())
			{
				Intent intent = new Intent(this, ChatActivity.class);
				startActivity(intent);
			}
			else
			{
				Toast.makeText(this, "Unable to Login", Toast.LENGTH_SHORT).show();
			}
		}
		else
		{
			Toast.makeText(this, "Unable to Connect", Toast.LENGTH_SHORT).show();
		}
	}
}
