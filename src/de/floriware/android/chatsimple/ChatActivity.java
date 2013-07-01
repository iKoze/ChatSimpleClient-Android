package de.floriware.android.chatsimple;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ChatActivity extends Activity {

	public static ChatActivity CHATACTIVITY;
	public TextView tv_messages;
	public EditText et_message;
	public Button btn_send;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);
		
		CHATACTIVITY = this;
		
		tv_messages = (TextView) findViewById(R.id.tv_messages);
		tv_messages.setMovementMethod(new ScrollingMovementMethod());
		et_message = (EditText) findViewById(R.id.et_message);
		btn_send = (Button) findViewById(R.id.btn_send);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.chat, menu);
		return true;
	}
	
	public void sendMessage(View v)
	{
		ChatService.CHATSERVICE.client.sendChatMessage(et_message.getText().toString());
		et_message.setText("");
	}
	
	public void appendText(final String text)
	{
		runOnUiThread(new Runnable() {
		     public void run() {

		    	 tv_messages.append(text);

		    }
		});
	}

}
