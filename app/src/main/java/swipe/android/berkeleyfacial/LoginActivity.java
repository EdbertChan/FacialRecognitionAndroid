package swipe.android.berkeleyfacial;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;


import com.android.volley.Response;
import com.facebook.AccessToken;
import com.facebook.AppEventsLogger;
import com.facebook.Request;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.android.Facebook;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import swipe.android.berkeleyfacial.api.tinder.TinderLoginResponse;
import swipe.android.berkeleyfacial.managers.APIManager;
import swipe.android.berkeleyfacial.managers.SessionManager;

public class LoginActivity extends FacialActivity implements
        Response.Listener<TinderLoginResponse>{


    public static final int REGISTER_REQUEST_CODE = 100;
    public final String API_KEY = "906296966129825";
    Facebook facebook = new Facebook(API_KEY);


    @InjectView(R.id.facebook_login)
    LoginButton FBloginButton;

    @Override
    protected void onResume() {
        super.onResume();
        uiHelper.onResume();
        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
        Session session = Session.getActiveSession();
        if(session != null && session.isOpened() && !isLoggingIn)
            doFacebookLogin(session);
    }
    boolean isLoggingIn = false;

    @Override
    protected void onPause() {
        super.onPause();
        uiHelper.onPause();

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        ButterKnife.inject(this);
        //Facebook Setup
        FBloginButton.setReadPermissions(Arrays.asList("user_friends"));

        uiHelper = new UiLifecycleHelper(this, callback);
        uiHelper.onCreate(savedInstanceState);



    }

    private void onSessionStateChange(Session session, SessionState state,
                                      Exception exception) {
        if (state.isOpened()) {
            isLoggingIn = true;
            doFacebookLogin(session);
        }
    }

    private void doFacebookLogin(final Session session) {



        Request request = Request.newMeRequest(session,
                new Request.GraphUserCallback() {
                    @Override
                    public void onCompleted(GraphUser user, com.facebook.Response response) {
                        // If the response is successful

                        if (session == Session.getActiveSession()) {
                            if (user != null) {

                                String fbId = user.getId();
                                String fbName = user.getName();
                                String accessToken= Session.getActiveSession().getAccessToken();
                                //want to get user token now
                                SessionManager.getInstance(LoginActivity.this).setFacebookID(fbId);
                                SessionManager.getInstance(LoginActivity.this).setFacebookToken(accessToken);
                                SessionManager.getInstance(LoginActivity.this).setName(fbName);

                                Log.d("FacebookID", fbId);
                                Log.d("fbName", fbName);
                                Log.d("accessToken", accessToken);
                                APIManager.getInstance().signInTinder(
                                        LoginActivity.this, LoginActivity.this);

                            }
                        }
                        if (response.getError() != null) {
                            // Handle errors, will do so later.
                        }
                    }
                });
        request.executeAsync();
    }

    private UiLifecycleHelper uiHelper;
    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state,
                         Exception exception) {

            onSessionStateChange(session, state, exception);
        }
    };



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Check which request we're responding to
        if (requestCode == REGISTER_REQUEST_CODE && resultCode == RESULT_OK) {
            // Make sure the request was successful
         /*   APIManager.getInstance().signIn(data.getStringExtra(RegisterActivity.USERNAME),
                    data.getStringExtra(RegisterActivity.PASSWORD),
                    this, this);*/
        }

        Session.getActiveSession().onActivityResult(this, requestCode,
                resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }

    @Override
    public void onResponse(TinderLoginResponse tinderLoginResponse) {

    }
}
