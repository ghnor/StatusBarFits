# StatusBarFits
适配4.4以上版本的状态栏（颜色，内容布局延伸到状态栏）

## Gradle
```
compile 'com.ghnor:statusbarfits:1.0.0'
```

## Maven
```
<dependency>
  <groupId>com.ghnor</groupId>
  <artifactId>statusbarfits</artifactId>
  <version>1.0.0</version>
  <type>pom</type>
</dependency>
```
## 示例

||API 19(Kitkat 4.4)|API 21(Lollipop 5.0)|
|-----|------------------|--------------------|
|DrawerLayout - open|||

## 用法
在`setContentView()`方法之后调用：
```java
setContentView(R.layout.main_activity);
...
StatusBarFits.setColor(activity);
```
### 
