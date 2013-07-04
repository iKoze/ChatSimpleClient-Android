package de.floriware.android.chatsimple.tasks;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.widget.Toast;
import de.floriware.android.chatsimple.Manager;
import de.floriware.android.chatsimple.R;
import de.floriware.android.chatsimple.activities.ConnectActivity;
import de.floriware.chatsimple.client.simplified.SimplifiedChatsimpleClient;

public class ConnectTask extends AsyncTask<SimplifiedChatsimpleClient, Integer, Integer>
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
		dialog.setCancelable(true);
		dialog.setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                cancel(true);
            }
        });
		dialog.show();
	}
	
	@Override
	protected void onPostExecute(final Integer success)
	{
		if(dialog.isShowing())
		{
			dialog.dismiss();
		}
		if(success == 0)
		{
			Manager.getInstance().getWorker().e_loggedin(activity);
		}
		else if(success == 1)
		{
			Toast.makeText(activity, activity.getString(R.string.conn_toast_login_failed), Toast.LENGTH_SHORT).show();
		}
		else
		{
			Toast.makeText(activity, activity.getString(R.string.conn_toast_conn_failed), Toast.LENGTH_SHORT).show();
		}
	}
	
	@Override
	protected void onProgressUpdate(Integer... i)
	{
		dialog.setMessage(activity.getString(R.string.conn_dialog_logging_in));
	}
	
	@Override
	protected Integer doInBackground(SimplifiedChatsimpleClient... params) {
		SimplifiedChatsimpleClient client = params[0];
		if(client.connect())
		{
			publishProgress(1);
			if(client.login())
			{
				return 0;
			}
			else
			{
				return 1;
			}
		}
		return 2;
	}
}
