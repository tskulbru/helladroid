package info.unyttig.helladroid.newzbin;

import java.util.ArrayList;
import java.util.HashMap;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

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
@SuppressWarnings("unused")
public class NewzBinDRHandler extends DefaultHandler {
	private boolean in_reportinfo = false;
	private boolean in_id = false;
	private boolean in_title = false;
	private boolean in_category = false;
	private boolean in_flags = false;
	private boolean in_url = false;
	private boolean in_status = false;
	private boolean in_progress = false;
	private boolean in_nfo = false;
	private boolean in_poster = false;
	private boolean in_size = false;
	private boolean in_dates = false;
	private boolean in_dates_reported = false;
	private boolean in_dates_first_file = false;
	private boolean in_dates_last_file = false;
	private boolean in_newsgroups = false;
	private boolean in_newsgroups_newsgroup = false;
	private boolean in_attributes = false;
	private boolean in_attributes_attribute = false;
	private boolean in_attributes_attribute_value = false;
	private boolean in_tags = false;
	private boolean in_tags_tag = false;
	private boolean in_comments = false;
	private boolean in_comments_comment = false;
	private boolean in_comments_comment_body = false;
	private boolean in_files = false;
	private boolean in_files_file = false;

	private HashMap<String, Long> dates = new HashMap<String, Long>();
	private HashMap<String, ArrayList<String>> attributes = new HashMap<String, ArrayList<String>>();
	private ArrayList<String> newsgroups = new ArrayList<String>();
	private String tempStr;
	private String tempAttr;

	NewzBinReport nbdr;

	/**
	 * Returns the NewzBinReported when its finished parsed.
	 * @return
	 */
	public NewzBinReport getParsedData() {
		return this.nbdr;
	}
	
	/**
	 * Starts the parsing
	 */
	public void startDocument() throws SAXException {
		if(nbdr == null)
			this.nbdr = new NewzBinReport();
	}

	/**
	 * Ends the parsing
	 */
	public void endDocument() throws SAXException {
		// do nothing
	}

	/**
	 * Keeps track of where we are in the XML, and defines opening tags.
	 */
	public void startElement(String namespaceURI, String localName, 
			String qName, Attributes atts) throws SAXException {
		if(localName.equals("reportinfo")) {
			this.in_reportinfo = true;
		} else if(localName.equals("id")) {
			this.in_id = true;
			this.nbdr.setNb32id(atts.getValue("nb32"));
		} else if(localName.equals("title")) {
			this.in_title = true;
		} else if(localName.equals("category")) {
			this.in_category = true;
		} else if(localName.equals("flags")) {
			this.in_flags = true;
		} else if(localName.equals("flag")) {
			int attr = Integer.parseInt(atts.getValue("bool"));
			this.nbdr.setPassworded(attr == 1);
		} else if(localName.equals("url")) {
			this.in_url = true;
		} else if(localName.equals("status")) {
			this.in_status = true;
		} else if(localName.equals("progress")) {
			this.in_progress = true;
		} else if(localName.equals("nfo")) {
			this.in_nfo = true;
		} else if(localName.equals("poster")) {
			this.in_poster = true;
		} else if(localName.equals("size")) {
			this.in_size = true;
		} else if(localName.equals("dates")) {
			this.in_dates = true;
		} else if(localName.equals("reported")) {
			this.in_dates_reported = true;
		} else if(localName.equals("first_file")) {
			this.in_dates_first_file = true;
		} else if(localName.equals("last_file")) {
			this.in_dates_last_file = true;
		} else if(localName.equals("newsgroups")) {
			this.in_newsgroups = true;
		} else if(localName.equals("newsgroup")) {
			this.in_newsgroups_newsgroup = true;
		} else if(localName.equals("attributes")) {
			this.in_attributes = true;
		} else if(localName.equals("attribute")) {
			this.in_attributes_attribute = true;
			this.tempAttr = atts.getValue("type");
		} else if(localName.equals("value")) {
			this.in_attributes_attribute_value = true;
		} else if(localName.equals("tags")) {
			this.in_tags =  true;
		} else if(localName.equals("tag")) {
			// nothing
		} else if(localName.equals("comments")) {
			this.in_comments = true;
		} else if(localName.equals("comment")) {
			this.in_comments_comment = true;
		} else if(localName.equals("body")) {
			this.in_comments_comment_body = true;
		} else if(localName.equals("files")) {
			this.in_files = true;
		}
	}

	/**
	 * Keeps track of where we are in the XML, and defines closing tags
	 */
	public void endElement(String namespaceURI, String localName, 
			String qName) throws SAXException { 
		if(localName.equals("reportinfo")) {
			this.in_reportinfo = false;
		} else if(localName.equals("id")) {
			this.in_id = false;
		} else if(localName.equals("title")) {
			this.in_title = false;
		} else if(localName.equals("category")) {
			this.in_category = false;
		} else if(localName.equals("flags")) {
			this.in_flags = false;
		} else if(localName.equals("flag")) {
			// nothing
		} else if(localName.equals("url")) {
			this.in_url = false;
		} else if(localName.equals("status")) {
			this.in_status = false;
		} else if(localName.equals("progress")) {
			this.in_progress = false;
		} else if(localName.equals("nfo")) {
			this.in_nfo = false;
		} else if(localName.equals("poster")) {
			this.in_poster = false;
		} else if(localName.equals("size")) {
			this.in_size = false;
		} else if(localName.equals("dates")) {
			this.in_dates = false;
			this.nbdr.setDates(dates);
		} else if(localName.equals("reported")) {
			this.in_dates_reported = false;
		} else if(localName.equals("first_file")) {
			this.in_dates_first_file = false;
		} else if(localName.equals("last_file")) {
			this.in_dates_last_file = false;
		} else if(localName.equals("newsgroups")) {
			this.in_newsgroups = false;
			this.nbdr.setNewsgroups(newsgroups);
		} else if(localName.equals("newsgroup")) {
			this.in_newsgroups_newsgroup = false;
		} else if(localName.equals("attributes")) {
			this.in_attributes = false;
			this.nbdr.setAttributes(attributes);
		} else if(localName.equals("attribute")) {
			this.in_attributes_attribute = false;
		} else if(localName.equals("value")) {
			this.in_attributes_attribute_value = false;
		} else if(localName.equals("tags")) {
			this.in_tags = false;
		} else if(localName.equals("comments")) {
			this.in_comments = false;
		} else if(localName.equals("comment")) {
			this.in_comments_comment = false;
		} else if(localName.equals("body")) {
			this.in_comments_comment_body = false;
		} else if(localName.equals("files")) {
			this.in_files = false;
		}
	}

	/**
	 * Fetches the strings which are between the opening and closing tags.
	 */
	public void characters(char ch[], int start, int length) {
		try {
			if(this.in_id) {
				this.nbdr.setNzbId(Integer.parseInt(new String(ch, start, length)));
			} else if(this.in_title) {
				this.nbdr.setTitle(new String(ch, start, length));
			} else if(this.in_category) {
				this.nbdr.setCategory(new String(ch, start, length));
			} else if(this.in_url) {
				this.nbdr.setUrl(new String(ch, start, length));
			} else if(this.in_status) {
				this.nbdr.setStatus(new String(ch, start, length));
			} else if(this.in_progress) {
				this.nbdr.setProgress(new String(ch, start, length));
			} else if(this.in_poster) {
				this.nbdr.setPoster(new String(ch, start, length));
			} else if(this.in_size) {
				this.nbdr.setSize(Long.parseLong(new String(ch, start, length)));
			} else if(this.in_dates_reported) {
				dates.put("reported", Long.parseLong(new String(ch, start, length)));
			} else if(this.in_dates_first_file) {
				dates.put("first_file", Long.parseLong(new String(ch, start, length)));
			} else if(this.in_dates_last_file) {
				dates.put("last_file", Long.parseLong(new String(ch, start, length)));
			} else if(this.in_newsgroups_newsgroup) {
				newsgroups.add(new String(ch, start, length));
			} else if(this.in_attributes_attribute_value) {
				ArrayList<String> attrs;
				if(attributes.containsKey(tempAttr)) {
					attrs = attributes.get(tempAttr);
					attrs.add(new String(ch, start, length));
					attributes.put(tempAttr, attrs);
				} else {
					attrs = new ArrayList<String>();
					attrs.add(new String(ch, start, length));
				}
				attributes.put(tempAttr, attrs);
			}
		} catch(Exception e) {
			Log.e("NewzBinDRHandler: ", "XMLError: ", e);
		}
	} 
}
