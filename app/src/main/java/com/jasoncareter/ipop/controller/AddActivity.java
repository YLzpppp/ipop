package com.jasoncareter.ipop.controller;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.design.button.MaterialButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jasoncareter.ipop.R;
import com.jasoncareter.ipop.model.AppDataBase;
import com.jasoncareter.ipop.model.ContactItemObject;
import com.jasoncareter.ipop.model.ContactsEntity;
import com.jasoncareter.ipop.model.ContactsRecyclerViewAdapter;
import com.jasoncareter.ipop.model.TargetClientIP;
import com.jasoncareter.ipop.model.TargetFriend;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class AddActivity extends AppCompatActivity {

    private String outcome ;
    private String ip = "";
    private String result ="";
    private TextInputEditText editText;
    private TextView resulttext;
    private MaterialButton addbutton;
    private static final String baseUrl = "http://www.lzppp.cn/ipopServer/server/";
    private AppDataBase db = null ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        InitViews();

        db = AppDataBase.getInstance(getApplicationContext());

        addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                result = editText.getText().toString();

                final Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(baseUrl)
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .build();
                TargetFriend targetFriend = retrofit.create(TargetFriend.class);
                Call<String> addfriend = targetFriend.AddFriend(result);
                addfriend.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        String out = response.body().toString();
                        if (out.equals("true")){
                            //TODO:user exists , click to add

                                    new AlertDialog.Builder(AddActivity.this)
                                            .setTitle("Warning")
                                            .setMessage("Confirm Add ? ")
                                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.cancel();
                                                }
                                            })
                                            .setPositiveButton("Enter", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    //TODO : send adding request , update the database

                                                    Retrofit retrofit1 = new Retrofit.Builder()
                                                            .baseUrl(baseUrl)
                                                            .addConverterFactory(ScalarsConverterFactory.create())
                                                            .build();
                                                    TargetClientIP clientIP = retrofit1.create(TargetClientIP.class);
                                                    Call<String> getIP = clientIP.TargetIP(result);
                                                    getIP.enqueue(new Callback<String>() {
                                                        @Override
                                                        public void onResponse(Call<String> call, Response<String> response) {
                                                            ip = response.body().toString();
                                                        }

                                                        @Override
                                                        public void onFailure(Call<String> call, Throwable t) {

                                                        }
                                                    });

                                                    Observable.create(new ObservableOnSubscribe<ContactsEntity>() {
                                                        @Override
                                                        public void subscribe(ObservableEmitter<ContactsEntity> emitter) throws Exception {
                                                            ContactsEntity entity = new ContactsEntity();
                                                            entity.setUsername(result);
                                                            entity.setIdentity(Integer.valueOf(result));
                                                            entity.setIP(ip);
                                                            emitter.onNext(entity);
                                                            emitter.onComplete();
                                                        }
                                                    }).subscribeOn(AndroidSchedulers.mainThread())
                                                            .observeOn(Schedulers.io())
                                                            .subscribe(new Observer<ContactsEntity>() {
                                                                @Override
                                                                public void onSubscribe(Disposable d) {

                                                                }

                                                                @Override
                                                                public void onNext(ContactsEntity entity) {
                                                                    boolean more = false ;
                                                                    List<ContactsEntity> list = db.contactsDao().getAllContacts();
                                                                    for ( ContactsEntity entity1 : list){
                                                                        if(entity1.getUsername() == result){
                                                                            Toast.makeText(getApplicationContext() , "User's already in your lists",Toast.LENGTH_SHORT).show();
                                                                            more = true;
                                                                            break;
                                                                        }
                                                                    }
                                                                    if(!more)
                                                                        db.contactsDao().InsertOne(entity);
                                                                }

                                                                @Override
                                                                public void onError(Throwable e) {

                                                                }

                                                                @Override
                                                                public void onComplete() {
                                                                    (new ContactsRecyclerViewAdapter(db)).setDataChanged(true);

                                                                }
                                                            });

                                                    dialog.dismiss();
                                                }
                                            })
                                            .create()
                                    .show();

                        }else {
                            resulttext.setText("no match found");
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });

            }
        });
    }

   private void InitViews(){
        editText = findViewById(R.id.addinputedit);
        resulttext = findViewById(R.id.addresult);
        addbutton = findViewById(R.id.addbutton);
   }
}
