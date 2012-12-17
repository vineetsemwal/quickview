package com.aplombee;

import org.apache.wicket.markup.repeater.IItemFactory;
import org.apache.wicket.markup.repeater.IItemReuseStrategy;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;

import java.io.Serializable;
import java.util.Iterator;

/**
 *  Interface for item reuse strategies.
 * <p>
 * <u>Notice:</u> Child items will be rendered in the order they are provided by the returned
 * iterator, so it is important that the strategy preserve this order
 * </p>
 *
 * depending on use any new reuse strategy can be created ,few ready-made for QuickView strategies are <br/>
 *
 * 1) {@link DefaultQuickReuseStrategy}
 *
 *  <br/>
 * 2) {@link QuickReuseIfModelsEqualStrategy}
 *  <br/>
 *
 * 3) {@link ItemsNavigationStrategy}
 *   <br/>
 *
 * 4) {@link ReuseAllStrategy}
 *
 *
 * @author Vineet Semwal
 */
public interface IQuickReuseStrategy extends Serializable {

    /**
     * Returns an iterator over items that will be added to the view without re-rendering the QuickView
     *
     * @param <T>
     *            type of Item
     *
     * @param factory
     *            implementation of IItemFactory
     * @param newModels
     *            iterator over models for items

     * @return iterator over items that will be added
     */
    <T> Iterator<Item<T>> addItems(int startIndex,IItemFactory<T> factory, Iterator<IModel<T>> newModels);

    /**
     * Returns an iterator over items that will be added to the view when QuickView is re-rendered. The iterator needs to return
     * all the items because the old ones are removed prior to the new ones added.
     *
     * @param dataProvider dataProvider provided to QuickView
     *
     * @param itemsPerRequest no of item created per request
     *
     * @param <T>    type of Item
     *
     * @param factory
     *            implementation of IItemFactory
     * @param newModels
     *            iterator over models for items
     * @param existingItems
     *            iterator over child items
     * @return iterator over items that will be added after all the old items are moved.
     */
    <T> Iterator<Item<T>> getItems(IDataProvider<T> dataProvider,int itemsPerRequest, IItemFactory<T> factory, Iterator<IModel<T>> newModels, Iterator<Item<T>> existingItems);


    /**
     *  tells whether reuse strategy support addition of items to view without re-rendering QuickView
     *
     * @return   boolean
     */
    boolean isAddItemsSupported();

}
