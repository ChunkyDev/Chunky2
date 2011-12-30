package org.getchunky.chunkyapi.persistence;

public interface Persistence {

    /**
     * This method is called to initialize the storage system.  If using a DB back end, this
     * is the method that should create the tables if they don't exist.
     * <p/>
     * It is possible that this method could be called multiple times, so it is this methods
     * responsibility to keep track of whether it has already initialized and deal with that
     * situation appropriately.
     */
    public void initialize();

    /**
     * Notify the backing store that it should purge any in-memory cache it has.
     */
    public void purgeCache();
}
