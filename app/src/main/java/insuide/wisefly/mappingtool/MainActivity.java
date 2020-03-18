package insuide.wisefly.mappingtool;

import androidx.appcompat.app.AppCompatActivity;
import insuide.wisefly.mappingtool.interfaces.wifiscanprovider;
import insuide.wisefly.mappingtool.scanners.WifiScanner;

import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity implements wifiscanprovider {
    private WifiScanner mScanner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mScanner = new WifiScanner(this, this);
        mScanner.startWifiScanning();
    }

    @Override
    public void onWifiScanAvailable(final List<ScanResult> wifiDetails) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, wifiDetails.size()+ " number of devices found", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onWifiPassMessage(String message) {
        Toast.makeText(MainActivity.this, "Scan error", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mScanner != null){
            mScanner.invalidate();
        }
    }
}
