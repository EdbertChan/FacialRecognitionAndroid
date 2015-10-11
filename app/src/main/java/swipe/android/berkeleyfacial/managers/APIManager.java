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

import swipe.android.berkeleyfacial.api.tinder.LikeDislikeResponse;
import swipe.android.berkeleyfacial.api.tinder.LoginResponse;
import swipe.android.berkeleyfacial.api.tinder.TinderRecommendationResponse;
import swipe.android.berkeleyfacial.api.tinder.TrainingBodyResponse;

/**
 * API Calls
 */
public class APIManager {

    //server name. Change to point to a production server or test server
    private static final String BASE_TINDER_URL = "http://a14a1c8c.ngrok.io";
    static RequestQueue queue;

    private static APIManager instance;

    private APIManager() {

    }
    private String defaultParams(){
        return "token="+SessionManager.getInstance(this.ctx).getToken();
    }
    public String loginURL(){
        return BASE_TINDER_URL + "/login?"+defaultParams();
    }
    public String trainingURL(){
        return BASE_TINDER_URL+"/train?"+defaultParams();
    } public String likeURL(String id){
        return BASE_TINDER_URL+"/like?"+defaultParams()+"&person="+id;
    } public String dislikeURL(String id){
        return BASE_TINDER_URL+"/dislike?"+defaultParams()+"&person="+id;
    }
    public String tinderRecommendationURL(){
        return BASE_TINDER_URL + "/peeps";
    }

    public static void initialize(Context context) {

        if (queue != null)
            queue.cancelAll(ctx);
        ctx = context;
        queue = Volley.newRequestQueue(ctx);
    }
    public static void stopQueue(){
        queue.cancelAll(ctx);
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


    public void signIn(Response.Listener successListener, Response.ErrorListener errorListener) {
        String url = loginURL() + "?facebook_token="+ SessionManager.getInstance(APIManager.ctx).getFacebookToken() + "&facebook_id="
                +SessionManager.getInstance(APIManager.ctx).getFacebookID();
        GsonRequest<LoginResponse> jsObjRequest = new GsonRequest<LoginResponse>(Request.Method.POST,
               url, LoginResponse.class, null, successListener, errorListener) {


        };
        queue.add(jsObjRequest);
    }

    public void recommendationTinder(Response.Listener successListener, Response.ErrorListener errorListener) {
        GsonRequest<TinderRecommendationResponse> jsObjRequest = new GsonRequest<TinderRecommendationResponse>(Request.Method.POST,
                tinderRecommendationURL(), TinderRecommendationResponse.class, null, successListener, errorListener) {

        };
        queue.add(jsObjRequest);
    }
    public void dislike(String id, Response.Listener successListener, Response.ErrorListener errorListener) {
        GsonRequest<LikeDislikeResponse> jsObjRequest = new GsonRequest<LikeDislikeResponse>(Request.Method.POST,
                dislikeURL(id), LikeDislikeResponse.class, null, successListener, errorListener) {

        };
        queue.add(jsObjRequest);
    }
    public void like(String id, Response.Listener successListener, Response.ErrorListener errorListener) {
        GsonRequest<LikeDislikeResponse> jsObjRequest = new GsonRequest<LikeDislikeResponse>(Request.Method.POST,
                dislikeURL(id), LikeDislikeResponse.class, null, successListener, errorListener) {

        };
        queue.add(jsObjRequest);
    }


    public void submitTrainingData(final String body, Response.Listener successListener, Response.ErrorListener errorListener) {
        GsonRequest<TrainingBodyResponse> jsObjRequest = new GsonRequest<TrainingBodyResponse>(Request.Method.POST,
                trainingURL(), TrainingBodyResponse.class, null, successListener, errorListener) {
            @Override
            public byte[] getBody() throws AuthFailureError {
               return body.getBytes();
            }

        };
        queue.add(jsObjRequest);
    }
}
