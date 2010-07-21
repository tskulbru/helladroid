package info.unyttig.helladroid.nzbmatrix;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import info.unyttig.helladroid.HellaDroid;

import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
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
public class NzbMatrixController {
	private final static int MSG_NOTIFY_USER_ERROR = 2;
	private final static String LOG_NAME = "<HellaDroid> NzbMatrixController: ";
	private final static String APIURL = "http://nzbmatrix.com/";
	private final static SharedPreferences preferences = HellaDroid.preferences;
	private final static String nzbMatrixUsername = preferences.getString("nzbmatrix_username", "");
	private final static String nzbMatrixApiKey = preferences.getString("nzbmatrix_apikey", "");
	private final static String nzbMatrixSearchLimit = preferences.getString("nzbmatrix_search_limit", "");
	
	private static ArrayList<NzbMatrixReport> reports = new ArrayList<NzbMatrixReport>();
	public static int totalRes;
	
	
	/**
	 * Searches NzbMatrix.com based on a search string and a category id, 
	 * returns error message if needed, else it returns a list of report objects 
	 * which are to be shown in the activity
	 * 
	 * @param messageHandler
	 * @param searchStr
	 * @param catId
	 * @return ArrayList<NzbMatrixReport>|reports A list of search result objects
	 */
	public static ArrayList<NzbMatrixReport> searchNzbMatrix(final Handler messageHandler, String searchStr, String catId) {
		reports.clear();
		try {
			String url = String.format(APIURL + "api-nzb-search.php?search=%s&catid=%s&num=%s&username=%s&apikey=%s",
			searchStr, catId, nzbMatrixSearchLimit, nzbMatrixUsername, nzbMatrixApiKey);
			String[] data = doPost(url);
			return parseData(data);
		} catch(NzbMatrixException e) {
			Log.e(LOG_NAME, "NzbMatrix query error: " + e.toString());
			sendUserMsg(messageHandler, genErrorString(e.getMessage()));
		} catch(Exception e) {
			Log.e(LOG_NAME, e.toString());
			sendUserMsg(messageHandler, "Site might be down, try again later");
		} return reports;
	}
	
	/**
	 * Sends HTTP request to NzbMatrix with given parameters
	 * @param sUrl
	 * @return String[]|data Array of unparsed report results
	 */
	private static String[] doPost(String sUrl) {
		String[] data = null;
 		String str = "";
		StringBuffer sb = new StringBuffer();
		try {
			URL url = new URL(sUrl);
			BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
			while((str = br.readLine()) != null) 
				sb.append(str);
			String temp = sb.toString();
//			String temp = "NZBID:596568;NZBNAME:Real Heroes Among Us S01E14 DSR XviD WaLMaRT;LINK:nzbmatrix.com/nzb-details.php?id=596568&hit=1;SIZE:421988925.44;INDEX_DATE:2010-03-08 17:30:34;USENET_DATE:2010-03-08 5:39:04;CATEGORY:TV > Divx/Xvid;GROUP:alt.binaries.teevee;COMMENTS:0;HITS:64;NFO:yes;WEBLINK:;LANGUAGE:English;IMAGE:;REGION:0;|NZBID:584765;NZBNAME:Heroes on Hot Wheels [Premo];LINK:nzbmatrix.com/nzb-details.php?id=584765&hit=1;SIZE:2523419115.52;INDEX_DATE:2010-02-15 17:09:02;USENET_DATE:2010-02-14 3:15:41;CATEGORY:TV > Divx/Xvid;GROUP:alt.binaries.multimedia.cartoons;COMMENTS:0;HITS:138;NFO:;WEBLINK:http://en.wikipedia.org/wiki/Michel_Vaillant;LANGUAGE:English;IMAGE:http://img220.imageshack.us/img220/2578/250pxmichelvaillant01u.jpg;REGION:0;|NZBID:580617;NZBNAME:Heroes S04E19 HDTV XviD LOL;LINK:nzbmatrix.com/nzb-details.php?id=580617&hit=1;SIZE:418172108.8;INDEX_DATE:2010-02-09 03:03:11;USENET_DATE:2010-02-09 3:11:01;CATEGORY:TV > Divx/Xvid;GROUP:alt.binaries.teevee;COMMENTS:0;HITS:1462;NFO:yes;WEBLINK:;LANGUAGE:English;IMAGE:;REGION:0;|NZBID:575930;NZBNAME:Heroes S04E18 HDTV XviD LOL;LINK:nzbmatrix.com/nzb-details.php?id=575930&hit=1;SIZE:420332175.36;INDEX_DATE:2010-02-02 03:03:13;USENET_DATE:2010-02-02 3:10:45;CATEGORY:TV > Divx/Xvid;GROUP:alt.binaries.teevee;COMMENTS:1;HITS:1512;NFO:yes;WEBLINK:;LANGUAGE:English;IMAGE:;REGION:0;|NZBID:571455;NZBNAME:Heroes S04E17 HDTV XviD 2HD;LINK:nzbmatrix.com/nzb-details.php?id=571455&hit=1;SIZE:418329395.2;INDEX_DATE:2010-01-26 03:08:04;USENET_DATE:2010-01-26 3:14:54;CATEGORY:TV > Divx/Xvid;GROUP:alt.binaries.teevee;COMMENTS:0;HITS:1487;NFO:yes;WEBLINK:;LANGUAGE:English;IMAGE:;REGION:0;|NZBID:567337;NZBNAME:Heroes S04E16 HDTV XviD LOL;LINK:nzbmatrix.com/nzb-details.php?id=567337&hit=1;SIZE:420458004.48;INDEX_DATE:2010-01-19 03:03:22;USENET_DATE:2010-01-19 3:09:53;CATEGORY:TV > Divx/Xvid;GROUP:alt.binaries.teevee;COMMENTS:5;HITS:1534;NFO:yes;WEBLINK:;LANGUAGE:English;IMAGE:;REGION:0;|NZBID:562319;NZBNAME:Heroes S04E15 HDTV XviD LOL;LINK:nzbmatrix.com/nzb-details.php?id=562319&hit=1;SIZE:420416061.44;INDEX_DATE:2010-01-12 03:03:04;USENET_DATE:2010-01-12 3:09:23;CATEGORY:TV > Divx/Xvid;GROUP:alt.binaries.teevee;COMMENTS:3;HITS:5386;NFO:yes;WEBLINK:;LANGUAGE:English;IMAGE:;REGION:0;|NZBID:561268;NZBNAME:Heroes S04E01 E02 Orientation Jump Push Fall HDTV XviD FQM (REQ);LINK:nzbmatrix.com/nzb-details.php?id=561268&hit=1;SIZE:808431124.48;INDEX_DATE:2010-01-10 12:30:40;USENET_DATE:2009-09-22 8:22:04;CATEGORY:TV > Divx/Xvid;GROUP:alt.binaries.mom;COMMENTS:0;HITS:451;NFO:yes;WEBLINK:http://www.imdb.com/title/tt0813715/;LANGUAGE:English;IMAGE:;REGION:0;|NZBID:557543;NZBNAME:Heroes S04E14 HDTV XviD LOL;LINK:nzbmatrix.com/nzb-details.php?id=557543&hit=1;SIZE:591868723.2;INDEX_DATE:2010-01-05 03:16:45;USENET_DATE:2010-01-05 3:22:51;CATEGORY:TV > Divx/Xvid;GROUP:alt.binaries.teevee;COMMENTS:1;HITS:5545;NFO:yes;WEBLINK:;LANGUAGE:English;IMAGE:;REGION:0;|NZBID:557524;NZBNAME:Heroes S04E13 HDTV XviD LOL;LINK:nzbmatrix.com/nzb-details.php?id=557524&hit=1;SIZE:420458004.48;INDEX_DATE:2010-01-05 02:03:51;USENET_DATE:2010-01-05 2:10:04;CATEGORY:TV > Divx/Xvid;GROUP:alt.binaries.teevee;COMMENTS:0;HITS:5247;NFO:yes;WEBLINK:;LANGUAGE:English;IMAGE:;REGION:0;|NZBID:537810;NZBNAME:Heroes S04E12 HDTV XviD LOL;LINK:nzbmatrix.com/nzb-details.php?id=537810&hit=1;SIZE:431751168;INDEX_DATE:2009-12-01 02:19:06;USENET_DATE:2009-12-01 3:08:54;CATEGORY:TV > Divx/Xvid;GROUP:alt.binaries.multimedia;COMMENTS:10;HITS:6928;NFO:yes;WEBLINK:;LANGUAGE:English;IMAGE:;REGION:0;";
			data = temp.split("\\|");
			
		} catch (Exception e) {
			Log.e(LOG_NAME, e.toString());
		} return data;
	}
	

	/**
	 * Parses the array with the results, and creates a list of 
	 * report objects to be handled later.
	 * 
	 * @param input
	 * @return ArrayList|res contains all the reports from the query
	 * @throws NzbMatrixException 
	 */
	private static ArrayList<NzbMatrixReport> parseData(String[] input) throws NzbMatrixException {
		String[] checkError = input[0].split("\\:");
		if(checkError[0].equals("error"))
			throw new NzbMatrixException(checkError[1]);
		// TODO: This solution sucks big time and is ugly, find a better and prettier one.
		for(int i = 0; i < input.length; i++) {
			String data = input[i];
			
			int nzbidpos = data.indexOf("NZBID:");
			int nzbnamepos = data.indexOf("NZBNAME:");
			int linkpos = data.indexOf("LINK:");
			int sizepos = data.indexOf("SIZE:");
			int index_datepos = data.indexOf("INDEX_DATE:");
			int usenet_datepos = data.indexOf("USENET_DATE:");
			int categorypos = data.indexOf("CATEGORY:");
			int grouppos = data.indexOf("GROUP:");
			int commentspos = data.indexOf("COMMENTS:");
			int hitspos = data.indexOf("HITS:");
			int nfopos = data.indexOf("NFO:");
			int weblinkpos = data.indexOf("WEBLINK:");
			int languagepos = data.indexOf("LANGUAGE:");
			int imagepos = data.indexOf("IMAGE:");
			int regionpos = data.indexOf("REGION:");
			
			NzbMatrixReport rep = new NzbMatrixReport();
			rep.setNzbId(Integer.parseInt(data.substring(nzbidpos+6, nzbnamepos-1)));
			rep.setNzbName(data.substring(nzbnamepos+8, linkpos-1));
			rep.setLink("http://" + data.substring(linkpos+5, sizepos-1));
			rep.setSize(Double.parseDouble(data.substring(sizepos+5, index_datepos-1)));
			rep.setIndex_date(data.substring(index_datepos+11, usenet_datepos-1));
			rep.setUsenet_date(data.substring(usenet_datepos+12, categorypos-1));
			rep.setCategory(data.substring(categorypos+9, grouppos-1));
			rep.setGroup(data.substring(grouppos+6, commentspos-1));
			rep.setComments(Integer.parseInt(data.substring(commentspos+9, hitspos-1)));
			rep.setHits(Integer.parseInt(data.substring(hitspos+5, nfopos-1)));
			rep.setNfo(data.substring(nfopos+4, weblinkpos-1));
			rep.setWeblink(data.substring(weblinkpos+8, languagepos-1));
			rep.setLanguage(data.substring(languagepos+9, imagepos-1));
			rep.setImage(data.substring(imagepos+6, regionpos-1));
			rep.setRegion(Integer.parseInt(data.substring(regionpos+7, data.length()-1)));
			
			// Add it to our result
			reports.add(rep);
		}
		totalRes = reports.size(); 
		return reports;
	}

	/**
	 * Generate a proper url string for HellaNZB to download
	 * 
	 * @param nzbId
	 * @return String 
	 */
	public static String genDownloadString(int nzbId) {
		return String.format(APIURL + "api-nzb-download.php?id=%d&username=%s&apikey=%s", 
				nzbId, nzbMatrixUsername, nzbMatrixApiKey);
	}

	/**
	 * Generates readable error messages based on the exception message
	 * 
	 * @param errorExMsg
	 * @return String
	 */
	private static String genErrorString(String errorExMsg) {
		if(errorExMsg.equals("invalid_login"))
				return "There is a problem with the username you have provided.";
		else if(errorExMsg.equals("invalid_api"))
				return "There is a problem with the API Key you have provided.";
		else if(errorExMsg.equals("invalid_nzbid"))
				return "There is a problem with the NZBid supplied.";
		else if(errorExMsg.equals("vip_only"))
				return "You need to be VIP or higher to access.";
		else if(errorExMsg.equals("disabled_account"))
				return "User Account Disabled.";
		else if(errorExMsg.equals("no_nzb_found"))
				return "No NZB found.";
		else if(errorExMsg.equals("no_search"))
				return "No search query.";
		else if(errorExMsg.equals("please_wait_30"))
				return "Please wait 30 seconds before retry.";
		else if(errorExMsg.equals("nothing_found"))
				return "No Results Found.";
		// Havent seen this error message yet, so no idea what x is supposed to be so just check if it contains it.
		else if(errorExMsg.contains("_daily_limit"))
				return "You have reached the daily download limit";
		return errorExMsg;
	}
	
	/**
	 * Send back a update status message to the message handler.
	 * This is used for short messages to the user, like "Search had no result" etc.
	 * 
	 * @param messageHandler
	 * @param txt
	 */
	private static void sendUserMsg(final Handler messageHandler, final String txt) {
		Message msg = new Message();
		msg.setTarget(messageHandler);
		msg.what = MSG_NOTIFY_USER_ERROR;
		msg.obj = txt;
		msg.sendToTarget();
	}
}
