#!/bin/bash

# TerentX Launcher Startup Script for Linux/Mac

# Get script directory
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR"

# Colors
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${BLUE}╔══════════════════════════════════════════════════╗${NC}"
echo -e "${BLUE}║         TerentX Client Launcher v2.0            ║${NC}"
echo -e "${BLUE}╚══════════════════════════════════════════════════╝${NC}"
echo ""

# Check for Java
if ! command -v java &> /dev/null; then
    echo -e "${RED}Java not found!${NC}"
    echo "Please install Java 17 or later."
    echo "On Ubuntu/Debian: sudo apt install openjdk-17-jdk"
    echo "On Mac: brew install openjdk@17"
    exit 1
fi

# Get Java version
JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2)
echo -e "${GREEN}✓${NC} Java detected: $JAVA_VERSION"

# Check for launcher JAR
LAUNCHER_JAR=""
if [ -f "$SCRIPT_DIR/build/libs/TerentX-Launcher-2.0.0.jar" ]; then
    LAUNCHER_JAR="$SCRIPT_DIR/build/libs/TerentX-Launcher-2.0.0.jar"
elif [ -f "$SCRIPT_DIR/TerentX-Launcher-2.0.0.jar" ]; then
    LAUNCHER_JAR="$SCRIPT_DIR/TerentX-Launcher-2.0.0.jar"
else
    echo -e "${YELLOW}!${NC} Launcher JAR not found in expected locations."
    echo "Building launcher..."
    cd "$SCRIPT_DIR"
    ./gradlew jar
    if [ -f "$SCRIPT_DIR/build/libs/TerentX-Launcher-2.0.0.jar" ]; then
        LAUNCHER_JAR="$SCRIPT_DIR/build/libs/TerentX-Launcher-2.0.0.jar"
    fi
fi

if [ -z "$LAUNCHER_JAR" ]; then
    echo -e "${RED}✗${NC} Could not find or build launcher JAR."
    exit 1
fi

echo -e "${GREEN}✓${NC} Launcher found: $(basename $LAUNCHER_JAR)"
echo ""

# Set memory settings
MIN_RAM=${MIN_RAM:-1024M}
MAX_RAM=${MAX_RAM:-2048M}

echo -e "${BLUE}Memory Settings:${NC}"
echo "  Min RAM: $MIN_RAM"
echo "  Max RAM: $MAX_RAM"
echo ""

# Create .terentx-launcher directory if not exists
mkdir -p "$HOME/.terentx-launcher"

# Launch with custom properties
echo -e "${GREEN}Starting TerentX Launcher...${NC}"
echo ""

java \
    -Xms$MIN_RAM \
    -Xmx$MAX_RAM \
    -Dterentx.launcher.version=2.0.0 \
    -Dterentx.launcher.dir="$HOME/.terentx-launcher" \
    -jar "$LAUNCHER_JAR"

EXIT_CODE=$?

if [ $EXIT_CODE -eq 0 ]; then
    echo ""
    echo -e "${GREEN}TerentX Launcher closed successfully!${NC}"
else
    echo ""
    echo -e "${RED}TerentX Launcher exited with code: $EXIT_CODE${NC}"
fi

exit $EXIT_CODE
