package com.gasis.rts.utils;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;

/**
 * Neat functions related to current platform
 */
public class PlatformUtils {

    /**
     * Checks if the current platform is mobile
     *
     * @return
     */
    public static boolean isMobile() {
        return Gdx.app.getType() == Application.ApplicationType.Android
                || Gdx.app.getType() == Application.ApplicationType.iOS;
    }
}
