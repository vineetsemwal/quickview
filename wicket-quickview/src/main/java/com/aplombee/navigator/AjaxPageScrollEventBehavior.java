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
package com.aplombee.navigator;

import com.aplombee.IRepeaterUtil;
import com.aplombee.RepeaterUtil;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;

/**
 * behavior that can be attached to page ,on page scroll event will be fired if navigation-bar
 * is moved to the the bottom of page.
 *
 * <strong>you need to call {@link com.aplombee.IQuickView#addItemsForNextPage()} when you implement {@link this#onEvent(org.apache.wicket.ajax.AjaxRequestTarget)}</strong>
 *
 *
 */
public abstract class AjaxPageScrollEventBehavior extends AjaxEventBehavior {
    protected IRepeaterUtil getRepeaterUtil(){
        return RepeaterUtil.get();
    }
    public AjaxPageScrollEventBehavior(){
       super("scroll");
    }


    @Override
    protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
        super.updateAjaxAttributes(attributes);
         attributes.getAjaxCallListeners().add(new PageScrollListener());
        }


    public static class PageScrollListener extends AjaxCallListener {
        @Override
        public CharSequence getPrecondition(Component component) {
            super.getPrecondition(component);
            String call=RepeaterUtil.get().isPageScrollBarAtBottom();
            return "return "+ call;
        }
    }


}

