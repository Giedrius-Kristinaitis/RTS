package com.gasis.rts.utils;

/**
 * Contains global game constants
 */
public class Constants {

    public static final float WIDTH = 20; // width of the game world
    public static final float HEIGHT = 14; // height of the game world

    // asset folders
    public static final String FOLDER_ATLASES = "atl/";
    public static final String FOLDER_MAPS = "maps/";
    public static final String FOLDER_ANIMATIONS = "animations/";
    public static final String FOLDER_STANDALONE_IMAGES = "standalone_images/";
    public static final String FOLDER_UNITS = "units/";
    public static final String FOLDER_BUILDINGS = "buildings/";
    public static final String FOLDER_TECHS = "techs/";
    public static final String FOLDER_FACTIONS = "factions/";
    public static final String FOLDER_CONFIG = "config/";
    public static final String FOLDER_CONTROL_CONTEXTS = "config/control_contexts/";
    public static final String FOLDER_SOUNDS = "sounds/";

    // global textures
    public static final String GENERAL_TEXTURE_ATLAS = "atl/general.atlas";
    public static final String CRATER_TEXTURE_ATLAS = "craters.atlas";
    public static final String LARGE_CRATER_PREFIX = "large_crater_";
    public static final String SMALL_CRATER_PREFIX = "small_crater_";
    public static final int LARGE_CRATER_COUNT = 4;
    public static final int SMALL_CRATER_COUNT = 3;
    public static final String HP_BAR_BACKGROUND_TEXTURE = "hp_bar_background";
    public static final String HP_BAR_GREEN_TEXTURE = "hp_bar_green";
    public static final String HP_BAR_YELLOW_TEXTURE = "hp_bar_yellow";
    public static final String HP_BAR_RED_TEXTURE = "hp_bar_red";
    public static final String CONSTRUCTION_HP_BAR_TEXTURE = "construction_hp_bar";
    public static final String UNIT_SELECTION_CIRCLE_TEXTURE = "unit_selection_circle";
    public static final String PRODUCTION_PROGRESS_TEXTURE = "production_progress";
    public static final String NO_ELECTRICITY_INDICATOR_TEXTURE = "no_electricity_indicator";
    public static final String BUILDING_DAMAGE_ATLAS = "atl/building_damage.atlas";
    public static final String BUILDING_DAMAGE_PREFIX = "building_damage_";
    public static final int BUILDING_DAMAGE_TEXTURE_COUNT = 7;
    public static final String CURSOR_ATLAS = "atl/general.atlas";
    public static final String CURSOR_TEXTURE_NORMAL = "cursor_normal";
    public static final String CURSOR_TEXTURE_ATTACK = "cursor_attack";
    public static final String UNEXPLORED_AREA = "unexplored_area";
    public static final String FOG_OF_WAR = "fog_of_war";

    // minimap textures
    public static final String MINIMAP_ATLAS = "minimap.atlas";
    public static final String MINIMAP_BLOCK_UNEXPLORED = "block_unexplored";
    public static final String MINIMAP_BLOCK_EXPLORED_INVISIBLE = "block_explored_invisible";
    public static final String MINIMAP_BLOCK_VISIBLE = "block_visible";
    public static final String MINIMAP_BLOCK_TERRAIN_OBJECT = "block_terrain_object";
    public static final String MINIMAP_BLOCK_OBJECT_PREFIX = "block_object_";
    public static final String MINIMAP_HEAVY_UNIT_PREFIX = "heavy_unit_";
    public static final String MINIMAP_LIGHT_UNIT_PREFIX = "light_unit_";
    public static final String MINIMAP_BOUNDS = "bounds";
    public static final String MINIMAP_BORDER_RIGHT = "border_right";
    public static final String MINIMAP_BORDER_BOTTOM = "border_bottom";
}
