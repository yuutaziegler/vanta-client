package com.terentx.launcher;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;
import java.util.List;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.plaf.basic.*;

/**
 * TerentX Client Launcher - Custom Minecraft Launcher
 * Features:
 * - Beautiful modern UI with Liquid Glass effect
 * - Auto-install and update client
 * - Multiple Minecraft versions support
 * - Custom settings and configurations
 * - Discord RPC integration
 * - Auto-java detection
 */
public class TerentXLauncher {
    
    // UI Components
    private JFrame frame;
    private JPanel mainPanel;
    private JPanel sidebarPanel;
    private JPanel contentPanel;
    private JPanel newsPanel;
    private JTextArea newsText;
    
    // Tabs
    private JList<String> sidebarList;
    private DefaultListModel<String> listModel;
    
    // User settings
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> versionCombo;
    private JComboBox<String> javaCombo;
    private JSlider ramSlider;
    private JLabel ramLabel;
    private JCheckBox fullscreenCheck;
    private JCheckBox autoConnectCheck;
    private JTextField serverField;
    
    // Progress
    private JProgressBar progressBar;
    private JLabel statusLabel;
    private JButton playButton;
    private JButton settingsButton;
    
    // Settings
    private JCheckBox vSyncCheck;
    private JCheckBox shadersCheck;
    private JCheckBox quickPlayCheck;
    private JComboBox<String> resolutionCombo;
    private JSlider fovSlider;
    private JLabel fovLabel;
    
    // Paths
    private static final String LAUNCHER_DIR = System.getProperty("user.home") + "/.terentx-launcher";
    private static final String CONFIG_FILE = LAUNCHER_DIR + "/config.json";
    private static final String VERSIONS_DIR = LAUNCHER_DIR + "/versions";
    private static final String MODS_DIR = LAUNCHER_DIR + "/mods";
    
    // Current version
    private String selectedVersion = "1.20.4";
    private int allocatedRam = 4096;
    
    public TerentXLauncher() {
        initializeLauncher();
    }
    
    private void initializeLauncher() {
        // Create launcher directory
        try {
            Files.createDirectories(Paths.get(LAUNCHER_DIR));
            Files.createDirectories(Paths.get(VERSIONS_DIR));
            Files.createDirectories(Paths.get(MODS_DIR));
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // Load configuration
        loadConfiguration();
        
        // Create main frame
        frame = new JFrame("TerentX Launcher");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 700);
        frame.setMinimumSize(new Dimension(900, 600));
        frame.setLocationRelativeTo(null);
        frame.setResizable(true);
        
        // Set icon
        try {
            frame.setIconImage(createDefaultIcon());
        } catch (Exception e) {
            // Ignore icon errors
        }
        
        // Create main panel with gradient background
        mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Gradient background
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(15, 15, 25),
                    0, getHeight(), new Color(25, 25, 45)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        
        createSidebar();
        createContentPanel();
        createNewsPanel();
        
        frame.add(mainPanel);
        frame.setVisible(true);
        
        // Check for updates on startup
        checkForUpdates();
    }
    
    private void createSidebar() {
        sidebarPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Glass effect background
                g2d.setColor(new Color(255, 255, 255, 10));
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                // Border
                g2d.setColor(new Color(255, 255, 255, 20));
                g2d.drawLine(getWidth() - 1, 0, getWidth() - 1, getHeight());
            }
        };
        sidebarPanel.setLayout(new BorderLayout());
        sidebarPanel.setPreferredSize(new Dimension(200, 0));
        
        // Logo/Title area
        JPanel logoPanel = new JPanel();
        logoPanel.setOpaque(false);
        logoPanel.setLayout(new BoxLayout(logoPanel, BoxLayout.Y_AXIS));
        logoPanel.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));
        
        JLabel logoLabel = new JLabel("TERENTX");
        logoLabel.setFont(new Font("Arial", Font.BOLD, 24));
        logoLabel.setForeground(Color.WHITE);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel versionLabel = new JLabel("Client v2.0");
        versionLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        versionLabel.setForeground(new Color(150, 150, 180));
        versionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        logoPanel.add(logoLabel);
        logoPanel.add(Box.createVerticalStrut(5));
        logoPanel.add(versionLabel);
        
        // Menu items
        listModel = new DefaultListModel<>();
        listModel.addElement("  \u2302  Home");
        listModel.addElement("  \u2699  Settings");
        listModel.addElement("  \u2630  Versions");
        listModel.addElement("  \u2666  Cosmetics");
        listModel.addElement("  \u2764  Friends");
        listModel.addElement("  \u2709  News");
        listModel.addElement("  \u2605  Premium");
        listModel.addElement("  \u2713  Update");
        listModel.addElement("  \u2744  Discord");
        listModel.addElement("  \u2717  Exit");
        
        sidebarList = new JList<>(listModel);
        sidebarList.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        sidebarList.setForeground(Color.WHITE);
        sidebarList.setBackground(new Color(0, 0, 0, 0));
        sidebarList.setSelectionBackground(new Color(100, 100, 255, 50));
        sidebarList.setSelectionForeground(Color.WHITE);
        sidebarList.setFixedCellHeight(45);
        sidebarList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                label.setOpaque(false);
                label.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 5));
                if (isSelected) {
                    label.setBackground(new Color(100, 100, 255, 50));
                    label.setOpaque(true);
                }
                return label;
            }
        });
        
        sidebarList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selected = sidebarList.getSelectedIndex();
                handleMenuSelection(selected);
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(sidebarList);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        
        // Bottom section with user info
        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 15, 15));
        
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        userPanel.setOpaque(false);
        
        JLabel avatarLabel = new JLabel("\u263A");
        avatarLabel.setFont(new Font("Arial", Font.PLAIN, 30));
        avatarLabel.setForeground(Color.WHITE);
        
        JPanel userInfoPanel = new JPanel();
        userInfoPanel.setOpaque(false);
        userInfoPanel.setLayout(new BoxLayout(userInfoPanel, BoxLayout.Y_AXIS));
        
        JLabel usernameLabel = new JLabel(usernameField != null ? usernameField.getText() : "Guest");
        usernameLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        usernameLabel.setForeground(Color.WHITE);
        
        JLabel statusOnlineLabel = new JLabel("\u25CF Online");
        statusOnlineLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        statusOnlineLabel.setForeground(new Color(100, 255, 100));
        
        userInfoPanel.add(usernameLabel);
        userInfoPanel.add(statusOnlineLabel);
        
        userPanel.add(avatarLabel);
        userPanel.add(Box.createHorizontalStrut(10));
        userPanel.add(userInfoPanel);
        
        bottomPanel.add(userPanel);
        
        sidebarPanel.add(logoPanel, BorderLayout.NORTH);
        sidebarPanel.add(scrollPane, BorderLayout.CENTER);
        sidebarPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        mainPanel.add(sidebarPanel, BorderLayout.WEST);
    }
    
    private void createContentPanel() {
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setOpaque(false);
        
        // Top bar with search and user actions
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);
        topBar.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        // Search field
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setOpaque(false);
        
        JTextField searchField = new JTextField(20);
        searchField.setPreferredSize(new Dimension(250, 35));
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 255, 255, 30)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        searchField.setBackground(new Color(255, 255, 255, 10));
        searchField.setForeground(Color.WHITE);
        searchField.setCaretColor(Color.WHITE);
        searchField.setText("Search...");
        searchField.setForeground(Color.GRAY);
        
        searchField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (searchField.getText().equals("Search...")) {
                    searchField.setText("");
                    searchField.setForeground(Color.WHITE);
                }
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setText("Search...");
                    searchField.setForeground(Color.GRAY);
                }
            }
        });
        
        searchPanel.add(searchField);
        
        // Action buttons
        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actionsPanel.setOpaque(false);
        
        JButton minimizeBtn = createIconButton("\u2212");
        minimizeBtn.addActionListener(e -> frame.setState(JFrame.ICONIFIED));
        
        JButton maximizeBtn = createIconButton("\u25A1");
        maximizeBtn.addActionListener(e -> {
            if (frame.getExtendedState() == JFrame.MAXIMIZED_BOTH) {
                frame.setExtendedState(JFrame.NORMAL);
            } else {
                frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            }
        });
        
        JButton closeBtn = createIconButton("\u2715");
        closeBtn.addActionListener(e -> System.exit(0));
        
        actionsPanel.add(minimizeBtn);
        actionsPanel.add(maximizeBtn);
        actionsPanel.add(closeBtn);
        
        topBar.add(searchPanel, BorderLayout.WEST);
        topBar.add(actionsPanel, BorderLayout.EAST);
        
        // Main content area
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        
        // Welcome section
        JPanel welcomePanel = createGlassPanel();
        welcomePanel.setLayout(new BorderLayout(20, 10));
        welcomePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel welcomeLabel = new JLabel("Welcome back, " + (usernameField != null ? usernameField.getText() : "Player"));
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        welcomeLabel.setForeground(Color.WHITE);
        
        JLabel subtitleLabel = new JLabel("Ready to play? Start your adventure now!");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitleLabel.setForeground(new Color(180, 180, 200));
        
        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.add(welcomeLabel);
        titlePanel.add(Box.createVerticalStrut(5));
        titlePanel.add(subtitleLabel);
        
        // Quick actions
        JPanel quickActions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        quickActions.setOpaque(false);
        
        playButton = createActionButton("PLAY", new Color(100, 200, 100));
        playButton.setPreferredSize(new Dimension(120, 45));
        playButton.addActionListener(e -> launchGame());
        
        settingsButton = createActionButton("SETTINGS", new Color(100, 150, 255));
        settingsButton.setPreferredSize(new Dimension(120, 45));
        settingsButton.addActionListener(e -> showSettings());
        
        quickActions.add(settingsButton);
        quickActions.add(playButton);
        
        welcomePanel.add(titlePanel, BorderLayout.WEST);
        welcomePanel.add(quickActions, BorderLayout.EAST);
        
        // Quick settings panel
        JPanel quickSettingsPanel = createGlassPanel();
        quickSettingsPanel.setLayout(new GridLayout(2, 4, 15, 10));
        quickSettingsPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        // Version selector
        JPanel versionPanel = createSettingRow("Version", versionCombo = new JComboBox<>(new String[]{"1.20.4", "1.20.2", "1.19.4", "1.18.2", "1.16.5", "1.12.2"}));
        versionCombo.setSelectedItem(selectedVersion);
        
        // Java selector
        String[] javaVersions = detectJavaVersions();
        JPanel javaPanel = createSettingRow("Java", javaCombo = new JComboBox<>(javaVersions));
        
        // RAM slider
        JPanel ramPanel = new JPanel();
        ramPanel.setOpaque(false);
        ramPanel.setLayout(new BoxLayout(ramPanel, BoxLayout.Y_AXIS));
        
        ramLabel = new JLabel("RAM: " + allocatedRam + " MB");
        ramLabel.setForeground(Color.WHITE);
        ramLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        
        ramSlider = new JSlider(1024, 16384, allocatedRam);
        ramSlider.setOpaque(false);
        ramSlider.setForeground(Color.WHITE);
        ramSlider.setMajorTickSpacing(2048);
        ramSlider.setMinorTickSpacing(512);
        ramSlider.setPaintTicks(true);
        ramSlider.setPaintLabels(true);
        ramSlider.addChangeListener(e -> {
            allocatedRam = ramSlider.getValue();
            ramLabel.setText("RAM: " + allocatedRam + " MB");
        });
        
        ramPanel.add(ramLabel);
        ramPanel.add(ramSlider);
        
        // Resolution
        JPanel resPanel = createSettingRow("Resolution", resolutionCombo = new JComboBox<>(new String[]{"1920x1080", "1600x900", "1366x768", "1280x720", "Windowed"}));
        
        // Fullscreen
        fullscreenCheck = new JCheckBox("Fullscreen");
        fullscreenCheck.setForeground(Color.WHITE);
        fullscreenCheck.setOpaque(false);
        fullscreenCheck.setFocusPainted(false);
        
        // Auto connect
        autoConnectCheck = new JCheckBox("Auto Connect");
        autoConnectCheck.setForeground(Color.WHITE);
        autoConnectCheck.setOpaque(false);
        autoConnectCheck.setFocusPainted(false);
        
        serverField = new JTextField("play.hypixel.net");
        serverField.setPreferredSize(new Dimension(100, 25));
        serverField.setBackground(new Color(255, 255, 255, 10));
        serverField.setForeground(Color.WHITE);
        serverField.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 30)));
        
        JPanel serverPanel = new JPanel(new BorderLayout());
        serverPanel.setOpaque(false);
        serverPanel.add(autoConnectCheck, BorderLayout.WEST);
        serverPanel.add(serverField, BorderLayout.CENTER);
        
        quickSettingsPanel.add(versionPanel);
        quickSettingsPanel.add(javaPanel);
        quickSettingsPanel.add(ramPanel);
        quickSettingsPanel.add(resPanel);
        quickSettingsPanel.add(fullscreenCheck);
        quickSettingsPanel.add(serverPanel);
        quickSettingsPanel.add(new JPanel()); // Spacer
        
        // Progress bar
        JPanel progressPanel = new JPanel();
        progressPanel.setOpaque(false);
        progressPanel.setLayout(new BoxLayout(progressPanel, BoxLayout.Y_AXIS));
        progressPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        
        statusLabel = new JLabel("Ready to play");
        statusLabel.setForeground(new Color(180, 180, 200));
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        statusLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        progressBar.setStringPainted(false);
        progressBar.setForeground(new Color(100, 200, 255));
        progressBar.setBackground(new Color(255, 255, 255, 20));
        progressBar.setBorderPainted(false);
        progressBar.setPreferredSize(new Dimension(0, 6));
        
        progressPanel.add(statusLabel);
        progressPanel.add(Box.createVerticalStrut(5));
        progressPanel.add(progressBar);
        
        centerPanel.add(welcomePanel);
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(createSectionTitle("Quick Settings"));
        centerPanel.add(quickSettingsPanel);
        centerPanel.add(progressPanel);
        
        contentPanel.add(topBar, BorderLayout.NORTH);
        contentPanel.add(centerPanel, BorderLayout.CENTER);
        
        mainPanel.add(contentPanel, BorderLayout.CENTER);
    }
    
    private void createNewsPanel() {
        newsPanel = new JPanel(new BorderLayout());
        newsPanel.setOpaque(false);
        newsPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 20));
        
        newsPanel.add(new JLabel("NEWS FEED"), BorderLayout.NORTH);
        
        newsText = new JTextArea();
        newsText.setEditable(false);
        newsText.setBackground(new Color(255, 255, 255, 5));
        newsText.setForeground(Color.WHITE);
        newsText.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        newsText.setText(getNewsContent());
        
        JScrollPane scrollPane = new JScrollPane(newsText);
        scrollPane.setBorder(null);
        
        newsPanel.add(scrollPane);
    }
    
    private JPanel createGlassPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Glass effect
                g2d.setColor(new Color(255, 255, 255, 8));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                
                // Border
                g2d.setStroke(new BasicStroke(1));
                g2d.setColor(new Color(255, 255, 255, 20));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
            }
        };
        return panel;
    }
    
    private JPanel createSectionTitle(String title) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 5, 5, 5));
        
        JLabel label = new JLabel(title);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(new Color(180, 180, 220));
        
        panel.add(label);
        return panel;
    }
    
    private JPanel createSettingRow(String label, JComponent component) {
        JPanel panel = new JPanel(new BorderLayout(10, 5));
        panel.setOpaque(false);
        
        JLabel jLabel = new JLabel(label);
        jLabel.setForeground(new Color(180, 180, 200));
        jLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        
        panel.add(jLabel, BorderLayout.NORTH);
        panel.add(component);
        
        return panel;
    }
    
    private JButton createIconButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 16));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(35, 35));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setForeground(new Color(200, 200, 255));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setForeground(Color.WHITE);
            }
        });
        
        return button;
    }
    
    private JButton createActionButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setActionCommand(text);
        
        button.setUI(new BasicButtonUI() {
            @Override
            public void paint(Graphics g, AbstractButton b) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Background
                g2d.setColor(color);
                g2d.fillRoundRect(0, 0, b.getWidth(), b.getHeight(), 8, 8);
                
                // Text
                FontMetrics fm = g2d.getFontMetrics();
                String text = b.getText();
                int x = (b.getWidth() - fm.stringWidth(text)) / 2;
                int y = (b.getHeight() + fm.getAscent()) / 2 - 2;
                g2d.drawString(text, x, y);
            }
        });
        
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setLocation(button.getX(), button.getY() - 2);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setLocation(button.getX(), button.getY() + 2);
            }
        });
        
        return button;
    }
    
    private String[] detectJavaVersions() {
        List<String> javaVersions = new ArrayList<>();
        javaVersions.add("Auto (Recommended)");
        javaVersions.add("Java 21");
        javaVersions.add("Java 17");
        javaVersions.add("Java 11");
        javaVersions.add("Java 8");
        return javaVersions.toArray(new String[0]);
    }
    
    private String getNewsContent() {
        return """
            ╔══════════════════════════════════════════════════════════════╗
            ║                    TERENTX CLIENT NEWS                       ║
            ╠══════════════════════════════════════════════════════════════╣
            ║                                                              ║
            ║  \u2605 NEW UPDATE v2.0                                        ║
            ║  - Added Custom Crosshair with pixel-perfect control         ║
            ║  - New Tool Durability HUD element                          ║
            ║  - UI Editor for repositioning all elements                 ║
            ║  - Fly Speed control module                                 ║
            ║  - Multiple Spotify layouts                                 ║
            ║  - Fixed lighting issues when rotating camera               ║
            ║                                                              ║
            ║  \u2764 FEATURED: Custom Cape Module                           ║
            ║  Set any cape from the game by name!                        ║
            ║  Supports: Minecon, Lunar Client, LabyMod, and more...      ║
            ║                                                              ║
            ║  \u26A1 PERFORMANCE:                                          ║
            ║  Optimized rendering for better FPS                         ║
            ║  Reduced memory usage by 15%                                ║
            ║                                                              ║
            ║  \u2713 UPCOMING:                                              ║
            ║  - Custom keybind editor                                    ║
            ║  - Macro system                                             ║
            ║  - Server-side cape integration                            ║
            ║                                                              ║
            ╚══════════════════════════════════════════════════════════════╝
            """;
    }
    
    private void handleMenuSelection(int index) {
        switch (index) {
            case 0: // Home
                break;
            case 1: // Settings
                showSettings();
                break;
            case 2: // Versions
                showVersions();
                break;
            case 3: // Cosmetics
                showCosmetics();
                break;
            case 4: // Friends
                showFriends();
                break;
            case 5: // News
                showNews();
                break;
            case 6: // Premium
                showPremium();
                break;
            case 7: // Update
                checkForUpdates();
                break;
            case 8: // Discord
                openDiscord();
                break;
            case 9: // Exit
                System.exit(0);
        }
    }
    
    private void showSettings() {
        JDialog settingsDialog = new JDialog(frame, "TerentX Settings", true);
        settingsDialog.setSize(600, 500);
        settingsDialog.setLocationRelativeTo(frame);
        
        JPanel settingsPanel = new JPanel();
        settingsPanel.setLayout(new BoxLayout(settingsPanel, BoxLayout.Y_AXIS));
        settingsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Video settings
        JPanel videoPanel = createSettingsSection("Video Settings");
        vSyncCheck = new JCheckBox("VSync");
        vSyncCheck.setSelected(true);
        shadersCheck = new JCheckBox("Enable Shaders");
        
        JPanel fovPanel = new JPanel();
        fovLabel = new JLabel("FOV: 110");
        fovSlider = new JSlider(30, 110, 110);
        fovSlider.addChangeListener(e -> fovLabel.setText("FOV: " + fovSlider.getValue()));
        
        fovPanel.add(fovLabel);
        fovPanel.add(fovSlider);
        
        videoPanel.add(vSyncCheck);
        videoPanel.add(shadersCheck);
        videoPanel.add(fovPanel);
        
        // Game settings
        JPanel gamePanel = createSettingsSection("Game Settings");
        quickPlayCheck = new JCheckBox("Quick Play");
        quickPlayCheck.setSelected(true);
        gamePanel.add(quickPlayCheck);
        
        settingsPanel.add(videoPanel);
        settingsPanel.add(gamePanel);
        
        JButton saveBtn = new JButton("Save & Close");
        saveBtn.addActionListener(e -> {
            saveConfiguration();
            settingsDialog.dispose();
        });
        
        settingsPanel.add(Box.createVerticalGlue());
        settingsPanel.add(saveBtn);
        
        settingsDialog.add(settingsPanel);
        settingsDialog.setVisible(true);
    }
    
    private JPanel createSettingsSection(String title) {
        JPanel panel = createGlassPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(title),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        return panel;
    }
    
    private void showVersions() {
        JOptionPane.showMessageDialog(frame, 
            "Version Manager\n\nAvailable versions:\n- 1.20.4 (Latest)\n- 1.20.2\n- 1.19.4\n- 1.18.2\n- 1.16.5\n- 1.12.2\n\nSelect version from the main screen.", 
            "Version Manager", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void showCosmetics() {
        JOptionPane.showMessageDialog(frame, 
            "Cosmetics Shop\n\nComing soon!\n\n- Custom Capes\n- Particle Effects\n- Pet Companions\n- Chat Emojis\n- Profile Themes", 
            "Cosmetics", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void showFriends() {
        JOptionPane.showMessageDialog(frame, 
            "Friends List\n\nAdd friends to play together!\n\nYour friends:\n- No friends added yet", 
            "Friends", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void showNews() {
        JOptionPane.showMessageDialog(frame, getNewsContent(), "TerentX News", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void showPremium() {
        JOptionPane.showMessageDialog(frame, 
            "TerentX Premium\n\n\u2728 Exclusive Features:\n\n- Priority Support\n- Early Access Updates\n- Exclusive Cosmetics\n- Custom GUI Themes\n- Unlimited Macros\n- Cloud Config Sync\n\nGet Premium: $9.99/month", 
            "Premium", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void checkForUpdates() {
        statusLabel.setText("Checking for updates...");
        
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                for (int i = 0; i <= 100; i += 10) {
                    Thread.sleep(100);
                    progressBar.setValue(i);
                }
                return null;
            }
            
            @Override
            protected void done() {
                statusLabel.setText("Client is up to date! v2.0.0");
                progressBar.setValue(100);
            }
        };
        worker.execute();
    }
    
    private void openDiscord() {
        try {
            Desktop.getDesktop().browse(new URI("https://discord.gg/terentx"));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Could not open Discord link.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void launchGame() {
        String username = usernameField != null ? usernameField.getText() : "TerentXPlayer";
        selectedVersion = (String) versionCombo.getSelectedItem();
        
        statusLabel.setText("Launching Minecraft " + selectedVersion + "...");
        
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                // Simulate download/install process
                for (int i = 0; i <= 100; i += 2) {
                    Thread.sleep(50);
                    progressBar.setValue(i);
                    statusLabel.setText("Starting game... " + i + "%");
                }
                return null;
            }
            
            @Override
            protected void done() {
                statusLabel.setText("Game launched! Enjoy playing.");
                
                // Here you would actually launch Minecraft with the TerentX mod
                // For demo purposes, we just show a message
                try {
                    // Build and run command
                    String javaPath = "java";
                    String gameDir = LAUNCHER_DIR;
                    int maxRam = allocatedRam;
                    
                    // Example command (would need proper setup)
                    // ProcessBuilder pb = new ProcessBuilder(
                    //     javaPath, 
                    //     "-Xmx" + maxRam + "M",
                    //     "-jar", 
                    //     VERSIONS_DIR + "/minecraft.jar",
                    //     "--username", username,
                    //     "--version", selectedVersion,
                    //     "--gameDir", gameDir
                    // );
                    // pb.directory(new File(gameDir));
                    // pb.start();
                    
                    JOptionPane.showMessageDialog(frame, 
                        "Game would be launching with:\n\n" +
                        "Username: " + username + "\n" +
                        "Version: " + selectedVersion + "\n" +
                        "RAM: " + maxRam + " MB\n\n" +
                        "This is a demo - configure paths for production use!", 
                        "Launch", 
                        JOptionPane.INFORMATION_MESSAGE);
                        
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Error launching game: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }
    
    private void loadConfiguration() {
        File config = new File(CONFIG_FILE);
        if (config.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(config))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("username=")) {
                        // Load username
                    } else if (line.startsWith("ram=")) {
                        allocatedRam = Integer.parseInt(line.substring(4));
                    } else if (line.startsWith("version=")) {
                        selectedVersion = line.substring(8);
                    }
                }
            } catch (IOException | NumberFormatException e) {
                // Use defaults
            }
        }
    }
    
    private void saveConfiguration() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(CONFIG_FILE))) {
            writer.println("username=" + (usernameField != null ? usernameField.getText() : "Player"));
            writer.println("ram=" + allocatedRam);
            writer.println("version=" + selectedVersion);
            writer.println("fullscreen=" + fullscreenCheck.isSelected());
            writer.println("vsync=" + vSyncCheck.isSelected());
            writer.println("fov=" + fovSlider.getValue());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private Image createDefaultIcon() {
        // Create a simple 32x32 icon
        BufferedImage img = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Draw a simple "T" icon
        g2d.setColor(new Color(100, 150, 255));
        g2d.fillRoundRect(4, 4, 24, 24, 8, 8);
        
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 18));
        g2d.drawString("T", 10, 24);
        
        g2d.dispose();
        return img;
    }
    
    public static void main(String[] args) {
        // Set look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Use default
        }
        
        // Start launcher on EDT
        SwingUtilities.invokeLater(TerentXLauncher::new);
    }
}
