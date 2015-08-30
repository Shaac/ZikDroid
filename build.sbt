name := "ZikDroid"

javacOptions ++= Seq("-source", "1.7", "-target", "1.7")
scalaVersion := "2.11.7"
scalacOptions in Compile += "-feature"

import android.Keys._
android.Plugin.androidBuild

platformTarget in Android := "android-19"

// http://proguard.sourceforge.net/manual/troubleshooting.html#attributes
proguardOptions in Android += "-keepattributes Signature"

libraryDependencies += "org.scaloid" %% "scaloid" % "4.0"
proguardCache in Android += "org.scaloid"
proguardOptions in Android += "-dontwarn org.scaloid.**"

libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % "1.0.5"

run <<= run in Android
install <<= install in Android

retrolambdaEnable in Android := false // falster build time
antLayoutDetector in Android := () // I am just fine with ant for now
