package com.aplombee;

import org.apache.wicket.markup.repeater.IItemFactory;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;

import java.util.Iterator;

/**
 *   abstract reuse strategy that does NOT support addition of items to the view without the need to re-render .
 *   this strategy is typically used only for paging foreg. when QuickView is used with {@link org.apache.wicket.markup.html.navigation.paging.PagingNavigator}
 *
 *   also to read more @see IQuickReuseStrategy
 *
 *
 * @author Vineet Semwal
 */
public abstract class AbstractPagingNavigationStrategy implements IQuickReuseStrategy{

    @Override
    public <T> Iterator<Item<T>> addItems(int startIndex, IItemFactory<T> factory, Iterator<IModel<T>> newModels) {
        throw  new UnsupportedOperationException("adding items dynamically for partial updates is not supported by this strategy");
    }

    public boolean isPaging(){
        return true;
    }
}
