package org.nashorn.prototype;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        webView = (WebView)findViewById(R.id.webview);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebBrowserClient());
        webView.loadUrl(HOME_URL);
    }

    public void goHome(View view) {
        webView.loadUrl(HOME_URL);
    }
    public void goSignup(View view) {
        webView.loadUrl(SIGNUP_URL);
    }
    public void goUserList(View view) {
        webView.loadUrl(USERLIST_URL);
    }
}
