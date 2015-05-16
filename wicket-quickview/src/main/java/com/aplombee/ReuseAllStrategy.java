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
import org.apache.wicket.markup.repeater.ReuseIfModelsEqualStrategy;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * reuse strategy that reuse existing items on re-render if models are equal see
 * {@link org.apache.wicket.markup.repeater.ReuseIfModelsEqualStrategy} to read more
 *
 *
 * this strategy also supports partial updates ie. items can be added/removed without
 * the need to render  view                                                    *
 *
 * this strategy is different from QuickReuseIfModelsEqualStrategy in that it keeps
 * all the items upto the the page rendered on re-render , QuickReuseIfModelsEqualStrategy only
 * renders the items for the last page on re-render
 *
 *
 *
 * <p/>
 * used with {@link com.aplombee.navigator.AjaxItemsNavigator}
 * or
 * {@link com.aplombee.navigator.AjaxScrollEventBehaviorBase}
 * <br/>
 *
 * @author Vineet Semwal
 */

public class ReuseAllStrategy extends AbstractItemsNavigationStrategy {

    /**
     *  reuses if models are equal
     *
     * @param <T>           type of Item
     * @param factory       implementation of IItemFactory
     * @param newModels     iterator over models for items
     * @param existingItems iterator over child items
     * @return iterator over existing items
     */
    @Override
    public <T> Iterator<Item<T>> getItems(IItemFactory<T> factory, Iterator<IModel<T>> newModels, Iterator<Item<T>> existingItems) {
        return ReuseIfModelsEqualStrategy.getInstance().getItems(factory,newModels,existingItems);

    }


    @Override
    public long getPageCreatedOnRender() {
        return -1;
    }
}
