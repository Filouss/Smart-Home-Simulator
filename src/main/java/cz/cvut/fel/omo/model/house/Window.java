package cz.cvut.fel.omo.model.house;

import cz.cvut.fel.omo.model.simulation.event.BaseDoesStuffEveryTenMinutes;

/**
 * This class represents a house window.
 */
public class Window extends BaseDoesStuffEveryTenMinutes {
    private boolean blindsClosed;
    private boolean windowClosed;

    public Window() {
        this.blindsClosed = false;
        this.windowClosed = true;
    }

    /**
     * This method determines whether the blinds are open or closed
     */
    public boolean isBlindsClosed() {
        return this.blindsClosed;
    }

    public void setBlindsClosed(boolean blindsClosed) {
        this.blindsClosed = blindsClosed;
    }

    /**
     * This method determines whether the window is closed
     */
    public boolean isWindowClosed() {
        return this.windowClosed;
    }

    public void setWindowClosed(boolean windowClosed) {
        this.windowClosed = windowClosed;
    }
}
