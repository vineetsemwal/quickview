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

import com.aplombee.RepeaterUtil;
import org.apache.wicket.MarkupContainer;

/**
 * behavior that can be attached to quickview's parent ,on scroll event will be fired if scroll-bar
 * is moved to the bottom,for this to happen,you must specify the parent to have scroll in css by defining overflow-y property.
 *
 *<strong>you need to call {@link this#addItemsForNextPage()} when you implement {@link this#onScroll(org.apache.wicket.ajax.AjaxRequestTarget)}</strong>
 *
 *
 *  @author Vineet Semwal
 *
 */
public abstract class AjaxComponentScrollEventBehavior extends AjaxScrollEventBehaviorBase {


        public CharSequence getPreconditionScript() {
            super.getPreconditionScript();
             String call=RepeaterUtil.get().isComponentScrollBarAtBottom((MarkupContainer) getComponent());
            return "return "+ call +";";
            }
}
