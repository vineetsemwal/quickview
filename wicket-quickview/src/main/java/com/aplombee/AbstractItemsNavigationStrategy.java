package com.aplombee;

import org.apache.wicket.markup.repeater.IItemFactory;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *   abstract reuse strategy that supports addition of items to the view without the need to re-render  .
 *
 *   also to read more @see IQuickReuseStrategy
 *    
 *
 * @author Vineet Semwal
 */
public abstract class AbstractItemsNavigationStrategy implements IQuickReuseStrategy {

    /**
     * @inheritDoc
     */
    @Override
    public <T> Iterator<Item<T>> addItems(int startIndex, IItemFactory<T> factory, Iterator<IModel<T>> newModels) {
        int itemIndex=startIndex;
        List<Item<T>> components = new ArrayList<Item<T>>();
        for (;newModels.hasNext();itemIndex++)
        {
            IModel<T>newModel=newModels.next();
           Item<T>item=factory.newItem(itemIndex,newModel);
            components.add(item);
        }

        return components.iterator();
    }

    /**
     * return false because this reuse stategy supports addition of items
     *
     */
    @Override
    public boolean isAddItemsSupported() {
        return true;
    }


}
