package com.aplombee;

import org.apache.wicket.markup.repeater.IItemFactory;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *  reuse strategy that reuse existing items on re-render ,this strategy supports addition of new
 *  items without the need to re-render the view
 *
 *  used with {@link com.aplombee.navigator.AjaxItemsNavigator}
 *  or
 * {@link com.aplombee.navigator.AjaxScrollEventBehaviorBase}
 * <br/>
 *
 *
 *  @author Vineet Semwal
 *
 */

public class ReuseAllStrategy extends AbstractItemsNavigationStrategy{

    /**
     * reuseas all the existing items by returning iterator over existing items after copying them
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
     * @return iterator over exisiting items
     */
    @Override
    public <T> Iterator<Item<T>> getItems(IDataProvider<T> dataProvider, int itemsPerRequest, IItemFactory<T> factory, Iterator<IModel<T>> newModels, Iterator<Item<T>> existingItems) {
        List<Item<T>>copy=new ArrayList<Item<T>>();
        while(existingItems.hasNext()){
            copy.add(existingItems.next());
        }
        if(!copy.isEmpty())
        {
            return copy.iterator();
        }

        for(int index=0;newModels.hasNext();index++)
         {
           IModel<T> model=newModels.next();
           Item<T>item= factory.newItem(index,model);
             copy.add(item);
         }

        return copy.iterator();
    }

    @Override
    public boolean isAlwaysZeroPageCreatedOnReRender() {
        return false;
    }
}
