package info.unyttig.helladroid.newzbin;

import info.unyttig.helladroid.R;

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
public class NewzBinSearchAdapter extends ArrayAdapter<NewzBinReport> {
	private ArrayList<NewzBinReport> searchItems = new ArrayList<NewzBinReport>();
	Activity context;
	
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
		
		((TextView) row.findViewById(R.id.searchRowLabelNzbname)).setText(temp.getTitle());
		((TextView) row.findViewById(R.id.searchRowLabelNzbID)).setText("ID: " + temp.getNzbId());
		((TextView) row.findViewById(R.id.searchRowLabelNzbSize)).setText(temp.getSize());
		return (row);
	}
}
