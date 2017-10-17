package com.example.inscripts.ccuisample;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.inscripts.helpers.VolleyHelper;
import com.inscripts.interfaces.Callbacks;
import com.inscripts.interfaces.LaunchCallbacks;
import com.inscripts.interfaces.VolleyAjaxCallbacks;
import com.inscripts.utils.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import cometchat.inscripts.com.cometchatcore.coresdk.CometChat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final java.lang.String TAG = MainActivity.class.getSimpleName();
    private final int PERMISSION_LAUNCH_COMETCHAT = 11;

    private CometChat cometChat;

    private Button btnLaunchUser, btnLaunchGroup;

    private String username = "demo 3", password = "123";
    private String url = "";
    private String apiKey = "";
    private String userId = "10", groupId = "1";
    private String licenseKey = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLaunchUser = (Button) findViewById(R.id.btnLaunchUser);
        btnLaunchGroup = (Button) findViewById(R.id.btnLaunchGroup);

        btnLaunchUser.setOnClickListener(this);
        btnLaunchGroup.setOnClickListener(this);

            cometChat = CometChat.getInstance(this);

            cometChat.initializeCometChat(url,licenseKey, "", false, new Callbacks() {
                @Override
                public void successCallback(JSONObject jsonObject) {
                    Log.e(TAG,"Initialize cometchat success");

                    if (cometChat.isCometOnDemand()) {
                        System.out.println("cometChat : isCometOnDemand");
                        checkAndLaunchCloudCometchat();
                    } else {
                        System.out.println("cometChat : no isCometOnDemand");
                        checkAndLaunchCometchat();
                    }
                }

                @Override
                public void failCallback(JSONObject jsonObject) {
                    Logger.error(TAG,"Initialize cometchat fail");
                }
            });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLaunchUser:

                if (!cometChat.isCometOnDemand()) {
                    cometChat.login(username, password, new Callbacks() {
                        @Override
                        public void successCallback(JSONObject responce) {
                            Logger.error(TAG, "Login success responce = " + responce);


                        }

                        @Override
                        public void failCallback(JSONObject responce) {
                            Logger.error(TAG, "Login fail responce = " + responce);
                        }
                    });
                }

                break;

            case R.id.btnLaunchGroup:

                if (!cometChat.isCometOnDemand()) {
                    cometChat.login(username, password, new Callbacks() {
                        @Override
                        public void successCallback(JSONObject responce) {
                            Logger.error(TAG, "Login success responce = " + responce);

                        }

                        @Override
                        public void failCallback(JSONObject jsonObject) {

                        }
                    });
                }
                break;
        }
    }

    private void checkAndLaunchCometchat() {
        final CometChat cometChat = CometChat.getInstance(this);
        cometChat.login(username,password, new Callbacks() {
            @Override
            public void successCallback(JSONObject responce) {
                Log.e(TAG, "Login success responce = " + responce);
                cometChat.launchCometChat(MainActivity.this, false, new LaunchCallbacks() {
                    @Override
                    public void successCallback(JSONObject jsonObject) {

                    }

                    @Override
                    public void failCallback(JSONObject jsonObject) {
                        Log.e(TAG,"on fail responce = "+jsonObject);
                    }

                    @Override
                    public void userInfoCallback(JSONObject jsonObject) {

                    }

                    @Override
                    public void chatroomInfoCallback(JSONObject jsonObject) {

                    }

                    @Override
                    public void onMessageReceive(JSONObject jsonObject) {

                    }

                    @Override
                    public void error(JSONObject jsonObject) {
                        Log.e(TAG,"Onerror responce = "+jsonObject);
                    }

                    @Override
                    public void onLogout() {

                    }
                });
            }

            @Override
            public void failCallback(JSONObject responce) {
                Log.e(TAG, "Login fail responce = " + responce);
            }
        });
    }

    private void checkAndLaunchCloudCometchat() {

        final String username = "abcd";
        String password = "123";

        String adduserURL = "http://api.cometondemand.net/api/createuser";
        VolleyHelper volleyHelper = new VolleyHelper(this, adduserURL, new VolleyAjaxCallbacks() {
            @Override
            public void successCallback(String s) {
                System.out.println("cometChat : successCallback " + s);
                try {
                    JSONObject adduserResponce = new JSONObject(s);
                    long userid = -1;
                    if (adduserResponce.has("success")) {
                        userid = adduserResponce.getJSONObject("success").getJSONObject("data").getInt("userid");
                    } else if (adduserResponce.has("failed")) {
                        userid = adduserResponce.getJSONObject("failed").getJSONObject("data").getInt("userid");
                    }

                    System.out.println("cometChat : userid " + userid);
                    if (userid != -1) {
                        cloudLogin(userid);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failCallback(String s, boolean b) {

                System.out.println("cometChat : failCallback " + s);
            }
        });

        volleyHelper.addNameValuePair("action", "createuser");
        volleyHelper.addNameValuePair("username", username);
        volleyHelper.addNameValuePair("password", password);
        volleyHelper.sendAjax();
    }

    private void cloudLogin(long userid) {
        cometChat = CometChat.getInstance(this);
        cometChat.login(String.valueOf(userid), new Callbacks() {
            @Override
            public void successCallback(JSONObject jsonObject) {
                System.out.println("cometChat : cloudLogin successCallback " + jsonObject.toString());

                cometChat.launchCometChat(MainActivity.this, true, new LaunchCallbacks() {
                    @Override
                    public void successCallback(JSONObject jsonObject) {
                        System.out.println("cometChat : successCallback " + jsonObject.toString());
                    }

                    @Override
                    public void failCallback(JSONObject jsonObject) {
                        System.out.println("cometChat : failCallback " + jsonObject.toString());
                    }

                    @Override
                    public void userInfoCallback(JSONObject jsonObject) {
                        System.out.println("cometChat : userInfoCallback " + jsonObject.toString());
                    }

                    @Override
                    public void chatroomInfoCallback(JSONObject jsonObject) {
                        System.out.println("cometChat : chatroomInfoCallback " + jsonObject.toString());
                    }

                    @Override
                    public void onMessageReceive(JSONObject jsonObject) {
                        System.out.println("cometChat : onMessageReceive " + jsonObject.toString());
                    }

                    @Override
                    public void error(JSONObject jsonObject) {
                        Logger.error(TAG,"Onerror responce = "+jsonObject);
                    }

                    @Override
                    public void onLogout() {

                    }
                });
            }

            @Override
            public void failCallback(JSONObject jsonObject) {
                System.out.println("cometChat : failCallback " + jsonObject.toString());
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[],
                                           int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_LAUNCH_COMETCHAT: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //checkAndLaunchCometchat();
                } else {
                    Toast.makeText(this, "CometChat requires this permission to launch...", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            }

        }
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
}
