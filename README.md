# StatusBarFits
适配4.4以上版本的状态栏（颜色，内容布局延伸到状态栏）

## Gradle
```
compile 'com.ghnor:statusbarfits:1.1.0'
```

## Maven
```
<dependency>
  <groupId>com.ghnor</groupId>
  <artifactId>statusbarfits</artifactId>
  <version>1.1.0</version>
  <type>pom</type>
</dependency>
```

## 用法
在`setContentView()`方法之后调用：
```java
setContentView(R.layout.main_activity);
...
StatusBarFits.setColor(activity);
```
如果布局是`DrawerLayout`，需要设置`android:fitsSystemWindows="true"`。

## 示例

### 设置状态栏颜色

```
StatusBarFits.setColor();
```

||设置状态栏颜色|
|-----|--------------------------------------|
|DrawerLayout - close|<img src="https://github.com/ghnor/StatusBarFits/blob/master/imgs/color_drawer_close.png"/>|
|DrawerLayout - open|<img src="https://github.com/ghnor/StatusBarFits/blob/master/imgs/color_drawer_open.png"/>|
|Other|<img src="https://github.com/ghnor/StatusBarFits/blob/master/imgs/color_ordinary.png"/>|

### 设置状态栏半透明或透明

```
StatusBarFits.setTranslucent();
```
```
StatusBarFits.setTransparent();
```

||设置状态栏半透明|
|-----|--------------------------------------|
|DrawerLayout - close|<img src="https://github.com/ghnor/StatusBarFits/blob/master/imgs/translucent_drawer_close.png"/>|
|DrawerLayout - open|<img src="https://github.com/ghnor/StatusBarFits/blob/master/imgs/translucent_drawer_open.png"/>|
|Other|<img src="https://github.com/ghnor/StatusBarFits/blob/master/imgs/translucent_ordinary.png"/>|

## Thanks
[laobie/StatusBarUtil](https://github.com/laobie/StatusBarUtil)