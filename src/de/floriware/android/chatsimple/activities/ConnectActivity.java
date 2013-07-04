package de.floriware.android.chatsimple.activities;

import de.floriware.android.chatsimple.R;
import de.floriware.android.chatsimple.Manager;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class ConnectActivity extends Activity
{
	public EditText et_srv_name, et_usr_name, et_srv_pass, et_proto_se;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_connect);
		
		et_srv_name = (EditText) findViewById(R.id.et_srv_name);
		et_usr_name = (EditText) findViewById(R.id.et_usr_name);
		et_srv_pass = (EditText) findViewById(R.id.et_srv_pass);
		et_proto_se = (EditText) findViewById(R.id.et_proto_se);
		
		SharedPreferences settings = getPreferences(Context.MODE_PRIVATE);
        et_srv_name.setText(settings.getString("et_srv_name", ""));
        et_usr_name.setText(settings.getString("et_usr_name", ""));
        et_srv_pass.setText(settings.getString("et_proto_se", ""));
        et_proto_se.setText(settings.getString("et_proto_se", ""));
        
        Manager.getInstance().setConnectActivity(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void connect(View v)
	{
		Manager.getInstance().getWorker().e_connect(this);
	}
}
