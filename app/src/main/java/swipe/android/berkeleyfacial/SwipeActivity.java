package swipe.android.berkeleyfacial;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.bumptech.glide.Glide;
import com.edbert.library.utils.OptionsManager;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import swipe.android.berkeleyfacial.adapter.FacesAdapter;
import swipe.android.berkeleyfacial.api.tinder.TinderRecommendationResponse;
import swipe.android.berkeleyfacial.api.tinder.TrainingBodyResponse;
import swipe.android.berkeleyfacial.api.tinder.model.Photo;
import swipe.android.berkeleyfacial.managers.APIManager;
import swipe.android.berkeleyfacial.managers.SessionManager;
import swipe.android.berkeleyfacial.model.Data;
import swipe.android.berkeleyfacial.util.CSVFile;
import swipe.android.swipecardslib.SwipeFlingAdapterView;


public class SwipeActivity extends FacialActivity implements
        Response.Listener<TrainingBodyResponse> {

    public static FacesAdapter myAppAdapter;
    private ArrayList<Data> al;
    private HashMap<String, Boolean> likesDislikes;
    private SwipeFlingAdapterView flingContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe);

        flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);
        likesDislikes = new HashMap<>();
        al = new ArrayList<>();
        myAppAdapter = new FacesAdapter(al, this);
        flingContainer.setAdapter(myAppAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {

            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                al.remove(0);
                myAppAdapter.notifyDataSetChanged();

            }

            @Override
            public void onRightCardExit(Object dataObject) {

                //  likesDislikes.put(al.get(0), Boolean.valueOf(true));
                al.remove(0);
                myAppAdapter.notifyDataSetChanged();
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                if (itemsInAdapter == 0) {//send it off
                    sendTrainingResults();
                }

            }

            @Override
            public void onScroll(float scrollProgressPercent) {

                View view = flingContainer.getSelectedView();
                view.findViewById(R.id.background).setAlpha(0);
                view.findViewById(R.id.item_swipe_right_indicator).setAlpha(scrollProgressPercent < 0 ? -scrollProgressPercent : 0);
                view.findViewById(R.id.item_swipe_left_indicator).setAlpha(scrollProgressPercent > 0 ? scrollProgressPercent : 0);
            }
        });


        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {

                View view = flingContainer.getSelectedView();
                view.findViewById(R.id.background).setAlpha(0);

                myAppAdapter.notifyDataSetChanged();
            }
        });

        initDataSet();

    }
    public void sendTrainingResults() {
        onResponse(null);
     //   JSONObject body = new JSONObject(likesDislikes);
      //  APIManager.getInstance().submitTrainingData(body.toString(), this, this);
    }

    public void initDataSet() {
        InputStream inputStream = getResources().openRawResource(R.raw.training_data);
        CSVFile csvFile = new CSVFile(inputStream);

        List<String> scoreList = csvFile.read();
        for(String s : scoreList)
            al.add(new Data(s, "",false));
        myAppAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResponse(TrainingBodyResponse tinderRecommendationResponse) {
        //add data to adapter
        //go to tinderbot
        Intent i = new Intent(this, TinderBotActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.logout:
                SessionManager.getInstance(this).clearAll();
                APIManager.getInstance().stopQueue();
                Intent i = new Intent(this, SwipeActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                LoginActivity.callFacebookLogout(SwipeActivity.this);

                startActivity(i);
                finish();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
