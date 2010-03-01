package info.unyttig.helladroid.newzbin;

import info.unyttig.helladroid.R;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class NewzBinSearchAdapter extends ArrayAdapter<NewzBinReport> {
	Activity context;
//	private ArrayList<String> searchItems = new ArrayList<String>();
  private ArrayList<NewzBinReport> searchItems = new ArrayList<NewzBinReport>();
	public NewzBinSearchAdapter(Activity context, ArrayList<NewzBinReport> searchItems)
	{
		super(context, R.layout.searchnzbitem, searchItems);

		this.context = context;
		this.searchItems = searchItems;
	}
	
	/**
	 * Populate the ListView with the queue elements returned from the controller 
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = context.getLayoutInflater();
		View row = inflater.inflate(R.layout.searchnzbitem, null);
		NewzBinReport temp = searchItems.get(position);
		long size = temp.getNzbSize()/1024/1024;
		String nzbSizeMB = size + " MB";
		
		((TextView) row.findViewById(R.id.searchRowLabelNzbname)).setText(temp.getNzbName());
		((TextView) row.findViewById(R.id.searchRowLabelNzbID)).setText("ID: " + temp.getNzbId());
		((TextView) row.findViewById(R.id.searchRowLabelNzbSize)).setText(nzbSizeMB);
		
//		String[] values = searchItems.get(position).split("	");
//		long size = Long.parseLong(values[1])/1024/1024;
//		String nzbSizeMB = size + " MB";
//		
//		((TextView) row.findViewById(R.id.searchRowLabelNzbname)).setText(values[2]);
//		((TextView) row.findViewById(R.id.searchRowLabelNzbID)).setText("ID: " + values[0]);
//		((TextView) row.findViewById(R.id.searchRowLabelNzbSize)).setText(nzbSizeMB);
		return (row);
	}

}
