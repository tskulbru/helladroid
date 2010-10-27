package info.unyttig.helladroid.activity;

import java.util.ArrayList;

import info.unyttig.helladroid.R;
import info.unyttig.helladroid.newzbin.NewzBinCommentAdapter;
import info.unyttig.helladroid.newzbin.NewzBinController;
import info.unyttig.helladroid.newzbin.NewzBinReportComment;
import android.app.ListActivity;
import android.os.Bundle;


public class NewzBinCommentsActivity extends ListActivity {
	
	private ArrayList<NewzBinReportComment> comments;
	private int reportId;
	
	/**
	 * Called when activity is first created
	 */
	@SuppressWarnings("unchecked")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);
		Bundle extras = getIntent().getExtras();
		reportId = extras.getInt("info.unyttig.helladroid.newzbin.NewzBinReport.nzbId");
		comments = NewzBinController.reports.get(reportId).getComments();
		this.setListAdapter(new NewzBinCommentAdapter(this, comments));
	}
}
