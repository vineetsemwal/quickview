/**
 *
 Copyright 2012 Vineet Semwal

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
package com.aplombee;

import com.aplombee.navigator.TestPanel2;
import junit.framework.Assert;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.mock.MockApplication;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.util.tester.WicketTester;
import org.mockito.Mockito;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vineet Semwal
 *
 */
public class RepeaterUtilTest {

    @Test(groups = {"utilTests"})
    public void insertBefore_1(){
        final String child="child" ,parent="parent",tag="div";
               String actual= RepeaterUtil.get().insertBefore(tag,child,parent);
        String expected="insertBefore('div','child','parent')";
        Assert.assertEquals(actual.trim(),expected.trim());
    }
    @Test(groups = {"utilTests"})
    public void insertBefore_2(){
        WicketTester tester=new WicketTester(createMockApplication()) ;
        TestPanel panel=new TestPanel("id");
        tester.startComponentInPage(panel);
        QuickView quick=   panel.getQuickView();
        ComponentTag tag= RepeaterUtil.get().getComponentTag(quick);
       String actual= RepeaterUtil.get().insertBefore(quick,quick.getParent());
       String expected=String.format("insertBefore('%s','%s','%s')",tag.getName(),quick.getMarkupId(),quick.getParent().getMarkupId());
         Assert.assertEquals(actual.trim(),expected.trim());
    }

    /**
     * check with testpanel
     */
    @Test(groups = {"utilTests"})
    public void getComponentTag_1(){
        WicketTester tester=new WicketTester(createMockApplication()) ;
        TestPanel panel=new TestPanel("id");
             tester.startComponentInPage(panel);
        QuickView quick=   panel.getQuickView();
         ComponentTag tag= RepeaterUtil.get().getComponentTag(quick);
         Assert.assertEquals(tag.getName(),"div");
    }
    /**
     * check with testpanel2
     */
    @Test(groups = {"utilTests"})
    public void getComponentTag_2(){
        WicketTester tester=new WicketTester(createMockApplication()) ;
        TestPanel2 panel=new TestPanel2("id");
        tester.startComponentInPage(panel);
        QuickView quick=   panel.getQuickView();
        ComponentTag tag= RepeaterUtil.get().getComponentTag(quick);
        Assert.assertEquals(tag.getName(),"li");
    }

    @Test(groups = {"utilTests"})
    public void insertAfter_1(){
        final String child="child" ,parent="parent",tag="div";
        String call= RepeaterUtil.get().insertAfter(tag, child, parent);
        String expected="insertAfter('div','child','parent')";
        Assert.assertEquals(call,expected);
    }
    @Test(groups = {"utilTests"})
    public void insertAfter_2(){
        WicketTester tester=new WicketTester(createMockApplication()) ;
        TestPanel panel=new TestPanel("id");
        tester.startComponentInPage(panel);
        QuickView quick=   panel.getQuickView();
        ComponentTag tag= RepeaterUtil.get().getComponentTag(quick);
        String actual= RepeaterUtil.get().insertAfter(quick, quick.getParent());
        String expected=String.format("insertAfter('%s','%s','%s')",tag.getName(),quick.getMarkupId(),quick.getParent().getMarkupId());
        Assert.assertEquals(actual.trim(),expected.trim());
    }

    @Test(groups = {"utilTests"})
    public void removeItem_1(){
      final String repeaterMarkupId="quick";
      final String expected="removeItem('quick')";
       final  String actual=RepeaterUtil.get().removeItem(repeaterMarkupId);
        Assert.assertEquals(actual.trim(),expected.trim());
    }

    @Test(groups = {"utilTests"})
    public void removeItem(){
        final String repeaterMarkupId="quick";
        Item item=Mockito.mock(Item.class);
        Mockito.when(item.getMarkupId()).thenReturn(repeaterMarkupId);
        final  String actual=RepeaterUtil.get().removeItem(item);
        final String expected="removeItem('quick')";
        Assert.assertEquals(actual.trim(),expected.trim());
    }

    /**
     * integer overflow
     */
    @Test(groups = {"utilTests"},expectedExceptions = IllegalArgumentException.class)
    public void safeLongToInt_1(){
        long val=  (long)Integer.MAX_VALUE+1l;
        RepeaterUtil.get().safeLongToInt(val);
    }

    /**
     * integer overflow
     */
    @Test(groups = {"utilTests"} ,expectedExceptions = IllegalArgumentException.class)
    public void safeLongToInt_2(){
        long val= (long) Integer.MIN_VALUE-(long)1;
      RepeaterUtil.get().safeLongToInt(val);
    }

    /**
     * integer doesn't overflow
     */
    @Test(groups = {"utilTests"})
    public void safeLongToInt_3(){
       int min= RepeaterUtil.get().safeLongToInt(Integer.MIN_VALUE);
       Assert.assertEquals(min,Integer.MIN_VALUE);
        int max=RepeaterUtil.get().safeLongToInt(Integer.MAX_VALUE);
        Assert.assertEquals(max,Integer.MAX_VALUE);
    }
    private static WebApplication createMockApplication() {
        WebApplication app = new MockApplication();
        return app;
    }

    @Test(groups = {"utilTests"},expectedExceptions = RepeaterUtil.ReuseStrategyNotSupportedException.class)
    public void reuseStategyNotSupportedForItemsNavigation_1(){
        IQuickView quickView=Mockito.mock(IQuickView.class);
        Mockito.when(quickView.getReuse()).thenReturn(ReUse.DEFAULT_PAGING);
           RepeaterUtil.get().reuseStategyNotSupportedForItemsNavigation(quickView);
    }
    @Test(groups = {"utilTests"},expectedExceptions = RepeaterUtil.ReuseStrategyNotSupportedException.class)
    public void reuseStategyNotSupportedForItemsNavigation_2(){
        IQuickView quickView=Mockito.mock(IQuickView.class);
        Mockito.when(quickView.getReuse()).thenReturn(ReUse.CURRENTPAGE);
         RepeaterUtil.get().reuseStategyNotSupportedForItemsNavigation(quickView);
    }

    @Test(groups = {"utilTests"},expectedExceptions = RepeaterUtil.ReuseNotInitializedException.class)
    public void reuseStategyNotSupportedForItemsNavigation_3(){
        IQuickView quickView=Mockito.mock(IQuickView.class);
        Mockito.when(quickView.getReuse()).thenReturn(ReUse.NOT_INITIALIZED);
        RepeaterUtil.get().reuseStategyNotSupportedForItemsNavigation(quickView);
    }

    @Test(groups = {"utilTests"},expectedExceptions = IllegalArgumentException.class)
    public void reuseStategyNotSupportedForItemsNavigation_4(){

        RepeaterUtil.get().reuseStategyNotSupportedForItemsNavigation(null);
    }

    @Test(groups = {"utilTests"})
    public void reuseStategyNotSupportedForItemsNavigation_5(){
         IQuickView quickView=Mockito.mock(IQuickView.class);
        Mockito.when(quickView.getReuse()).thenReturn(ReUse.DEFAULT_ITEMSNAVIGATION);

        RepeaterUtil.get().reuseStategyNotSupportedForItemsNavigation(quickView);
    }

    @Test(groups = {"utilTests"})
    public void reuseStategyNotSupportedForItemsNavigation_6(){
        IQuickView quickView=Mockito.mock(IQuickView.class);
        Mockito.when(quickView.getReuse()).thenReturn(ReUse.ALL);
        RepeaterUtil.get().reuseStategyNotSupportedForItemsNavigation(quickView);
    }

    @Test(groups = {"utilTests"},expectedExceptions = IllegalArgumentException.class)
    public void reuseNotInitialized_1(){

        RepeaterUtil.get().reuseNotInitialized(null);
    }

    @Test(groups = {"utilTests"},expectedExceptions = RepeaterUtil.ReuseNotInitializedException.class)
    public void reuseNotInitialized_2(){
    IDataProvider data=Mockito.mock(IDataProvider.class);
    QuickView quickView=new QuickView("id",data,ReUse.NOT_INITIALIZED) {
        @Override
        protected void populate(Item item) {
        }
    } ;
        RepeaterUtil.get().reuseNotInitialized(quickView);
    }

    @Test(groups = {"utilTests"},expectedExceptions = RepeaterUtil.OutputMarkupIdNotTrueException.class)
    public  void outPutMarkupIdNotTrue_1(){
        IDataProvider data=Mockito.mock(IDataProvider.class);
        QuickView quickView=new QuickView("id",data,ReUse.DEFAULT_ITEMSNAVIGATION) {
            @Override
            protected void populate(Item item) {
            }
        } ;
        WebMarkupContainer parent=new WebMarkupContainer("parent");
        parent.add(quickView);
        RepeaterUtil.get().outPutMarkupIdNotTrue(quickView);
    }

    @Test(groups = {"utilTests"})
    public  void outPutMarkupIdNotTrue_2(){
        IDataProvider data=Mockito.mock(IDataProvider.class);
        QuickView quickView=new QuickView("id",data,ReUse.DEFAULT_ITEMSNAVIGATION) {
            @Override
            protected void populate(Item item) {
            }
        } ;
        WebMarkupContainer parent=new WebMarkupContainer("parent");
        parent.add(quickView);
        parent.setOutputMarkupId(true);
        RepeaterUtil.get().outPutMarkupIdNotTrue(quickView);
    }
    @Test(groups = {"utilTests"})
    public  void outPutMarkupIdNotTrue_3(){
        IDataProvider data=Mockito.mock(IDataProvider.class);

        QuickView quickView=new QuickView("id",data,ReUse.DEFAULT_ITEMSNAVIGATION) {
            @Override
            protected void populate(Item item) {
            }
        } ;

        WebMarkupContainer parent=new WebMarkupContainer("parent");
        parent.add(quickView);
        parent.setOutputMarkupPlaceholderTag(true);
        RepeaterUtil.get().outPutMarkupIdNotTrue(quickView);
    }

    /**
     * parent=null,reuse= ReUse.CURRENTPAGE
     */
    @Test(groups = {"utilTests"})
    public  void parentNotSuitable_1(){
        IQuickView quickView=Mockito.mock(IQuickView.class);
        Mockito.when(quickView.getReuse()).thenReturn(ReUse.CURRENTPAGE);
        RepeaterUtil.get().parentNotSuitable(quickView);
    }

    /**
     * parent=null,reuse= ReUse.DEFAULT_PAGING
     */
    @Test(groups = {"utilTests"})
    public  void parentNotSuitable_2(){
        IQuickView quickView=Mockito.mock(IQuickView.class);
        Mockito.when(quickView.getReuse()).thenReturn(ReUse.DEFAULT_PAGING);
        RepeaterUtil.get().parentNotSuitable(quickView);
    }

    /**
     * parent=null,reuse= ReUse.DEFAULT_ITEMSNAVIGATION
     */
    @Test(groups = {"utilTests"},expectedExceptions = RepeaterUtil.QuickViewNotAddedToParentException.class)
    public  void parentNotSuitable_3(){
        IQuickView quickView=Mockito.mock(IQuickView.class);
        Mockito.when(quickView.getReuse()).thenReturn(ReUse.DEFAULT_ITEMSNAVIGATION);
        RepeaterUtil.get().parentNotSuitable(quickView);
    }

    /**
     * parent=page,reuse= ReUse.DEFAULT_ITEMSNAVIGATION
     */
    @Test(groups = {"utilTests"},expectedExceptions = RepeaterUtil.QuickViewNotAddedToParentException.class)
    public  void parentNotSuitable_4(){
        WebPage parent=Mockito.mock(WebPage.class);
        IQuickView quickView=Mockito.mock(IQuickView.class);
        Mockito.when(quickView.getParent()).thenReturn(parent);
        Mockito.when(quickView.getReuse()).thenReturn(ReUse.DEFAULT_ITEMSNAVIGATION);
        RepeaterUtil.get().parentNotSuitable(quickView);
    }

    /**
     * parent children size=2,reuse= ReUse.DEFAULT_ITEMSNAVIGATION
     */
    @Test(groups = {"utilTests"},expectedExceptions = RepeaterUtil.ParentNotUnaryException.class)
    public  void parentNotSuitable_5(){
        WebMarkupContainer parent=Mockito.mock(WebMarkupContainer.class);
        Mockito.when(parent.size()).thenReturn(2) ;
        IQuickView quickView=Mockito.mock(IQuickView.class);
        Mockito.when(quickView.getParent()).thenReturn(parent);
       Mockito.when(quickView.getReuse()).thenReturn(ReUse.DEFAULT_ITEMSNAVIGATION);
        RepeaterUtil.get().parentNotSuitable(quickView);
    }

    /**
     * parent children size=1,reuse= ReUse.DEFAULT_ITEMSNAVIGATION
     */
    @Test(groups = {"utilTests"})
    public  void parentNotSuitable_6(){
        WebMarkupContainer parent=Mockito.mock(WebMarkupContainer.class);
        Mockito.when(parent.size()).thenReturn(1) ;
        IQuickView quickView=Mockito.mock(IQuickView.class);
        Mockito.when(quickView.getParent()).thenReturn(parent);
        Mockito.when(quickView.getReuse()).thenReturn(ReUse.DEFAULT_ITEMSNAVIGATION);
        RepeaterUtil.get().parentNotSuitable(quickView);
    }

    /**
     * parent children size=2,reuse= ReUse.ALL
     */

    @Test(groups = {"utilTests"},expectedExceptions = RepeaterUtil.ParentNotUnaryException.class)
    public  void parentNotSuitable_7(){
        WebMarkupContainer parent=Mockito.mock(WebMarkupContainer.class);
        Mockito.when(parent.size()).thenReturn(2) ;
        IQuickView quickView=Mockito.mock(IQuickView.class);
        Mockito.when(quickView.getParent()).thenReturn(parent);
        Mockito.when(quickView.getReuse()).thenReturn(ReUse.ALL);
        RepeaterUtil.get().parentNotSuitable(quickView);
    }

    /**
     * parent children size=1,reuse= ReUse.ALL
     */

    @Test(groups = {"utilTests"})
    public  void parentNotSuitable_8(){
        WebMarkupContainer parent=Mockito.mock(WebMarkupContainer.class);
        Mockito.when(parent.size()).thenReturn(1) ;
        IQuickView quickView=Mockito.mock(IQuickView.class);
        Mockito.when(quickView.getParent()).thenReturn(parent);
        Mockito.when(quickView.getReuse()).thenReturn(ReUse.ALL);
        RepeaterUtil.get().parentNotSuitable(quickView);
    }

    @Test(groups = {"utilTests"})
    public void testScripts_1(){
        List<String> markupIdToComponent=new ArrayList<String>();
        markupIdToComponent.add("1");
        markupIdToComponent.add("2") ;
        List<String>prependJavaScripts=new ArrayList<String>();
        prependJavaScripts.add("prep1");
        prependJavaScripts.add("prep2");
        List<String>appendJavaScripts=new ArrayList<String>();
        appendJavaScripts.add("append1");
        appendJavaScripts.add("append2");
       final String input= constructAjaxRequestTargetInput(markupIdToComponent,prependJavaScripts,appendJavaScripts);
        String actualPrependScripts=RepeaterUtil.get().prependedScripts(input);
        Assert.assertEquals(actualPrependScripts,prependJavaScripts.toString());
        String actualAppendScripts=RepeaterUtil.get().appendedScripts(input);
        Assert.assertEquals(actualAppendScripts,appendJavaScripts.toString());
    }

    @Test(groups = {"utilTests"})
    public void testScripts_2(){
        List<String> markupIdToComponent=new ArrayList<String>();
        markupIdToComponent.add("1");
        markupIdToComponent.add("2") ;
        markupIdToComponent.add("3") ;
        List<String>prependJavaScripts=new ArrayList<String>();
        prependJavaScripts.add("prep1");
        prependJavaScripts.add("prep2");
        prependJavaScripts.add("fun1('a','b')");
        List<String>appendJavaScripts=new ArrayList<String>();
        appendJavaScripts.add("append1");
        appendJavaScripts.add("append2");
        appendJavaScripts.add("fun2('c','d')");
        final String input= constructAjaxRequestTargetInput(markupIdToComponent,prependJavaScripts,appendJavaScripts);
        String actualPrependScripts=RepeaterUtil.get().prependedScripts(input);
        Assert.assertEquals(actualPrependScripts,prependJavaScripts.toString());
        String actualAppendScripts=RepeaterUtil.get().appendedScripts(input);
        Assert.assertEquals(actualAppendScripts,appendJavaScripts.toString());
    }

     String constructAjaxRequestTargetInput(List<String>markupIdToComponent,List<String> prependJavaScripts,List<String>appendJavaScripts){
        final String input="[AjaxRequestTarget@" + "xyz" + " markupIdToComponent [" + markupIdToComponent +
                "], prependJavaScript [" + prependJavaScripts + "], appendJavaScript [" +
                appendJavaScripts + "]" ;
         return input;
    }

}
