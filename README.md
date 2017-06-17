### 自定义webview长按文本弹出选项，并且点击后返回选项与所选中的文本，你的webview不再只支持系统的复制等功能了，长按web文本实现文本一键收藏、分享，就是这么简单。
-------------------

[![](https://jitpack.io/v/CarGuo/CustomActionWebView.svg)](https://jitpack.io/#CarGuo/CustomActionWebView)
[![Build Status](https://travis-ci.org/CarGuo/CustomActionWebView.svg?branch=master)](https://travis-ci.org/CarGuo/CustomActionWebView)

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
