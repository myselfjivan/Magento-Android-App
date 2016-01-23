package in.co.mrfoody.mrfoody.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by om on 20/1/16.
 */
public class mrfoodySer extends Service{
    private static String TAG = "co.in.mrfoody.Service";
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        super.onStart(intent, startId);
        Log.d(TAG, "FirstService started");
        this.stopSelf();
        return super.onStartCommand(intent, flags, startId);
    }

}
