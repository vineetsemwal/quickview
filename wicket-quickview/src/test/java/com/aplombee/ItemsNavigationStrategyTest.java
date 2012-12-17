package com.aplombee;

import org.apache.wicket.markup.repeater.IItemFactory;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.model.IModel;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *  @author Vineet Semwal
 */
public class ItemsNavigationStrategyTest extends AbstractItemsNavigationStrategyTest{

    @Test(groups = {"wicketTests"} )
    public void addItems_1(){
        super.assertAddItems(new ItemsNavigationStrategy());
    }

    @Test(groups = {"wicketTests"})
    public void isPaging_1(){
       super.assertIsAddItemsSupported(new ItemsNavigationStrategy());
    }


    @Test(groups = {"wicketTests"} )
    public void getItems_1(){
     IQuickReuseStrategy strategy=new ItemsNavigationStrategy();
        List<Integer>list=new ArrayList<Integer>();
        list.add(45);
        list.add(76);
        IDataProvider<Integer>data=new ListDataProvider<Integer>(list);
        int itemsPerRequest=5;

        IItemFactory factory=Mockito.mock(IItemFactory.class);
        final int index=0;
        final int index2=1;
        IModel<Integer>model1=data.model(list.get(0));
        IModel<Integer>model2=data.model(list.get(1));
        Item item1=new Item("0",0,model1) ;
        Mockito.when(factory.newItem(0,model1)).thenReturn(item1);
        Item item2=new Item("1",index2,model2);
        Mockito.when(factory.newItem(index2,model2)).thenReturn(item2);
        List<IModel<Integer>>newModels=new ArrayList<IModel<Integer>>();
        newModels.add(model1);
        newModels.add(model2);

      Iterator<Item<Integer>>actual=  strategy.getItems(data,itemsPerRequest,factory,newModels.iterator(),null);
      Assert.assertEquals(actual.next(),item1);
      Assert.assertEquals(actual.next(),item2);

    }


    }
