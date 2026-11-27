package fr.juke.commands.util;

public final class TextFormatter {
    private TextFormatter() {
    }

    public static String tablistChars(String string) {
        if (string == null) {
            return "";
        }

        String output = string;
        output = output.replace("#TPS", Double.toString(TablistVariables.getTps()));
        output = output.replace("#MSPT", Double.toString(TablistVariables.getMspt()));
        output = output.replace("#UPTIME", TablistVariables.getUptime());
        output = output.replace("#PLAYERCOUNT", Integer.toString(TablistVariables.getPlayerCount()));
        output = output.replace("#N", "\n");
        return output;
    }
}
