This application was made for workaround glitchy MIUI/HyperOS Dnd mode

How it works:
- DndReceiver catches system events (DND mode triggering event - INTERRUPTION_FILTER_CHANGED)
- If DND was enabled -> App will turn on Silent mode in system (and vise-versa)

There is no background services or something, so battery drain is about 0%

To work properly app needed (All settings accessible from app screen):
 - Access to dnd settings
 - Access to autostart (for catching event, even if receiver killed by OS)

How to setup it?
1. Tap on each button
2. Find Kray DND Workaround
3. Turn switch on (Accept in dialog windowf, if neccessary)
4. You are great!

Now app will turn on silent mode, if system DND enabled. You can use DND schedule or what you want for manage DND

![ezgif-3-d6884e39da](https://github.com/user-attachments/assets/6d2e19c8-8fc0-4c89-b7fb-a334c352810f)
