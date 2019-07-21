package com.gasis.rts;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.gasis.rts.resources.Resources;
import com.gasis.rts.ui.abstractions.BasicScreen;
import com.gasis.rts.ui.abstractions.ScreenSwitcher;
import com.gasis.rts.ui.abstractions.ScreenWithInput;
import com.gasis.rts.ui.implementations.GameScreen;
import com.gasis.rts.ui.implementations.LoadingScreen;
import com.gasis.rts.utils.Constants;
import com.sun.org.apache.bcel.internal.classfile.Constant;

import java.util.Map;
import java.util.TreeMap;

/**
 * Main class of the game
 */
public class Main extends Game implements ScreenSwitcher {

	// current visible ui
	private BasicScreen currentScreen;

	// game's resources
	private Resources resources;

	// viewport used by all screens
	private final Viewport port = new FillViewport(Constants.WIDTH, Constants.HEIGHT);

	/**
	 * Performs initialization. Called automatically by libGDX
	 */
	@Override
	public void create () {
		resources = new Resources();

		// define assets to be loaded
		Map<String, Class> assetsToLoad = new TreeMap<String, Class>();

		assetsToLoad.put(Constants.FOLDER_ATLASES + "t1.atlas", TextureAtlas.class);
		assetsToLoad.put(Constants.FOLDER_ATLASES + "unit.atlas", TextureAtlas.class);
		assetsToLoad.put(Constants.FOLDER_ATLASES + "effects.atlas", TextureAtlas.class);
		assetsToLoad.put(Constants.FOLDER_ATLASES + "zeus.atlas", TextureAtlas.class);
		assetsToLoad.put(Constants.FOLDER_ATLASES + "building.atlas", TextureAtlas.class);
		assetsToLoad.put(Constants.FOLDER_ATLASES + "building2.atlas", TextureAtlas.class);
		assetsToLoad.put(Constants.FOLDER_ATLASES + "building3.atlas", TextureAtlas.class);
		assetsToLoad.put(Constants.FOLDER_ATLASES + "building4.atlas", TextureAtlas.class);
		assetsToLoad.put(Constants.FOLDER_ATLASES + "porcupine.atlas", TextureAtlas.class);
		assetsToLoad.put(Constants.FOLDER_ATLASES + "torrent.atlas", TextureAtlas.class);
		assetsToLoad.put(Constants.FOLDER_ATLASES + "hammer.atlas", TextureAtlas.class);
		assetsToLoad.put(Constants.GENERAL_TEXTURE_ATLAS, TextureAtlas.class);

		// change the current screen to loading screen
		showScreen(new LoadingScreen(
				new GameScreen(),
				Constants.FOLDER_STANDALONE_IMAGES + "loading.png",
				true,
				assetsToLoad,
				null
		));
	}

	/**
	 * Game loop render method. Called automatically by libGDX
	 */
	@Override
	public void render () {
		super.render();
	}

	/**
	 * Disposes of all heavy resources. Called automatically by libGDX
	 */
	@Override
	public void dispose () {
		if (currentScreen != null) {
			currentScreen.dispose();
		}

		resources.dispose();
	}

	/**
	 * Sets the current screen to the specified one
	 * @param screen screen to be shown
	 */
	@Override
	public void showScreen(BasicScreen screen) {
		if (currentScreen != null) {
			currentScreen.dispose();
		}

		this.currentScreen = screen;
		this.currentScreen.setViewport(port);
		this.currentScreen.initialize();
		this.currentScreen.setResources(resources);
		this.currentScreen.setScreenSwitcher(this);

		this.setScreen(currentScreen);

		// if the screen has input handling, register it
		if (currentScreen instanceof ScreenWithInput) {
			Gdx.input.setInputProcessor(((ScreenWithInput) currentScreen).getInputProcessor());
		}
	}
}
