package com.aplombee;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;

import java.util.Iterator;

/**
 * quickview's safe parent for tests
 */

    public class QuickViewParent extends WebMarkupContainer {
        public QuickViewParent(String id){
            super(id);
        }
        public QuickViewBase getChild(){
            Iterator<Component> it=iterator();
            Component child=it.next();
            if(child instanceof  IQuickView)
            {QuickViewBase quickView= (QuickViewBase)child;
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

