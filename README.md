# ⚡ Vanta Client — 1.21.10 Fabric PvP & Utility Client

Custom Minecraft Java Edition 1.21.10 client built on the Fabric Loader framework. Features full custom GUI screens, Capes, Custom Fonts, Shaders, Discord RPC, and PvP enhancements.

---

## 🚀 Features

- **Custom UI & HUD**: High-FPS custom title screens, options menu, and server browser.
- **Custom Fonts & Shaders**: High-DPI font rendering with built-in post-processing blur shaders.
- **Cosmetics & Capes**: Integrated cape system supporting classic, Minecon, and custom designs.
- **Keybind Architecture**: Modular configurable bindings (`config/bindings.json`).
- **Discord Game SDK**: Real-time rich presence integration.

---

## 📦 Quick Installation

1. Install **Fabric Loader 0.17.2** for Minecraft **1.21.10**.
2. Download `vanta-client-1.21.10.jar` and `fabric-api-0.138.0+1.21.10.jar` from `build/libs/`.
3. Drop both files into your `.minecraft/mods` folder.
4. Launch the game using the Fabric profile.

---

## 🛠️ Building From Source

```bash
# Clone the repository
git clone https://github.com/yuutaziegler/vanta-client.git
cd vanta-client

# Build the client mod jar
./gradlew build
```

The compiled mod jar will be located in `build/libs/`.

---

## 📄 License
Licensed under the GPL-3.0 License.
