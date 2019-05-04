package com.hjsoftware.qrbarcodecouponscanner.webservices;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

/**
 * Created by hjsoft on 12/1/17.
 */
public class SessionManager {


    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "SharedPref";
    private static final String IS_LOGIN = "IsLoggedIn";
    public static final String KEY_MOBILE = "mobile";
    //public static final String KEY_PWD = "pwd";
    public static final String KEY_PROFILE_ID = "profile_id";

    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     * */
    public void createLoginSession(String mobile){

        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_MOBILE, mobile);
        //editor.putString(KEY_PWD, pwd);
        //editor.putString(KEY_PROFILE_ID, profile_id);
        editor.commit();
    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     * */
    public boolean checkLogin(){

        boolean login=false;

        if(!this.isLoggedIn()){
            // user is not logged in redirect him to Login Activity

        }
        else
        {
            // user is not logged in redirect him to Login Activity
          /*  Intent i = new Intent(_context, HomeActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            _context.startActivity(i);*/

            login=true;
        }

        return  login;
    }
    /**
     * Get stored session data
     * */
    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        // user name
        user.put(KEY_MOBILE, pref.getString(KEY_MOBILE, null));
       // user.put(KEY_PWD, pref.getString(KEY_PWD, null));
       // user.put(KEY_PROFILE_ID, pref.getString(KEY_PROFILE_ID, null));
        return user;
    }
    /**
     * Clear session details
     * */
    public void logoutUser(){
        // Clearing all data from Shared Preferences

       // editor.clear();
      //  editor.commit();

        editor.putBoolean(IS_LOGIN, false);
        editor.commit();

        //  Intent i = new Intent(_context, MainActivity.class);
        // i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //  i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // Staring Login Activity
        //  _context.startActivity(i);
    }

    /**
     * Quick check for login
     * **/
    // Get Login State
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }

}
