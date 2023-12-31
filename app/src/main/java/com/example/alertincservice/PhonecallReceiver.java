package com.example.alertincservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.Date;

public abstract class PhonecallReceiver extends BroadcastReceiver
{
    private static int lastState = TelephonyManager.CALL_STATE_IDLE;
    private static Date callStartTime;
    private static boolean isIncoming;
    private static String savedNumber;

    @Override
    public void onReceive(Context context, Intent intent)
    {
        try
        {
            if (intent.getAction().equals("android.intent.action.NEW_OUTGOING_CALL"))
            {
                savedNumber = intent.getExtras().getString("android.intent.extra.PHONE_NUMBER");
            }
            else
            {
                String stateStr = intent.getExtras().getString(TelephonyManager.EXTRA_STATE);
                String number = intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER);

                int state = 0;
                if(stateStr.equals(TelephonyManager.EXTRA_STATE_IDLE))
                {
                    state = TelephonyManager.CALL_STATE_IDLE;
                }
                else if(stateStr.equals(TelephonyManager.EXTRA_STATE_OFFHOOK))
                {
                    state = TelephonyManager.CALL_STATE_OFFHOOK;
                }
                else if(stateStr.equals(TelephonyManager.EXTRA_STATE_RINGING))
                {
                    state = TelephonyManager.CALL_STATE_RINGING;
                }


                Log.e("phoneState",""+ number);

                if(number != null){
                    onCallStateChanged(context, state, number);
                }

            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    //Derived classes should override these to respond to specific events of interest
    protected void onIncomingCallStarted(Context ctx, String number, Date start){}
    protected void onIncomingCallEnded(Context ctx, String number, Date start, Date end){}

    public void onCallStateChanged(Context context, int state, String number)
    {
        if(lastState == state)
        {
            //No change, debounce extras
            return;
        }
        switch (state)
        {
            case TelephonyManager.CALL_STATE_RINGING:
                isIncoming = true;
                callStartTime = new Date();
                savedNumber = number;
                onIncomingCallStarted(context, number, callStartTime);
                break;

            case TelephonyManager.CALL_STATE_OFFHOOK:
                if (isIncoming)
                {
                    onIncomingCallEnded(context,number,callStartTime,new Date());
                }

            case TelephonyManager.CALL_STATE_IDLE:
                if(isIncoming)
                {
                    onIncomingCallEnded(context, number, callStartTime, new Date());
                }
        }
        lastState = state;
    }
}
