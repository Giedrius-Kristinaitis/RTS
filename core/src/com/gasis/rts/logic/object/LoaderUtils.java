package com.gasis.rts.logic.object;

import com.gasis.rts.filehandling.FileLineReader;
import com.gasis.rts.logic.object.combat.FireSource;
import com.gasis.rts.logic.object.combat.RotatingGun;
import com.gasis.rts.math.Point;

import java.util.*;

/**
 * Utility functions for game object loaders
 */
@SuppressWarnings("Duplicates") // the damned IDE gets mad about 3 repeated lines
public class LoaderUtils {

    /**
     * Reads fire sources of the unit
     *
     * @param reader reader to read data from
     * @return list of fire sources
     */
    public static List<FireSource> readFireSources(FileLineReader reader) {
        List<FireSource> fireSources = new ArrayList<FireSource>();

        List<String> sources = reader.readLines("fire source");

        if (sources == null) {
            return fireSources;
        }

        for (String source : sources) {
            fireSources.add(readFireSource(source, reader));
        }

        return fireSources;
    }

    /**
     * Reads fire sources of the unit
     *
     * @param prefixes prefixes of the fire sources' data
     * @param reader   reader to read data from
     * @return list of fire sources
     */
    public static List<FireSource> readFireSources(String[] prefixes, FileLineReader reader) {
        List<FireSource> fireSources = new ArrayList<FireSource>();

        for (String prefix : prefixes) {
            fireSources.add(readFireSource(prefix, reader));
        }

        return fireSources;
    }

    /**
     * Reads a single fire source
     *
     * @param prefix prefix of the fire source data
     * @param reader reader to read data from
     * @return fire source
     */
    public static FireSource readFireSource(String prefix, FileLineReader reader) {
        FireSource fireSource = new FireSource();

        String fireType = reader.readLine(prefix + " fire type");

        if (fireType.equalsIgnoreCase("bullet")) {
            fireSource.setFireType(FireSource.FIRE_TYPE_BULLET);
        } else if (fireType.equalsIgnoreCase("missile")) {
            fireSource.setFireType(FireSource.FIRE_TYPE_MISSILE);
        } else if (fireType.equalsIgnoreCase("shell")) {
            fireSource.setFireType(FireSource.FIRE_TYPE_SHELL);
        } else if (fireType.equalsIgnoreCase("flame")) {
            fireSource.setFireType(FireSource.FIRE_TYPE_FLAME);
        }

        String projectileScale = reader.readLine(prefix + " projectile scale");

        if (projectileScale.equalsIgnoreCase("small")) {
            fireSource.setProjectileScale(FireSource.SMALL);
        } else if (projectileScale.equalsIgnoreCase("medium")) {
            fireSource.setProjectileScale(FireSource.MEDIUM);
        } else if (projectileScale.equalsIgnoreCase("heavy")) {
            fireSource.setProjectileScale(FireSource.HEAVY);
        }

        fireSource.setGunCount(Byte.parseByte(reader.readLine(prefix + " gun count")));
        fireSource.setProjectileSpeed(Float.parseFloat(reader.readLine(prefix + " projectile speed")));

        try {
            fireSource.setDamageCoefficient(Float.parseFloat(reader.readLine(prefix + " damage coefficient")));
        } catch (Exception ex) {
            fireSource.setDamageCoefficient(1);
        }

        try {
            fireSource.setSoundEffect(reader.readLine(prefix + " sound effect"));
        } catch (Exception ex) {
        }

        try {
            String requiredTech = reader.readLine(prefix + " required tech id");

            if (requiredTech != null) {
                fireSource.setRequiredTechId(requiredTech);
                fireSource.setEnabled(false);
            } else {
                fireSource.setEnabled(true);
            }
        } catch (Exception ex) {
        }

        List<Point> firePoints = new ArrayList<Point>();

        firePoints.add(new Point(
                Float.parseFloat(reader.readLine(prefix + " fire point north x")),
                Float.parseFloat(reader.readLine(prefix + " fire point north y"))
        ));

        firePoints.add(new Point(
                Float.parseFloat(reader.readLine(prefix + " fire point north east x")),
                Float.parseFloat(reader.readLine(prefix + " fire point north east y"))
        ));

        firePoints.add(new Point(
                Float.parseFloat(reader.readLine(prefix + " fire point east x")),
                Float.parseFloat(reader.readLine(prefix + " fire point east y"))
        ));

        firePoints.add(new Point(
                Float.parseFloat(reader.readLine(prefix + " fire point south east x")),
                Float.parseFloat(reader.readLine(prefix + " fire point south east y"))
        ));

        firePoints.add(new Point(
                Float.parseFloat(reader.readLine(prefix + " fire point south x")),
                Float.parseFloat(reader.readLine(prefix + " fire point south y"))
        ));

        firePoints.add(new Point(
                Float.parseFloat(reader.readLine(prefix + " fire point south west x")),
                Float.parseFloat(reader.readLine(prefix + " fire point south west y"))
        ));

        firePoints.add(new Point(
                Float.parseFloat(reader.readLine(prefix + " fire point west x")),
                Float.parseFloat(reader.readLine(prefix + " fire point west y"))
        ));

        firePoints.add(new Point(
                Float.parseFloat(reader.readLine(prefix + " fire point north west x")),
                Float.parseFloat(reader.readLine(prefix + " fire point north west y"))
        ));

        fireSource.setFirePoints(firePoints);
        fireSource.setProjectileDeviation(Float.parseFloat(reader.readLine(prefix + " projectile deviation")));

        String presence = reader.readLine(prefix + " present");

        if (presence.equalsIgnoreCase("always")) {
            fireSource.setPresentInSiegeMode(true);
            fireSource.setPresentOutOfSiegeMode(true);
        } else if (presence.equalsIgnoreCase("siege mode")) {
            fireSource.setPresentInSiegeMode(true);
            fireSource.setPresentOutOfSiegeMode(false);
        } else if (presence.equalsIgnoreCase("not siege mode")) {
            fireSource.setPresentInSiegeMode(false);
            fireSource.setPresentOutOfSiegeMode(true);
        }

        return fireSource;
    }

    /**
     * Reads rotating guns of the unit if there are any
     *
     * @param reader reader to read data from
     * @return map of rotating guns with it's fire sources
     */
    public static Map<RotatingGun, List<FireSource>> readRotatingGuns(FileLineReader reader) {
        Map<RotatingGun, List<FireSource>> rotatingGuns = new HashMap<RotatingGun, List<FireSource>>();

        List<String> guns = reader.readLines("rotating gun");

        if (guns == null) {
            return rotatingGuns;
        }

        for (String gun : guns) {
            Map.Entry<RotatingGun, List<FireSource>> entry = readRotatingGun(gun, reader);

            rotatingGuns.put(entry.getKey(), entry.getValue());
        }

        return rotatingGuns;
    }

    /**
     * Reads a single rotating gun
     *
     * @param prefix prefix of the gun's data
     * @param reader reader to read data from
     * @return rotating gun
     */
    public static Map.Entry<RotatingGun, List<FireSource>> readRotatingGun(String prefix, FileLineReader reader) {
        RotatingGun rotatingGun = new RotatingGun();

        List<String> gunTextures = new ArrayList<String>();

        gunTextures.add(reader.readLine(prefix + " texture north"));
        gunTextures.add(reader.readLine(prefix + " texture north east"));
        gunTextures.add(reader.readLine(prefix + " texture east"));
        gunTextures.add(reader.readLine(prefix + " texture south east"));
        gunTextures.add(reader.readLine(prefix + " texture south"));
        gunTextures.add(reader.readLine(prefix + " texture south west"));
        gunTextures.add(reader.readLine(prefix + " texture west"));
        gunTextures.add(reader.readLine(prefix + " texture north west"));

        rotatingGun.setAtlas(reader.readLine(prefix + " atlas"));
        rotatingGun.setTextures(gunTextures);
        rotatingGun.setWidth(Float.parseFloat(reader.readLine(prefix + " width")));
        rotatingGun.setHeight(Float.parseFloat(reader.readLine(prefix + " height")));
        rotatingGun.setRecoilResistance(Float.parseFloat(reader.readLine(prefix + " recoil resistance")));
        rotatingGun.setRecoil(Float.parseFloat(reader.readLine(prefix + " recoil")));
        rotatingGun.setRotationSpeed(Float.parseFloat(reader.readLine(prefix + " rotation speed")));

        List<Float> relativeX = new ArrayList<Float>();
        List<Float> relativeY = new ArrayList<Float>();

        relativeX.add(Float.parseFloat(reader.readLine(prefix + " relative x north")));
        relativeX.add(Float.parseFloat(reader.readLine(prefix + " relative x north east")));
        relativeX.add(Float.parseFloat(reader.readLine(prefix + " relative x east")));
        relativeX.add(Float.parseFloat(reader.readLine(prefix + " relative x south east")));
        relativeX.add(Float.parseFloat(reader.readLine(prefix + " relative x south")));
        relativeX.add(Float.parseFloat(reader.readLine(prefix + " relative x south west")));
        relativeX.add(Float.parseFloat(reader.readLine(prefix + " relative x west")));
        relativeX.add(Float.parseFloat(reader.readLine(prefix + " relative x north west")));

        relativeY.add(Float.parseFloat(reader.readLine(prefix + " relative y north")));
        relativeY.add(Float.parseFloat(reader.readLine(prefix + " relative y north east")));
        relativeY.add(Float.parseFloat(reader.readLine(prefix + " relative y east")));
        relativeY.add(Float.parseFloat(reader.readLine(prefix + " relative y south east")));
        relativeY.add(Float.parseFloat(reader.readLine(prefix + " relative y south")));
        relativeY.add(Float.parseFloat(reader.readLine(prefix + " relative y south west")));
        relativeY.add(Float.parseFloat(reader.readLine(prefix + " relative y west")));
        relativeY.add(Float.parseFloat(reader.readLine(prefix + " relative y north west")));

        rotatingGun.setRelativeX(relativeX);
        rotatingGun.setRelativeY(relativeY);

        try {
            String requiredTech = reader.readLine(prefix + " required tech id");

            if (requiredTech != null) {
                rotatingGun.setRequiredTechId(requiredTech);
                rotatingGun.setCurrentlyPresent(false);
            } else {
                rotatingGun.setCurrentlyPresent(true);
            }
        } catch (Exception ex) {
        }

        try {
            rotatingGun.setIndividualRange(Float.parseFloat(reader.readLine(prefix + " individual range")));
        } catch (Exception ex) {
        }

        try {
            rotatingGun.setIndividualReloadSpeed(Float.parseFloat(reader.readLine(prefix + " individual reload speed")));
        } catch (Exception ex) {
        }

        Object[] lines = reader.readLines(prefix + " fire source").toArray();

        List<FireSource> fireSources = readFireSources(Arrays.copyOf(lines, lines.length, String[].class), reader);

        AbstractMap.SimpleEntry<RotatingGun, List<FireSource>> entry = new AbstractMap.SimpleEntry<RotatingGun, List<FireSource>>(rotatingGun, fireSources);

        return entry;
    }
}
