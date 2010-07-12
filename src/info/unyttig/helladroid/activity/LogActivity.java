package info.unyttig.helladroid.activity;

import java.util.ArrayList;
import java.util.HashMap;

import info.unyttig.helladroid.R;
import info.unyttig.helladroid.hellanzb.HellaNZBController;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This file is a part of HellaDroid
 * 
 * HellaDroid - http://code.google.com/p/helladroid
 * "A remote HellaNZB query client."
 * 
 * Copyright (C) 2010 Torstein S. Skulbru <serrghi>
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * @author Torstein S. Skulbru <serrghi>
 * @see <a href="http://code.google.com/p/helladroid 
 */
public class LogActivity extends Activity {

	/** 
	 * Called when the activity is first created. 
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.log);
		HellaNZBController.showInfoLog(messageHandler);
	}
	
	/**
	 * Create the options menu, using log_menu.xml as layout
	 */
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.log_menu, menu);
		return true;
	}
	
	/**
	 * Defining what happens when a options menu item is selected.
	 * Currently just refreshes the log.
	 */
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case R.id.logrefresh:
			DisplayRToast("Refreshing log");
			HellaNZBController.showInfoLog(messageHandler);
			return true;
		} return false;
	}
	
	/**
	 * Updates the Log's view with data from HellaNZB
	 * 
	 * @param logRes
	 */
	private void updateLog(HashMap<String, Object> logRes) {
		((TextView) findViewById(R.id.hellanzbversion)).setText("HellaNZB v" + logRes.get("version").toString());
		((TextView) findViewById(R.id.hellanzbuptime)).setText(logRes.get("uptime").toString());
		((TextView) findViewById(R.id.hellanzbhostname)).setText(logRes.get("hostname").toString());
		
		@SuppressWarnings("unchecked")
		ArrayList<String> errorEntries = (ArrayList<String>) logRes.get("errorEntries");
		String errorString = "";
		for(String errorEntry : errorEntries)
			errorString = errorString + String.format("%s%n-------%n", errorEntry);
		((TextView) findViewById(R.id.hellanzberrorlog)).setText(errorString);
		
		@SuppressWarnings("unchecked")
		ArrayList<String> infoEntries = (ArrayList<String>) logRes.get("infoEntries");
		String infoString = "";
		for(String infoEntry : infoEntries)
			infoString = infoString + String.format("%s%n-------%n", infoEntry);
		((TextView) findViewById(R.id.hellanzbinfolog)).setText(infoString);
	}
	
	/**
	 * Communicates messages to the user
	 * 
	 * @param msg The message to be presented, from R.string
	 */
	private void DisplayRToast(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * The message handler, controls what message should be displayed
	 * based on the id brought back from the controller.
	 */
	private Handler messageHandler = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			switch(msg.what) {
			case HellaNZBController.MSG_SHOW_LOG:
				HashMap<String, Object> logRes = (HashMap<String, Object>) msg.obj;
				if(logRes.containsKey("exception"))
					DisplayRToast(logRes.get("exception").toString());
				else
					updateLog(logRes);
				break;
			}
		}
	};
}
