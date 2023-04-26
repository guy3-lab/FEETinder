// Catherine
package com.idk.feetinder;
import android.os.Handler;


public class Utils {

    // Delay mechanism

    public interface DelayCallback{
        void afterDelay();
    }

    public static void delay(long milli, final DelayCallback delayCallback){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                delayCallback.afterDelay();
            }
        }, milli); // afterDelay will be executed after (secs*1000) milliseconds.
    }
}
