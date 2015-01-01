package com.shashi.provider;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.PushService;

public class GlobalApplication extends Application {

	public static boolean isAppOpend = false;
	public static String installationId;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Parse.initialize(this, "pwuBEQ600d8CL0v8vu6GuVkndT2zgYee0odYktmQ",
				"hZtz11BGcEwLofUZuKo1fVEN6emhE58tsoDp8OeR");
		PushService.setDefaultPushCallback(this, MainActivity.class);
		ParseUser.enableAutomaticUser();
		ParseACL defaultACL = new ParseACL();
		defaultACL.setPublicReadAccess(true);
		ParseACL.setDefaultACL(defaultACL, true);
		ParseInstallation installation = ParseInstallation
				.getCurrentInstallation();
		installationId = installation.getInstallationId();
	}
}
