package de.floriware.android.chatsimple.activities;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import de.floriware.android.chatsimple.Manager;
import de.floriware.android.chatsimple.R;

public class ChatActivity extends Activity
{
	public TextView tv_messages;
	public EditText et_message;
	public Button btn_send;
	public ScrollView sv_textscroller;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);
		
		tv_messages = (TextView) findViewById(R.id.tv_messages);
		tv_messages.setMovementMethod(new ScrollingMovementMethod());
		et_message = (EditText) findViewById(R.id.et_message);
		btn_send = (Button) findViewById(R.id.btn_send);
		sv_textscroller = (ScrollView) findViewById(R.id.sv_textscroller);
		
		Manager.getInstance().setChatActivity(this);
		
        et_message.setOnEditorActionListener(new OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                	Manager.getInstance().getWorker().e_send(Manager.getInstance().getChatActivity());
                    return true;
                }
                return false;
            }
        });
        
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_chat, menu);
		return true;
	}
	
	public void e_send(View v)
	{
		Manager.getInstance().getWorker().e_send(this);
	}
	
	public void e_disconnect(MenuItem i)
	{
		Manager.getInstance().getWorker().e_disconnect(this);
	}
	
	public void e_exit(MenuItem i)
	{
		Manager.getInstance().getWorker().e_exit(this);
	}
	
	public void appendText(final String text)
	{
		runOnUiThread(new Runnable() {
		     public void run() {

		    	 tv_messages.append(text);

		    }
		});
	}
	
	public void scrollDown()
	{
		runOnUiThread(new Runnable() {
			public void run()
			{
				//if(tv_messages.getMeasuredHeight() <= sv_textscroller.getScrollY() + sv_textscroller.getHeight())
				//{
					sv_textscroller.fullScroll(View.FOCUS_DOWN);
				//}
			}
		});
	}
	
	@Override
	public void onPause()
	{
		Manager.getInstance().setChatVisible(false);
		super.onPause();
	}
	
	@Override
	public void onResume()
	{
		Manager.getInstance().setChatVisible(true);
		super.onResume();
	}
	
	@Override
	public void onDestroy()
	{
		Manager.getInstance().removeChatActivity();
		super.onDestroy();
	}

}
