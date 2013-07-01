package de.floriware.android.chatsimple;

import de.floriware.chatsimple.client.simplified.SimplifiedChatsimpleClient;
import android.os.AsyncTask;

public class ConnectTask extends AsyncTask<SimplifiedChatsimpleClient, Integer, Boolean>
{
	@Override
	protected Boolean doInBackground(SimplifiedChatsimpleClient... params) {
		return params[0].connect();
	}
}
