# CameraPreview
android CameraPreview ,google facedetector,

#错误解决

1.Custom view CustomView is not using the 2- or 3-argument View constructors; XML attributes will not work

You need to override the other 2 constructors of View in CustomView:
