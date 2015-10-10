package swipe.android.berkeleyfacial.managers;

import android.content.Context;
import android.util.Log;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;
import org.michenux.drodrolib.network.volley.GsonRequest;

import java.io.File;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import swipe.android.berkeleyfacial.api.tinder.TinderLoginResponse;

/**
 * API Calls
 */
public class APIManager {

    //server name. Change to point to a production server or test server
    private static final String BASE_TINDER_URL = "https://api.gotinder.com";
    static RequestQueue queue;

    private static APIManager instance;

    private APIManager() {

    }
    public String tinderLoginUrl(){
        return BASE_TINDER_URL + "/auth";
    }

    public static void initialize(Context context) {

        if (queue != null)
            queue.cancelAll(ctx);
        ctx = context;
        queue = Volley.newRequestQueue(ctx);
    }

    private static Context ctx;

    public static synchronized APIManager getInstance() {
        if (ctx == null) {
            throw new IllegalArgumentException("Impossible to get the instance. This class must be initialized before");
        }
        if (instance == null)
            instance = new APIManager();
        return instance;
    }
    //setUPTinder


    public void signInTinder(Response.Listener successListener, Response.ErrorListener errorListener) {
        GsonRequest<TinderLoginResponse> jsObjRequest = new GsonRequest<TinderLoginResponse>(Request.Method.POST,
                tinderLoginUrl(), TinderLoginResponse.class, null, successListener, errorListener) {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<String,String>();
                params.put("facebook_token", SessionManager.getInstance(APIManager.ctx).getFacebookToken());
                params.put("facebook_id", SessionManager.getInstance(APIManager.ctx).getFacebookID());
                return params;

            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> headers=new HashMap<String,String>();
                headers.put("Accept","application/json");
                headers.put("Content-Type","application/json");
                headers.put("User-Agent","Tinder/3.0.4 (iPhone; iOS 7.1; Scale/2.00)");
                return headers;
            }
        };
        queue.add(jsObjRequest);
    }

}