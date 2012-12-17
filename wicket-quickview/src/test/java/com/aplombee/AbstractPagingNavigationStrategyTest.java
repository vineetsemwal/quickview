package com.aplombee;

import org.apache.wicket.markup.repeater.IItemFactory;
import org.mockito.Mockito;
import org.testng.Assert;

import java.util.Iterator;

/**
 * @author Vineet Semwal
 */
public class AbstractPagingNavigationStrategyTest {

    public void assertIsAddItemsSupported(IQuickReuseStrategy strategy){
       Assert.assertFalse(strategy.isAddItemsSupported());
    }

   public void assertAddItems(IQuickReuseStrategy strategy){
        Iterator newModels= Mockito.mock(Iterator.class);
        IItemFactory factory=Mockito.mock(IItemFactory.class);
        int startIndex=345;
        strategy.addItems(startIndex,factory,newModels);
    }

    public void assertZeroPageCreatedOnReRender(IQuickReuseStrategy strategy){
        Assert.assertFalse(strategy.isAlwaysZeroPageCreatedOnReRender());
    }
}
