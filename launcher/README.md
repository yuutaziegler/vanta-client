# TerentX Client Launcher

<div align="center">
  <img src="https://img.shields.io/badge/Version-2.0.0-blue.svg" alt="Version">
  <img src="https://img.shields.io/badge/Java-17+-green.svg" alt="Java">
  <img src="https://img.shields.io/badge/Platform-Windows%20%7C%20Linux%20%7C%20Mac-orange.svg" alt="Platform">
</div>

## Features

### 🎮 Core Features
- **Modern UI** - Beautiful glass-morphism design with smooth animations
- **Multi-Version Support** - Play on Minecraft 1.12.2 to 1.20.4
- **Smart RAM Allocation** - Automatic and manual RAM configuration
- **Quick Settings** - Instantly change game settings from the launcher
- **Auto-Connect** - Automatically connect to your favorite servers

### ⚡ Performance
- **Auto Java Detection** - Automatically selects the best Java version
- **Optimized Startup** - Fast loading and minimal resource usage
- **Memory Management** - Smart RAM allocation with min/max settings

### 🎨 Customization
- **Multiple Layouts** - Customizable sidebar and content panels
- **Theme Support** - Dark mode with accent colors
- **News Feed** - Built-in news and update announcements

## Screenshots

```
┌─────────────────────────────────────────────────────────────┐
│  TERENTX  Client v2.0                                      │
├─────────┬───────────────────────────────────────────────────┤
│         │  Welcome back, Player                             │
│  🏠 Home│  Ready to play? Start your adventure now!        │
│  ⚙ Set. │                    [SETTINGS] [PLAY ▶]           │
│  📋 Ver.│                                                   │
│  💎 Cosm│  Quick Settings                                   │
│  ❤️ Frnds│ ┌────────────┬────────────┬────────────────────┐│
│  📧 News│ │ Version    │ Java       │ RAM: 4096 MB       ││
│  ⭐ Prem│ │ [1.20.4 ▼] │ [Auto ▼]   │ [====|====]        ││
│  ✓ Updt│ ├────────────┼────────────┼────────────────────┤│
│  ❄ Disc│ │ Resolution │ ☑ Fullscrn │ ☑ Auto Connect     ││
│  ✗ Exit │ │ [1920x1080│ [16:9 ▼]   │ [play.hypixel.net] ││
│         │ └────────────┴────────────┴────────────────────┘│
│         │  Ready to play                                   │
│ ┌─────┐ │  [▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓]                 │
│ │ 😊  │ │                                                   │
│ │Player│ │  ┌──────────────────────────────────────────┐   │
│ │●Online│ │  │         TERENTX CLIENT NEWS              │   │
│ └─────┘ │  │  ★ NEW UPDATE v2.0                        │   │
│         │  │  - Added Custom Crosshair...               │   │
│         │  │  - New Tool Durability HUD...             │   │
│         │  │  ♥ FEATURED: Custom Cape Module           │   │
│         │  └──────────────────────────────────────────┘   │
└─────────┴───────────────────────────────────────────────────┘
```

## Installation

### Prerequisites
- **Java 17 or higher** - [Download here](https://adoptium.net/)
- **Minecraft** - Any version supported by TerentX Client

### Windows

1. **Option A: Direct Run**
   ```bash
   # Build the launcher
   gradlew build
   
   # Run it
   start.bat
   ```

2. **Option B: With Gradle**
   ```bash
   gradlew run
   ```

3. **Option C: Create Desktop Shortcut**
   - Right-click on `start.bat`
   - Select "Create shortcut"
   - Move shortcut to Desktop

### Linux / Mac

1. Build the launcher:
   ```bash
   chmod +x start.sh
   ./start.sh
   ```

2. Or with Gradle:
   ```bash
   ./gradlew build
   java -jar build/libs/TerentX-Launcher-2.0.0.jar
   ```

## Building

### Standard Build
```bash
gradlew build
```

### Create Distribution
```bash
gradlew distZip
```

This creates `dist/TerentX-Launcher-2.0.0.zip`

### Clean Build
```bash
gradlew clean
```

## Configuration

Configuration is stored in `~/.terentx-launcher/config.json`

```json
{
  "username": "YourUsername",
  "ram": 4096,
  "version": "1.20.4",
  "fullscreen": false,
  "vsync": true,
  "fov": 110,
  "autoConnect": true,
  "server": "play.hypixel.net"
}
```

## Directory Structure

```
~/.terentx-launcher/
├── config.json           # User settings
├── versions/             # Minecraft versions
│   ├── 1.20.4/
│   ├── 1.20.2/
│   └── ...
├── mods/                 # Client mods
└── logs/                 # Launcher logs
```

## Features Overview

### Home Tab
- Quick access to play button
- Recent news and updates
- User status display

### Settings Tab
- **Video Settings**: VSync, Shaders, FOV
- **Game Settings**: Quick Play, Auto Connect
- **Resolution**: Multiple presets + custom

### Version Manager
- Switch between Minecraft versions
- Auto-download missing versions
- Version-specific mod configurations

### Cosmetics Tab (Coming Soon)
- Custom capes
- Particle effects
- Pet companions

### Premium Features
- Priority support
- Early access updates
- Exclusive cosmetics

## Integration with TerentX Client

The launcher is designed to work seamlessly with TerentX Client:

1. **Auto-detects** installed client mods
2. **Launches** Minecraft with client injected
3. **Syncs** settings across sessions
4. **Updates** client automatically

## Troubleshooting

### "Java not found"
Install Java 17+ from https://adoptium.net/

### "Launcher JAR not found"
Run `gradlew build` first

### "Game won't launch"
Check:
- RAM allocation (try reducing)
- Minecraft installation
- Java version compatibility

### Performance issues
- Reduce RAM allocation
- Close background applications
- Update graphics drivers

## Discord

Join our Discord for support and updates:
- Link: https://discord.gg/terentx

## License

TerentX Launcher - Custom Minecraft Launcher
Copyright (c) 2024 TerentX

All rights reserved.

## Credits

- **Developer**: TerentX
- **UI Design**: Custom glass-morphism
- **Framework**: Java Swing

---

<div align="center">
  Made with ❤️ by TerentX
</div>
