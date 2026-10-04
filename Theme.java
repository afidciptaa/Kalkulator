import java.awt.Color;

/**
 * Modern aesthetic color palettes:
 * - DARK: Midnight Obsidian with neon/glass accents (Apple Pro / Fluent style)
 * - LIGHT: Arctic Snow with crisp indigo and slate accents
 */
public class Theme {
    public final Color bgMain;
    public final Color bgDisplay;
    public final Color bgSidebar;
    public final Color bgCard;

    // Button Backgrounds
    public final Color bgNum;
    public final Color bgNumHover;
    public final Color bgNumPressed;

    public final Color bgOp;
    public final Color bgOpHover;
    public final Color bgOpPressed;

    public final Color bgSci;
    public final Color bgSciHover;
    public final Color bgSciPressed;

    public final Color bgAction;
    public final Color bgActionHover;
    public final Color bgActionPressed;

    public final Color bgEqualsStart;
    public final Color bgEqualsEnd;
    public final Color bgEqualsHoverStart;
    public final Color bgEqualsHoverEnd;

    // Foreground Text & Accents
    public final Color fgPrimary;
    public final Color fgSecondary;
    public final Color fgDim;
    public final Color fgNum;
    public final Color fgOp;
    public final Color fgSci;
    public final Color fgAction;
    public final Color fgEquals;

    // Badges & Highlights
    public final Color border;
    public final Color highlightTop;
    public final Color itemHover;
    public final Color memActive;
    public final Color memInactive;
    public final Color badgeBg;
    public final Color badgeFg;

    public Theme(
        Color bgMain, Color bgDisplay, Color bgSidebar, Color bgCard,
        Color bgNum, Color bgNumHover, Color bgNumPressed,
        Color bgOp, Color bgOpHover, Color bgOpPressed,
        Color bgSci, Color bgSciHover, Color bgSciPressed,
        Color bgAction, Color bgActionHover, Color bgActionPressed,
        Color bgEqualsStart, Color bgEqualsEnd, Color bgEqualsHoverStart, Color bgEqualsHoverEnd,
        Color fgPrimary, Color fgSecondary, Color fgDim,
        Color fgNum, Color fgOp, Color fgSci, Color fgAction, Color fgEquals,
        Color border, Color highlightTop, Color itemHover,
        Color memActive, Color memInactive, Color badgeBg, Color badgeFg
    ) {
        this.bgMain = bgMain;
        this.bgDisplay = bgDisplay;
        this.bgSidebar = bgSidebar;
        this.bgCard = bgCard;
        this.bgNum = bgNum;
        this.bgNumHover = bgNumHover;
        this.bgNumPressed = bgNumPressed;
        this.bgOp = bgOp;
        this.bgOpHover = bgOpHover;
        this.bgOpPressed = bgOpPressed;
        this.bgSci = bgSci;
        this.bgSciHover = bgSciHover;
        this.bgSciPressed = bgSciPressed;
        this.bgAction = bgAction;
        this.bgActionHover = bgActionHover;
        this.bgActionPressed = bgActionPressed;
        this.bgEqualsStart = bgEqualsStart;
        this.bgEqualsEnd = bgEqualsEnd;
        this.bgEqualsHoverStart = bgEqualsHoverStart;
        this.bgEqualsHoverEnd = bgEqualsHoverEnd;
        this.fgPrimary = fgPrimary;
        this.fgSecondary = fgSecondary;
        this.fgDim = fgDim;
        this.fgNum = fgNum;
        this.fgOp = fgOp;
        this.fgSci = fgSci;
        this.fgAction = fgAction;
        this.fgEquals = fgEquals;
        this.border = border;
        this.highlightTop = highlightTop;
        this.itemHover = itemHover;
        this.memActive = memActive;
        this.memInactive = memInactive;
        this.badgeBg = badgeBg;
        this.badgeFg = badgeFg;
    }

    // Midnight Obsidian Dark Theme (Super Premium Look)
    public static final Theme DARK = new Theme(
        new Color(0x0F, 0x11, 0x17), // bgMain (Deep obsidian)
        new Color(0x18, 0x1B, 0x26), // bgDisplay (Elevated glass card)
        new Color(0x14, 0x16, 0x20), // bgSidebar
        new Color(0x1E, 0x22, 0x30), // bgCard

        new Color(0x20, 0x24, 0x33), // bgNum
        new Color(0x2D, 0x33, 0x47), // bgNumHover
        new Color(0x18, 0x1B, 0x26), // bgNumPressed

        new Color(0x43, 0x38, 0xCA), // bgOp (Royal Indigo)
        new Color(0x4F, 0x46, 0xE5), // bgOpHover
        new Color(0x37, 0x30, 0xA3), // bgOpPressed

        new Color(0x1B, 0x1F, 0x2E), // bgSci (Deep Navy)
        new Color(0x27, 0x2C, 0x40), // bgSciHover
        new Color(0x14, 0x17, 0x22), // bgSciPressed

        new Color(0x3B, 0x1E, 0x28), // bgAction (Muted Crimson)
        new Color(0x52, 0x24, 0x35), // bgActionHover
        new Color(0x29, 0x15, 0x1C), // bgActionPressed

        new Color(0x3B, 0x82, 0xF6), // bgEqualsStart (Electric Blue)
        new Color(0x10, 0xB9, 0x81), // bgEqualsEnd (Emerald Cyan Gradient)
        new Color(0x60, 0xA5, 0xFA), // bgEqualsHoverStart
        new Color(0x34, 0xD3, 0x99), // bgEqualsHoverEnd

        new Color(0xF8, 0xFA, 0xFC), // fgPrimary (Crisp white)
        new Color(0x94, 0xA3, 0xB8), // fgSecondary (Soft slate)
        new Color(0x64, 0x74, 0x8B), // fgDim
        new Color(0xF1, 0xF5, 0xF9), // fgNum
        new Color(0xFF, 0xFF, 0xFF), // fgOp
        new Color(0x38, 0xBD, 0xF8), // fgSci (Cyan accent)
        new Color(0xFB, 0x71, 0x85), // fgAction (Rose accent)
        new Color(0xFF, 0xFF, 0xFF), // fgEquals

        new Color(0x2A, 0x30, 0x44), // border
        new Color(255, 255, 255, 18), // highlightTop
        new Color(0x28, 0x2E, 0x40), // itemHover
        new Color(0x34, 0xD3, 0x99), // memActive (Glowing Emerald)
        new Color(0x47, 0x55, 0x69), // memInactive
        new Color(0x31, 0x2E, 0x81), // badgeBg (Indigo pill)
        new Color(0xA5, 0xB4, 0xFC)  // badgeFg
    );

    // Arctic Snow Light Theme
    public static final Theme LIGHT = new Theme(
        new Color(0xF1, 0xF5, 0xF9), // bgMain (Soft platinum)
        new Color(0xFF, 0xFF, 0xFF), // bgDisplay (Crisp white card)
        new Color(0xF8, 0xFA, 0xFC), // bgSidebar
        new Color(0xFF, 0xFF, 0xFF), // bgCard

        new Color(0xFF, 0xFF, 0xFF), // bgNum
        new Color(0xE2, 0xE8, 0xF0), // bgNumHover
        new Color(0xCB, 0xD5, 0xE1), // bgNumPressed

        new Color(0x4F, 0x46, 0xE5), // bgOp (Royal Indigo)
        new Color(0x43, 0x38, 0xCA), // bgOpHover
        new Color(0x37, 0x30, 0xA3), // bgOpPressed

        new Color(0xEE, 0xF2, 0xF6), // bgSci
        new Color(0xE2, 0xE8, 0xF0), // bgSciHover
        new Color(0xCB, 0xD5, 0xE1), // bgSciPressed

        new Color(0xFE, 0xE2, 0xE2), // bgAction (Soft Rose)
        new Color(0xFE, 0xCD, 0xCD), // bgActionHover
        new Color(0xFC, 0xA5, 0xA5), // bgActionPressed

        new Color(0x25, 0x63, 0xEB), // bgEqualsStart (Royal Blue)
        new Color(0x05, 0x96, 0x69), // bgEqualsEnd (Emerald Gradient)
        new Color(0x1D, 0x4E, 0xD8), // bgEqualsHoverStart
        new Color(0x04, 0x78, 0x57), // bgEqualsHoverEnd

        new Color(0x0F, 0x17, 0x2A), // fgPrimary (Deep slate)
        new Color(0x47, 0x55, 0x69), // fgSecondary
        new Color(0x94, 0xA3, 0xB8), // fgDim
        new Color(0x0F, 0x17, 0x2A), // fgNum
        new Color(0xFF, 0xFF, 0xFF), // fgOp
        new Color(0x4F, 0x46, 0xE5), // fgSci
        new Color(0xE1, 0x1D, 0x48), // fgAction
        new Color(0xFF, 0xFF, 0xFF), // fgEquals

        new Color(0xDA, 0xE2, 0xED), // border
        new Color(255, 255, 255, 90), // highlightTop
        new Color(0xEA, 0xF0, 0xF8), // itemHover
        new Color(0x05, 0x96, 0x69), // memActive
        new Color(0x94, 0xA3, 0xB8), // memInactive
        new Color(0xEE, 0xF2, 0xFF), // badgeBg
        new Color(0x43, 0x38, 0xCA)  // badgeFg
    );
}
