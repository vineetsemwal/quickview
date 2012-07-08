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

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Vineet Semwal
 *
 */
public class TestPanel extends Panel {

    public TestPanel(String id){
        super(id);
    }
   private  QuickView quickView ;
   public QuickView getQuickView() {
        return quickView;
    }


    @Override
    protected void onInitialize() {
        super.onInitialize();
        List<Integer> list=new ArrayList<Integer>() ;
        list.add(1);
        list.add(2);
        IDataProvider<Integer>data= new ListDataProvider<Integer>(list);

        quickView=new QuickView("quick",data,ReUse.DEFAULT_ROWSNAVIGATOR) {
            @Override
            protected void populate(Item item) {
            }
        } ;
       WebMarkupContainer  parent=new WebMarkupContainer("parent");
        parent.setOutputMarkupId(true);
        parent.add(quickView);
        add(parent);

    }
}
