package info.unyttig.helladroid.activity;

import info.unyttig.helladroid.HellaDroid;
import info.unyttig.helladroid.R;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;

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
public class SettingsActivity extends PreferenceActivity {
	
	/**
	 * Called when activity is started
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getPreferenceManager().setSharedPreferencesName("HellaDroid");
		addPreferencesFromResource(R.layout.preferences);
	}
	
	/**
	 * When the settings activity is close/destroyed, it updates HellaDroid's 
	 * field variable; paused, according to if the server was marked as active.
	 */
	protected void onDestroy() {
		SharedPreferences pref = this.getSharedPreferences("HellaDroid", 0);
		HellaDroid.paused = !pref.getBoolean("server_active", false);
		super.onDestroy();
	}
}
