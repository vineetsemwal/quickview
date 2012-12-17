package com.aplombee;


import org.testng.annotations.Test;

/**
 *  @author Vineet Semwal
 */
public class QuickReuseIfModelsEqualStrategyTest extends AbstractPagingNavigationStrategyTest{

    @Test(groups = {"wicketTests"})
    public void isPaging_1(){
        super.assertIsPaging(new QuickReuseIfModelsEqualStrategy());
    }

    @Test(groups = {"wicketTests"} ,expectedExceptions = UnsupportedOperationException.class)
    public void addItems_1(){
       super.assertAddItems(new QuickReuseIfModelsEqualStrategy());
    }


}
