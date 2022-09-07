# OpenCamera
#### opencamera是一套高性能的图库框架，
#基于opengles+glsurfaceview,能实现实时滤镜，拍照，录制短视频，美颜磨皮等功能。

## 效果图


## 已实现功能
1.拍照<br>
2.录视频<br>
3.美颜，滤镜<br>



 


#### 添加权限
```xml
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.CAMERA"/>
    <uses-permission android:name="android.permission.RECORD_AUDIO"/>
```
#### xml布局文件
```xml
 <com.atech.glcamera.views.GLCameraView
        app:layout_constraintTop_toTopOf="parent"
        android:id="@+id/glcamera"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />
```
#### 开启或关闭美颜（默认开启）
```java
 mCameraView.enableBeauty(true);
```
#### 美颜程度（0~1）
```java
mCameraView.setBeautyLevel(0.5f);
```
#### 添加滤镜

```java

private List<FilterFactory.FilterType>filters = new ArrayList<>();
  ...
  filters.add(FilterFactory.FilterType.Original);
  filters.add(FilterFactory.FilterType.Sunrise);
  filters.add(FilterFactory.FilterType.Sunset);
  filters.add(FilterFactory.FilterType.BlackWhite);
  filters.add(FilterFactory.FilterType.WhiteCat);
  filters.add(FilterFactory.FilterType.BlackCat);
  filters.add(FilterFactory.FilterType.SkinWhiten);

```

#### 切换滤镜
```java
 mCameraView.updateFilter(filters.get(pos));
```

#### 切换镜头
```java
 mCameraView.switchCamera();
```

#### 拍照

```java
 mCameraView.takePicture(new FilteredBitmapCallback() {
            @Override
            public void onData(Bitmap bitmap) {
                 ...
            }
        });
```

#### 录制视频
```java
 private boolean isRecording = false;  // 录制状态
   ...
    
       btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
		
		isRecording = !isRecording;
       
                mCameraView.changeRecordingState(isRecording);
		
            }
        });

       
         
```

#### 设置视频保存路径及拍摄完成的回调
```java
 
        mCameraView.setOuputMP4File(mFile);
       
        mCameraView.setrecordFinishedListnener(new FileCallback() {
            @Override
            public void onData(File file) {

                //update the gallery
                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                        Uri.fromFile(file)));

            }
        });
```

## 参考
#### google/grafika 
https://github.com/google/grafika
#### android gpuimage
https://github.com/cats-oss/android-gpuimage
#### magic camera
https://github.com/wuhaoyu1990/MagicCamera

