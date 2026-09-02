# ⚡ Vanta Client (formerly TerentX) — Minecraft 1.21.10 Fabric Client

A comprehensive, high-performance Minecraft Java Edition 1.21.10 Client built on Fabric Loader (0.17.2). Engineered for modern PvP, smooth NanoVG vector graphics, customizable GUI screens, Liquid Glass shaders, and modular module architecture.

> **Note for AI Agents & Developers**: This repository contains the **complete decompiled source code** (441 Java classes), all **51+ assets and logos**, **47 custom typography fonts**, **GLSL shaders**, **embedded native libraries**, and the **compiled release jar**.

---

## 📂 Repository Architecture

```
vanta-client/
├── build.gradle                             # Fabric Loom 1.7 build script (Minecraft 1.21.10, Yarn mappings)
├── settings.gradle                          # Project definitions
├── gradle.properties                        # JVM & dependency parameters
├── config/
│   └── bindings.json                        # Keybindings & module toggle states
├── build/libs/
│   ├── vanta-client-1.21.10.jar             # Compiled, ready-to-run release mod jar
│   └── fabric-api-0.138.0+1.21.10.jar       # Required Fabric API library
├── src/main/resources/
│   ├── fabric.mod.json                      # Fabric metadata & entrypoint (wtf.opal.OpalFabric)
│   ├── terentx.mixins.json                  # Mixin definitions
│   ├── terentx.accesswidener                # Fabric access widener rules
│   ├── client-terentx-refmap.json           # Mixin obfuscation refmap
│   ├── META-INF/jars/                       # Embedded native libraries & JARs
│   │   ├── discord-game-sdk4j-v0.5.5.jar    # Discord RPC support
│   │   ├── lwjgl-nanovg-3.3.3*.jar          # NanoVG vector rendering engine & platform natives
│   │   ├── mod-api-1.0.1.jar                # Hypixel Mod API integration
│   │   └── polyglot-23.1.0.jar              # GraalVM polyglot engine
│   └── assets/terentx/
│       ├── logo.png                         # Primary client logo
│       ├── logomeniu.png                    # Title screen menu logo
│       ├── capes/                           # 20+ capes (Minecon, Mojang, Senoe, Exhibition, TerentX)
│       ├── fonts/                           # 47 TTF fonts (Geist, Inter, Manrope, SF UI, Rubik, Ubuntu)
│       ├── icons/                           # macOS & circle icons
│       ├── image/                           # Button icons (single, multi, settings, quit)
│       ├── images/                          # mainmenubg.png
│       ├── mainmenu/                        # Title screen backgrounds and action button icons
│       ├── panorama/                        # 6-sided background panorama cube (panorama_0 to 5)
│       ├── shaders/                         # Post-processing blur shaders (blur.json, GLSL shaders)
│       └── window-icons/                    # High-res client window icons (16x16 up to 256x256, .icns)
└── src/main/java/                           # 441 Java Source Code Files
    └── wtf/opal/
        ├── OpalFabric.java                  # Main Client Mod Entrypoint
        ├── duck/                            # Duck-interface mixin accessors
        ├── mixin/                           # Fabric Mixins into Minecraft client
        └── client/
            ├── OpalClient.java              # Central client instance & lifecycle manager
            ├── ReleaseInfo.java             # Version & build info
            ├── binding/                     # Keybinding management system
            ├── command/                     # In-game chat commands (/bind, /toggle, /friend, etc.)
            ├── notification/                # Toast notification manager
            ├── renderer/                    # Rendering subsystems
            │   ├── NVGRenderer.java         # NanoVG vector graphics pipeline
            │   ├── NVGTextRenderer.java     # Sub-pixel font renderer with TTF caching
            │   ├── LiquidGlassRenderer.java # Blur & glass refraction shader engine
            │   ├── WorldRenderer.java       # In-world 3D ESP & tracer rendering
            │   └── shader/                  # Framebuffer blur & GL pipeline
            ├── screen/                      # Custom GUI Screens
            │   ├── TerentXTitleScreen.java  # Animated title screen with panorama & buttons
            │   ├── TerentXOptionsScreen.java# Custom options menu
            │   ├── TerentXServerListScreen.java # Custom multiplayer server browser
            │   ├── TerentXSettingsScreen.java # Client preferences
            │   ├── click/dropdown/          # Draggable Dropdown ClickGUI (Panels, Categories, Properties)
            │   └── hud/HUDEditorScreen.java # Drag-and-drop HUD element customizer
            └── feature/
                ├── helper/                  # Combat, rotation, and packet helpers
                │   ├── player/rotation/     # Rotation models (Hypixel, Linear, Organic)
                │   ├── player/packet/       # Packet blockage & buffering
                │   └── webhook/             # Discord webhook integration
                └── module/                  # Mod Modules (Combat, Movement, Visual, Utility, World)
                    ├── impl/combat/         # KillAura, AutoClicker, Velocity, Criticals, Reach
                    ├── impl/movement/       # Fly, Speed, LongJump, NoSlow, SafeWalk, Clipper, Strafe
                    ├── impl/visual/         # ESP, Chams, Tracers, HUD, Animations, Ambience, Fullbright
                    ├── impl/visual/overlay/ # Dynamic Island HUD, Spotify, Keystrokes, ArmorHUD, TargetInfo
                    ├── impl/utility/        # Disabler, AutoArmor, ChestStealer, InvManager, FastUse, IRC
                    └── impl/world/          # Scaffold, Breaker, FastBreak, Timer
```

---

## 🧩 Key Subsystems Breakdown

### 1. NanoVG Graphics & Liquid Glass
The client uses LWJGL NanoVG (`NVGRenderer.java`) for hardware-accelerated vector drawing (rounded rectangles, gradients, drop shadows) combined with OpenGL framebuffers (`ShaderFramebuffer.java` & `LiquidGlassRenderer.java`) to achieve spatial Apple-style frosted glass blur.

### 2. Custom Screen Hierarchy
* `TerentXTitleScreen`: Replaces vanilla main menu with smooth button animations, custom logos (`logo.png`, `logomeniu.png`), and camera rotation.
* `DropdownClickGUI`: Right-Shift interface categorizing modules into Combat, Movement, Visual, Utility, and World with interactive sliders, mode pickers, and color pickers.
* `DynamicIslandElement`: In-game dynamic island pill overlay showing real-time metrics, media, and notifications.

### 3. Module & Property System
Every module extends `Module.java` and registers typed properties (`BooleanProperty`, `NumberProperty`, `ModeProperty`, `ColorProperty`) which automatically bind to the ClickGUI and save/load from `config/bindings.json`.

---

## 🚀 Quick Start & Installation

### Option A: Use Prebuilt Release Jars
1. Download `vanta-client-1.21.10.jar` and `fabric-api-0.138.0+1.21.10.jar` from `build/libs/`.
2. Place both jars in `%appdata%/.minecraft/mods/`.
3. Launch Minecraft with **Fabric Loader 0.17.2 for 1.21.10**.

### Option B: Build From Source
```bash
git clone https://github.com/yuutaziegler/vanta-client.git
cd vanta-client
./gradlew build
```
The compiled mod will be output to `build/libs/`.
