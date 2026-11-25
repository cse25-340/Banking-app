#!/usr/bin/env bash
# Usage:  bash run.sh              -> starts the Banking System window
#         bash run.sh --setup-only -> only installs what JavaFX needs
cd "$(dirname "$0")" || exit 1

# Libraries JavaFX needs on Linux (skipped if already there)
if ! ldconfig -p | grep -q "libgtk-3.so.0" || ! ldconfig -p | grep -q "libXtst.so.6"; then
  echo "Installing JavaFX system libraries (one time)..."
  sudo apt-get update
  sudo apt-get install -y libgtk-3-0 libxtst6 libxxf86vm1 libgl1 libasound2t64 fonts-dejavu
fi

if [ "$1" = "--setup-only" ]; then
  exit 0
fi

export DISPLAY="${DISPLAY:-:1}"
echo "Starting the app. Open the Ports tab, click the globe next to port 6080,"
echo "and sign in with password: vscode"
mvn -q javafx:run
