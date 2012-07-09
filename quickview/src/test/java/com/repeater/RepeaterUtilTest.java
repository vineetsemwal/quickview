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
package com.repeater;

import com.repeater.navigator.TestPanel2;
import junit.framework.Assert;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.mock.MockApplication;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.util.tester.WicketTester;
import org.mockito.Mockito;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

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
}
