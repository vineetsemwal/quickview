package com.aplombee;

import org.apache.wicket.markup.repeater.IItemFactory;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.ReuseIfModelsEqualStrategy;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;

import java.util.Iterator;


/**
 * reuse  strategy that does NOT support addition of items without re-rendering the view.
 *
 * used with {@link org.apache.wicket.markup.html.navigation.paging.PagingNavigator}
 *
 *  basically it's a wrapper of {@link ReuseIfModelsEqualStrategy}
 *
 *  to read more about it @see ReuseIfModelsEqualStrategy
 *
 *
 * @author Vineet Semwal
 *
 */
public class QuickReuseIfModelsEqualStrategy extends AbstractPagingNavigationStrategy {

    /**
     * @see org.apache.wicket.markup.repeater.IItemReuseStrategy#getItems(org.apache.wicket.markup.repeater.IItemFactory,
     *      java.util.Iterator, java.util.Iterator)
     */
      @Override
    public <T> Iterator<Item<T>> getItems(IDataProvider<T> dataProvider, int itemsPerRequest, IItemFactory<T> factory, Iterator<IModel<T>> newModels, Iterator<Item<T>> existingItems) {
        return ReuseIfModelsEqualStrategy.getInstance().getItems(factory,newModels,existingItems);
    }

}
