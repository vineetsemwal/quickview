package com.aplombee.navigator;

import com.aplombee.IQuickView;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.repeater.Item;

import java.util.List;

/**
 *  base ajax scroll event behavior which are extended by {@link AjaxPageScrollEventBehavior} and {@link AjaxComponentScrollEventBehavior}
 *
 *@author Vineet Semwal
 * .
 */
public abstract class AjaxScrollEventBehaviorBase extends AjaxEventBehavior{
    public AjaxScrollEventBehaviorBase(){
        super("scroll");
    }

    @Override
    protected final void onEvent(AjaxRequestTarget target) {
        onScroll(target);
    }

    /**
     * Listener method for the ajax scroll event
     * when you implement this method call {@link this#addItemsForNextPage(com.aplombee.IQuickView)} to create items for next page/request
     *
     * @param target
     *      the current request handler
     */
    protected abstract void onScroll(AjaxRequestTarget target);

    public List<Item> addItemsForNextPage(IQuickView quickView) {
        return quickView.addItemsForNextPage();
    }

}
