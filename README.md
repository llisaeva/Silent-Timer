### **Silent Timer**
`SilentTimer` is an Android application that mutes and unmutes audio at times specified by the user. Silent Mode can be scheduled to repeat on chosen weekdays and times.

![](https://github.com/llisaeva/Silent-Timer/blob/remade/demo-img/demo_screenshot.png)


__Compatible with Android 4.2 Jelly Bean (API 17) and newer versions.__

Has five screens: 
+ __Main screen__ - displayed on app start, shows all created silent intervals.
+ __Immediate screen__ - mutes audio starting from the current time, for a time increment selected by the user on this screen. This screen opens after clicking `+` on the bar in the Main screen.
+ __Schedule screen__ - schedules either a repeating or non-repeating silent interval.
+ __Edit List screen__ - user can reorder list entries, and delete selected ones.
+ __Settings__ - lets the user configure the theme (Dark Theme or Light Theme), the time increment choices displayed on the Immediate screen (defaults are 30 minutes, 1 hour, 2 hours, 3 hours), and debug options.

### **Tools used**
[Android Studio](https://developer.android.com/studio/?gclid=Cj0KCQjwjPaCBhDkARIsAISZN7QdwHj_tAYOvtweq-nhfQMH4W8-1hxSfg-bDu62AIp796tycL82El0aAhYDEALw_wcB&gclsrc=aw.ds),
[Joda-Time API](https://www.joda.org/joda-time/apidocs/index.html),
[Androidx Room](https://developer.android.com/jetpack/androidx/releases/room?gclid=Cj0KCQjwjPaCBhDkARIsAISZN7SRCJ76FjIF4fjxf8yV9uhdlnLm_q0sei3_-NzXuXLnrZk23t4lLl8aAszREALw_wcB&gclsrc=aw.ds),
[Android Databinding](https://developer.android.com/reference/android/databinding/packages),
[Mockito 3.8.0](https://javadoc.io/doc/org.mockito/mockito-core/latest/overview-summary.html)
