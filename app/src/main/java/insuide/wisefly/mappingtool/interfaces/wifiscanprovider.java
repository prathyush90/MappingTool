package insuide.wisefly.mappingtool.interfaces;

import android.net.wifi.ScanResult;

import java.util.List;

public interface wifiscanprovider {
    public void onWifiScanAvailable(List<ScanResult> wifiDetails);
    public void onWifiPassMessage(String message);
}
