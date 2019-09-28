package com.gasis.rts.ui.composer;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.gasis.rts.filehandling.FileLineReader;

/**
 * Composes user interface from ui description files
 */
public interface ComposerInterface {

    /**
     * Composes a ui table from the data collected from the specified line reader
     *
     * @param reader reader to read data from
     * @return
     */
    Table compose(FileLineReader reader);
}
