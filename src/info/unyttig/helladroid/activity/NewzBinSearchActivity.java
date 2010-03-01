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
import android.app.ListActivity;
import android.app.AlertDialog.Builder;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;

public class NewzBinSearchActivity extends ListActivity {
	private final static SharedPreferences preferences = HellaDroid.preferences;
	private final static int MSG_NEW_STATUS_UPDATE = 1;
	private final int MSG_NOTIFY_USER_ERROR = 2;

	private ArrayList<NewzBinReport> searchRes;

	/**
	 * Called when activity is first created
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);
		Bundle extras = getIntent().getExtras();
		HashMap<String, String> searchOptions = new HashMap<String, String>();
		searchOptions.put("q", extras.getString("searchString"));
		searchOptions.put("category", extras.getString("categoryNr"));
		searchRes = findReport(searchOptions);

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
//		DisplayToast(report.getNzbName());
//		Builder adb=new AlertDialog.Builder(NewzBinSearchActivity.this);
//		adb.setTitle("LVSelectedItemExample");
//		adb.setMessage("Selected Item is = mongo");
//		adb.setPositiveButton("Ok", null);
//		adb.show();
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
	 * Fetches result from the controller
	 * 
	 * @param searchOptions
	 * @return
	 */
	private ArrayList<NewzBinReport> findReport(HashMap<String, String> searchOptions) {
		searchOptions.put("limit", preferences.getString("newzbin_search_limit", "10"));
		searchOptions.put("retention", preferences.getString("newzbin_retention", "7"));
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
				DisplayToast((String)msg.obj);
				break;
			}
		}
	};
}
