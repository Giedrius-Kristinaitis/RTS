package com.gasis.rts.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.gasis.rts.Main;
import com.gasis.rts.ui.behavior.DesktopBehavior;

/**
 * Launches the desktop version of the game
 */
public class DesktopLauncher {

    /**
     * Entry point of the program
     *
     * @param arg arguments for the program
     */
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

        config.width = 1024;
        config.height = 720;
        config.title = "RTS";

        new LwjglApplication(new Main(new DesktopBehavior()), config);
    }
}
