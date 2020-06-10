package org.lowfer.config;

@SuppressWarnings("SpellCheckingInspection")
public enum Color {
    BLACK("F2F0EF", "BAB0AB", "343a40"),
    BLUE("DEE6EF", "4E79A7", "324E6B"),
    ORANGE("FCEAD8", "F28E2C", "9B5B1D"),
    RED("F9E0E0", "E15759", "903839"),
    CYAN("E6F1F1", "76B7B2", "4C7572"),
    GREEN("E0EDDF", "59A14F", "396733"),
    YELLOW("FBF5DD", "EDC949", "AD9336"),
    PURPLE("F0E6ED", "AF7AA1", "805976"),
    PINK("FFEDEF", "FF9DA7", "BA737A"),
    BROWN("EDE5E1", "9C755F", "725646"),
    GRAY("F2F0EF", "BAB0AB", "88817D"),
    ;

    private final String canvas;
    private final String background;
    private final String stroke;

    Color(String canvas, String background, String stroke) {
        this.canvas = canvas;
        this.background = background;
        this.stroke = stroke;
    }

    public String getCanvas() {
        return "#" + canvas;
    }

    public String getBackground() {
        return "#" + background;
    }

    public String getStroke() {
        return "#" + stroke;
    }
}
