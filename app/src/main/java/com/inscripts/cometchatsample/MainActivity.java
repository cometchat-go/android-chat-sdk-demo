package com.inscripts.cometchatsample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.inscripts.interfaces.Callbacks;
import com.inscripts.interfaces.LaunchCallbacks;

import org.json.JSONObject;

import cometchat.inscripts.com.cometchatcore.coresdk.CometChat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private String licenseKey = "COMETCHAT-XXXXX-XXXXX-XXXXX-XXXXX"; // Replace the value with your CometChat License Key here
    private String apiKey = "xxxxxxxxxxxxxxxxxxxxxx"; // Replace the value with your CometChat Api Key here
    private String UID1 = "SUPERHERO1";
    private String UID2 = "SUPERHERO2";
    private boolean isCometOnDemand = true;
    private CometChat cometChat;


    private Button btnLoginSuperHero1, btnLoginSuperHero2, btnLaunchChat, btnInitializeChat;
    private ProgressBar pbLoading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cometChat = CometChat.getInstance(MainActivity.this);
        setUpFields();

    }

    private void setUpFields() {
        btnLoginSuperHero1 = findViewById(R.id.btnLoginSuperHero1);
        btnLoginSuperHero2 = findViewById(R.id.btnLoginSuperHero2);
        btnLaunchChat = findViewById(R.id.btnLaunchChat);
        btnInitializeChat = findViewById(R.id.btnInitialize);
        pbLoading = findViewById(R.id.pb_loading);
        btnLoginSuperHero1.setOnClickListener(this);
        btnLoginSuperHero2.setOnClickListener(this);
        btnLaunchChat.setOnClickListener(this);
        btnInitializeChat.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnInitialize:
                initializeChat();
                break;

            case R.id.btnLoginSuperHero1:
                login(UID1);
                break;

            case R.id.btnLoginSuperHero2:
                login(UID2);
                break;

            case R.id.btnLaunchChat:
                launchChat();
                break;


        }
    }

    /**
     * Initializes the Chat SDK.Initialization binds the SDK to your app and syncs the various basic parameters required for the CometChat SDK to function.
     */
    private void initializeChat() {

        if (licenseKey != null && !TextUtils.isEmpty(licenseKey)) {

            if (apiKey != null && !TextUtils.isEmpty(apiKey)) {
                showLoading(true);
                cometChat.initializeCometChat("", licenseKey, apiKey, isCometOnDemand, new Callbacks() {
                    @Override
                    public void successCallback(JSONObject jsonObject) {
                        Log.d(TAG, "Initialize Success : " + jsonObject.toString());
                        Toast.makeText(MainActivity.this, "CometChat initialized successfully", Toast.LENGTH_LONG).show();
                        btnLoginSuperHero1.setEnabled(true);
                        btnLoginSuperHero2.setEnabled(true);
                        showLoading(false);
                    }

                    @Override
                    public void failCallback(JSONObject jsonObject) {
                        Log.d(TAG, "Initialize Fail : " + jsonObject.toString());
                        Toast.makeText(MainActivity.this, "Initialize Failed with error: " + jsonObject.toString(), Toast.LENGTH_LONG).show();
                        showLoading(false);
                    }
                });
            } else {
                Toast.makeText(MainActivity.this, "API Key cannot be null or empty", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(MainActivity.this, "License Key cannot be null or empty", Toast.LENGTH_LONG).show();
        }

    }
    /**
     * Logs the user in to chat.
     *
     * @param UID - ID of the user to log in to the chat.
     *
     */
    private void login(String UID) {
        if (UID != null && !TextUtils.isEmpty(UID)) {
            showLoading(true);
            cometChat.loginWithUID(MainActivity.this, UID, new Callbacks() {
                @Override
                public void successCallback(JSONObject jsonObject) {
                    Log.d(TAG, "Login Success : " + jsonObject.toString());
                    Toast.makeText(MainActivity.this, jsonObject.toString(), Toast.LENGTH_LONG).show();
                    btnLaunchChat.setEnabled(true);
                    showLoading(false);
                }

                @Override
                public void failCallback(JSONObject jsonObject) {
                    Log.d(TAG, "Login Fail : " + jsonObject.toString());
                    Toast.makeText(MainActivity.this, jsonObject.toString(), Toast.LENGTH_LONG).show();
                    showLoading(false);
                }
            });
        } else {
            Toast.makeText(MainActivity.this, "UID be null or empty", Toast.LENGTH_LONG).show();
        }

    }

    /**
     *  Launches the chat.
     */
    private void launchChat() {

        cometChat.launchCometChat(MainActivity.this, true, new LaunchCallbacks() {
            @Override
            public void successCallback(JSONObject jsonObject) {
                Log.d(TAG, "Launch Success : " + jsonObject.toString());
            }

            @Override
            public void failCallback(JSONObject jsonObject) {
                Log.d(TAG, "Launch Fail : " + jsonObject.toString());
            }

            @Override
            public void userInfoCallback(JSONObject jsonObject) {
                Log.d(TAG, "User Info Received : " + jsonObject.toString());
            }

            @Override
            public void chatroomInfoCallback(JSONObject jsonObject) {
                Log.d(TAG, "Chatroom Info Received : " + jsonObject.toString());
            }

            @Override
            public void onMessageReceive(JSONObject jsonObject) {
                Log.d(TAG, "Message Received : " + jsonObject.toString());
            }

            @Override
            public void error(JSONObject jsonObject) {
                Log.d(TAG, "Error : " + jsonObject.toString());
            }

            @Override
            public void onWindowClose(JSONObject jsonObject) {
                Log.d(TAG, "Chat Window Closed : " + jsonObject.toString());
            }

            @Override
            public void onLogout() {
                Log.d(TAG, "Logout");
            }
        });

    }

    private void showLoading(boolean show) {
        if (show) {
            pbLoading.setVisibility(View.VISIBLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        } else {
            pbLoading.setVisibility(View.GONE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }
}
