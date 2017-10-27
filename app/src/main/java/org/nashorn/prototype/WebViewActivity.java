package org.nashorn.prototype;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;

public class WebViewActivity extends AppCompatActivity {
    private static final String HOME_URL = "http://10.0.2.2:9000/#!/";
    private static final String SIGNUP_URL = "http://10.0.2.2:9000/#!/signup";
    private static final String USERLIST_URL = "http://10.0.2.2:9000/#!/user/list";
    private WebView webView = null;

    final class WebBrowserClient extends WebChromeClient {
        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            final JsResult fResult = result;
            AlertDialog.Builder dialog = new AlertDialog.Builder(WebViewActivity.this);
            dialog.setTitle("Javascript Alert");
            dialog.setMessage(message);
            dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    fResult.confirm();
                }
            });
            dialog.show();
            return super.onJsAlert(view, url, message, result);
        }
    }

    class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.i("URL=", url);
            String urls[] = url.split(":");
            if (urls.length > 2 && urls[1].equals("detail")) {
                String params[] = urls[2].split("&");
                Log.i("urls[2]",urls[2]);
                try {
                    params[1] = URLDecoder.decode(params[1], "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                Log.i("params[0]",params[0]);
                Log.i("params[1]",params[1]);
                Log.i("params[2]",params[2]);
                LinearLayout popup = (LinearLayout)findViewById(R.id.popup);
                TextView popupText = (TextView)findViewById(R.id.popup_text);
                popup.setVisibility(View.VISIBLE);
                popupText.setText(params[1]);
                return true;
            } else if (url.equals("login:")) {
                LayoutInflater layoutInflater =
                        (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View loginView = layoutInflater.inflate(R.layout.login, null);

                AlertDialog.Builder loginDialog =
                        new AlertDialog.Builder(WebViewActivity.this);
                loginDialog.setTitle("Login");
                loginDialog.setMessage("아이디와 비밀번호를 입력하세요.");
                loginDialog.setView(loginView);
                loginDialog.setPositiveButton("로그인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        TextView idText = (TextView)loginView.findViewById(R.id.user_id);
                        TextView passwordText = (TextView)loginView.findViewById(R.id.user_password);
                        Toast.makeText(WebViewActivity.this, idText.getText()+"/"+
                            passwordText.getText(), Toast.LENGTH_LONG).show();
                        new LoadUserList().execute(
                                "http://172.16.1.248:52273/user/login",
                                idText.getText().toString(),
                                passwordText.getText().toString());

                    }
                });
                loginDialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        webView.loadUrl(HOME_URL);
                    }
                });
                loginDialog.show();
                return true;
            }
            return super.shouldOverrideUrlLoading(view, url);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        webView = (WebView)findViewById(R.id.webview);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webView.setWebChromeClient(new WebBrowserClient());
        webView.setWebViewClient(new MyWebViewClient());
        webView.loadUrl(HOME_URL);

        //로그인한 상태인지 확인하고, 비로그인이면 로그인 화면으로 전환
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        if (pref.getString("token", "").equals("")) {
            Intent intent = new Intent(WebViewActivity.this,
                    LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
    //로그아웃
    public void logout(View view) {
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove("token");
        editor.commit();
        finish();
    }

    public void goHome(View view) {
        webView.loadUrl(HOME_URL+"?os=android&version=1.0&device=emul");
    }
    public void goSignup(View view) {
        webView.loadUrl(SIGNUP_URL);
    }
    public void goUserList(View view) {
        webView.loadUrl(USERLIST_URL);
    }
    public void closePopup(View view) {
        LinearLayout popup = (LinearLayout)findViewById(R.id.popup);
        popup.setVisibility(View.GONE);
    }

    class LoadUserList extends AsyncTask<String,String,String> {
        ProgressDialog dialog = new ProgressDialog(WebViewActivity.this);
        @Override
        protected void onPreExecute() {
            dialog.setMessage("사용자 목록 로딩 중...");
            dialog.show();
        }
        @Override
        protected void onPostExecute(String s) {//s-->서버에서 받은 JSON문자열
            dialog.dismiss();
            try {//JSON 파싱 {result:true, token:"asdklsajkj123uasoasduosusadouiss"}
                JSONObject json = new JSONObject(s);
                if (json.getBoolean("result") == true) {//로그인 성공
                    webView.loadUrl(USERLIST_URL);

                    SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("token", json.getString("token"));
                    editor.commit();
                } else {
                    webView.loadUrl(HOME_URL);

                    LinearLayout popup = (LinearLayout)findViewById(R.id.popup);
                    TextView popupText = (TextView)findViewById(R.id.popup_text);
                    popup.setVisibility(View.VISIBLE);
                    popupText.setText("암호가 틀렸거나 해당 계정이 존재하지 않습니다.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        @Override
        protected String doInBackground(String... params) {
            StringBuilder output = new StringBuilder();
            String urlString = params[0];
            String paramId = params[1];
            String paramPassword = params[2];
            try {
                URL url = new URL(urlString+"?id="+paramId+"&password="+paramPassword);
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                if (conn != null) {
                    conn.setConnectTimeout(10000);
                    conn.setRequestMethod("GET");
                    //conn.setDoInput(true); conn.setDoOutput(true);
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(conn.getInputStream()));
                    String line = null;
                    while(true) {
                        line = reader.readLine();
                        if (line == null) break;
                        output.append(line);
                    }
                    reader.close();
                    conn.disconnect();
                }
            } catch (Exception e) { e.printStackTrace(); }
            return output.toString();
        }
    }
}
