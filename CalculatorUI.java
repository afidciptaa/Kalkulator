import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Ultra-Modern Neo-Fluent Desktop Calculator UI.
 * Features glassmorphic cards, squircle buttons, gradient equals pill,
 * integrated status badges, and 100% pure Java2D vector icons.
 */
public class CalculatorUI {
    private final JFrame frame;
    private final CalculatorEngine engine;
    private final HistoryManager historyManager;

    // Theme state
    private boolean isDark = true;
    private Theme currentTheme = Theme.DARK;

    // Mode state
    private boolean isScientific = false;
    private boolean showHistory = false;

    // Calculator input state
    private String currentInput = "0";
    private String expression = "";
    private boolean isNewCalculation = true;

    // UI Header Components
    private JPanel headerPanel;
    private JLabel logoIconLabel;
    private JLabel appTitleLabel;
    private ModernButton modeBtn;
    private ModernButton themeBtn;
    private ModernButton historyBtn;
    private ModernButton clearHistBtn;

    // Display Card Components
    private JPanel displayCard;
    private JLabel modeBadge;
    private ModernButton angleBadgeBtn;
    private JLabel memoryIndicator;
    private JLabel copyToastLabel;
    private ModernButton copyBtn;
    private JLabel exprLabel;
    private JLabel resultLabel;

    // Memory Bar
    private JPanel memPanel;
    private final List<ModernButton> memButtons = new ArrayList<>();

    // Keypad Area
    private JPanel centerPanel;
    private JPanel keypadPanel;
    private final List<ButtonMeta> keypadButtons = new ArrayList<>();

    // Sidebar
    private JPanel sidebarPanel;
    private JPanel historyListPanel;
    private JScrollPane historyScrollPane;

    private static class ButtonMeta {
        final ModernButton button;
        final String type; // "num", "op", "sci", "action", "equals"
        final String actionId;

        ButtonMeta(ModernButton button, String type, String actionId) {
            this.button = button;
            this.type = type;
            this.actionId = actionId;
        }
    }

    public CalculatorUI() {
        this.engine = new CalculatorEngine();
        this.historyManager = new HistoryManager();

        frame = new JFrame("Kalkulator Desktop Modern");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(430, 680);
        frame.setMinimumSize(new Dimension(390, 620));
        frame.setLocationRelativeTo(null);

        // Set multi-resolution application icons
        frame.setIconImages(IconFactory.getAppIconList());
        IconFactory.saveIconFileIfMissing("icon.png");

        buildUI();
        setupGlobalKeyBindings();
        updateDisplay();
    }

    public void show() {
        frame.setVisible(true);
    }

    private void buildUI() {
        JPanel contentPane = new JPanel(new BorderLayout(0, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                // Subtle vertical background gradient for rich depth
                GradientPaint bgGrad = new GradientPaint(
                        0, 0, currentTheme.bgMain,
                        0, getHeight(), isDark ? new Color(0x13, 0x16, 0x22) : new Color(0xE8, 0xEE, 0xF5)
                );
                g2.setPaint(bgGrad);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        contentPane.setBackground(currentTheme.bgMain);
        frame.setContentPane(contentPane);

        // 1. Sleek Modern Header Bar
        buildHeader(contentPane);

        // 2. Center Container (Display Card + Memory Bar + Keypad + History Sidebar)
        centerPanel = new JPanel(new BorderLayout(0, 0));
        centerPanel.setOpaque(false);
        centerPanel.setBorder(new EmptyBorder(0, 14, 14, 14));

        // Top Section of Center: Display Card + Memory Bar
        JPanel topSection = new JPanel(new BorderLayout(0, 6));
        topSection.setOpaque(false);

        buildDisplayCard(topSection);
        buildMemoryBar(topSection);

        centerPanel.add(topSection, BorderLayout.NORTH);

        // Keypad
        keypadPanel = new JPanel();
        keypadPanel.setOpaque(false);
        renderKeypad();
        centerPanel.add(keypadPanel, BorderLayout.CENTER);

        // History Sidebar
        buildSidebar();

        contentPane.add(centerPanel, BorderLayout.CENTER);
    }

    private void buildHeader(JPanel contentPane) {
        headerPanel = new JPanel(new BorderLayout(10, 0));
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(10, 14, 8, 14));

        // Header Left: App Icon + Title + PRO Badge
        JPanel headerLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        headerLeft.setOpaque(false);

        logoIconLabel = new JLabel(new ImageIcon(IconFactory.createAppIcon(26)));
        headerLeft.add(logoIconLabel);

        appTitleLabel = new JLabel("Kalkulator");
        appTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        appTitleLabel.setForeground(currentTheme.fgPrimary);
        headerLeft.add(appTitleLabel);

        headerPanel.add(headerLeft, BorderLayout.WEST);

        // Header Right: Mode Pill + Theme Toggle + History Toggle
        JPanel headerRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        headerRight.setOpaque(false);

        // Mode switch pill button
        modeBtn = new ModernButton("Standar", currentTheme.bgCard, currentTheme.fgPrimary,
                currentTheme.bgNumHover, new Font("Segoe UI", Font.BOLD, 11));
        modeBtn.setIcon(IconFactory.createMenuIcon(13, currentTheme.fgPrimary));
        modeBtn.setPreferredSize(new Dimension(95, 30));
        modeBtn.setCornerRadius(15); // Pill shape
        modeBtn.setBorderColor(currentTheme.border);
        modeBtn.setToolTipText("Ganti Mode (Standar / Ilmiah) [Ctrl+S]");
        modeBtn.addActionListener(e -> toggleMode());
        headerRight.add(modeBtn);

        // Theme toggle circular button
        themeBtn = new ModernButton("", currentTheme.bgCard, currentTheme.fgPrimary,
                currentTheme.bgNumHover, new Font("Segoe UI", Font.PLAIN, 12));
        themeBtn.setIcon(IconFactory.createSunIcon(15, currentTheme.fgPrimary));
        themeBtn.setPreferredSize(new Dimension(32, 30));
        themeBtn.setCornerRadius(15); // Circle pill
        themeBtn.setBorderColor(currentTheme.border);
        themeBtn.setToolTipText("Ganti Tema Gelap/Terang [Ctrl+T]");
        themeBtn.addActionListener(e -> toggleTheme());
        headerRight.add(themeBtn);

        // History toggle pill button
        historyBtn = new ModernButton("Riwayat", currentTheme.bgCard, currentTheme.fgPrimary,
                currentTheme.bgNumHover, new Font("Segoe UI", Font.PLAIN, 11));
        historyBtn.setIcon(IconFactory.createHistoryIcon(13, currentTheme.fgPrimary));
        historyBtn.setPreferredSize(new Dimension(90, 30));
        historyBtn.setCornerRadius(15); // Pill shape
        historyBtn.setBorderColor(currentTheme.border);
        historyBtn.setToolTipText("Buka / Tutup Riwayat [Ctrl+H]");
        historyBtn.addActionListener(e -> toggleHistory());
        headerRight.add(historyBtn);

        headerPanel.add(headerRight, BorderLayout.EAST);
        contentPane.add(headerPanel, BorderLayout.NORTH);
    }

    private void buildDisplayCard(JPanel parent) {
        // Elevated glassmorphic card for calculation readout
        displayCard = new JPanel(new BorderLayout(0, 4)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth();
                int h = getHeight();

                // Card background
                g2.setColor(currentTheme.bgDisplay);
                g2.fill(new RoundRectangle2D.Float(0, 0, w, h, 18, 18));

                // Subtle top highlight border
                g2.setColor(new Color(255, 255, 255, isDark ? 25 : 80));
                g2.setStroke(new BasicStroke(1.2f));
                g2.draw(new RoundRectangle2D.Float(1, 1, w - 2, h - 2, 17, 17));

                // Subtle outer border
                g2.setColor(currentTheme.border);
                g2.setStroke(new BasicStroke(1.0f));
                g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, w - 1, h - 1, 18, 18));

                g2.dispose();
                super.paintComponent(g);
            }
        };
        displayCard.setOpaque(false);
        displayCard.setBorder(new EmptyBorder(10, 16, 12, 16));

        // 1. Status Badges Row (Mode, Angle, Memory, Copy)
        JPanel metaRow = new JPanel(new BorderLayout(6, 0));
        metaRow.setOpaque(false);

        JPanel badgesLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        badgesLeft.setOpaque(false);

        modeBadge = new JLabel("STANDAR");
        modeBadge.setFont(new Font("Segoe UI", Font.BOLD, 10));
        modeBadge.setForeground(currentTheme.fgDim);
        badgesLeft.add(modeBadge);

        angleBadgeBtn = new ModernButton(engine.getAngleMode(), currentTheme.bgSci, currentTheme.fgSci,
                currentTheme.bgSciHover, new Font("Segoe UI", Font.BOLD, 10));
        angleBadgeBtn.setPreferredSize(new Dimension(48, 20));
        angleBadgeBtn.setCornerRadius(6);
        angleBadgeBtn.setToolTipText("Klik untuk beralih DEG / RAD");
        angleBadgeBtn.addActionListener(e -> toggleAngleMode());
        angleBadgeBtn.setVisible(false); // Only in scientific mode
        badgesLeft.add(angleBadgeBtn);

        memoryIndicator = new JLabel("● M");
        memoryIndicator.setFont(new Font("Segoe UI", Font.BOLD, 10));
        memoryIndicator.setForeground(currentTheme.memInactive);
        memoryIndicator.setToolTipText("Indikator Memori");
        badgesLeft.add(memoryIndicator);

        metaRow.add(badgesLeft, BorderLayout.WEST);

        JPanel badgesRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 0));
        badgesRight.setOpaque(false);

        copyToastLabel = new JLabel("");
        copyToastLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        copyToastLabel.setForeground(currentTheme.memActive);
        badgesRight.add(copyToastLabel);

        copyBtn = new ModernButton(IconFactory.createCopyIcon(13, currentTheme.fgSecondary),
                currentTheme.bgCard, currentTheme.itemHover);
        copyBtn.setPreferredSize(new Dimension(28, 22));
        copyBtn.setCornerRadius(7);
        copyBtn.setBorderColor(currentTheme.border);
        copyBtn.setToolTipText("Salin Hasil [Ctrl+C]");
        copyBtn.addActionListener(e -> copyResultToClipboard());
        badgesRight.add(copyBtn);

        metaRow.add(badgesRight, BorderLayout.EAST);
        displayCard.add(metaRow, BorderLayout.NORTH);

        // 2. Middle: Expression Label (Formula)
        exprLabel = new JLabel(" ", SwingConstants.RIGHT);
        exprLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        exprLabel.setForeground(currentTheme.fgSecondary);
        displayCard.add(exprLabel, BorderLayout.CENTER);

        // 3. Bottom: Main Numerical Readout
        resultLabel = new JLabel("0", SwingConstants.RIGHT);
        resultLabel.setFont(new Font("Segoe UI Semibold", Font.BOLD, 34));
        resultLabel.setForeground(currentTheme.fgPrimary);
        resultLabel.setBorder(new EmptyBorder(2, 0, 2, 0));
        displayCard.add(resultLabel, BorderLayout.SOUTH);

        parent.add(displayCard, BorderLayout.NORTH);
    }

    private void buildMemoryBar(JPanel parent) {
        memPanel = new JPanel(new GridLayout(1, 5, 5, 0));
        memPanel.setOpaque(false);
        memPanel.setBorder(new EmptyBorder(1, 2, 4, 2));

        String[] memLabels = {"MC", "MR", "M+", "M-", "MS"};
        for (String m : memLabels) {
            ModernButton btn = new ModernButton(m, currentTheme.bgCard, currentTheme.memInactive,
                    currentTheme.bgNumHover, new Font("Segoe UI", Font.BOLD, 11));
            btn.setPreferredSize(new Dimension(50, 26));
            btn.setCornerRadius(9);
            btn.setBorderColor(currentTheme.border);
            btn.addActionListener(e -> handleMemory(m));
            memPanel.add(btn);
            memButtons.add(btn);
        }
        parent.add(memPanel, BorderLayout.SOUTH);
    }

    private void buildSidebar() {
        sidebarPanel = new JPanel(new BorderLayout(0, 0));
        sidebarPanel.setBackground(currentTheme.bgSidebar);
        sidebarPanel.setPreferredSize(new Dimension(225, 0));
        sidebarPanel.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, currentTheme.border));

        JPanel sbHeader = new JPanel(new BorderLayout(4, 0));
        sbHeader.setBackground(currentTheme.bgSidebar);
        sbHeader.setBorder(new EmptyBorder(10, 12, 10, 12));

        JLabel title = new JLabel("Riwayat");
        title.setIcon(IconFactory.createHistoryIcon(14, currentTheme.fgPrimary));
        title.setFont(new Font("Segoe UI", Font.BOLD, 13));
        title.setForeground(currentTheme.fgPrimary);
        sbHeader.add(title, BorderLayout.WEST);

        clearHistBtn = new ModernButton("Hapus", currentTheme.bgCard, currentTheme.fgAction,
                currentTheme.itemHover, new Font("Segoe UI", Font.PLAIN, 11));
        clearHistBtn.setIcon(IconFactory.createTrashIcon(13, currentTheme.fgAction));
        clearHistBtn.setPreferredSize(new Dimension(75, 26));
        clearHistBtn.setCornerRadius(8);
        clearHistBtn.setBorderColor(currentTheme.border);
        clearHistBtn.setToolTipText("Hapus Semua Riwayat Perhitungan");
        clearHistBtn.addActionListener(e -> {
            historyManager.clear();
            refreshHistoryUI();
        });
        sbHeader.add(clearHistBtn, BorderLayout.EAST);
        sidebarPanel.add(sbHeader, BorderLayout.NORTH);

        historyListPanel = new JPanel();
        historyListPanel.setLayout(new BoxLayout(historyListPanel, BoxLayout.Y_AXIS));
        historyListPanel.setBackground(currentTheme.bgSidebar);
        historyListPanel.setBorder(new EmptyBorder(6, 8, 6, 8));

        historyScrollPane = new JScrollPane(historyListPanel);
        historyScrollPane.setBackground(currentTheme.bgSidebar);
        historyScrollPane.getViewport().setBackground(currentTheme.bgSidebar);
        historyScrollPane.setBorder(null);
        historyScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        sidebarPanel.add(historyScrollPane, BorderLayout.CENTER);

        sidebarPanel.setVisible(false);
        centerPanel.add(sidebarPanel, BorderLayout.EAST);
    }

    private void renderKeypad() {
        keypadPanel.removeAll();
        keypadButtons.clear();

        if (!isScientific) {
            renderStandardKeypad();
        } else {
            renderScientificKeypad();
        }

        keypadPanel.revalidate();
        keypadPanel.repaint();
    }

    private ModernButton createKeyButton(String text, String type, String actionId, ActionListener al) {
        Color bg, fg, hbg;
        Font font;

        switch (type) {
            case "num":
                bg = currentTheme.bgNum;
                fg = currentTheme.fgNum;
                hbg = currentTheme.bgNumHover;
                font = new Font("Segoe UI", Font.BOLD, 16);
                break;
            case "op":
                bg = currentTheme.bgOp;
                fg = currentTheme.fgOp;
                hbg = currentTheme.bgOpHover;
                font = new Font("Segoe UI", Font.BOLD, 16);
                break;
            case "sci":
                bg = currentTheme.bgSci;
                fg = currentTheme.fgSci;
                hbg = currentTheme.bgSciHover;
                font = new Font("Segoe UI", Font.BOLD, 12);
                break;
            case "action":
                bg = currentTheme.bgAction;
                fg = currentTheme.fgAction;
                hbg = currentTheme.bgActionHover;
                font = new Font("Segoe UI", Font.BOLD, 13);
                break;
            case "equals":
                bg = currentTheme.bgEqualsStart;
                fg = currentTheme.fgEquals;
                hbg = currentTheme.bgEqualsHoverStart;
                font = new Font("Segoe UI", Font.BOLD, 20);
                break;
            default:
                bg = currentTheme.bgNum;
                fg = currentTheme.fgPrimary;
                hbg = currentTheme.bgNumHover;
                font = new Font("Segoe UI", Font.PLAIN, 14);
        }

        ModernButton btn = new ModernButton(text, bg, fg, hbg, font);
        btn.setCornerRadius(14); // Generous squircle shape
        btn.setBorderColor(currentTheme.border);

        // Equals button gets radiant gradient
        if ("equals".equals(type)) {
            btn.setGradient(
                    currentTheme.bgEqualsStart, currentTheme.bgEqualsEnd,
                    currentTheme.bgEqualsHoverStart, currentTheme.bgEqualsHoverEnd
            );
        }

        // Backspace button gets pure vector arrow icon
        if ("backspace".equals(actionId)) {
            btn.setIcon(IconFactory.createBackspaceIcon(16, currentTheme.fgAction));
            btn.setText("");
        }

        btn.addActionListener(al);
        keypadButtons.add(new ButtonMeta(btn, type, actionId));
        return btn;
    }

    private void renderStandardKeypad() {
        keypadPanel.setLayout(new GridLayout(6, 4, 7, 7)); // 7px gap gives modern breathing room

        // Row 0: ( ), %, C, Backspace
        keypadPanel.add(createKeyButton("( )", "sci", "parens", e -> pressSmartParentheses()));
        keypadPanel.add(createKeyButton("%", "sci", "%", e -> pressPercent()));
        keypadPanel.add(createKeyButton("C", "action", "c", e -> pressClear()));
        keypadPanel.add(createKeyButton("", "action", "backspace", e -> pressBackspace()));

        // Row 1: 1/x, x², √x, ÷
        keypadPanel.add(createKeyButton("1/x", "sci", "inv", e -> pressFunction("1/")));
        keypadPanel.add(createKeyButton("x²", "sci", "sqr", e -> pressFunction("sqr")));
        keypadPanel.add(createKeyButton("√x", "sci", "sqrt", e -> pressFunction("sqrt")));
        keypadPanel.add(createKeyButton("÷", "op", "div", e -> pressOperator("÷")));

        // Row 2: 7, 8, 9, ×
        keypadPanel.add(createKeyButton("7", "num", "7", e -> pressDigit("7")));
        keypadPanel.add(createKeyButton("8", "num", "8", e -> pressDigit("8")));
        keypadPanel.add(createKeyButton("9", "num", "9", e -> pressDigit("9")));
        keypadPanel.add(createKeyButton("×", "op", "mul", e -> pressOperator("×")));

        // Row 3: 4, 5, 6, −
        keypadPanel.add(createKeyButton("4", "num", "4", e -> pressDigit("4")));
        keypadPanel.add(createKeyButton("5", "num", "5", e -> pressDigit("5")));
        keypadPanel.add(createKeyButton("6", "num", "6", e -> pressDigit("6")));
        keypadPanel.add(createKeyButton("−", "op", "sub", e -> pressOperator("-")));

        // Row 4: 1, 2, 3, +
        keypadPanel.add(createKeyButton("1", "num", "1", e -> pressDigit("1")));
        keypadPanel.add(createKeyButton("2", "num", "2", e -> pressDigit("2")));
        keypadPanel.add(createKeyButton("3", "num", "3", e -> pressDigit("3")));
        keypadPanel.add(createKeyButton("+", "op", "add", e -> pressOperator("+")));

        // Row 5: ±, 0, ., =
        keypadPanel.add(createKeyButton("±", "num", "neg", e -> pressNegate()));
        keypadPanel.add(createKeyButton("0", "num", "0", e -> pressDigit("0")));
        keypadPanel.add(createKeyButton(".", "num", "dot", e -> pressDot()));
        keypadPanel.add(createKeyButton("=", "equals", "eq", e -> pressEquals()));
    }

    private void renderScientificKeypad() {
        keypadPanel.setLayout(new GridLayout(7, 5, 6, 6)); // 6px gap in scientific mode

        // Row 0: sin, cos, tan, π, e
        keypadPanel.add(createKeyButton("sin", "sci", "sin", e -> pressFunction("sin")));
        keypadPanel.add(createKeyButton("cos", "sci", "cos", e -> pressFunction("cos")));
        keypadPanel.add(createKeyButton("tan", "sci", "tan", e -> pressFunction("tan")));
        keypadPanel.add(createKeyButton("π", "sci", "pi", e -> pressConstant("π")));
        keypadPanel.add(createKeyButton("e", "sci", "e", e -> pressConstant("e")));

        // Row 1: asin, acos, atan, ln, log
        keypadPanel.add(createKeyButton("asin", "sci", "asin", e -> pressFunction("asin")));
        keypadPanel.add(createKeyButton("acos", "sci", "acos", e -> pressFunction("acos")));
        keypadPanel.add(createKeyButton("atan", "sci", "atan", e -> pressFunction("atan")));
        keypadPanel.add(createKeyButton("ln", "sci", "ln", e -> pressFunction("ln")));
        keypadPanel.add(createKeyButton("log", "sci", "log", e -> pressFunction("log")));

        // Row 2: (, ), xʸ, C, Backspace
        keypadPanel.add(createKeyButton("(", "sci", "lp", e -> pressParenthesis("(")));
        keypadPanel.add(createKeyButton(")", "sci", "rp", e -> pressParenthesis(")")));
        keypadPanel.add(createKeyButton("xʸ", "sci", "pow", e -> pressOperator("^")));
        keypadPanel.add(createKeyButton("C", "action", "c", e -> pressClear()));
        keypadPanel.add(createKeyButton("", "action", "backspace", e -> pressBackspace()));

        // Row 3: n!, 7, 8, 9, ÷
        keypadPanel.add(createKeyButton("n!", "sci", "fact", e -> pressFunction("fact")));
        keypadPanel.add(createKeyButton("7", "num", "7", e -> pressDigit("7")));
        keypadPanel.add(createKeyButton("8", "num", "8", e -> pressDigit("8")));
        keypadPanel.add(createKeyButton("9", "num", "9", e -> pressDigit("9")));
        keypadPanel.add(createKeyButton("÷", "op", "div", e -> pressOperator("÷")));

        // Row 4: √x, 4, 5, 6, ×
        keypadPanel.add(createKeyButton("√x", "sci", "sqrt", e -> pressFunction("sqrt")));
        keypadPanel.add(createKeyButton("4", "num", "4", e -> pressDigit("4")));
        keypadPanel.add(createKeyButton("5", "num", "5", e -> pressDigit("5")));
        keypadPanel.add(createKeyButton("6", "num", "6", e -> pressDigit("6")));
        keypadPanel.add(createKeyButton("×", "op", "mul", e -> pressOperator("×")));

        // Row 5: x², 1, 2, 3, −
        keypadPanel.add(createKeyButton("x²", "sci", "sqr", e -> pressFunction("sqr")));
        keypadPanel.add(createKeyButton("1", "num", "1", e -> pressDigit("1")));
        keypadPanel.add(createKeyButton("2", "num", "2", e -> pressDigit("2")));
        keypadPanel.add(createKeyButton("3", "num", "3", e -> pressDigit("3")));
        keypadPanel.add(createKeyButton("−", "op", "sub", e -> pressOperator("-")));

        // Row 6: ±, 0, ., +, =
        keypadPanel.add(createKeyButton("±", "num", "neg", e -> pressNegate()));
        keypadPanel.add(createKeyButton("0", "num", "0", e -> pressDigit("0")));
        keypadPanel.add(createKeyButton(".", "num", "dot", e -> pressDot()));
        keypadPanel.add(createKeyButton("+", "op", "add", e -> pressOperator("+")));
        keypadPanel.add(createKeyButton("=", "equals", "eq", e -> pressEquals()));
    }

    private void updateDisplay() {
        exprLabel.setText(expression.isEmpty() ? " " : expression);

        // Smart dynamic font scaling
        int len = currentInput.length();
        int fontSize;
        if (len <= 9) {
            fontSize = 34;
        } else if (len <= 14) {
            fontSize = 26;
        } else if (len <= 19) {
            fontSize = 20;
        } else {
            fontSize = 15;
        }

        resultLabel.setFont(new Font("Segoe UI Semibold", Font.BOLD, fontSize));
        resultLabel.setText(formatDisplayNumber(currentInput));

        // Update memory status indicator
        boolean hasMemory = engine.getMemory() != 0.0;
        memoryIndicator.setForeground(hasMemory ? currentTheme.memActive : currentTheme.memInactive);
        for (ModernButton btn : memButtons) {
            btn.updateColors(currentTheme.bgCard, hasMemory ? currentTheme.memActive : currentTheme.memInactive,
                    currentTheme.bgNumHover, hasMemory ? currentTheme.memActive : currentTheme.memInactive);
        }
    }

    private String formatDisplayNumber(String val) {
        if (val.contains("Error") || val.contains("Bukan") || val.contains("Tak") || val.contains("Sintaks")) {
            return val;
        }
        if (val.contains("(") || val.contains(")") || val.contains("sin") || val.contains("cos")) {
            return val;
        }
        try {
            if (val.contains(".")) {
                String[] parts = val.split("\\.");
                long intPart = Long.parseLong(parts[0]);
                return String.format(Locale.US, "%,d.%s", intPart, parts[1]);
            } else {
                long intPart = Long.parseLong(val);
                return String.format(Locale.US, "%,d", intPart);
            }
        } catch (Exception e) {
            return val;
        }
    }

    // --- Keypad Action Implementations ---
    public void pressDigit(String digit) {
        if (isNewCalculation) {
            currentInput = digit;
            isNewCalculation = false;
        } else {
            if ("0".equals(currentInput)) {
                currentInput = digit;
            } else {
                currentInput += digit;
            }
        }
        updateDisplay();
    }

    public void pressDot() {
        if (isNewCalculation) {
            currentInput = "0.";
            isNewCalculation = false;
        } else if (!currentInput.contains(".")) {
            currentInput += ".";
        }
        updateDisplay();
    }

    public void pressNegate() {
        if (currentInput.startsWith("-")) {
            currentInput = currentInput.substring(1);
        } else if (!"0".equals(currentInput)) {
            currentInput = "-" + currentInput;
        }
        updateDisplay();
    }

    public void pressOperator(String op) {
        if (isNewCalculation && expression.isEmpty()) {
            expression = currentInput + " " + op + " ";
        } else if (isNewCalculation && !expression.isEmpty()) {
            String[] tokens = expression.trim().split("\\s+");
            if (tokens.length > 0 && "+-×÷^".contains(tokens[tokens.length - 1])) {
                tokens[tokens.length - 1] = op;
                StringBuilder sb = new StringBuilder();
                for (String t : tokens) sb.append(t).append(" ");
                expression = sb.toString();
            }
        } else {
            expression += currentInput + " " + op + " ";
        }
        isNewCalculation = true;
        updateDisplay();
    }

    public void pressPercent() {
        try {
            double v = Double.parseDouble(currentInput) / 100.0;
            currentInput = CalculatorEngine.formatResult(v);
            updateDisplay();
        } catch (Exception e) {
            currentInput = "Error";
            updateDisplay();
        }
    }

    public void pressSmartParentheses() {
        // Counts open vs closed parentheses in expression
        int openCount = 0;
        for (char c : expression.toCharArray()) {
            if (c == '(') openCount++;
            else if (c == ')') openCount--;
        }

        if (openCount > 0 && !isNewCalculation) {
            pressParenthesis(")");
        } else {
            pressParenthesis("(");
        }
    }

    public void pressFunction(String fn) {
        try {
            double val = Double.parseDouble(currentInput);
            double res;
            String exprStr;

            switch (fn) {
                case "sqrt":
                    if (val < 0) throw new ArithmeticException("Akar negatif tidak valid");
                    res = Math.sqrt(val);
                    exprStr = "√(" + currentInput + ")";
                    break;
                case "sqr":
                    res = val * val;
                    exprStr = "(" + currentInput + ")²";
                    break;
                case "1/":
                    if (val == 0) throw new ArithmeticException("Dibagi nol");
                    res = 1.0 / val;
                    exprStr = "1/(" + currentInput + ")";
                    break;
                case "fact":
                    res = engine.evaluate("fact(" + val + ")");
                    exprStr = "fact(" + currentInput + ")";
                    break;
                default:
                    res = engine.evaluate(fn + "(" + val + ")");
                    exprStr = fn + "(" + currentInput + ")";
                    break;
            }

            String resStr = CalculatorEngine.formatResult(res);
            currentInput = resStr;
            historyManager.addEntry(exprStr, resStr);
            if (showHistory) refreshHistoryUI();
            isNewCalculation = true;
            updateDisplay();
        } catch (Exception e) {
            currentInput = e.getMessage() != null ? e.getMessage() : "Error";
            isNewCalculation = true;
            updateDisplay();
        }
    }

    public void pressConstant(String name) {
        if ("π".equals(name)) currentInput = "3.1415926535";
        else if ("e".equals(name)) currentInput = "2.7182818284";
        isNewCalculation = true;
        updateDisplay();
    }

    public void pressParenthesis(String p) {
        if (isNewCalculation) {
            expression = p;
            isNewCalculation = false;
        } else {
            expression += " " + currentInput + " " + p;
            currentInput = "0";
            isNewCalculation = true;
        }
        updateDisplay();
    }

    public void pressClear() {
        currentInput = "0";
        expression = "";
        isNewCalculation = true;
        updateDisplay();
    }

    public void pressCE() {
        currentInput = "0";
        isNewCalculation = true;
        updateDisplay();
    }

    public void pressBackspace() {
        if (isNewCalculation) {
            currentInput = "0";
        } else {
            if (currentInput.length() > 1) {
                currentInput = currentInput.substring(0, currentInput.length() - 1);
            } else {
                currentInput = "0";
                isNewCalculation = true;
            }
        }
        updateDisplay();
    }

    public void pressEquals() {
        if (expression.isEmpty()) return;

        String fullExpr = (expression + " " + currentInput).trim();
        try {
            double res = engine.evaluate(fullExpr);
            String resStr = CalculatorEngine.formatResult(res);

            historyManager.addEntry(fullExpr, resStr);
            if (showHistory) refreshHistoryUI();

            expression = fullExpr + " =";
            currentInput = resStr;
            isNewCalculation = true;
            updateDisplay();
            expression = "";
        } catch (ArithmeticException e) {
            currentInput = "Tidak dapat dibagi nol";
            expression = "";
            isNewCalculation = true;
            updateDisplay();
        } catch (Exception e) {
            currentInput = "Sintaks Error";
            expression = "";
            isNewCalculation = true;
            updateDisplay();
        }
    }

    // --- Memory Actions ---
    private void handleMemory(String op) {
        try {
            double val = Double.parseDouble(currentInput);
            switch (op) {
                case "MC":
                    engine.memoryClear();
                    break;
                case "MR":
                    currentInput = CalculatorEngine.formatResult(engine.getMemory());
                    isNewCalculation = true;
                    break;
                case "M+":
                    engine.memoryAdd(val);
                    isNewCalculation = true;
                    break;
                case "M-":
                    engine.memorySubtract(val);
                    isNewCalculation = true;
                    break;
                case "MS":
                    engine.setMemory(val);
                    isNewCalculation = true;
                    break;
            }
            updateDisplay();
        } catch (Exception ignored) {}
    }

    // --- Toggles ---
    public void toggleMode() {
        isScientific = !isScientific;
        modeBtn.setText(isScientific ? "Ilmiah" : "Standar");
        modeBtn.setIcon(isScientific ? IconFactory.createScientificIcon(13, currentTheme.fgPrimary)
                                    : IconFactory.createMenuIcon(13, currentTheme.fgPrimary));
        modeBadge.setText(isScientific ? "ILMIAH" : "STANDAR");
        angleBadgeBtn.setVisible(isScientific);

        int curW = frame.getWidth();
        int curH = frame.getHeight();
        if (isScientific) {
            frame.setSize(Math.max(curW, 510), Math.max(curH, 700));
        } else {
            frame.setSize(Math.min(curW, 430), Math.min(curH, 680));
        }

        renderKeypad();
    }

    public void toggleAngleMode() {
        engine.toggleAngleMode();
        angleBadgeBtn.setText(engine.getAngleMode());
    }

    public void toggleHistory() {
        showHistory = !showHistory;
        sidebarPanel.setVisible(showHistory);
        int curW = frame.getWidth();
        int curH = frame.getHeight();
        if (showHistory) {
            frame.setSize(curW + 225, curH);
            refreshHistoryUI();
        } else {
            frame.setSize(Math.max(curW - 225, 390), curH);
        }
        frame.revalidate();
    }

    public void toggleTheme() {
        isDark = !isDark;
        currentTheme = isDark ? Theme.DARK : Theme.LIGHT;
        applyTheme();
    }

    private void applyTheme() {
        frame.getContentPane().setBackground(currentTheme.bgMain);

        appTitleLabel.setForeground(currentTheme.fgPrimary);

        modeBtn.updateColors(currentTheme.bgCard, currentTheme.fgPrimary, currentTheme.bgNumHover, currentTheme.fgPrimary);
        modeBtn.setIcon(isScientific ? IconFactory.createScientificIcon(13, currentTheme.fgPrimary)
                                    : IconFactory.createMenuIcon(13, currentTheme.fgPrimary));
        modeBtn.setBorderColor(currentTheme.border);

        themeBtn.updateColors(currentTheme.bgCard, currentTheme.fgPrimary, currentTheme.bgNumHover, currentTheme.fgPrimary);
        themeBtn.setIcon(isDark ? IconFactory.createSunIcon(15, currentTheme.fgPrimary)
                                : IconFactory.createMoonIcon(15, currentTheme.fgPrimary));
        themeBtn.setBorderColor(currentTheme.border);

        historyBtn.updateColors(currentTheme.bgCard, currentTheme.fgPrimary, currentTheme.bgNumHover, currentTheme.fgPrimary);
        historyBtn.setIcon(IconFactory.createHistoryIcon(13, currentTheme.fgPrimary));
        historyBtn.setBorderColor(currentTheme.border);

        if (clearHistBtn != null) {
            clearHistBtn.updateColors(currentTheme.bgCard, currentTheme.fgAction, currentTheme.itemHover, currentTheme.fgAction);
            clearHistBtn.setIcon(IconFactory.createTrashIcon(13, currentTheme.fgAction));
            clearHistBtn.setBorderColor(currentTheme.border);
        }

        modeBadge.setForeground(currentTheme.fgDim);
        angleBadgeBtn.updateColors(currentTheme.bgSci, currentTheme.fgSci, currentTheme.bgSciHover, currentTheme.fgSci);
        angleBadgeBtn.setBorderColor(currentTheme.border);

        exprLabel.setForeground(currentTheme.fgSecondary);
        resultLabel.setForeground(currentTheme.fgPrimary);

        copyBtn.setIcon(IconFactory.createCopyIcon(13, currentTheme.fgSecondary));
        copyBtn.updateColors(currentTheme.bgCard, currentTheme.itemHover);
        copyBtn.setBorderColor(currentTheme.border);

        // Memory buttons
        boolean hasMemory = engine.getMemory() != 0.0;
        memoryIndicator.setForeground(hasMemory ? currentTheme.memActive : currentTheme.memInactive);
        for (ModernButton btn : memButtons) {
            btn.updateColors(currentTheme.bgCard, hasMemory ? currentTheme.memActive : currentTheme.memInactive,
                    currentTheme.bgNumHover, hasMemory ? currentTheme.memActive : currentTheme.memInactive);
            btn.setBorderColor(currentTheme.border);
        }

        // Keypad buttons
        for (ButtonMeta meta : keypadButtons) {
            meta.button.setBorderColor(currentTheme.border);
            switch (meta.type) {
                case "num":
                    meta.button.updateColors(currentTheme.bgNum, currentTheme.fgNum, currentTheme.bgNumHover, currentTheme.fgNum);
                    break;
                case "op":
                    meta.button.updateColors(currentTheme.bgOp, currentTheme.fgOp, currentTheme.bgOpHover, currentTheme.fgOp);
                    break;
                case "sci":
                    meta.button.updateColors(currentTheme.bgSci, currentTheme.fgSci, currentTheme.bgSciHover, currentTheme.fgSci);
                    break;
                case "action":
                    meta.button.updateColors(currentTheme.bgAction, currentTheme.fgAction, currentTheme.bgActionHover, currentTheme.fgAction);
                    if ("backspace".equals(meta.actionId)) {
                        meta.button.setIcon(IconFactory.createBackspaceIcon(16, currentTheme.fgAction));
                    }
                    break;
                case "equals":
                    meta.button.updateColors(currentTheme.bgEqualsStart, currentTheme.fgEquals, currentTheme.bgEqualsHoverStart, currentTheme.fgEquals);
                    meta.button.setGradient(
                            currentTheme.bgEqualsStart, currentTheme.bgEqualsEnd,
                            currentTheme.bgEqualsHoverStart, currentTheme.bgEqualsHoverEnd
                    );
                    break;
            }
        }

        // Sidebar
        sidebarPanel.setBackground(currentTheme.bgSidebar);
        sidebarPanel.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, currentTheme.border));
        historyListPanel.setBackground(currentTheme.bgSidebar);
        historyScrollPane.setBackground(currentTheme.bgSidebar);
        historyScrollPane.getViewport().setBackground(currentTheme.bgSidebar);

        if (showHistory) {
            refreshHistoryUI();
        }
        frame.repaint();
    }

    private void refreshHistoryUI() {
        historyListPanel.removeAll();
        List<HistoryManager.Entry> entries = historyManager.getEntries();

        if (entries.isEmpty()) {
            JLabel emptyLbl = new JLabel("Belum ada riwayat", SwingConstants.CENTER);
            emptyLbl.setFont(new Font("Segoe UI", Font.ITALIC, 11));
            emptyLbl.setForeground(currentTheme.fgDim);
            emptyLbl.setBorder(new EmptyBorder(30, 0, 0, 0));
            emptyLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
            historyListPanel.add(emptyLbl);
        } else {
            for (HistoryManager.Entry entry : entries) {
                // Floating card for history entry
                JPanel card = new JPanel(new BorderLayout(0, 3)) {
                    @Override
                    protected void paintComponent(Graphics g) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        g2.setColor(getBackground());
                        g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 12, 12));
                        g2.setColor(currentTheme.border);
                        g2.setStroke(new BasicStroke(0.8f));
                        g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, getWidth() - 1, getHeight() - 1, 12, 12));
                        g2.dispose();
                        super.paintComponent(g);
                    }
                };
                card.setOpaque(false);
                card.setBackground(currentTheme.bgCard);
                card.setBorder(new EmptyBorder(8, 10, 8, 10));
                card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 58));
                card.setCursor(new Cursor(Cursor.HAND_CURSOR));

                // Top: Formula + Timestamp
                JPanel topH = new JPanel(new BorderLayout());
                topH.setOpaque(false);

                JLabel timeLbl = new JLabel(entry.time);
                timeLbl.setFont(new Font("Segoe UI", Font.PLAIN, 9));
                timeLbl.setForeground(currentTheme.fgDim);
                topH.add(timeLbl, BorderLayout.WEST);

                JLabel expLbl = new JLabel(entry.expression + " =", SwingConstants.RIGHT);
                expLbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                expLbl.setForeground(currentTheme.fgSecondary);
                topH.add(expLbl, BorderLayout.EAST);

                card.add(topH, BorderLayout.NORTH);

                // Bottom: Result Number
                JLabel resLbl = new JLabel(entry.result, SwingConstants.RIGHT);
                resLbl.setFont(new Font("Segoe UI Semibold", Font.BOLD, 14));
                resLbl.setForeground(currentTheme.fgPrimary);
                card.add(resLbl, BorderLayout.CENTER);

                MouseAdapter ma = new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        currentInput = entry.result;
                        isNewCalculation = true;
                        updateDisplay();
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        card.setBackground(currentTheme.itemHover);
                        card.repaint();
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        card.setBackground(currentTheme.bgCard);
                        card.repaint();
                    }
                };
                card.addMouseListener(ma);
                expLbl.addMouseListener(ma);
                resLbl.addMouseListener(ma);
                timeLbl.addMouseListener(ma);

                historyListPanel.add(card);
                historyListPanel.add(Box.createVerticalStrut(6));
            }
        }

        historyListPanel.revalidate();
        historyListPanel.repaint();
    }

    private void copyResultToClipboard() {
        StringSelection selection = new StringSelection(currentInput);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, selection);

        copyToastLabel.setText("✓ Tersalin!");
        Timer timer = new Timer(1500, e -> copyToastLabel.setText(""));
        timer.setRepeats(false);
        timer.start();
    }

    // --- Global Keyboard Bindings ---
    private void setupGlobalKeyBindings() {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(e -> {
            if (e.getID() != KeyEvent.KEY_PRESSED) {
                return false;
            }

            int code = e.getKeyCode();
            char ch = e.getKeyChar();
            boolean isCtrl = (e.getModifiersEx() & InputEvent.CTRL_DOWN_MASK) != 0;

            if (isCtrl) {
                if (code == KeyEvent.VK_H) {
                    toggleHistory();
                    return true;
                } else if (code == KeyEvent.VK_S) {
                    toggleMode();
                    return true;
                } else if (code == KeyEvent.VK_T) {
                    toggleTheme();
                    return true;
                } else if (code == KeyEvent.VK_C) {
                    copyResultToClipboard();
                    return true;
                }
                return false;
            }

            // Digits
            if (ch >= '0' && ch <= '9') {
                pressDigit(String.valueOf(ch));
                return true;
            }

            // Operators
            switch (ch) {
                case '+':
                    pressOperator("+");
                    return true;
                case '-':
                    pressOperator("-");
                    return true;
                case '*':
                    pressOperator("×");
                    return true;
                case '/':
                    pressOperator("÷");
                    return true;
                case '%':
                    pressPercent();
                    return true;
                case '^':
                    pressOperator("^");
                    return true;
                case '.':
                case ',':
                    pressDot();
                    return true;
                case '(':
                    pressParenthesis("(");
                    return true;
                case ')':
                    pressParenthesis(")");
                    return true;
                case '=':
                    pressEquals();
                    return true;
            }

            // Special Keys
            if (code == KeyEvent.VK_ENTER) {
                pressEquals();
                return true;
            } else if (code == KeyEvent.VK_BACK_SPACE) {
                pressBackspace();
                return true;
            } else if (code == KeyEvent.VK_ESCAPE) {
                pressClear();
                return true;
            } else if (code == KeyEvent.VK_DELETE) {
                pressCE();
                return true;
            }

            return false;
        });
    }

    public String getCurrentInput() {
        return currentInput;
    }
}
