#!/bin/sh

rasterize () {
    mkdir -p ../res/drawable-$4
    inkscape $1 -e ../res/drawable-$4/$2.png -w $3 -h $3
}

rasterize ZikDroid.svg ic_launcher 32 ldpi
rasterize ZikDroid.svg ic_launcher 48 mdpi
rasterize ZikDroid.svg ic_launcher 72 hdpi
rasterize ZikDroid.svg ic_launcher 96 xhdpi
rasterize ZikDroid.svg ic_launcher 144 xxhdpi
rasterize ZikDroid.svg ic_launcher 192 xxxhdpi
