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

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Vineet Semwal
 *
 */
public class AjaxLinkPage extends WebPage {
    private List<Integer> list=new ArrayList<Integer>();
    public AjaxLinkPage(){
        for(int i=0;i<5;i++){
            list.add(i)  ;
        }
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        IDataProvider<Integer>data= new ListDataProvider<Integer>(list);
        WebMarkupContainer numbers=new WebMarkupContainer("numbers");   //parent for quickview
        numbers.setOutputMarkupId(true);  //needed for ajax
        final QuickView number=new QuickView("number",data,ReUse.DEFAULT_ROWSNAVIGATOR) {
            @Override
            protected void populate(Item item) {
                item.add(new Label("display",item.getModel()));
            }
        };
        numbers.add(number);
        add(numbers);

        AjaxLink addLink=new AjaxLink("addLink") {
            private boolean  clicked=false;

            @Override
            protected void onConfigure() {
                super.onConfigure();
                setVisible(!clicked);
            }

            @Override
            public void onClick(AjaxRequestTarget target) {
                list.add(100);
                Item<Integer>item=  number.buildCompleteItem(number.newChildId(), 100);
                  number.add(item);  //just enough to create a new row

              // no need to add quickview parent to ajaxrequesttarget,it will make the whole repeater to render again!
                clicked=true;
                target.add(this);
            }

        } ;
        addLink.setOutputMarkupPlaceholderTag(true);
        add(addLink);
    }
}
