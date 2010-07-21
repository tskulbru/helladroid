package info.unyttig.helladroid.activity;

import info.unyttig.helladroid.R;
import info.unyttig.helladroid.hellanzb.HellaNZBController;
import info.unyttig.helladroid.newzbin.NewzBinController;
import info.unyttig.helladroid.newzbin.NewzBinReport;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
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
 * @link href="http://code.google.com/p/helladroid 
 */
public class NewzBinDetailedSearch extends Activity {
	private NewzBinReport nbdr;
	
	/**
	 * Called when activity is first created
	 */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.searchdetailed);
		fillView();
	}
	
	/**
	 * Create the options menu, using search_menu.xml as layout
	 */
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.search_menu3, menu);
		return true;
	}
	
	/**
	 * Defining what happens when a options menu item is selected.
	 */
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case R.id.detailedAddDownload:
			HellaNZBController.enqueueNewzBinID(messageHandler, ""+nbdr.getNzbId());
			return true;
		} return false;
	}
	
	/**
	 * Communicates messages to the user
	 * 
	 * @param msg The message to be presented, from R.string
	 */
	private void DisplayRToast(int msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * The message handler
	 */
	private Handler messageHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch(msg.what) {
			case HellaNZBController.MSG_NEW_STATUS_UPDATE:
				DisplayRToast((Integer)msg.obj);
				break;
			}
		}
	};
	
	/**
	 * Fill the view based on the results from the newzbin api call,
	 * this displays a detailed view of a report id.
	 */
	private void fillView() {
		Bundle extras = getIntent().getExtras();
		nbdr = NewzBinController.getReportInfo(extras.getInt("info.unyttig.helladroid.newzbin.NewzBinReport.nzbId"));
		Log.i("SearchDetailed: ", nbdr.toString());
		
		// Start filling
		TextView title = (TextView) findViewById(R.id.detailedTitle);
		title.setText(nbdr.getTitle());
		
		TextView status = (TextView) findViewById(R.id.detailedStatus);
		status.setText(nbdr.getStatus());
		
		TextView progress = (TextView) findViewById(R.id.detailedProgress);
		progress.setText(nbdr.getProgress());
		
		TextView url = (TextView) findViewById(R.id.detailedUrl);
		url.setText(nbdr.getUrl());
		
		TextView poster = (TextView) findViewById(R.id.detailedPoster);
		poster.setText(nbdr.getPoster());
		
		TextView reported = (TextView) findViewById(R.id.detailedReported);
		reported.setText(nbdr.getReported());
		
		TextView size = (TextView) findViewById(R.id.detailedSize);
		size.setText(nbdr.getSize());
		
		TextView newsgroups = (TextView) findViewById(R.id.detailedNewsgroups);
		newsgroups.setText(nbdr.getNewsgroups());
		
		TextView attributes = (TextView) findViewById(R.id.detailedAttributes);
		attributes.setText(nbdr.getAttributes());
	}
}
