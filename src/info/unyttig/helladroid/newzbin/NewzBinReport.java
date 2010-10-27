package info.unyttig.helladroid.newzbin;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

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
public class NewzBinReport {
	private String nb32id;
	private int nzbId;
	private String title;
	private String category;
	private boolean passworded;
	private String url;
	private String status;
	private String progress;
	private int nfoId;
	private String poster;
	private long size;
	private HashMap<String, Long> dates;
	private ArrayList<String> newsgroups;
	private HashMap<String, ArrayList<String>> attributes;
	private ArrayList<NewzBinReportComment> comments;
	
	/* Getters and setters */
	public String getNb32id() {
		return nb32id;
	}
	public void setNb32id(String nb32id) {
		this.nb32id = nb32id;
	}
	public int getNzbId() {
		return nzbId;
	}
	public void setNzbId(int nzbId) {
		this.nzbId = nzbId;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public boolean isPassworded() {
		return passworded;
	}
	public void setPassworded(boolean passworded) {
		this.passworded = passworded;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getProgress() {
		return progress;
	}
	public void setProgress(String progress) {
		this.progress = progress;
	}
	public int getNfoId() {
		return nfoId;
	}
	public void setNfoId(int nfoId) {
		this.nfoId = nfoId;
	}
	public String getPoster() {
		return poster;
	}
	public void setPoster(String poster) {
		this.poster = poster;
	}
	public String getSize() {
		long temp = size/1024/1024;
		return temp+" MB";
	}
	public void setSize(long size) {
		this.size = size;
	}
	public HashMap<String, Long> getDates() {
		return dates;
	}
	public void setDates(HashMap<String, Long> dates) {
		this.dates = dates;
	}
	
	public String getNewsgroups() {
		String temp = null;
		for(String newsgroup : newsgroups) {
			if(temp == null)
				temp = newsgroup;
			else
				temp += String.format("%n%s", newsgroup);
		}
		return temp;
	}
	
	public void setNewsgroups(ArrayList<String> newsgroups) {
		this.newsgroups = newsgroups;
	}
	
	public String getAttributes() {
		String r = "";
		Iterator<String> itKeys = attributes.keySet().iterator(); 
		String k;
		ArrayList<String> v;
		while (itKeys.hasNext()) { 
			k = itKeys.next(); 
			v = attributes.get(k);
			r += String.format("%s%n", k);
			for(String attribute : v) {
				r += String.format("%5s%s%s%n", " ","- ",attribute);
			}
		} 
		return r;
	}
	
	public void setAttributes(HashMap<String, ArrayList<String>> attributes) {
		this.attributes = attributes;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getReported() {
		DateFormat df = DateFormat.getDateInstance(DateFormat.LONG);
		Date d = new Date();
		d.setTime(dates.get("reported")*1000);
		return df.format(new Date(dates.get("reported")*1000));
	}
	
	public String toString() {
		return "NewzBinDetailedReport [attributes=" + attributes
				+ ", category=" + category + ", dates=" + dates + ", nb32id="
				+ nb32id + ", newsgroups=" + newsgroups + ", nfoId=" + nfoId
				+ ", nzbId=" + nzbId + ", passworded=" + passworded
				+ ", poster=" + poster + ", progress=" + progress + ", size="
				+ size + ", status=" + status + ", title=" + title + ", url="
				+ url + "]";
	}
	public void setComments(ArrayList<NewzBinReportComment> comments) {
		this.comments = comments;
	}
	public ArrayList<NewzBinReportComment> getComments() {
		return comments;
	}
}
