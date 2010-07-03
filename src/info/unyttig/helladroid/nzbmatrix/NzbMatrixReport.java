package info.unyttig.helladroid.nzbmatrix;

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
public class NzbMatrixReport {
	private int nzbId;
	private String nzbName;
	private String link;
	private double size;
	private String index_date;
	private String usenet_date;
	private String category;
	private String group;
	private int comments;
	private int hits;
	private String nfo;
	private String weblink;
	private String language;
	private String image;
	private int region;
	private String downloadString;
	
	public int getNzbId() {
		return nzbId;
	}
	public void setNzbId(int nzbId) {
		this.nzbId = nzbId;
	}
	public String getNzbName() {
		return nzbName;
	}
	public void setNzbName(String nzbName) {
		this.nzbName = nzbName;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getSize() {
		double temp = size/1024/1024;
		int temp2 = (int) temp;
		return temp2 + " MB";
	}
	public void setSize(double size) {
		this.size = size;
	}
	public String getIndex_date() {
		return index_date;
	}
	public void setIndex_date(String indexDate) {
		index_date = indexDate;
	}
	public String getUsenet_date() {
		return usenet_date;
	}
	public void setUsenet_date(String usenetDate) {
		usenet_date = usenetDate;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public int getComments() {
		return comments;
	}
	public void setComments(int comments) {
		this.comments = comments;
	}
	public int getHits() {
		return hits;
	}
	public void setHits(int hits) {
		this.hits = hits;
	}
	public String isNfo() {
		return nfo;
	}
	public void setNfo(String nfo) {
		this.nfo = nfo;
	}
	public String getWeblink() {
		return weblink;
	}
	public void setWeblink(String weblink) {
		this.weblink = weblink;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public int getRegion() {
		return region;
	}
	public void setRegion(int region) {
		this.region = region;
	}
	
	public void setDownloadString(String downloadString) {
		this.downloadString = downloadString;
	}
	public String getDownloadString() {
		return downloadString;
	}
	
	public String toString() {
		return "NzbMatrixReport [category=" + category + ", comments="
				+ comments + ", group=" + group + ", hits=" + hits + ", image="
				+ image + ", index_date=" + index_date + ", language="
				+ language + ", link=" + link + ", nfo=" + nfo + ", nzbId="
				+ nzbId + ", nzbName=" + nzbName + ", region=" + region
				+ ", size=" + size + ", usenet_date=" + usenet_date
				+ ", weblink=" + weblink + ", downloadString=" + downloadString + "]";
	}

}
