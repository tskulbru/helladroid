package info.unyttig.helladroid.hellanzb;


import info.unyttig.helladroid.HellaDroid;
import info.unyttig.helladroid.R;

import java.net.URI;
import java.util.ArrayList;
import java.util.Map;

import org.xmlrpc.android.XMLRPCClient;
import org.xmlrpc.android.XMLRPCException;

import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;

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
 * @version 1.0
 * @see <a href="http://code.google.com/p/helladroid
 * TODO: Create a query queue, ensures queries wont get "lost".
 */
public final class HellaNZBController {
	public static final int MSG_NEW_STATUS_UPDATE = 1; 
	public static final int MSG_QUEUE_UPDATE = 2;
	public static final int MSG_CURR_DOWN_UPDATE = 3;
	public static double downSpeed = 0.0;
	public static boolean isPaused = false;
	public static boolean isAlive  = false;
	public static boolean pendingQuery = false;
	private static URI uri;
	private static XMLRPCClient client;
	private static Map<String,Object> obj;

	/**
	 * Query a Pause or Continue request to HellaNZB
	 * Returns a message to the user depending on what state HellaNZB is.
	 * 
	 * @param messageHandler the programs messagehandler.
	 */
	@SuppressWarnings("unchecked")
	public static void pauseResumeQuery(final Handler messageHandler) {
		if(!isAlive) setupConnection();
		if(isAlive) {
			if(!pendingQuery) {
				Thread thread = new Thread() {
					public void run() {
						try {

							Map<String,Object> obj = (Map<String, Object>) makeApiCall("status");
							String is_paused = obj.get("is_paused").toString();
							if(is_paused == "true")
								isPaused = true;
							else
								isPaused = false;
							if(isPaused) { 
								makeApiCall("continue");
								isPaused = false;
								callBackUpdateStatus(messageHandler, R.string.msg_nzb_continue);
							} else {
								makeApiCall("pause");
								isPaused = true;
								callBackUpdateStatus(messageHandler, R.string.msg_nzb_pause);
							}
						} catch(Exception e) {
							callBackUpdateStatus(messageHandler, e); 
						} finally {
							pendingQuery = false;
						}
					}
				};
				pendingQuery = true;
				thread.start();
			}
		} else callBackUpdateStatus(messageHandler, R.string.msg_server_down);
	}


	/**
	 * Fetches what is being currently downloaded.
	 */
	@SuppressWarnings("unchecked")
	public static void showCurrentDownload(final Handler messageHandler) {
		if(!isAlive) setupConnection();
		if(isAlive) {

			try {
				obj = (Map<String, Object>) makeApiCall("status");
				Object[] tt = (Object[]) obj.get("currently_downloading");
				Map<String,Object> obj2 = (Map<String, Object>) tt[0];
				String eta = convertEta((Integer) obj.get("eta"));

				String currNzbItem = obj2.get("nzbName").toString() + "#" +
				obj.get("percent_complete").toString() + "#" + eta + "#" + 
				obj.get("queued_mb").toString() + "#" + obj2.get("total_mb").toString() + "#" + 
				obj.get("rate").toString();

				Message message = new Message();
				message.setTarget(messageHandler);
				message.what = MSG_CURR_DOWN_UPDATE;
				message.obj = currNzbItem;
				message.sendToTarget();

			} catch(Exception e) {
				//				callBackUpdateStatus(messageHandler, "Queue is empty"); 
				String currNzbItem = "Not downloading anything#0#--:--:--#--#--#-";

				Message message = new Message();
				message.setTarget(messageHandler);
				message.what = MSG_CURR_DOWN_UPDATE;
				message.obj = currNzbItem;
				message.sendToTarget();
			} finally {
				pendingQuery = false;
			}
		} else callBackUpdateStatus(messageHandler, R.string.msg_server_down);
	}


	/**
	 * Fetches what is currently in the queue to be downloaded.
	 * creates a string which looks like "nzbName#sizeofnzb"
	 * @param messageHandler
	 */
	@SuppressWarnings("unchecked")
	public static void listQueue(final Handler messageHandler) {
		if(!isAlive) setupConnection();
		if(isAlive) {
			if(!pendingQuery) {
				Thread thread = new Thread() {
					public void run() {
						try {
							showCurrentDownload(messageHandler);
//							Object[] tt = (Object[]) makeApiCall("list");
							Object[] tt = (Object[]) obj.get("queued");
							// Test to see if the queue is empty, if it is a error will be thrown.
							@SuppressWarnings("unused")
							Map<String,Object> testQueue = (Map<String, Object>) tt[0];
							ArrayList<String> rows = new ArrayList<String>();
							// List items in queue
							for(int i = 0; i < tt.length; i++) {
								Map<String,Object> obj2 = (Map<String, Object>) tt[i];
								String qItem = obj2.get("nzbName").toString() + "#" + obj2.get("total_mb").toString() +
								"#" + obj2.get("id").toString();
								rows.add(qItem);
							}
							Object[] result = new Object[2];
							result[1] = rows;

							// return the message to the message handler.
							Message message = new Message();
							message.setTarget(messageHandler);
							message.what = MSG_QUEUE_UPDATE;
							message.obj = result;
							message.sendToTarget();

						} catch(Exception e) { 
							// Used for debugging: callBackUpdateStatus(messageHandler, e.getMessage());
							// Queue is empty, so empty it.
							ArrayList<String> rows = new ArrayList<String>();
							Object[] result = new Object[2];
							result[1] = rows;

							// return the message to the message handler.
							Message message = new Message();
							message.setTarget(messageHandler);
							message.what = MSG_QUEUE_UPDATE;
							message.obj = result;
							message.sendToTarget();
						} finally {
							pendingQuery = false;
						}
					}
				};
				pendingQuery = true;
				thread.start();
			}
		} else callBackUpdateStatus(messageHandler, R.string.msg_server_down);
	}

	/**
	 * Enqueue a Newzbin ID.
	 * @see http://www.newzbin.com
	 * TODO in the future expand this to also accept url's
	 * 
	 * @param messageHandler
	 * @param nzbid
	 */
	public static void enqueueNewzBinID(final Handler messageHandler, final String nzbid) {
		if(!isAlive) setupConnection();
		if(isAlive) {
			if(!pendingQuery) {
				Thread thread = new Thread() {
					public void run() {
						try {
							makeApiCall("enqueuenewzbin", nzbid);
							callBackUpdateStatus(messageHandler, R.string.msg_enqueue_newzbin);
						} catch(Exception e) {

						} finally {
							pendingQuery = false;
						}
					}
				};
				pendingQuery = true;
				thread.start();
			}
		} else callBackUpdateStatus(messageHandler, R.string.msg_server_down);
	}

	/**
	 * Move a NZB in the queue to the top(forcing it to download) or bottom.
	 * 
	 * @param messageHandler
	 * @param nzbid
	 */
	public static void qItemDownloadNowOrLast(final Handler messageHandler, final String nzbId, final boolean top) {
		if(!isAlive) setupConnection();
		if(isAlive) {
			if(!pendingQuery) {
				Thread thread = new Thread() {
					public void run() {
						try {
							if(top) makeApiCall("force", nzbId);
							else makeApiCall("last", nzbId);
						} catch(Exception e) {

						} finally {
							pendingQuery = false;
						}
					}
				};
				pendingQuery = true;
				thread.start();
			}
		} else callBackUpdateStatus(messageHandler, R.string.msg_server_down);
	}
	
	/**
	 * Move a NZB in the queue up or down.
	 * 
	 * @param messageHandler
	 * @param nzbid
	 */
	public static void qItemMoveUpOrDown(final Handler messageHandler, final String nzbId, final boolean up) {
		if(!isAlive) setupConnection();
		if(isAlive) {
			if(!pendingQuery) {
				Thread thread = new Thread() {
					public void run() {
						try {
							if(up) makeApiCall("up", nzbId);
							else makeApiCall("down", nzbId);
						} catch(Exception e) {

						} finally {
							pendingQuery = false;
						}
					}
				};
				pendingQuery = true;
				thread.start();
			}
		} else callBackUpdateStatus(messageHandler, R.string.msg_server_down);
	}
	
	/**
	 * Dequeue a NZB, removing it completely from queue.
	 * 
	 * @param messageHandler
	 * @param nzbId
	 */
	public static void qItemDequeue(final Handler messageHandler, final String nzbId) {
		if(!isAlive) setupConnection();
		if(isAlive) {
			if(!pendingQuery) {
				Thread thread = new Thread() {
					public void run() {
						try {
							makeApiCall("dequeue", nzbId);
						} catch(Exception e) {

						} finally {
							pendingQuery = false;
						}
					}
				};
				pendingQuery = true;
				thread.start();
			}
		} else callBackUpdateStatus(messageHandler, R.string.msg_server_down);
	}
	
	/**
	 * Send back a update status message to the message handler.
	 * This is used for short messages to the user, like "Server is offline" etc.
	 * 
	 * @param messageHandler
	 * @param txt
	 */
	public static void callBackUpdateStatus(final Handler messageHandler, final Object txt) {
		Message msg = new Message();
		msg.setTarget(messageHandler);
		msg.what = MSG_NEW_STATUS_UPDATE;
		msg.obj = txt;
		msg.sendToTarget();
	}

	/**
	 * Makes a HellaNZB client call, with command
	 * 
	 * @param command String
	 */
	private static Object makeApiCall(String command) {
		try {
			if(isAlive) {
				return client.call(command);
			} else setupConnection();
		} catch(XMLRPCException e) {
			isAlive = false;
		}
		return null;
	}

	/**
	 * Makes a HellaNZB client call, with command and possible extras.
	 * @param command
	 * @param xtra
	 * @return
	 */
	private static Object makeApiCall(String command, String xtra) {
		try {
			if(isAlive) {
				return client.call(command, xtra);
			} else setupConnection();
		} catch(XMLRPCException e) {
			isAlive = false;
		}
		return null;
	}

	/**
	 * Setups a connection to the server
	 * The boolean variable isAlive is used for defining an active connection or not.
	 */
	private static void setupConnection() {
		try {
			SharedPreferences preferences = HellaDroid.preferences;
			uri = URI.create(preferences.getString("server_url", "") + ":" + 
					preferences.getString("server_port", "8760"));
			client = new XMLRPCClient(uri);
			client.setBasicAuthentication("hellanzb", preferences.getString("server_password",""));

			// Test connection
			if(client.call("aolsay") != "")
				isAlive = true;
		} catch(Exception e) {
			isAlive = false;
		}
	}

	/**
	 * Converts seconds into readable format, used for ETA.
	 * hh:mm:ss 
	 * 
	 * @param secs
	 * @return
	 */
	private static String convertEta(int secs) {
		int hours = secs / 3600,
		remainder = secs % 3600,
		minutes = remainder / 60,
		seconds = remainder % 60; 
		String disHour = (hours < 10 ? "0" : "") + hours,
		disMinu = (minutes < 10 ? "0" : "") + minutes ,
		disSec = (seconds < 10 ? "0" : "") + seconds ;

		return(disHour +":"+ disMinu+":"+disSec);
	}
}
