package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.xml;

/**
 * The Interface for database persistence.
 * The Interface consists of a method, which allows
 * the implementer to be saved in the database.
 */
public interface IPersistable {
    /**
     * Writes the data from the object to the database.
     * The id will be returned.
     *
     * @return The ID of the newly created entry
     */
    int save();
}
