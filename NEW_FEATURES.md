# TerentX / Vanta Client — Feature List & Fixes

## 1. Tool Durability HUD (`Overlay > Tool Durability`)
Shows durability of the held item (sword, pickaxe, anything with durability).
- **Display modes:** Numbers, Percentage, Both, **Hits Left** (≈ how many hits you can still land), **Blocks Left** (≈ how many blocks you can still break)
- Optional **"blocks left" info line** (`Show Block Count`) and **item name** label
- Durability bar with height, gradient and color-by-durability options
- **Movable** from the UI Editor / HUD Editor (drag it anywhere)
- **No darkening when Right-Shift is pressed** — the menu background dim was reduced
  globally so HUD elements stay fully visible behind the menu

## 2. UI Editor — dedicated in-menu editor (no chat editing)
- HUD layout is **not editable from chat** anymore; there are no chat commands for it
- **`Visual > UI Editor`** module, the **"UI Editor"** button in the TerentX client menu
  sidebar, and the **Settings screen > HUD tab > "Open UI Editor"** all open it
- Drag elements to move, **drag corner/edge handles to resize**, right-click while
  dragging resets, `H` toggles help, `ESC` saves and exits
- Renders with liquid glass panels

## 3. Liquid Glass (ReGlass) everywhere
- Bundled ReGlass mod (`reglass/reglass-1.1.0-1.21.9.jar`, also in `build/libs/`)
  styles all vanilla screens/widgets; it is now part of the default launcher mod profile
- The client's own menus got the same treatment with the built-in LiquidGlass renderer:
  - **Client menu** (window, sidebar, buttons)
  - **Dropdown ClickGUI** (panels + search bar + logo chip)
  - **Title screen** (glass backing on the menu buttons + glass tooltips)
  - **Server list**, **HUD Editor**, **UI Editor**, **Settings screen header**
  - All HUD elements (durability, spotify, info HUD, …) draw glass panels

## 4. Custom Cape — any cape or skin by name (`Visual > Custom Cape`)
- **Preset / Custom Name:** any bundled cape (`terentx`, `cobalt`, `migrator`,
  `mojang`, `mojang_studios`, `minecon_2011..2016`, `senoe`, `prismarine`, `edge`,
  `firefox`, `billyk_`, `exhibition_1/2`, `ketamine_1/2`) or any asset name typed in
- **From Player:** type **any username** and the client fetches that player's
  **cape AND skin from Mojang** and applies them to you (async download, cached)
- `Also Apply Skin` toggle controls whether the fetched skin replaces yours
- Fixed: cape texture slugs now match the actual asset files (underscore naming)
- Fixed: the cape mixin (`PlayerListEntryMixin`) is now actually registered in
  `terentx.mixins.json`, so capes render in game

## 5. Custom Crosshair (`Visual > Custom Crosshair`)
- Styles: Cross, Dot, Cross+Dot, Circle, Square, Plus, Minus, T Shape and
  **Custom (Pixels)** — build your own crosshair **pixel by pixel** on a 9×9 grid
  (`Row 1..9` strings, `X` = pixel) with adjustable pixel size
- Size / thickness / gap, dot, outline, dynamic color when targeting entities,
  shake reduction + smooth animation
- The crosshair is now actually rendered in game (it replaces the vanilla crosshair
  while the module is on)

## 6. Lighting flicker fix
- Removed a broken "stabilize" hack that called non-existent MatrixStack methods
- The fullbright gamma redirect now applies to **every** gamma read in the lightmap
  update, so the internal smoothing interpolation can no longer oscillate between
  raw and boosted values — this removes the continuous light flicker when moving
  the camera left/right
- Smooth eye-height transitions in `CameraMixin` remain for the old-sneaking animation

## 7. Fly Speed (`Movement > Fly Speed`)
- Changes your **creative/spectator fly speed from the menu** (0.1x – 10x) by scaling
  the player abilities fly speed — it only affects actual flying, never walking
- Optional independent vertical speed (jump/sneak keys) and reset-on-disable

## 8. Spotify layouts (`Overlay > Spotify > Layout`)
Five selectable layouts, all with liquid glass panels:
- **Compact** – small icon + title/artist + progress bar
- **Detailed** – big art, SPOTIFY label, title, artist, wide progress bar
- **Wide** – one-line horizontal strip
- **Minimal** – title-only glass pill
- **Vertical** – centered art over centered text
- Options: progress bar, album art, animated (indeterminate) progress

## 9. LabyMod / Lunar style extras
- New **Info HUD** overlay element (`Overlay > Info HUD`) with toggleable lines:
  Coordinates, FPS, Ping, BPS, Clock
- Existing Laby/Lunar-style features: Keystrokes, Armor HUD, Target Info,
  Toggle Modules list, Zoom, Fullbright, custom crosshair, custom capes

## Notes
- HUD elements are moved/resized **only** via the UI Editor (menu), not chat
- ReGlass + TerentX jars should both sit in your `mods` folder
  (both are in `build/libs/`); the launcher profile already lists them
