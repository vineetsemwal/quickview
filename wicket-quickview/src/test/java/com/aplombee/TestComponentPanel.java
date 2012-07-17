package com.aplombee;

import com.aplombee.navigator.AjaxItemsNavigator;
import com.aplombee.navigator.ItemsNavigatorBase;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;

import java.util.Iterator;

/**
 * @author Vineet Semwal
 *
 */
public abstract class TestComponentPanel extends Panel {
    public static final String quickViewId="quickview",parentId="parent",ajaxLinkId="link",navigatorId="navigator";
    public TestComponentPanel(String id){
        super(id);
        setOutputMarkupId(true);
    }
    public abstract Parent newParent();
    private Parent parent;
    private AbstractLink link;
    private ItemsNavigatorBase navigator;
    public AbstractLink newLink(){
        Link link=new Link(ajaxLinkId) {
            @Override
            public void onClick() {
            }
        };
        return link;
    }

    public Parent getQuicKViewParent() {
        return parent;
    }

    public AbstractLink getLink() {
        return link;
    }

    public ItemsNavigatorBase getNavigator() {
        return navigator;
    }

    public ItemsNavigatorBase newNavigator(){
            AjaxItemsNavigator navigator=new AjaxItemsNavigator(navigatorId,parent.getChild());
        return navigator;
    }
    @Override
    protected void onInitialize() {
        super.onInitialize();
    add(parent=newParent());
    add(link = newLink());
    add(navigator=newNavigator());
    }

    public static class Parent extends WebMarkupContainer{
        public Parent(String id){
               super(id);
           }
        public QuickView getChild(){
            Iterator<Component> it=iterator();
            Component child=it.next();
            if(child instanceof  QuickView)
            {QuickView quickView= (QuickView)child;
            return quickView;
            }
            throw new RuntimeException("quickview not found as first child");
        }

        @Override
        protected void onBeforeRender() {
            super.onBeforeRender();
                 getChild();
        }
    }

}
