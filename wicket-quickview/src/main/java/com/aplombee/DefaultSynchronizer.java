package com.aplombee;


import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.core.request.handler.IPartialPageRequestHandler;

import java.util.Map;

/**
 * Synchronizer basically adds components(repeater's items) and scripts to the the AjaxRequestTarget after
 * checking parent is not added to AjaxRequestTarget .If parent is added scripts and
 * items are not added to the AjaxRequestTarget
 *
 * @author Vineet Semwal
 */
public class DefaultSynchronizer extends Synchronizer implements AjaxRequestTarget.IListener {


    public DefaultSynchronizer(final MarkupContainer parent,
                               final IPartialPageRequestHandler requestHandler){
        super(parent, requestHandler);
    }

    @Override
    public void onBeforeRespond(final Map<String, Component> map, final AjaxRequestTarget target) {
       submit();
    }

    @Override
    public void onAfterRespond(Map<String, Component> map, AjaxRequestTarget.IJavaScriptResponse response) {
    }

    @Override
    public void updateAjaxAttributes(AbstractDefaultAjaxBehavior behavior, AjaxRequestAttributes attributes) {
    }


}