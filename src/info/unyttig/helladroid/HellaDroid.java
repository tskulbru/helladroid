package info.unyttig.helladroid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import com.google.android.apps.analytics.GoogleAnalyticsTracker;

import info.unyttig.helladroid.R;
import info.unyttig.helladroid.activity.NewzBinSearchActivity;
import info.unyttig.helladroid.activity.NzbMatrixSearchActivity;
import info.unyttig.helladroid.activity.SettingsActivity;
import info.unyttig.helladroid.hellanzb.HellaNZBController;
import info.unyttig.helladroid.nzbmatrix.NzbMatrixController;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
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
import android.view.View.OnCreateContextMenuListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
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
public class HellaDroid extends Activity {
	private final int EMPTY_SETTINGS = 0;
	private final int ADD_NEWZBIN_ID = 2;
	private final int ABOUT_DIALOG = 3;
	private final int SEARCH_DIALOG = 4;
	private final int CANCEL_ITEM = 99;

	// TODO: Let users choose the time intervall.
	private final int REFRESH_INTERVALL = 10000;

	public static SharedPreferences preferences;
	private static ArrayList<String> queueRows = new ArrayList<String>();
	public static boolean paused = false;
	private final Handler handler = new Handler();
	public static HashMap<String,String> searchCatnHd = new HashMap<String,String>();

	private Timer t;
	private ListView listview;

	GoogleAnalyticsTracker tracker;

	/** 
	 * Called when the activity is first created. 
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
//		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		// Tracking info
		tracker = GoogleAnalyticsTracker.getInstance();
		tracker.start("UA-8731983-3", 20, this);
		tracker.trackPageView("/hellaDroidHome");

		preferences = getSharedPreferences("HellaDroid", 0);
		paused = !preferences.getBoolean("server_active", false);

		listview = (ListView) findViewById(R.id.queueNzbList);
		listview.setAdapter(new QueueNzbListRowAdapter(this, queueRows));
		listview.setOnCreateContextMenuListener(this);

		// Fill the newzbin categories (O(1) so no worries)
		searchCatnHd.put("Everything", "-1");
		searchCatnHd.put("Anime", "11");
		searchCatnHd.put("Apps", "1");
		searchCatnHd.put("Books", "13");
		searchCatnHd.put("Consoles", "2");
		searchCatnHd.put("Discussions", "15");
		searchCatnHd.put("Emulation", "10");
		searchCatnHd.put("Games", "4");
		searchCatnHd.put("Misc", "5");
		searchCatnHd.put("Movies", "6");
		searchCatnHd.put("Music", "7");
		searchCatnHd.put("PDA", "12");
		searchCatnHd.put("Resources", "14");
		searchCatnHd.put("TV", "8");
		
		// Quality
		searchCatnHd.put("Any", "");
		searchCatnHd.put("XviD", "attr:VideoF~xvid ");
		searchCatnHd.put("HD", "attr:VideoF~x264 ");
		searchCatnHd.put("720p", "attr:VideoF~720p ");
		searchCatnHd.put("1080p", "attr:VideoF~1080p ");
		
		checkForLife();
		autoQueueRefresh();
	}


	/**
	 * When activity pauses, also pause the autoQueueRefresh
	 */
	protected void onPause() {
		super.onPause();
		paused = true;
	}

	/**
	 * When activity is resumed, resume the refresh.
	 */
	protected void onResume() {
		super.onResume();
		paused = false;
	}

	/**
	 * Clean up on exit
	 */
	protected void onDestroy() {
		super.onDestroy();
		tracker.stop();
//		Uncomment next line to kill when orientation changes
//		this.finish();
	}

	/**
	 * Create the options menu, using options_menu.xml as layout
	 */
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.options_menu, menu);
		return true;
	}

	/**
	 * Create the context menu for the queuelist items, using queue_menu.xml as layout
	 */
	public void onCreateContextMenu(ContextMenu menu, View view,
			ContextMenuInfo menuInfo) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.queue_menu, menu);
		super.onCreateContextMenu(menu, view, menuInfo);
	}

	/**
	 * Defining what happens when a options menu item is selected.
	 * Also calls checkForLife() method for methods which try to 
	 * contact the server.
	 */
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case R.id.menuAddNzbID:
			if(checkForLife())
				showDialog(ADD_NEWZBIN_ID);
			return true;
		case R.id.menuPause:
			if(checkForLife())
				HellaNZBController.pauseResumeQuery(messageHandler);
			return true;
		case R.id.menuPreferences:
			showSettings();
			return true;
		case R.id.menuRefreshQueue:
			if(checkForLife())
				manualQueueRefresh();
			return true;
		case R.id.menuSearch:
				showDialog(SEARCH_DIALOG);
			return true;
		case R.id.menuAbout:
			showDialog(ABOUT_DIALOG);
			return true;
		} return false;
	}

	/**
	 * The context menu for the ListView items. Can manipulate the queue 
	 * items in several ways.
	 */
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		switch(item.getItemId()) {
		case CANCEL_ITEM:
			HellaNZBController.qCurrCancel(messageHandler);
			return true;
		}

		String nzbItem = getNzbItemAdapter().getItem((int)info.id);
		String[] temp = nzbItem.split("#");
		String nzbId = temp[2];

		switch(item.getItemId()) {
		case R.id.qItemDownloadNow:
			HellaNZBController.qItemDownloadNowOrLast(messageHandler, nzbId, true);
			return true;
		case R.id.qItemDownloadLast:
			HellaNZBController.qItemDownloadNowOrLast(messageHandler, nzbId, false);
			return true;
		case R.id.qItemMoveUp:
			HellaNZBController.qItemMoveUpOrDown(messageHandler, nzbId, true);
			return true;
		case R.id.qItemMoveDown:
			HellaNZBController.qItemMoveUpOrDown(messageHandler, nzbId, false);
			return true;
		case R.id.qItemDequeue:
			HellaNZBController.qItemDequeue(messageHandler, nzbId);
			return true;
		case CANCEL_ITEM:
			showDialog(ABOUT_DIALOG);
			return true;
		} return false;
	}

	/**
	 * The method which contains all the dialogs for quiting, adding etc.
	 */
	protected Dialog onCreateDialog(int id) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		AlertDialog alert;
		final EditText input = new EditText(this);

		switch(id) {
		case ADD_NEWZBIN_ID:
			tracker.trackPageView("/hellaAddNewzId");
			builder.setTitle("Add NZB from Newzbin")
			.setMessage("Enter the Newzbin ID you wish to add to queue:")
			.setView(input)
			.setPositiveButton("Add", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					HellaNZBController.enqueueNewzBinID(messageHandler, input.getText().toString());
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

		case EMPTY_SETTINGS:
			builder.setTitle("Configuration needed")
			.setMessage("HellaDroid isn't configured properly, do it now?")
			.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					showSettings();
					manualQueueRefresh();
				}
			})
			.setNegativeButton("No",
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					return;
				}
			});
			alert = builder.create();
			return alert;
		case ABOUT_DIALOG:
			tracker.trackPageView("/hellaAbout");
			builder.setTitle("About");
			View about = getLayoutInflater().inflate(R.layout.about, null);
			((TextView)about.findViewById(R.id.helladroid)).setText("HellaDroid v" + getCurrentVersion(this));
			builder.setView(about);
			alert = builder.create();
			return alert;
		case SEARCH_DIALOG:
			tracker.trackPageView("/hellaSearch");
			final View searchDialog = getLayoutInflater().inflate(R.layout.searchdialog, null);
//			builder.setTitle("Search NewzBin")
//			builder.setMessage("Please enter your search")
			builder.setView(searchDialog)
			.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					try {
						EditText et = (EditText) searchDialog.findViewById(R.id.searchString);
						Spinner s = (Spinner) searchDialog.findViewById(R.id.searchCatgegorySpinner);
						Spinner s2 = (Spinner) searchDialog.findViewById(R.id.searchQualitySpinner);
						Spinner s3 = (Spinner) searchDialog.findViewById(R.id.searchProvSpinner);
						
						if(et.getText().length() <= 0) {
							DisplayRToast(R.string.msg_empty_search_string);
							return;
						}
						if(s3.getSelectedItem().toString().equals("NewzBin")) {
							String ss = searchCatnHd.get(s2.getSelectedItem().toString()) + et.getText().toString();
							showSearchActivity(ss, searchCatnHd.get(s.getSelectedItem().toString()), "NewzBin");
						} else {
							String temp = "" + et.getText();
							temp = temp.replaceAll("^[ \t]+|[ \t]+$", "");
							if(temp.length() <= 0) {
								DisplayRToast(R.string.msg_empty_search_string);
								return;
							} else 
								showSearchActivity(et.getText().toString(), "0", "NzbMatrix");
						}
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
	 * Refreshes the queue and the current download.
	 */
	private void manualQueueRefresh() {
		if(checkForLife()) {
			HellaNZBController.listQueue(messageHandler);
			tracker.dispatch();
		}
	}

	/**
	 * The automatic queue-refresher. This 
	 * method will run for as long as is defined in REFRESH_INTERVAL.
	 * TODO let the user choose this time period himself.
	 */
	private void autoQueueRefresh() {
		t = new Timer();
		t.schedule(new TimerTask() {
			public void run() {
				handler.post(new Runnable() {
					public void run() {
						if(!paused && HellaNZBController.isAlive)
							manualQueueRefresh();
					}
				});
			}
		}, 0, REFRESH_INTERVALL);
	}

	/**
	 * This method starts the settings activity
	 */
	private void showSettings() {
		startActivity(new Intent(this, SettingsActivity.class));
	}
	
	/**
	 * This method starts the search activity
	 * 
	 * @param searchString
	 * @param categoryNr
	 */
	private void showSearchActivity(String searchString, String categoryNr, String provider) {
		if(provider.equals("NewzBin")) {
			Intent searchIntent = new Intent(this, NewzBinSearchActivity.class);
			searchIntent.putExtra("searchString", searchString);
			searchIntent.putExtra("categoryNr", categoryNr);
			startActivity(searchIntent);
		} else {
			Intent searchIntent = new Intent(this, NzbMatrixSearchActivity.class);
			searchIntent.putExtra("searchString", searchString);
			searchIntent.putExtra("categoryNr", categoryNr);
			startActivity(searchIntent);
		}
	}

	/**
	 * Communicates messages to the user
	 * 
	 * @param msg The message to be presented, from R.string
	 */
	private void DisplayRToast(Integer msg) {
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
			case HellaNZBController.MSG_NEW_STATUS_UPDATE:
				DisplayRToast((Integer)msg.obj);
				break;
			case HellaNZBController.MSG_QUEUE_UPDATE: // Update queue
				Object[] result = (Object[]) msg.obj;
				queueRows.clear();
				queueRows.addAll((ArrayList<String>)result[1]);
				ListView listView = (ListView) findViewById(R.id.queueNzbList);
				ArrayAdapter<String> adapter = (ArrayAdapter<String>) listView.getAdapter();
				adapter.notifyDataSetChanged();
				break;
			case HellaNZBController.MSG_CURR_DOWN_UPDATE:
				updateCurrentDownload(msg.obj.toString());
				break;
			}
		}
	};

	/**
	 * Updates the labels of the current download.
	 * 
	 * @param curr
	 */
	private void updateCurrentDownload(String curr) {
		String[] values = curr.split("#");
		try
		{
			RelativeLayout rl = (RelativeLayout) findViewById(R.id.currDownLayout);

			if(values[0].compareTo("Not downloading anything") != 0) {
				rl.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {

					@Override
					public void onCreateContextMenu(ContextMenu menu, View v,
							ContextMenuInfo menuInfo) {
						// TODO Auto-generated method stub

						menu.add(0, CANCEL_ITEM, 0, "Cancel download");
					}

				}
				);
			} else {
				rl.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {

					@Override
					public void onCreateContextMenu(ContextMenu menu, View v,
							ContextMenuInfo menuInfo) {
					}

				}
				);
			}
			if(HellaNZBController.isPaused)
				((TextView) findViewById(R.id.currStatus)).setText("Paused");
			else
				((TextView) findViewById(R.id.currStatus)).setText("");
			((TextView) findViewById(R.id.currNzbName)).setText(values[0]);
			((ProgressBar) findViewById(R.id.currPercBar)).setProgress(Integer.parseInt(values[1]));
			((TextView) findViewById(R.id.currNzbETA)).setText(values[2]);
			((TextView) findViewById(R.id.currNzbMb)).setText(values[3] + " / " + values[4] + " MB");
			((TextView) findViewById(R.id.currNzbSpeed)).setText(values[5] + " KB/s ");
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Get current version number.
	 * 
	 * @param context
	 * @return
	 */
	public static String getCurrentVersion(Context context) {
		String version = "?";
		try {
			PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			version = pi.versionName;
		} catch(PackageManager.NameNotFoundException e) {
			// make use of log later
		}
		return version;
	}
	/**
	 * This method checks if there is a live server setting.
	 * TODO make som REAL use of this method.
	 * @return
	 */
	private boolean checkForLife() {
		if(!preferences.getBoolean("server_active", false)){
			showDialog(EMPTY_SETTINGS);
			return false;
		}
		return true;
	}

	/**
	 * Fetches the ListView adapter.
	 * 
	 * @return
	 */
	private QueueNzbListRowAdapter getNzbItemAdapter() {
		return (QueueNzbListRowAdapter) listview.getAdapter();
	}
}
