### 自定义webview长按文本弹出选项，并且点击后返回选项与所选中的文本，你的webview不再只支持系统的复制等功能了，长按web文本实现文本一键收藏、分享，就是这么简单。Demo中附带对webView的详细使用：api详解，配置详解，js多种通信方式详解。
-------------------

[![](https://jitpack.io/v/CarGuo/CustomActionWebView.svg)](https://jitpack.io/#CarGuo/CustomActionWebView)
[![Build Status](https://travis-ci.org/CarGuo/CustomActionWebView.svg?branch=master)](https://travis-ci.org/CarGuo/CustomActionWebView)


## [实现解析](http://www.jianshu.com/p/16713361bbd3)

#### 在project下的build.gradle添加
```
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```
#### 在module下的build.gradle添加
```
dependencies {
   compile 'com.github.CarGuo:CustomActionWebView:v1.0.1'
}
```

### 示例图

### JPG

![](https://ooo.0o0.ooo/2017/06/17/59450eac66a3a.jpg)
![](https://ooo.0o0.ooo/2017/06/17/59450eae894c5.jpg)

### GIF

![](https://ooo.0o0.ooo/2017/06/17/59450f5c52301.gif)


### 使用实例

和普通的webview使用基本一致

```
List<String> list = new ArrayList<>();
list.add("Item1");
list.add("Item2");
list.add("Item3");

mCustomActionWebView.setWebViewClient(new bViewClient());

//设置item
mCustomActionWebView.setActionList(list);

//链接js注入接口，使能选中返回数据
mCustomActionWebView.linkJSInterface();

ctionWebView.getSettings().setBuiltInZoomContro;
ctionWebView.getSettings().setDisplayZoomContro);
//使用javascript
ctionWebView.getSettings().setJavaScriptEnabled
ctionWebView.getSettings().setDomStorageEnabled


//增加点击回调
ctionWebView.setActionSelectListener(new lectListener() {
    @Override
    public void onClick(String title, String xt) {
        Toast.makeText(MainActivity.this, tem: " + title + "。\n\nValue: " + selectText, NGTH_LONG).show();
    }
});

//加载url
mCustomActionWebView.postDelayed(new Runnable() 
    @Override
    public void run() {
        mCustomActionWebView.loadUrl("http://shu.com/p/b32187d6e0ad");
    }
}, 1000);
```

### webView使用详细解析

* Demo中的APIWebViewActivity.java
```
/**
 * 演示WebView中的Api说明、js交互的方法，还有注意事项
 * <p>
 * 1、内存泄漏防备
 * 2、配置webView
 * 3、页面加载开始，错误，拦截请求，接受Error等
 * 4、页面加载进度，title，图标，js弹框等
 * 5、js交互与安全
 */
```

### 其他WebView推荐

* [Android开发：最全面、最易懂的Webview使用详解](http://www.jianshu.com/p/3c94ae673e2a)
* [最全面总结 Android WebView与 JS 的交互方式](http://www.jianshu.com/p/345f4d8a5cfa)
* [你不知道的 Android WebView 使用漏洞](http://www.jianshu.com/p/3a345d27cd42)
