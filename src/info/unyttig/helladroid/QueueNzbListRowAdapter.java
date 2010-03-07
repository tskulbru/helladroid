package info.unyttig.helladroid;


import java.util.ArrayList;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

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
public class QueueNzbListRowAdapter extends ArrayAdapter<String>
{
	Activity context;
	private ArrayList<String> nzbItems = new ArrayList<String>();

	public QueueNzbListRowAdapter(Activity context, ArrayList<String> nzbItems)
	{
		super(context, R.layout.queuenzbitem, nzbItems);

		this.context = context;
		this.nzbItems = nzbItems;
	}
	
	/**
	 * Populate the ListView with the queue elements returned from the controller 
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = context.getLayoutInflater();
		View row = inflater.inflate(R.layout.queuenzbitem, null);

		String[] values = nzbItems.get(position).split("#");
		String nzbSizeMB;
		int mb = Integer.parseInt(values[1]);
		if(mb == 0)
			nzbSizeMB = "unknown";
		else
			nzbSizeMB = values[1] + " MB";
		
		((TextView) row.findViewById(R.id.queueRowLabelNzbname)).setText(values[0]);
		((TextView) row.findViewById(R.id.queueRowLabelNzbCompleted)).setText(nzbSizeMB);
		((TextView) row.findViewById(R.id.queueRowLabelNzbID)).setText(values[2]);
		return (row);
	}
}
