package info.unyttig.helladroid.activity;

import java.util.ArrayList;
import java.util.HashMap;

import info.unyttig.helladroid.HellaDroid;
import info.unyttig.helladroid.R;
import info.unyttig.helladroid.hellanzb.HellaNZBController;
import info.unyttig.helladroid.newzbin.NewzBinController;
import info.unyttig.helladroid.newzbin.NewzBinReport;
import info.unyttig.helladroid.newzbin.NewzBinSearchAdapter;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

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
public class NewzBinSearchActivity extends ListActivity {
	private final static SharedPreferences preferences = HellaDroid.preferences;
	private final static int MSG_NEW_STATUS_UPDATE = 1;
	private final int MSG_NOTIFY_USER_ERROR = 2;
	private final int SHOW_LOADING_DIALOG = 3;
	private final int SHOW_SEARCH_DIALOG = 4;
	
	private ProgressDialog dialog;
	private ArrayList<NewzBinReport> searchRes;
	private int searchItemsLeft = NewzBinController.totalRes;
	private final int LIMIT = Integer.parseInt(preferences.getString("newzbin_search_limit", ""));
	private int offset = 0;
	private String searchString;
	private String categoryNr;

	/**
	 * Called when activity is first created
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.search);
		Bundle extras = getIntent().getExtras();
		searchString = extras.getString("searchString");
		categoryNr = extras.getString("categoryNr");
		searchRes = findReport(searchString, categoryNr);
		searchItemsLeft = NewzBinController.totalRes;
		searchItemsLeft -= LIMIT;

		this.setListAdapter(new NewzBinSearchAdapter(this, searchRes));
		this.registerForContextMenu(this.getListView());
	}

	/**
	 * Creates a new intent when the individual list item is clicked
	 * which represents more info about the selected newzbin id
	 */
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		NewzBinReport report = (NewzBinReport) this.getListAdapter().getItem(position);
		Intent detailedSearch = new Intent(this, NewzBinDetailedSearch.class);
		detailedSearch.putExtra("nzbId", report.getNzbId());
		startActivity(detailedSearch);
	}
	
	/**
	 * Create the context menu for the queuelist items, using queue_menu.xml as layout
	 */
	public void onCreateContextMenu(ContextMenu menu, View view,
			ContextMenuInfo menuInfo) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.search_context_menu, menu);
		super.onCreateContextMenu(menu, view, menuInfo);
	}

	/**
	 * Create the options menu, using search_menu.xml as layout
	 */
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.search_menu, menu);
		return true;
	}
	
	/**
	 * Define if the next button should be called, 
	 * is called before optionsmenu is created.
	 */
	public boolean onPrepareOptionsMenu(Menu menu) {
		MenuItem item = menu.findItem(R.id.menuSearchNext);
		if(searchItemsLeft > 0)
			item.setVisible(true);
		else
			item.setVisible(false);
		return super.onPrepareOptionsMenu(menu);
	}
	
	/**
	 * The context menu for the ListView items. Can manipulate the queue 
	 * items in several ways.
	 */
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		switch(item.getItemId()) {
		case R.id.searchItemDownloadNow:
			NewzBinReport report = (NewzBinReport) this.getListAdapter().getItem((int)info.id);
			HellaNZBController.enqueueNewzBinID(messageHandler, ""+report.getNzbId());
			return true;
		} return false;
	}
	
	/**
	 * Defining what happens when a options menu item is selected.
	 */
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case R.id.menuSearch:
			showDialog(SHOW_SEARCH_DIALOG);
			return true;
		case R.id.menuSearchNext:
			Log.i("HellaSearch:", "total:"+NewzBinController.totalRes+" limit:"+LIMIT);
			Log.i("asdasd", ""+this.searchItemsLeft);
			showDialog(SHOW_LOADING_DIALOG);
			updateSearchRes(findReport(this.searchString, this.categoryNr));
			return true;
		} return false;
	}
	
	/**
	 * Methods contains different types of dialogs
	 */
	protected Dialog onCreateDialog(int id) {
		switch(id) {
		case SHOW_LOADING_DIALOG:
			dialog = new ProgressDialog(this);
			dialog.setMessage("Searching..");
			dialog.setIndeterminate(true);
			dialog.setCancelable(false);
			return dialog;
		case SHOW_SEARCH_DIALOG:
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			AlertDialog alert;
			final View searchDialog = getLayoutInflater().inflate(R.layout.searchdialog, null);
			builder.setView(searchDialog)
			.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					try {
						EditText et = (EditText) searchDialog.findViewById(R.id.searchString);
						Spinner s = (Spinner) searchDialog.findViewById(R.id.searchCatgegorySpinner);
						Spinner s2 = (Spinner) searchDialog.findViewById(R.id.searchQualitySpinner);
						if(et.getText().length() <= 0) {
							DisplayRToast(R.string.msg_empty_search_string);
							return;
						}
						String ss = HellaDroid.searchCatnHd.get(s2.getSelectedItem().toString()) + et.getText().toString();
						showDialog(SHOW_LOADING_DIALOG);
						updateSearchRes(findReport(ss, HellaDroid.searchCatnHd.get(s.getSelectedItem().toString())));
						Log.i("SearchString: ", et.getText().toString());
						Log.i("SearchCategory: ", s.getSelectedItem().toString());
					} catch(Exception e) {
						Log.e("SearchException: ", ""+e);
					}
					return;
				}
			})
			.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					return;
				}
			});
			alert = builder.create();
			return alert;
		}
		return null;
	}

	/**
	 * Fetches result from the controller
	 * 
	 * @param searchOptions
	 * @return
	 */
	private ArrayList<NewzBinReport> findReport(String searchString, String categoryNr) {
		HashMap<String, String> searchOptions = new HashMap<String, String>();
		searchOptions.put("q", searchString);
		searchOptions.put("category", categoryNr);
		searchOptions.put("offset", ""+this.offset);
		searchItemsLeft -= LIMIT;
		this.offset += LIMIT;
		return NewzBinController.findReport(messageHandler, searchOptions);
	}

	/**
	 * Communicates messages to the user
	 * 
	 * @param msg The message to be presented
	 */
	private void DisplayToast(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
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
			case MSG_NEW_STATUS_UPDATE:
				DisplayRToast((Integer)msg.obj);
				break;
			case MSG_NOTIFY_USER_ERROR:
				DisplayToast((String)msg.obj.toString());
				break;
			}
		}
	};
	
	/**
	 * Updates the search results listview.
	 * 
	 * @param searchRes
	 */
	private void updateSearchRes(ArrayList<NewzBinReport> searchRes) {
		this.searchRes.clear();
		this.searchRes.addAll(searchRes);
		NewzBinSearchAdapter ad =  (NewzBinSearchAdapter) this.getListAdapter();
		ad.notifyDataSetChanged();
		removeDialog(SHOW_LOADING_DIALOG);
	}
}
