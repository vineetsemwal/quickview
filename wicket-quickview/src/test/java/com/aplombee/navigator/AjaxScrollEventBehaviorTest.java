package com.aplombee.navigator;

import com.aplombee.IQuickView;
import junit.framework.Assert;
import org.apache.wicket.ajax.AjaxRequestHandler;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.mock.MockApplication;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.util.tester.WicketTester;
import org.mockito.Mockito;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * @author Vineet Semwal
 */
public class AjaxScrollEventBehaviorTest {
    WicketTester tester;

    @BeforeTest
    void setup() {
        tester = new WicketTester(createMockApplication());
    }
    private static WebApplication createMockApplication() {
        WebApplication app = new MockApplication();
        return app;
    }
    @Test(groups = {"wicketTests"})
    public void constructor(){
        AjaxScrollEventBehaviorBase behavior=new AjaxScrollEventBehaviorBase() {
            @Override
            protected void onScroll(AjaxRequestTarget target) {
                         }
        };
        Assert.assertEquals(behavior.getEvent(),"scroll");
    }

    @Test(groups = {"wicketTests"})
   public void onScroll(){
        WebMarkupContainer container=new WebMarkupContainer("id");
        AjaxScrollEventBehaviorBase behavior=new AjaxScrollEventBehaviorBase() {
           @Override
           protected void onScroll(AjaxRequestTarget target) {}
       };
        AjaxScrollEventBehaviorBase spy=Mockito.spy(behavior);

        container.add(spy);
        tester.startComponentInPage(container);
        tester.executeAjaxEvent(container,"scroll");
        Mockito.verify(spy,Mockito.times(1)).onScroll(Mockito.any(AjaxRequestHandler.class));
   }

    @Test(groups = {"wicketTests"})
    public void addItemsForNextPage(){
        IQuickView quickView=Mockito.mock(IQuickView.class);
        AjaxScrollEventBehaviorBase behavior=new AjaxScrollEventBehaviorBase() {
            @Override
            protected void onScroll(AjaxRequestTarget target) {}
        };
        AjaxScrollEventBehaviorBase spy=Mockito.spy(behavior);
        spy.addItemsForNextPage(quickView);
         Mockito.verify(quickView,Mockito.times(1)).addItemsForNextPage();
    }

}
