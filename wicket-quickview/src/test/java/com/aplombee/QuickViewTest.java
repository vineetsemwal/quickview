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


import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.wicket.Application;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.RuntimeConfigurationType;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.mock.MockApplication;
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.util.tester.WicketTester;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * @author Vineet Semwal
 */
public class QuickViewTest {

    private static Logger logger = Logger.getLogger(QuickViewTest.class);
    Component updateBefore;

    WicketTester tester;

    @BeforeTest
    void setup() {
        tester = new WicketTester(createMockApplication());
        items.clear();
    }

    private static QuickMockApplication createMockApplication() {
       return new QuickMockApplication();
    }

    /**
     * check when everything is passed from constructor
     */
    @Test(groups = {"wicketTests"})
    public void constructor_1() {
        int oneBlock = 2;
        final String repeaterId = "repeater";
        IDataProvider<TestObj> provider = mockProvider(oneBlock);
        QuickView<TestObj> repeater = new QuickView<TestObj>(repeaterId, provider, ReUse.ITEMSNAVIGATION, oneBlock) {

            @Override
            protected void populate(Item<TestObj> item) {
            }

        };
        repeater.setMarkupId("con");
        Assert.assertEquals(ReUse.ITEMSNAVIGATION, repeater.getReuse());
        Assert.assertEquals(repeater.getDataProvider(), provider);
        //  Assert.assertEquals(repeater.getFirstInitialization(), start);
        Assert.assertEquals(repeater.getItemsPerRequest(), oneBlock);

    }

    /**
     * reuse  is not passed from constructor
     */
    @Test(groups = {"wicketTests"})
    public void constructor_2() {
        int oneBlock = 12;
        final String id = "connn", repeaterId = "repeat";
        IDataProvider<TestObj> provider = mockProvider(oneBlock);
        QuickView<TestObj> repeater = new QuickView<TestObj>(repeaterId, provider, oneBlock) {
            @Override
            public void populate(Item<TestObj> item) {
            }
        };
        repeater.setMarkupId("con");
        Assert.assertEquals(ReUse.NOT_INITIALIZED, repeater.getReuse());
        Assert.assertEquals(repeater.getDataProvider(), provider);
        Assert.assertEquals(repeater.getItemsPerRequest(), oneBlock);

    }

    /**
     * Reuse.All is set
     */

    @Test(groups = {"wicketTests"})
    public void constructor_3() {
        int oneBlock = 12;
        final String id = "connn", repeaterId = "repeat";
        IDataProvider<TestObj> provider = mockProvider(oneBlock);
        QuickView<TestObj> repeater = new QuickView<TestObj>(repeaterId, provider, ReUse.ALL, oneBlock) {

            @Override
            public void populate(Item<TestObj> item) {
            }
        };
        repeater.setReuse(ReUse.ALL);
        repeater.setMarkupId("con");
        Assert.assertEquals(repeater.getReuse(), ReUse.ALL);
        Assert.assertEquals(repeater.getDataProvider(), provider);
        Assert.assertEquals(repeater.getItemsPerRequest(), oneBlock);

    }

    /**
     * Reuse.All is set
     */

    @Test(groups = {"wicketTests"})
    public void constructor_4() {
        final String id = "connn", repeaterId = "repeat";
        IDataProvider<TestObj> provider = mockProvider(10);
        QuickView<TestObj> repeater = new QuickView<TestObj>(repeaterId, provider, ReUse.ALL) {

            @Override
            public void populate(Item<TestObj> item) {
            }
        };
        repeater.setReuse(ReUse.ALL);
        repeater.setMarkupId("con");
        Assert.assertEquals(repeater.getReuse(), ReUse.ALL);
        Assert.assertEquals(repeater.getDataProvider(), provider);
        Assert.assertEquals(repeater.getItemsPerRequest(), Integer.MAX_VALUE);

    }



    @Test(groups = {"wicketTests"})
    public void constructor_5() {
        final String id = "connn", repeaterId = "repeat";
        IDataProvider<TestObj> provider = mockProvider(10);
        QuickView<TestObj> repeater = new QuickView<TestObj>(repeaterId, provider) {

            @Override
            public void populate(Item<TestObj> item) {
            }
        };
        repeater.setReuse(ReUse.ALL);
        repeater.setMarkupId("con");
        Assert.assertEquals(repeater.getReuse(), ReUse.ALL);
        Assert.assertEquals(repeater.getDataProvider(), provider);
        Assert.assertEquals(repeater.getItemsPerRequest(), Integer.MAX_VALUE);

    }
    @Test(groups = {"utilTests"},expectedExceptions = IllegalArgumentException.class)
    public void constructor_6(){
        IDataProvider data=Mockito.mock(IDataProvider.class);
        QuickView quickView=new QuickView("id",data,null) {
            @Override
            protected void populate(Item item) {
            }
        } ;

    }
    /**
     * Reuse set to null
     */
    @Test(groups = {"wicketTests"}, expectedExceptions = RuntimeException.class)
    public void setReuse_1() {
        int start = 5;
        int oneBlock = 12;
        final String id = "connn", repeaterId = "repeat";
        IDataProvider<TestObj> provider = mockProvider(oneBlock);
        QuickView<TestObj> repeater = new QuickView<TestObj>(repeaterId, provider, oneBlock) {

            @Override
            public void populate(Item<TestObj> item) {
            }
        };

        repeater.setReuse(null);
    }

    /**
     * Reuse set to NOT_INITIALIZED
     */
    @Test(groups = {"wicketTests"})
    public void setReuse_2() {
        int start = 5;
        int oneBlock = 12;
        final String id = "connn", repeaterId = "repeat";
        IDataProvider<TestObj> provider = mockProvider(oneBlock);
        QuickView<TestObj> repeater = new QuickView<TestObj>(repeaterId, provider, oneBlock) {

            @Override
            public void populate(Item<TestObj> item) {
            }
        };
        boolean isexception = false;
        try {
            repeater.setReuse(ReUse.NOT_INITIALIZED);
        } catch (Exception e) {
            isexception = true;
        }
        Assert.assertTrue(isexception);
    }

    /**
     * Reuse set properly
     */
    @Test(groups = {"wicketTests"})
    public void setReuse_3() {
        int start = 5;
        int oneBlock = 12;
        final String id = "connn", repeaterId = "repeat";
        IDataProvider<TestObj> provider = mockProvider(oneBlock);
        QuickView<TestObj> repeater = new QuickView<TestObj>(repeaterId, provider, oneBlock) {

            @Override
            public void populate(Item<TestObj> item) {
            }
        };
        boolean isexception = false;

        repeater.setReuse(ReUse.ITEMSNAVIGATION);
        Assert.assertEquals(repeater.getReuse(), ReUse.ITEMSNAVIGATION);
    }


    /**
     * add one component ,isajax=true,isParentadded=false
     */
    @Test(groups = {"wicketTests"})
    public void add_1() {
        items.clear();
        final int itemsPerRequest=1;
        final QuickViewParent parent = new QuickViewParent("parent");
        parent.setOutputMarkupId(true);
        final IDataProvider dataProvider =  new ListDataProvider(data(10));
        QuickMockApplication app=new QuickMockApplication();
        WicketTester tester=new WicketTester(app);
        final QuickView<Integer> quickView = new QuickView<Integer>("quickview", dataProvider,itemsPerRequest) {

            public void populate(Item<Integer> item) {
            }
        };
        final AjaxLink link=new AjaxLink("link") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                Iterator<Integer> it=  dataProvider.iterator(1,itemsPerRequest);
                while(it.hasNext()){
                    int object=it.next();

                    Item<Integer> item=quickView.buildItem(quickView.newChildId(),quickView.getChildId() ,object);
                    items.add(item);
                    quickView.add(item);
                }
            }
        };
        parent.add(quickView);
        quickView.setReuse(ReUse.ITEMSNAVIGATION);

        TestQuickViewContainer panel=new TestQuickViewContainer("panel"){
            @Override
            public QuickViewParent newParent() {
                return parent;
            }

            @Override
            public AbstractLink newLink() {
                return link;
            }
        };

        tester.startComponentInPage(panel);
        tester.executeAjaxEvent(link, "onclick");
        AjaxRequestTarget target=app.getLastAjaxRequestTarget();
        Item<Integer> actualItem1=items.get(0);
        String expectedPrependedScript1=RepeaterUtil.get().insertAfter(actualItem1,parent) ;
        List<String>expectedPrependedScripts=new ArrayList<String>() ;
        expectedPrependedScripts.add(expectedPrependedScript1);
        String actualPrependedScripts=quickView.getRepeaterUtil().prependedScripts(target.toString());

        Assert.assertEquals(quickView.size(),2);
        //asserting prependedscripts
        Assert.assertEquals(actualPrependedScripts,expectedPrependedScripts.toString());

        //asserting first item
        Assert.assertTrue(target.getComponents().contains(actualItem1));
       Assert.assertEquals(actualItem1.getModelObject().intValue(), 1);

    }


    /**
     * add one component ,isajax=false,isParentadded=false
     */
    @Test(groups = {"wicketTests"})
    public void add_2() {
        items.clear();
        final int itemsPerRequest=2;

        final QuickViewParent parent = new QuickViewParent("parent");
        parent.setOutputMarkupId(true);
        final IDataProvider dataProvider =  new ListDataProvider(data(10));
        QuickMockApplication app=new QuickMockApplication();
        WicketTester tester=new WicketTester(app);
        final QuickView<Integer> quickView = new QuickView<Integer>("quickview", dataProvider,itemsPerRequest) {

            public void populate(Item<Integer> item) {
            }
        };
        final AjaxLink link=new AjaxLink("link") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                Iterator<Integer> it=  dataProvider.iterator(2,itemsPerRequest);
                while(it.hasNext()){
                    int object=it.next();
                    Item<Integer> item=quickView.buildItem(quickView.newChildId(), quickView.getChildId() ,object);
                    items.add(item);
                    quickView.add(item);
                }
            }
        };
        parent.add(quickView);
        quickView.setReuse(ReUse.ITEMSNAVIGATION);

        TestQuickViewContainer panel=new TestQuickViewContainer("panel"){
            @Override
            public QuickViewParent newParent() {
                return parent;
            }

            @Override
            public AjaxLink newLink() {
                return link;
            }
        };

        tester.startComponentInPage(panel);
        tester.executeAjaxEvent(link, "onclick");

        Assert.assertEquals(quickView.size(),4);
        AjaxRequestTarget target=app.getLastAjaxRequestTarget();
        Item<Integer> actualItem1=items.get(0);
        Item<Integer> actualItem2=items.get(1);
        String expectedPrependedScript1=RepeaterUtil.get().insertAfter(actualItem1,parent) ;
        String expectedPrependedScript2=RepeaterUtil.get().insertAfter(actualItem2,parent) ;
        List<String>expectedPrependedScripts=new ArrayList<String>() ;
        expectedPrependedScripts.add(expectedPrependedScript1);
        expectedPrependedScripts.add(expectedPrependedScript2);
        String actualPrependedScripts=quickView.getRepeaterUtil().prependedScripts(target.toString());

        //asserting prependedscripts
        Assert.assertEquals(actualPrependedScripts,expectedPrependedScripts.toString());

        //asserting first item

        Assert.assertTrue(target.getComponents().contains(actualItem1));
        Assert.assertEquals(actualItem1.getModelObject().intValue(),2);


        //asserting seconditem
        Assert.assertTrue(target.getComponents().contains(actualItem2));
        Assert.assertEquals(actualItem2.getModelObject().intValue(),3);
    }


    /**
     * one component added ,ajax=false ,parent added=false
     */
    @Test(groups = {"wicketTests"})
    public void add_3() {
        items.clear();
        final int itemsPerRequest=1;

        final QuickViewParent parent = new QuickViewParent("parent");
        parent.setOutputMarkupId(true);
        final IDataProvider dataProvider =  new ListDataProvider(data(10));
        QuickMockApplication app=new QuickMockApplication();
        WicketTester tester=new WicketTester(app);
        final QuickView<Integer> quickView = new QuickView<Integer>("quickview", dataProvider,itemsPerRequest) {

            public void populate(Item<Integer> item) {
            }
        };
        final Link link=new Link("link") {
            @Override
            public void onClick() {
                Iterator<Integer> it=  dataProvider.iterator(1,itemsPerRequest);
                while(it.hasNext()){
                    int object=it.next();
                    Item<Integer> item=quickView.buildItem(quickView.newChildId(),quickView.getChildId() ,object);
                    items.add(item);
                    quickView.add(item);
                }
            }
        };
        parent.add(quickView);
        //reuse all is intentionally set because quickview is rendered again and any other strategy would not retail all  which we need to check for quickview.size()
        quickView.setReuse(ReUse.ALL);

        TestQuickViewContainer panel=new TestQuickViewContainer("panel"){
            @Override
            public QuickViewParent newParent() {
                return parent;
            }

            @Override
            public Link newLink() {
                return link;
            }
        };

        tester.startComponentInPage(panel);
        tester.clickLink(link);
        Assert.assertEquals(quickView.size(),2);
        Item<Integer> actualItem1=items.get(0);
        //asserting first item
        Assert.assertEquals(actualItem1.getModelObject().intValue(),1);
    }


    /**
     * one component added ,ajax=true,parent added=true
     */
    @Test(groups = {"wicketTests"})
    public void add_4() {
        items.clear();
        final int itemsPerRequest=1;
        final QuickViewParent parent = new QuickViewParent("parent");
        parent.setOutputMarkupId(true);
        final IDataProvider dataProvider =  new ListDataProvider(data(10));
        QuickMockApplication app=new QuickMockApplication();
        WicketTester tester=new WicketTester(app);
        final QuickView<Integer> quickView = new QuickView<Integer>("quickview", dataProvider,itemsPerRequest) {

            public void populate(Item<Integer> item) {
            }
        };
        final AjaxLink link=new AjaxLink("link") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                Iterator<Integer> it=  dataProvider.iterator(1,itemsPerRequest);
                while(it.hasNext()){
                    int object=it.next();
                    Item<Integer> item=quickView.buildItem(quickView.newChildId(),quickView.getChildId() , object);
                    items.add(item);
                    target.add(parent);
                    quickView.add(item);

                }
            }
        };
        parent.add(quickView);
        //reuse all is intentionally set because quickview is rendered again and any other strategy would not retail all which we need to check for quickview.size()
        quickView.setReuse(ReUse.ALL);

        TestQuickViewContainer panel=new TestQuickViewContainer("panel"){
            @Override
            public QuickViewParent newParent() {
                return parent;
            }

            @Override
            public AbstractLink newLink() {
                return link;
            }
        };

        tester.startComponentInPage(panel);
        tester.executeAjaxEvent(link, "onclick");
        AjaxRequestTarget target=app.getLastAjaxRequestTarget();
        Item<Integer> actualItem1=items.get(0);

        List<String>expectedPrependedScripts=new ArrayList<String>() ;
        String actualPrependedScripts=quickView.getRepeaterUtil().prependedScripts(target.toString());

        //asserting prependedscripts  are empty
        Assert.assertEquals(actualPrependedScripts,expectedPrependedScripts.toString());

        Assert.assertEquals(quickView.size(),2);
        //asserting first item
        Assert.assertFalse(target.getComponents().contains(actualItem1));
       Assert.assertEquals(actualItem1.getModelObject().intValue(),1);

    }


    /**
     * one component added ,ajax=true ,parent added=false
     */
    @Test(groups = {"wicketTests"})
    public void addAtStart_1() {
        items.clear();
        final int itemsPerRequest=1;
        final QuickViewParent parent = new QuickViewParent("parent");
        parent.setOutputMarkupId(true);
        final IDataProvider dataProvider =  new ListDataProvider(data(10));
        QuickMockApplication app=new QuickMockApplication();
        WicketTester tester=new WicketTester(app);
        final QuickView<Integer> quickView = new QuickView<Integer>("quickview", dataProvider,itemsPerRequest) {

            public void populate(Item<Integer> item) {
            }
        };
        final AjaxLink link=new AjaxLink("link") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                Iterator<Integer> it=  dataProvider.iterator(1,itemsPerRequest);
                while(it.hasNext()){
                    int object=it.next();
                    Item<Integer> item=quickView.buildItem(quickView.newChildId(),quickView.getChildId() , object);
                    items.add(item);
                    quickView.addAtStart(item);
                }
            }
        };
        parent.add(quickView);
        quickView.setReuse(ReUse.ITEMSNAVIGATION);

        TestQuickViewContainer panel=new TestQuickViewContainer("panel"){
            @Override
            public QuickViewParent newParent() {
                return parent;
            }

            @Override
            public AbstractLink newLink() {
                return link;
            }
        };

        tester.startComponentInPage(panel);
        tester.executeAjaxEvent(link, "onclick");
        AjaxRequestTarget target=app.getLastAjaxRequestTarget();
        Item<Integer> actualItem1=items.get(0);
        String expectedPrependedScript1=RepeaterUtil.get().insertBefore(actualItem1,parent) ;
        List<String>expectedPrependedScripts=new ArrayList<String>() ;
        expectedPrependedScripts.add(expectedPrependedScript1);
        String actualPrependedScripts=quickView.getRepeaterUtil().prependedScripts(target.toString());

        Assert.assertEquals(quickView.size(),2);
        //asserting prependedscripts
        Assert.assertEquals(actualPrependedScripts,expectedPrependedScripts.toString());

        //asserting first item
        Assert.assertTrue(target.getComponents().contains(actualItem1));
      Assert.assertEquals(actualItem1.getModelObject().intValue(),1);

    }

    /**
     * 2 components added ,ajax=true ,parent added=false
     */
    @Test(groups = {"wicketTests"})
    public void addAtStart_2() {
        items.clear();
        final int itemsPerRequest=2;

        final QuickViewParent parent = new QuickViewParent("parent");
        parent.setOutputMarkupId(true);
        final IDataProvider dataProvider =  new ListDataProvider(data(10));
        QuickMockApplication app=new QuickMockApplication();
        WicketTester tester=new WicketTester(app);
        final QuickView<Integer> quickView = new QuickView<Integer>("quickview", dataProvider,itemsPerRequest) {

            public void populate(Item<Integer> item) {
            }
        };
        final AjaxLink link=new AjaxLink("link") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                Iterator<Integer> it=  dataProvider.iterator(2,itemsPerRequest);
                while(it.hasNext()){
                    int object=it.next();
                    Item<Integer> item=quickView.buildItem(quickView.newChildId(),quickView.getChildId() , object);
                    items.add(item);
                    quickView.addAtStart(item);
                }
            }
        };
        parent.add(quickView);
        quickView.setReuse(ReUse.ITEMSNAVIGATION);

        TestQuickViewContainer panel=new TestQuickViewContainer("panel"){
            @Override
            public QuickViewParent newParent() {
                return parent;
            }

            @Override
            public AjaxLink newLink() {
                return link;
            }
        };

        tester.startComponentInPage(panel);
        tester.executeAjaxEvent(link, "onclick");

        Assert.assertEquals(quickView.size(),4);
        AjaxRequestTarget target=app.getLastAjaxRequestTarget();
        Item<Integer> actualItem1=items.get(0);
        Item<Integer> actualItem2=items.get(1);
        String expectedPrependedScript1=RepeaterUtil.get().insertBefore(actualItem1,parent) ;
        String expectedPrependedScript2=RepeaterUtil.get().insertBefore(actualItem2,parent) ;
        List<String>expectedPrependedScripts=new ArrayList<String>() ;
        expectedPrependedScripts.add(expectedPrependedScript1);
        expectedPrependedScripts.add(expectedPrependedScript2);
        String actualPrependedScripts=quickView.getRepeaterUtil().prependedScripts(target.toString());

        //asserting prependedscripts
        Assert.assertEquals(actualPrependedScripts,expectedPrependedScripts.toString());

        //asserting first item

        Assert.assertTrue(target.getComponents().contains(actualItem1));
       Assert.assertEquals(actualItem1.getModelObject().intValue(),2);


        //asserting seconditem
        Assert.assertTrue(target.getComponents().contains(actualItem2));
       Assert.assertEquals(actualItem2.getModelObject().intValue(),3);
    }

    /**
     * one component added ,ajax=false ,parent added=false
     */
    @Test(groups = {"wicketTests"})
    public void addAtStart_3() {
        items.clear();
        final int itemsPerRequest=1;

        final QuickViewParent parent = new QuickViewParent("parent");
        parent.setOutputMarkupId(true);
        final IDataProvider dataProvider =  new ListDataProvider(data(10));
        QuickMockApplication app=new QuickMockApplication();
        WicketTester tester=new WicketTester(app);
        final QuickView<Integer> quickView = new QuickView<Integer>("quickview", dataProvider,itemsPerRequest) {

            public void populate(Item<Integer> item) {
            }
        };
        final Link link=new Link("link") {
            @Override
            public void onClick() {
                Iterator<Integer> it=  dataProvider.iterator(1,itemsPerRequest);
                while(it.hasNext()){
                    int object=it.next();
                    Item<Integer> item=quickView.buildItem(quickView.newChildId(),quickView.getChildId() , object);
                    items.add(item);
                    quickView.addAtStart(item);
                }
            }
        };
        parent.add(quickView);
        //reuse all is intentionally set because quickview is rendered again and any other strategy would not retail all  which we need to check for quickview.size()
        quickView.setReuse(ReUse.ALL);

        TestQuickViewContainer panel=new TestQuickViewContainer("panel"){
            @Override
            public QuickViewParent newParent() {
                return parent;
            }

            @Override
            public Link newLink() {
                return link;
            }
        };

        tester.startComponentInPage(panel);
        tester.clickLink(link);
        Assert.assertEquals(quickView.size(),2);
        Item<Integer> actualItem1=items.get(0);
        //asserting first item
       Assert.assertEquals(actualItem1.getModelObject().intValue(), 1);
    }

    /**
     * one component added ,ajax=true,parent added=true
     */
    @Test(groups = {"wicketTests"})
    public void addAtStart_4() {
        items.clear();
        final int itemsPerRequest=1;
        final QuickViewParent parent = new QuickViewParent("parent");
        parent.setOutputMarkupId(true);
        final IDataProvider dataProvider =  new ListDataProvider(data(10));
        QuickMockApplication app=new QuickMockApplication();
        WicketTester tester=new WicketTester(app);
        final QuickView<Integer> quickView = new QuickView<Integer>("quickview", dataProvider,itemsPerRequest) {

            public void populate(Item<Integer> item) {
            }
        };
        final AjaxLink link=new AjaxLink("link") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                Iterator<Integer> it=  dataProvider.iterator(1,itemsPerRequest);
                while(it.hasNext()){
                    int object=it.next();
                    Item<Integer> item=quickView.buildItem(quickView.newChildId(),quickView.getChildId() , object);
                    items.add(item);
                    target.add(parent);
                    quickView.addAtStart(item);

                }
            }
        };
        parent.add(quickView);
        //reuse all is intentionally set because quickview is rendered again and any other strategy would not retail all which we need to check for quickview.size()
        quickView.setReuse(ReUse.ALL);

        TestQuickViewContainer panel=new TestQuickViewContainer("panel"){
            @Override
            public QuickViewParent newParent() {
                return parent;
            }

            @Override
            public AbstractLink newLink() {
                return link;
            }
        };

        tester.startComponentInPage(panel);
        tester.executeAjaxEvent(link, "onclick");
        AjaxRequestTarget target=app.getLastAjaxRequestTarget();
        Item<Integer> actualItem1=items.get(0);

        List<String>expectedPrependedScripts=new ArrayList<String>() ;
        String actualPrependedScripts=quickView.getRepeaterUtil().prependedScripts(target.toString());

        //asserting prependedscripts  are empty
        Assert.assertEquals(actualPrependedScripts,expectedPrependedScripts.toString());

        Assert.assertEquals(quickView.size(),2);
        //asserting first item
        Assert.assertFalse(target.getComponents().contains(actualItem1));
       Assert.assertEquals(actualItem1.getModelObject().intValue(),1);

    }



    /*
     *start index=0
    */
    @Test(groups = {"wicketTests"})
    public void buildItems_1() {
        List<Integer> data = data(10);
        IDataProvider<Integer> dataProvider = new ListDataProvider<Integer>(data);
        QuickView quickView = new QuickView("quickview", dataProvider, ReUse.ITEMSNAVIGATION) {
            @Override
            protected void populate(Item item) {
            }
        };
        Iterator<? extends Integer> dataIterator = dataProvider.iterator(0, 2);
        Iterator<Item<Integer>> itemsIterator = quickView.buildItems(0, dataIterator);
        Item<Integer> item1 = itemsIterator.next();
        Item<Integer> item2 = itemsIterator.next();
        Assert.assertEquals(item1.getIndex(), 0);
        Assert.assertEquals(item1.getModelObject().intValue(), 0);
        Assert.assertEquals(item2.getIndex(), 1);
        Assert.assertEquals(item2.getModelObject().intValue(), 1);
        Assert.assertTrue(Long.parseLong(item2.getId()) > Long.parseLong(item1.getId()));
    }

    /*
     *start index=10
    */
    @Test(groups = {"wicketTests"})
    public void buildItems_2() {
        List<Integer> data = data(10);
        IDataProvider<Integer> dataProvider = new ListDataProvider<Integer>(data);
        QuickView<Integer> quickView = new QuickView<Integer>("quickview", dataProvider, ReUse.ITEMSNAVIGATION) {
            @Override
            protected void populate(Item item) {
            }
        };
        Iterator<? extends Integer> dataIterator = dataProvider.iterator(5, 2);
        Iterator<Item<Integer>> itemsIterator = quickView.buildItems(10, dataIterator);
        Item<Integer> item1 = itemsIterator.next();
        Item<Integer> item2 = itemsIterator.next();
        Assert.assertEquals(item1.getIndex(), 10);
        Assert.assertEquals(item1.getModelObject().intValue(), 5);
        Assert.assertEquals(item2.getIndex(), 11);
        Assert.assertEquals(item2.getModelObject().intValue(), 6);
        Assert.assertTrue(Long.parseLong(item2.getId()) > Long.parseLong(item1.getId()));
    }


    /**
     * create children with iterator
     */

    @Test(groups = {"wicketTests"})
    public void createChildren_1() {
        List<Integer> data = data(10);
        IDataProvider<Integer> dataProvider = new ListDataProvider<Integer>(data);
        QuickView<Integer> quickView = new QuickView("quickview", dataProvider, ReUse.ITEMSNAVIGATION) {
            @Override
            protected void populate(Item item) {
            }
        };
        Iterator<? extends Integer> dataIterator = dataProvider.iterator(0, 2);
        Iterator<Item<Integer>> itemsIterator = quickView.buildItems(0, dataIterator);
        quickView.createChildren(itemsIterator);
        Assert.assertEquals(quickView.size(), 2);
        Item<Integer> item1 = (Item) quickView.get(0);
        Item<Integer> item2 = (Item) quickView.get(1);

        Assert.assertEquals(item1.getIndex(), 0);
        Assert.assertEquals(item1.getModelObject().intValue(), 0);
        Assert.assertEquals(item2.getIndex(), 1);
        Assert.assertEquals(item2.getModelObject().intValue(), 1);
        Assert.assertTrue(Long.parseLong(item2.getId()) > Long.parseLong(item1.getId()));

    }

    /*
     *start index=10
    */
    @Test(groups = {"wicketTests"})
    public void createChildren_2() {
        List<Integer> data = data(10);
        IDataProvider<Integer> dataProvider = new ListDataProvider<Integer>(data);
        QuickView<Integer> quickView = new QuickView<Integer>("quickview", dataProvider, ReUse.ITEMSNAVIGATION) {
            @Override
            protected void populate(Item item) {
            }
        };
        Iterator<? extends Integer> dataIterator = dataProvider.iterator(5, 2);
        Iterator<Item<Integer>> itemsIterator = quickView.buildItems(10, dataIterator);
        quickView.createChildren(itemsIterator);
        Assert.assertEquals(quickView.size(), 2);
        Item<Integer> item1 = (Item) quickView.get(0);
        Item<Integer> item2 = (Item) quickView.get(1);

        Assert.assertEquals(item1.getIndex(), 10);
        Assert.assertEquals(item1.getModelObject().intValue(), 5);
        Assert.assertEquals(item2.getIndex(), 11);
        Assert.assertEquals(item2.getModelObject().intValue(), 6);
        Assert.assertTrue(Long.parseLong(item2.getId()) > Long.parseLong(item1.getId()));
    }


    /**
     * create children for page=0
     */

    @Test(groups = {"wicketTests"})
    public void createChildren_3() {
        List<Integer> data = data(10);
        IDataProvider<Integer> dataProvider = new ListDataProvider<Integer>(data);
        QuickView<Integer> quickView = new QuickView("quickview", dataProvider, ReUse.ITEMSNAVIGATION) {
            @Override
            protected void populate(Item item) {
            }
        };
        quickView.setItemsPerRequest(2);
        quickView.createChildren(0);
        Assert.assertEquals(quickView.size(), 2);
        Item<Integer> item1 = (Item) quickView.get(0);
        Item<Integer> item2 = (Item) quickView.get(1);
        Assert.assertEquals(item1.getIndex(), 0);
        Assert.assertEquals(item1.getModelObject().intValue(), 0);
        Assert.assertEquals(item2.getIndex(), 1);
        Assert.assertEquals(item2.getModelObject().intValue(), 1);
        Assert.assertTrue(Long.parseLong(item2.getId()) > Long.parseLong(item1.getId()));

    }


    /**
     * create children for page=1
     */

    @Test(groups = {"wicketTests"})
    public void createChildren_4() {
        List<Integer> data = data(10);
        IDataProvider<Integer> dataProvider = new ListDataProvider<Integer>(data);
        QuickView<Integer> quickView = new QuickView("quickview", dataProvider, ReUse.ITEMSNAVIGATION) {
            @Override
            protected void populate(Item item) {
            }
        };
        final int itemsPerRequest = 3, page = 1;
        quickView.setItemsPerRequest(itemsPerRequest);
        quickView.createChildren(page);
        Assert.assertEquals(quickView.size(), itemsPerRequest);
        Item<Integer> item1 = (Item) quickView.get(0);
        Item<Integer> item2 = (Item) quickView.get(1);
        Item<Integer> item3 = (Item) quickView.get(2);

        Assert.assertEquals(item1.getIndex(), 0);
        Assert.assertEquals(item1.getModelObject().intValue(), 3);
        Assert.assertEquals(item2.getIndex(), 1);
        Assert.assertEquals(item2.getModelObject().intValue(), 4);
        Assert.assertEquals(item3.getIndex(), 2);
        Assert.assertEquals(item3.getModelObject().intValue(), 5);
        Assert.assertTrue(Long.parseLong(item2.getId()) > Long.parseLong(item1.getId()));
        Assert.assertTrue(Long.parseLong(item3.getId()) > Long.parseLong(item2.getId()));

    }

    @Test(groups = {"wicketTests"})
    public void reuseItemsForCurrentPage_1() {
        IDataProvider<Integer> dataProvider = Mockito.mock(IDataProvider.class);
        final int page = 1;
        final Iterator iterator = Mockito.mock(Iterator.class);
        final Iterator newIterator = Mockito.mock(Iterator.class);
        final Iterator oldIterator = Mockito.mock(Iterator.class);
        final IRepeaterUtil util=Mockito.mock(IRepeaterUtil.class);
        Mockito.when(util.reuseItemsIfModelsEqual(oldIterator, newIterator)).thenReturn(iterator);
        QuickView quickView = new QuickView("quickview", dataProvider, ReUse.ITEMSNAVIGATION) {
            @Override
            protected void populate(Item item) {
            }

            @Override
            public Iterator<Component> itemsIterator() {
                return oldIterator;
            }

            @Override
            protected Iterator buildItems(int index, Iterator iterator) {
                return newIterator;
            }

            @Override
            protected IRepeaterUtil getRepeaterUtil() {
                return util;
            }
        };

        final int itemsPerRequest = 2;
        quickView.setItemsPerRequest(itemsPerRequest);
        QuickView<Integer> spy = Mockito.spy(quickView);
        final int start = page * itemsPerRequest;
        Iterator dataIterator = Mockito.mock(Iterator.class);
        Mockito.when(dataProvider.iterator(start, itemsPerRequest)).thenReturn(dataIterator);
        Mockito.when(spy.buildItems(0, dataIterator)).thenReturn(newIterator);
        spy.reuseItemsForCurrentPage(page);
        Mockito.verify(util, Mockito.times(1)).reuseItemsIfModelsEqual(oldIterator, newIterator);

    }

    @Test(groups = {"wicketTests"})
    public void remove_1() {
        items.clear();
        final int itemsPerRequest=1;
        final QuickViewParent parent = new QuickViewParent("parent");
        parent.setOutputMarkupId(true);
        final IDataProvider dataProvider =  new ListDataProvider(data(10));
        QuickMockApplication app=new QuickMockApplication();
        WicketTester tester=new WicketTester(app);
        final QuickView<Integer> quickView = new QuickView<Integer>("quickview", dataProvider,itemsPerRequest) {

            public void populate(Item<Integer> item) {
            }
        };
        final AjaxLink link=new AjaxLink("link") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                     Item<Integer>item=quickView.getItem(0);
                items.add(item);
                    quickView.remove(item);
            }
        };
        parent.add(quickView);
        quickView.setReuse(ReUse.ITEMSNAVIGATION);

        TestQuickViewContainer panel=new TestQuickViewContainer("panel"){
            @Override
            public QuickViewParent newParent() {
                return parent;
            }

            @Override
            public AbstractLink newLink() {
                return link;
            }
        };

        tester.startComponentInPage(panel);
        Assert.assertEquals(quickView.size(),1);
        tester.executeAjaxEvent(link, "onclick");
        AjaxRequestTarget target=app.getLastAjaxRequestTarget();
        Item<Integer> actualItem1=items.get(0);
        String expectedPrependedScript1=RepeaterUtil.get().removeItem(actualItem1) ;
        List<String>expectedPrependedScripts=new ArrayList<String>() ;
        expectedPrependedScripts.add(expectedPrependedScript1);
        String actualPrependedScripts=quickView.getRepeaterUtil().prependedScripts(target.toString());
        Assert.assertEquals(quickView.size(),0);
        //asserting prependedscripts
        Assert.assertEquals(actualPrependedScripts,expectedPrependedScripts.toString());
        //asserting first item
       Assert.assertTrue(target.getComponents().contains(actualItem1));

    }



    /**
     * reuse={@link ReUse.DEFAULT_PAGING}  ,size=0   .current page=2
     */

    @Test(groups = {"wicketTests"})
    public void onPopulate_1() {
        IDataProvider provider = Mockito.mock(IDataProvider.class);
        final int size = 0;
       final IRepeaterUtil util=Mockito.mock(IRepeaterUtil.class);
        QuickView repeater = new QuickView("repeater", provider, 2) {

            @Override
            public IRepeaterUtil getRepeaterUtil() {
                return  util;
            }

            @Override
            protected void populate(Item item) {
            }

            @Override
            public MarkupContainer simpleAdd(Component... c) {
                return this;
            }

            @Override
            protected void createChildren(int page) {
            }

            @Override
            public MarkupContainer simpleRemoveAll() {
                return this;
            }

            @Override
            public int size() {
                return size;
            }

            @Override
            public int _getCurrentPage() {
                return 2;
            }
        };
        repeater.setReuse(ReUse.PAGING);
        QuickView spy = Mockito.spy(repeater);
        spy.onPopulate();
        Mockito.verify(util,Mockito.times(1)).reuseNotInitialized(spy);
        Mockito.verify(util,Mockito.times(1)).parentNotSuitable(spy);
        Mockito.verify(spy, Mockito.times(1)).simpleRemoveAllIfNotReuse();
        Mockito.verify(spy, Mockito.times(1)).createChildren(2);

    }


    /**
     * reuse={@link ReUse.DEFAULT_ROWSNAVIGATOR}  ,size=0   .current page=2
     */

    @Test(groups = {"wicketTests"})
    public void onPopulate_2() {
        IDataProvider provider = Mockito.mock(IDataProvider.class);
        final int size = 0;
        final IRepeaterUtil util=Mockito.mock(IRepeaterUtil.class);
        QuickView repeater = new QuickView("repeater", provider, 3) {
            @Override
            protected void populate(Item item) {
            }

            @Override
            public IRepeaterUtil getRepeaterUtil() {
                return  util;
            }
            @Override
            public MarkupContainer simpleAdd(Component... c) {
                return this;
            }

            @Override
            protected void createChildren(int page) {
            }

            @Override
            public MarkupContainer simpleRemoveAll() {
                return this;
            }

            @Override
            public int size() {
                return size;
            }

            @Override
            public int _getCurrentPage() {
                return 2;
            }
        };
        repeater.setReuse(ReUse.ITEMSNAVIGATION);
        QuickView spy = Mockito.spy(repeater);
        spy.onPopulate();
        Mockito.verify(util,Mockito.times(1)).reuseNotInitialized(spy);
        Mockito.verify(util,Mockito.times(1)).parentNotSuitable(spy);
        Mockito.verify(spy, Mockito.times(1)).simpleRemoveAllIfNotReuse();
        Mockito.verify(spy, Mockito.times(1)).createChildren(0);
        Mockito.verify(spy, Mockito.times(1)).setCurrentPage(0);

    }

    /**
     * reuse={@link ReUse.REUSE_ALL}  ,size=0   .current page=2
     */

    @Test(groups = {"wicketTests"})
    public void onPopulate_3() {
        IDataProvider provider = Mockito.mock(IDataProvider.class);
        final int size = 0;
        final IRepeaterUtil util=Mockito.mock(IRepeaterUtil.class);
        QuickView repeater = new QuickView("repeater", provider, 3) {
            @Override
            protected void populate(Item item) {
            }

            @Override
            public IRepeaterUtil getRepeaterUtil() {
                return  util;
            }
            @Override
            public MarkupContainer simpleAdd(Component... c) {
                return this;
            }

            @Override
            public void simpleRemoveAllIfNotReuse() {
            }

            @Override
            protected void createChildren(int page) {
            }

            @Override
            public MarkupContainer simpleRemoveAll() {
                return this;
            }

            @Override
            public int size() {
                return size;
            }

            @Override
            public int _getCurrentPage() {
                return 2;
            }
        };
        repeater.setReuse(ReUse.ALL);
        QuickView spy = Mockito.spy(repeater);
        spy.onPopulate();
        Mockito.verify(util,Mockito.times(1)).reuseNotInitialized(spy);
        Mockito.verify(util,Mockito.times(1)).parentNotSuitable(spy);
        Mockito.verify(spy, Mockito.times(1)).simpleRemoveAllIfNotReuse();
        Mockito.verify(spy, Mockito.times(1)).createChildren(0);

    }


    /**
     * reuse={@link ReUse.CURRENTPAGE}  ,size=0   .current page=2
     */

    @Test(groups = {"wicketTests"})
    public void onPopulate_4() {
        IDataProvider provider = Mockito.mock(IDataProvider.class);
        final int size = 0;
        final IRepeaterUtil util=Mockito.mock(IRepeaterUtil.class);
        QuickView repeater = new QuickView("repeater", provider, 3) {
            @Override
            protected void populate(Item item) {
            }
            @Override
            public IRepeaterUtil getRepeaterUtil() {
                return  util;
            }
            @Override
            public MarkupContainer simpleAdd(Component... c) {
                return this;
            }

            @Override
            protected void createChildren(int page) {
            }

            @Override
            public void simpleRemoveAllIfNotReuse() {
            }

            @Override
            public int size() {
                return size;
            }

            @Override
            public int _getCurrentPage() {
                return 2;
            }
        };
        repeater.setReuse(ReUse.CURRENTPAGE);
        QuickView spy = Mockito.spy(repeater);
        spy.onPopulate();
        Mockito.verify(util,Mockito.times(1)).reuseNotInitialized(spy);
        Mockito.verify(util,Mockito.times(1)).parentNotSuitable(spy);
        Mockito.verify(spy, Mockito.times(1)).simpleRemoveAllIfNotReuse();
        Mockito.verify(spy, Mockito.times(1)).createChildren(0);

    }

    /**
     * reuse ={@link ReUse.CURRENTPAGE}   ,size!=0    ,pagecount=6
     */

    @Test(groups = {"wicketTests"})
    public void onPopulate_5() {
        IDataProvider provider = Mockito.mock(IDataProvider.class);
        final int size = 5;
        final IRepeaterUtil util=Mockito.mock(IRepeaterUtil.class);
        final Iterator reusedItems=Mockito.mock(Iterator.class);
        QuickView repeater = new QuickView("repeater", provider, 3) {
            @Override
            protected void populate(Item item) {
            }

            @Override
            public MarkupContainer simpleAdd(Component... c) {
                return this;
            }

            @Override
            protected void createChildren(int page) {
            }
            @Override
            public IRepeaterUtil getRepeaterUtil() {
                return  util;
            }
            @Override
            public void simpleRemoveAllIfNotReuse() {
            }

            @Override
            public int size() {
                return size;
            }

            @Override
            public int _getCurrentPage() {
                return 2;
            }


            @Override
            public Iterator reuseItemsForCurrentPage(int currentPage) {
                return reusedItems;
            }

            @Override
            public MarkupContainer simpleRemoveAll() {
                return this;
            }


            @Override
            public int _getPageCount() {
                return 6;
            }
        };
        repeater.setCurrentPage(2);
        repeater.setReuse(ReUse.CURRENTPAGE);
        QuickView spy = Mockito.spy(repeater);
        spy.onPopulate();
        Mockito.verify(util, Mockito.times(1)).reuseNotInitialized(spy);
        Mockito.verify(util, Mockito.times(1)).parentNotSuitable(spy);
        Mockito.verify(spy, Mockito.times(1)).simpleRemoveAllIfNotReuse();
        Mockito.verify(spy,Mockito.times(1)) .reuseItemsForCurrentPage(2) ;
        Mockito.verify(spy,Mockito.times(1)).simpleRemoveAll();

    }

    /**
     * reuse ={@link ReUse.ALL}   ,size!=0    ,pagecount=6
     */

    @Test(groups = {"wicketTests"})
    public void onPopulate_6() {
        IDataProvider provider = Mockito.mock(IDataProvider.class);
        final int size = 5;
        final IRepeaterUtil util=Mockito.mock(IRepeaterUtil.class);
        QuickView repeater = new QuickView("repeater", provider, 3) {
            @Override
            protected void populate(Item item) {
            }

            @Override
            public IRepeaterUtil getRepeaterUtil() {
                return  util;
            }
            @Override
            public MarkupContainer simpleAdd(Component... c) {
                return this;
            }

            @Override
            protected void createChildren(int page) {
            }

            @Override
            public void simpleRemoveAllIfNotReuse() {
            }

            @Override
            public int size() {
                return size;
            }

            @Override
            public int _getCurrentPage() {
                return 2;
            }


            @Override
            public int _getPageCount() {
                return 6;    //To change body of overridden methods use File | Settings | File Templates.
            }
        };
        repeater.setReuse(ReUse.ALL);
        QuickView spy = Mockito.spy(repeater);
        spy.onPopulate();
        Mockito.verify(util,Mockito.times(1)).reuseNotInitialized(spy);
        Mockito.verify(util,Mockito.times(1)).parentNotSuitable(spy);
        Mockito.verify(spy, Mockito.times(1)).simpleRemoveAllIfNotReuse();
        Mockito.verify(spy, Mockito.never()).createChildren(Mockito.anyInt());

    }

    /**
     * items=10,itemsperrequest=2
     */
    @Test(groups = {"wicketTests"})
    public void getPageCount_1() {
        final int itemsperrequest = 2;
        IDataProvider provider = Mockito.mock(IDataProvider.class);
        Mockito.when(provider.size()).thenReturn(10);
        QuickView repeater = new QuickView("repeater", provider, itemsperrequest) {
            @Override
            protected void populate(Item item) {
            }

        };
        long actual = repeater.getPageCount();
        Assert.assertEquals(actual, 5);
    }

    /**
     * items=10,itemsperrequest=3
     */

    @Test(groups = {"wicketTests"})
    public void getPageCount_2() {
        final int itemsperrequest = 3;
        IDataProvider provider = Mockito.mock(IDataProvider.class);
        Mockito.when(provider.size()).thenReturn(10);
        QuickView repeater = new QuickView("repeater", provider, itemsperrequest) {
            @Override
            protected void populate(Item item) {
            }

        };
        long actual = repeater.getPageCount();
        Assert.assertEquals(actual, 4);
    }


    @Test(groups = {"wicketTests"})
    public void getItemsCount_1() {
        final int itemsPerRequest = 3;
        IDataProvider provider = Mockito.mock(IDataProvider.class);
        Mockito.when(provider.size()).thenReturn(10);
        QuickView repeater = new QuickView("repeater", provider, itemsPerRequest) {
            @Override
            protected void populate(Item item) {
            }

        };
        long actual = repeater.getItemsCount();
        Assert.assertEquals(actual, 10l);
    }

    /*
     * when repeater is not visible in hierarchy
     */
    @Test(groups = {"wicketTests"})
    public void getRowsCount_1() {
        final int itemsPerRequest = 3;
        IDataProvider provider = Mockito.mock(IDataProvider.class);
        Mockito.when(provider.size()).thenReturn(10);
        QuickView repeater = new QuickView("repeater", provider, itemsPerRequest) {
            @Override
            protected void populate(Item item) {
            }

            @Override
            public boolean isVisible() {
                return false;
            }
        };

        long actual = repeater.getRowsCount();
        Assert.assertEquals(actual, 0l);
    }

    /*
    * when repeater is  visible in hierarchy
    */
    @Test(groups = {"wicketTests"})
    public void getRowsCount_2() {
        final int itemsPerRequest = 3;
        IDataProvider provider = Mockito.mock(IDataProvider.class);
        Mockito.when(provider.size()).thenReturn(10);
        QuickView repeater = new QuickView("repeater", provider, itemsPerRequest) {
            @Override
            protected void populate(Item item) {
            }

            @Override
            public boolean isVisible() {
                return true;
            }

        };

        long actual = repeater.getRowsCount();
        Assert.assertEquals(actual, 10l);
    }

    /**
     * itemsperrequest>0
     */
    @Test(groups = {"wicketTests"})
    public void setItemsPerRequest_1() {
        final int itemsPerRequest = 3;
        IDataProvider provider = Mockito.mock(IDataProvider.class);
        Mockito.when(provider.size()).thenReturn(10);
        QuickView repeater = new QuickView("repeater", provider) {
            @Override
            protected void populate(Item item) {
            }

            @Override
            public boolean isVisible() {
                return true;
            }

        };
        repeater.setItemsPerRequest(itemsPerRequest);
        Assert.assertEquals(repeater.getItemsPerRequest(), 3);
    }

    /**
     * itemsperrequest<0
     */
    @Test(groups = {"wicketTests"})
    public void setItemsPerRequest_2() {
        final int itemsPerRequest = -1;
        IDataProvider provider = Mockito.mock(IDataProvider.class);
        Mockito.when(provider.size()).thenReturn(10);
        QuickView repeater = new QuickView("repeater", provider) {
            @Override
            protected void populate(Item item) {
            }

            @Override
            public boolean isVisible() {
                return true;
            }

        };

        boolean isException = false;
        try {
            repeater.setItemsPerRequest(itemsPerRequest);
        } catch (IllegalArgumentException ex) {
            isException = true;
        }
        Assert.assertTrue(isException);
    }

    @Test(groups = {"wicketTests"})
    public void setItemsPerRequest_3() {
        final int oldItemsPerRequest = 3;
        final int newItemsPerRequest = 5;
        IDataProvider provider = Mockito.mock(IDataProvider.class);
        Mockito.when(provider.size()).thenReturn(10);
        QuickView repeater = new QuickView("repeater", provider) {
            @Override
            protected void populate(Item item) {
            }

            @Override
            public boolean isVisible() {
                return true;
            }

        };
        repeater.setItemsPerRequest(oldItemsPerRequest);
       QuickView spy= Mockito.spy(repeater);
       spy.setItemsPerRequest(newItemsPerRequest);
       Assert.assertEquals(spy.getItemsPerRequest(), newItemsPerRequest);
        Mockito.verify(spy,Mockito.times(1)).setItemsPerRequest(newItemsPerRequest);
        Mockito.verify(spy,Mockito.times(1))._setCurrentPage(0);
    }

    /**
     * itemsPerRequest not changed  ie. if itemsPerRequest is not changed but it's set again
     */
    @Test(groups = {"wicketTests"})
    public void setItemsPerRequest_4() {
        final int itemsPerRequest = 3;
        IDataProvider provider = Mockito.mock(IDataProvider.class);
        Mockito.when(provider.size()).thenReturn(10);
        QuickView repeater = new QuickView("repeater", provider) {
            @Override
            protected void populate(Item item) {
            }

            @Override
            public boolean isVisible() {
                return true;
            }

        };
        repeater.setItemsPerRequest(itemsPerRequest);
        QuickView spy= Mockito.spy(repeater);
        spy.setItemsPerRequest(itemsPerRequest);
        Assert.assertEquals(spy.getItemsPerRequest(), itemsPerRequest);
        Mockito.verify(spy,Mockito.never())._setCurrentPage(0);
    }


    @Test(groups = {"wicketTests"})
    public void renderHead_1() {
        IDataProvider data = Mockito.mock(IDataProvider.class);
        QuickView quick = new QuickView("id", data, 1) {
            @Override
            protected void populate(Item item) {
            }
        };
        IHeaderResponse response = Mockito.mock(IHeaderResponse.class);
        quick.renderHead(response);
        Mockito.verify(response, Mockito.times(1)).renderJavaScriptReference(RepeaterUtilReference.get());
        Mockito.verify(response,Mockito.times(1)).renderJavaScriptReference(JqueryResourceReference.get());
    }



    /**
     * added two,removed one
     */
    @Test(groups = {"wicketTests"})
    public void simpleRemove(){
        IDataProvider data=Mockito.mock(IDataProvider.class);
        QuickView quickView=new QuickView("id",data) {
            @Override
            protected void populate(Item item) {
            }
        } ;
        Item one=quickView.buildItem( 67);
        Item two=quickView.buildItem(68);
        quickView.simpleAdd(one, two);
        quickView.simpleRemove(one);
        Assert.assertEquals(quickView.size(), 1);
    }

    /**
     * added two,removed all
     */
    @Test(groups = {"wicketTests"})
    public void simpleRemoveAll(){
        IDataProvider data=Mockito.mock(IDataProvider.class);
        QuickView quickView=new QuickView("id",data) {
            @Override
            protected void populate(Item item) {
            }
        } ;
        Item one=quickView.buildItem(67);
        Item two=quickView.buildItem(68);
        quickView.simpleAdd(one,two);
        quickView.simpleRemoveAll();
        Assert.assertEquals(quickView.size(), 0);
    }

    @Test(groups = {"wicketTests"})
    public void simpleAdd(){
        IDataProvider data=Mockito.mock(IDataProvider.class);
        QuickView quickView=new QuickView("id",data) {
            @Override
            protected void populate(Item item) {
            }
        } ;
        Item one=quickView.buildItem( 67);
        quickView.simpleAdd(one);

        Assert.assertEquals(quickView.size(), 1);
        Item two=quickView.buildItem(68);
        quickView.simpleAdd(two);
        Assert.assertEquals(quickView.size(),2);

    }

    /**
     * model object is integer
     */
    @Test(groups = {"wicketTests"})
    public void newItem_1(){
        final  int object=89;
        final Model<Integer>model=new Model<Integer>(object);
        IDataProvider<Integer> data=Mockito.mock(IDataProvider.class);
        Mockito.when(data.model(object)).thenReturn(model);
        QuickView<Integer> quickView=new QuickView<Integer>("id",data,ReUse.ITEMSNAVIGATION) {
            @Override
            protected void populate(Item<Integer> item) {
            }
        } ;
        final int index=9;
        final String id="67";
       Item <Integer>item= quickView.newItem(id, index, object);
        Assert.assertEquals(item.getModelObject().intValue(), 89);
        Assert.assertEquals(item.getMarkupId(), id);
        Assert.assertEquals(item.getIndex(),index);
        Assert.assertTrue(item.getOutputMarkupId());
    }

    /**
     * modelobject is string
     */
    @Test(groups = {"wicketTests"})
    public void newItem_2(){
        final  String object="theobject";
        IDataProvider data=Mockito.mock(IDataProvider.class);
        Model<String>model=new Model<String>(object);
        Mockito.when(data.model(object)).thenReturn(model);
        QuickView<String> quickView=new QuickView<String>("id",data,ReUse.ITEMSNAVIGATION) {
            @Override
            protected void populate(Item<String> item) {
            }
        } ;
        final int index=9;
        final String id="345";
        Item <String>item= quickView.newItem(id,index,object);
        Assert.assertEquals(item.getModelObject(),object);
        Assert.assertEquals(item.getMarkupId(),id);
        Assert.assertTrue(item.getOutputMarkupId());
        Assert.assertEquals(item.getIndex(),index);

    }

    /**
     * test for  {@link QuickViewBase#buildItem(Object)}
     */
    @Test(groups = {"wicketTests"})
    public void buildItem_1(){
        IDataProvider data=Mockito.mock(IDataProvider.class);
        final Item item=Mockito.mock(Item.class);
        QuickView<TestObj> quickView=new QuickView<TestObj>("id",data,ReUse.ITEMSNAVIGATION) {
            @Override
            protected void populate(Item item) {
            }

            @Override
            protected Item newItem(String id,int index, TestObj object) {
                return item;
            }
        } ;
      //  final String id="9";
       final TestObj object=Mockito.mock(TestObj.class);
        QuickView<TestObj>spy=Mockito.spy(quickView);
        Item <TestObj>actual= spy.buildItem(object);
        Assert.assertEquals(actual,item);
       InOrder order= Mockito.inOrder(spy, item);
         order.verify(spy, Mockito.times(1)).newItem("1", 1, object);
         order.verify(spy, Mockito.times(1)).populate(item);
     }


    /**
     * test for  {@link QuickViewBase#buildItem(object)}
     */
    @Test(groups = {"wicketTests"})
    public void buildItem_2(){
        IDataProvider data=Mockito.mock(IDataProvider.class);
        final Item item=Mockito.mock(Item.class);
         final String childId="78";
        QuickView<TestObj> quickView=new QuickView<TestObj>("id",data,ReUse.ITEMSNAVIGATION) {
            @Override
            protected void populate(Item<TestObj> item) {}

            @Override
            public Item<TestObj> buildItem(String id, int index, TestObj object) {
                return item;
            }

            @Override
            public String newChildId() {
                return childId;
            }

            @Override
            public int getChildId() {
                return Integer.parseInt(childId);
            }
        } ;
       QuickView<TestObj>spy= Mockito.spy(quickView);
        TestObj obj=Mockito.mock(TestObj.class);
        spy.buildItem(obj);
        Mockito.verify(spy).buildItem(childId,78,obj);

    }
    @Test(groups = {"wicketTests"})
    public void addItemsForNextPage_1() {
        final int dataProviderSize = 12;
        final int current=5,next=6 ,pages=7;
        IDataProvider dataProvider=Mockito.mock(IDataProvider.class);
        Mockito.when(dataProvider.size()).thenReturn(dataProviderSize);
        final AjaxRequestTarget target=Mockito.mock(AjaxRequestTarget.class);
        final List<Item> items=Mockito.mock(List.class);
        QuickView quickview=new QuickView("quick",dataProvider,ReUse.ITEMSNAVIGATION) {
            @Override
            protected void populate(Item item) {
            }

            @Override
            protected int _getPageCount() {
                return pages;
            }

            @Override
            protected int _getCurrentPage() {
                return current;
            }

            @Override
            public AjaxRequestTarget getAjaxRequestTarget() {
                return  target;
            }

            @Override
            public List addItemsForPage(int page) {
                        return items;
            }
        };

        QuickView spy= Mockito.spy(quickview);
        List<Item>actual= spy.addItemsForNextPage();
        Mockito.verify(spy,Mockito.times(1))._setCurrentPage(next);
        Mockito.verify(spy,Mockito.times(1)).addItemsForPage(next);
        Assert.assertEquals(actual, items);
    }

    /**
     * when current page= pages count
     *
     */
    @Test(groups = {"utilTests"})
    public void addItemsForNextPage_2() {

        final int dataProviderSize = 12;
        final int current=5,next=6 ,pages=6;
        IDataProvider dataProvider=Mockito.mock(IDataProvider.class);
        Mockito.when(dataProvider.size()).thenReturn(dataProviderSize);
        final AjaxRequestTarget target=Mockito.mock(AjaxRequestTarget.class);
        final List<Item> items=Mockito.mock(List.class);
        QuickView quickview=new QuickView("quick",dataProvider,ReUse.ITEMSNAVIGATION) {
            @Override
            protected void populate(Item item) {
            }

            @Override
            protected int _getPageCount() {
                return pages;
            }

            @Override
            protected int  _getCurrentPage() {
                return current;
            }
            @Override
            public AjaxRequestTarget getAjaxRequestTarget() {
                return  target;
            }

            @Override
            public List addItemsForPage(int page) {
                return items;
            }
        };
        QuickView spy= Mockito.spy(quickview);
        List<Item>actual= spy.addItemsForNextPage();
        Mockito.verify(spy,Mockito.never())._setCurrentPage(next);
        Mockito.verify(spy,Mockito.never()).addItemsForPage(next);
        Assert.assertTrue(actual.isEmpty());
    }

    /**
     * when current page> pages count
     *
     */
    @Test(groups = {"utilTests"})
    public void addItemsForNextPage_3() {
        final int dataProviderSize = 12;
        final int current=7,next=6 ,pages=6;
        final AjaxRequestTarget target=Mockito.mock(AjaxRequestTarget.class);
        final List<Item> items=Mockito.mock(List.class);
        IDataProvider dataProvider=Mockito.mock(IDataProvider.class);
        Mockito.when(dataProvider.size()).thenReturn(dataProviderSize);
        QuickView quickview=new QuickView("quick",dataProvider,ReUse.ITEMSNAVIGATION) {
            @Override
            protected void populate(Item item) {
            }

            @Override
            protected int  _getPageCount() {
                return pages;
            }

            @Override
            protected int _getCurrentPage() {
                return current;
            }

            @Override
            public AjaxRequestTarget getAjaxRequestTarget() {
                return  target;
            }

            @Override
            public List addItemsForPage(int page) {
                return items;
            }
        };
        QuickView spy= Mockito.spy(quickview);
        List<Item>actual= spy.addItemsForNextPage();
        Mockito.verify(spy,Mockito.never())._setCurrentPage(next);
        Mockito.verify(spy,Mockito.never()).addItemsForPage(next);
        Assert.assertTrue(actual.isEmpty());
    }

    /**
     * page=2 ,itemsperrequest=2 ,reuse=ReUse.ITEMSNAVIGATION
     */

    @Test(groups = {"wicketTests"})
    public void addItemsForPage_1() {
        int itemsPerRequest = 2;
        IDataProvider dataProvider = Mockito.mock(IDataProvider.class);
         Iterator data = mockIterator();
        Mockito.when(dataProvider.iterator(4, itemsPerRequest)).thenReturn(data);
        Mockito.when(data.hasNext()).thenReturn(true).thenReturn(true).thenReturn(false);

        Item item1=new Item("1",1,new Model(1));
        Item item2=new Item("2",2,new Model(2));
         List<Item> list=new ArrayList();
          list.add(item1);
        list.add(item2);
        final Iterator newIterator=list.iterator();
        QuickView repeater = new QuickView("repeater", dataProvider, ReUse.ITEMSNAVIGATION, itemsPerRequest) {

            public void populate(Item item) {
            }

            @Override
            public void simpleRemoveAllIfNotReuse() {
            }

            @Override
            public MarkupContainer add(Component... c) {
                return this;
            }

            @Override
            protected Iterator buildItems(int index, Iterator iterator) {
                return newIterator;
            }
        };
        QuickView spy = Mockito.spy(repeater);
        List<Item<TestObj>> items = spy.addItemsForPage(2);
        Assert.assertEquals(items.size(),list.size());
        Mockito.verify(dataProvider).iterator(4,itemsPerRequest);
        Mockito.verify(spy,Mockito.times(1)).buildItems(4,data);
        Mockito.verify(spy, Mockito.times(1)).add(items.get(0));
        Mockito.verify(spy, Mockito.times(1)).add(items.get(1));

    }

    /**
     * page=2 ,itemsperrequest=2 ,reuse=ReUse.ALL
     */

    @Test(groups = {"wicketTests"})
    public void addItemsForPage_2() {
        int itemsPerRequest = 2;
        IDataProvider dataProvider = Mockito.mock(IDataProvider.class);
        Iterator data = mockIterator();
        Mockito.when(dataProvider.iterator(4, itemsPerRequest)).thenReturn(data);
        Mockito.when(data.hasNext()).thenReturn(true).thenReturn(true).thenReturn(false);
        Item item1=new Item("1",1,new Model(1));
        Item item2=new Item("2",2,new Model(2));
        List<Item> list=new ArrayList();
        list.add(item1);
        list.add(item2);
        final Iterator newIterator=list.iterator();
        QuickView repeater = new QuickView("repeater", dataProvider, ReUse.ALL, itemsPerRequest) {

            public void populate(Item item) {
            }

            @Override
            public void simpleRemoveAllIfNotReuse() {
            }

            @Override
            public MarkupContainer add(Component... c) {
                return this;
            }

            @Override
            protected Iterator buildItems(int index, Iterator iterator) {
                return newIterator;
            }
        };
        QuickView spy = Mockito.spy(repeater);
        List<Item<TestObj>> items = spy.addItemsForPage(2);
        Assert.assertEquals(items.size(),list.size());
        Mockito.verify(dataProvider).iterator(4,itemsPerRequest);
        Mockito.verify(spy,Mockito.times(1)).buildItems(4,data);
        Mockito.verify(spy, Mockito.times(1)).add(items.get(0));
        Mockito.verify(spy, Mockito.times(1)).add(items.get(1));

    }

    /**
     * development
     */
    @Test(groups = {"wicketTests"})
    public void jqueryResourceReference_1(){
        WebApplication app=new MockApplication(){
            @Override
            public RuntimeConfigurationType getConfigurationType() {
                return RuntimeConfigurationType.DEVELOPMENT;
            }
        };

        WicketTester tester=new WicketTester(app) ;
        List<Integer>data=data(1);
        WebMarkupContainer parent=new WebMarkupContainer("parent");
        parent.setOutputMarkupId(true);
        IDataProvider dataProvider = new ListDataProvider(data);
        QuickView repeater = new QuickView("repeater", dataProvider, ReUse.ALL, 1) {
            @Override
            protected void populate(Item item) {
            }
        };
        parent.add(repeater);
        parent.internalInitialize();
        parent.beforeRender();
       ResourceReference actual= repeater.jqueryReference();
        Assert.assertEquals(actual,JqueryResourceReference.get());
    }

    /**
     * deployment
     */
    @Test(groups = {"wicketTests"})
    public void jqueryResourceReference_2(){
        WebApplication app=new MockApplication(){
            @Override
            public RuntimeConfigurationType getConfigurationType() {
                return RuntimeConfigurationType.DEPLOYMENT;
            }
        };

        WicketTester tester=new WicketTester(app) ;
        List<Integer>data=data(1);
        WebMarkupContainer parent=new WebMarkupContainer("parent");
        parent.setOutputMarkupId(true);
        IDataProvider dataProvider = new ListDataProvider(data);
        QuickView repeater = new QuickView("repeater", dataProvider, ReUse.ALL, 1) {
            @Override
            protected void populate(Item item) {
            }
        };
        parent.add(repeater);
        parent.internalInitialize();
        parent.beforeRender();
        ResourceReference actual= repeater.jqueryReference();
        Assert.assertEquals(actual,JqueryCompressedReference.get());
    }

    public AjaxRequestTarget mockTarget() {
        AjaxRequestTarget target = mock(AjaxRequestTarget.class);
        return target;
    }

    public Iterator<TestObj> mockIterator() {
        Iterator<TestObj> it = mock(Iterator.class);
        return it;
    }
    List<Integer>data(int size){
        List<Integer>list=new ArrayList<Integer>();
        for(int i=0;i<size;i++){
            list.add(i) ;
        }
        return list;
    }
    public IDataProvider<TestObj> mockProvider(int size) {
        IDataProvider<TestObj> dp = mock(IDataProvider.class);
        when(dp.size()).thenReturn(size);
        return dp;
    }

     IRepeaterUtil mockRepeaterUtil(){
         return Mockito.mock(IRepeaterUtil.class);
     }

   private  List<Item<Integer>>items=new ArrayList<Item<Integer>>();



}
