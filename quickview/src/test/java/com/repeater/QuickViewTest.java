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


import org.apache.log4j.Logger;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.mock.MockApplication;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.util.tester.WicketTester;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
    }

    private static WebApplication createMockApplication() {
        WebApplication app = new MockApplication();
        return app;
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
        final int itemsPerRequest = 2;
        final WebMarkupContainer parent = Mockito.mock(WebMarkupContainer.class);
        IDataProvider dataProvider = Mockito.mock(IDataProvider.class);
        final IRepeaterUtil util = mockRepeaterUtil();
        final TestObj to = Mockito.mock(TestObj.class);
        final AjaxRequestTarget target = Mockito.mock(AjaxRequestTarget.class);
        final Item c = Mockito.mock(Item.class);

        QuickView<TestObj> arc = new QuickView<TestObj>("repeater", dataProvider, itemsPerRequest) {

            public void populate(Item<TestObj> item) {
            }

            public MarkupContainer _getParent() {
                return parent;
            }

            public MarkupContainer simpleAdd(Component... c) {
                return this;
            }

            @Override
            public boolean isParentAddedInAjaxRequestTarget() {
                return false;
            }

            public boolean isAjax() {
                return true;
            }

            @Override
            public AjaxRequestTarget getAjaxRequestTarget() {
                return target;
            }

            @Override
            public IRepeaterUtil getRepeaterUtil() {
                return util;
            }

        };
        final String script = "script..";
        Mockito.when(util.insertAfter(c, parent)).thenReturn(script);
        QuickView sparc = Mockito.spy(arc);
        sparc.add(c);

        Mockito.verify(sparc, Mockito.times(1)).simpleAdd(c);
        Mockito.verify(target, Mockito.times(1)).add(c);
        Mockito.verify(target, Mockito.times(1)).prependJavaScript(script);

    }


    /**
     * add one component ,isajax=false,isParentadded=false
     */
    @Test(groups = {"wicketTests"})
    public void add_2() {
        int oneBlock = 2;
        final WebMarkupContainer parent = Mockito.mock(WebMarkupContainer.class);
        IDataProvider dataProvider = Mockito.mock(IDataProvider.class);
        final IRepeaterUtil util = mockRepeaterUtil();
        final TestObj to = Mockito.mock(TestObj.class);
        final AjaxRequestTarget target = Mockito.mock(AjaxRequestTarget.class);
        final Item c = Mockito.mock(Item.class);

        QuickView<TestObj> arc = new QuickView<TestObj>("repeater", dataProvider, oneBlock) {

            public void populate(Item<TestObj> item) {
            }

            public MarkupContainer _getParent() {
                return parent;
            }

            public MarkupContainer simpleAdd(Component... c) {
                return this;
            }

            @Override
            public boolean isParentAddedInAjaxRequestTarget() {
                return false;
            }

            public boolean isAjax() {
                return false;
            }

            @Override
            public AjaxRequestTarget getAjaxRequestTarget() {
                return target;
            }


            @Override
            public IRepeaterUtil getRepeaterUtil() {
                return util;
            }


        };
        final String script = "script..";
        Mockito.when(util.insertAfter(c, parent)).thenReturn(script);
        QuickView sparc = Mockito.spy(arc);
        sparc.add(c);

        Mockito.verify(sparc, Mockito.times(1)).simpleAdd(c);
        Mockito.verify(target, Mockito.never()).add(c);
        Mockito.verify(target, Mockito.never()).prependJavaScript(script);

    }


    /**
     * add one component ,isajax=true,isParentadded=true
     */
    @Test(groups = {"wicketTests"})
    public void add_3() {
        int oneBlock = 2;
        final WebMarkupContainer parent = Mockito.mock(WebMarkupContainer.class);
        IDataProvider dataProvider = Mockito.mock(IDataProvider.class);
        final IRepeaterUtil util = mockRepeaterUtil();
        final TestObj to = Mockito.mock(TestObj.class);
        final AjaxRequestTarget target = Mockito.mock(AjaxRequestTarget.class);
        final Item c = Mockito.mock(Item.class);

        QuickView<TestObj> arc = new QuickView<TestObj>("repeater", dataProvider, oneBlock) {

            public void populate(Item<TestObj> item) {
            }

            public MarkupContainer _getParent() {
                return parent;
            }

            public MarkupContainer simpleAdd(Component... c) {
                return this;
            }

            @Override
            public boolean isParentAddedInAjaxRequestTarget() {
                return true;
            }

            public boolean isAjax() {
                return true;
            }

            @Override
            public AjaxRequestTarget getAjaxRequestTarget() {
                return target;
            }

            @Override
            public IRepeaterUtil getRepeaterUtil() {
                return util;
            }

        };
        final String script = "script..";
        Mockito.when(util.insertAfter(c, parent)).thenReturn(script);
        QuickView sparc = Mockito.spy(arc);
        sparc.add(c);

        Mockito.verify(sparc, Mockito.times(1)).simpleAdd(c);
        Mockito.verify(target, Mockito.never()).add(c);
        Mockito.verify(target, Mockito.never()).prependJavaScript(script);

    }

    /**
     * add two components ,isajax=true,isParentadded=false
     */
    @Test(groups = {"wicketTests"})
    public void add_4() {
        int start = 0;
        int oneBlock = 2;
        final WebMarkupContainer parent = Mockito.mock(WebMarkupContainer.class);
        IDataProvider dataProvider = Mockito.mock(IDataProvider.class);
        final IRepeaterUtil util = mockRepeaterUtil();
        final TestObj to = Mockito.mock(TestObj.class);
        final AjaxRequestTarget target = Mockito.mock(AjaxRequestTarget.class);
        final Item c = Mockito.mock(Item.class);
        final Item c2 = Mockito.mock(Item.class);

        QuickView<TestObj> arc = new QuickView<TestObj>("repeater", dataProvider, oneBlock) {

            public void populate(Item<TestObj> item) {
            }

            public MarkupContainer _getParent() {
                return parent;
            }

            public MarkupContainer simpleAdd(Component... c) {
                return this;
            }

            @Override
            public boolean isParentAddedInAjaxRequestTarget() {
                return false;
            }

            public boolean isAjax() {
                return true;
            }

            @Override
            public AjaxRequestTarget getAjaxRequestTarget() {
                return target;
            }

            @Override
            public IRepeaterUtil getRepeaterUtil() {
                return util;
            }

        };
        final String script = "script..", script2 = "script2!";
        Mockito.when(util.insertAfter(c, parent)).thenReturn(script);
        Mockito.when(util.insertAfter(c2, parent)).thenReturn(script2);
        QuickView sparc = Mockito.spy(arc);
        sparc.add(c, c2);
        Mockito.verify(sparc, Mockito.times(1)).simpleAdd(c, c2);
        Mockito.verify(target, Mockito.times(1)).add(c, c2);
        Mockito.verify(target, Mockito.times(1)).prependJavaScript(script);
        Mockito.verify(target, Mockito.times(1)).prependJavaScript(script2);

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
            public Item buildCompleteItem(long id, TestObj object) {
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
            public Item buildCompleteItem(long id, TestObj object) {
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
        final long page = 2l;
        Iterator it = mockIterator();
        long start = itemsPerRequest * page;
        Mockito.when(dataProvider.iterator(start, itemsPerRequest)).thenReturn(it);
        Mockito.when(it.hasNext()).thenReturn(true).thenReturn(true).thenReturn(false);
        QuickView<TestObj> repeater = new QuickView<TestObj>("repeater", dataProvider, ReUse.DEFAULT_ITEMSNAVIGATION, itemsPerRequest) {

            public void populate(Item<TestObj> item) {
            }

            @Override
            public Item buildCompleteItem(long id, TestObj object) {
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
            public List<Item<TestObj>> addComponentsFromIndex(long index) {
                return null;
            }
        };
        QuickView spy = Mockito.spy(repeater);
        List<Item<TestObj>> items = spy.addComponentsForPage(page);
        Mockito.verify(spy).addComponentsFromIndex(start);

    }


    @Test(groups = {"wicketTests"})
    public void remove_1() {
        final int itemsPerRequest = 2;
        final WebMarkupContainer parent = Mockito.mock(WebMarkupContainer.class);
        IDataProvider dataProvider = Mockito.mock(IDataProvider.class);
        final IRepeaterUtil util = mockRepeaterUtil();
        final TestObj to = Mockito.mock(TestObj.class);
        final AjaxRequestTarget target = Mockito.mock(AjaxRequestTarget.class);
        final Item c = Mockito.mock(Item.class);
        final String itemId = "item";
        Mockito.when(c.getMarkupId()).thenReturn(itemId);
        QuickView<TestObj> arc = new QuickView<TestObj>("repeater", dataProvider, itemsPerRequest) {

            public void populate(Item<TestObj> item) {
            }

            public MarkupContainer _getParent() {
                return parent;
            }

            @Override
            public MarkupContainer simpleRemove(Component c) {
                return this;
            }

            @Override
            public boolean isParentAddedInAjaxRequestTarget() {
                return false;
            }

            public boolean isAjax() {
                return true;
            }

            @Override
            public AjaxRequestTarget getAjaxRequestTarget() {
                return target;
            }

            @Override
            public IRepeaterUtil getRepeaterUtil() {
                return util;
            }


        };
        final String script = "script..";
        Mockito.when(util.removeItem(c)).thenReturn(script);
        QuickView spy = Mockito.spy(arc);
        spy.remove(c);
        Mockito.verify(spy, Mockito.times(1)).simpleRemove(c);
        Mockito.verify(target, Mockito.times(1)).add(c);
        Mockito.verify(target, Mockito.times(1)).prependJavaScript(script);

    }

    /**
     * one component added ,ajax=true ,parent added=false
     */
    @Test(groups = {"wicketTests"})
    public void addAtStart_1() {
        int start = 0;
        int oneBlock = 2;
        final WebMarkupContainer parent = Mockito.mock(WebMarkupContainer.class);
        IDataProvider dataProvider = Mockito.mock(IDataProvider.class);
        final IRepeaterUtil util = mockRepeaterUtil();
        final TestObj to = Mockito.mock(TestObj.class);
        final AjaxRequestTarget target = Mockito.mock(AjaxRequestTarget.class);
        final Item c = Mockito.mock(Item.class);

        QuickView<TestObj> arc = new QuickView<TestObj>("repeater", dataProvider, oneBlock) {

            public void populate(Item<TestObj> item) {
            }

            public MarkupContainer _getParent() {
                return parent;
            }

            public MarkupContainer simpleAdd(Component... c) {
                return this;
            }

            @Override
            public boolean isParentAddedInAjaxRequestTarget() {
                return false;
            }

            public boolean isAjax() {
                return true;
            }

            @Override
            public AjaxRequestTarget getAjaxRequestTarget() {
                return target;
            }

            @Override
            public IRepeaterUtil getRepeaterUtil() {
                return util;
            }


        };
        final String script = "script..";
        Mockito.when(util.insertBefore(c, parent)).thenReturn(script);
        QuickView sparc = Mockito.spy(arc);
        sparc.addAtStart(c);
        Mockito.verify(sparc, Mockito.times(1)).simpleAdd(c);
        Mockito.verify(target, Mockito.times(1)).add(c);
        Mockito.verify(target, Mockito.times(1)).prependJavaScript(script);

    }

    /**
     * 2 components added ,ajax=true ,parent added=false
     */
    @Test(groups = {"wicketTests"})
    public void addAtStart_2() {
        int start = 0;
        int oneBlock = 2;
        final WebMarkupContainer parent = Mockito.mock(WebMarkupContainer.class);
        IDataProvider dataProvider = Mockito.mock(IDataProvider.class);
        final IRepeaterUtil util = mockRepeaterUtil();
        final TestObj to = Mockito.mock(TestObj.class);
        final AjaxRequestTarget target = Mockito.mock(AjaxRequestTarget.class);
        final Item c = Mockito.mock(Item.class);
        final Item c2 = Mockito.mock(Item.class);
        QuickView<TestObj> arc = new QuickView<TestObj>("repeater", dataProvider, oneBlock) {

            public void populate(Item<TestObj> item) {
            }

            public MarkupContainer _getParent() {
                return parent;
            }

            public MarkupContainer simpleAdd(Component... c) {
                return this;
            }

            @Override
            public boolean isParentAddedInAjaxRequestTarget() {
                return false;
            }

            public boolean isAjax() {
                return true;
            }

            @Override
            public AjaxRequestTarget getAjaxRequestTarget() {
                return target;
            }


            @Override
            public IRepeaterUtil getRepeaterUtil() {
                return util;
            }

        };
        final String script = "script..", script2 = "script2!";
        Mockito.when(util.insertBefore(c, parent)).thenReturn(script);
        Mockito.when(util.insertBefore(c2, parent)).thenReturn(script2);
        QuickView sparc = Mockito.spy(arc);
        sparc.addAtStart(c, c2);
        Mockito.verify(sparc, Mockito.times(1)).simpleAdd(c, c2);
        Mockito.verify(target, Mockito.times(1)).add(c, c2);
        Mockito.verify(target, Mockito.times(1)).prependJavaScript(script);
        Mockito.verify(target, Mockito.times(1)).prependJavaScript(script2);
    }

    /**
     * one component added ,ajax=false ,parent added=false
     */
    @Test(groups = {"wicketTests"})
    public void addAtStart_3() {

        int oneBlock = 2;
        final WebMarkupContainer parent = Mockito.mock(WebMarkupContainer.class);
        IDataProvider dataProvider = Mockito.mock(IDataProvider.class);
        final IRepeaterUtil util = mockRepeaterUtil();
        final TestObj to = Mockito.mock(TestObj.class);
        final AjaxRequestTarget target = Mockito.mock(AjaxRequestTarget.class);
        final Item c = Mockito.mock(Item.class);

        QuickView<TestObj> arc = new QuickView<TestObj>("repeater", dataProvider, oneBlock) {

            public void populate(Item<TestObj> item) {
            }

            public MarkupContainer _getParent() {
                return parent;
            }

            public MarkupContainer simpleAdd(Component... c) {
                return this;
            }

            @Override
            public boolean isParentAddedInAjaxRequestTarget() {
                return false;
            }

            public boolean isAjax() {
                return false;
            }

            @Override
            public AjaxRequestTarget getAjaxRequestTarget() {
                return target;
            }

            @Override
            public IRepeaterUtil getRepeaterUtil() {
                return util;
            }


        };
        final String script = "script..";
        Mockito.when(util.insertBefore(c, parent)).thenReturn(script);
        QuickView sparc = Mockito.spy(arc);
        sparc.addAtStart(c);
        Mockito.verify(sparc, Mockito.times(1)).simpleAdd(c);
        Mockito.verify(target, Mockito.never()).add(c);
        Mockito.verify(target, Mockito.never()).prependJavaScript(script);

    }

    /**
     * one component added ,ajax=true,parent added=true
     */
    @Test(groups = {"wicketTests"})
    public void addAtStart_4() {
        int start = 0;
        int oneBlock = 2;
        final WebMarkupContainer parent = Mockito.mock(WebMarkupContainer.class);
        IDataProvider dataProvider = Mockito.mock(IDataProvider.class);
        final IRepeaterUtil util = mockRepeaterUtil();
        final TestObj to = Mockito.mock(TestObj.class);
        final AjaxRequestTarget target = Mockito.mock(AjaxRequestTarget.class);
        final Item c = Mockito.mock(Item.class);

        QuickView<TestObj> arc = new QuickView<TestObj>("repeater", dataProvider, oneBlock) {

            public void populate(Item<TestObj> item) {
            }

            public MarkupContainer _getParent() {
                return parent;
            }

            public MarkupContainer simpleAdd(Component... c) {
                return this;
            }

            @Override
            public boolean isParentAddedInAjaxRequestTarget() {
                return false;
            }

            public boolean isAjax() {
                return false;
            }

            @Override
            public AjaxRequestTarget getAjaxRequestTarget() {
                return target;
            }

            @Override
            public IRepeaterUtil getRepeaterUtil() {
                return util;
            }


        };
        final String script = "script..";
        Mockito.when(util.insertBefore(c, parent)).thenReturn(script);
        QuickView sparc = Mockito.spy(arc);
        sparc.addAtStart(c);
        Mockito.verify(sparc, Mockito.times(1)).simpleAdd(c);
        Mockito.verify(target, Mockito.never()).add(c);
        Mockito.verify(target, Mockito.never()).prependJavaScript(script);

    }


    @Test(groups = {"wicketTests"})
    public void createChildren_1() {

        int itemsPerRequest = 3;

        IDataProvider provider = Mockito.mock(IDataProvider.class);
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
            public Item buildCompleteItem(long id, TestObj object) {
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
     *
     */

    @Test(groups = {"wicketTests"})
    public void createChildren_2() {
        final int itemsPerRequest = 3;
        IDataProvider provider = Mockito.mock(IDataProvider.class);
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
            public Item buildCompleteItem(long id, TestObj object) {
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
            protected void createChildren(long page) {
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
            public long _getCurrentPage() {
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
            protected void createChildren(long page) {
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
            public long _getCurrentPage() {
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
            protected void createChildren(long page) {
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
            public long _getCurrentPage() {
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
            protected void createChildren(long page) {
            }

            @Override
            public void simpleRemoveAllIfNotReuse() {
            }

            @Override
            public int size() {
                return size;
            }

            @Override
            public long _getCurrentPage() {
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
            protected void createChildren(long page) {
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
            public long _getCurrentPage() {
                return 2;
            }

            @Override
            public MarkupContainer removePages(long startPage, long stopPage) {
                return this;
            }


            @Override
            public long _getPageCount() {
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
            protected void createChildren(long page) {
            }

            @Override
            public void simpleRemoveAllIfNotReuse() {
            }

            @Override
            public int size() {
                return size;
            }

            @Override
            public long _getCurrentPage() {
                return 2;
            }

            @Override
            public MarkupContainer removePages(long startPage, long stopPage) {
                return this;
            }


            @Override
            public long _getPageCount() {
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
        Mockito.verify(spy, Mockito.never()).createChildren(Mockito.anyLong());

    }

    /**
     * items=10,itemsperrequest=2
     */
    @Test(groups = {"wicketTests"})
    public void getPageCount_1() {
        final int itemsperrequest = 2;
        IDataProvider provider = Mockito.mock(IDataProvider.class);
        Mockito.when(provider.size()).thenReturn(10l);
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
        Mockito.when(provider.size()).thenReturn(10l);
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
        Mockito.when(provider.size()).thenReturn(10l);
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
        Mockito.when(provider.size()).thenReturn(10l);
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
        Mockito.when(provider.size()).thenReturn(10l);
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
        Mockito.when(provider.size()).thenReturn(10l);
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
        Mockito.when(provider.size()).thenReturn(10l);
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
            public Item getItem(long index) {
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
     * components not empty  and repeater's parent is to another component which is added to A.R.T
     */
    @Test(groups = {"wicketTests"})
    public void isParentAdded_1() {
        IDataProvider provider = Mockito.mock(IDataProvider.class);
        final AjaxRequestTarget target = Mockito.mock(AjaxRequestTarget.class);
        List cs = new ArrayList();
        Mockito.when(target.getComponents()).thenReturn(cs);
        WebMarkupContainer one = Mockito.mock(WebMarkupContainer.class);
        WebMarkupContainer two = Mockito.mock(WebMarkupContainer.class);
        Label three = Mockito.mock(Label.class);
        cs.add(one);
        cs.add(two);
        cs.add(three);
        final MarkupContainer parent = Mockito.mock(MarkupContainer.class);
        QuickView repeater = new QuickView("repeater", provider, 3) {
            @Override
            protected void populate(Item item) {
            }

            public MarkupContainer _getParent() {
                return parent;
            }

            @Override
            public AjaxRequestTarget getAjaxRequestTarget() {
                return target;
            }

            @Override
            protected Boolean addNewChildVisitor(MarkupContainer parent, Component searchFor) {
                return true;
            }
        };

        QuickView spy = Mockito.spy(repeater);
        spy.isParentAddedInAjaxRequestTarget();
        Mockito.verify(spy, Mockito.times(1)).addNewChildVisitor(one, parent);
        Mockito.verify(spy, Mockito.never()).addNewChildVisitor(two, parent);
    }

    /**
     * components not empty  and repeater's parent is to another component which is added to A.R.T
     */
    @Test(groups = {"wicketTests"})
    public void isParentAdded_2() {
        IDataProvider provider = Mockito.mock(IDataProvider.class);
        final AjaxRequestTarget target = Mockito.mock(AjaxRequestTarget.class);
        List cs = new ArrayList();
        Mockito.when(target.getComponents()).thenReturn(cs);
        WebMarkupContainer one = Mockito.mock(WebMarkupContainer.class);
        WebMarkupContainer two = Mockito.mock(WebMarkupContainer.class);
        Label three = Mockito.mock(Label.class);
        cs.add(one);
        cs.add(two);
        cs.add(three);
        final MarkupContainer parent = Mockito.mock(MarkupContainer.class);
        QuickView repeater = new QuickView("repeater", provider, 3) {
            @Override
            protected void populate(Item item) {
            }

            public MarkupContainer _getParent() {
                return parent;
            }

            @Override
            public AjaxRequestTarget getAjaxRequestTarget() {
                return target;
            }

            @Override
            protected Boolean addNewChildVisitor(MarkupContainer parent, Component searchFor) {
                return false;
            }
        };

        QuickView spy = Mockito.spy(repeater);
        boolean result = spy.isParentAddedInAjaxRequestTarget();
        Mockito.verify(spy, Mockito.times(1)).addNewChildVisitor(one, parent);
        Mockito.verify(spy, Mockito.times(1)).addNewChildVisitor(two, parent);
        Assert.assertFalse(result);
    }

    /**
     * components not empty  and repeater's parent is added directly in A.R.T
     */
    @Test(groups = {"wicketTests"})
    public void isParentAdded_3() {
        IDataProvider provider = Mockito.mock(IDataProvider.class);
        final AjaxRequestTarget target = Mockito.mock(AjaxRequestTarget.class);
        List cs = new ArrayList();
        Mockito.when(target.getComponents()).thenReturn(cs);
        final WebMarkupContainer one = Mockito.mock(WebMarkupContainer.class);
        WebMarkupContainer two = Mockito.mock(WebMarkupContainer.class);
        Label three = Mockito.mock(Label.class);
        cs.add(one);
        cs.add(two);

        QuickView repeater = new QuickView("repeater", provider, 3) {
            @Override
            protected void populate(Item item) {
            }

            public MarkupContainer _getParent() {
                return one;
            }

            @Override
            public AjaxRequestTarget getAjaxRequestTarget() {
                return target;
            }

            @Override
            protected Boolean addNewChildVisitor(MarkupContainer parent, Component searchFor) {
                return false;
            }
        };

        QuickView spy = Mockito.spy(repeater);
        spy.isParentAddedInAjaxRequestTarget();
        Mockito.verify(spy, Mockito.never()).addNewChildVisitor(one, one);
        Mockito.verify(spy, Mockito.never()).addNewChildVisitor(two, one);

    }


    /**
     * components is empty  and repeater's parent is added directly in A.R.T
     */
    @Test(groups = {"wicketTests"})
    public void isParentAdded_4() {
        IDataProvider provider = Mockito.mock(IDataProvider.class);
        final AjaxRequestTarget target = Mockito.mock(AjaxRequestTarget.class);
        List cs = new ArrayList();
        Mockito.when(target.getComponents()).thenReturn(cs);


        QuickView repeater = new QuickView("repeater", provider, 2) {
            @Override
            protected void populate(Item item) {
            }


            @Override
            public AjaxRequestTarget getAjaxRequestTarget() {
                return target;
            }


        };

        QuickView spy = Mockito.spy(repeater);
        spy.isParentAddedInAjaxRequestTarget();
        Mockito.verify(spy, Mockito.never()).addNewChildVisitor(Mockito.any(MarkupContainer.class), Mockito.any(MarkupContainer.class));
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
        Mockito.verify(response, Mockito.times(1)).render(JavaScriptHeaderItem.forReference(RepeaterUtilReference.get()));
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
        Item one=quickView.buildCompleteItem(0,67);
        Item two=quickView.buildCompleteItem(1,68);
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
        Item one=quickView.buildCompleteItem(0,67);
        Item two=quickView.buildCompleteItem(1,68);
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
        Item one=quickView.buildCompleteItem(0,67);
        quickView.simpleAdd(one);
        Assert.assertEquals(quickView.getIndex(),1);
        Assert.assertEquals(quickView.size(),1);
        Item two=quickView.buildCompleteItem(1,68);
        quickView.simpleAdd(two);
        Assert.assertEquals(quickView.getIndex(),2);
        Assert.assertEquals(quickView.size(),2);

    }

    public AjaxRequestTarget mockTarget() {
        AjaxRequestTarget target = mock(AjaxRequestTarget.class);
        return target;
    }

    public Iterator<TestObj> mockIterator() {
        Iterator<TestObj> it = mock(Iterator.class);
        return it;
    }

    public IDataProvider<TestObj> mockProvider(long size) {
        IDataProvider<TestObj> dp = mock(IDataProvider.class);
        when(dp.size()).thenReturn(size);
        return dp;
    }


    IRepeaterUtil mockRepeaterUtil() {
        return mock(IRepeaterUtil.class);

    }

}
