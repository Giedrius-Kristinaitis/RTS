package com.gasis.rts.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gasis.rts.logic.map.blockmap.Block;
import com.gasis.rts.logic.map.blockmap.BlockMap;
import com.gasis.rts.logic.object.building.Building;
import com.gasis.rts.logic.object.building.BuildingLoader;
import com.gasis.rts.logic.object.building.OffensiveBuilding;
import com.gasis.rts.logic.object.unit.RotatingGunUnit;
import com.gasis.rts.logic.object.unit.Unit;
import com.gasis.rts.logic.object.unit.UnitLoader;
import com.gasis.rts.logic.player.Player;
import com.gasis.rts.resources.Resources;
import com.gasis.rts.utils.Constants;

public class Test implements Updatable, Renderable {

    private RotatingGunUnit unit1;
    private RotatingGunUnit unit2;
    private RotatingGunUnit unit3;
    private RotatingGunUnit unit4;
    private RotatingGunUnit unit5;
    private RotatingGunUnit unit6;
    private RotatingGunUnit unit7;
    private RotatingGunUnit unit8;

    private Unit zeus1;
    private Unit zeus2;
    private Unit zeus3;
    private Unit zeus4;
    private Unit zeus5;
    private Unit zeus6;
    private Unit zeus7;
    private Unit zeus8;

    private Building plant;
    private OffensiveBuilding launcher;
    private OffensiveBuilding launcher2;
    private OffensiveBuilding launcher3;
    private OffensiveBuilding launcher4;
    private Building factory;
    private Building factory2;

    private RotatingGunUnit porcupine1;
    private RotatingGunUnit porcupine2;
    private RotatingGunUnit porcupine3;
    private RotatingGunUnit porcupine4;
    private RotatingGunUnit porcupine5;
    private RotatingGunUnit porcupine6;
    private RotatingGunUnit porcupine7;
    private RotatingGunUnit porcupine8;

    private RotatingGunUnit torrent1;
    private RotatingGunUnit torrent2;
    private RotatingGunUnit torrent3;
    private RotatingGunUnit torrent4;
    private RotatingGunUnit torrent5;
    private RotatingGunUnit torrent6;
    private RotatingGunUnit torrent7;
    private RotatingGunUnit torrent8;


    public Test(BlockMap map, Player player) {
        UnitLoader loader = new UnitLoader(map);
        loader.load(Gdx.files.internal(Constants.FOLDER_UNITS + "rhino"));

        unit1 = (RotatingGunUnit) loader.newInstance();
        unit2 = (RotatingGunUnit) loader.newInstance();
        unit3 = (RotatingGunUnit) loader.newInstance();
        unit4 = (RotatingGunUnit) loader.newInstance();
        unit5 = (RotatingGunUnit) loader.newInstance();
        unit6 = (RotatingGunUnit) loader.newInstance();
        unit7 = (RotatingGunUnit) loader.newInstance();
        unit8 = (RotatingGunUnit) loader.newInstance();

        unit1.setX(3.75f);
        unit1.setY(8.9f);
        unit1.rotateToDirection(Unit.EAST);
        unit1.aimAt(8, 8);

        unit2.setX(2.5f);
        unit2.setY(7.5f);
        unit2.rotateToDirection(Unit.SOUTH_EAST);
        unit2.aimAt(8, 8);

        unit3.setX(2);
        unit3.setY(5);
        unit3.rotateToDirection(Unit.SOUTH);
        unit3.aimAt(8, 8);

        unit4.setX(5);
        unit4.setY(3.5f);
        unit4.rotateToDirection(Unit.SOUTH_WEST);
        unit4.aimAt(8.5f, 8.5f);

        unit5.setX(13f);
        unit5.setY(6.5f);
        unit5.rotateToDirection(Unit.WEST);
        unit5.aimAt(8.5f, 8.5f);

        unit6.setX(5);
        unit6.setY(7.5f);
        unit6.rotateToDirection(Unit.NORTH_WEST);
        unit6.aimAt(8.5f, 8.5f);

        unit7.setX(5);
        unit7.setY(10);
        unit7.rotateToDirection(Unit.NORTH);
        unit7.aimAt(8.5f, 8.5f);

        unit8.setX(7.5f);
        unit8.setY(5f);
        unit8.rotateToDirection(Unit.NORTH_EAST);
        unit8.aimAt(8.5f, 8.5f);

        UnitLoader zeusLoader = new UnitLoader(map);
        zeusLoader.load(Gdx.files.internal(Constants.FOLDER_UNITS + "zeus"));

        zeus1 = zeusLoader.newInstance();
        zeus2 = zeusLoader.newInstance();
        zeus3 = zeusLoader.newInstance();
        zeus4 = zeusLoader.newInstance();
        zeus5 = zeusLoader.newInstance();
        zeus6 = zeusLoader.newInstance();
        zeus7 = zeusLoader.newInstance();
        zeus8 = zeusLoader.newInstance();

        zeus1.setX(1.75f);
        zeus1.setY(8.9f);
        zeus1.rotateToDirection(Unit.EAST);
        zeus1.aimAt(8, 8);
        map.occupyBlock((short)(1.75f / Block.BLOCK_WIDTH), (short)(8.9f / Block.BLOCK_HEIGHT), zeus1);

        zeus2.setX(2.5f);
        zeus2.setY(6.25f);
        zeus2.rotateToDirection(Unit.SOUTH_EAST);
        zeus2.aimAt(8, 8);

        zeus3.setX(4);
        zeus3.setY(5);
        zeus3.rotateToDirection(Unit.SOUTH);
        zeus3.aimAt(8, 8);

        zeus4.setX(8.2f);
        zeus4.setY(3.5f);
        zeus4.rotateToDirection(Unit.SOUTH_WEST);
        zeus4.aimAt(8.5f, 8.5f);

        zeus5.setX(11f);
        zeus5.setY(9f);
        zeus5.rotateToDirection(Unit.WEST);
        zeus5.aimAt(8.5f, 8.5f);

        zeus6.setX(9.65f);
        zeus6.setY(10.45f);
        zeus6.rotateToDirection(Unit.NORTH_WEST);
        zeus6.aimAt(8.5f, 8.5f);

        zeus7.setX(6);
        zeus7.setY(11);
        zeus7.rotateToDirection(Unit.NORTH);
        zeus7.aimAt(8.5f, 8.5f);

        zeus8.setX(6f);
        zeus8.setY(5f);
        zeus8.rotateToDirection(Unit.NORTH_EAST);
        zeus8.aimAt(8.5f, 8.5f);

        BuildingLoader buildingLoader = new BuildingLoader(map);
        buildingLoader.load(Gdx.files.internal(Constants.FOLDER_BUILDINGS + "power_plant_rebels"));

        plant = buildingLoader.newInstance();
        plant.setX(0);
        plant.setY(0);
        plant.initializeAnimations();

        BuildingLoader launcherLoader = new BuildingLoader(map);
        launcherLoader.load(Gdx.files.internal(Constants.FOLDER_BUILDINGS + "rocket_launcher_conf"));

        launcher = (OffensiveBuilding) launcherLoader.newInstance();
        launcher.setX(15.5f);
        launcher.setY(10);
        launcher.aimAt(8.5f, 8.5f);

        launcher2 = (OffensiveBuilding) launcherLoader.newInstance();
        launcher2.setX(15.5f);
        launcher2.setY(7);
        launcher2.aimAt(8.5f, 8.5f);

        launcher3 = (OffensiveBuilding) launcherLoader.newInstance();
        launcher3.setX(4);
        launcher3.setY(1.5f);
        launcher3.aimAt(8.5f, 8.5f);

        launcher4 = (OffensiveBuilding) launcherLoader.newInstance();
        launcher4.setX(0);
        launcher4.setY(5);
        launcher4.aimAt(8.5f, 8.5f);

        BuildingLoader factoryLoader = new BuildingLoader(map);
        factoryLoader.load(Gdx.files.internal(Constants.FOLDER_BUILDINGS + "machine_factory_conf"));

        factory = factoryLoader.newInstance();
        factory.setX(10);
        factory.setY(0);
        factory.initializeAnimations();

        BuildingLoader factoryLoader2 = new BuildingLoader(map);
        factoryLoader2.load(Gdx.files.internal(Constants.FOLDER_BUILDINGS + "machine_factory_rebels"));

        factory2 = factoryLoader2.newInstance();
        factory2.setX(16);
        factory2.setY(0);
        factory2.initializeAnimations();

        UnitLoader porcLoader = new UnitLoader(map);
        porcLoader.load(Gdx.files.internal(Constants.FOLDER_UNITS + "porcupine"));

        porcupine1 = (RotatingGunUnit) porcLoader.newInstance();
        porcupine1.setX(27);
        porcupine1.setY(1);
        porcupine1.rotateToDirection(Unit.NORTH);
        porcupine1.aimAt(23, 7f);

        porcupine2 = (RotatingGunUnit) porcLoader.newInstance();
        porcupine2.setX(27);
        porcupine2.setY(3);
        porcupine2.rotateToDirection(Unit.NORTH_EAST);
        porcupine2.aimAt(23, 7f);

        porcupine3 = (RotatingGunUnit) porcLoader.newInstance();
        porcupine3.setX(27);
        porcupine3.setY(5);
        porcupine3.rotateToDirection(Unit.EAST);
        porcupine3.aimAt(23, 7);

        porcupine4 = (RotatingGunUnit) porcLoader.newInstance();
        porcupine4.setX(27);
        porcupine4.setY(7);
        porcupine4.rotateToDirection(Unit.SOUTH_EAST);
        porcupine4.aimAt(23, 7f);

        porcupine5 = (RotatingGunUnit) porcLoader.newInstance();
        porcupine5.setX(20);
        porcupine5.setY(1);
        porcupine5.rotateToDirection(Unit.SOUTH);
        porcupine5.aimAt(25, 4.5f);

        porcupine6 = (RotatingGunUnit) porcLoader.newInstance();
        porcupine6.setX(20);
        porcupine6.setY(2);
        porcupine6.rotateToDirection(Unit.SOUTH_WEST);
        porcupine6.aimAt(25, 4.5f);

        porcupine7 = (RotatingGunUnit) porcLoader.newInstance();
        porcupine7.setX(20);
        porcupine7.setY(3);
        porcupine7.rotateToDirection(Unit.WEST);
        porcupine7.aimAt(25, 4.5f);

        porcupine8 = (RotatingGunUnit) porcLoader.newInstance();
        porcupine8.setX(20);
        porcupine8.setY(4);
        porcupine8.rotateToDirection(Unit.NORTH_WEST);
        porcupine8.aimAt(25, 4.5f);

        UnitLoader torrentLoader = new UnitLoader(map);
        torrentLoader.load(Gdx.files.internal(Constants.FOLDER_UNITS + "torrent"));

        torrent1 = (RotatingGunUnit) torrentLoader.newInstance();
        torrent1.setX(3);
        torrent1.setY(12);
        torrent1.rotateToDirection(Unit.EAST);
        torrent1.aimAt(8.5f, 8.5f);

        torrent2 = (RotatingGunUnit) torrentLoader.newInstance();
        torrent2.setX(15);
        torrent2.setY(5);
        torrent2.rotateToDirection(Unit.NORTH_EAST);
        torrent2.aimAt(8.5f, 8.5f);

        torrent3 = (RotatingGunUnit) torrentLoader.newInstance();
        torrent3.setX(12);
        torrent3.setY(10);
        torrent3.rotateToDirection(Unit.NORTH);
        torrent3.aimAt(8.5f, 8.5f);

        torrent4 = (RotatingGunUnit) torrentLoader.newInstance();
        torrent4.setX(1);
        torrent4.setY(10);
        torrent4.rotateToDirection(Unit.SOUTH_EAST);
        torrent4.aimAt(8.5f, 8.5f);

        torrent5 = (RotatingGunUnit) torrentLoader.newInstance();
        torrent5.setX(10);
        torrent5.setY(4);
        torrent5.rotateToDirection(Unit.SOUTH);
        torrent5.aimAt(8.5f, 8.5f);

        torrent6 = (RotatingGunUnit) torrentLoader.newInstance();
        torrent6.setX(3);
        torrent6.setY(4);
        torrent6.rotateToDirection(Unit.SOUTH_WEST);
        torrent6.aimAt(8.5f, 8.5f);

        torrent7 = (RotatingGunUnit) torrentLoader.newInstance();
        torrent7.setX(23);
        torrent7.setY(8);
        torrent7.rotateToDirection(Unit.WEST);
        torrent7.aimAt(8.5f, 8.5f);

        torrent8 = (RotatingGunUnit) torrentLoader.newInstance();
        torrent8.setX(21);
        torrent8.setY(5);
        torrent8.rotateToDirection(Unit.NORTH_WEST);
        torrent8.aimAt(8.5f, 8.5f);

        player.addUnit(zeus1);
        player.addUnit(zeus2);
        player.addUnit(zeus3);
        player.addUnit(zeus4);
        player.addUnit(zeus5);
        player.addUnit(zeus6);
        player.addUnit(zeus7);
        player.addUnit(zeus8);
        player.addUnit(torrent1);
        player.addUnit(torrent2);
        player.addUnit(torrent3);
        player.addUnit(torrent4);
        player.addUnit(torrent5);
        player.addUnit(torrent6);
        player.addUnit(torrent7);
        player.addUnit(torrent8);
        player.addBuilding(launcher);
        player.addBuilding(launcher2);
        player.addBuilding(launcher3);
        player.addBuilding(launcher4);
        player.addBuilding(factory);

        zeus1.setHp(zeus1.getDefensiveSpecs().getMaxHp() * 0.50f);
        zeus2.setHp(zeus2.getDefensiveSpecs().getMaxHp() * 0.25f);
    }

    /**
     * Renders the object to the screen
     *
     * @param batch     sprite batch to draw to
     * @param resources game assets
     */
    @Override
    public void render(SpriteBatch batch, Resources resources) {
        unit1.render(batch, resources);
        unit2.render(batch, resources);
        unit3.render(batch, resources);
        unit4.render(batch, resources);
        unit5.render(batch, resources);
        unit6.render(batch, resources);
        unit7.render(batch, resources);
        unit8.render(batch, resources);

        plant.render(batch, resources);

        porcupine1.render(batch, resources);
        porcupine2.render(batch, resources);
        porcupine3.render(batch, resources);
        porcupine4.render(batch, resources);
        porcupine5.render(batch, resources);
        porcupine6.render(batch, resources);
        porcupine7.render(batch, resources);
        porcupine8.render(batch, resources);

        factory2.render(batch, resources);
    }

    /**
     * Updates the state of the object
     *
     * @param delta time elapsed since the last update
     */
    @Override
    public void update(float delta) {
        unit1.update(delta);
        unit2.update(delta);
        unit3.update(delta);
        unit4.update(delta);
        unit5.update(delta);
        unit6.update(delta);
        unit7.update(delta);
        unit8.update(delta);

        plant.update(delta);

        porcupine1.update(delta);
        porcupine2.update(delta);
        porcupine3.update(delta);
        porcupine4.update(delta);
        porcupine5.update(delta);
        porcupine6.update(delta);
        porcupine7.update(delta);
        porcupine8.update(delta);

        factory2.update(delta);

        if (Gdx.input.isTouched()) {
            unit1.setInSiegeMode(!unit1.isInSiegeMode());
            unit2.setInSiegeMode(!unit2.isInSiegeMode());
            unit3.setInSiegeMode(!unit3.isInSiegeMode());
            unit4.setInSiegeMode(!unit4.isInSiegeMode());
            unit5.setInSiegeMode(!unit5.isInSiegeMode());
            unit6.setInSiegeMode(!unit6.isInSiegeMode());
            unit7.setInSiegeMode(!unit7.isInSiegeMode());
            unit8.setInSiegeMode(!unit8.isInSiegeMode());

            unit1.rotateToDirection(Unit.WEST);
            unit2.rotateToDirection(Unit.NORTH_WEST);
            unit3.rotateToDirection(Unit.NORTH);
            unit4.rotateToDirection(Unit.SOUTH_WEST);
            unit5.rotateToDirection(Unit.SOUTH);
            unit6.rotateToDirection(Unit.SOUTH);
            unit7.rotateToDirection(Unit.NORTH_EAST);
        }
    }
}
