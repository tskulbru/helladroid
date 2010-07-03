package info.unyttig.helladroid.nzbmatrix;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import info.unyttig.helladroid.HellaDroid;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

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
	 * api-nzb-search.php?search={SEARCH TERM}&catid={CATEGORYID}&num={MAX RESULTS}&username={USERNAME}&apikey={APIKEY}
	 * @param searchStr
	 * @param catId
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
			sendUserMsg(messageHandler, e.getMessage());
		} catch(Exception e) {
			Log.e(LOG_NAME, e.toString());
		} return reports;
	}
	
	/**
	 * Sends HTTP request to NzbMatrix with given parameters
	 * @param sUrl
	 * @return
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
			BufferedReader br = new BufferedReader(new StringReader(data));
			
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

	public static String genDownloadString(int nzbId) {
		return String.format(APIURL + "api-nzb-download.php?id=%d&username=%s&apikey=%s", 
				nzbId, nzbMatrixUsername, nzbMatrixApiKey);
	}
	private static void sendUserMsg(final Handler messageHandler, final String txt) {
		Message msg = new Message();
		msg.setTarget(messageHandler);
		msg.what = MSG_NOTIFY_USER_ERROR;
		msg.obj = txt;
		msg.sendToTarget();
	}
	/**
	 * We are introducing an API (Application Programming Interface) for NZBMatrix.

To access the API you will need an API Key for your account. This can be found in "Your Account", and you must be a VIP member to use it.

The API is intended for use by application authors and developers that require an interface into NZBMatrix.

Any abuse may impose an API ban, or further restrictions. Not for Commercial use.

API KEY INFORMATION:

Generating your own unique API Key is very simple, simply visit "Your Account" and click [Generate New API Key]. A API Key will then be generated and displayed in the API area under Current API Key:

Your unique API Key is locked to your username, no one else will have access to it. If for some reason you think its been used elsewhere, stolen, lost or you just want it changed then simply generate a new key. Generating a new API Key will render the old one useless.

The API key is a 32 character string containing letters and numbers only. It is an algorithm of several aspects of your account details and random generated keys. It is impossible to replicate.


DIRECT DOWNLOAD API:

Put simple, using the Direct Download API will save you having to visit the site. No HTML output is generated and the NZB is sent directly to your software/browser etc.

Currently you can only request complete nzb posts, partial nzb postings are not supported.

Usage:
To use the Direct Download API please use the following syntax:

http://nzbmatrix.com/api-nzb-download.php?id={NZBID}&username={USERNAME}&apikey={APIKEY}

eg: http://nzbmatrix.com/api-nzb-download.php?id=123456&username=foobar&apikey=838d43ef5cb5346d83520f6886adf935


Breakdown:
http://nzbmatrix.com/api-nzb-download.php? = URL to use. No other URL is supported, please do not try to use them.
id={NZBID} = NZBid from NZBMatrix.com
&username={USERNAME} = Your account username.
&apikey={APIKEY} = Your API Key.
&scenename=1 = Optional, this will restore scene names

Output Results:
If your request was successful the NZB you requested will be sent.

If there was a problem with your request the following will be output:
error:invalid_login = There is a problem with the username you have provided.
error:invalid_api = There is a problem with the API Key you have provided.
error:invalid_nzbid = There is a problem with the NZBid supplied.
error:please_wait_x = Please wait x seconds before retry.
error:vip_only = You need to be VIP or higher to access.
error:disabled_account = User Account Disabled.
error:x_daily_limit = You have reached the daily download limit of x.
error:no_nzb_found = No NZB found.

Notes:
All NZB posts downloaded with the API will count towards your daily download limits.
Only full posts can be retrieved, there is no support for partial posts.
You are limited to 6 NZBs per 60 seconds.
Only one post can be downloaded per request.
SSL is supported, please use https://nzbmatrix.com instead.
All output is sent using GZip Compression.


NZB POST DETAILS API:

This API will display various details of an NZB posting

Usage:
To use the NZB DETAILS API please use the following syntax:

http://nzbmatrix.com/api-nzb-details.php?id={NZBID}&username={USERNAME}&apikey={APIKEY}

eg: http://nzbmatrix.com/api-nzb-details.php?id=123456&username=foobar&apikey=838d43ef5cb5346d83520f6886adf935


Breakdown:
http://nzbmatrix.com/api-nzb-details.php? = URL to use. No other URL is supported, please do not try to use them.
id={NZBID} = NZBid from NZBMatrix.com
&username={USERNAME} = Your account username.
&apikey={APIKEY} = Your API Key.

Output Results:
Sample output is as follows, {FIELD}:{VALUE};
NZBNAME:mandriva linux 2009; = NZB Name On Site
INDEX_DATE:2009-02-14 09:08:55; = Indexed By Site (Date/Time GMT)
USENET_DATE:2009-02-12 2:48:47; = Posted To Usenet (Date/Time GMT)
GROUP:alt.binaries.linux; = Usenet Newsgroup
PARTS:5702; = Number Of Parts
SIZE:1469988208.64; = Size in bytes
USENET_SUBJECT:[mandriva linux 2009] - "mandriva linux 2009.part03.rar" yEnc (1/201); = Usenet Post Subject
COMMENTS:0; = Number Of Comments Posted
HITS:174; = Number Of Hits (Views)
NFO:yes; = NFO Present
LINK:http://linux.org; = HTTP Link To Attached Website
CATEGORY:Apps > PC; = Our Site Category
LANGUAGE:English; = Language Attached From Our Index
IMAGE:http://linux.org/logo.gif; = HTTP Link To Attached Image
REGION:FREE; = Region Info

If there was a problem with your request the following will be output:
error:invalid_login = There is a problem with the username you have provided.
error:invalid_api = There is a problem with the API Key you have provided.
error:invalid_nzbid = There is a problem with the NZBid supplied.
error:vip_only = You need to be VIP or higher to access.
error:disabled_account = User Account Disabled.
error:no_nzb_found = No NZB found.

Notes:
Only one post can be downloaded per request.
SSL is supported, please use https://nzbmatrix.com instead.
All output is sent using GZip Compression.


NZB SEARCH API:

This API will perform a NZB Post search and return a maximum of 15 results

Usage:
To use the NZB SEARCH API please use the following syntax:

http://nzbmatrix.com/api-nzb-search.php?search={SEARCH TERM}&catid={CATEGORYID}&num={MAX RESULTS}&username={USERNAME}&apikey={APIKEY}

eg: http://nzbmatrix.com/api-nzb-search.php?search=linux&catid=11&num=1&username=foobar&apikey=838d43ef5cb5346d83520f6886adf935


Breakdown:
http://nzbmatrix.com/api-nzb-search.php? = URL to use. No other URL is supported, please do not try to use them.
search={SEARCH TERM} = Search Term
&catid={CATEGORYID} = OPTIONAL, if left blank all categories are searched, category ID used from site.
&num={MAX RESULTS} = OPTIONAL, if left blank a maximum of 5 results will display, 5 is the maximum number of results that can be produced.
&age={MAX AGE} = OPTIONAL, if left blank full site retention will be used. Age must be number of "days" eg 200
&region={SEE DESC} = OPTIONAL, if left blank results will not be limited 1 = PAL, 2 = NTSC, 3 = FREE
&group={SEE DESC} = OPTIONAL, if left blank all groups will be searched, format is full group name "alt.binaries.X"
&username={USERNAME} = Your account username.
&apikey={APIKEY} = Your API Key.
&larger={MIN SIZE} = OPTIONAL, minimum size in MB
&smaller={MAX SIZE} = OPTIONAL, maximum size in MB
&minhits={MIN HITS} = OPTIONAL, minimum hits
&maxhits={MAX HITS} = OPTIONAL, maximum hits
&maxage={MAX AGE} = OPTIONAL, same as &age (above) here for matching site search vars only
&englishonly=1{ENGLISH ONLY} = OPTIONAL, if added the search will only return ENGLISH and UNKNOWN matches
&searchin={SEARCH FIELD} = OPTIONAL, (name, subject, weblink) if left blank or not used then search filed is "name"

Output Results:
Sample output is as follows, {FIELD}:{VALUE};
NZBID:444027; = NZB ID On Site
NZBNAME:mandriva linux 2009; = NZB Name On Site
LINK:nzbmatrix.com/nzb-details.php?id=444027&hit=1; = Link To NZB Details PAge
SIZE:1469988208.64; = Size in bytes
INDEX_DATE:2009-02-14 09:08:55; = Indexed By Site (Date/Time GMT)
USENET_DATE:2009-02-12 2:48:47; = Posted To Usenet (Date/Time GMT)
CATEGORY:TV > Divx/Xvid; = NZB Post Category
GROUP:alt.binaries.linux; = Usenet Newsgroup
COMMENTS:0; = Number Of Comments Posted
HITS:174; = Number Of Hits (Views)
NFO:yes; = NFO Present
WEBLINK:http://linux.org; = HTTP Link To Attached Website
LANGUAGE:English; = Language Attached From Our Index
IMAGE:http://linux.org/logo.gif; = HTTP Link To Attached Image
REGION:0; = Region Coding (See notes)

If there was a problem with your request the following will be output:
error:invalid_login = There is a problem with the username you have provided.
error:invalid_api = There is a problem with the API Key you have provided.
error:invalid_nzbid = There is a problem with the NZBid supplied.
error:vip_only = You need to be VIP or higher to access.
error:disabled_account = User Account Disabled.
error:no_nzb_found = No NZB found.
error:no_search = No search query.
error:please_wait_x = Please wait x seconds before retry.
error:nothing_found = No Results Found.

Notes:
A maximum of 15 results only.
&num=x OPTIONAL (Default 15).
&catid=x OPTIONAL (Default All).
You are limited to 1 search per 10 seconds.
Region Coding 0 = Unknown, 1 = PAL, 2 = NTSC, 3 = FREE
| Character will seperate search results
SSL is supported, please use https://nzbmatrix.com instead.
All output is sent using GZip Compression.
	 */
}
