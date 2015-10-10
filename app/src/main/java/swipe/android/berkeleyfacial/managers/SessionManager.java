package swipe.android.berkeleyfacial.managers;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Handles shared preference
 */
public class SessionManager {
    // Shared Preferences
    static SharedPreferences pref;

    // Editor for Shared preferences
    static Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    static int PRIVATE_MODE = 0;

    // Sharedpref file name
    public static final String PREF_NAME = "BerkeleyFacial";
    private static SessionManager instance;

    // USER
    private static final String IS_LOGGED_IN = "IS_LOGGED_IN";
    private static final String USER_NAME = "username";
    private static final String TOKEN = "token";
    private static final String ID = "id";


    //constructor. We set private mode b/c we dont want to share w/ other apps
    private SessionManager(Context c) {
        this._context = c;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    //Singleton
    public static synchronized SessionManager getInstance(Context c) {
        if (instance == null)
            instance = new SessionManager(c);
        return instance;

    }

    public void clearAll() {
        editor.clear();
        editor.commit();
    }
    public void setFacebookToken(String token) {
        editor.putString(TOKEN, token);
        editor.commit();

    }
    public void setFacebookID(String id) {
        editor.putString(ID, id);
        editor.commit();

    }
    public void setName(String name) {
        editor.putString(USER_NAME, name);
        editor.commit();

    }
    public void setIsLoggedIn(boolean isLoggedIn) {
        editor.putBoolean(IS_LOGGED_IN, isLoggedIn);
        editor.commit();

    }
    public String getName(){
        return pref.getString(USER_NAME, "");
    }
    public String getFacebookToken(){
        return pref.getString(TOKEN, "");
    }
    public boolean getIsLoggedIn(){
        return pref.getBoolean(IS_LOGGED_IN, false);
    }

    public String getFacebookID(){
        return pref.getString(ID, "");
    }

}