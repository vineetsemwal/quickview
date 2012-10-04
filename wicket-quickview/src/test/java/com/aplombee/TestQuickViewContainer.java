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
public abstract class TestQuickViewContainer extends WebMarkupContainer  implements IMarkupResourceStreamProvider {
    public static final String quickViewId="quickview",parentId="parent",ajaxLinkId="link",navigatorId="navigator";
    private AbstractLink link;
    private QuickViewParent parent ;
    private ItemsNavigatorBase navigator;

    public ItemsNavigatorBase getNavigator() {
        return navigator;
    }

    public QuickViewParent getQuickViewParent() {
        return parent;
    }

    public TestQuickViewContainer(String id){
        super(id);
    }

    public AbstractLink getLink() {
        return link;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        add(parent=newParent());
        add(navigator=newNavigator());
        add(link=newLink()) ;
    }
    public abstract AbstractLink newLink();
    public abstract QuickViewParent newParent();
    public ItemsNavigatorBase newNavigator(){
        AjaxItemsNavigator navigator=new AjaxItemsNavigator(navigatorId,parent.getChild());
        return navigator;
    }

    @Override
    protected IMarkupSourcingStrategy newMarkupSourcingStrategy()
    {
        return new PanelMarkupSourcingStrategy(false);
    }


    @Override
    public IResourceStream getMarkupResourceStream(MarkupContainer container, Class<?> containerClass)
    {
        return new StringResourceStream("<wicket:panel> " +
                "<div wicket:id=\""+ getQuickViewParent().getId()+ "\">" +

                "<div wicket:id=\""+getQuickViewParent().getChild().getId()+"\"> </div>" +

                "</div>"  +

                " <div wicket:id=\""+ getNavigator().getId()+"\"></div>" +

                "<a wicket:id= \""+ getLink().getId()  +"\" > </a>"+



                "</wicket:panel>"

        );
    }

}
