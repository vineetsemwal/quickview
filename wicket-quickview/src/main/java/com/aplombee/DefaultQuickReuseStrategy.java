package com.aplombee;

import org.apache.wicket.markup.repeater.DefaultItemReuseStrategy;
import org.apache.wicket.markup.repeater.IItemFactory;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;

import java.util.Iterator;

/**
 * default reuse strategy used by QuickView ,this does NOT support addition of new items without re-rendering
 * the view .it's  used with {@link org.apache.wicket.markup.html.navigation.paging.PagingNavigator}
 * or {@link org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator}
 *
 * basically it's a wrapper of DefaultItemReuseStrategy
 *
 *  to read more about it  {@see DefaultItemReuseStrategy}
 *
 * @author Vineet Semwal
 */
public class DefaultQuickReuseStrategy extends AbstractPagingNavigationStrategy {

    /**
     * Returns an iterator over items that will be added to the view. The iterator needs to return
     * all the items because the old ones are removed prior to the new ones added.
     *
     * @param <T>
     *            type of Item
     *
     * @param factory
     *            implementation of IItemFactory
     * @param newModels
     *            iterator over models for items
     * @param existingItems
     *            iterator over child items
     * @return iterator over items that will be added after all the old items are moved.
     */
    @Override
    public <T> Iterator<Item<T>> getItems(IDataProvider<T>dataProvider,int itemsPerRequest,IItemFactory<T> factory, Iterator<IModel<T>> newModels, Iterator<Item<T>> existingItems) {
      return DefaultItemReuseStrategy.getInstance().getItems(factory,newModels,existingItems);
    }

}
