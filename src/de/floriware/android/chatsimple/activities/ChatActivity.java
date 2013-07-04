package de.floriware.android.chatsimple.activities;

import de.floriware.android.chatsimple.R;
import de.floriware.android.chatsimple.Manager;
import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ChatActivity extends Activity {

	public TextView tv_messages;
	public EditText et_message;
	public Button btn_send;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);
		
		tv_messages = (TextView) findViewById(R.id.tv_messages);
		tv_messages.setMovementMethod(new ScrollingMovementMethod());
		et_message = (EditText) findViewById(R.id.et_message);
		btn_send = (Button) findViewById(R.id.btn_send);
		
		Manager.getInstance().setChatActivity(this);
		
        et_message.setOnKeyListener(new OnKeyListener(){
        	public boolean onKey(View v, int keyCode, KeyEvent event) {
        		if (event.getAction() == KeyEvent.ACTION_DOWN)
        		{
        			System.out.println("KK: "+keyCode + "");
        			if(keyCode == KeyEvent.KEYCODE_ENTER)
        			{
        				Manager.getInstance().getWorker().e_send(Manager.getInstance().getChatActivity());
        				return true;
        			}
        		}
        		return false;
        	}
        });
        
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.chat, menu);
		return true;
	}
	
	public void e_send(View v)
	{
		Manager.getInstance().getWorker().e_send(this);
	}
	
	public void appendText(final String text)
	{
		runOnUiThread(new Runnable() {
		     public void run() {

		    	 tv_messages.append(text);

		    }
		});
	}
	
	@Override
	public void onPause()
	{
		Manager.getInstance().removeChatActivity();
		super.onPause();
	}

}
