package de.floriware.android.chatsimple.tasks;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;
import de.floriware.android.chatsimple.Manager;
import de.floriware.android.chatsimple.R;
import de.floriware.android.chatsimple.activities.ConnectActivity;
import de.floriware.chatsimple.client.simplified.SimplifiedChatsimpleClient;

public class ConnectTask extends AsyncTask<SimplifiedChatsimpleClient, Integer, Boolean>
{
	private ConnectActivity activity;
	private ProgressDialog dialog;
	
	public ConnectTask(ConnectActivity activity)
	{
		this.activity = activity;
		dialog = new ProgressDialog(activity);
	}
	
	@Override
	protected void onPreExecute()
	{
		dialog.setMessage(activity.getString(R.string.conn_dialog_connect));
		dialog.show();
	}
	
	@Override
	protected void onPostExecute(final Boolean success)
	{
		if(dialog.isShowing())
		{
			dialog.dismiss();
		}
		if(success)
		{
			Manager.getInstance().getWorker().e_connected(activity);
		}
		else
		{
			Toast.makeText(activity, activity.getString(R.string.conn_toast_conn_failed), Toast.LENGTH_SHORT).show();
		}
	}
	
	@Override
	protected Boolean doInBackground(SimplifiedChatsimpleClient... params) {
		return params[0].connect();
	}
}
