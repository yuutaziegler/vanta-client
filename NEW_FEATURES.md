# New Features Added to Vanta Client

## 0. FULL UI REWRITE (v2.1.0) — "Liquid Glass" menus

The entire client UI was rebuilt. Fixes for: black/dark inventory, missing
ReGlass look, broken Right-Shift menu, and HUD editor that couldn't resize.

### New Right-Shift menu (`VantaClickGUIScreen`)
- **Right Shift opens the menu from anywhere** (inventory/chat/menus), and Right Shift / ESC closes it.
- Liquid-glass window (frosted body, rim light, top sheen, soft shadow) — a self-contained
  ReGlass-style look that needs no external shader mod (`VantaGlass`).
- **Draggable window** (drag the title bar).
- **Resize/zoom**: `-`/`+` buttons in the title bar, or hold **Ctrl + scroll**.
- Left **category rail** (Combat, Movement, Visual, World, Utility, ...) + "All".
- **Working search bar** (click it, type; filters by module name/description).
- Module rows: left-click toggles, **+ / - expands settings**, **middle-mouse-click = bind key**.
- Full property editing: boolean pills, **number sliders** (drag), **mode cyclers**
  (left/right click cycles), and an **HSB color picker** (saturation/brightness box + hue bar).
- Style selector in module settings: `Liquid Glass (new)` / `Modern TerentX` / `Dropdown`
  (old dropdown GUI kept as a fallback).
- Never paints an opaque backdrop — the screen can no longer appear black.

### Inventory / containers
- Removed the opaque dark overlay that made the player inventory look "intunecat".
- Every container screen (inventory, chests, anvils, ...) now gets a **light dim + liquid-glass
  frame** around the slot panel instead of a black fill. Slots stay fully visible.
- The branding logo sits on a small glass plaque at the top without covering any slot.

### HUD / UI Editor (`UIEditorScreen`)
- Opened from the Visual category ("UI Editor") or the **HUD** button in the new menu.
- Liquid-glass control window (movable, scalable).
- **Drag any HUD element to move it** (polled every frame — works while holding the button).
- **8 resize handles** (4 corners + 4 edges) on every element — drag to resize live.
- **Right-click an element** = reset its position.
- **H** toggles the help panel; **ESC / Right Shift** saves and exits.
- Renders every overlay element (even inactive ones) so all are editable.

### Other fixes
- Old Dropdown ClickGUI: panels no longer shrink exponentially every frame (the scale bug).
- Right-Shift handling moved to the keybind system (respects your `bindings.json`).

### Rebuilding
Run `build-vanta-ui.bat` (Windows) or `./build-vanta-ui.sh` (Linux/macOS).
Requires JDK 21. Output jar: `build/libs/terentx-v0.1.jar`
(copied to `vanta-client-2.1.0.jar`). Put it + `fabric-api-0.138.0+1.21.10.jar`
in your mods folder and **delete the old vanta/terentx jar**.

---

## 1. Tool Durability HUD Element
**Location:** `Overlay > Tool Durability`

Shows the durability of your currently held tool, sword, or any item with durability.
- **Display Modes:** Numbers, Percentage, or Both
- **Visual Options:** 
  - Durability bar with customizable height
  - Gradient bar effect
  - Colorful bar (changes color based on durability)
  - Item icon display
- **IMPORTANT:** The durability display now works correctly when blocking (R-Shift) - no more darkening!

## 2. Custom Crosshair Module
**Location:** `Visual > Custom Crosshair`

Fully customizable crosshair with pixel-perfect control:
- **Styles:** Cross, Dot, Cross+Dot, Circle, Square, Plus, Minus, Swastika, Custom
- **Size Settings:** Size, Thickness, Gap
- **Dot Settings:** Show/hide, size control
- **Outline:** Add outline for better visibility
- **Dynamic Color:** Changes color when targeting entities
- **Shake Reduction:** Reduce crosshair shake during movement

## 3. Custom Cape Module  
**Location:** `Visual > Custom Cape`

Set any cape from the game by name:
- **Preset Mode:** Select from predefined capes
- **Custom Name Mode:** Enter any cape name manually
- **Available Capes:** TerentX, Cobalt, Migrator, Minecon 2011-2016, Mojank, Dinnerbone, Grumm, LabyMod, Lunar Client, and many more!
- **Glow Effect:** Optional glow effect for capes

## 4. UI Editor Module
**Location:** `Visual > UI Editor`

Edit the position and size of all UI elements including menus:
- **Drag to Move:** Left-click and drag any element
- **Resize:** Drag corner or edge handles
- **Right-click:** Reset position to default
- **H Key:** Toggle help overlay
- **ESC:** Save and exit

## 5. Fly Speed Module
**Location:** `Movement > Fly Speed`

Control fly speed when in Creative or Spectator mode:
- **Speed Control:** Adjust horizontal fly speed (0.1 - 10.0)
- **Vertical Movement:** Independent control for up/down movement
- **Reset on Disable:** Automatically reset speed when module is disabled

## 6. Spotify Overlay Layouts
**Location:** `Overlay > Spotify`

Multiple layout options for the Spotify overlay:
- **Compact:** Small, minimal display
- **Detailed:** Full information display
- **Wide:** Horizontal layout
- **Minimal:** Just song name
- **Vertical:** Tall, narrow layout
- Additional options: Progress bar, album art, animated progress

## 7. Lighting Fix

Fixed the lighting flickering issue when rotating the camera left/right:
- Smooth eye height transitions during camera movement
- Prevents "pop" effects that cause lighting instability
- Works in all game modes

## 8. Liquid Glass Integration (ReGlass)

The ReGlass/Liquid Glass effect is now more widely available:
- Works with HUD panels
- Configurable opacity and blur
- Frosted glass effect option
- Customizable corner radius
- Shadow settings

## Module Registration

New modules are automatically registered in:
- HUD Editor (drag to reposition)
- Tab GUI
- Settings Menu (both Modern and Dropdown styles)

## Configuration

All new features are saved with your client config and will persist between sessions.
