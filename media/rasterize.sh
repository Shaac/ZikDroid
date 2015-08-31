#!/bin/sh

rasterize () {
    mkdir -p ../res/drawable-$4
    inkscape $1 -e ../res/drawable-$4/$2.png -w $3 -h $3
}

rasterize ZikDroid.svg ic_launcher 36 ldpi
rasterize ZikDroid.svg ic_launcher 48 mdpi
rasterize ZikDroid.svg ic_launcher 72 hdpi
rasterize ZikDroid.svg ic_launcher 96 xhdpi
rasterize ZikDroid.svg ic_launcher 144 xxhdpi
rasterize ZikDroid.svg ic_launcher 192 xxxhdpi

rasterize notification.svg ic_notify 18 ldpi
rasterize notification.svg ic_notify 24 mdpi
rasterize notification.svg ic_notify 36 hdpi
rasterize notification.svg ic_notify 48 xhdpi
rasterize notification.svg ic_notify 72 xxhdpi
rasterize notification.svg ic_notify 96 xxxhdpi
