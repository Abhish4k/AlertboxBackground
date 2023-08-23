package com.example.alertincservice;
import androidx.appcompat.app.AppCompatActivity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;

public class MainActivity extends AppCompatActivity {
    private IncomingCallService mService;
    private boolean mBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Intent intent = new Intent(this , IncomingCallService.class);
        bindService(intent , mConnection , Context.BIND_AUTO_CREATE);

    }

    protected void onDestroy() {
        super.onDestroy();
        if(mBound){
            unbindService(mConnection);
            mBound = false;

        }
    }

    private final ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder iBinder) {
            IncomingCallService.LocalBinder binder = (IncomingCallService.LocalBinder) iBinder;
            mService = binder.getService();
            mBound = true ;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBound = false;

        }

    };

}