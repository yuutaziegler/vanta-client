#!/usr/bin/env bash
# Vanta Client - rebuild the mod after the full UI rewrite.
# Requires JDK 21. First run downloads Minecraft + Fabric via Loom.
set -e
cd "$(dirname "$0")"

echo "=== [1/3] Cleaning old build..."
./gradlew clean --no-daemon

echo "=== [2/3] Building mod jar..."
./gradlew build -x test --no-daemon

echo "=== [3/3] Publishing release jar..."
cp -f build/libs/terentx-v0.1.jar build/libs/vanta-client-2.1.0.jar

echo ""
echo "DONE. Copy into ~/.minecraft/mods/ (or %appdata%\\.minecraft\\mods):"
echo "  build/libs/terentx-v0.1.jar        (the mod)"
echo "  build/libs/fabric-api-0.138.0+1.21.10.jar"
echo "Remove any OLD vanta/terentx jar from the mods folder first."
