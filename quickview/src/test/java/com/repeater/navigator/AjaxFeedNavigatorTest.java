
package com.repeater.navigator;

import com.repeater.IQuickView;
import com.repeater.QuickView;
import com.repeater.QuickViewBase;
import com.repeater.ReUse;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.mock.MockApplication;
import org.apache.wicket.model.IModel;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.util.tester.WicketTester;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.util.List;

import static org.mockito.Mockito.mock;

/**
 *
 * @author Vineet Semwal
 *
 */
public class AjaxFeedNavigatorTest {

    WicketTester tester;

           void setup() {
        tester = new WicketTester(createMockApplication());
    }

    private static WebApplication createMockApplication() {
        WebApplication app = new MockApplication();
        return app;
    }

    @Test(groups = {"wicketTests"})
    public void constructor_1(){
        IQuickView repeater=Mockito.mock(IQuickView.class);
        final AjaxRequestTarget target=Mockito.mock(AjaxRequestTarget.class);
        final List<Item> items=Mockito.mock(List.class);
        AjaxRowsNavigator navigator=new AjaxRowsNavigator("nav",repeater);
           Assert.assertEquals(navigator.getRepeater(),repeater);
    }

    @Test(groups = {"wicketTests"},expectedExceptions = RuntimeException.class)
    public void constructor_2(){
        IQuickView repeater=Mockito.mock(IQuickView.class);
        final AjaxRequestTarget target=Mockito.mock(AjaxRequestTarget.class);
        final List<Item> items=Mockito.mock(List.class);

        boolean isException=false;

        AjaxRowsNavigator navigator=new AjaxRowsNavigator("nav",null);

    }

    /**
     * when current page< pages count
     */

   @Test(groups = {"wicketTests"})
    public void OnStatefulEvent_1() {
        final String id = "id";
        final int repeaterSize = 2;
        final int dataProviderSize = 12;
       final long current=5,next=6 ,pages=7;
       IQuickView repeater=Mockito.mock(IQuickView.class);
       Mockito.when(repeater.getCurrentPage()).thenReturn(current);
       Mockito.when(repeater.getPageCount()).thenReturn(pages);
       final AjaxRequestTarget target=Mockito.mock(AjaxRequestTarget.class);
       final List<Item> items=Mockito.mock(List.class);
        AjaxRowsNavigator navigator=new AjaxRowsNavigator("nav",repeater){
            @Override
            public AjaxRequestTarget getAjaxRequestTarget() {
             return target;
            }
        };
       AjaxRowsNavigator spy=Mockito.spy(navigator);
       List<Item>actual= navigator.onStatefulEvent();
       Mockito.verify(repeater,Mockito.times(1)).setCurrentPage(next);
       Mockito.verify(repeater,Mockito.times(1)).addComponentsForPage(next);
       Mockito.verify(target,Mockito.times(1)).add(navigator.getMore());
       Assert.assertEquals(actual,items);
    }

    /**
     * when current page= pages count
     *
     */
    @Test(groups = {"wicketTests"})
    public void OnStatefulEvent_2() {
        final String id = "id";
        final int repeaterSize = 2;
        final int dataProviderSize = 12;
        final long current=5,next=6 ,pages=6;

        IQuickView repeater=Mockito.mock(IQuickView.class);
        Mockito.when(repeater.getCurrentPage()).thenReturn(current);
        Mockito.when(repeater.getPageCount()).thenReturn(pages);
        final AjaxRequestTarget target=Mockito.mock(AjaxRequestTarget.class);
        final List<Item> items=Mockito.mock(List.class);
        AjaxRowsNavigator navigator=new AjaxRowsNavigator("nav",repeater){
            @Override
            public AjaxRequestTarget getAjaxRequestTarget() {
                return target;
            }
        };
        AjaxRowsNavigator spy=Mockito.spy(navigator);
        List<Item>actual= navigator.onStatefulEvent();
        Mockito.verify(repeater,Mockito.never()).setCurrentPage(next);
        Mockito.verify(repeater,Mockito.never()).addComponentsForPage(next);
        Mockito.verify(target,Mockito.times(1)).add(navigator.getMore());
        Assert.assertTrue(actual.isEmpty());
    }

    /**
     * when current page> pages count
     *
     */
    @Test(groups = {"wicketTests"})
    public void OnStatefulEvent_3() {
        final String id = "id";
        final int repeaterSize = 2;
        final int dataProviderSize = 12;
        final long current=7,next=6 ,pages=6;

        IQuickView repeater=Mockito.mock(IQuickView.class);
        Mockito.when(repeater.getCurrentPage()).thenReturn(current);
        Mockito.when(repeater.getPageCount()).thenReturn(pages);
        final AjaxRequestTarget target=Mockito.mock(AjaxRequestTarget.class);
        final List<Item> items=Mockito.mock(List.class);
        AjaxRowsNavigator navigator=new AjaxRowsNavigator("nav",repeater){
            @Override
            public AjaxRequestTarget getAjaxRequestTarget() {
                return target;
            }
        };
        AjaxRowsNavigator spy=Mockito.spy(navigator);
        List<Item>actual= navigator.onStatefulEvent();
        Mockito.verify(repeater,Mockito.never()).setCurrentPage(next);
        Mockito.verify(repeater,Mockito.never()).addComponentsForPage(next);
        Mockito.verify(target,Mockito.times(1)).add(navigator.getMore());
        Assert.assertTrue(actual.isEmpty());
    }

    /**
     * current page=2 pages=4
     */

   @Test(groups = {"wicketTests"})
    public void onConfigure_1() {
        final String id = "id";
        final long currentpage = 2,pages = 4;
         RowsNavigatorBase navigator = Mockito.mock(RowsNavigatorBase.class);
       IQuickView repeater = mock(IQuickView.class);
       Mockito.when(repeater.getCurrentPage()).thenReturn(currentpage);
       Mockito.when(repeater.getPageCount()).thenReturn(pages) ;
       Mockito.when(navigator.getRepeater()).thenReturn(repeater);
       IModel model=Mockito.mock(IModel.class);
        MoreLabel more = new MoreLabel(id,model, navigator);
        more.onConfigure();
        Assert.assertTrue(more.isVisible());
    }

    /**
     * current page=3 pages=4
     */

    @Test(groups = {"wicketTests"})
    public void onConfigure_2() {
        final String id = "id";
        final long currentpage = 3,pages = 4;
        RowsNavigatorBase navigator = Mockito.mock(RowsNavigatorBase.class);
        IQuickView repeater = mock(IQuickView.class);
        Mockito.when(repeater.getCurrentPage()).thenReturn(currentpage);
        Mockito.when(repeater.getPageCount()).thenReturn(pages) ;
        Mockito.when(navigator.getRepeater()).thenReturn(repeater);
        IModel model=Mockito.mock(IModel.class);
        MoreLabel more = new MoreLabel(id,model, navigator);
        more.onConfigure();
        Assert.assertFalse(more.isVisible());
    }

    /**
     * current page=4 pages=4
     */

    @Test(groups = {"wicketTests"})
    public void onConfigure_3() {
        final String id = "id";
        final long currentpage = 4,pages = 4;
        RowsNavigatorBase navigator = Mockito.mock(RowsNavigatorBase.class);
        IQuickView repeater = mock(IQuickView.class);
        Mockito.when(repeater.getCurrentPage()).thenReturn(currentpage);
        Mockito.when(repeater.getPageCount()).thenReturn(pages) ;
        Mockito.when(navigator.getRepeater()).thenReturn(repeater);
        IModel model=Mockito.mock(IModel.class);
        MoreLabel more = new MoreLabel(id,model, navigator);
        more.onConfigure();
        Assert.assertFalse(more.isVisible());
    }
    /**
     * current page=5 pages=4
     */

    @Test(groups = {"wicketTests"})
    public void onConfigure_4() {
        final String id = "id";
        final long currentpage = 5,pages = 4;
        RowsNavigatorBase navigator = Mockito.mock(RowsNavigatorBase.class);
        IQuickView repeater = mock(IQuickView.class);
        Mockito.when(repeater.getCurrentPage()).thenReturn(currentpage);
        Mockito.when(repeater.getPageCount()).thenReturn(pages) ;
        Mockito.when(navigator.getRepeater()).thenReturn(repeater);
        IModel model=Mockito.mock(IModel.class);
        MoreLabel more = new MoreLabel(id,model, navigator);
        more.onConfigure();
        Assert.assertFalse(more.isVisible());
    }

    @Test(groups = {"wicketTests"})
    public void renderHead_1(){
        IQuickView repeater = mock(IQuickView.class);
       AjaxRowsNavigator navigator=new AjaxRowsNavigator("id",repeater);
        IHeaderResponse response=Mockito.mock(IHeaderResponse.class);
        navigator.renderHead(response);
        Mockito.verify(response,Mockito.times(1)).render(CssHeaderItem.forReference(NavigatorCssReference.get()));
    }

    /**
     *  parent is null
     */

    @Test(groups = {"wicketTests"},expectedExceptions =RowsNavigatorBase.RepeatNotAddedToParentException.class)
    public void  onBeforeRender_1(){
        IDataProvider data=Mockito.mock(IDataProvider.class);
        QuickViewBase repeater = new QuickView("id",data,10) {
            @Override
            protected void populate(Item item) { }
        };
        AjaxRowsNavigator navigator=new AjaxRowsNavigator("id",repeater);
        navigator.onBeforeRender();
       }


    /**
     *  parent not null ,outputmarkupid not set to true
     * @Test(groups = {"wicketTests"}),outmarkupid not true
     */
    @Test(groups = {"wicketTests"},expectedExceptions = RowsNavigatorBase.OutputMarkupIdNotTrueException.class)
    public void onBeforeRender_2(){
        WebMarkupContainer parent=new WebMarkupContainer("parent");
        IDataProvider data=Mockito.mock(IDataProvider.class);
        QuickViewBase repeater = new QuickView("id",data,10) {
            @Override
            protected void populate(Item item) { }
        };
        parent.add(repeater);
        AjaxRowsNavigator navigator=new AjaxRowsNavigator("id",repeater);
        boolean isException=false;
       navigator.onBeforeRender();
    }

    /**
     *
     *      parent not null ,reusestrategy=  ReUse.DEFAULT_PAGING
     * reuse stategy not correct exception
     */
    @Test(groups = {"wicketTests"},expectedExceptions = RowsNavigatorBase.ReuseStrategyNotSupportedException.class)
    public void onBeforeRender_3(){
        WebMarkupContainer parent=new WebMarkupContainer("parent");
        IDataProvider data=Mockito.mock(IDataProvider.class);
        QuickViewBase repeater = new QuickView("id",data,10) {
            @Override
            protected void populate(Item item) { }
        };
        repeater.setReuse(ReUse.DEFAULT_PAGING);
        parent.add(repeater);
        parent.setOutputMarkupId(true);

        AjaxRowsNavigator navigator=new AjaxRowsNavigator("id",repeater);
      navigator.onBeforeRender();

    }

    /**
     *  parent not null ,outputmarkupid set to true
     * reuse stategy is correct
     */
    @Test(groups = {"wicketTests"})
    public void onBeforeRender_4(){
        WebMarkupContainer parent=new WebMarkupContainer("parent");
        IDataProvider data=Mockito.mock(IDataProvider.class);
        QuickViewBase repeater = new QuickView("id",data,10) {
            @Override
            protected void populate(Item item) { }
        };
        repeater.setReuse(ReUse.DEFAULT_ROWSNAVIGATOR);
        parent.add(repeater);
        parent.setOutputMarkupId(true);

        AjaxRowsNavigator navigator=new AjaxRowsNavigator("id",repeater);
        navigator.onBeforeRender();

    }

    /**
     *  parent not null ,OutputMarkupPlaceholderTag set to true
     * reuse stategy is correct
     */
    @Test(groups = {"wicketTests"})
    public void onBeforeRender_5(){
        WebMarkupContainer parent=new WebMarkupContainer("parent");
        IDataProvider data=Mockito.mock(IDataProvider.class);
        QuickViewBase repeater = new QuickView("id",data,10) {
            @Override
            protected void populate(Item item) { }
        };
        repeater.setReuse(ReUse.DEFAULT_ROWSNAVIGATOR);
        parent.add(repeater);
        parent.setOutputMarkupPlaceholderTag(true);

        AjaxRowsNavigator navigator=new AjaxRowsNavigator("id",repeater);
        navigator.onBeforeRender();
    }



}
