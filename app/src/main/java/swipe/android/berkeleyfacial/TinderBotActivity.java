package swipe.android.berkeleyfacial;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import swipe.android.berkeleyfacial.adapter.FacesAdapter;
import swipe.android.berkeleyfacial.api.tinder.TinderRecommendationResponse;
import swipe.android.berkeleyfacial.api.tinder.model.People;
import swipe.android.berkeleyfacial.api.tinder.model.Photo;
import swipe.android.berkeleyfacial.managers.APIManager;
import swipe.android.berkeleyfacial.managers.SessionManager;
import swipe.android.berkeleyfacial.model.Data;
import swipe.android.berkeleyfacial.model.TinderBotProcessor;
import swipe.android.berkeleyfacial.util.CSVFile;
import swipe.android.swipecardslib.SwipeFlingAdapterView;


public class TinderBotActivity extends FacialActivity implements
        Response.Listener<TinderRecommendationResponse> {

    public static FacesAdapter myAppAdapter;
    private ArrayList<Data> al;
    private SwipeFlingAdapterView flingContainer;


    Timer timer;
    TimerTask timerTask;

    //we are going to use a handler to be able to run in our TimerTask
    final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe);

        flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);
        setTitle("Bot Mode");
        al = new ArrayList<>();
        //al = getIntent().getParcelableArrayListExtra("List");
        loadData();
        myAppAdapter = new FacesAdapter(al, this);
        flingContainer.setAdapter(myAppAdapter);

        myAppAdapter.notifyDataSetChanged();
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                al.remove(0);
                myAppAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {

            }

            @Override
            public void onRightCardExit(Object dataObject) {
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                if (itemsInAdapter == 0) {//send it off
                    loadData();
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
    }
    public void loadData() {

  //     APIManager.getInstance().recommendationTinder(this, this);
al.clear();
            InputStream inputStream = getResources().openRawResource(R.raw.training_data2);
            CSVFile csvFile = new CSVFile(inputStream);

            List<Data> scoreList = csvFile.read2();
            for(Data s : scoreList)
                al.add(s);

        Collections.shuffle(al);
        if(myAppAdapter != null)
            myAppAdapter.notifyDataSetChanged();

    }
    LinkedList<String> checkedIds = new LinkedList<>();
    @Override
    public void onResponse(TinderRecommendationResponse tinderRecommendationResponse) {
        //add data to adapter
checkedIds.clear();
        for(People p : tinderRecommendationResponse.peeps){
            ArrayList<Photo> k = p.photos;
            for(Photo l : k) {

                al.add(new Data(l.url, "", true));
            }
        }
        Collections.shuffle(al);
        myAppAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();

        //onResume we start our timer so it can start when the app comes from the background
        startTimer();
    }

    public void startTimer() {
        //set a new Timer
        timer = new Timer();

        //initialize the TimerTask's job
        initializeTimerTask();

        //schedule the timer, after the first 5000ms the TimerTask will run every 10000ms
        timer.schedule(timerTask, 2000, 2000); //
    }
@Override
public void onPause(){
    super.onPause();
    stoptimertask();
}
    public void stoptimertask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public void initializeTimerTask() {

        timerTask = new TimerTask() {
            public void run() {

                //use a handler to run a toast that shows the current timestamp
                handler.post(new Runnable() {
                    public void run() {
                        Data s = (Data) myAppAdapter.getItem(0);
                       Boolean b = s.isIsrecommend();
                        if(b) {
                            if(!checkedIds.contains(s.getId())){
                           // APIManager.getInstance().like(s.getId(), TinderBotActivity.this,TinderBotActivity.this);
                            checkedIds.add(s.getId());}
                            flingContainer.getTopCardListener().selectRight();
                            myAppAdapter.notifyDataSetChanged();
                        }
                        else {
                            if(!checkedIds.contains(s.getId())){
                           // APIManager.getInstance().dislike(s.getId(), TinderBotActivity.this, TinderBotActivity.this);
                                checkedIds.add(s.getId());}

                            flingContainer.getTopCardListener().selectLeft();
                            myAppAdapter.notifyDataSetChanged();

                        }

                    }
                });
            }
        };
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
              //  LoginActivity.callFacebookLogout(SwipeActivity.this);

                startActivity(i);
                finish();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
