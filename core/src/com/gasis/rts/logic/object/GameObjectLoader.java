package com.gasis.rts.logic.object;

import com.badlogic.gdx.files.FileHandle;
import com.gasis.rts.filehandling.FileLineReader;

/**
 * Loads a game object from a file
 */
public abstract class GameObjectLoader {

    // has the object been loaded yet
    protected boolean loaded = false;

    // code of the object
    protected String code;

    // name of the texture atlas that holds the object's textures
    protected String atlas;

    // dimensions of the object
    protected float width;
    protected float height;

    // the width of the object's hp bar
    protected float hpBarWidth = 1f;

    // the object's control context's name
    protected String controlContextName = "default";

    /**
     * Loads a game object from the given file
     *
     * @param file file to load from
     *
     * @return true if the object was loaded successfully
     */
    public final boolean load(FileHandle file) {
        try {
            FileLineReader reader = new FileLineReader(file.read(), ":");

            readMetaData(reader);
            readOtherData(reader);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }

        return (loaded = true);
    }

    /**
     * Reads meta data of a unit
     *
     * @param reader reader to read data from
     */
    protected void readMetaData(FileLineReader reader) {
        code = reader.readLine("code");
        atlas = reader.readLine("atlas");

        width = Float.parseFloat(reader.readLine("width"));
        height = Float.parseFloat(reader.readLine("height"));

        try {
            hpBarWidth = Float.parseFloat(reader.readLine("hp bar width"));
        } catch (Exception ex) {}
    }

    /**
     * Reads other data of the object that is not meta data
     *
     * @param reader reader to read data from
     */
    protected abstract void readOtherData(FileLineReader reader);

    /**
     * Creates a new instance of the loaded object
     *
     * @return new instance of the loaded object
     */
    public abstract GameObject newInstance();

    /**
     * Gets the code of the loaded object
     * @return
     */
    public String getCode() {
        return code;
    }

    /**
     * Gets the width of the object
     * @return
     */
    public float getWidth() {
        return width;
    }

    /**
     * Gets the height of the object
     * @return
     */
    public float getHeight() {
        return height;
    }

    /**
     * Gets the name of the building's control context
     * @return
     */
    public String getControlContextName() {
        return controlContextName;
    }

    /**
     * Sets the name of the building's control context
     *
     * @param controlContextName new name
     */
    public void setControlContextName(String controlContextName) {
        this.controlContextName = controlContextName;
    }
}
