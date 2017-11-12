package com.ming;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;

import java.util.HashMap;
import java.util.Map;

/**
 * for test
 */
public class MainActivity extends AppCompatActivity {

    private EditText urlEv;
    private WebView webViewFront;
    private WebView webViewBack;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    webViewFront.setVisibility(View.GONE);
                    webViewBack.setVisibility(View.VISIBLE);
                    //添加头信息
                    Map<String,String> map=new HashMap<String,String>();
                    map.put("User-Agent","Android");
                    webViewBack.loadUrl("http://www.so.com", map);
                    break;
                case 2:
                    webViewFront.setVisibility(View.VISIBLE);
                    webViewBack.setVisibility(View.GONE);
                    webViewBack.clearHistory();
                    webViewBack.clearCache(true);
                    break;
                case 3:
                    webViewFront.setVisibility(View.GONE);
                    webViewBack.setVisibility(View.VISIBLE);
                    webViewBack.loadUrl("http://www.baidu.com");
                    break;
                case 4:
                    webViewFront.setVisibility(View.VISIBLE);
                    webViewBack.setVisibility(View.GONE);
                    webViewBack.clearHistory();
                    webViewBack.clearCache(true);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        urlEv = (EditText) findViewById(R.id.url_edit);
        webViewFront = (WebView) findViewById(R.id.webview_front);
        webViewBack = (WebView) findViewById(R.id.webview_back);
        setWebViewSetting(webViewFront);
        setWebViewSetting(webViewBack);

        webViewFront.setWebViewClient(webViewClient);
        webViewBack.setWebViewClient(webViewClient);
        webViewFront.setWebChromeClient(webChromeClient);
        webViewBack.setWebChromeClient(webChromeClient);

        //
        webViewFront.addJavascriptInterface(new JSInterface(),"app");//添加js脚本接口
        webViewFront.loadUrl("file:///android_asset/test.html");

    }

    /**
     * 添加JS接口回调。
     * 在js中的使用如下
     * <div id='b'>
     <a onclick="window.app.showWebView2()">b.c</a>
     </div>
     */
    public class  JSInterface
    {

        @JavascriptInterface
        public void showWebView2() //提供给js调用的方法
        {
            mHandler.sendEmptyMessage(1);
        }
        @JavascriptInterface
        public void showWebView1() //提供给js调用的方法
        {
            mHandler.sendEmptyMessage(2);
        }
        @JavascriptInterface
        public void openBaidu() //提供给js调用的方法
        {
            mHandler.sendEmptyMessage(3);
        }
        @JavascriptInterface
        public void back() //提供给js调用的方法
        {
            mHandler.sendEmptyMessage(4);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Check if the key event was the Back button and if there's history
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            boolean isSuc = false;
            if ((webViewFront.getVisibility() == View.VISIBLE && webViewFront.canGoBack())) {
                webViewFront.goBack();
                isSuc = true;
            }
            if (webViewBack.getVisibility() == View.VISIBLE && webViewBack.canGoBack()) {
                webViewBack.goBack();
                isSuc = true;
            }
            if (!isSuc) {
                return super.onKeyDown(keyCode, event);
            }
            return true;
        }
        // If it wasn't the Back key or there's no web page history, bubble up to the default
        // system behavior (probably exit the activity)
        return super.onKeyDown(keyCode, event);
    }

    private WebViewClient webViewClient = new WebViewClient() {
        //在网页上的所有加载都经过这个方法,这个函数我们可以做很多操作。比如获取url，查看url.contains(“add”)，进行添加操作
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return super.shouldOverrideUrlLoading(view, request);
        }

        //处理在浏览器中的按键事件。
        @Override
        public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
            return super.shouldOverrideKeyEvent(view, event);
        }

        //开始载入页面时调用的，我们可以设定一个loading的页面，告诉用户程序在等待网络响应。
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        //在页面加载结束时调用, 我们可以关闭loading 条，切换程序动作。
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }

        //在加载页面资源时会调用，每一个资源（比如图片）的加载都会调用一次。
        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
        }

        //报告错误信息
        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
        }

        //WebView发生改变时调用
        @Override
        public void onScaleChanged(WebView view, float oldScale, float newScale) {
            super.onScaleChanged(view, oldScale, newScale);
        }

        //Key事件未被加载时调用
        @Override
        public void onUnhandledKeyEvent(WebView view, KeyEvent event) {
            super.onUnhandledKeyEvent(view, event);
        }
    };
    private WebChromeClient webChromeClient = new WebChromeClient() {
        public void onProgressChanged(WebView view, int newProgress) {

        } //获得网页的加载进度，显示在右上角的TextView控件中

        public void onReceivedTitle(WebView view, String title) {

        } //获取Web页中的title用来设置自己界面中的title, 当加载出错的时候，比如无网络，这时onReceiveTitle中获取的标题为”找不到该网页”,

        public void onReceivedIcon(WebView view, Bitmap icon) {

        } //获取Web页中的icon

        public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
            return super.onCreateWindow(view, isDialog, isUserGesture, resultMsg);
        }

        public void onCloseWindow(WebView window) {

        }

        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            return super.onJsAlert(view, url, message, result);
        } //处理alert弹出框，html 弹框的一种方式

        public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
            return super.onJsPrompt(view, url, message, defaultValue, result);
        } //处理confirm弹出框

        public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
            return super.onJsConfirm(view, url, message, result);
        } //处理prompt弹出框
    };

    private void setWebViewSetting(WebView webView) {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true); //支持js
//        settings.setPluginsEnabled(true); //支持插件
        settings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口

        settings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        settings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        settings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        settings.setBuiltInZoomControls(true); //设置内置的缩放控件。 这个取决于setSupportZoom(), 若setSupportZoom(false)，则该WebView不可缩放，这个不管设置什么都不能缩放。
        settings.setDisplayZoomControls(false); //隐藏原生的缩放控件
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN); //支持内容重新布局
        settings.supportMultipleWindows(); //多窗口
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //关闭webview中缓存
        settings.setAllowFileAccess(true); //设置可以访问文件
    }
}
