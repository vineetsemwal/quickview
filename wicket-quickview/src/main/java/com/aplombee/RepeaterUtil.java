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

import org.apache.wicket.*;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.IMarkupFragment;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.util.lang.Args;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *@author Vineet Semwal
 *
 */
public class RepeaterUtil implements  IRepeaterUtil{
    private static final MetaDataKey<RepeaterUtil> REPEATER_UTIL_KEY = new MetaDataKey<RepeaterUtil>() {
    };

    public static RepeaterUtil get() {
        Application app = Application.get();
        RepeaterUtil util = app.getMetaData(REPEATER_UTIL_KEY);
        if (util != null) {
            return util;
        }
        util = new RepeaterUtil(app); //this registers too
        return util;
    }

    private Application application;

    public RepeaterUtil(Application application) {
        this.application = application;
        application.setMetaData(REPEATER_UTIL_KEY, this);
    }

    /**
     * {@inheritDoc}
     */
    public String insertBefore(String tag, String markupId, String parentMarkupId) {
        String script = String.format("insertBefore('%s','%s','%s');", tag, markupId, parentMarkupId);
        return script;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String insertBefore(MarkupContainer component, MarkupContainer parent) {
        String script = insertBefore(getComponentTag(component).getName(), component.getMarkupId(), parent.getMarkupId());
        return script;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String insertAfter(String tag, String markupId, String parentMarkupId) {
        String script = String.format("insertAfter('%s','%s','%s');", tag, markupId, parentMarkupId);
        return script;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ComponentTag getComponentTag(Component c) {
        IMarkupFragment markup = c.getMarkup();
        MarkupStream stream = new MarkupStream(markup);
        return stream.getTag();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String insertAfter(MarkupContainer c, MarkupContainer parent) {
        return insertAfter(getComponentTag(c).getName(), c.getMarkupId(), parent.getMarkupId());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String removeItem(String markupId) {
        String script = String.format("removeItem('%s');", markupId);
        return script;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String removeItem(Component component) {
        Args.notNull(component, "component");
        return removeItem(component.getMarkupId());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int safeLongToInt(long value) {
        if (value < Integer.MIN_VALUE || value > Integer.MAX_VALUE) {
            throw new IllegalArgumentException(value + " cannot be cast to int ");
        }
        return (int) value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void parentNotSuitable(IQuickView quickView) {
        Args.notNull(quickView, "quickview");
        if (!quickView.getReuseStrategy() .isAddItemsSupported()) {
            return;
        }
        MarkupContainer parent = quickView.getParent();
        if (parent == null) {
            throw new QuickViewNotAddedToParentException("add quickview to a markupcontainer");
        }
        if (parent instanceof Page) {
            throw new QuickViewNotAddedToParentException("add quickview to a markupcontainer");
        }
        if (parent.size() > 1) {
            throw new ParentNotUnaryException("the markupcontainer to which quickview is attached should have quickview as its only child");
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public final void outPutMarkupIdNotTrue(IQuickView quickView) {
        Args.notNull(quickView, "quickview");
        MarkupContainer container = quickView.getParent();
        if (container.getOutputMarkupId() == false && container.getOutputMarkupPlaceholderTag() == false) {
            throw new OutputMarkupIdNotTrueException("parent doesn't have setOutputMarkupId to true");
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public final void reuseStategyNotSupportedForItemsNavigation(IQuickView quickView) {
        Args.notNull(quickView, "quickview");
        if (!quickView.getReuseStrategy().isAddItemsSupported()) {
            throw new ReuseStrategyNotSupportedException(" stategy is not supported for itemsnavigator ");
        }
    }


    @Override
    public String scrollToBottom(String markupId){
      return String.format("scrollToBottom('%s');",markupId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String scrollToBottom(IQuickView quickView){
      return scrollToBottom(quickView.getParent().getMarkupId());
    }

    @Override
    public String scrollToTop(String markupId){
        return String.format("scrollToTop('%s');",markupId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String scrollToTop(IQuickView quickView){
        return  scrollToTop(quickView.getParent().getMarkupId());
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String scrollTo(IQuickView quickView,int height){
            return scrollTo(quickView.getParent().getMarkupId(),height);
    }

    @Override
    public String scrollTo(String markupId,int height){
        return String.format("scrollTo('%s',%d);",markupId,height);
    }



    /**
     * {@inheritDoc}
     */
    @Override
    public String isComponentScrollBarAtBottom(MarkupContainer component) {
        return String.format("isComponentScrollBarAtBottom('%s');",component.getMarkupId());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String isPageScrollBarAtBottom(){
       return "isPageScrollBarAtBottom();";
    }


}
