package agh.cs.pdonkey;

/**
 * pDonkey
 * Created by luknw on 16.12.2016
 */

public class AppConfigImpl implements AppConfig {
    private final AppMode mode;
    private final MP mp;

    public AppConfigImpl(AppMode mode, MP mp) {
        this.mp = mp;
        this.mode = mode;
    }

    public AppMode getMode() {
        return mode;
    }

    public MP getMP() {
        return mp;
    }
}
