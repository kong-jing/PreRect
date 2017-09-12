[![Build Status](https://www.travis-ci.org/kong-jing/PreRect.svg?branch=master)](https://www.travis-ci.org/kong-jing/PreRect) [![API](https://img.shields.io/badge/API-14%2B-green.svg?style=flat)](https://android-arsenal.com/api?level=14)
# CameraPreview
android CameraPreview ,google facedetector.Use facedetector draw a face frame.

使用谷歌人脸检测算法在打开摄像头预览后画出当前镜头检测到的人脸框。

#错误解决

1.Custom view CustomView is not using the 2- or 3-argument View constructors; XML attributes will not work

You need to override the other 2 constructors of View in CustomView:
