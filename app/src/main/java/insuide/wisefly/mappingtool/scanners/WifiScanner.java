package insuide.wisefly.mappingtool.scanners;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;

import insuide.wisefly.mappingtool.interfaces.wifiscanprovider;

public class WifiScanner {
    private Context mContext;
    private wifiscanprovider mListener;
    private WifiManager mwifiManager;
    private Handler handler;
    private Runnable runnable;
    private WifiManager.WifiLock wlock;

    public WifiScanner(Context context, wifiscanprovider listener) {
        this.mContext = context;
        this.mListener = listener;
        this.checkAndroidVersion();
        mwifiManager = (WifiManager)
                context.getSystemService(Context.WIFI_SERVICE);
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
              performWifiScan();
              handler.postDelayed(runnable, 500);
            }
        };

    }

    private void startWifiScanning(){
        handler.postDelayed(runnable, 0);
    }

    private void invalidate(){
        handler.removeCallbacksAndMessages(null);
        handler = null;
        runnable = null;
        if(wlock.isHeld()){
            wlock.release();
        }
    }

    private void checkAndroidVersion(){
        int version = android.os.Build.VERSION.SDK_INT;
        if(version >= Build.VERSION_CODES.O && version < Build.VERSION_CODES.Q){
            if(mListener != null){
                mListener.onWifiPassMessage("Device incompatible for scanning wifi");
            }
        }
        return;
    }

    private void performWifiScan(){

        mwifiManager.setWifiEnabled(false);
        wlock = mwifiManager.createWifiLock(WifiManager.WIFI_MODE_SCAN_ONLY,"mappertool");

        if(!wlock.isHeld()){
            wlock.acquire();
        }
        boolean status = mwifiManager.startScan();
        if(!status){
            mListener.onWifiPassMessage("Scan failed");
        }
    }

    BroadcastReceiver wifiScanReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context c, Intent intent) {
            boolean success = intent.getBooleanExtra(
                    WifiManager.EXTRA_RESULTS_UPDATED, false);
            if (success) {
                //scanSuccess();
            }
        }
    };
}