package com.shuyu;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.Set;

/**
 * 演示WebView中的Api说明、js交互的方法，还有注意事项
 * <p>
 * 1、内存泄漏防备
 * 2、配置webView
 * 3、页面加载开始，错误，拦截请求，接受Error等
 * 4、页面加载进度，title，图标，js弹框等
 * 5、js交互与安全
 */
public class APIWebViewActivity extends AppCompatActivity implements View.OnClickListener {

    FrameLayout mRootLayout;
    WebView mWebView;
    Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api_webview);

        findViewById(R.id.call_js_function).setOnClickListener(this);

        //添加webView到布局中
        addWebViewToLayout();

        //set webView Setting
        setWebView();

        //set webView Client
        setWebClient();

        //set webView chrome
        setWebViewChromeClient();

        //load web
        loadUrl();

    }

    /**
     * 主动清空销毁来避免内存泄漏
     */
    @Override
    protected void onDestroy() {
        if (mWebView != null) {
            mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mWebView.clearHistory();
            ((ViewGroup) mWebView.getParent()).removeView(mWebView);
            mWebView.destroy();
            mWebView = null;
        }
        super.onDestroy();
    }


    /**
     * 1、不在xml中定义Webview，而是在需要的时候在Activity中创建
     * 使用getApplicationgContext()，避免内存泄漏。
     * <p>
     * 2、当然，你也可以配置webView所在Activity，
     * 在AndroidManifest中的进程为：android:process=":remote"
     * 避免泄漏影响主进程
     **/
    void addWebViewToLayout() {
        mRootLayout = (FrameLayout) findViewById(R.id.js_root_layout);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mWebView = new WebView(getApplicationContext());
        mWebView.setLayoutParams(params);
        mRootLayout.addView(mWebView);
    }


    /**
     * 配置webView
     */
    void setWebView() {
        //声明WebSettings子类
        WebSettings webSettings = mWebView.getSettings();

        //支持Javascript交互
        webSettings.setJavaScriptEnabled(true);


        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小

        //缩放操作
        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件

        //其他细节操作
        //webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //关闭webview中缓存
        webSettings.setAllowFileAccess(true); //设置可以访问文件

        //对于不需要使用 file 协议的应用，禁用 file 协议；防止文件泄密，file协议即是file://
        //webSettings.setAllowFileAccess(false);
        //webSettings.setAllowFileAccessFromFileURLs(false);
        //webSettings.setAllowUniversalAccessFromFileURLs(false);



        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式

        mWebView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                //网页中触发下载动作
            }
        });

        //增加js交互接口
        mWebView.addJavascriptInterface(new JsCallAndroidInterface(), "JSCallBackInterface");
    }

    /**
     * 设置webView的Client，如页面加载开始，错误，拦截请求，接受Error等
     */
    void setWebClient() {
        mWebView.setWebViewClient(new WebViewClient() {

            //拦截页面中的url加载,21以下的
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (resolveShouldLoadLogic(url)) {
                    return true;
                }
                return super.shouldOverrideUrlLoading(view, url);
            }

            //拦截页面中的url加载,21以上的
            @TargetApi(21)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                if (resolveShouldLoadLogic(request.getUrl().toString())) {
                    return true;
                }
                return super.shouldOverrideUrlLoading(view, request);
            }

            //页面开始加载
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            //页面加载完成
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            //页面加载每一个资源，如图片
            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
            }

            //监听WebView发出的请求并做相应的处理
            //浏览器的渲染以及资源加载都是在一个线程中，如果在shouldInterceptRequest处理时间过长，WebView界面就会阻塞
            //21以下的
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                return super.shouldInterceptRequest(view, url);
            }

            //监听WebView发出的请求并做相应的处理
            //浏览器的渲染以及资源加载都是在一个线程中，如果在shouldInterceptRequest处理时间过长，WebView界面就会阻塞
            //21以上的
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                return super.shouldInterceptRequest(view, request);
            }

            //页面加载出现错误,23以下的
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                switch (errorCode) {
                    case 404:
                        //view.loadUrl("加载一个错误页面提示，优化体验");
                        break;
                }
            }

            //页面加载出现错误,23以上的
            @TargetApi(23)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                switch (error.getErrorCode()) {
                    case 404:
                        //view.loadUrl("加载一个错误页面提示，优化体验");
                        break;
                }

            }

            //https错误
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                super.onReceivedSslError(view, handler, error);
            }
        });
    }

    /**
     * 设置webView的辅助功能，如页面加载进度，title，图标，js弹框等
     */
    void setWebViewChromeClient() {
        mWebView.setWebChromeClient(new WebChromeClient() {

            //页面加载进度
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
            }

            //获取标题
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }

            //获取图标
            @Override
            public void onReceivedIcon(WebView view, Bitmap icon) {
                super.onReceivedIcon(view, icon);
            }

            //是否支持页面中的js警告弹出框
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {

                Toast.makeText(APIWebViewActivity.this, message, Toast.LENGTH_SHORT).show();

                return super.onJsAlert(view, url, message, result);
            }

            //是否支持页面中的js确定弹出框
            @Override
            public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
                return super.onJsConfirm(view, url, message, result);
            }

            //是否支持页面中的js输入弹出框
            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
                /**
                 * 有时候，为了安全考虑，js的参数回调，会通过这类地方回调回来，然后不弹出框。
                 */
                if(resolveJSPrompt(message)) {
                    return true;
                }
                return super.onJsPrompt(view, url, message, defaultValue, result);

            }
        });
    }

    /**
     * 加载url
     */
    void loadUrl() {
        // 格式规定为:file:///android_asset/文件名.html
        mWebView.loadUrl("file:///android_asset/localHtml.html");
        //方式1. 加载远程网页：
        //mWebView.loadUrl("http://www.google.com/");
        //方式2：加载asset的html页面
        //mWebView.loadUrl("file:///android_asset/localHtml.html");
        //方式3：加载手机SD的html页面
        //mWebView.loadUrl("file:///mnt/sdcard/database/taobao.html");
    }

    /**
     * 执行网页中的js方法
     */
    void callJsFunction() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mWebView.evaluateJavascript("javascript:callJS()", new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String value) {
                    //接受返回值
                }
            });
        } else {
            mWebView.loadUrl("javascript:callJS()");
        }
    }

    /**
     * js与web交互1
     * js 与 原生交互接口
     */
    private class JsCallAndroidInterface {

        //@JavascriptInterface注解方法，js端调用，4.2以后安全
        //4.2以前，当JS拿到Android这个对象后，就可以调用这个Android对象中所有的方法，包括系统类（java.lang.Runtime 类），从而进行任意代码执行。
        @JavascriptInterface
        public void callback(String msg) {
            Toast.makeText(APIWebViewActivity.this, "JS方法回调到web了 ：" + msg, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * js与web交互2
     * 通过 shouldOverrideUrlLoading 与 js交互
     */
    private boolean resolveShouldLoadLogic(String url) {
        Uri uri = Uri.parse(url);
        //解析协议
        if (uri.getScheme().equals("js")) {
            if (uri.getAuthority().equals("Authority")) {
                //你还可以继续接续参数
                //Set<String> collection = uri.getQueryParameterNames();
                Toast.makeText(APIWebViewActivity.this, "JS 2方法回调到web了", Toast.LENGTH_SHORT).show();

            }
            return true;
        }
        return false;
    }

    /**
     * js与web交互3
     * 通过 onJsPrompt 与 js交互
     */
    private boolean resolveJSPrompt(String message) {
        Uri uri = Uri.parse(message);
        if ( uri.getScheme().equals("js")) {
            if (uri.getAuthority().equals("Authority")) {

                //Set<String> collection = uri.getQueryParameterNames();
                //参数result:代表消息框的返回值(输入值)
                //result.confirm("JS 3方法回调到web");
                Toast.makeText(APIWebViewActivity.this, "JS 3方法回调到web了", Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        return false;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.call_js_function:
                callJsFunction();
                break;
        }
    }
}
