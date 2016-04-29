package com.app.tao.juheweather.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.app.tao.juheweather.R;


/**
 * Created by Tao on 2016/4/29.
 */
public class Test extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        TextView textView = (TextView) findViewById(R.id.tv);
        Intent intent = getIntent();
        textView.setText(intent.getStringExtra("district_name"));
    }
}
