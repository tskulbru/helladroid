package info.unyttig.helladroid.activity;

import info.unyttig.helladroid.HellaDroid;
import info.unyttig.helladroid.R;
import info.unyttig.helladroid.hellanzb.HellaNZBController;
import info.unyttig.helladroid.nzbmatrix.NzbMatrixController;
import info.unyttig.helladroid.nzbmatrix.NzbMatrixReport;
import info.unyttig.helladroid.nzbmatrix.NzbMatrixSearchAdapter;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class NzbMatrixSearchActivity extends ListActivity {
	private final static SharedPreferences preferences = HellaDroid.preferences;
	private final static int MSG_NEW_STATUS_UPDATE = 1;
	private final int MSG_NOTIFY_USER_ERROR = 2;
	
	private ArrayList<NzbMatrixReport> searchRes;
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
		searchRes = searchNzbMatrix(searchString, categoryNr);

		this.setListAdapter(new NzbMatrixSearchAdapter(this, searchRes));
		this.registerForContextMenu(this.getListView());
	}
	
	/**
	 * Creates a new intent when the individual list item is clicked
	 * which represents more info about the selected newzbin id
	 */
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		NzbMatrixReport report = (NzbMatrixReport) this.getListAdapter().getItem(position);
		Intent detailedSearch = new Intent(this, NzbMatrixDetailedSearch.class);
		detailedSearch.putExtra("info.unyttig.helladroid.nzbmatrix.NzbMatrixReport", report);
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
	 * The context menu for the ListView items. Can manipulate the queue 
	 * items in several ways.
	 */
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		switch(item.getItemId()) {
		case R.id.searchItemDownloadNow:
			NzbMatrixReport report = (NzbMatrixReport) this.getListAdapter().getItem((int)info.id);
			HellaNZBController.enqueueUrl(messageHandler, NzbMatrixController.genDownloadString(report.getNzbId()));
			return true;
		} return false;
	}
	
	/**
	 * Search NzbMatrix with a gives string and category
	 * 
	 * @param searchStr
	 * @param catId
	 * @return
	 */
	private ArrayList<NzbMatrixReport> searchNzbMatrix(String searchStr, String catId) {
		return NzbMatrixController.searchNzbMatrix(messageHandler, searchStr, catId);
		
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

}
