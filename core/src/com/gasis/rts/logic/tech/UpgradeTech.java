package com.gasis.rts.logic.tech;

import com.gasis.rts.filehandling.FileLineReader;
import com.gasis.rts.logic.object.GameObject;
import com.gasis.rts.logic.object.building.BuildingLoader;
import com.gasis.rts.logic.object.combat.DefensiveSpecs;
import com.gasis.rts.logic.object.combat.OffensiveSpecs;
import com.gasis.rts.logic.object.research.TechApplicationListener;
import com.gasis.rts.logic.object.unit.UnitLoader;
import com.gasis.rts.logic.player.Player;

import java.util.*;

/**
 * Any unit or building upgrade
 */
public class UpgradeTech extends Tech implements TechApplicationListener {

    // to what object type this tech applies
    protected Set<String> objectCodes = new HashSet<String>();

    // how much are stats increased for the target objects
    protected Map<String, Float> statIncreases = new HashMap<String, Float>();

    /**
     * Applies the tech to the specified player
     *
     * @param player player to apply the tech to
     */
    @Override
    public void apply(Player player) {
        if ((requiredTechId == null || player.isTechResearched(requiredTechId)) && player.getSelectedBuilding() != null && !player.isTechResearched(id) && !player.isTechQueuedUp(id)) {
            player.getSelectedBuilding().queueUpTech(this);
        }
    }

    /**
     * Called when the tech is finished being applied
     *
     * @param player player the tech was applied to
     */
    @Override
    public void applied(Player player) {
        player.addResearchedTech(id);

        if (!statIncreases.isEmpty()) {
            applyUnitStatIncreases(player);
            applyBuildingStatIncreases(player);
        }
    }

    /**
     * Applies stat increases to player's units
     *
     * @param player player to apply to
     */
    private void applyUnitStatIncreases(Player player) {
        for (UnitLoader loader : player.getFaction().getUnitLoaders().values()) {
            if (objectCodes.contains(loader.getCode())) {
                applyStatIncreaseToSpecs(player, loader.getCode(), loader.getDefensiveSpecs(), loader.getOffensiveSpecs());
            }
        }
    }

    /**
     * Applies spec increases to spec instances
     *
     * @param player     which player does the tech apply to
     * @param objectCode object's code
     * @param def        defensive specs
     * @param off        offensive specs
     */
    private void applyStatIncreaseToSpecs(Player player, String objectCode, DefensiveSpecs def, OffensiveSpecs off) {
        for (Map.Entry<String, Float> stat : statIncreases.entrySet()) {
            if (stat.getKey().equalsIgnoreCase("attack")) {
                off.setAttack(off.getAttack() + stat.getValue());
            } else if (stat.getKey().equalsIgnoreCase("siege mode attack")) {
                off.setSiegeModeAttack(off.getSiegeModeAttack() + stat.getValue());
            } else if (stat.getKey().equalsIgnoreCase("attack range")) {
                off.setAttackRange(off.getAttackRange() + stat.getValue());
            } else if (stat.getKey().equalsIgnoreCase("siege mode attack range")) {
                off.setSiegeModeAttackRange(off.getSiegeModeAttackRange() + stat.getValue());
            } else if (stat.getKey().equalsIgnoreCase("speed")) {
                off.setSpeed(off.getSpeed() + stat.getValue());
            } else if (stat.getKey().equalsIgnoreCase("defence")) {
                def.setDefence(def.getDefence() + stat.getValue());
            } else if (stat.getKey().equalsIgnoreCase("max hp")) {
                float oldMaxHp = def.getMaxHp();
                def.setMaxHp(def.getMaxHp() + stat.getValue());
                modifyObjectHp(player, objectCode, oldMaxHp);
            } else if (stat.getKey().equalsIgnoreCase("sight range")) {
                def.setSightRange(def.getSightRange() + stat.getValue());
            } else if (stat.getKey().equalsIgnoreCase("siege mode sight range")) {
                def.setSiegeModeSightRange(def.getSiegeModeSightRange() + stat.getValue());
            }
        }
    }

    /**
     * Modifies objects' hp on hp stat increase
     *
     * @param player   object owner
     * @param code     object code
     * @param oldMaxHp the old max hp value for the objects
     */
    private void modifyObjectHp(Player player, String code, float oldMaxHp) {
        for (GameObject object : player.getUnits()) {
            if (object.getCode().equals(code)) {
                object.setHp(object.getHp() + object.getDefensiveSpecs().getMaxHp() - oldMaxHp);
            }
        }

        for (GameObject object : player.getBuildings()) {
            if (object.getCode().equals(code)) {
                object.setHp(object.getHp() + object.getDefensiveSpecs().getMaxHp() - oldMaxHp);
            }
        }
    }

    /**
     * Applies stat increases to player's buildings
     *
     * @param player player to apply to
     */
    private void applyBuildingStatIncreases(Player player) {
        for (BuildingLoader loader : player.getFaction().getBuildingLoaders().values()) {
            if (objectCodes.contains(loader.getCode())) {
                applyStatIncreaseToSpecs(player, loader.getCode(), loader.getDefensiveSpecs(), loader.getOffensiveSpecs());
            }
        }
    }

    /**
     * Loads tech data
     *
     * @param reader file reader to read data from
     * @return
     */
    @Override
    protected void loadData(FileLineReader reader) {
        try {
            List<String> objectCodes = reader.readLines("applies to object");
            this.objectCodes.addAll(objectCodes);

            List<String> stats = reader.readLines("stat increase");

            for (String stat : stats) {
                statIncreases.put(stat, Float.parseFloat(reader.readLine(stat + " increased by")));
            }
        } catch (Exception ex) {
        }
    }
}
