package com.gasis.rts.logic.map;

/**
 * Parses a map from one map format to another
 *
 * @param <T> type of the map that will be parsed to a normal map (by normal map meaning any
 * map format that is not in the com.gasis.rts.map package or it's sub-packages)
 */
public interface MapParser<T> {

    /**
     * Parses a given map into a normal map
     *
     * @param map map to parse
     * @return normal parsed map
     */
    Map parse(T map);
}
