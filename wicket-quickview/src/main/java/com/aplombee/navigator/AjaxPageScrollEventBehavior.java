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
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.IHeaderResponse;

/**
 * behavior that can be attached to page ,on page scroll event will be fired if scroll-bar
 * is moved to the the bottom of page.
 *
 * <strong>you need to call {@link this#addItemsForNextPage()} when you implement {@link this#onScroll(org.apache.wicket.ajax.AjaxRequestTarget)}</strong>
 *
 *
 * @author Vineet Semwal
 */
public abstract class AjaxPageScrollEventBehavior extends AjaxScrollEventBehaviorBase{


    protected String newScrollScript(){
       String call="wicketAjaxGet(\"" +getCallbackUrl() +"\",function() { },"
           + "function() {} ,function() {"+getPreconditionScript() +" } )";
        String script="document.body.setAttribute(\"onscroll\",   ' "+ call +" '  ) ; ";

        return script;
    }

    @Override
    protected CharSequence getPreconditionScript() {
        String call= RepeaterUtil.get().isPageScrollBarAtBottom();
        return "return "+call+";";
    }


    @Override
    public void renderHead(Component component, IHeaderResponse response) {
        super.renderHead(component, response);
        response.renderOnDomReadyJavaScript(newScrollScript());
    }

}
