package info.unyttig.helladroid.activity;

import info.unyttig.helladroid.R;
import info.unyttig.helladroid.hellanzb.HellaNZBController;
import info.unyttig.helladroid.nzbmatrix.NzbMatrixController;
import info.unyttig.helladroid.nzbmatrix.NzbMatrixReport;
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
public class NzbMatrixDetailedSearch extends Activity {
	private NzbMatrixReport nbdr;
	
	/**
	 * Called when activity is first created
	 */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.searchdetailedmatrix);
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
			HellaNZBController.enqueueUrl(messageHandler, NzbMatrixController.genDownloadString(nbdr.getNzbId()));
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
	 * Fill the view based on the object identified by its id,
	 * this displays a detailed view of a report id.
	 */
	private void fillView() {
		Bundle extras = getIntent().getExtras();
		nbdr = (NzbMatrixReport) extras.getSerializable("info.unyttig.helladroid.nzbmatrix.NzbMatrixReport");
		Log.i("asdasd", nbdr.toString());
		
		// Start filling
		TextView title = (TextView) findViewById(R.id.detailedTitle);
		title.setText(nbdr.getNzbName());
		
		TextView status = (TextView) findViewById(R.id.detailedCategory);
		status.setText(nbdr.getCategory());
		
		TextView progress = (TextView) findViewById(R.id.detailedLanguage);
		progress.setText(nbdr.getLanguage());
		
		TextView url = (TextView) findViewById(R.id.detailedUrl);
		url.setText(nbdr.getWeblink());
		
		TextView reported = (TextView) findViewById(R.id.detailedReported);
		reported.setText(nbdr.getIndex_date());
		
		TextView size = (TextView) findViewById(R.id.detailedSize);
		size.setText(nbdr.getSize());
		
		TextView newsgroups = (TextView) findViewById(R.id.detailedNewsgroups);
		newsgroups.setText(nbdr.getGroup());
	}
}