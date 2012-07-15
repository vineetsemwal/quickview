package com.repeater.examples;

import com.repeater.QuickView;
import com.repeater.ReUse;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.ListDataProvider;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Vineet Semwal
 *
 */
public class RemoveItemsPage extends WebPage {
    private List<Integer> list=new ArrayList<Integer>();
    private QuickView<Integer> quickView;
   public RemoveItemsPage(){
    for (int i = 1; i < 5; i++) {
        list.add(i);
    }
}

    @Override
    protected void onInitialize() {
        super.onInitialize();
        ListDataProvider<Integer>data=new ListDataProvider<Integer>(list);
        WebMarkupContainer numbers = new WebMarkupContainer("numbers");   //parent for quickview
        numbers.setOutputMarkupId(true);  //needed for ajax
         quickView=new QuickView<Integer>("number",data, ReUse.DEFAULT_ITEMSNAVIGATION) {
           @Override
                protected void populate(final Item<Integer> item) {
                 item.add(new Label("display", item.getModel()));
               AjaxLink remove=new AjaxLink("remove") {
                   private boolean clicked = false;

                   @Override
                   protected void onConfigure() {
                       super.onConfigure();
                       setVisible(!clicked);
                   }

                   @Override
                   public void onClick(AjaxRequestTarget target) {
                       list.remove(item.getModelObject()) ;
                       quickView.remove(item);//just enough to remove item
                       clicked=false;
                       target.add(this);
                   }
               } ;
               remove.setOutputMarkupId(true);
               item.add(remove);

                }

        }  ;
       numbers.add(quickView);
        add(numbers);
    }
}

