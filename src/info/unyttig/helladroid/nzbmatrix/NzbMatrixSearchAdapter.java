package info.unyttig.helladroid.nzbmatrix;

import info.unyttig.helladroid.R;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ArrayAdapter;

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
 * @link http://code.google.com/p/helladroid
 */
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
