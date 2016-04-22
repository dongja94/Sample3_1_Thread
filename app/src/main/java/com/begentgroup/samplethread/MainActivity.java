package com.begentgroup.samplethread;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    ProgressBar progressView;

    private static final int MESSAGE_PROGRESS = 1;
    private static final int MESSAGE_COMPLETE = 2;

    Handler mHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MESSAGE_PROGRESS :
                    int progress = msg.arg1;
                    textView.setText("progress : " + progress);
                    progressView.setProgress(progress);
                    break;
                case MESSAGE_COMPLETE :
                    textView.setText("download completed");
                    progressView.setProgress(100);
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView)findViewById(R.id.text_message);
        progressView = (ProgressBar)findViewById(R.id.progress_download);
        progressView.setMax(100);

        Button btn = (Button)findViewById(R.id.btn_start);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDownload();
            }
        });
    }

    class ProgressRunnable implements Runnable {
        int progress;
        public ProgressRunnable(int progress) {
            this.progress = progress;
        }
        @Override
        public void run() {
            textView.setText("progress : " + progress);
            progressView.setProgress(progress);
        }
    }

    class CompleteRunnable implements Runnable {

        @Override
        public void run() {
            textView.setText("download completed");
            progressView.setProgress(100);
        }
    }

    private void startDownload() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int progress = 0;
                while(progress <= 100) {
//                    textView.setText("progress : " + progress);
//                    progressView.setProgress(progress);

//                    Message msg = mHandler.obtainMessage(MESSAGE_PROGRESS, progress, 0);
//                    mHandler.sendMessage(msg);
                    mHandler.post(new ProgressRunnable(progress));

                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    progress+=5;
                }
//                textView.setText("download completed");
//                progressView.setProgress(100);

//                mHandler.sendEmptyMessage(MESSAGE_COMPLETE);
                mHandler.post(new CompleteRunnable());

            }
        }).start();
    }
}
