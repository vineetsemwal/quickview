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

import org.apache.log4j.Logger;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.repeater.IQuickView;


/**
 * ajax items navigator which enables {@link com.repeater.QuickView} to create and draw new children
 *
 *  <strong>add quickview to markupcontainer to use with rowsnavigator and markupcontainer should setoutputmarkupid
 *  or setgetOutputMarkupPlaceholderTag to true</strong>
 *
 * @author Vineet Semwal
 *
 */
public class AjaxItemsNavigator extends ItemsNavigatorBase {

    private static Logger logger = Logger.getLogger(AjaxItemsNavigator.class);

    /**
     *  this navigator onclick creates new items for the quickview
     *
     * @param id
     * @param repeater
     */
    public AjaxItemsNavigator(String id, IQuickView repeater) {
        this(id, new Model("more"), repeater);
    }

    /**
     * this navigator onclick creates new items for the quickview
     *
     * @param id      navigator id
     * @param model   model is passed to the link, default value is "more"
     * @param repeater
     */
    public AjaxItemsNavigator(String id, IModel model, IQuickView repeater) {
        super(id,model, repeater);
    }

    @Override
    public MoreLabel newMore() {
        return new MoreLabel("moreLabel",getDefaultModel(),this);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
      }


    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference(NavigatorCssReference.get());
    }

}
