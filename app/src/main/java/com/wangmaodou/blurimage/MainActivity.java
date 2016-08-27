package com.wangmaodou.blurimage;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    BlurImageView mBlurImageView;
    SeekBar mSeekBar;
    TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBlurImageView=(BlurImageView)findViewById(R.id.main_blurimageview);
        mSeekBar=(SeekBar) findViewById(R.id.main_seekbar);
        mTextView=(TextView)findViewById(R.id.mian_textview);

        mBlurImageView.setImage(BitmapFactory.decodeResource(getResources(),R.drawable.me));

        mSeekBar.setMax(100);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                int p=seekBar.getProgress();
                mBlurImageView.setBlurRadius(p);
                L.d("Seekbar===="+p);
                mTextView.setText("图片的模糊程度是："+p+"%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
