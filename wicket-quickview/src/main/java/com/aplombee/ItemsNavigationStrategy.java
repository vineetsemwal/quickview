package com.aplombee;

import org.apache.wicket.markup.repeater.DefaultItemReuseStrategy;
import org.apache.wicket.markup.repeater.IItemFactory;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.ReuseIfModelsEqualStrategy;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * this strategy supports addition of new items without the need to re-render the view  .
 *  on re-render all items are removed and items for first page are created again
 *
 *  used with  {@link com.aplombee.navigator.AjaxItemsNavigator}
 *  or {@link com.aplombee.navigator.AjaxScrollEventBehaviorBase}
 * <p/>
 * 1)all children are removed and children of first page are created again on re-render <br/>
 * 2) new children for next page is created in  {@link com.aplombee.QuickViewBase#addItemsForNextPage()}
 * <p/>
 *
 * earlier it was used as Reuse.ITEMSNAVIGATION
 *
 * @author Vineet Semwal
 */
public class ItemsNavigationStrategy extends AbstractItemsNavigationStrategy {

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
    public <T> Iterator<Item<T>> getItems(IDataProvider<T>dataProvider,int itemsPerRequest, IItemFactory<T> factory, Iterator<IModel<T>> newModels, Iterator<Item<T>> existingItems) {
     Iterator<IModel<T>>models=new QuickViewBase.ModelIterator<T>(dataProvider,0,itemsPerRequest);
       return DefaultItemReuseStrategy.getInstance().getItems(factory, models, existingItems);
    }

}
