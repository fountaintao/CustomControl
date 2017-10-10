package com.taoyong.customcontrol;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;

import com.android.internal.telephony.ITelephony;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class IncomingListenerActivity extends AppCompatActivity {
    private static final String TAG = "IncomingListenerActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incoming_listener);
        initListener();
    }

    private void initListener() {
        TelephonyManager telephony = (TelephonyManager) getApplication()
                .getSystemService(Context.TELEPHONY_SERVICE);
        telephony.listen(new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                switch (state) {
                    case TelephonyManager.CALL_STATE_RINGING:
                        Log.i(TAG, "onCallStateChanged: 来电：" + incomingNumber);
                        if (true) {
                            answerPhone();
                        } else {
                            endPhone();
                        }
                        break;
                    case TelephonyManager.CALL_STATE_IDLE:
                        Log.i(TAG, "onCallStateChanged: 挂断：" + incomingNumber);
                        break;
                    case TelephonyManager.CALL_STATE_OFFHOOK:
                        Log.i(TAG, "onCallStateChanged: 通话：" + incomingNumber);
                        break;
                }
                super.onCallStateChanged(state, incomingNumber);
            }
        }, PhoneStateListener.LISTEN_CALL_STATE);
    }

    private void endPhone() {
        Method method = null;
        try {
            method = Class.forName("android.os.ServiceManager").getMethod("getService", String.class);
            IBinder binder = (IBinder) method.invoke(null, new Object[]{"phone"});
            ITelephony telephony = ITelephony.Stub.asInterface(binder);
            telephony.endCall();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void answerPhone() {
        Method method = null;
        try {
            method = Class.forName("android.os.ServiceManager").getMethod("getService", String.class);
            IBinder binder = (IBinder) method.invoke(null, new Object[]{"phone"});
            ITelephony telephony = ITelephony.Stub.asInterface(binder);
            telephony.answerRingingCall();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (Exception e) {
            Log.e(TAG, "" + e, e);
            try {
                Runtime.getRuntime().exec("input keyevent " +
                        Integer.toString(KeyEvent.KEYCODE_HEADSETHOOK));
            } catch (IOException e2) {
                // Runtime.exec(String) had an I/O problem, try to fall back
                String enforcedPerm = "android.permission.CALL_PRIVILEGED";
                Intent btnDown = new Intent(Intent.ACTION_MEDIA_BUTTON).putExtra(
                        Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_DOWN,
                                KeyEvent.KEYCODE_HEADSETHOOK));
                Intent btnUp = new Intent(Intent.ACTION_MEDIA_BUTTON).putExtra(
                        Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_UP,
                                KeyEvent.KEYCODE_HEADSETHOOK));
                sendOrderedBroadcast(btnDown, enforcedPerm);
                sendOrderedBroadcast(btnUp, enforcedPerm);
            }
        }
    }
}