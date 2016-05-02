package com.example.user.onlyltebczes;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MainLTEStatus extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_ltestatus);

        final Handler h = new Handler();
        final int delay = 5000; //milliseconds

        h.postDelayed(new Runnable(){
            public void run(){
                TextView networkStatus = (TextView)findViewById(R.id.networkStatus);
                networkStatus.setText(getNetworkClass());
                h.postDelayed(this, delay);
            }
        }, delay);

    }

    public void buttonOnClick(View v) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Button onOffButton = (Button) v;
        if(((Button) v).getText().equals("Run")) {
            startActivity(new Intent(MainLTEStatus.this, willrefactorLater.class ));
        } else {
           ((Button) v).setText("Run");
        }
    }

    public String getNetworkClass() {
        TelephonyManager mTelephonyManager =(TelephonyManager)getSystemService(TELEPHONY_SERVICE);
        int networkType = mTelephonyManager.getNetworkType();
        switch (networkType) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return "2G";
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return "3G";
            case TelephonyManager.NETWORK_TYPE_LTE:
                return "4G";
            default:
                return "Unknown";
        }
    }

    public void getState() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method dataConnSwitchmethod;
        Class telephonyManagerClass;
        Object ITelephonyStub;
        Class ITelephonyClass;
        boolean isEnabled;
        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);

        if(telephonyManager.getDataState() == TelephonyManager.DATA_CONNECTED){
            isEnabled = true;
        }else{
            isEnabled = false;
        }

        telephonyManagerClass = Class.forName(telephonyManager.getClass().getName());
        Method getITelephonyMethod = telephonyManagerClass.getDeclaredMethod("getITelephony");
        getITelephonyMethod.setAccessible(true);
        ITelephonyStub = getITelephonyMethod.invoke(telephonyManager);
        ITelephonyClass = Class.forName(ITelephonyStub.getClass().getName());

        if (isEnabled) {
            dataConnSwitchmethod = ITelephonyClass
                    .getDeclaredMethod("disableDataConnectivity");
        } else {
            dataConnSwitchmethod = ITelephonyClass
                    .getDeclaredMethod("enableDataConnectivity");
        }
        dataConnSwitchmethod.setAccessible(true);
        dataConnSwitchmethod.invoke(ITelephonyStub);
    }

    private void setMobileDataEnabled(Context context, boolean enabled) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        final ConnectivityManager conman = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final Class conmanClass = Class.forName(conman.getClass().getName());
        final Field iConnectivityManagerField = conmanClass.getDeclaredField("mService");
        iConnectivityManagerField.setAccessible(true);
        final Object iConnectivityManager = iConnectivityManagerField.get(conman);
        final Class iConnectivityManagerClass = Class.forName(iConnectivityManager.getClass().getName());
        final Method setMobileDataEnabledMethod = iConnectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
        setMobileDataEnabledMethod.setAccessible(true);

        setMobileDataEnabledMethod.invoke(iConnectivityManager, enabled);
    }

}
