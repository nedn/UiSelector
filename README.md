# UiSelector
Build:
ant build

To start the UiSelector server:
adb push bin/bundle.jar /data/local/tmp
adb push bin/UiSelector.jar /data/local/tmp
adb shell uiautomator runtest UiSelector.jar bundle.jar -c selector.Runner

To profile the sever communication on host side (make sure that you starts the
UISelector server first):
python -m cProfile -s cumtime request.py > profiling_stats.txt
