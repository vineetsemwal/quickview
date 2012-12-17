package com.aplombee;


import org.testng.annotations.Test;

/**
 *  @author Vineet Semwal
 */
public class QuickReuseIfModelsEqualStrategyTest extends AbstractPagingNavigationStrategyTest{

    @Test(groups = {"wicketTests"})
    public void isPaging_1(){
        super.assertIsAddItemsSupported(new QuickReuseIfModelsEqualStrategy());
    }

    @Test(groups = {"wicketTests"} ,expectedExceptions = IRepeaterUtil.ReuseStrategyNotSupportedException.class)
    public void addItems_1(){
       super.assertAddItems(new QuickReuseIfModelsEqualStrategy());
    }


}
