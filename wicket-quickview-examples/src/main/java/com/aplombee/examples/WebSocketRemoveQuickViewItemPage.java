/**
 * Copyright 2012 Vineet Semwal
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.aplombee.examples;

import com.aplombee.QuickView;
import com.aplombee.ReuseAllStrategy;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.protocol.ws.api.IWebSocketRequestHandler;
import org.apache.wicket.protocol.ws.api.WebSocketBehavior;
import org.apache.wicket.protocol.ws.api.WebSocketRequestHandler;
import org.apache.wicket.protocol.ws.api.message.ConnectedMessage;
import org.apache.wicket.protocol.ws.api.message.IWebSocketPushMessage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Vineet Semwal
 */
public class WebSocketRemoveQuickViewItemPage extends WebPage {
    private List<Integer> list = new ArrayList<>();

    public WebSocketRemoveQuickViewItemPage() {

        for (int i = 0; i < 30; i++) {
            list.add(i);
        }
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        IDataProvider<Integer> data = new ListDataProvider<Integer>(list);
        WebMarkupContainer numbers = new WebMarkupContainer("numbers");   //parent for quickview
        numbers.setOutputMarkupId(true);  //needed for ajax
        Component start, end;
        numbers.add(start = new EmptyPanel("start").setOutputMarkupPlaceholderTag(true));
        numbers.add(end = new EmptyPanel("end").setOutputMarkupPlaceholderTag(true));

        final QuickView<Integer> number = new QuickView<Integer>("number", data, new ReuseAllStrategy(), start, end) {
            @Override
            protected void populate(Item<Integer> item) {
                item.add(new Label("display", item.getModel()));
            }
        };
        //
        //register request handler that quickview should be aware of
        //
        number.register(IWebSocketRequestHandler.class);
        numbers.add(number);
        add(numbers);

        add(new WebSocketBehavior() {
            @Override
            protected void onConnect(ConnectedMessage message) {
                super.onConnect(message);
                WicketApplication.get().addDecrementConnectMessage(message);

            }

            @Override
            protected void onPush(WebSocketRequestHandler handler, IWebSocketPushMessage message) {
                super.onPush(handler, message);
                if (message instanceof CounterMessage) {
                    CounterMessage counterMessage = (CounterMessage) message;
                    Integer counter=counterMessage.getCounter();
                    Component item=componentAssociatedWithModelObject(number,counter);
                    if(item!=null) {
                        number.remove(item);
                        list.remove(counter);
                    }
                }
            }
        });


    }

    public Component componentAssociatedWithModelObject(final QuickView<Integer> quickView,final int modelObject){
       Iterator<Component>iterator= quickView.getItems();
       while (iterator.hasNext()){
           Component item= iterator.next();
           if(item.getDefaultModelObject().equals(modelObject)){
               return item;
           }

       }
       return null;
    }

}
