package com.jasoncareter.ipop.controller;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jasoncareter.ipop.R;
import com.jasoncareter.ipop.model.Allports;
import com.jasoncareter.ipop.model.AppDataBase;
import com.jasoncareter.ipop.model.LoginCheck;
import com.jasoncareter.ipop.model.MeInfoEntity;
import com.jasoncareter.ipop.model.Myport;
import com.jasoncareter.ipop.model.RegisterUser;
import com.jasoncareter.ipop.model.TargetPort;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private static final String baseurl = "http://www.lzppp.cn/ipopServer/server/";
    private TextInputLayout userlayout;
    private TextInputEditText useredit;
    private TextInputEditText passwordedit;
    private TextInputLayout passwordlayout;
    private TextView register;
    private Button login;
    public String MyIdentity = "";
    private static final Object a = new Object();
    private static final Object b = new Object();
    private AppDataBase db ;
    private int mport ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        db = AppDataBase.getInstance(getApplicationContext());
        initViews();

        Regist();
        Login();
    }



    void initViews(){
        useredit = findViewById(R.id.un_edit_text);
        passwordedit = findViewById(R.id.pd_edit_text);
        userlayout = findViewById(R.id.login_frame_un_layout);
        register = findViewById(R.id.register);
        passwordlayout = findViewById(R.id.login_frame_pd_layout);
        login = findViewById(R.id.login);
    }

    private void Login(){
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( UserValide()&&PasswdValide()){
                    final String u = getUser();
                    String p = getPasswd();
                    Map<String ,String > m = new HashMap<>();
                    m.put("username",u);
                    m.put("password",p);
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(baseurl)
                            .addConverterFactory(ScalarsConverterFactory.create())
                            .build();
                    LoginCheck loUser = retrofit.create(LoginCheck.class);
                    TargetPort targetPort = retrofit.create(TargetPort.class);

                    Call<String> lUser = loUser.LoginToServer(m);
                    Call<String> mePort = targetPort.getTargetPort(u);

                    mePort.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            String port = response.body();
                            mport = Integer.valueOf(port);

//                            Log.i("tag", "onResponse: "+"登录成功，本机端口号为: "+p);
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {

                        }
                    });

                    lUser.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            String s = response.body();
                            switch (s){
                                case "right":
                                    MyIdentity = u ;
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Log.i("tag", "run: "+"获取到的端口号为"+mport);
                                            int i = mport ;
                                            Log.i("tag", "run: "+" i :"+i);
                                            db.meInfoDao().insert(new MeInfoEntity(u , i));
                                        }
                                    }).start();
                                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(i);
                                    break;
                                case "wrong":
                                    passwordlayout.setHelperTextEnabled(true);
                                    passwordlayout.setHelperText("账号密码出错，请检查");
                                    break;
                                case "account not exists":
                                    userlayout.setHelperTextEnabled(true);
                                    userlayout.setHelperText("账号不存在 ");
                                    break;
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {

                        }
                    });
                }
            }
        });
    }
    private void Regist(){
        register.setClickable(true);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (UserValide() && PasswdValide()){

                    new Thread(new Runnable() {
                        Myport myport = new Myport();
                        @Override
                        public void run() {
                            synchronized (a){
                                synchronized (b){
                                    Log.i("tag", "run: "+" 线程 1    更新port线程正在执行 ");
                                    HttpURLConnection connection = null ;
                                    try {
                                        URL url = new URL(baseurl+"AllPorts");
                                        connection = (HttpURLConnection) url.openConnection();
                                        connection.setRequestMethod("GET");
                                        connection.setReadTimeout(3000);
                                        connection.setConnectTimeout(3000);

                                        InputStream in = connection.getInputStream();
                                        byte[] bytes = new byte[1024];
                                        StringBuilder builder = new StringBuilder();
                                        int len = 0;
                                        while( (len = in.read(bytes))!=-1){
                                            builder.append(new String(bytes , 0 , len));
                                        }
                                        String result = builder+"";
                                        int t = myport.getPort();
                                        int temp = t;
                                        if(result.trim() != "") {
                                            String[] list  =result.split(",");
                                            for (int i = 0; i < list.length; i++) {
                                                temp = Integer.valueOf(list[i]);
                                                if ( t < temp)
                                                    t = temp;
                                                Log.i("tag", " 线程 1     onResponse: "+temp);
                                            }
                                        }
                                        myport.setPort(++t);

                                    } catch (MalformedURLException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    Log.i("tag", " 线程 1    onResponse: "+"线程1更新完成，最终值为"+myport.getPort());
                                    b.notify();
                                }
                                try {
                                    a.wait();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }).start();

                    new Thread(new Runnable() {
                        private Myport myport = new Myport();
                        @Override
                        public void run() {
                            synchronized (b){
                                synchronized (a){
                                    Log.i("tag ", "线程 2     run: "+" 注册线程开始执行 ");
                                    String  u = getUser();
                                    String  p = getPasswd();

                                    Log.i("tag", "线程 2     onClick: "+" 端口值 is now : "+myport.getPort());
                                    Retrofit retrofit = new Retrofit.Builder()
                                            .baseUrl(baseurl)
                                            .addConverterFactory( ScalarsConverterFactory.create())
                                            .build();
                                    RegisterUser reUser = retrofit.create(RegisterUser.class);
                                    Call<String> rUser = reUser.registerToserver(u,p,myport.getPort());

                                    rUser.enqueue(new Callback<String>() {
                                        @Override
                                        public void onResponse(Call<String> call, Response<String> response) {
                                            String s  =  response.body();
                                            if ( s == "already exists"){
                                                Toast.makeText(getApplicationContext(),"已存在该用户",Toast.LENGTH_SHORT).show();
                                            }else{
                                                Toast.makeText(getApplicationContext(),"注册成功",Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        @Override
                                        public void onFailure(Call<String> call, Throwable t) {
                                            Toast.makeText(getApplicationContext(),"Unknown Error",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    a.notify();
                                }
                                try {
                                    b.wait();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }).start();

                }
            } //end of onClick()
        });

    }

    private String getUser(){
        return useredit.getText().toString();
    }
    private boolean UserValide(){
        int len = useredit.getText().length();
        if(len!=6 && useredit.getText().toString().trim() != ""){
            userlayout.setErrorEnabled(true);
            userlayout.setErrorTextColor(ColorStateList.valueOf( Color.RED));
            userlayout.setError("At Least 6 Words And No Whitespace");
            return false;
        }
        return true;
    }
    private String getPasswd(){
        return passwordedit.getText().toString();
    }

    private void getPort( ){
        final Myport myport = new Myport();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseurl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        Allports allports = retrofit.create(Allports.class);
        Call<String> getall = allports.getAllPorts();
        getall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if ( response.body() != null) {
                    String str = response.body();
                    String[] ports = str.split(",");
                    int t = myport.getPort();
                    int temp = t;
                    for (int i = 0; i < ports.length; i++) {
                        temp = Integer.valueOf(ports[i]);
                        if ( t < temp)
                            t = temp;
                        Log.i("tag", "onResponse: "+temp);
                    }
                    t++;
                    myport.setPort(t);
                    Log.i("tag", "getPort: "+"修改port函数执行完成，端口值现在为 t : "+myport.getPort());
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });
    }

    private boolean PasswdValide(){
        int len = useredit.getText().length();
        if(len < 6){
            userlayout.setErrorEnabled(true);
            userlayout.setErrorTextColor(ColorStateList.valueOf( Color.RED));
            userlayout.setError("Password's length should be at least 6 ");
            return false;
        }
        return true;
    }

}

