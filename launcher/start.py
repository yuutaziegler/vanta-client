#!/usr/bin/env python3
"""
TerentX Launcher - Python Edition
A lightweight, cross-platform launcher for TerentX Client

Requirements:
- Python 3.8+
- Minecraft installed

Usage:
    python3 start.py
    ./start.py (on Unix with +x)
"""

import os
import sys
import json
import subprocess
import webbrowser
from pathlib import Path

# Try to import tkinter (built into Python)
try:
    import tkinter as tk
    from tkinter import ttk, messagebox
    TKINTER_AVAILABLE = True
except ImportError:
    TKINTER_AVAILABLE = False

# Configuration
LAUNCHER_DIR = Path.home() / ".terentx-launcher"
CONFIG_FILE = LAUNCHER_DIR / "config.json"
VERSION = "2.0.0"

# Default settings
DEFAULT_CONFIG = {
    "username": "TerentXPlayer",
    "version": "1.20.4",
    "ram": 4096,
    "fullscreen": False,
    "vsync": True,
    "fov": 110,
    "autoConnect": False,
    "server": "play.hypixel.net"
}

def load_config():
    """Load configuration from file"""
    if CONFIG_FILE.exists():
        try:
            with open(CONFIG_FILE, 'r') as f:
                return {**DEFAULT_CONFIG, **json.load(f)}
        except:
            pass
    return DEFAULT_CONFIG.copy()

def save_config(config):
    """Save configuration to file"""
    LAUNCHER_DIR.mkdir(parents=True, exist_ok=True)
    with open(CONFIG_FILE, 'w') as f:
        json.dump(config, f, indent=4)

def get_java_version():
    """Get installed Java version"""
    try:
        result = subprocess.run(['java', '-version'], capture_output=True, text=True)
        return result.stderr.split('\n')[0] if result.stderr else "Java found"
    except:
        return "Java not found"

def check_minecraft():
    """Check if Minecraft is installed"""
    if sys.platform == "win32":
        mc_path = Path(os.environ.get('APPDATA', '')) / '.minecraft'
    elif sys.platform == "darwin":
        mc_path = Path.home() / 'Library' / 'Application Support' / 'minecraft'
    else:
        mc_path = Path.home() / '.minecraft'
    
    return mc_path.exists(), mc_path

def launch_game(config):
    """Launch Minecraft with TerentX Client"""
    print(f"Launching Minecraft {config['version']}...")
    print(f"Username: {config['username']}")
    print(f"RAM: {config['ram']} MB")
    
    # Check for Minecraft
    installed, mc_path = check_minecraft()
    if installed:
        print(f"Minecraft found at: {mc_path}")
    else:
        print("Warning: Minecraft installation not detected")
    
    # Here you would implement actual game launching
    # For demo purposes, we just show a message
    return True

class TerentXGUI:
    """Main launcher GUI using tkinter"""
    
    def __init__(self):
        self.root = tk.Tk()
        self.root.title(f"TerentX Launcher v{VERSION}")
        self.root.geometry("900x650")
        self.root.minsize(800, 550)
        self.root.configure(bg='#0F0F19')
        
        self.config = load_config()
        
        self.setup_ui()
        
    def setup_ui(self):
        """Setup the user interface"""
        # Main container
        main_frame = tk.Frame(self.root, bg='#0F0F19')
        main_frame.pack(fill=tk.BOTH, expand=True)
        
        # Left sidebar
        self.create_sidebar(main_frame)
        
        # Right content area
        self.create_content(main_frame)
        
    def create_sidebar(self, parent):
        """Create the sidebar menu"""
        sidebar = tk.Frame(parent, bg='#151525', width=200)
        sidebar.pack(side=tk.LEFT, fill=tk.Y)
        sidebar.pack_propagate(False)
        
        # Logo area
        logo_frame = tk.Frame(sidebar, bg='#151525', height=100)
        logo_frame.pack(fill=tk.X, pady=(20, 10))
        logo_frame.pack_propagate(False)
        
        tk.Label(logo_frame, text="TERENTX", font=('Arial', 20, 'bold'),
                bg='#151525', fg='white').pack(pady=(20, 0))
        tk.Label(logo_frame, text=f"Client v{VERSION}", font=('Arial', 9),
                bg='#151525', fg='#9999BB').pack()
        
        # Menu buttons
        menu_items = [
            ("🏠 Home", self.show_home),
            ("⚙️ Settings", self.show_settings),
            ("📋 Versions", self.show_versions),
            ("💎 Cosmetics", self.show_cosmetics),
            ("❤️ Friends", self.show_friends),
            ("📧 News", self.show_news),
            ("⭐ Premium", self.show_premium),
            ("❄️ Discord", lambda: webbrowser.open("https://discord.gg/terentx")),
            ("❌ Exit", self.root.quit)
        ]
        
        for text, cmd in menu_items:
            btn = tk.Button(sidebar, text=text, font=('Segoe UI', 11),
                          bg='#151525', fg='white', anchor='w',
                          padx=20, pady=12, relief='flat',
                          command=cmd, cursor='hand2')
            btn.pack(fill=tk.X, padx=5, pady=2)
            btn.bind('<Enter>', lambda e: btn.configure(bg='#252540'))
            btn.bind('<Leave>', lambda e: btn.configure(bg='#151525'))
        
        # User info at bottom
        user_frame = tk.Frame(sidebar, bg='#151525', height=80)
        user_frame.pack(side=tk.BOTTOM, fill=tk.X)
        user_frame.pack_propagate(False)
        
        tk.Label(user_frame, text="😊", font=('Arial', 24),
                bg='#151525', fg='white').place(x=15, y=15)
        tk.Label(user_frame, text=self.config.get('username', 'Player'),
                font=('Segoe UI', 10, 'bold'), bg='#151525', fg='white').place(x=55, y=18)
        tk.Label(user_frame, text="● Online", font=('Segoe UI', 8),
                bg='#151525', fg='#66FF66').place(x=55, y=38)
        
    def create_content(self, parent):
        """Create the main content area"""
        content = tk.Frame(parent, bg='#0F0F19')
        content.pack(side=tk.RIGHT, fill=tk.BOTH, expand=True, padx=20, pady=20)
        
        # Welcome section
        welcome_frame = self.create_glass_panel(content)
        welcome_frame.pack(fill=tk.X, pady=(0, 15))
        
        inner = tk.Frame(welcome_frame, bg='#00000000')
        inner.pack(fill=tk.BOTH, expand=True, padx=20, pady=15)
        
        left = tk.Frame(inner, bg='#00000000')
        left.pack(side=tk.LEFT, fill=tk.Y)
        
        tk.Label(left, text=f"Welcome back, {self.config.get('username', 'Player')}",
                font=('Segoe UI', 18, 'bold'), bg='#00000000', fg='white').pack(anchor='w')
        tk.Label(left, text="Ready to play? Start your adventure now!",
                font=('Segoe UI', 10), bg='#00000000', fg='#BBBBCC').pack(anchor='w', pady=(5, 0))
        
        right = tk.Frame(inner, bg='#00000000')
        right.pack(side=tk.RIGHT, fill=tk.Y)
        
        tk.Button(right, text="SETTINGS", font=('Segoe UI', 10, 'bold'),
                bg='#6496FF', fg='white', padx=20, pady=10,
                relief='flat', cursor='hand2').pack(side=tk.LEFT, padx=5)
        
        self.play_btn = tk.Button(right, text="PLAY ▶", font=('Segoe UI', 10, 'bold'),
                bg='#64C864', fg='white', padx=25, pady=10,
                relief='flat', cursor='hand2', command=self.launch_game)
        self.play_btn.pack(side=tk.LEFT, padx=5)
        
        # Quick settings section
        tk.Label(content, text="Quick Settings", font=('Segoe UI', 12, 'bold'),
                bg='#0F0F19', fg='#BBBBCC', anchor='w').pack(fill=tk.X, pady=(10, 5))
        
        settings_frame = self.create_glass_panel(content)
        settings_frame.pack(fill=tk.X, pady=(0, 15))
        
        settings_inner = tk.Frame(settings_frame, bg='#00000000')
        settings_inner.pack(fill=tk.BOTH, expand=True, padx=15, pady=15)
        
        # Row 1
        row1 = tk.Frame(settings_inner, bg='#00000000')
        row1.pack(fill=tk.X, pady=5)
        
        # Version
        tk.Label(row1, text="Version:", font=('Segoe UI', 10),
                bg='#00000000', fg='#BBBBCC').pack(side=tk.LEFT)
        self.version_var = tk.StringVar(value=self.config.get('version', '1.20.4'))
        version_combo = ttk.Combobox(row1, textvariable=self.version_var, width=12,
                                    values=['1.20.4', '1.20.2', '1.19.4', '1.18.2', '1.16.5', '1.12.2'])
        version_combo.pack(side=tk.LEFT, padx=(10, 30))
        
        # Java
        tk.Label(row1, text="Java:", font=('Segoe UI', 10),
                bg='#00000000', fg='#BBBBCC').pack(side=tk.LEFT)
        java_combo = ttk.Combobox(row1, width=12, values=['Auto', 'Java 21', 'Java 17', 'Java 11'])
        java_combo.current(0)
        java_combo.pack(side=tk.LEFT, padx=(10, 30))
        
        # RAM
        tk.Label(row1, text="RAM:", font=('Segoe UI', 10),
                bg='#00000000', fg='#BBBBCC').pack(side=tk.LEFT)
        self.ram_var = tk.IntVar(value=self.config.get('ram', 4096))
        self.ram_label = tk.Label(row1, text=f"{self.ram_var.get()} MB",
                font=('Segoe UI', 10, 'bold'), bg='#00000000', fg='white')
        self.ram_label.pack(side=tk.LEFT, padx=(5, 10))
        tk.Scale(row1, from_=1024, to_=16384, orient=tk.HORIZONTAL,
                length=200, variable=self.ram_var, command=self.update_ram_label,
                bg='#0F0F19', fg='white', troughcolor='#333355',
                sliderrelief='flat').pack(side=tk.LEFT)
        
        # Row 2
        row2 = tk.Frame(settings_inner, bg='#00000000')
        row2.pack(fill=tk.X, pady=5)
        
        # Resolution
        tk.Label(row2, text="Resolution:", font=('Segoe UI', 10),
                bg='#00000000', fg='#BBBBCC').pack(side=tk.LEFT)
        res_combo = ttk.Combobox(row2, width=15,
                                values=['1920x1080', '1600x900', '1366x768', '1280x720', 'Windowed'])
        res_combo.current(0)
        res_combo.pack(side=tk.LEFT, padx=(10, 30))
        
        # Fullscreen checkbox
        self.fs_var = tk.BooleanVar(value=self.config.get('fullscreen', False))
        tk.Checkbutton(row2, text="Fullscreen", variable=self.fs_var,
                      bg='#00000000', fg='white', selectcolor='#333355').pack(side=tk.LEFT, padx=(0, 20))
        
        # Auto connect
        self.ac_var = tk.BooleanVar(value=self.config.get('autoConnect', False))
        tk.Checkbutton(row2, text="Auto Connect", variable=self.ac_var,
                      bg='#00000000', fg='white', selectcolor='#333355').pack(side=tk.LEFT)
        
        self.server_entry = tk.Entry(row2, width=20, bg='#222233', fg='white',
                                    insertbackground='white')
        self.server_entry.insert(0, self.config.get('server', 'play.hypixel.net'))
        self.server_entry.pack(side=tk.LEFT, padx=10)
        
        # Status bar
        status_frame = tk.Frame(content, bg='#0F0F19')
        status_frame.pack(fill=tk.X, side=tk.BOTTOM)
        
        tk.Label(status_frame, text="Ready to play",
                font=('Segoe UI', 9), bg='#0F0F19', fg='#BBBBCC').pack(side=tk.LEFT)
        
        # Progress bar
        self.progress = ttk.Progressbar(status_frame, length=200, mode='determinate')
        self.progress.pack(side=tk.RIGHT, pady=5)
        
    def create_glass_panel(self, parent):
        """Create a glass-morphism style panel"""
        panel = tk.Frame(parent, bg='#1A1A2E', relief='flat')
        
        # Add rounded corners effect with border
        panel.configure(highlightbackground='#FFFFFF11', highlightthickness=1)
        
        return panel
    
    def update_ram_label(self, val):
        """Update RAM label when slider changes"""
        self.ram_label.configure(text=f"{self.ram_var.get()} MB")
    
    def launch_game(self):
        """Launch the game"""
        self.config['username'] = self.config.get('username', 'Player')
        self.config['version'] = self.version_var.get()
        self.config['ram'] = self.ram_var.get()
        self.config['fullscreen'] = self.fs_var.get()
        self.config['autoConnect'] = self.ac_var.get()
        self.config['server'] = self.server_entry.get()
        
        save_config(self.config)
        
        # Animate button
        self.play_btn.configure(text="LAUNCHING...")
        self.root.update()
        
        import time
        for i in range(0, 101, 5):
            self.progress['value'] = i
            self.root.update()
            time.sleep(0.05)
        
        launch_game(self.config)
        
        self.play_btn.configure(text="PLAY ▶")
        self.progress['value'] = 0
    
    def show_home(self):
        """Show home tab"""
        pass
    
    def show_settings(self):
        """Show settings dialog"""
        messagebox.showinfo("Settings", "Settings panel coming soon!")
    
    def show_versions(self):
        """Show version manager"""
        messagebox.showinfo("Versions", "Version Manager\n\nAvailable: 1.20.4, 1.20.2, 1.19.4, 1.18.2, 1.16.5, 1.12.2")
    
    def show_cosmetics(self):
        """Show cosmetics shop"""
        messagebox.showinfo("Cosmetics", "Cosmetics Shop\n\nComing soon!\n\n- Custom Capes\n- Particle Effects\n- Pet Companions")
    
    def show_friends(self):
        """Show friends list"""
        messagebox.showinfo("Friends", "Friends List\n\nYour friends:\n- No friends added yet")
    
    def show_news(self):
        """Show news"""
        news = """TERENTX CLIENT NEWS

★ NEW UPDATE v2.0
- Added Custom Crosshair with pixel-perfect control
- New Tool Durability HUD element
- UI Editor for repositioning all elements
- Fly Speed control module
- Multiple Spotify layouts
- Fixed lighting issues when rotating camera

♥ FEATURED: Custom Cape Module
Set any cape from the game by name!
Supports: Minecon, Lunar Client, LabyMod, and more...

⚡ PERFORMANCE:
Optimized rendering for better FPS
Reduced memory usage by 15%
"""
        messagebox.showinfo("News", news)
    
    def show_premium(self):
        """Show premium info"""
        messagebox.showinfo("Premium", """TerentX Premium

✨ Exclusive Features:

- Priority Support
- Early Access Updates
- Exclusive Cosmetics
- Custom GUI Themes
- Unlimited Macros
- Cloud Config Sync

Get Premium: $9.99/month""")
    
    def run(self):
        """Run the launcher"""
        self.root.mainloop()

def console_mode():
    """Simple console launcher (fallback if tkinter not available)"""
    print(f"""
╔══════════════════════════════════════════════════╗
║         TerentX Client Launcher v{VERSION}           ║
╚══════════════════════════════════════════════════╝
""")
    
    config = load_config()
    
    print(f"Java: {get_java_version()}")
    installed, mc_path = check_minecraft()
    print(f"Minecraft: {'Found at ' + str(mc_path) if installed else 'Not found'}")
    print()
    
    print("Configuration:")
    print(f"  Username: {config['username']}")
    print(f"  Version: {config['version']}")
    print(f"  RAM: {config['ram']} MB")
    print()
    
    action = input("[1] Play  [2] Settings  [3] Exit\n> ").strip()
    
    if action == '1':
        launch_game(config)
    elif action == '2':
        print("\nSettings (coming soon!)")
    else:
        print("\nGoodbye!")
        sys.exit(0)

def main():
    """Main entry point"""
    if TKINTER_AVAILABLE:
        app = TerentXGUI()
        app.run()
    else:
        print("tkinter not available, running in console mode...")
        console_mode()

if __name__ == "__main__":
    main()
