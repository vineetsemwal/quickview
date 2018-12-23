package com.aplombee;

import org.apache.wicket.Component;

import java.io.Serializable;
import java.util.Iterator;

/**
 * store for elements added at start in quickview
 */
public interface IAddAtStartStore extends Serializable {

    /**
     * remove component id from store
     * @param id component id
     */
    void remove(String id);

    /**
     * remove component id from store
     * @param component component whose id has to be removed
     */
    void remove(Component component) ;

    /**
     * iterator of components id kept in the store
     *
     * @return
     */
    Iterator<String> iterator();

    /**
     * count of ids kept in store
     *
     * @return
     */
    int size();

    /**
     * adds components id in store along with their indexes
     * <p>
     * should be called before calling simpleAdd(Components) else
     * it will not assume the right quickview indexes
     *
     * @param components component that are added at start
     */
    void add(final Component... components);

    /**
     *  returns true if component's id exist in store
     *
     * @param component
     * @return
     */
    boolean contains(Component component);

    /**
     * returns true if id exists in store
     *
     * @param componentId
     * @return
     */
    boolean contains(String componentId);

}
