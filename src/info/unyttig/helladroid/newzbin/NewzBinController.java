package info.unyttig.helladroid.newzbin;

import info.unyttig.helladroid.HellaDroid;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException; 
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList; 
import java.util.HashMap;
import java.util.Iterator; 
import java.util.Map; 

import org.apache.http.HttpResponse; 
import org.apache.http.NameValuePair; 
import org.apache.http.client.ClientProtocolException; 
import org.apache.http.client.HttpClient; 
import org.apache.http.client.entity.UrlEncodedFormEntity; 
import org.apache.http.client.methods.HttpPost; 
import org.apache.http.impl.client.DefaultHttpClient; 
import org.apache.http.message.BasicNameValuePair; 
import org.apache.http.util.ByteArrayBuffer;

import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class NewzBinController { 
	private final static String LOG_NAME = "<HellaDroid> NewzbinController: ";
	private final static SharedPreferences preferences = HellaDroid.preferences;
	private final static String NBAPIURL = "http://www.newzbin.com/api/";
	private final static int MSG_NOTIFY_USER_ERROR = 2;

	/**
	 * TODO sjekk om dne finnes fra før i et hashmap<string,NewzBinReport>
	 * TODO utvid newzbinreport til å ha reportInfo også event lag et nytt obj og knytt de
	 * @param id
	 */
	public static void getReportInfo(int id) {
		String url = NBAPIURL + "reportinfo/";
	}

	/**
	 * Finds reports based on the paramaters given in searchOptions
	 * 
	 * @param searchOptions
	 * @return ArrayList<NewzBinReport> - list of result reports.
	 */
	public static ArrayList<NewzBinReport> findReport(final Handler messageHandler, 
			final HashMap<String, String> searchOptions) {
		String url = NBAPIURL + "reportfind/";
		ArrayList<NewzBinReport> searchRes = new ArrayList<NewzBinReport>();
		try {
			HttpResponse response = doPost(url, searchOptions);
			checkReturnCode(response.getStatusLine().getStatusCode(), false);

			InputStream is = response.getEntity().getContent(); 
			BufferedInputStream bis = new BufferedInputStream(is); 
			ByteArrayBuffer baf = new ByteArrayBuffer(20); 

			int current = 0;   
			while((current = bis.read()) != -1){   
				baf.append((byte)current);   
			}   
			/* Convert the Bytes read to a String. */   
			String text = new String(baf.toByteArray());
//			Log.d(LOG_NAME, text);
			
			BufferedReader reader = new BufferedReader(new StringReader(text));
			String str = reader.readLine();
			String totalRes = str;
			while ((str = reader.readLine()) != null) {
				String[] values = str.split("	");
				NewzBinReport temp2 = new NewzBinReport(Integer.parseInt(values[0]),Long.parseLong(values[1]),values[2]);
				searchRes.add(temp2);
			}

			Object[] result = new Object[2];
			result[0] = totalRes;
			result[1] = searchRes;
			return searchRes;
			
			// TODO message handling
		} catch (ClientProtocolException e) {
			Log.e(LOG_NAME, "ClientProtocol thrown: ", e);
			sendUserMsg(messageHandler, e.toString());
		} catch (IOException e) {
			Log.e(LOG_NAME, "IOException thrown: ", e);
			sendUserMsg(messageHandler, e.toString());
		} catch (NewzBinPostReturnCodeException e) {
			Log.e(LOG_NAME, "POST ReturnCode error: " + e.toString());
			sendUserMsg(messageHandler, e.getMessage());
		}
		return searchRes;
	}

	/**
	 * Validate the post return code, if not return code 200, throw exception.
	 * 
	 * @param statusCode
	 * @param hasId - to separate reportInfo and reportFind
	 * @throws NewzBinPostReturnCodeException
	 */
	private static void checkReturnCode(int statusCode, boolean hasId) 
	throws NewzBinPostReturnCodeException {
		switch(statusCode) {
		case 204: throw new NewzBinPostReturnCodeException("Search returned no results");
		case 401: throw new NewzBinPostReturnCodeException("Unauthorized: incorrect authentication details");
		case 402: throw new NewzBinPostReturnCodeException("Payment Required (the account is not a premium account)");
		case 404: throw new NewzBinPostReturnCodeException("Report not found (contact developer)");
		case 500: throw new NewzBinPostReturnCodeException("Internal Server Error (Newzbin broke)");
		case 503: throw new NewzBinPostReturnCodeException("Service Unavailable (Newzbin is down for maintenance/etc)");
		case 550: 
			if(hasId)
				throw new NewzBinPostReturnCodeException("Missing id parameter (contact developer)");
			else
				throw new NewzBinPostReturnCodeException("Missing search parameter");
		}

	}
	
	/**
	 * Send back a update status message to the message handler.
	 * This is used for short messages to the user, like "Search had no result" etc.
	 * 
	 * @param messageHandler
	 * @param txt
	 */
	public static void sendUserMsg(final Handler messageHandler, final String txt) {
		Message msg = new Message();
		msg.setTarget(messageHandler);
		msg.what = MSG_NOTIFY_USER_ERROR;
		msg.obj = txt;
		msg.sendToTarget();
	}
	
	/**
	 * Sends HTTP POST requests to a Newzbin with given parameters. 
	 * 
	 * @param url Type:String - The url to connect to
	 * @param kvPairs Type:Map - The map of parameters to post
	 * @return Returns a HttpResponse with results 
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static HttpResponse doPost(String url, HashMap<String, String> kvPairs) 
	throws ClientProtocolException, IOException {

		HttpClient httpclient = new DefaultHttpClient(); 
		HttpPost httppost = new HttpPost(url); 
		httppost.addHeader("Content-type", "application/x-www-form-urlencoded");

		if (kvPairs != null && kvPairs.isEmpty() == false) { 
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>( 
					kvPairs.size()+2);
			nameValuePairs.add(new BasicNameValuePair("username", preferences.getString("newzbin_username", ""))); 
			nameValuePairs.add(new BasicNameValuePair("password", preferences.getString("newzbin_password", ""))); 
			String k, v; 
			Iterator<String> itKeys = kvPairs.keySet().iterator(); 
			while (itKeys.hasNext()) { 
				k = itKeys.next(); 
				v = kvPairs.get(k); 
				nameValuePairs.add(new BasicNameValuePair(k, v)); 
			} 
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs)); 
		} 
		return httpclient.execute(httppost);  
	} 

} 