# HellaDroid #
This is an Android application which utilizes XMLRPC to monitor a running HellaNZB process on a remote server. It can add a NewzBin ID to the server, pause/resume, manipulate the queue, abort transfers. Everything a boy or girl needs to monitor their usenet downloads on the road. This application uses Google Analytics to track user activity which are used for development purposes. Now has the ability to search for reports on NewzbBin and add individual results to the queue.

## What's to come ##
Im currently developing a complete rewrite of the application. The new version will hold a new design, multi-server and SABnzbd support, and hopefully even more nzb sites to search. The new version will be split into two apps, (new v2) paid version with all the mentioned features, and a upgraded version (free v1.5) with the new UI and bugfixes.

## Version ##
Current version is 1.4.0
### Changelog ###
**v1.4.0**
NewzBin reports now include comments from newzbin, simply press menu when watching the details of a report to see if there's any comments (if so, there will be a new button). Can now search for related posts on Newzbin, if the search category is TV there's additional selections for the related search.

**v1.3.1**
Updated the "Add NZB" menu option, now accepts NewzBin IDs', URL's, and Files from SDcard

**v1.3.0**
Added a new activity, Log. Using this activity, users can see info and error logs from the HellaNZB server.

**v1.2.0**
Added NzbMatrix search support, however the search has to specific to be worth while due to limitations with nzbmatrix's api. Also there seems to be some problems with hellanzb sometimes failing to download the nzb from nzbmatrix, but instead downloads the php file. This is beucase the way nzbmatrix reply's its nzb files to hellanzb. Just try it a couple of times, it should work eventually. If not, blame HellaNZB and NzbMatrix. I also removed the search dialog in the search activity, and added a "previous results" button on the newzbin search results activity. NzbMatrix's api doesnt have this function, and therefore the results are limited to max 15 at a time. Yes i know its stupid, blame NzbMatrix. But as long as you are somewhat specific in your search it should be fine.

**v1.1.7**
http:// in url setting no longer needed and other minor bugfixes mentioned in issues tracker.

**v1.1.6**
Fixed a bug; when queue contained nzb's not added through newzbin api, queue would fail because size isnt shown. Now it displays unknown for nzbs not added through the api.

**v1.1.5**
Added a detailed view of individual reports, plus minor bugfixes

**v1.1.0**
Added a newzbin search function, the users can now search for files at newzbin, define quality, search limits, retention and also add the result to the queue.

**v1.0.2**
Added 1.6 support and more analytics tracking, nothing major.

**v1.0.1**
Minor bug fixes, now uses Google Analytics to track phone versions and behaviour. Also added a new contextmenu (push-hold-popup) for the current download which only has "Cancel" for the moment. Feature requests are welcome. Also has a new "Pause" indicator in the top.

**v1.0**
Initial release

## Requirements ##
  * Android 1.6 (or more) phone
  * HellaNZB
  * Open HellaNZB XMLRPC port in your router (default: 8760)
  * NewzBin account (if you want to search or add nzb's through interface)
    * Remember to define account credentials in hellanzb.conf
  * NzbMatrix VIP account (if you want to search or add nzb's through interface)

## Other ##
The application is in continuously development and is open-source and free. Please submit any bugs or feature requests in the [Issues List](http://code.google.com/p/helladroid/issues/list).

If you like this program, feel free to donate using PayPal (link below) and I'll add you to the [Donators](http://code.google.com/p/helladroid/wiki/Donators) page.


[![](https://www.paypal.com/en_US/i/btn/btn_donateCC_LG.gif)](https://www.paypal.com/cgi-bin/webscr?cmd=_donations&business=P84XTX5Y48N76&lc=NO&item_name=HellaDroid&item_number=helladroid&currency_code=EUR&bn=PP%2dDonationsBF%3abtn_donateCC_LG%2egif%3aNonHosted)

Thanks to zalzice for creating an awesome logo/icon :o

# Screenshots #
![http://www.unyttig.info/wp-content/uploads/2010/02/helladroid-addnzb.png](http://www.unyttig.info/wp-content/uploads/2010/02/helladroid-addnzb.png)![http://www.unyttig.info/wp-content/uploads/2010/03/helladroid-optionsmenu.png](http://www.unyttig.info/wp-content/uploads/2010/03/helladroid-optionsmenu.png)![http://www.unyttig.info/wp-content/uploads/2010/02/helladroid-context.png](http://www.unyttig.info/wp-content/uploads/2010/02/helladroid-context.png)![http://www.unyttig.info/wp-content/uploads/2010/02/helladroid-settings.png](http://www.unyttig.info/wp-content/uploads/2010/02/helladroid-settings.png)
![http://www.unyttig.info/wp-content/uploads/2010/03/helladroid-search.png](http://www.unyttig.info/wp-content/uploads/2010/03/helladroid-search.png)