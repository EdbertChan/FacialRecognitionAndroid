package swipe.android.berkeleyfacial;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class FacialActivity extends AppCompatActivity implements Response.ErrorListener {
    View myView;

    protected ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    protected void hideKeyboard(View view) {
        View focusedView = this.getCurrentFocus();
        if (focusedView != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    protected void showPopupProgress(final boolean show) {
        if (show) {
            if(dialog == null) {
                dialog = new ProgressDialog(this);
                dialog.setMessage("Loading...");
                dialog.show();
            }
        } else {
            if (dialog != null) {

                dialog.dismiss();
                dialog = null;
            }
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {try {
        String bytesAsString = new String(error.networkResponse.data, "UTF-8");
        Log.d("abd", "Error: " + error
                + ">>" + error.networkResponse.statusCode
                + ">>" + bytesAsString
                + ">>" + error.getCause()
                + ">>" + error.getMessage());
        new AlertDialog.Builder(this)
                .setTitle(this.getString(R.string.network_error_title))
                .setMessage(this.getString(R.string.network_error_message))
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        dialog.cancel();
                    }
                })
                .show();
    }catch(Exception e){}
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        onBackPressed();

        return true;
    }

    protected void goHomeClearStack() {
        /*Intent i = new Intent(this, HomeActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();*/
    }

    protected void timedDialog(String message) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this)
                .setMessage(message);

        final AlertDialog alert = dialog.create();
        alert.show();

// Hide after some seconds
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (alert.isShowing()) {
                    alert.dismiss();
                }
            }
        };

        alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                handler.removeCallbacks(runnable);
            }
        });

        handler.postDelayed(runnable, 1000);
    }

    public void setupUIKeyboardHide(){
        setupUI(getWindow().getDecorView().findViewById(android.R.id.content));
    }
    public void setupUI(View view) {

        //Set up touch listener for non-text box views to hide keyboard.
        if(!(view instanceof EditText)) {

            view.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
                    FacialActivity.this.hideKeyboard(FacialActivity.this.getCurrentFocus());
                    return false;
                }

            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {

            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

                View innerView = ((ViewGroup) view).getChildAt(i);

                setupUI(innerView);
            }
        }
    }
}