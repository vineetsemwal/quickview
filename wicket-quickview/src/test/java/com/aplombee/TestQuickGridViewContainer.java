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
package com.aplombee;

import com.aplombee.navigator.AjaxItemsNavigator;
import com.aplombee.navigator.ItemsNavigatorBase;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.IMarkupResourceStreamProvider;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.panel.IMarkupSourcingStrategy;
import org.apache.wicket.markup.html.panel.PanelMarkupSourcingStrategy;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.StringResourceStream;

/**
 * @author Vineet Semwal
 */
public abstract class TestQuickGridViewContainer extends TestQuickViewContainer {
    public static final String quickViewId="quickview",parentId="parent",ajaxLinkId="link",navigatorId="navigator";

    public TestQuickGridViewContainer(String id){
        super(id);
    }

    @Override
    public IResourceStream getMarkupResourceStream(MarkupContainer container, Class<?> containerClass)
    {
        return new StringResourceStream("<wicket:panel> " +
                "<table wicket:id=\""+ getQuickViewParent().getId()+ "\">" +
                "<tr wicket:id=\""+getQuickViewParent().getChild().getId()+"\"> " +
                "<td wicket:id=\"cols\"> </td>"+
                "</tr>" +
                "</table>" +
                " <div wicket:id=\""+ getNavigator().getId()+"\"></div>" +
                "<a wicket:id= \""+ getLink().getId()  +"\" > </a>"+
                "</wicket:panel>"

        );
    }

}
