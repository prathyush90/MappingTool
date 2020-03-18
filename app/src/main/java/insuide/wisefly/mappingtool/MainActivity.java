package insuide.wisefly.mappingtool;

import androidx.appcompat.app.AppCompatActivity;
import insuide.wisefly.mappingtool.interfaces.wifiscanprovider;
import insuide.wisefly.mappingtool.scanners.WifiScanner;

import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity implements wifiscanprovider {
    private WifiScanner mScanner;
    private TextView mTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = findViewById(R.id.logText);
        mScanner = new WifiScanner(this, this);
        mScanner.startWifiScanning();
    }

    @Override
    public void onWifiScanAvailable(final List<ScanResult> wifiDetails) {
        String text = "";
        for(int i=0;i<wifiDetails.size();i++){
            ScanResult result = wifiDetails.get(i);
            String bssid = result.BSSID;
            int rssi = result.level;
            text += bssid +" -> "+rssi + "\n";
        }
        final String finalText = text;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //Toast.makeText(MainActivity.this, finalText, Toast.LENGTH_SHORT).show();
                mTextView.setText(finalText);
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
