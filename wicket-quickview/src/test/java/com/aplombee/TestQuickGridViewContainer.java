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
