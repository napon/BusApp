package com.napontaratan.BusApp.view;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import com.napontaratan.BusApp.R;

/**
 * Created by Napon Taratan on 2014-06-16.
 */
public class AlarmView extends Activity {
    @Override
    public void onCreate(Bundle onSavedState){
        super.onCreate(null);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.alarm);

        TextView text = (TextView) findViewById(R.id.alarm_dialog);
        text.setText("Approaching stop: " + getIntent().getStringExtra("location_name"));

        final Vibrator vibrator;
        vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);

        vibrator.vibrate(5000);

        Button dismiss = (Button) findViewById(R.id.dismiss);
        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.cancel();
                finish();
            }
        });
    }
}
