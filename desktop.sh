#!/usr/bin/env bash
sudo rm -f /etc/apt/sources.list.d/yarn.list
if ! command -v Xvfb >/dev/null; then
  sudo apt-get update
  sudo apt-get install -y xvfb x11vnc fluxbox novnc websockify libgtk-3-0 libxtst6 libxxf86vm1 libgl1 libasound2t64 fonts-dejavu
fi
for p in Xvfb fluxbox x11vnc websockify; do pkill "$p" 2>/dev/null; done
Xvfb :1 -screen 0 1280x720x24 >/dev/null 2>&1 &
sleep 2
export DISPLAY=:1
fluxbox >/dev/null 2>&1 &
x11vnc -display :1 -nopw -forever -shared -rfbport 5901 >/dev/null 2>&1 &
websockify --web /usr/share/novnc 6080 localhost:5901 >/dev/null 2>&1 &
sleep 2
echo "Desktop ready."