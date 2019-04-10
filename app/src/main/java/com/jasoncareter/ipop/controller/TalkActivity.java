package com.jasoncareter.ipop.controller;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jasoncareter.ipop.R;
import com.jasoncareter.ipop.model.AppDataBase;
import com.jasoncareter.ipop.model.Message;
import com.jasoncareter.ipop.model.MessageAdapter;
import com.jasoncareter.ipop.model.TargetClientIP;
import com.jasoncareter.ipop.model.TargetPort;
import com.jasoncareter.ipop.model.TargetStatus;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class TalkActivity extends AppCompatActivity {

    private static final String baseurl = "http://www.lzppp.cn/ipopServer/server/";
    private Button sendbt = null ;
    private RecyclerView mrecyclerview = null;
    private EditText editText = null ;
    private int TargetClientPort = 0;
    private String TargetIpAddress = "";
    private String data = "";
    private boolean networkFine = false ;
    private boolean TargetOnline = false;
    private Retrofit retrofit = null;
    private MessageAdapter messageAdapter = null ;
    private ArrayList<Message> arrayList = new ArrayList<>();
    private int thisport = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talk);

//        Log.i("tag", "onCreate: "+" 聊天界面, 获取的端口号现在为 ："+meInfor.getMePort());
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.flags = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS ;

        sendbt = findViewById(R.id.send);
        mrecyclerview = findViewById(R.id.msg_recycler_view);
        editText = findViewById(R.id.input_text);

        new Thread(new Runnable() {
            @Override
            public void run() {
                thisport = AppDataBase.getInstance(getApplicationContext()).meInfoDao().getAll().getMePort();
            }
        }).start();

        messageAdapter = new MessageAdapter();
        mrecyclerview.setAdapter(messageAdapter);
        mrecyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        String TargetUser = getIntent().getStringExtra("TargetClient");


        /**
         *  get target client's Port for UDP socket
         */
         retrofit = new Retrofit.Builder()
                .baseUrl(baseurl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        TargetPort targetPort = retrofit.create(TargetPort.class);
        TargetStatus targetStatus = retrofit.create(TargetStatus.class);
        TargetClientIP targetClientIP = retrofit.create(TargetClientIP.class);

        Call<String> getport = targetPort.getTargetPort(TargetUser);
        Call<String> getstatus = targetStatus.getTargetStatus(TargetUser);
        Call<String> getIP = targetClientIP.TargetIP(TargetUser);

        getport.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String s = response.body();
                if(s.equals("no match")){
                    Toast.makeText(getApplicationContext(),"network error",Toast.LENGTH_SHORT).show();
                    networkFine = false;
                }else {
                    TargetClientPort = Integer.valueOf(s);
                    networkFine = true;
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) { }
        });

        getstatus.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String s = response.body();
                int b = Integer.valueOf(s);
                if(b == 1){
                    TargetOnline = true;
                }else{
                    TargetOnline = false;
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) { }
        });

        getIP.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String ip = response.body();
                TargetIpAddress = ip;
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) { }
        });


        /*
         *  Receive event
         */
//        ReceiveAsync receiveAsync = new ReceiveAsync(thisport);
//        receiveAsync.Execute();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    byte[] bytes = new byte[1024];
                    Log.i("tag", "Execute:  "+"  接受线程执行中 ，等待接受消息");
                    DatagramSocket datagramSocket = new DatagramSocket(50555);
                    Log.i("tag", "Execute:  "+" 创建socket完毕 ");
                    DatagramPacket datagramPacket = new DatagramPacket(bytes ,bytes.length);

                    datagramSocket.receive(datagramPacket);
                    Log.i("tag", "run: "+bytes.toString());
                    bytes = datagramPacket.getData();
                    String str = new String(bytes ,0 ,bytes.length);
                    Log.i("tag", "Execute: "+"获取消息为 ："+str);
                    MessageAdapter adapter = new MessageAdapter();
                    Log.i("tag", "Execute: "+"消息列表适配器准备更新内容 ,当前消息数为 : "+adapter.getItemCount());
                    adapter.addMessage( (new Message(str ,Message.TYPE_RECEIVED) ) , (adapter.getItemCount()-1 ) );
                    Log.i("tag", "Execute: "+"更新完成，现在数据总数为: "+adapter.getItemCount());
                    datagramSocket.close();
                } catch (SocketException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        /*
         *  Send event
         */
        sendbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                 *  first of all , we need to get data from input edit
                 */
                String output = editText.getText().toString();

                // then we call the SendAsync function to send data
                if( !output.equals(""))
                    new SendAsync().execute(output,TargetIpAddress,
                            String.valueOf(TargetClientPort) ,String.valueOf(thisport ));
                    mrecyclerview.scrollToPosition(messageAdapter.getItemCount()-1);
                    editText.setText("");

            }
        });



    }
}

/**
 *  As for sending messages through UDP , every time new data passed in this SendAsync class should
 *  packet it into a new    @Param DatagramPacket
 */
class SendAsync extends AsyncTask<String,Integer ,String>{

    String d ;String i; int p; int l ;


    MessageAdapter adapter = new MessageAdapter();
    @Override
    protected void onPreExecute() {
        /**
         *  tasks here will be done in UIThread
         *
         * @Param mePort { represent this client's port }
         */

        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... strings) {
        /**
         * @Param data
         * @Param length
         * @Param InetAddress
         * @Param port , this port represents TargetClient's port
         */

       try {

            String data = strings[0];
            String ip = strings[1];
            int port = Integer.valueOf(strings[2]);
            int thisport = Integer.valueOf(strings[3]);
            int dataLength = data.length();

        Log.i("tag", "doInBackground: "+"发送消息，对方IP地址为 : "+ip+"  端口为 : "+port);

            InetAddress inetAddress = InetAddress.getByName(ip);

            // packet's fourth param @Param port should be this client's Port instead of TargetClient's port
           Log.i("tag", "doInBackground: "+"发送的消息内容为 : "+data);
            DatagramPacket packet = new DatagramPacket(data.getBytes(),data.getBytes().length,inetAddress,port);
            // data's ready , prepare for sending
           Log.i("tag", "doInBackground: "+"本机端口为 ："+thisport);
            DatagramSocket socket = new DatagramSocket(thisport);
            socket.send(packet);

            socket.close();

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        int length = adapter.getItemCount();
        adapter.addMessage(new Message(d ,Message.TYPE_SENT),length);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }
}

/**
 *  receive async  , keeping receive data from target client
 */

class ReceiveAsync{
    private Object a = new Object();
    private Object b = new Object();
    private int port ;

    public ReceiveAsync( int port ) {
        this.port = port;
    }
    public void Execute() {
        Log.i("tag", "Execute: "+"接受线程启动 ，本机端口为："+port);
        new Thread(new ReceiveRun(a,b ,port)).start();
        new Thread(new ReceiveRun(b,a , port)).start();
    }
}

class ReceiveRun implements Runnable{

    private Object o1;
    private Object o2;
    private int mePort = 0;
    private byte[]  bytes = new byte[1024];

    public ReceiveRun(Object o1 ,Object o2 ,int meport) {
        this.o1 = o1;
        this.o2 = o2;
        this.mePort = meport;
    }
    @Override
    public void run() {
        Log.i("tag", "run: "+"接受runnable run方法开始执行");
        while(true) {
            synchronized(o1) {
                synchronized(o2) {
                    try {
                        Log.i("tag", "Execute:  "+o1.toString()+"  接受线程执行中 ，等待接受消息");
                        DatagramSocket datagramSocket = new DatagramSocket(mePort);
                        Log.i("tag", "Execute:  "+o1.toString()+" 创建socket完毕 ");
                        DatagramPacket datagramPacket = new DatagramPacket(bytes ,bytes.length);

                        datagramSocket.receive(datagramPacket);

                        byte[] bytes = datagramPacket.getData();
                        String str = new String(bytes ,0 ,bytes.length);
                        Log.i("tag", "Execute: "+"获取消息为 ："+str);
                        MessageAdapter adapter = new MessageAdapter();
                        Log.i("tag", "Execute: "+"消息列表适配器准备更新内容 ,当前消息数为 : "+adapter.getItemCount());
                        adapter.addMessage( (new Message(str ,Message.TYPE_RECEIVED) ) , (adapter.getItemCount()-1 ) );
                        Log.i("tag", "Execute: "+"更新完成，现在数据总数为: "+adapter.getItemCount());
                        datagramSocket.close();
                    } catch (SocketException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    o2.notify();
                }
                try {
                    o1.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private void receiveEvent(){

    }
}

