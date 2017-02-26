package com.example.administrator.okhttptest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.BitmapCallback;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "download";
    private Button btnOkhttp,btnOkhttpUtils,btnDown;
    private ImageView mImageView;
    private TextView mTextView;
    private ProgressBar mProgressBar;
    private String urlstr="http://a1.gdcp.cn/index.shtml";
    private String urlimg="http://upload.qlwb.com.cn/2017/0204/1486200485129.jpg";
    private String urlimg1="https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1487918901399&di=4a337672151361df35112c07a6482734&imgtype=0&src=http%3A%2F%2Fpic.3h3.com%2Fup%2F2016-8%2F201688331334551289.jpg";
    private String urlDownload="http://dlsw.baidu.com/sw-search-sp/soft/01/23623/FeiQ.1060559168.exe";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

    }

    private void initViews() {
        btnOkhttp= (Button) findViewById(R.id.btn_okhttp);
        btnOkhttpUtils= (Button) findViewById(R.id.btn_okhttpUtils);
        btnDown= (Button) findViewById(R.id.btn_down);
        mImageView= (ImageView) findViewById(R.id.iv_image);
        mTextView= (TextView) findViewById(R.id.tv_content);
        mProgressBar= (ProgressBar) findViewById(R.id.progressbar);

        btnDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownLoadFile();
            }
        });

        btnOkhttp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //使用Okhttp加载网站代码
                useOkhttpDisplayPage();
                //使用Okhttp加载图片
                useOkhttpDisplayImage();
            }
        });
        btnOkhttpUtils.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //使用OkhttpUtils加载网站代码
                useOkhttpUtilsDisplayPage();
                //使用Okhttputils加载图片
                useOkhttpUtilsDisplayImage();
            }
        });

    }

    private void useOkhttpUtilsDisplayImage() {
        OkHttpUtils
                .get()//
                .url(urlimg1)//
                .build()//
                .execute(new BitmapCallback() {
                    @Override
                    public void onError(okhttp3.Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(Bitmap response, int id) {
                        mImageView.setImageBitmap(response);
                    }
                });


    }

    private void DownLoadFile() {
        OkHttpUtils
                .get()
                .url(urlDownload)
                .build().execute(new FileCallBack(Environment.getExternalStorageDirectory().getAbsolutePath(),"zhang.exe") {
            @Override
            public void onError(okhttp3.Call call, Exception e, int id) {
                Log.e(TAG, "onError :" + e.getMessage());

            }

            @Override
            public void onResponse(File file, int id) {
                Log.e(TAG, "onResponse :" + file.getAbsolutePath());
            }

            @Override
            public void inProgress(float progress, long total, int id) {
                mProgressBar.setVisibility(View.VISIBLE);
                mProgressBar.setProgress((int) (100*progress));
            }
        });

    }

    private void useOkhttpUtilsDisplayPage() {
        OkHttpUtils.get().url(urlstr).build().execute(new StringCallback() {
            @Override
            public void onError(okhttp3.Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                mTextView.setText(response);
            }
        });
    }


    private void useOkhttpDisplayImage() {
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder().url(urlimg).build();
        Call call=client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }
            @Override
            public void onResponse(Response response) throws IOException {
                final byte[] b=response.body().bytes();
                final Bitmap bitmap= BitmapFactory.decodeByteArray(b,0,b.length);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mImageView.setImageBitmap(bitmap);
                    }
                });
            }
        });
    }

    private void useOkhttpDisplayPage() {
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder().url(urlstr).build();
        Call call=client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }
            @Override
            public void onResponse(Response response) throws IOException {
                final String content=response.body().string();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mTextView.setText(content);
                    }
                });
            }
        });
    }




    }
