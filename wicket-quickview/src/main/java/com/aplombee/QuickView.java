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


import org.apache.wicket.markup.repeater.data.IDataProvider;

/**
 *
 * adds,deletes elements without the need to re-render the View
 *
 * QuickView's default behavior is of paging ie. items are added to view on re-render . it uses
 * {@link DefaultQuickReuseStrategy}  by default so It works fine with
 * {@link org.apache.wicket.markup.html.navigation.paging.PagingNavigator}
 * or {@link org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator}  by default
 *
 *  however on setting strategy like {@link ItemsNavigationStrategy} or {@link ReuseAllStrategy} QuickView get capability
 *  to add  new items without the need to re-render the view
 *
 * limitation of QuickView is the parent of QuickView should have only one child and that should be QuickView .this
 * limitation is only for case where item(s) has to be added dynamically without re-rendering the View.
 *
 *
 */
public abstract class QuickView<T> extends QuickViewBase<T> {

    /**
     *
     * @param id
     * @param dataProvider
     *  @param itemsPerRequest items to be constructed per Page or request
     */
    public QuickView(String id, IDataProvider<T> dataProvider, IQuickReuseStrategy reuseStrategy, int itemsPerRequest) {
        super(id, dataProvider, reuseStrategy);
        setItemsPerRequest(itemsPerRequest);

    }

    /**
     *
     * @param id
     * @param dataProvider
     *
     */
    public QuickView(String id, IDataProvider<T> dataProvider,IQuickReuseStrategy reuseStrategy) {
        super(id, dataProvider, reuseStrategy);
     }

    /**
     *
     * @param id
     * @param dataProvider
     *
     */
    public QuickView(String id, IDataProvider<T> dataProvider) {
        super(id, dataProvider,new DefaultQuickReuseStrategy());
    }

    public QuickView(String id, IDataProvider<T> dataProvider, int itemsPerRequest) {
        super(id, dataProvider, new DefaultQuickReuseStrategy());
        setItemsPerRequest(itemsPerRequest);
    }
}
