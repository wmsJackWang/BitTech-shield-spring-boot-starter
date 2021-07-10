package com.ruoyi.context;


public class ShieldBanner {

    public static final String INIT_VERSION = "1.0.0";

    public static final String LOGO ="       .__    .__       .__       .___\n" +
            "  _____|  |__ |__| ____ |  |    __| _/\n" +
            " /  ___/  |  \\|  |/ __ \\|  |   / __ | \n" +
            " \\___ \\|   Y  \\  \\  ___/|  |__/ /_/ | \n" +
            "/____  >___|  /__|\\___  >____/\\____ | \n" +
            "     \\/     \\/        \\/           \\/ ";

    public static String buildBannerText() {
        return System.getProperty("line.separator") + LOGO + " :: Shield ::        (v" + ShieldVersion.getVersion(INIT_VERSION) + ")" + System.getProperty("line.separator");
    }
}
