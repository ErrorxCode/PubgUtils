package com.pubg.utils;


/**
 * This interface is used to execute a piece of code.
 */
public interface OneTimeEventInterface {
    /**
     * This fires the event
     * @return <code>true</code> if the event has to be executed once more, <code>false</code> otherwise
     */
    boolean OneTimeEvent();
}
