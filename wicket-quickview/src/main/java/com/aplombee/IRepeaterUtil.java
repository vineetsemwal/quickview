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

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.repeater.Item;

import java.util.Iterator;
import java.util.List;

/**
 * @author Vineet Semwal
 */
public interface IRepeaterUtil {
    /**
     * insertBefore js call
     *
     * @param tag      repeater tag
     * @param id       repeater markupid
     * @param parentId parent markupid
     * @return insertBefore call of js
     */
    String insertBefore(String tag, String id, String parentId);

    /**
     * insertBefore js call for the repeeater and parent passed
     *
     * @param component repeater
     * @param parent    parent to which repeater is added
     * @return insertBefore call of js
     */
    String insertBefore(MarkupContainer component, MarkupContainer parent);

    /**
     * @param tag      repeater tag
     * @param id       repeater markupid
     * @param parentId parent markupid
     * @return insertAfter call of js
     */
    String insertAfter(String tag, String id, String parentId);

    /**
     * finds {@link ComponentTag} of the component passed
     *
     * @param c component whose componenttag has to be found
     * @return {@link ComponentTag}
     */
    ComponentTag getComponentTag(Component c);

    /**
     * @param c      repeater
     * @param parent parent
     * @return insertAfter js call
     */
    String insertAfter(MarkupContainer c, MarkupContainer parent);

    /**
     * removes js call for item whose markupid is passed
     *
     * @param id markupid of the element which needs to be removed
     * @return remove js call
     */
    String removeItem(String id);

    /**
     * removes js call for component which is provided
     *
     * @param component
     * @return remove js call
     */
    String removeItem(Component component);

    /**
     * safely converts long to int
     *
     * @param l
     * @return int value for long passed
     */
    int safeLongToInt(long l);


    /**
     * throws exception if no suitable unary parent is found,unary parent is one which only has one child
     * @param repeater
     */
    void parentNotSuitable(IQuickView repeater);

    /**
     * throws exception if reuse strategy is not supported  for items navigation
     * @param repeater
     */
    void reuseStategyNotSupportedForItemsNavigation(IQuickView repeater);

    /**
     *  throws exception if outmarkupid of parent is not set true and outputMarkupPlaceholderTag is not set true
     *
     * @param repeater
     */
    void outPutMarkupIdNotTrue(IQuickView repeater);


    /**
     *  js call to scroll to top
     *
     * @param quickView
     * @return   js call  string
     */
    String scrollToTop(IQuickView quickView) ;

    String scrollToTop(String markupId);

    /**
     *
     * js call to scroll to bottom
     * @param quickView
     * @return  js call  string
     */
    String scrollToBottom(IQuickView quickView);

    String scrollToBottom(String markupId);

    String scrollTo(String markupId,int height);

    /**
     * js call to scroll to height
     *
     * @param quickView
     * @param height
     * @return  js call string
     */
    String scrollTo(IQuickView quickView,int height);

    /**
     * js calls which calls methods that when fired returns true if component's navigation-bar is at the bottom
     * @param component
     * @return   js call string
     */
    public String isComponentScrollBarAtBottom(MarkupContainer component);

    /**
     * js calls which calls methods that when fired returns true if page's navigation-bar is at the bottom
     * @param component
     * @return  js call string
     */
    public String isPageScrollBarAtBottom();


    /**
     *  throw this exception if quickview's parent is not found
     *
     * @author Vineet Semwal
     */
    public static class QuickViewNotAddedToParentException extends RuntimeException {
        public QuickViewNotAddedToParentException(String message) {
            super(message);
        }
    }

    /**
     * throw this exception if outmarkupid is not set to true
     * @author Vineet Semwal
     */
    public static class OutputMarkupIdNotTrueException extends RuntimeException {
        public OutputMarkupIdNotTrueException(String message) {
            super(message);
        }
    }

    /**
     *  throw this exception if reuse constant set is not supported
     * @author Vineet Semwal
     */
    public static class ReuseStrategyNotSupportedException extends RuntimeException {
        public ReuseStrategyNotSupportedException(String message) {
            super(message);
        }
    }

    /**
     *  throw this exception if quickview's parent has more than one child in case of items navigation
     * @author Vineet Semwal
     */
    public static class ParentNotUnaryException extends RuntimeException {
        public ParentNotUnaryException(String message) {
            super(message);
        }
    }


    /**
     *  throw this exception if quickview is not initialized with reuse constant
     * @author Vineet Semwal
     */
    public static class ReuseNotInitializedException extends RuntimeException {
        public ReuseNotInitializedException(String message) {
            super(message);
        }
    }
}
