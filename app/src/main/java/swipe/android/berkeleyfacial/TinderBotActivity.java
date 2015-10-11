package swipe.android.berkeleyfacial;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import swipe.android.berkeleyfacial.adapter.FacesAdapter;
import swipe.android.berkeleyfacial.api.tinder.TinderRecommendationResponse;
import swipe.android.berkeleyfacial.api.tinder.model.Photo;
import swipe.android.berkeleyfacial.managers.APIManager;
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

        al = new ArrayList<>();
        myAppAdapter = new FacesAdapter(al, this);
        flingContainer.setAdapter(myAppAdapter);
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
                if (itemsInAdapter == 3) {//send it off
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

        loadData();
    }
    public void loadData() {
      //
      //  APIManager.getInstance().recommendationTinder(this,this);

            InputStream inputStream = getResources().openRawResource(R.raw.training_data);
            CSVFile csvFile = new CSVFile(inputStream);

            List<String> scoreList = csvFile.read();
            for(String s : scoreList)
                al.add(new Data(s, "",false));
            myAppAdapter.notifyDataSetChanged();

    }
    @Override
    public void onResponse(TinderRecommendationResponse tinderRecommendationResponse) {
        //add data to adapter

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
                        if(b) {//   APIManager.getInstance().like(s.getId(), TinderBotActivity.this,TinderBotActivity.this);
                            flingContainer.getTopCardListener().selectRight();
                            myAppAdapter.notifyDataSetChanged();
                        }
                        else {  //APIManager.getInstance().dislike(s.getId(), TinderBotActivity.this,TinderBotActivity.this);

                            flingContainer.getTopCardListener().selectLeft();
                            myAppAdapter.notifyDataSetChanged();

                        }

                    }
                });
            }
        };
    }

}
