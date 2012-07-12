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
package com.repeater;


import org.apache.log4j.Logger;
import org.apache.wicket.markup.repeater.data.IDataProvider;

/**
 *
 * adds,deletes elements without the need to re-render the whole repeater
 *
 * the type of reuse constant to use  must be set ,for {@link org.apache.wicket.markup.html.navigation.paging.PagingNavigator}
 * or {@link org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator} {@link ReUse.DEFAULT_PAGING} is preferred
 *
 * for {@link com.repeater.navigator.AjaxItemsNavigator} {@link ReUse.DEFAULT_ROWSNAVIGATOR} is preferred
 *
 * add quickview to a Markupcontainer to use with {@link com.repeater.navigator.AjaxItemsNavigator}  or if you want to add new rows
 * using an ajax acomponent ,the markupcontainer should have only one child and that should be quickview for items/rows navigation
 *
 *
 */
public abstract class QuickView<T> extends QuickViewBase<T> {

    private static Logger logger = Logger.getLogger(QuickView.class);
    /**
     *
     * @param id
     * @param dataProvider
     *  @param itemsPerRequest items to be constructed per Page or request
     */
    public QuickView(String id, IDataProvider<T> dataProvider, ReUse reUse, int itemsPerRequest) {
        super(id, dataProvider, reUse);
        setItemsPerRequest(itemsPerRequest);

    }

    /**
     *
     * @param id
     * @param dataProvider
     *
     */
    public QuickView(String id, IDataProvider<T> dataProvider, ReUse reUse) {
        super(id, dataProvider, reUse);
     }

    /**
     *
     * @param id
     * @param dataProvider
     *
     */
    public QuickView(String id, IDataProvider<T> dataProvider) {
        super(id, dataProvider, ReUse.NOT_INITIALIZED);
    }

    public QuickView(String id, IDataProvider<T> dataProvider, int itemsPerRequest) {
        super(id, dataProvider, ReUse.NOT_INITIALIZED);
        setItemsPerRequest(itemsPerRequest);
    }
}
