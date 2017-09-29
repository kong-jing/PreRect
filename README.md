[![Build Status](https://www.travis-ci.org/kong-jing/PreRect.svg?branch=master)](https://www.travis-ci.org/kong-jing/PreRect) [![API](https://img.shields.io/badge/API-14%2B-green.svg?style=flat)](https://android-arsenal.com/api?level=14) ![license](https://img.shields.io/badge/license-Apache-000000.svg) [![codebeat badge](https://codebeat.co/badges/0872a40c-adfb-44b3-b641-8c4064e4e291)](https://codebeat.co/projects/github-com-kong-jing-prerect-master)


# PreRect
android PreRect ,google facedetector.Use facedetector draw a face frame.

使用谷歌人脸检测算法在打开摄像头预览后画出当前镜头检测到的人脸框。

# 错误解决

1.Custom view CustomView is not using the 2- or 3-argument View constructors; XML attributes will not work

You need to override the other 2 constructors of View in CustomView:

**双摄像头节点，热插拔，摄像头节点查找(Dual cameras, Hot usb)**


    sudo chmod 600 ××× （只有所有者有读和写的权限）
    sudo chmod 644 ××× （所有者有读和写的权限，组用户只有读的权限）
    sudo chmod 700 ××× （只有所有者有读和写以及执行的权限）
    sudo chmod 666 ××× （每个人都有读和写的权限）
    sudo chmod 777 ××× （每个人都有读和写以及执行的权限）


```adb shell```

```cd dev```

```ls```

```ls | grep video```

```chmod 777 video1```





License
-------

    Copyright 2017 Kong Jing

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
