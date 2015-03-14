# Introduction #
Some of you have had problems configuring HellaNZB and HellaDroid. I'll try to explain how you do just this in this wiki-article.


# Prerequisites #
  * HellaNZB installed and running (see either their own webpage, or your GNU/Linux distro's wiki to get it up and running)
  * Android phone using Android 1.6 or newer.
  * HellaDroid installed

# Configuring HellaNZB #

To be able to connect and communicate the HellaNZB server, we need to enable some features in HellaNZB's .conf.

  1. Locate your hellanzb.conf, and open it up in your favourite editor. Now change the following lines to your liking (if you don't have them you can add them)
```
Hellanzb.CATEGORIZE_DEST = True # Optional
Hellanzb.XMLRPC_SERVER = 'localhost' # Don't change this
Hellanzb.XMLRPC_PORT = 8765 # You can change this to whatever you want though
Hellanzb.XMLRPC_PASSWORD = 'changeme' 
 
# If you want Newzbin support, you HAVE to change these as well
Hellanzb.NEWZBIN_USERNAME = 'yournewzbinuser'
Hellanzb.NEWZBIN_PASSWORD = 'yournewzbinpass'
```

  1. Restart your HellaNZB process (if its running, either way start it).
  1. Now you need to configure your router to allow outside calls to your port 8765 (if you didn't change it), I won't go into details on how you do this, Google it (port forwarding).
  1. _OPTIONAL: I recommend using some sort of dynamic dns so you don't have to remember your external IP all the time, DynDNS.org  is such a free service. You can then have hellanzb.homeftp.org or something like that. DynDNS is well documented and covered for both Windows and GNU/Linux (and probably Mac also)._

# Configuring HellaDroid #

This should be pretty straight forward

  1. Open up HellaDroid, open up menu, select settings and fill in everything.
  1. When your done, exit the menu (so your back to the main view), hit refresh and if you don't get any error message everything should work. If didn't work, try restarting the application.
  1. You can now test the application by doing a Newzbin/NzbMatrix search and trying to add a report (either long press a list item, or open one of the list items and press menu and download).