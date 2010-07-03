package info.unyttig.helladroid.nzbmatrix;

import info.unyttig.helladroid.R;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ArrayAdapter;

public class NzbMatrixSearchAdapter extends ArrayAdapter<NzbMatrixReport> {
	private ArrayList<NzbMatrixReport> searchItems = new ArrayList<NzbMatrixReport>();
	Activity context;
	
	public NzbMatrixSearchAdapter (Activity context, ArrayList<NzbMatrixReport> searchItems)
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
		NzbMatrixReport temp = searchItems.get(position);
		
		((TextView) row.findViewById(R.id.searchRowLabelNzbname)).setText(temp.getNzbName());
		((TextView) row.findViewById(R.id.searchRowLabelNzbID)).setText("ID: " + temp.getNzbId());
		((TextView) row.findViewById(R.id.searchRowLabelNzbSize)).setText(temp.getSize());
		return (row);
	}

}
