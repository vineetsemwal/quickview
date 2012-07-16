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

/**
 * reuse strategy that is used with {@link QuickView}
 *
 * @author Vineet Semwal
 */
public enum ReUse {

    /**
     * all children are removed and children of last page visited are created ,used for paging say with {@link org.apache.wicket.markup.html.navigation.paging.PagingNavigator} or
     * {@link org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator}  ,this is the prefered strategy for paging navigation
     */
 DEFAULT_PAGING,

    /**
     * all children are removed and children of first page are created again ,mostly used and preferred for {@link com.repeater.navigator.AjaxItemsNavigator}
     */
    DEFAULT_ITEMSNAVIGATION,


    /**
     * reuse the current page visited children ,all else are removed  ,used with pagingnavigator ,not supported in Items or rows navigation
     */
  CURRENTPAGE,


    /**
     *     all children are reused,no child gets removed  or recreated,this should only be used with {@link com.repeater.navigator.AjaxItemsNavigator},
     *     the usecase for this can be a user has  itemsperequest 3 times after the initial render ,on page reload you want to show
     *     him all the rows he created so that he doesn't have to start again
     */
    ALL     ,


    /**
     * dont use it,this is internally used in QuickView
     */
    NOT_INITIALIZED
}
