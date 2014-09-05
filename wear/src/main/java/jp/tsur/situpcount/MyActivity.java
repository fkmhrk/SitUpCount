package jp.tsur.situpcount;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MyActivity extends Activity implements SensorEventListener {

    private static final int STATUS_UP = 3;
    private static final int STATUS_DOWN = -7;

    @InjectView(R.id.text_view)
    TextView mTextView;

    private SensorManager mSensorManager;
    private Sensor mSensor;

    private boolean mStatusDown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        ButterKnife.inject(this);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    // Sensor
    @Override
    public void onSensorChanged(SensorEvent event) {
        // Z 軸の加速度
        float acceleration = event.values[2];
        mTextView.setText(String.valueOf((int) acceleration));

        if (mStatusDown) {
            if (acceleration > STATUS_UP) {
                // 起きた
                mStatusDown = false;
            }
        } else {
            if (acceleration < STATUS_DOWN) {
                // 寝た
                mStatusDown = true;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
