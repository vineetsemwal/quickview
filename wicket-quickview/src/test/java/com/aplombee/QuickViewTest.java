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
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.model.Model;
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
        QuickView<TestObj> repeater = new QuickView<TestObj>(repeaterId, provider, ReUse.DEFAULT_ITEMSNAVIGATION, oneBlock) {

            @Override
            protected void populate(Item<TestObj> item) {
            }

        };
        repeater.setMarkupId("con");
        Assert.assertEquals(ReUse.DEFAULT_ITEMSNAVIGATION, repeater.getReuse());
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

        repeater.setReuse(ReUse.DEFAULT_ITEMSNAVIGATION);
        Assert.assertEquals(repeater.getReuse(), ReUse.DEFAULT_ITEMSNAVIGATION);
    }


    /**
     * add one component ,isajax=true,isParentadded=false
     */
    @Test(groups = {"wicketTests"})
    public void add_1() {
        items.clear();
        final int itemsPerRequest=1;
        final TestComponentPanel.Parent parent = new TestComponentPanel.Parent(TestComponentPanel.parentId);
        parent.setOutputMarkupId(true);
        final IDataProvider dataProvider =  new ListDataProvider(data(10));
        QuickMockApplication app=new QuickMockApplication();
        WicketTester tester=new WicketTester(app);
        final QuickView<Integer> quickView = new QuickView<Integer>(TestComponentPanel.quickViewId, dataProvider,itemsPerRequest) {

            public void populate(Item<Integer> item) {
            }
        };
        final AjaxLink link=new AjaxLink(TestComponentPanel.ajaxLinkId) {
            @Override
            public void onClick(AjaxRequestTarget target) {
                Iterator<Integer> it=  dataProvider.iterator(1,itemsPerRequest);
                while(it.hasNext()){
                    int object=it.next();
                    Item<Integer> item=quickView.buildItem(quickView.newChildId(), object);
                    items.add(item);
                    quickView.add(item);
                }
            }
        };
        parent.add(quickView);
        quickView.setReuse(ReUse.DEFAULT_ITEMSNAVIGATION);

        TestComponentPanel panel=new TestComponentPanel("panel"){
            @Override
            public Parent newParent() {
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
        Assert.assertEquals(Integer.parseInt(actualItem1.getMarkupId()),1);
        Assert.assertEquals(actualItem1.getModelObject().intValue(),1);

    }


    /**
     * add one component ,isajax=false,isParentadded=false
     */
    @Test(groups = {"wicketTests"})
    public void add_2() {
        items.clear();
        final int itemsPerRequest=2;

        final TestComponentPanel.Parent parent = new TestComponentPanel.Parent(TestComponentPanel.parentId);
        parent.setOutputMarkupId(true);
        final IDataProvider dataProvider =  new ListDataProvider(data(10));
        QuickMockApplication app=new QuickMockApplication();
        WicketTester tester=new WicketTester(app);
        final QuickView<Integer> quickView = new QuickView<Integer>(TestComponentPanel.quickViewId, dataProvider,itemsPerRequest) {

            public void populate(Item<Integer> item) {
            }
        };
        final AjaxLink link=new AjaxLink(TestComponentPanel.ajaxLinkId) {
            @Override
            public void onClick(AjaxRequestTarget target) {
                Iterator<Integer> it=  dataProvider.iterator(2,itemsPerRequest);
                while(it.hasNext()){
                    int object=it.next();
                    Item<Integer> item=quickView.buildItem(quickView.newChildId(), object);
                    items.add(item);
                    quickView.add(item);
                }
            }
        };
        parent.add(quickView);
        quickView.setReuse(ReUse.DEFAULT_ITEMSNAVIGATION);

        TestComponentPanel panel=new TestComponentPanel("panel"){
            @Override
            public Parent newParent() {
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
        Assert.assertEquals(Integer.parseInt(actualItem1.getMarkupId()),2);
        Assert.assertEquals(actualItem1.getModelObject().intValue(),2);


        //asserting seconditem
        Assert.assertTrue(target.getComponents().contains(actualItem2));
        Assert.assertEquals(Integer.parseInt(actualItem2.getMarkupId()),3);
        Assert.assertEquals(actualItem2.getModelObject().intValue(),3);
    }


    /**
     * one component added ,ajax=false ,parent added=false
     */
    @Test(groups = {"wicketTests"})
    public void add_3() {
        items.clear();
        final int itemsPerRequest=1;

        final TestComponentPanel.Parent parent = new TestComponentPanel.Parent(TestComponentPanel.parentId);
        parent.setOutputMarkupId(true);
        final IDataProvider dataProvider =  new ListDataProvider(data(10));
        QuickMockApplication app=new QuickMockApplication();
        WicketTester tester=new WicketTester(app);
        final QuickView<Integer> quickView = new QuickView<Integer>(TestComponentPanel.quickViewId, dataProvider,itemsPerRequest) {

            public void populate(Item<Integer> item) {
            }
        };
        final Link link=new Link(TestComponentPanel.ajaxLinkId) {
            @Override
            public void onClick() {
                Iterator<Integer> it=  dataProvider.iterator(1,itemsPerRequest);
                while(it.hasNext()){
                    int object=it.next();
                    Item<Integer> item=quickView.buildItem(quickView.newChildId(), object);
                    items.add(item);
                    quickView.add(item);
                }
            }
        };
        parent.add(quickView);
        //reuse all is intentionally set because quickview is rendered again and any other strategy would not retail all  which we need to check for quickview.size()
        quickView.setReuse(ReUse.ALL);

        TestComponentPanel panel=new TestComponentPanel("panel"){
            @Override
            public Parent newParent() {
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
        Assert.assertEquals(Integer.parseInt(actualItem1.getMarkupId()),1);
        Assert.assertEquals(actualItem1.getModelObject().intValue(),1);
    }


    /**
     * one component added ,ajax=true,parent added=true
     */
    @Test(groups = {"wicketTests"})
    public void add_4() {
        items.clear();
        final int itemsPerRequest=1;
        final TestComponentPanel.Parent parent = new TestComponentPanel.Parent(TestComponentPanel.parentId);
        parent.setOutputMarkupId(true);
        final IDataProvider dataProvider =  new ListDataProvider(data(10));
        QuickMockApplication app=new QuickMockApplication();
        WicketTester tester=new WicketTester(app);
        final QuickView<Integer> quickView = new QuickView<Integer>(TestComponentPanel.quickViewId, dataProvider,itemsPerRequest) {

            public void populate(Item<Integer> item) {
            }
        };
        final AjaxLink link=new AjaxLink(TestComponentPanel.ajaxLinkId) {
            @Override
            public void onClick(AjaxRequestTarget target) {
                Iterator<Integer> it=  dataProvider.iterator(1,itemsPerRequest);
                while(it.hasNext()){
                    int object=it.next();
                    Item<Integer> item=quickView.buildItem(quickView.newChildId(), object);
                    items.add(item);
                    target.add(parent);
                    quickView.add(item);

                }
            }
        };
        parent.add(quickView);
        //reuse all is intentionally set because quickview is rendered again and any other strategy would not retail all which we need to check for quickview.size()
        quickView.setReuse(ReUse.ALL);

        TestComponentPanel panel=new TestComponentPanel("panel"){
            @Override
            public Parent newParent() {
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
        Assert.assertEquals(Integer.parseInt(actualItem1.getMarkupId()),1);
        Assert.assertEquals(actualItem1.getModelObject().intValue(),1);

    }


    /**
     * one component added ,ajax=true ,parent added=false
     */
    @Test(groups = {"wicketTests"})
    public void addAtStart_1() {
        items.clear();
        final int itemsPerRequest=1;
        final TestComponentPanel.Parent parent = new TestComponentPanel.Parent(TestComponentPanel.parentId);
        parent.setOutputMarkupId(true);
        final IDataProvider dataProvider =  new ListDataProvider(data(10));
        QuickMockApplication app=new QuickMockApplication();
        WicketTester tester=new WicketTester(app);
        final QuickView<Integer> quickView = new QuickView<Integer>(TestComponentPanel.quickViewId, dataProvider,itemsPerRequest) {

            public void populate(Item<Integer> item) {
            }
        };
        final AjaxLink link=new AjaxLink(TestComponentPanel.ajaxLinkId) {
            @Override
            public void onClick(AjaxRequestTarget target) {
                Iterator<Integer> it=  dataProvider.iterator(1,itemsPerRequest);
                while(it.hasNext()){
                    int object=it.next();
                    Item<Integer> item=quickView.buildItem(quickView.newChildId(), object);
                    items.add(item);
                    quickView.addAtStart(item);
                }
            }
        };
        parent.add(quickView);
        quickView.setReuse(ReUse.DEFAULT_ITEMSNAVIGATION);

        TestComponentPanel panel=new TestComponentPanel("panel"){
            @Override
            public Parent newParent() {
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
        Assert.assertEquals(Integer.parseInt(actualItem1.getMarkupId()),1);
        Assert.assertEquals(actualItem1.getModelObject().intValue(),1);

    }

    /**
     * 2 components added ,ajax=true ,parent added=false
     */
    @Test(groups = {"wicketTests"})
    public void addAtStart_2() {
        items.clear();
        final int itemsPerRequest=2;

        final TestComponentPanel.Parent parent = new TestComponentPanel.Parent(TestComponentPanel.parentId);
        parent.setOutputMarkupId(true);
        final IDataProvider dataProvider =  new ListDataProvider(data(10));
        QuickMockApplication app=new QuickMockApplication();
        WicketTester tester=new WicketTester(app);
        final QuickView<Integer> quickView = new QuickView<Integer>(TestComponentPanel.quickViewId, dataProvider,itemsPerRequest) {

            public void populate(Item<Integer> item) {
            }
        };
        final AjaxLink link=new AjaxLink(TestComponentPanel.ajaxLinkId) {
            @Override
            public void onClick(AjaxRequestTarget target) {
                Iterator<Integer> it=  dataProvider.iterator(2,itemsPerRequest);
                while(it.hasNext()){
                    int object=it.next();
                    Item<Integer> item=quickView.buildItem(quickView.newChildId(), object);
                    items.add(item);
                    quickView.addAtStart(item);
                }
            }
        };
        parent.add(quickView);
        quickView.setReuse(ReUse.DEFAULT_ITEMSNAVIGATION);

        TestComponentPanel panel=new TestComponentPanel("panel"){
            @Override
            public Parent newParent() {
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
        Assert.assertEquals(Integer.parseInt(actualItem1.getMarkupId()),2);
        Assert.assertEquals(actualItem1.getModelObject().intValue(),2);


        //asserting seconditem
        Assert.assertTrue(target.getComponents().contains(actualItem2));
        Assert.assertEquals(Integer.parseInt(actualItem2.getMarkupId()),3);
        Assert.assertEquals(actualItem2.getModelObject().intValue(),3);
    }

    /**
     * one component added ,ajax=false ,parent added=false
     */
    @Test(groups = {"wicketTests"})
    public void addAtStart_3() {
        items.clear();
        final int itemsPerRequest=1;

        final TestComponentPanel.Parent parent = new TestComponentPanel.Parent(TestComponentPanel.parentId);
        parent.setOutputMarkupId(true);
        final IDataProvider dataProvider =  new ListDataProvider(data(10));
        QuickMockApplication app=new QuickMockApplication();
        WicketTester tester=new WicketTester(app);
        final QuickView<Integer> quickView = new QuickView<Integer>(TestComponentPanel.quickViewId, dataProvider,itemsPerRequest) {

            public void populate(Item<Integer> item) {
            }
        };
        final Link link=new Link(TestComponentPanel.ajaxLinkId) {
            @Override
            public void onClick() {
                Iterator<Integer> it=  dataProvider.iterator(1,itemsPerRequest);
                while(it.hasNext()){
                    int object=it.next();
                    Item<Integer> item=quickView.buildItem(quickView.newChildId(), object);
                    items.add(item);
                    quickView.addAtStart(item);
                }
            }
        };
        parent.add(quickView);
        //reuse all is intentionally set because quickview is rendered again and any other strategy would not retail all  which we need to check for quickview.size()
        quickView.setReuse(ReUse.ALL);

        TestComponentPanel panel=new TestComponentPanel("panel"){
            @Override
            public Parent newParent() {
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
        Assert.assertEquals(Integer.parseInt(actualItem1.getMarkupId()),1);
        Assert.assertEquals(actualItem1.getModelObject().intValue(),1);
    }

    /**
     * one component added ,ajax=true,parent added=true
     */
    @Test(groups = {"wicketTests"})
    public void addAtStart_4() {
        items.clear();
        final int itemsPerRequest=1;
        final TestComponentPanel.Parent parent = new TestComponentPanel.Parent(TestComponentPanel.parentId);
        parent.setOutputMarkupId(true);
        final IDataProvider dataProvider =  new ListDataProvider(data(10));
        QuickMockApplication app=new QuickMockApplication();
        WicketTester tester=new WicketTester(app);
        final QuickView<Integer> quickView = new QuickView<Integer>(TestComponentPanel.quickViewId, dataProvider,itemsPerRequest) {

            public void populate(Item<Integer> item) {
            }
        };
        final AjaxLink link=new AjaxLink(TestComponentPanel.ajaxLinkId) {
            @Override
            public void onClick(AjaxRequestTarget target) {
                Iterator<Integer> it=  dataProvider.iterator(1,itemsPerRequest);
                while(it.hasNext()){
                    int object=it.next();
                    Item<Integer> item=quickView.buildItem(quickView.newChildId(), object);
                    items.add(item);
                    target.add(parent);
                    quickView.addAtStart(item);

                }
            }
        };
        parent.add(quickView);
        //reuse all is intentionally set because quickview is rendered again and any other strategy would not retail all which we need to check for quickview.size()
        quickView.setReuse(ReUse.ALL);

        TestComponentPanel panel=new TestComponentPanel("panel"){
            @Override
            public Parent newParent() {
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
        Assert.assertEquals(Integer.parseInt(actualItem1.getMarkupId()),1);
        Assert.assertEquals(actualItem1.getModelObject().intValue(),1);

    }




    @Test(groups = {"wicketTests"})
    public void addComponentsFromIndex_1() {
        int itemsPerRequest = 2;
        final WebMarkupContainer parent = Mockito.mock(WebMarkupContainer.class);
        IDataProvider dataProvider = Mockito.mock(IDataProvider.class);
        final TestObj to = Mockito.mock(TestObj.class);
        final AjaxRequestTarget target = Mockito.mock(AjaxRequestTarget.class);
        // final long page=2l;
        Iterator it = mockIterator();
        // long start=itemsPerRequest*page;
        Mockito.when(dataProvider.iterator(4, itemsPerRequest)).thenReturn(it);
        Mockito.when(it.hasNext()).thenReturn(true).thenReturn(true).thenReturn(false);
        QuickView<TestObj> repeater = new QuickView<TestObj>("repeater", dataProvider, ReUse.DEFAULT_ITEMSNAVIGATION, itemsPerRequest) {

            public void populate(Item<TestObj> item) {
            }

            @Override
            public Item buildItem(int id, TestObj object) {
                Item item = Mockito.mock(Item.class);
                Mockito.when(item.getMarkupId()).thenReturn(String.valueOf(id));
                // Mockito.when(item.getIndex()).thenReturn(id);
                return item;
            }

            @Override
            public void simpleRemoveAllIfNotReuse() {
            }

            @Override
            public MarkupContainer add(Component... c) {
                return this;
            }
        };
        QuickView spy = Mockito.spy(repeater);
        List<Item<TestObj>> items = spy.addComponentsFromIndex(4);
        Mockito.verify(spy, Mockito.times(1)).simpleRemoveAllIfNotReuse();
        Assert.assertEquals(items.size(), 2);
        Mockito.verify(spy, Mockito.times(1)).add(items.get(0));
        Mockito.verify(spy, Mockito.times(1)).add(items.get(1));
        Assert.assertEquals(Integer.valueOf(items.get(0).getMarkupId()).intValue(), 4);
        Assert.assertEquals(Integer.valueOf(items.get(1).getMarkupId()).intValue(), 5);
    }

    /**
     * add one component ,itemsPerRequest=2
     */

    @Test(groups = {"wicketTests"})
    public void addComponentsFromIndex_2() {
        final int itemsPerRequest = 2;
        final WebMarkupContainer parent = Mockito.mock(WebMarkupContainer.class);
        IDataProvider dataProvider = Mockito.mock(IDataProvider.class);
        final TestObj to = Mockito.mock(TestObj.class);
        final AjaxRequestTarget target = Mockito.mock(AjaxRequestTarget.class);

        Iterator it = mockIterator();
        int index = 3;
        Mockito.when(dataProvider.iterator(index, itemsPerRequest)).thenReturn(it);
        Mockito.when(it.hasNext()).thenReturn(true).thenReturn(false);
        QuickView<TestObj> repeater = new QuickView<TestObj>("repeater", dataProvider, itemsPerRequest) {

            public void populate(Item<TestObj> item) {
            }

            @Override
            public Item buildItem(int id, TestObj object) {
                Item item = Mockito.mock(Item.class);
                Mockito.when(item.getMarkupId()).thenReturn(String.valueOf(id));
                return item;
            }

            @Override
            public void simpleRemoveAllIfNotReuse() {
            }

            @Override
            public MarkupContainer add(Component... c) {
                return this;
            }
        };
        QuickView spy = Mockito.spy(repeater);
        List<Item<TestObj>> items = spy.addComponentsFromIndex(index);

        Mockito.verify(spy, Mockito.times(1)).simpleRemoveAllIfNotReuse();
        Assert.assertEquals(items.size(), 1);
        Mockito.verify(spy, Mockito.times(1)).add(items.get(0));
        Assert.assertEquals(Integer.valueOf(items.get(0).getMarkupId()).intValue(), 3);
    }


    /**
     * itemsPerRequest=2    ,page=2
     */

    @Test(groups = {"wicketTests"})
    public void addComponentForPage_1() {
        int itemsPerRequest = 2;
        final WebMarkupContainer parent = Mockito.mock(WebMarkupContainer.class);
        IDataProvider dataProvider = Mockito.mock(IDataProvider.class);
        final TestObj to = Mockito.mock(TestObj.class);
        final AjaxRequestTarget target = Mockito.mock(AjaxRequestTarget.class);
        final int page = 2;
        Iterator it = mockIterator();
        int start = itemsPerRequest * page;
        Mockito.when(dataProvider.iterator(start, itemsPerRequest)).thenReturn(it);
        Mockito.when(it.hasNext()).thenReturn(true).thenReturn(true).thenReturn(false);
        QuickView<TestObj> repeater = new QuickView<TestObj>("repeater", dataProvider, ReUse.DEFAULT_ITEMSNAVIGATION, itemsPerRequest) {

            public void populate(Item<TestObj> item) {
            }

            @Override
            public Item buildItem(int id, TestObj object) {
                Item item = Mockito.mock(Item.class);
                Mockito.when(item.getMarkupId()).thenReturn(String.valueOf(id));
                // Mockito.when(item.getIndex()).thenReturn(id);
                return item;
            }

            @Override
            public void simpleRemoveAllIfNotReuse() {
            }

            @Override
            public MarkupContainer add(Component... c) {
                return this;
            }

            @Override
            public List<Item<TestObj>> addComponentsFromIndex(int index) {
                return null;
            }
        };
        QuickView spy = Mockito.spy(repeater);
        List<Item<TestObj>> items = spy.addComponentsForPage(page);
        Mockito.verify(spy).addComponentsFromIndex(start);

    }
               //dataprovider.size >itemsPerrequest

    @Test(groups = {"wicketTests"})
    public void remove_1() {
        items.clear();
        final int itemsPerRequest=1;
        final TestComponentPanel.Parent parent = new TestComponentPanel.Parent(TestComponentPanel.parentId);
        parent.setOutputMarkupId(true);
        final IDataProvider dataProvider =  new ListDataProvider(data(10));
        QuickMockApplication app=new QuickMockApplication();
        WicketTester tester=new WicketTester(app);
        final QuickView<Integer> quickView = new QuickView<Integer>(TestComponentPanel.quickViewId, dataProvider,itemsPerRequest) {

            public void populate(Item<Integer> item) {
            }
        };
        final AjaxLink link=new AjaxLink(TestComponentPanel.ajaxLinkId) {
            @Override
            public void onClick(AjaxRequestTarget target) {
                     Item<Integer>item=quickView.getItem(0);
                items.add(item);
                    quickView.remove(item);
            }
        };
        parent.add(quickView);
        quickView.setReuse(ReUse.DEFAULT_ITEMSNAVIGATION);

        TestComponentPanel panel=new TestComponentPanel("panel"){
            @Override
            public Parent newParent() {
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

    @Test(groups = {"wicketTests"})
    public void createChildren_1() {
        int itemsPerRequest = 3;
        IDataProvider provider = Mockito.mock(IDataProvider.class);
        Mockito.when(provider.size()).thenReturn(10);
        Iterator it = mockIterator();
        Mockito.when(provider.iterator(0, itemsPerRequest)).thenReturn(it);
        Mockito.when(it.hasNext()).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(false);
        final List<Item> list = new ArrayList<Item>();

        QuickView<TestObj> arc = new QuickView<TestObj>("repeater", provider, itemsPerRequest) {
            @Override
            protected void populate(Item<TestObj> item) {
            }

            @Override
            public MarkupContainer simpleAdd(Component... c) {
                return this;
            }

            @Override
            public Item buildItem(int id, TestObj object) {
                Item i = Mockito.mock(Item.class);
                Mockito.when(i.getMarkupId()).thenReturn(String.valueOf(id));
                list.add(i);
                return i;
            }
        };

        QuickView spy = Mockito.spy(arc);
        spy.createChildren(0);

        Mockito.verify(spy, Mockito.times(3)).simpleAdd(Mockito.any(Item.class));
        Mockito.verify(spy, Mockito.times(1)).simpleAdd(list.get(0));
        Assert.assertEquals(Long.parseLong(list.get(0).getMarkupId()), 0l);
        Mockito.verify(spy, Mockito.times(1)).simpleAdd(list.get(1));
        Assert.assertEquals(Long.parseLong(list.get(1).getMarkupId()), 1l);
        Mockito.verify(spy, Mockito.times(1)).simpleAdd(list.get(2));
        Assert.assertEquals(Long.parseLong(list.get(2).getMarkupId()), 2l);
    }

    //dataprovider.size()< itemsPerRequest
    @Test(groups = {"wicketTests"})
    public void createChildren_2() {
        int itemsPerRequest = 3;

        IDataProvider provider = Mockito.mock(IDataProvider.class);
        Mockito.when(provider.size()).thenReturn(2);
        Iterator it = mockIterator();
        Mockito.when(provider.iterator(0, 3)).thenReturn(it);
        Mockito.when(it.hasNext()).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(false);
        final List<Item> list = new ArrayList<Item>();

        QuickView<TestObj> arc = new QuickView<TestObj>("repeater", provider, itemsPerRequest) {
            @Override
            protected void populate(Item<TestObj> item) {
            }

            @Override
            public MarkupContainer simpleAdd(Component... c) {
                return this;
            }

            @Override
            public Item buildItem(int id, TestObj object) {
                Item i = Mockito.mock(Item.class);
                Mockito.when(i.getMarkupId()).thenReturn(String.valueOf(id));
                list.add(i);
                return i;
            }
        };

        QuickView spy = Mockito.spy(arc);
        spy.createChildren(0);

        Mockito.verify(spy, Mockito.times(3)).simpleAdd(Mockito.any(Item.class));
        Mockito.verify(spy, Mockito.times(1)).simpleAdd(list.get(0));
        Assert.assertEquals(Long.parseLong(list.get(0).getMarkupId()), 0l);
        Mockito.verify(spy, Mockito.times(1)).simpleAdd(list.get(1));
        Assert.assertEquals(Long.parseLong(list.get(1).getMarkupId()), 1l);
        Mockito.verify(spy, Mockito.times(1)).simpleAdd(list.get(2));
        Assert.assertEquals(Long.parseLong(list.get(2).getMarkupId()), 2l);
    }


    /*
     * current page=2 ,iterator can iterate 2 times ,itemsperrequest=3
     *                       data.size>itemsperrequest
     */

    @Test(groups = {"wicketTests"})
    public void createChildren_3() {
        final int itemsPerRequest = 3;
        IDataProvider provider = Mockito.mock(IDataProvider.class);
        Mockito.when(provider.size()).thenReturn(10);
        Iterator it = mockIterator();
        Mockito.when(provider.iterator(2 * 3, itemsPerRequest)).thenReturn(it);
        Mockito.when(it.hasNext()).thenReturn(true).thenReturn(true).thenReturn(false);
        final List<Item> list = new ArrayList<Item>();

        QuickView<TestObj> arc = new QuickView<TestObj>("repeater", provider, itemsPerRequest) {
            @Override
            protected void populate(Item<TestObj> item) {
            }

            @Override
            public MarkupContainer simpleAdd(Component... c) {
                return this;
            }

            @Override
            public Item buildItem(int id, TestObj object) {
                Item i = Mockito.mock(Item.class);
                Mockito.when(i.getMarkupId()).thenReturn(String.valueOf(id));
                list.add(i);
                return i;
            }
        };

        QuickView spy = Mockito.spy(arc);
        spy.createChildren(2);

        Mockito.verify(spy, Mockito.times(2)).simpleAdd(Mockito.any(Item.class));
        Mockito.verify(spy, Mockito.times(1)).simpleAdd(list.get(0));
        Assert.assertEquals(Long.parseLong(list.get(0).getMarkupId()), 6l);
        Mockito.verify(spy, Mockito.times(1)).simpleAdd(list.get(1));
        Assert.assertEquals(Long.parseLong(list.get(1).getMarkupId()), 7l);

    }

    /**
     * items per request not set ,iterator iterates 3 times
     */
    @Test(groups = {"wicketTests"})
    public void createChildren_4() {
        //int itemsPerRequest = 3;
        IDataProvider provider = Mockito.mock(IDataProvider.class);
        Mockito.when(provider.size()).thenReturn(10);
        Iterator it = mockIterator();
        Mockito.when(provider.iterator(0,Integer.MAX_VALUE)).thenReturn(it);
        Mockito.when(it.hasNext()).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(false);
        final List<Item> list = new ArrayList<Item>();

        QuickView<TestObj> arc = new QuickView<TestObj>("repeater", provider) {
            @Override
            protected void populate(Item<TestObj> item) {
            }

            @Override
            public MarkupContainer simpleAdd(Component... c) {
                return this;
            }

            @Override
            public Item buildItem(int id, TestObj object) {
                Item i = Mockito.mock(Item.class);
                Mockito.when(i.getMarkupId()).thenReturn(String.valueOf(id));
                list.add(i);
                return i;
            }
        };

        QuickView spy = Mockito.spy(arc);
        spy.createChildren(0);

        Mockito.verify(spy, Mockito.times(3)).simpleAdd(Mockito.any(Item.class));
        Mockito.verify(spy, Mockito.times(1)).simpleAdd(list.get(0));
        Assert.assertEquals(Long.parseLong(list.get(0).getMarkupId()), 0l);
        Mockito.verify(spy, Mockito.times(1)).simpleAdd(list.get(1));
        Assert.assertEquals(Long.parseLong(list.get(1).getMarkupId()), 1l);
        Mockito.verify(spy, Mockito.times(1)).simpleAdd(list.get(2));
        Assert.assertEquals(Long.parseLong(list.get(2).getMarkupId()), 2l);
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
        repeater.setReuse(ReUse.DEFAULT_PAGING);
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
        repeater.setReuse(ReUse.DEFAULT_ITEMSNAVIGATION);
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
            public MarkupContainer removePages(int startPage, int stopPage) {
                return this;
            }


            @Override
            public int _getPageCount() {
                return 6;    //To change body of overridden methods use File | Settings | File Templates.
            }
        };
        repeater.setReuse(ReUse.CURRENTPAGE);
        QuickView spy = Mockito.spy(repeater);
        spy.onPopulate();
        Mockito.verify(util,Mockito.times(1)).reuseNotInitialized(spy);
        Mockito.verify(util,Mockito.times(1)).parentNotSuitable(spy);
        Mockito.verify(spy, Mockito.times(1)).simpleRemoveAllIfNotReuse();
        Mockito.verify(spy, Mockito.times(1)).removePages(0, 1);
        Mockito.verify(spy, Mockito.times(1)).removePages(3, 5);

    }

    /**
     * reuse ={@link ReUse.CURRENTPAGE}   ,size!=0    ,pagecount=6
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
            public MarkupContainer removePages(int startPage, int stopPage) {
                return this;
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
        Mockito.verify(spy, Mockito.never()).removePages(0, 1);
        Mockito.verify(spy, Mockito.never()).removePages(3, 5);
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
    public void removePages_1() {
        IDataProvider provider = Mockito.mock(IDataProvider.class);
        //final int size = 5;
        final List<Item> items = new ArrayList<Item>();
        QuickView repeater = new QuickView("repeater", provider, 2) {
            @Override
            protected void populate(Item item) {
            }

            @Override
            public MarkupContainer simpleRemove(Component c) {
                return this;
            }

            @Override
            public Item getItem(int index) {
                Item item = Mockito.mock(Item.class);
                Mockito.when(item.getMarkupId()).thenReturn(String.valueOf(index));
                items.add(item);
                return item;
            }
        };

        repeater.setReuse(ReUse.DEFAULT_PAGING);
        QuickView spy = Mockito.spy(repeater);
        spy.removePages(1, 2);
        Mockito.verify(spy, Mockito.times(4)).simpleRemove(Mockito.any(Item.class));
        Mockito.verify(spy, Mockito.times(1)).simpleRemove(items.get(0));
        Mockito.verify(spy, Mockito.times(1)).simpleRemove(items.get(1));
        Mockito.verify(spy, Mockito.times(1)).simpleRemove(items.get(2));
        Mockito.verify(spy, Mockito.times(1)).simpleRemove(items.get(3));
    }

    /**
     * components not empty  and repeater's parent is added to another component which is added to A.R.T
     */
    @Test(groups = {"wicketTests"})
    public void isParentAdded_1() {
        items.clear();
        final int itemsPerRequest=1;
        final TestComponentPanel.Parent parent = new TestComponentPanel.Parent(TestComponentPanel.parentId);
        parent.setOutputMarkupId(true);
        final IDataProvider dataProvider =  new ListDataProvider(data(10));
        QuickMockApplication app=new QuickMockApplication();
        WicketTester tester=new WicketTester(app);
        final QuickView<Integer> quickView = new QuickView<Integer>(TestComponentPanel.quickViewId, dataProvider,itemsPerRequest) {

            public void populate(Item<Integer> item) {
            }
        };
        final AjaxLink link=new AjaxLink(TestComponentPanel.ajaxLinkId) {
            @Override
            public void onClick(AjaxRequestTarget target) {
                target.add(parent.getParent());
            }
        };
        parent.add(quickView);
        //reuse all is intentionally set because quickview is rendered again and any other strategy would not retail all which we need to check for quickview.size()
        quickView.setReuse(ReUse.ALL);

        TestComponentPanel panel=new TestComponentPanel("panel"){
            @Override
            public Parent newParent() {
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
        Assert.assertTrue(quickView.isParentAddedInAjaxRequestTarget(target));
    }

    /**
     * components not empty  and repeater's parent is added directly in A.R.T
     */
    @Test(groups = {"wicketTests"})
    public void isParentAdded_2() {
        items.clear();
        final int itemsPerRequest=1;
        final TestComponentPanel.Parent parent = new TestComponentPanel.Parent(TestComponentPanel.parentId);
        parent.setOutputMarkupId(true);
        final IDataProvider dataProvider =  new ListDataProvider(data(10));
        QuickMockApplication app=new QuickMockApplication();
        WicketTester tester=new WicketTester(app);
        final QuickView<Integer> quickView = new QuickView<Integer>(TestComponentPanel.quickViewId, dataProvider,itemsPerRequest) {

            public void populate(Item<Integer> item) {
            }
        };
        final AjaxLink link=new AjaxLink(TestComponentPanel.ajaxLinkId) {
            @Override
            public void onClick(AjaxRequestTarget target) {
                    target.add(parent);

            }
        };
        parent.add(quickView);
        //reuse all is intentionally set because quickview is rendered again and any other strategy would not retail all which we need to check for quickview.size()
        quickView.setReuse(ReUse.ALL);

        TestComponentPanel panel=new TestComponentPanel("panel"){
            @Override
            public Parent newParent() {
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
        Assert.assertTrue(quickView.isParentAddedInAjaxRequestTarget(target));
    }

    /**
     * parenent not added to A.R.T
     */
    @Test(groups = {"wicketTests"})
    public void isParentAdded_3() {
        items.clear();
        final int itemsPerRequest=1;
        final TestComponentPanel.Parent parent = new TestComponentPanel.Parent(TestComponentPanel.parentId);
        parent.setOutputMarkupId(true);
        final IDataProvider dataProvider =  new ListDataProvider(data(10));
        QuickMockApplication app=new QuickMockApplication();
        WicketTester tester=new WicketTester(app);
        final QuickView<Integer> quickView = new QuickView<Integer>(TestComponentPanel.quickViewId, dataProvider,itemsPerRequest) {

            public void populate(Item<Integer> item) {
            }
        };
        final AjaxLink link=new AjaxLink(TestComponentPanel.ajaxLinkId) {
            @Override
            public void onClick(AjaxRequestTarget target) {
                TestComponentPanel panel=(TestComponentPanel)parent.getParent();
                target.add(panel.getNavigator());
            }
        };
        parent.add(quickView);
        //reuse all is intentionally set because quickview is rendered again and any other strategy would not retail all which we need to check for quickview.size()
        quickView.setReuse(ReUse.ALL);

        TestComponentPanel panel=new TestComponentPanel("panel"){
            @Override
            public Parent newParent() {
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
        Assert.assertFalse(quickView.isParentAddedInAjaxRequestTarget(target));
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
    }
    /*
@Test(groups = {"wicketTests"})
public void childVisitor_1(){
 QuickViewBase.ChildVisitor visitor=new QuickViewBase.ChildVisitor(searchFor);

}       */

    @Test(groups = {"wicketTests"})
    public void clearIndex_1() {
        IDataProvider data=Mockito.mock(IDataProvider.class);
         QuickView quickView=new QuickView("id",data) {
             @Override
             protected void populate(Item item) {
             }
         } ;
        quickView.incrementIndexByNumber(7);
        quickView.clearChildId();
        Assert.assertEquals(quickView.getIndex(),0l);
    }

    @Test(groups = {"wicketTests"})
    public void incrementIndexByNumber_1() {
        IDataProvider data=Mockito.mock(IDataProvider.class);
        QuickView quickView=new QuickView("id",data) {
            @Override
            protected void populate(Item item) {
            }
        } ;
        quickView.incrementIndexByNumber(7);
        Assert.assertEquals(quickView.getIndex(),7l);
    }
    @Test(groups = {"wicketTests"})
    public void incrementIndexByNumber_2() {
        IDataProvider data=Mockito.mock(IDataProvider.class);
        QuickView quickView=new QuickView("id",data) {
            @Override
            protected void populate(Item item) {
            }
        } ;
        quickView.incrementIndexByNumber(10);
        quickView.incrementIndexByNumber(-7);
        Assert.assertEquals(quickView.getIndex(),3l);
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
        Item one=quickView.buildItem(0, 67);
        Item two=quickView.buildItem(1, 68);
        quickView.simpleAdd(one,two);
        quickView.simpleRemove(one);
        Assert.assertEquals(quickView.getIndex(),1l);
        Assert.assertEquals(quickView.size(),1);
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
        Item one=quickView.buildItem(0, 67);
        Item two=quickView.buildItem(1, 68);
        quickView.simpleAdd(one,two);
        quickView.simpleRemoveAll();
         Assert.assertEquals(quickView.getIndex(),0l);
        Assert.assertEquals(quickView.size(),0);
    }

    @Test(groups = {"wicketTests"})
    public void simpleAdd(){
        IDataProvider data=Mockito.mock(IDataProvider.class);
        QuickView quickView=new QuickView("id",data) {
            @Override
            protected void populate(Item item) {
            }
        } ;
        Item one=quickView.buildItem(0, 67);
        quickView.simpleAdd(one);
        Assert.assertEquals(quickView.getIndex(),1);
        Assert.assertEquals(quickView.size(),1);
        Item two=quickView.buildItem(1, 68);
        quickView.simpleAdd(two);
        Assert.assertEquals(quickView.getIndex(),2);
        Assert.assertEquals(quickView.size(),2);

    }

    /**
     * model object is integer
     */
    @Test(groups = {"wicketTests"})
    public void newItem_1(){
        final  int object=89;
        final Model<Integer> model=new Model<Integer>(object);
        IDataProvider<Integer> data=Mockito.mock(IDataProvider.class);
        Mockito.when(data.model(object)).thenReturn(model);
        QuickView<Integer> quickView=new QuickView<Integer>("id",data,ReUse.DEFAULT_ITEMSNAVIGATION) {
            @Override
            protected void populate(Item<Integer> item) {
            }
        } ;
        final long id=9l;

       Item <Integer>item= quickView.newItem(id,object);
        Assert.assertEquals(item.getModelObject().intValue(), 89);
        Assert.assertEquals(Integer.parseInt(item.getMarkupId()),id);
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
        QuickView<String> quickView=new QuickView<String>("id",data,ReUse.DEFAULT_ITEMSNAVIGATION) {
            @Override
            protected void populate(Item<String> item) {
    }
        } ;
        final long id=9l;

        Item <String>item= quickView.newItem(id,object);
        Assert.assertEquals(item.getModelObject(),object);
        Assert.assertEquals(Integer.parseInt(item.getMarkupId()),id);
        Assert.assertTrue(item.getOutputMarkupId());
    }

    /**
     * test for  {@link QuickViewBase#buildItem(long, Object)}
     */
    @Test(groups = {"wicketTests"})
    public void buildItem_1(){
        IDataProvider data=Mockito.mock(IDataProvider.class);
        final Item item=Mockito.mock(Item.class);
        QuickView<TestObj> quickView=new QuickView<TestObj>("id",data,ReUse.DEFAULT_ITEMSNAVIGATION) {
            @Override
            protected void populate(Item item) {
            }

            @Override
            protected Item newItem(long id, TestObj object) {
                return item;
            }
        } ;
        final int id=9;
       final TestObj object=Mockito.mock(TestObj.class);
        QuickView<TestObj>spy=Mockito.spy(quickView);
        Item <TestObj>actual= spy.buildItem(id,object);
        Assert.assertEquals(actual,item);
       InOrder order= Mockito.inOrder(spy, item);
         order.verify(spy, Mockito.times(1)).newItem(id,object);
         order.verify(spy, Mockito.times(1)).populate(item);
     }

    /**
     * test for  {@link QuickViewBase#buildItem(String, Object)}
     */
    @Test(groups = {"wicketTests"})
    public void buildItem_2(){
        IDataProvider data=Mockito.mock(IDataProvider.class);
        final Item item=Mockito.mock(Item.class);
        QuickView<TestObj> quickView=new QuickView<TestObj>("id",data,ReUse.DEFAULT_ITEMSNAVIGATION) {
            @Override
            protected void populate(Item item) {
            }

            @Override
            public Item<TestObj> buildItem(int id, TestObj object) {
                return item;
            }
        } ;
        final String id="99";
        final TestObj object=Mockito.mock(TestObj.class);
        QuickView<TestObj>spy=Mockito.spy(quickView);
        Item <TestObj>actual= spy.buildItem(id,object);
        Assert.assertEquals(actual,item);
       Mockito.verify(spy,Mockito.times(1)).buildItem(99, object);
    }


    /**
     * test for  {@link QuickViewBase#buildItem(String, Object)}
     */
    @Test(groups = {"wicketTests"})
    public void buildItem_3(){
        final String id="78";
        IDataProvider data=Mockito.mock(IDataProvider.class);
        final Item item=Mockito.mock(Item.class);
        QuickView<TestObj> quickView=new QuickView<TestObj>("id",data,ReUse.DEFAULT_ITEMSNAVIGATION) {
            @Override
            protected void populate(Item item) {
            }

            @Override
            public Item<TestObj> buildItem( int id, TestObj object) {
                return item;
            }

            @Override
            public String newChildId() {
                return id;
            }
        } ;
       // final String id="99";
        final TestObj object=Mockito.mock(TestObj.class);
        QuickView<TestObj>spy=Mockito.spy(quickView);
        Item <TestObj>actual= spy.buildItem(object);
        Assert.assertEquals(actual,item);
        Mockito.verify(spy,Mockito.times(1)).buildItem(id,object);
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
