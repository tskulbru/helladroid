package info.unyttig.helladroid.activity;

import info.unyttig.helladroid.HellaDroid;
import info.unyttig.helladroid.R;
import info.unyttig.helladroid.hellanzb.HellaNZBController;
import info.unyttig.helladroid.newzbin.NewzBinController;
import info.unyttig.helladroid.newzbin.NewzBinReport;
import info.unyttig.helladroid.newzbin.NewzBinSearchAdapter;
import info.unyttig.helladroid.nzbmatrix.NzbMatrixController;
import info.unyttig.helladroid.nzbmatrix.NzbMatrixReport;
import info.unyttig.helladroid.nzbmatrix.NzbMatrixSearchAdapter;

import java.util.ArrayList;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class NzbMatrixSearchActivity extends ListActivity {
	private final static SharedPreferences preferences = HellaDroid.preferences;
	private final static int MSG_NEW_STATUS_UPDATE = 1;
	private final int MSG_NOTIFY_USER_ERROR = 2;
	private final int SHOW_LOADING_DIALOG = 3;
	private final int SHOW_SEARCH_DIALOG = 4;
	
	private ProgressDialog dialog;
	private ArrayList<NzbMatrixReport> searchRes;
	private int searchItemsLeft = NzbMatrixController.totalRes;
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
		searchRes = searchNzbMatrix(searchString, categoryNr);
		searchItemsLeft = NzbMatrixController.totalRes;
		searchItemsLeft -= LIMIT;

		this.setListAdapter(new NzbMatrixSearchAdapter(this, searchRes));
		this.registerForContextMenu(this.getListView());
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
//			Log.i("asdasda", NzbMatrixController.genDownloadString(report.getNzbId()));
			HellaNZBController.enqueueUrl(messageHandler, NzbMatrixController.genDownloadString(report.getNzbId()));
//			HellaNZBController.enqueueUrl(messageHandler, ""+report.getNzbId());
			return true;
		} return false;
	}
	
	private ArrayList<NzbMatrixReport> searchNzbMatrix(String searchStr, String catId) {
		searchItemsLeft -= LIMIT;
		this.offset += LIMIT;
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
