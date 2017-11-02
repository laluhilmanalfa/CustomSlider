# Custom SLider
This is a Custom View for Slider Widget Android. 


### Sample 
<p align="center">
  <img width="800" height="300" src="http://laluhilman.com/github/slider/slider.gif">
</p>



### Installing
1. Compile library to produce customslider-release.aar file.
```
  ./gradlew :customsliderlibrary:assembleRelease :customsliderlibrary:publishToMavenLocal  *for mac/ubuntu only
```
```
  gradlew :customsliderlibrary:assembleRelease :customsliderlibrary:publishToMavenLocal  *for windows only
```
2. Add dependency to customslider in your app build gradle
```
    compile('laluhilman.com:customsliderlibrary:0.0.+') {
        changing true
    }
```
3. Add customslider to your xml layout

```
   <com.laluhilman.customsliderlibrary.JJASliderCheckOut
        android:layout_centerInParent="true"
        android:layout_width="match_parent"
        android:layout_height="70dp">

    </com.laluhilman.customsliderlibrary.JJASliderCheckOut>
```
  

## Built With
* https://github.com/RomainPiel/Shimmer-android - For ShimmerTextView

