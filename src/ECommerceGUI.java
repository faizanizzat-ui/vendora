import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import javax.imageio.ImageIO;

public class ECommerceGUI extends JFrame {
  private final EcommerceSystem system = new EcommerceSystem();
  private User activeUser;
  private BufferedImage loginBackground;
  private BufferedImage dashboardBackground;
  private BufferedImage currentBackground;

  // Dark Glassmorphism Theme
  private static final Color ACCENT = new Color(99, 102, 241);
  private static final Color ACCENT_DARK = new Color(79, 70, 229);
  private static final Color GLASS_BG = new Color(0, 0, 0, 60);
  private static final Color GLASS_BORDER = new Color(255, 255, 255, 25);
  private static final Color DARK_TINT = new Color(0, 0, 0, 160);
  private static final Color TEXT = new Color(240, 240, 250);
  private static final Color MUTED = new Color(140, 140, 160);
  private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 26);
  private static final Font SUBTITLE_FONT = new Font("Segoe UI", Font.BOLD, 18);
  private static final Font BODY_FONT = new Font("Segoe UI", Font.PLAIN, 13);

  private final CardLayout cardLayout = new CardLayout();
  private final JPanel cardPanel = new JPanel(cardLayout);

  private class GlassPanel extends JPanel {
    private final int radius;

    public GlassPanel(int radius) {
      this.radius = radius;
      setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
      Graphics2D g2 = (Graphics2D) g.create();
      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

      // Frosted glass background - dark theme
      g2.setColor(new Color(20, 20, 30, 180));
      g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

      // Subtle highlight at top
      GradientPaint highlight = new GradientPaint(0, 0, new Color(255, 255, 255, 15),
          0, getHeight() / 3, new Color(255, 255, 255, 0));
      g2.setPaint(highlight);
      g2.fillRoundRect(0, 0, getWidth(), getHeight() / 3, radius, radius);

      // Border glow
      g2.setColor(new Color(255, 255, 255, 25));
      g2.setStroke(new BasicStroke(1f));
      g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);

      g2.dispose();
      super.paintComponent(g);
    }
  }

  private final DefaultTableModel productModel = new DefaultTableModel(new String[] { "ID", "Name", "Price", "Stock" },
      0) {
    @Override
    public boolean isCellEditable(int row, int column) {
      return false;
    }
  };

  private JTable customerTable;
  private JTable adminTable;
  private JTextArea cartArea;
  private JLabel totalLabel;
  private JLabel welcomeLabel;
  private JTextArea userArea;
  private JTextArea transactionArea;
  private JLabel authMessage;

  public ECommerceGUI() {
    system.loadData();
    loadBackgroundImage();
    setTitle("E-Commerce Management");
    setSize(1200, 780);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        system.saveData();
        dispose();
      }
    });

    cardPanel.add(buildWelcomePanel(), "WELCOME");
    cardPanel.add(buildLoginPanel(), "LOGIN");
    cardPanel.add(buildRegisterPanel(), "REGISTER");
    cardPanel.add(buildCustomerPanel(), "CUSTOMER");
    cardPanel.add(buildAdminPanel(), "ADMIN");
    cardPanel.setOpaque(false);

    // Main content pane with liquid UI background
    JPanel contentPane = new JPanel(new BorderLayout()) {
      @Override
      protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        int w = getWidth();
        int h = getHeight();

        // Draw background image
        if (currentBackground != null) {
          g2.drawImage(currentBackground, 0, 0, w, h, null);
        }

        // Liquid UI - multiple gradient overlays for fluid glass effect
        // Base transparency layer - dark theme
        g2.setColor(new Color(0, 0, 0, 140));
        g2.fillRect(0, 0, w, h);

        // Liquid gradient blob 1 - top left
        RadialGradientPaint liquid1 = new RadialGradientPaint(
            w * 0.2f, h * 0.3f, w * 0.6f,
            new float[] { 0f, 0.5f, 1f },
            new Color[] { new Color(99, 102, 241, 40), new Color(79, 70, 229, 20), new Color(0, 0, 0, 0) });
        g2.setPaint(liquid1);
        g2.fillRect(0, 0, w, h);

        // Liquid gradient blob 2 - bottom right
        RadialGradientPaint liquid2 = new RadialGradientPaint(
            w * 0.8f, h * 0.7f, w * 0.5f,
            new float[] { 0f, 0.5f, 1f },
            new Color[] { new Color(139, 92, 246, 35), new Color(99, 102, 241, 15), new Color(0, 0, 0, 0) });
        g2.setPaint(liquid2);
        g2.fillRect(0, 0, w, h);

        // Liquid gradient blob 3 - center glow
        RadialGradientPaint liquid3 = new RadialGradientPaint(
            w * 0.5f, h * 0.5f, w * 0.7f,
            new float[] { 0f, 0.6f, 1f },
            new Color[] { new Color(255, 255, 255, 8), new Color(255, 255, 255, 3), new Color(0, 0, 0, 0) });
        g2.setPaint(liquid3);
        g2.fillRect(0, 0, w, h);

        // Top edge highlight - liquid reflection
        GradientPaint topGlow = new GradientPaint(
            0, 0, new Color(255, 255, 255, 25),
            0, h * 0.15f, new Color(255, 255, 255, 0));
        g2.setPaint(topGlow);
        g2.fillRect(0, 0, w, (int) (h * 0.15f));

        g2.dispose();
      }
    };
    contentPane.add(cardPanel);
    setContentPane(contentPane);

    refreshProducts();
    showWelcome();
  }

  private void loadBackgroundImage() {
    try {
      // Try multiple paths for flexibility
      File loginFile = new File("Images/login.jfif");
      if (!loginFile.exists()) {
        loginFile = new File("src/Images/login.jfif");
      }
      if (loginFile.exists()) {
        loginBackground = ImageIO.read(loginFile);
      }

      File dashboardFile = new File("Images/logic.jfif");
      if (!dashboardFile.exists()) {
        dashboardFile = new File("src/Images/logic.jfif");
      }
      if (dashboardFile.exists()) {
        dashboardBackground = ImageIO.read(dashboardFile);
      }
      // Default to login background initially
      currentBackground = loginBackground;
    } catch (Exception e) {
      System.out.println("Could not load background images: " + e.getMessage());
    }
  }

  // Welcome Panel - Landing page with Login and Register buttons
  private JPanel buildWelcomePanel() {
    JPanel panel = new JPanel(new BorderLayout()) {
      @Override
      protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int w = getWidth();
        int h = getHeight();

        // Large ambient liquid glow - more transparent
        RadialGradientPaint ambient = new RadialGradientPaint(
            w * 0.5f, h * 0.5f, Math.max(w, h) * 0.9f,
            new float[] { 0f, 0.3f, 0.6f, 1f },
            new Color[] { new Color(99, 102, 241, 70), new Color(139, 92, 246, 45),
                new Color(79, 70, 229, 25), new Color(0, 0, 0, 0) });
        g2.setPaint(ambient);
        g2.fillRect(0, 0, w, h);

        // Top-left liquid blob
        RadialGradientPaint topLeft = new RadialGradientPaint(
            w * 0.2f, h * 0.2f, w * 0.5f,
            new float[] { 0f, 0.4f, 1f },
            new Color[] { new Color(236, 72, 153, 45), new Color(139, 92, 246, 20), new Color(0, 0, 0, 0) });
        g2.setPaint(topLeft);
        g2.fillRect(0, 0, w, h);

        // Bottom-right liquid blob
        RadialGradientPaint bottomRight = new RadialGradientPaint(
            w * 0.8f, h * 0.8f, w * 0.45f,
            new float[] { 0f, 0.4f, 1f },
            new Color[] { new Color(34, 211, 238, 40), new Color(99, 102, 241, 18), new Color(0, 0, 0, 0) });
        g2.setPaint(bottomRight);
        g2.fillRect(0, 0, w, h);

        g2.dispose();
      }
    };
    panel.setOpaque(false);

    GlassPanel form = new GlassPanel(24);
    form.setBorder(new EmptyBorder(50, 50, 50, 50));
    form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
    form.setAlignmentX(Component.CENTER_ALIGNMENT);

    JLabel title = new JLabel("E-Commerce Store");
    title.setFont(new Font("Segoe UI", Font.BOLD, 36));
    title.setForeground(TEXT);
    title.setAlignmentX(Component.CENTER_ALIGNMENT);
    form.add(title);
    form.add(Box.createVerticalStrut(16));

    JLabel subtitle = new JLabel("Your one-stop shop for everything");
    subtitle.setFont(SUBTITLE_FONT);
    subtitle.setForeground(MUTED);
    subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
    form.add(subtitle);
    form.add(Box.createVerticalStrut(50));

    JButton loginBtn = glassButton("Login");
    loginBtn.setFont(SUBTITLE_FONT);
    loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
    Dimension btnSize = new Dimension(200, 50);
    loginBtn.setPreferredSize(btnSize);
    loginBtn.setMaximumSize(btnSize);

    JButton registerBtn = glassButtonOutline("Register");
    registerBtn.setFont(SUBTITLE_FONT);
    registerBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
    registerBtn.setPreferredSize(btnSize);
    registerBtn.setMaximumSize(btnSize);

    form.add(loginBtn);
    form.add(Box.createVerticalStrut(16));
    form.add(registerBtn);

    loginBtn.addActionListener(e -> showLogin());
    registerBtn.addActionListener(e -> showRegister());

    JPanel wrapper = new JPanel(new GridBagLayout());
    wrapper.setOpaque(false);
    wrapper.add(form);
    panel.add(wrapper, BorderLayout.CENTER);
    return panel;
  }

  // Login Panel
  private JLabel loginMessage;

  private JPanel buildLoginPanel() {
    JPanel panel = new JPanel(new BorderLayout()) {
      @Override
      protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int w = getWidth();
        int h = getHeight();

        // Large ambient liquid glow
        RadialGradientPaint ambient = new RadialGradientPaint(
            w * 0.5f, h * 0.45f, Math.max(w, h) * 0.85f,
            new float[] { 0f, 0.3f, 0.6f, 1f },
            new Color[] { new Color(99, 102, 241, 65), new Color(79, 70, 229, 40),
                new Color(139, 92, 246, 20), new Color(0, 0, 0, 0) });
        g2.setPaint(ambient);
        g2.fillRect(0, 0, w, h);

        // Left liquid blob - pink/magenta
        RadialGradientPaint leftBlob = new RadialGradientPaint(
            w * 0.05f, h * 0.55f, w * 0.45f,
            new float[] { 0f, 0.35f, 1f },
            new Color[] { new Color(236, 72, 153, 50), new Color(139, 92, 246, 25), new Color(0, 0, 0, 0) });
        g2.setPaint(leftBlob);
        g2.fillRect(0, 0, w, h);

        // Right liquid blob - cyan
        RadialGradientPaint rightBlob = new RadialGradientPaint(
            w * 0.95f, h * 0.35f, w * 0.4f,
            new float[] { 0f, 0.35f, 1f },
            new Color[] { new Color(34, 211, 238, 45), new Color(99, 102, 241, 20), new Color(0, 0, 0, 0) });
        g2.setPaint(rightBlob);
        g2.fillRect(0, 0, w, h);

        // Top subtle glow
        RadialGradientPaint topGlow = new RadialGradientPaint(
            w * 0.5f, h * 0.1f, w * 0.6f,
            new float[] { 0f, 0.5f, 1f },
            new Color[] { new Color(255, 255, 255, 15), new Color(99, 102, 241, 8), new Color(0, 0, 0, 0) });
        g2.setPaint(topGlow);
        g2.fillRect(0, 0, w, h);

        g2.dispose();
      }
    };
    panel.setOpaque(false);

    GlassPanel form = new GlassPanel(24);
    form.setBorder(new EmptyBorder(50, 60, 50, 60));
    form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
    form.setAlignmentX(Component.CENTER_ALIGNMENT);

    // Icon/Avatar placeholder
    JLabel avatarLabel = new JLabel("\uD83D\uDC64", SwingConstants.CENTER);
    avatarLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
    avatarLabel.setForeground(ACCENT);
    avatarLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    form.add(avatarLabel);
    form.add(Box.createVerticalStrut(16));

    JLabel title = new JLabel("Welcome Back");
    title.setFont(new Font("Segoe UI", Font.BOLD, 28));
    title.setForeground(TEXT);
    title.setAlignmentX(Component.CENTER_ALIGNMENT);
    form.add(title);
    form.add(Box.createVerticalStrut(6));

    JLabel subtitle = new JLabel("Sign in to continue shopping");
    subtitle.setFont(BODY_FONT);
    subtitle.setForeground(MUTED);
    subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
    form.add(subtitle);
    form.add(Box.createVerticalStrut(35));

    JTextField usernameField = styledGlassField();
    Dimension fieldSize = new Dimension(320, 44);
    usernameField.setPreferredSize(fieldSize);
    usernameField.setMaximumSize(fieldSize);
    applyPlaceholder(usernameField, "Username");

    JPasswordField passwordField = styledGlassPasswordField();
    passwordField.setPreferredSize(fieldSize);
    passwordField.setMaximumSize(fieldSize);
    setupPasswordPlaceholder(passwordField, "Password");

    form.add(usernameField);
    form.add(Box.createVerticalStrut(16));
    form.add(passwordField);
    form.add(Box.createVerticalStrut(12));

    // Styled show password toggle with label
    JPanel showPassPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
    showPassPanel.setOpaque(false);
    showPassPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
    showPassPanel.setMaximumSize(new Dimension(320, 32));

    JToggleButton showPassBtn = createStyledToggle("\uD83D\uDC41  Show Password");
    JLabel showPassLabel = new JLabel("Show Password");
    showPassLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
    showPassLabel.setForeground(MUTED);
    showPassPanel.add(showPassBtn);
    showPassPanel.add(showPassLabel);
    form.add(showPassPanel);
    form.add(Box.createVerticalStrut(25));

    JButton loginBtn = glassButton("Sign In  →");
    loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
    loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
    Dimension btnSize = new Dimension(320, 48);
    loginBtn.setPreferredSize(btnSize);
    loginBtn.setMaximumSize(btnSize);
    form.add(loginBtn);
    form.add(Box.createVerticalStrut(16));

    loginMessage = new JLabel(" ");
    loginMessage.setForeground(new Color(248, 113, 113));
    loginMessage.setFont(BODY_FONT);
    loginMessage.setAlignmentX(Component.CENTER_ALIGNMENT);
    form.add(loginMessage);
    form.add(Box.createVerticalStrut(25));

    // Divider
    JSeparator divider = new JSeparator();
    divider.setForeground(GLASS_BORDER);
    divider.setMaximumSize(new Dimension(280, 1));
    form.add(divider);
    form.add(Box.createVerticalStrut(20));

    JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    backPanel.setOpaque(false);
    JLabel noAccount = new JLabel("Don't have an account?");
    noAccount.setForeground(MUTED);
    noAccount.setFont(BODY_FONT);
    JButton toRegister = new JButton("Create one");
    toRegister.setForeground(ACCENT);
    toRegister.setFont(new Font("Segoe UI", Font.BOLD, 13));
    toRegister.setBorderPainted(false);
    toRegister.setContentAreaFilled(false);
    toRegister.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    backPanel.add(noAccount);
    backPanel.add(toRegister);
    form.add(backPanel);

    JButton backBtn = glassButtonOutline("← Back to Welcome");
    backBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
    form.add(Box.createVerticalStrut(12));
    form.add(backBtn);

    loginBtn.addActionListener(e -> {
      loginMessage.setForeground(new Color(248, 113, 113));
      String user = usernameField.getText().trim();
      String pass = new String(passwordField.getPassword());
      if (user.isEmpty() || pass.isEmpty() || user.equals("Username") || pass.equals("Password")) {
        loginMessage.setText("Please enter username and password.");
        return;
      }
      User logged = system.login(user, pass);
      if (logged == null) {
        loginMessage.setText("Invalid credentials. Please try again.");
        return;
      }
      activeUser = logged;
      welcomeLabel.setText("Hello, " + activeUser.getUsername() + " (" + activeUser.getType() + ")");
      loginMessage.setText(" ");
      usernameField.setText("Username");
      usernameField.setForeground(MUTED);
      passwordField.setText("Password");
      passwordField.setEchoChar((char) 0);
      passwordField.setForeground(MUTED);
      refreshProducts();
      refreshCart();
      if (activeUser instanceof Admin) {
        refreshUsers();
        refreshTransactions();
        showAdmin();
      } else {
        showCustomer();
      }
    });

    showPassBtn.addActionListener(ev -> togglePasswordVisibility(passwordField, showPassBtn.isSelected()));
    toRegister.addActionListener(e -> showRegister());
    backBtn.addActionListener(e -> showWelcome());

    JPanel wrapper = new JPanel(new GridBagLayout());
    wrapper.setOpaque(false);
    wrapper.add(form);
    panel.add(wrapper, BorderLayout.CENTER);
    return panel;
  }

  // Register Panel
  private JLabel registerMessage;

  private JPanel buildRegisterPanel() {
    JPanel panel = new JPanel(new BorderLayout()) {
      @Override
      protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int w = getWidth();
        int h = getHeight();

        // Large ambient liquid glow
        RadialGradientPaint ambient = new RadialGradientPaint(
            w * 0.5f, h * 0.45f, Math.max(w, h) * 0.85f,
            new float[] { 0f, 0.3f, 0.6f, 1f },
            new Color[] { new Color(139, 92, 246, 65), new Color(99, 102, 241, 40),
                new Color(79, 70, 229, 20), new Color(0, 0, 0, 0) });
        g2.setPaint(ambient);
        g2.fillRect(0, 0, w, h);

        // Top-right liquid blob - cyan
        RadialGradientPaint topRight = new RadialGradientPaint(
            w * 0.85f, h * 0.15f, w * 0.45f,
            new float[] { 0f, 0.35f, 1f },
            new Color[] { new Color(34, 211, 238, 50), new Color(99, 102, 241, 22), new Color(0, 0, 0, 0) });
        g2.setPaint(topRight);
        g2.fillRect(0, 0, w, h);

        // Bottom-left liquid blob - pink
        RadialGradientPaint bottomLeft = new RadialGradientPaint(
            w * 0.15f, h * 0.85f, w * 0.5f,
            new float[] { 0f, 0.35f, 1f },
            new Color[] { new Color(236, 72, 153, 45), new Color(139, 92, 246, 20), new Color(0, 0, 0, 0) });
        g2.setPaint(bottomLeft);
        g2.fillRect(0, 0, w, h);

        // Center subtle glow for form focus
        RadialGradientPaint centerGlow = new RadialGradientPaint(
            w * 0.5f, h * 0.5f, Math.min(w, h) * 0.4f,
            new float[] { 0f, 0.5f, 1f },
            new Color[] { new Color(255, 255, 255, 12), new Color(139, 92, 246, 6), new Color(0, 0, 0, 0) });
        g2.setPaint(centerGlow);
        g2.fillRect(0, 0, w, h);

        g2.dispose();
      }
    };
    panel.setOpaque(false);

    GlassPanel form = new GlassPanel(24);
    form.setBorder(new EmptyBorder(50, 60, 50, 60));
    form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
    form.setAlignmentX(Component.CENTER_ALIGNMENT);

    // Icon
    JLabel avatarLabel = new JLabel("\uD83D\uDC65", SwingConstants.CENTER);
    avatarLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
    avatarLabel.setForeground(ACCENT);
    avatarLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    form.add(avatarLabel);
    form.add(Box.createVerticalStrut(16));

    JLabel title = new JLabel("Create Account");
    title.setFont(new Font("Segoe UI", Font.BOLD, 28));
    title.setForeground(TEXT);
    title.setAlignmentX(Component.CENTER_ALIGNMENT);
    form.add(title);
    form.add(Box.createVerticalStrut(6));

    JLabel subtitle = new JLabel("Join us and start shopping today");
    subtitle.setFont(BODY_FONT);
    subtitle.setForeground(MUTED);
    subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
    form.add(subtitle);
    form.add(Box.createVerticalStrut(35));

    JTextField usernameField = styledGlassField();
    Dimension fieldSize = new Dimension(320, 44);
    usernameField.setPreferredSize(fieldSize);
    usernameField.setMaximumSize(fieldSize);
    applyPlaceholder(usernameField, "Choose a username");

    JPasswordField passwordField = styledGlassPasswordField();
    passwordField.setPreferredSize(fieldSize);
    passwordField.setMaximumSize(fieldSize);
    setupPasswordPlaceholder(passwordField, "Create a password");

    form.add(usernameField);
    form.add(Box.createVerticalStrut(16));
    form.add(passwordField);
    form.add(Box.createVerticalStrut(12));

    // Styled show password toggle with label
    JPanel showPassPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
    showPassPanel.setOpaque(false);
    showPassPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
    showPassPanel.setMaximumSize(new Dimension(320, 32));

    JToggleButton showPassBtn = createStyledToggle("\uD83D\uDC41  Show Password");
    JLabel showPassLabel = new JLabel("Show Password");
    showPassLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
    showPassLabel.setForeground(MUTED);
    showPassPanel.add(showPassBtn);
    showPassPanel.add(showPassLabel);
    form.add(showPassPanel);
    form.add(Box.createVerticalStrut(25));

    JButton registerBtn = glassButton("Create Account  →");
    registerBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
    registerBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
    Dimension btnSize = new Dimension(320, 48);
    registerBtn.setPreferredSize(btnSize);
    registerBtn.setMaximumSize(btnSize);
    form.add(registerBtn);
    form.add(Box.createVerticalStrut(16));

    registerMessage = new JLabel(" ");
    registerMessage.setForeground(new Color(248, 113, 113));
    registerMessage.setFont(BODY_FONT);
    registerMessage.setAlignmentX(Component.CENTER_ALIGNMENT);
    form.add(registerMessage);
    form.add(Box.createVerticalStrut(25));

    // Divider
    JSeparator divider = new JSeparator();
    divider.setForeground(GLASS_BORDER);
    divider.setMaximumSize(new Dimension(280, 1));
    form.add(divider);
    form.add(Box.createVerticalStrut(20));

    JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    backPanel.setOpaque(false);
    JLabel hasAccount = new JLabel("Already have an account?");
    hasAccount.setForeground(MUTED);
    hasAccount.setFont(BODY_FONT);
    JButton toLogin = new JButton("Sign in");
    toLogin.setForeground(ACCENT);
    toLogin.setFont(new Font("Segoe UI", Font.BOLD, 13));
    toLogin.setBorderPainted(false);
    toLogin.setContentAreaFilled(false);
    toLogin.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    backPanel.add(hasAccount);
    backPanel.add(toLogin);
    form.add(backPanel);

    JButton backBtn = glassButtonOutline("← Back to Welcome");
    backBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
    form.add(Box.createVerticalStrut(12));
    form.add(backBtn);

    registerBtn.addActionListener(e -> {
      registerMessage.setForeground(new Color(248, 113, 113));
      String user = usernameField.getText().trim();
      String pass = new String(passwordField.getPassword());
      if (user.isEmpty() || pass.isEmpty() || user.equals("Choose a username") || pass.equals("Create a password")) {
        registerMessage.setText("Please fill in all fields.");
        return;
      }
      if (pass.length() < 4) {
        registerMessage.setText("Password must be at least 4 characters.");
        return;
      }
      if (system.register(user, pass)) {
        system.saveData();
        registerMessage.setText(" ");
        usernameField.setText("Choose a username");
        usernameField.setForeground(MUTED);
        passwordField.setText("Create a password");
        passwordField.setEchoChar((char) 0);
        passwordField.setForeground(MUTED);
        showLogin();
        loginMessage.setForeground(new Color(134, 239, 172));
        loginMessage.setText("✓ Account created successfully! Please login.");
      } else {
        registerMessage.setText("Username already exists. Try another.");
      }
    });

    showPassBtn.addActionListener(ev -> togglePasswordVisibility(passwordField, showPassBtn.isSelected()));
    toLogin.addActionListener(e -> showLogin());
    backBtn.addActionListener(e -> showWelcome());

    JPanel wrapper = new JPanel(new GridBagLayout());
    wrapper.setOpaque(false);
    wrapper.add(form);
    panel.add(wrapper, BorderLayout.CENTER);
    return panel;
  }

  private JPanel buildCustomerPanel() {
    JPanel panel = new JPanel(new BorderLayout());
    panel.setOpaque(false);

    welcomeLabel = new JLabel("Hello, customer");
    welcomeLabel.setFont(SUBTITLE_FONT);
    welcomeLabel.setForeground(TEXT);
    welcomeLabel.setBorder(new EmptyBorder(12, 16, 12, 16));
    panel.add(welcomeLabel, BorderLayout.NORTH);

    customerTable = new JTable(productModel);
    styleGlassTable(customerTable);
    JScrollPane productScroll = styledGlassScroll(customerTable);

    cartArea = new JTextArea();
    cartArea.setEditable(false);
    cartArea.setFont(BODY_FONT);
    cartArea.setOpaque(false);
    cartArea.setForeground(TEXT);
    cartArea.setBorder(new EmptyBorder(8, 8, 8, 8));
    JScrollPane cartScroll = new JScrollPane(cartArea);
    cartScroll.setPreferredSize(new Dimension(320, 240));
    cartScroll.getViewport().setOpaque(false);
    cartScroll.setBorder(
        BorderFactory.createTitledBorder(BorderFactory.createLineBorder(GLASS_BORDER), "Your Cart"));
    cartScroll.setOpaque(false);

    JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, productScroll, cartScroll);
    split.setResizeWeight(0.62);
    split.setBorder(null);
    split.setOpaque(false);
    panel.add(split, BorderLayout.CENTER);

    GlassPanel actions = new GlassPanel(14);
    actions.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
    JLabel qtyLabel = new JLabel("Qty:");
    qtyLabel.setForeground(TEXT);
    qtyLabel.setFont(BODY_FONT);
    JSpinner qtySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
    qtySpinner.setFont(BODY_FONT);
    JButton addBtn = glassButton("Add to Cart");
    JButton checkoutBtn = glassButtonOutline("Checkout");
    JButton logoutBtn = glassButtonOutline("Logout");
    totalLabel = new JLabel("Total: $0.00");
    totalLabel.setFont(SUBTITLE_FONT);
    totalLabel.setForeground(ACCENT);

    actions.add(qtyLabel);
    actions.add(qtySpinner);
    actions.add(addBtn);
    actions.add(checkoutBtn);
    actions.add(totalLabel);
    actions.add(logoutBtn);

    panel.add(actions, BorderLayout.SOUTH);

    addBtn.addActionListener(e -> {
      int row = customerTable.getSelectedRow();
      if (row < 0) {
        JOptionPane.showMessageDialog(this, "Select a product first.");
        return;
      }
      String id = productModel.getValueAt(row, 0).toString();
      int qty = (int) qtySpinner.getValue();
      if (system.addProductToCart(id, qty)) {
        refreshProducts();
        refreshCart();
      } else {
        JOptionPane.showMessageDialog(this, "Product not found or insufficient stock.");
      }
    });

    checkoutBtn.addActionListener(e -> {
      double total = system.getCartTotal();
      if (activeUser == null) {
        JOptionPane.showMessageDialog(this, "Please login before checkout.");
        return;
      }
      if (total <= 0) {
        JOptionPane.showMessageDialog(this, "Cart is empty.");
        return;
      }
      boolean paid = system.processPayment(total);
      if (!paid) {
        JOptionPane.showMessageDialog(this, "Checkout failed. Please refresh products and try again.");
        refreshProducts();
        return;
      }
      system.saveData();
      refreshCart();
      refreshProducts();
      JOptionPane.showMessageDialog(this, "Payment processed. Thank you!");
    });

    logoutBtn.addActionListener(e -> logout());

    return panel;
  }

  private JPanel buildAdminPanel() {
    JPanel panel = new JPanel(new BorderLayout());
    panel.setOpaque(false);

    JLabel title = new JLabel("Admin Dashboard");
    title.setFont(SUBTITLE_FONT);
    title.setForeground(TEXT);
    title.setBorder(new EmptyBorder(12, 16, 12, 16));
    panel.add(title, BorderLayout.NORTH);

    adminTable = new JTable(productModel);
    styleGlassTable(adminTable);
    JScrollPane productScroll = styledGlassScroll(adminTable);

    JPanel right = new JPanel();
    right.setOpaque(false);
    right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));

    GlassPanel addForm = new GlassPanel(14);
    addForm.setLayout(new GridLayout(5, 2, 4, 4));
    addForm.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createTitledBorder(BorderFactory.createLineBorder(GLASS_BORDER), "Add Product"),
        new EmptyBorder(10, 10, 10, 10)));
    JTextField idField = styledGlassField();
    JTextField nameField = styledGlassField();
    JTextField priceField = styledGlassField();
    JTextField stockField = styledGlassField();
    JButton addBtn = glassButton("Add");
    addForm.add(labeled("ID"));
    addForm.add(idField);
    addForm.add(labeled("Name"));
    addForm.add(nameField);
    addForm.add(labeled("Price"));
    addForm.add(priceField);
    addForm.add(labeled("Stock"));
    addForm.add(stockField);
    addForm.add(new JLabel(""));
    addForm.add(addBtn);

    JButton removeBtn = glassButtonOutline("Remove Selected Product");
    removeBtn.setAlignmentX(Component.LEFT_ALIGNMENT);

    userArea = styledGlassArea();
    JScrollPane userScroll = styledGlassInfoPane(userArea, "Users");

    transactionArea = styledGlassArea();
    JScrollPane txnScroll = styledGlassInfoPane(transactionArea, "Transactions");

    GlassPanel removeUserPanel = new GlassPanel(12);
    removeUserPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 8, 8));
    removeUserPanel.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createTitledBorder(BorderFactory.createLineBorder(GLASS_BORDER), "Remove User"),
        new EmptyBorder(8, 8, 8, 8)));
    JTextField removeUserField = styledGlassField();
    removeUserField.setPreferredSize(new Dimension(220, 32));
    removeUserField.setMaximumSize(new Dimension(220, 32));
    JLabel removeUserLabel = labeled("Username");
    JButton removeUserBtn = glassButton("Remove");
    removeUserPanel.add(removeUserLabel);
    removeUserPanel.add(removeUserField);
    removeUserPanel.add(removeUserBtn);

    JButton refreshBtn = glassButtonOutline("Refresh Data");
    JButton logoutBtn = glassButtonOutline("Logout");

    right.add(addForm);
    right.add(Box.createVerticalStrut(8));
    right.add(removeBtn);
    right.add(Box.createVerticalStrut(12));
    right.add(userScroll);
    right.add(Box.createVerticalStrut(8));
    right.add(removeUserPanel);
    right.add(Box.createVerticalStrut(8));
    right.add(txnScroll);
    right.add(Box.createVerticalStrut(10));
    right.add(refreshBtn);
    right.add(Box.createVerticalStrut(6));
    right.add(logoutBtn);

    JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, productScroll, right);
    split.setResizeWeight(0.55);
    split.setBorder(null);
    split.setOpaque(false);
    panel.add(split, BorderLayout.CENTER);

    addBtn.addActionListener(e -> {
      try {
        String id = idField.getText().trim();
        String name = nameField.getText().trim();
        double price = Double.parseDouble(priceField.getText().trim());
        int stock = Integer.parseInt(stockField.getText().trim());
        if (system.addProduct(id, name, price, stock)) {
          JOptionPane.showMessageDialog(this, "Product added.");
          refreshProducts();
          system.saveData();
        } else {
          JOptionPane.showMessageDialog(this, "ID already exists.");
        }
      } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Invalid price or stock.");
      }
    });

    removeBtn.addActionListener(e -> {
      int row = adminTable.getSelectedRow();
      if (row < 0) {
        JOptionPane.showMessageDialog(this, "Select a product first.");
        return;
      }
      String id = productModel.getValueAt(row, 0).toString();
      if (system.removeProduct(id)) {
        JOptionPane.showMessageDialog(this, "Product removed.");
        refreshProducts();
        system.saveData();
      }
    });

    removeUserBtn.addActionListener(e -> {
      String uname = removeUserField.getText().trim();
      if (uname.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Enter a username to remove.");
        return;
      }
      if (activeUser != null && activeUser.getUsername().equals(uname)) {
        JOptionPane.showMessageDialog(this, "You cannot remove the currently logged-in admin.");
        return;
      }
      boolean ok = system.removeUser(uname);
      if (ok) {
        JOptionPane.showMessageDialog(this, "User removed successfully.");
        removeUserField.setText("");
        refreshUsers();
        system.saveData();
      } else {
        JOptionPane.showMessageDialog(this, "User not found or cannot remove admin.");
      }
    });

    refreshBtn.addActionListener(e -> {
      refreshProducts();
      refreshUsers();
      refreshTransactions();
    });

    logoutBtn.addActionListener(e -> logout());

    return panel;
  }

  private void refreshProducts() {
    productModel.setRowCount(0);
    List<Product> list = system.getProducts();
    for (Product p : list) {
      productModel.addRow(new Object[] { p.getId(), p.getName(), p.getPrice(), p.getStock() });
    }
  }

  private void refreshCart() {
    if (cartArea == null)
      return;
    StringBuilder sb = new StringBuilder();
    double total = 0;
    for (CartItem item : system.getCart().getItems()) {
      double line = item.getTotal();
      sb.append(item.getProduct().getName()).append(" x").append(item.getQuantity()).append(" = $")
          .append(String.format("%.2f", line)).append("\n");
      total += line;
    }
    if (sb.length() == 0) {
      cartArea.setText("Cart is empty.");
    } else {
      cartArea.setText(sb.toString());
    }
    if (totalLabel != null)
      totalLabel.setText("Total: $" + String.format("%.2f", total));
  }

  private void refreshUsers() {
    if (userArea == null)
      return;
    StringBuilder sb = new StringBuilder();
    for (User u : system.getUsers()) {
      sb.append(u.getId()).append(" | ").append(u.getUsername()).append(" | ").append(u.getType()).append("\n");
    }
    userArea.setText(sb.toString());
  }

  private void refreshTransactions() {
    if (transactionArea == null)
      return;
    StringBuilder sb = new StringBuilder();
    double total = 0;
    for (Transaction t : system.getTransactions()) {
      sb.append(t.toString()).append("\n");
      total += t.getAmount();
    }
    sb.append("Total Revenue: $").append(String.format("%.2f", total));
    transactionArea.setText(sb.toString());
  }

  private void logout() {
    system.logout();
    activeUser = null;
    refreshProducts();
    refreshCart();
    showWelcome();
  }

  private void showWelcome() {
    currentBackground = loginBackground;
    getContentPane().repaint();
    cardLayout.show(cardPanel, "WELCOME");
  }

  private void showLogin() {
    currentBackground = loginBackground;
    getContentPane().repaint();
    if (loginMessage != null)
      loginMessage.setText(" ");
    cardLayout.show(cardPanel, "LOGIN");
  }

  private void showRegister() {
    currentBackground = loginBackground;
    getContentPane().repaint();
    if (registerMessage != null)
      registerMessage.setText(" ");
    cardLayout.show(cardPanel, "REGISTER");
  }

  private void showCustomer() {
    currentBackground = dashboardBackground;
    getContentPane().repaint();
    cardLayout.show(cardPanel, "CUSTOMER");
  }

  private void showAdmin() {
    currentBackground = dashboardBackground;
    getContentPane().repaint();
    cardLayout.show(cardPanel, "ADMIN");
  }

  private JTextField styledGlassField() {
    JTextField field = new JTextField() {
      @Override
      protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Glass background - dark theme
        g2.setColor(new Color(0, 0, 0, 80));
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

        // Glass border
        g2.setColor(GLASS_BORDER);
        g2.setStroke(new BasicStroke(1f));
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);

        g2.dispose();
        super.paintComponent(g);
      }
    };
    field.setFont(BODY_FONT);
    field.setOpaque(false);
    field.setForeground(TEXT);
    field.setCaretColor(ACCENT);
    field.setBorder(new EmptyBorder(8, 12, 8, 12));
    field.setColumns(16);
    Dimension size = new Dimension(280, 36);
    field.setPreferredSize(size);
    field.setMaximumSize(size);
    field.setAlignmentX(Component.CENTER_ALIGNMENT);
    return field;
  }

  private JLabel labeled(String text) {
    JLabel l = new JLabel(text);
    l.setForeground(TEXT);
    l.setFont(BODY_FONT);
    return l;
  }

  private JButton glassButton(String text) {
    JButton b = new JButton(text) {
      @Override
      protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color btnColor = getModel().isArmed() ? ACCENT_DARK : ACCENT;
        g2.setColor(btnColor);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);

        // Glass highlight
        GradientPaint highlight = new GradientPaint(0, 0, new Color(255, 255, 255, 60),
            0, getHeight() / 2, new Color(255, 255, 255, 0));
        g2.setPaint(highlight);
        g2.fillRoundRect(1, 1, getWidth() - 2, getHeight() / 2, 12, 12);

        g2.dispose();
        super.paintComponent(g);
      }
    };
    b.setFont(BODY_FONT);
    b.setForeground(Color.WHITE);
    b.setOpaque(false);
    b.setContentAreaFilled(false);
    b.setBorderPainted(false);
    b.setFocusPainted(false);
    b.setBorder(BorderFactory.createEmptyBorder(10, 24, 10, 24));
    return b;
  }

  private JButton glassButtonOutline(String text) {
    JButton b = new JButton(text) {
      @Override
      protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Dark glass background
        g2.setColor(new Color(0, 0, 0, getModel().isArmed() ? 100 : 60));
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);

        // Border
        g2.setColor(new Color(99, 102, 241, 80));
        g2.setStroke(new BasicStroke(1f));
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);

        g2.dispose();
        super.paintComponent(g);
      }
    };
    b.setFont(BODY_FONT);
    b.setForeground(TEXT);
    b.setOpaque(false);
    b.setContentAreaFilled(false);
    b.setBorderPainted(false);
    b.setFocusPainted(false);
    b.setBorder(BorderFactory.createEmptyBorder(10, 24, 10, 24));
    return b;
  }

  private void styleTable(JTable table) {
    table.setFont(BODY_FONT);
    table.setForeground(TEXT);
    table.setBackground(new Color(255, 255, 255, 8));
    table.setRowHeight(26);
    table.setGridColor(new Color(255, 255, 255, 20));
    table.setSelectionBackground(ACCENT);
    table.setSelectionForeground(Color.WHITE);
    JTableHeader header = table.getTableHeader();
    header.setFont(BODY_FONT);
    header.setOpaque(false);
    header.setBackground(new Color(255, 255, 255, 12));
    header.setForeground(TEXT);
    javax.swing.table.DefaultTableCellRenderer headerRenderer = new javax.swing.table.DefaultTableCellRenderer();
    headerRenderer.setOpaque(false);
    headerRenderer.setBackground(new Color(255, 255, 255, 12));
    headerRenderer.setForeground(TEXT);
    headerRenderer.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    header.setDefaultRenderer(headerRenderer);
  }

  private JScrollPane styledScroll(JTable table) {
    JScrollPane sp = new JScrollPane(table);
    sp.getViewport().setOpaque(false);
    sp.getViewport().setBackground(new Color(255, 255, 255, 5));
    sp.setOpaque(false);
    sp.setBorder(BorderFactory.createLineBorder(GLASS_BORDER, 1));
    return sp;
  }

  private void styleGlassTable(JTable table) {
    table.setFont(BODY_FONT);
    table.setForeground(TEXT);
    table.setBackground(new Color(255, 255, 255, 8));
    table.setRowHeight(26);
    table.setGridColor(new Color(255, 255, 255, 20));
    table.setSelectionBackground(ACCENT);
    table.setSelectionForeground(Color.WHITE);
    table.setOpaque(false);
    JTableHeader header = table.getTableHeader();
    header.setFont(BODY_FONT);
    header.setOpaque(false);
    header.setBackground(new Color(255, 255, 255, 15));
    header.setForeground(TEXT);
    javax.swing.table.DefaultTableCellRenderer headerRenderer = new javax.swing.table.DefaultTableCellRenderer();
    headerRenderer.setOpaque(false);
    headerRenderer.setBackground(new Color(255, 255, 255, 15));
    headerRenderer.setForeground(TEXT);
    headerRenderer.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    header.setDefaultRenderer(headerRenderer);
  }

  private JScrollPane styledGlassScroll(JTable table) {
    JScrollPane sp = new JScrollPane(table);
    sp.getViewport().setOpaque(false);
    sp.setOpaque(false);
    sp.setBorder(BorderFactory.createLineBorder(GLASS_BORDER, 1));
    return sp;
  }

  private JTextArea styledArea() {
    JTextArea area = new JTextArea(8, 20);
    area.setEditable(false);
    area.setFont(BODY_FONT);
    area.setBackground(new Color(255, 255, 255, 5));
    area.setOpaque(false);
    area.setForeground(TEXT);
    area.setBorder(new EmptyBorder(8, 8, 8, 8));
    return area;
  }

  private JScrollPane styledInfoPane(JTextArea area, String title) {
    JScrollPane sp = new JScrollPane(area);
    sp.getViewport().setOpaque(false);
    sp.getViewport().setBackground(new Color(255, 255, 255, 5));
    sp.setOpaque(false);
    sp.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(GLASS_BORDER), title));
    return sp;
  }

  private JTextArea styledGlassArea() {
    JTextArea area = new JTextArea(8, 20);
    area.setEditable(false);
    area.setFont(BODY_FONT);
    area.setOpaque(false);
    area.setForeground(TEXT);
    area.setBorder(new EmptyBorder(8, 8, 8, 8));
    return area;
  }

  private JScrollPane styledGlassInfoPane(JTextArea area, String title) {
    JScrollPane sp = new JScrollPane(area);
    sp.getViewport().setOpaque(false);
    sp.setOpaque(false);
    sp.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(GLASS_BORDER), title));
    return sp;
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> new ECommerceGUI().setVisible(true));
  }

  // --- Styling helpers ---
  private JPasswordField styledGlassPasswordField() {
    JPasswordField field = new JPasswordField() {
      @Override
      protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Glass background - dark theme (same as styledGlassField)
        g2.setColor(new Color(0, 0, 0, 80));
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

        // Glass border
        g2.setColor(GLASS_BORDER);
        g2.setStroke(new BasicStroke(1f));
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);

        g2.dispose();
        super.paintComponent(g);
      }
    };
    field.setFont(BODY_FONT);
    field.setOpaque(false);
    field.setForeground(TEXT);
    field.setCaretColor(ACCENT);
    field.setBorder(new EmptyBorder(8, 12, 8, 12));
    field.setColumns(16);
    Dimension size = new Dimension(280, 36);
    field.setPreferredSize(size);
    field.setMaximumSize(size);
    field.setAlignmentX(Component.CENTER_ALIGNMENT);
    return field;
  }

  private void applyPlaceholder(JTextField field, String placeholder) {
    field.setText(placeholder);
    field.setForeground(MUTED);
    field.addFocusListener(new java.awt.event.FocusAdapter() {
      @Override
      public void focusGained(java.awt.event.FocusEvent e) {
        if (field.getText().equals(placeholder)) {
          field.setText("");
          field.setForeground(TEXT);
        }
      }

      @Override
      public void focusLost(java.awt.event.FocusEvent e) {
        if (field.getText().trim().isEmpty()) {
          field.setText(placeholder);
          field.setForeground(MUTED);
        }
      }
    });
  }

  private void setupPasswordPlaceholder(JPasswordField field, String placeholder) {
    final char bullet = '•';
    field.setEchoChar((char) 0);
    field.setText(placeholder);
    field.setForeground(MUTED);
    field.addFocusListener(new java.awt.event.FocusAdapter() {
      @Override
      public void focusGained(java.awt.event.FocusEvent e) {
        String txt = new String(field.getPassword());
        if (txt.equals(placeholder)) {
          field.setText("");
          field.setForeground(TEXT);
          field.setEchoChar(bullet);
        }
      }

      @Override
      public void focusLost(java.awt.event.FocusEvent e) {
        String txt = new String(field.getPassword());
        if (txt.trim().isEmpty()) {
          field.setEchoChar((char) 0);
          field.setText(placeholder);
          field.setForeground(MUTED);
        }
      }
    });
  }

  private void togglePasswordVisibility(JPasswordField field, boolean show) {
    String placeholder = "your-password";
    String current = new String(field.getPassword());
    if (current.equals(placeholder) && show) {
      field.setEchoChar((char) 0);
      return;
    }
    field.setEchoChar(show ? (char) 0 : '•');
  }

  // Create styled toggle button for show password with eye icon animation
  private JToggleButton createStyledToggle(String text) {
    JToggleButton toggle = new JToggleButton() {
      @Override
      protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        // Background pill shape
        if (isSelected()) {
          // Active state - accent color glow
          g2.setColor(new Color(99, 102, 241, 60));
          g2.fillRoundRect(0, 0, w, h, h, h);
          g2.setColor(new Color(99, 102, 241, 100));
          g2.setStroke(new BasicStroke(1.5f));
          g2.drawRoundRect(1, 1, w - 2, h - 2, h, h);
        } else {
          // Inactive state
          g2.setColor(new Color(255, 255, 255, 15));
          g2.fillRoundRect(0, 0, w, h, h, h);
          g2.setColor(new Color(255, 255, 255, 30));
          g2.setStroke(new BasicStroke(1f));
          g2.drawRoundRect(0, 0, w - 1, h - 1, h, h);
        }

        // Draw toggle circle
        int circleSize = h - 8;
        int circleY = 4;
        int circleX = isSelected() ? w - circleSize - 6 : 6;

        g2.setColor(isSelected() ? ACCENT : new Color(180, 180, 180));
        g2.fillOval(circleX, circleY, circleSize, circleSize);

        // Draw eye icon in circle
        g2.setColor(isSelected() ? Color.WHITE : new Color(60, 60, 60));
        int eyeX = circleX + circleSize / 2;
        int eyeY = circleY + circleSize / 2;
        int eyeW = circleSize / 2;
        int eyeH = circleSize / 3;

        // Eye shape
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawOval(eyeX - eyeW / 2, eyeY - eyeH / 2, eyeW, eyeH);
        g2.fillOval(eyeX - 2, eyeY - 2, 4, 4);

        // Strike-through line when hidden
        if (!isSelected()) {
          g2.setColor(new Color(60, 60, 60));
          g2.drawLine(eyeX - eyeW / 2 - 2, eyeY + eyeH / 2 + 2, eyeX + eyeW / 2 + 2, eyeY - eyeH / 2 - 2);
        }

        g2.dispose();
      }
    };
    toggle.setOpaque(false);
    toggle.setContentAreaFilled(false);
    toggle.setBorderPainted(false);
    toggle.setFocusPainted(false);
    toggle.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    toggle.setPreferredSize(new Dimension(56, 28));
    toggle.setToolTipText("Show/Hide Password");
    return toggle;
  }
}
