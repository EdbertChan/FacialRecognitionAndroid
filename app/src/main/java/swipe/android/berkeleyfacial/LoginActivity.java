package swipe.android.berkeleyfacial;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import swipe.android.berkeleyfacial.api.tinder.LoginResponse;
import swipe.android.berkeleyfacial.managers.APIManager;
import swipe.android.berkeleyfacial.managers.SessionManager;

public class LoginActivity extends FacialActivity implements
        Response.Listener<LoginResponse>{


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

        Intent i = new Intent(this, SwipeActivity.class);
        startActivity(i);
        finish();

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

    public static void callFacebookLogout(Context context) {
        Session session = Session.getActiveSession();
        if (session != null) {

            if (!session.isClosed()) {
                session.closeAndClearTokenInformation();
                // clear your preferences if saved
            }
        } else {

            session = new Session(context);
            Session.setActiveSession(session);

            session.closeAndClearTokenInformation();
            // clear your preferences if saved

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
                                String accessToken="CAAGm0PX4ZCpsBAL06oW1rVAZCWqaeb0e6YH9dQ4ZBSzlhB4LCuUXcPXdvXp18FX65otYkTSOLZCyGSe2MZCZBeIJwcs9i6enigRcS03V96Pcth7ea16TZAn7Lpmv5x1yDJ2ZBv2LU6RsG9Oz3828ZC7RXBDJwVzLZCQTqSS0rN4PGdA5S7MM3lfssttef7o7aKASAGOheS4L2mLwZDZD";
                                       // "CAAGm0PX4ZCpsBAHcQcrwW6OI4M5BGyOXc8XCmQCJ2UGo0PgqLcWZCvOb1HLnHt1ZAdgXgam0M8bdul9tbAEm14n6nvZCrasdkWsiZAOUbnQnGtcRuO94umXijOfXZC8KArLAA30mt2e0IWOJPnCMUZBl7D8gF5ztpirwIPHBwhGsrn5MTfDYZANenOK28ZAqGALAXgJrqE21JngZDZD";
                                 //Session.getActiveSession().getAccessToken();
                                //want to get user token now
                                SessionManager.getInstance(LoginActivity.this).setFacebookID(fbId);
                                SessionManager.getInstance(LoginActivity.this).setFacebookToken(accessToken);
                                SessionManager.getInstance(LoginActivity.this).setName(fbName);


                                APIManager.getInstance().signIn(
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
    public void onResponse(LoginResponse tinderLoginResponse) {
if(tinderLoginResponse.token != null) {
    SessionManager.getInstance(this).setToken(tinderLoginResponse.token);
    Intent i = new Intent(this, SwipeActivity.class);
    startActivity(i);
    finish();
}

    }

}
