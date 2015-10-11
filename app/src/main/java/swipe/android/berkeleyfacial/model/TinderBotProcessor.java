package swipe.android.berkeleyfacial.model;

import android.app.Instrumentation;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.SystemClock;
import android.test.InstrumentationTestCase;
import android.test.TouchUtils;
import android.view.Display;
import android.view.MotionEvent;
import android.view.WindowManager;

public class TinderBotProcessor{
    public static void leftSwipe(Context c) {
        WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point mdispSize = new Point();
        display.getSize(mdispSize);
        int maxX = mdispSize.x;
        int maxY = mdispSize.y;
        int centerX = maxX /2;
        int centerY = maxY /2;
drag( centerX, 0, centerY, centerY, 20);
    }
    public static void drag(float fromX, float toX, float fromY,
                            float toY, int stepCount) {

        long downTime = SystemClock.uptimeMillis();
        long eventTime = SystemClock.uptimeMillis();

        float y = fromY;
        float x = fromX;

        float yStep = (toY - fromY) / stepCount;
        float xStep = (toX - fromX) / stepCount;

        MotionEvent event = MotionEvent.obtain(downTime, eventTime,
                MotionEvent.ACTION_DOWN, x, y, 0);

        for (int i = 0; i < stepCount; ++i) {
            y += yStep;
            x += xStep;
            eventTime = SystemClock.uptimeMillis();
            event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_MOVE, x, y, 0);

        }

        eventTime = SystemClock.uptimeMillis();
        event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_UP, x, y, 0);

    }
}