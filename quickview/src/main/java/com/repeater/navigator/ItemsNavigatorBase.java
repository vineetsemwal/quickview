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
package com.repeater.navigator;

import com.repeater.IQuickView;
import com.repeater.ReUse;
import org.apache.log4j.Logger;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.lang.Args;
import org.apache.wicket.util.string.Strings;

import java.util.ArrayList;
import java.util.List;

/**
 * base navigator which is inherited by {@link  AjaxItemsNavigator}
 *
 * @author Vineet Semwal
 */
public abstract class ItemsNavigatorBase extends Panel {

    private IQuickView repeater;

    public IQuickView getRepeater() {
        return repeater;
    }

    private static Logger logger = Logger.getLogger(ItemsNavigatorBase.class);

    private String cssClass;

    public void setCssClass(String cssClass) {
        this.cssClass = cssClass;
    }

    public String getCssClass() {
        return cssClass;
    }

    private Component more;


    public ItemsNavigatorBase(String id, IModel model, IQuickView repeater) {
        super(id, model);
        Args.notNull(repeater, "repeater");
        setOutputMarkupPlaceholderTag(true);
        this.repeater = repeater;
    }

    private void parentNotNull(IQuickView repeater) {
        if (repeater.getParent() == null) {
            throw new RepeatNotAddedToParentException("parent is null , add quickview to a markupcontainer");
        }
    }

    private void outPutMarkupIdNotTrue(IQuickView repeater) {
        MarkupContainer container = repeater.getParent();
        if (container.getOutputMarkupId() == false && container.getOutputMarkupPlaceholderTag() == false) {
            throw new OutputMarkupIdNotTrueException("parent doesn't have setOutputMarkupId to true");
        }
    }

    private void reuseStategyNotSupported(IQuickView repeater) {
        if (ReUse.DEFAULT_PAGING == repeater.getReuse()) {
            throw new ReuseStrategyNotSupportedException(ReUse.DEFAULT_PAGING + "this reuse stategy is not supported for rowsnavigator ");
        }
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        add(more = newMore());
        if (!Strings.isEmpty(getCssClass())) {
            AttributeModifier cssClassModifier = new AttributeModifier("class ", getCssClass());
            more.add(cssClassModifier);
        }
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();
        parentNotNull(repeater);
        outPutMarkupIdNotTrue(repeater);
        reuseStategyNotSupported(repeater);
    }


    public abstract Component newMore();

    public Component getMore() {
        return more;
    }


    /**
     * on a stateful event say onclick ,this method creates new elements from the newindex
     *
     * @return list of items created
     */
    public List<Item> onStatefulEvent() {
        AjaxRequestTarget target = getAjaxRequestTarget();
        List<Item> list = new ArrayList<Item>();
        long current = getRepeater().getCurrentPage();

         // page for which new items have to created

        long next = current + 1;
        if (next < getRepeater().getPageCount()) {
            list = getRepeater().addComponentsForPage(next);
            getRepeater().setCurrentPage(next);
        }
        target.add(getMore());
        return list;
    }

    public AjaxRequestTarget getAjaxRequestTarget() {
        return getRequestCycle().find(AjaxRequestTarget.class);
    }


    public static class RepeatNotAddedToParentException extends RuntimeException {
        public RepeatNotAddedToParentException(String message) {
            super(message);
        }
    }

    public static class OutputMarkupIdNotTrueException extends RuntimeException {
        public OutputMarkupIdNotTrueException(String message) {
            super(message);
        }
    }

    public static class ReuseStrategyNotSupportedException extends RuntimeException {
        public ReuseStrategyNotSupportedException(String message) {
            super(message);
        }
    }

}
