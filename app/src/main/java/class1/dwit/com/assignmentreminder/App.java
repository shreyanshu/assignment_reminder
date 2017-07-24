package class1.dwit.com.assignmentreminder;

import android.app.Application;
import android.content.Context;

/**
 * Created by User on 5/4/2017.
 */

public class App extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static Context getContext(){
        return mContext;
    }
}
