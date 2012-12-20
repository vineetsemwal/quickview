/**
 *
 Copyright 2012 Vineet Semwal

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
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
    public <T> Iterator<Item<T>> getItems( IItemFactory<T> factory, Iterator<IModel<T>> newModels, Iterator<Item<T>> existingItems) {
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
