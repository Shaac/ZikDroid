ZikDroid
========

ZikDroid is an Android application to communicate with Parrot Zik devices.

Current version is 0.1, it is the result of a weekend’s work. Code refactor
and more features will come soon.

Why would I need this app?
--------------------------

If you possess a Parrot Zik, you must have an application (on smartphone or
desktop) for simple usage such as toggling noise cancellation, and see the
level of battery left.

Parrot provides an Android application for this, but ZikDroid is better on
the following points (comparing the official application for Zik version 1):

- ZikDroid is Free Software (as in free speech), meaning that anyone can
contribute to it;
- ZikDroid has no 3 seconds splashscreen advertizing a product you already own;
- ZikDroid notifies you when your battery runs low;
- ZikDroid takes way less than the non-movable 40Mo of the official application.

Features
--------

- Get battery level;
- Set or unset noise cancellation;
- Get a notification when Zik battery level is under 20%.

Building
--------

To build the project you need the [Android
 SDK](http://developer.android.com/sdk/index.html) and
[sbt](http://www.scala-sbt.org/).

You will also need [Inkscape](https://inkscape.org/) to generate resources.

### Generate resources

Icons are exported from SVG by Inkscape. To generate them, go to the `media`
folder and run the `rasterize.sh` script.

### Build

Run `sbt` in the root directory. From its shell, run `android:package` to
generate the APK in the bin directory. You can also use the sbt `install` and
`run` commands if an Android device is connected.

Third parties
-------------

- [Android SDK plugin](https://github.com/pfn/android-sdk-plugin):
Modified BSD License;
- [Progard](http://proguard.sourceforge.net/): GPLv2+
- [Scala XML](https://github.com/scala/scala-xml): Modified BSD License;
- [Sclaloid](https://github.com/pocorall/scaloid): Apache License, Version 2.0;
