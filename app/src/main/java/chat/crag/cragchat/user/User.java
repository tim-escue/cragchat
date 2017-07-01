package chat.crag.cragchat.user;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class User {

    private static final String PREFERENCE = "authPref";
    private static final String TOKEN = "currentToken";
    private static final String NAME = "userName";
    private static final String PRIVATE = "private";

    public static String currentToken(Activity context) {
        return getPreference(context, TOKEN);
    }

    public static String userName(Activity context) {
        return getPreference(context, NAME);
    }

    private static String getPreference(Activity context, String pref) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
        String token = sharedPref.getString(pref, "null");
        if (token.equals("null")) {
            return null;
        } else {
            return token;
        }
    }

    public static boolean isPrivate(Activity context) {
        String priv = getPreference(context, PRIVATE);
        return priv != null && priv.equals("true");
    }

    public static void logout(Activity context) {
        setUser(context, null, null, false);
    }

    public static void setPrivacy(Activity context, String priv) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(PRIVATE, priv == null ? "null" : priv);
        editor.commit();
    }

    public static void setUser(Activity context, String tokenNew, String userName, boolean priva) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(TOKEN, tokenNew == null ? "null" : tokenNew);
        editor.putString(NAME, userName == null ? "null" : userName);
        editor.putString(PRIVATE, priva ? "true" : "false");
        editor.commit();
    }


}
