[![Build Status](https://www.travis-ci.org/kong-jing/PreRect.svg?branch=master)](https://www.travis-ci.org/kong-jing/PreRect) [![API](https://img.shields.io/badge/API-14%2B-green.svg?style=flat)](https://android-arsenal.com/api?level=14) ![license](https://img.shields.io/badge/license-Apache-000000.svg) [![codebeat](https://codebeat.co/badges/0872a40c-adfb-44b3-b641-8c4064e4e291)](https://codebeat.co/projects/github-com-kong-jing-prerect-master)
# PreRect
android PreRect ,google facedetector.Use facedetector draw a face frame.

使用谷歌人脸检测算法在打开摄像头预览后画出当前镜头检测到的人脸框。

#错误解决

1.Custom view CustomView is not using the 2- or 3-argument View constructors; XML attributes will not work

You need to override the other 2 constructors of View in CustomView:
