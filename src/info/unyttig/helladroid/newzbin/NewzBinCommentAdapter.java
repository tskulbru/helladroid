package info.unyttig.helladroid.newzbin;

import java.util.ArrayList;

import info.unyttig.helladroid.R;
import android.app.Activity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class NewzBinCommentAdapter extends ArrayAdapter<NewzBinReportComment> {
	private ArrayList<NewzBinReportComment> comments = new ArrayList<NewzBinReportComment>();
	Activity context;
	
	public NewzBinCommentAdapter(Activity context, ArrayList<NewzBinReportComment> comments) {
		super(context, R.layout.commentitem, comments);
		
		this.context = context;
		this.comments = comments;
	}
	
	/**
	 * Populate the ListView with the queue elements returned from the controller 
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = context.getLayoutInflater();
		View row = inflater.inflate(R.layout.commentitem, null);
		NewzBinReportComment temp = comments.get(position);
		
		((TextView) row.findViewById(R.id.commentRowLabelAuthor)).setText(temp.getAuthor());
		((TextView) row.findViewById(R.id.commentRowLabelDate)).setText(temp.getDate());
		((TextView) row.findViewById(R.id.commentRowLabelBody)).setText(Html.fromHtml(temp.getBody()));
		return (row);
	}
}
