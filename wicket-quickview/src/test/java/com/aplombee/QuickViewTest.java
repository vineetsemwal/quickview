/**
 * Copyright 2012 Vineet Semwal
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.aplombee;


import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.IItemFactory;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.mock.MockApplication;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.util.tester.WicketTester;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Vineet Semwal
 */
public class QuickViewTest {
    Component updateBefore;

    WicketTester tester;

    @BeforeTest(groups = {"wicketTests"})
    void setup() {
        tester = new WicketTester(createMockApplication());
    }

    private static WebApplication createMockApplication() {
        return new MockApplication();
    }

    /**
     * check when everything is passed from constructor
     */
    @Test(groups = {"wicketTests"})
    public void constructor_1() {
        int oneBlock = 2;
        final String repeaterId = "repeater";
        IDataProvider<TestObj> provider = mockProvider(oneBlock);
        IQuickReuseStrategy reuse = Mockito.mock(IQuickReuseStrategy.class);
        QuickView<TestObj> repeater = new QuickView<TestObj>(repeaterId, provider, reuse, oneBlock) {

            @Override
            protected void populate(Item<TestObj> item) {
            }

        };
        repeater.setMarkupId("con");
        Assert.assertEquals(repeater.getReuseStrategy(), reuse);
        Assert.assertEquals(repeater.getDataProvider(), provider);
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
        Assert.assertTrue(repeater.getReuseStrategy() instanceof DefaultQuickReuseStrategy);
        Assert.assertEquals(repeater.getDataProvider(), provider);
        Assert.assertEquals(repeater.getItemsPerRequest(), oneBlock);

    }


    /**
     * reuse is set
     */

    @Test(groups = {"wicketTests"})
    public void constructor_3() {
        final String id = "connn", repeaterId = "repeat";
        IDataProvider<TestObj> provider = mockProvider(10);
        IQuickReuseStrategy reuse = Mockito.mock(IQuickReuseStrategy.class);
        QuickView<TestObj> repeater = new QuickView<TestObj>(repeaterId, provider, reuse) {

            @Override
            public void populate(Item<TestObj> item) {
            }
        };
        repeater.setMarkupId("con");
        Assert.assertEquals(repeater.getReuseStrategy(), reuse);
        Assert.assertEquals(repeater.getDataProvider(), provider);
        Assert.assertEquals(repeater.getItemsPerRequest(), Integer.MAX_VALUE);

    }


    @Test(groups = {"wicketTests"})
    public void constructor_4() {
        final String id = "connn", repeaterId = "repeat";
        IDataProvider<TestObj> provider = mockProvider(10);
        QuickView<TestObj> repeater = new QuickView<TestObj>(repeaterId, provider) {

            @Override
            public void populate(Item<TestObj> item) {
            }
        };

        repeater.setMarkupId("con");
        Assert.assertTrue(repeater.getReuseStrategy() instanceof DefaultQuickReuseStrategy);
        Assert.assertEquals(repeater.getDataProvider(), provider);
        Assert.assertEquals(repeater.getItemsPerRequest(), Integer.MAX_VALUE);

    }

    @Test(groups = {"utilTests"}, expectedExceptions = IllegalArgumentException.class)
    public void constructor_6() {
        IDataProvider data = Mockito.mock(IDataProvider.class);
        QuickView quickView = new QuickView("id", data, null) {
            @Override
            protected void populate(Item item) {
            }
        };

    }


    @Test(groups = {"wicketTests"})
    public void setReuseStrategy_1() {
        IQuickReuseStrategy strategy = Mockito.mock(IQuickReuseStrategy.class);
        int oneBlock = 12;
        final String id = "connn", repeaterId = "repeat";
        IDataProvider<TestObj> provider = mockProvider(oneBlock);
        QuickView<TestObj> repeater = new QuickView<TestObj>(repeaterId, provider, oneBlock) {

            @Override
            public void populate(Item<TestObj> item) {
            }
        };

        repeater.setReuseStrategy(strategy);
        Assert.assertEquals(repeater.getReuseStrategy(), strategy);
    }


    /**
     * add one component ,isajax=true
     */
    @Test(groups = {"wicketTests"})
    public void add_1() {
        final int itemsPerRequest = 2;
        final WebMarkupContainer parent = Mockito.mock(WebMarkupContainer.class);
        IDataProvider dataProvider = Mockito.mock(IDataProvider.class);
        final IRepeaterUtil util = mockRepeaterUtil();
        final TestObj to = Mockito.mock(TestObj.class);
        List<String> scripts = Mockito.mock(List.class);
        final QuickViewBase.Synchronizer synchronizer = Mockito.mock(QuickViewBase.Synchronizer.class);
        Mockito.when(synchronizer.getPrependScripts()).thenReturn(scripts);
        final Item c = Mockito.mock(Item.class);

        QuickView<TestObj> quickView = new QuickView<TestObj>("repeater", dataProvider, itemsPerRequest) {

            public void populate(Item<TestObj> item) {
            }

            public MarkupContainer _getParent() {
                return parent;
            }

            public MarkupContainer simpleAdd(Component... c) {
                return this;
            }


            public boolean isAjax() {
                return true;
            }

            @Override
            public Synchronizer getSynchronizer() {
                return synchronizer;
            }

            @Override
            public IRepeaterUtil getRepeaterUtil() {
                return util;
            }

        };
        final String script = "script..";
        Mockito.when(util.append(c, parent, null, null)).thenReturn(script);
        QuickView<TestObj> sparc = Mockito.spy(quickView);
        sparc.add(c);

        Mockito.verify(sparc, Mockito.times(1)).simpleAdd(c);
        Mockito.verify(synchronizer, Mockito.times(1)).add(c);
        Mockito.verify(scripts, Mockito.times(1)).add(script);

    }


    /**
     * add one component ,isajax=true
     */
    @Test(groups = {"wicketTests"})
    public void add_1_1() {
        final int itemsPerRequest = 2;
        final WebMarkupContainer parent = Mockito.mock(WebMarkupContainer.class);
        IDataProvider dataProvider = Mockito.mock(IDataProvider.class);
        final IRepeaterUtil util = mockRepeaterUtil();
        final TestObj to = Mockito.mock(TestObj.class);
        List<String> scripts = Mockito.mock(List.class);
        final QuickViewBase.Synchronizer synchronizer = Mockito.mock(QuickViewBase.Synchronizer.class);
        Mockito.when(synchronizer.getPrependScripts()).thenReturn(scripts);
        final Item c = Mockito.mock(Item.class);
        Component start = new Label("start");
        Component end = new Label("end");
        QuickView<TestObj> quickView = new QuickView<TestObj>("repeater", dataProvider, itemsPerRequest, start, end) {

            public void populate(Item<TestObj> item) {
            }

            public MarkupContainer _getParent() {
                return parent;
            }

            public MarkupContainer simpleAdd(Component... c) {
                return this;
            }


            public boolean isAjax() {
                return true;
            }

            @Override
            public Synchronizer getSynchronizer() {
                return synchronizer;
            }

            @Override
            public IRepeaterUtil getRepeaterUtil() {
                return util;
            }

        };
        final String script = "script..";
        Mockito.when(util.append(c, parent, start, end)).thenReturn(script);
        QuickView<TestObj> sparc = Mockito.spy(quickView);
        sparc.add(c);

        Mockito.verify(sparc, Mockito.times(1)).simpleAdd(c);
        Mockito.verify(synchronizer, Mockito.times(1)).add(c);
        Mockito.verify(scripts, Mockito.times(1)).add(script);

    }

    /**
     * add two components ,isajax=true
     */
    @Test(groups = {"wicketTests"})
    public void add_2() {
        int oneBlock = 2;
        List<String> scripts = Mockito.mock(List.class);
        final QuickViewBase.Synchronizer synchronizer = Mockito.mock(QuickViewBase.Synchronizer.class);
        Mockito.when(synchronizer.getPrependScripts()).thenReturn(scripts);
        final WebMarkupContainer parent = Mockito.mock(WebMarkupContainer.class);
        IDataProvider dataProvider = Mockito.mock(IDataProvider.class);
        final IRepeaterUtil util = mockRepeaterUtil();
        final TestObj to = Mockito.mock(TestObj.class);
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


            public boolean isAjax() {
                return true;
            }


            @Override
            public IRepeaterUtil getRepeaterUtil() {
                return util;
            }

            @Override
            public Synchronizer getSynchronizer() {
                return synchronizer;
            }
        };
        final String script = "script..", script2 = "script2!";
        Mockito.when(util.append(c, parent, null, null)).thenReturn(script);
        Mockito.when(util.append(c2, parent, null, null)).thenReturn(script2);
        QuickView sparc = Mockito.spy(arc);
        sparc.add(c, c2);
        Mockito.verify(sparc, Mockito.times(1)).simpleAdd(c, c2);
        Mockito.verify(synchronizer, Mockito.times(1)).add(c, c2);
        Mockito.verify(scripts, Mockito.times(1)).add(script);
        Mockito.verify(scripts, Mockito.times(1)).add(script2);

    }


    /**
     * add two components ,isajax=true
     */
    @Test(groups = {"wicketTests"})
    public void add_2_2() {
        int oneBlock = 2;
        List<String> scripts = Mockito.mock(List.class);
        final QuickViewBase.Synchronizer synchronizer = Mockito.mock(QuickViewBase.Synchronizer.class);
        Mockito.when(synchronizer.getPrependScripts()).thenReturn(scripts);
        final WebMarkupContainer parent = Mockito.mock(WebMarkupContainer.class);
        IDataProvider dataProvider = Mockito.mock(IDataProvider.class);
        final IRepeaterUtil util = mockRepeaterUtil();
        final TestObj to = Mockito.mock(TestObj.class);
        final Item c = Mockito.mock(Item.class);
        final Item c2 = Mockito.mock(Item.class);
        Component start = new Label("start");
        Component end = new Label("end");
        QuickView<TestObj> arc = new QuickView<TestObj>("repeater", dataProvider, oneBlock, start, end) {

            public void populate(Item<TestObj> item) {
            }

            public MarkupContainer _getParent() {
                return parent;
            }

            public MarkupContainer simpleAdd(Component... c) {
                return this;
            }


            public boolean isAjax() {
                return true;
            }


            @Override
            public IRepeaterUtil getRepeaterUtil() {
                return util;
            }

            @Override
            public Synchronizer getSynchronizer() {
                return synchronizer;
            }
        };
        final String script = "script..", script2 = "script2!";
        Mockito.when(util.append(c, parent, start, end)).thenReturn(script);
        Mockito.when(util.append(c2, parent, start, end)).thenReturn(script2);
        QuickView sparc = Mockito.spy(arc);
        sparc.add(c, c2);
        Mockito.verify(sparc, Mockito.times(1)).simpleAdd(c, c2);
        Mockito.verify(synchronizer, Mockito.times(1)).add(c, c2);
        Mockito.verify(scripts, Mockito.times(1)).add(script);
        Mockito.verify(scripts, Mockito.times(1)).add(script2);

    }


    /**
     * add one component ,isajax=false
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

            public boolean isAjax() {
                return false;
            }


            @Override
            public IRepeaterUtil getRepeaterUtil() {
                return util;
            }


        };
        final String script = "script..";
        Mockito.when(util.append(c, parent, null, null)).thenReturn(script);
        QuickView<TestObj> sparc = Mockito.spy(arc);
        sparc.add(c);

        Mockito.verify(sparc, Mockito.times(1)).simpleAdd(c);
        Mockito.verify(target, Mockito.never()).add(c);
        Mockito.verify(target, Mockito.never()).prependJavaScript(script);

    }


    /**
     * addNewItems(object1,object2)
     */
    @Test(groups = {"wicketTests"})
    public void addNewItems_1() {
        int oneBlock = 2;
        final WebMarkupContainer parent = Mockito.mock(WebMarkupContainer.class);
        IDataProvider dataProvider = Mockito.mock(IDataProvider.class);
        final IRepeaterUtil util = mockRepeaterUtil();
        TestObj obj1 = Mockito.mock(TestObj.class);
        TestObj obj2 = Mockito.mock(TestObj.class);
        final Iterator<Item<TestObj>> newItems = Mockito.mock(Iterator.class);
        final int size = 12;
        final int index1 = size;
        final int index2 = size + 1;
        final Item<TestObj> item1 = new Item<TestObj>("123", index1, new Model<TestObj>(obj1));
        final Item<TestObj> item2 = new Item<TestObj>("124", index2, new Model<TestObj>(obj2));
        Mockito.when(newItems.next()).thenReturn(item1).thenReturn(item2);
        Mockito.when(newItems.hasNext()).thenReturn(true).thenReturn(true).thenReturn(false);

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
            public IRepeaterUtil getRepeaterUtil() {
                return util;
            }


            @Override
            public Item buildItem(long index, TestObj object) {
                return null;
            }

            @Override
            public MarkupContainer add(Component... components) {
                return this;
            }

            @Override
            public int size() {
                return size;
            }
        };

        QuickView<TestObj> sparc = Mockito.spy(arc);
        Mockito.when(sparc.buildItem(index1, obj1)).thenReturn(item1);
        Mockito.when(sparc.buildItem(index2, obj2)).thenReturn(item2);
        sparc.addNewItems(obj1, obj2);
        Mockito.verify(sparc, Mockito.times(1)).buildItem(index1, obj1);
        Mockito.verify(sparc, Mockito.times(1)).buildItem(index2, obj2);
        Mockito.verify(sparc, Mockito.times(1)).add(item1);
        Mockito.verify(sparc, Mockito.times(1)).add(item2);

    }


    /**
     * addNewItemsAtStart(T ... object)
     */
    @Test(groups = {"wicketTests"})
    public void addNewItemsAtStart_1() {
        int oneBlock = 2;
        final WebMarkupContainer parent = Mockito.mock(WebMarkupContainer.class);
        IDataProvider dataProvider = Mockito.mock(IDataProvider.class);
        final IRepeaterUtil util = mockRepeaterUtil();
        TestObj obj1 = Mockito.mock(TestObj.class);
        TestObj obj2 = Mockito.mock(TestObj.class);
        final Iterator<Item<TestObj>> newItems = Mockito.mock(Iterator.class);
        final int size = 12;
        final int index1 = size;
        final int index2 = size + 1;
        final Item<TestObj> item1 = new Item<TestObj>("123", index1, new Model<TestObj>(obj1));
        final Item<TestObj> item2 = new Item<TestObj>("124", index2, new Model<TestObj>(obj2));
        Mockito.when(newItems.next()).thenReturn(item1).thenReturn(item2);
        Mockito.when(newItems.hasNext()).thenReturn(true).thenReturn(true).thenReturn(false);

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
            public IRepeaterUtil getRepeaterUtil() {
                return util;
            }


            @Override
            public Item buildItem(long index, TestObj object) {
                return null;
            }

            @Override
            public MarkupContainer add(Component... components) {
                return this;
            }

            @Override
            public int size() {
                return size;
            }
        };

        QuickView<TestObj> sparc = Mockito.spy(arc);
        Mockito.when(sparc.buildItem(index1, obj1)).thenReturn(item1);
        Mockito.when(sparc.buildItem(index2, obj2)).thenReturn(item2);
        sparc.addNewItemsAtStart(obj1, obj2);
        Mockito.verify(sparc, Mockito.times(1)).buildItem(index1, obj1);
        Mockito.verify(sparc, Mockito.times(1)).buildItem(index2, obj2);
        Mockito.verify(sparc, Mockito.times(1)).addAtStart(item1, item2);

    }


    @Test(groups = {"wicketTests"})
    public void remove_1() {
        final int itemsPerRequest = 2;
        final WebMarkupContainer parent = Mockito.mock(WebMarkupContainer.class);
        IDataProvider dataProvider = Mockito.mock(IDataProvider.class);
        final IRepeaterUtil util = mockRepeaterUtil();
        final TestObj to = Mockito.mock(TestObj.class);
        final Item c = Mockito.mock(Item.class);
        final String itemId = "item";
        Mockito.when(c.getMarkupId()).thenReturn(itemId);
        List<String> scripts = Mockito.mock(List.class);
        final QuickViewBase.Synchronizer synchronizer = Mockito.mock(QuickViewBase.Synchronizer.class);
        Mockito.when(synchronizer.getPrependScripts()).thenReturn(scripts);
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

            public boolean isAjax() {
                return true;
            }

            @Override
            public Synchronizer getSynchronizer() {
                return synchronizer;
            }

            @Override
            public IRepeaterUtil getRepeaterUtil() {
                return util;
            }


        };
        final String script = "script..";
        Mockito.when(util.removeItem(c, parent)).thenReturn(script);
        QuickView spy = Mockito.spy(arc);
        spy.remove(c);
        Mockito.verify(spy, Mockito.times(1)).simpleRemove(c);
        Mockito.verify(synchronizer, Mockito.never()).add(c);
        Mockito.verify(scripts, Mockito.times(1)).add(script);

    }

    /**
     * one component added ,ajax=true ,parent added=false
     */
    @Test(groups = {"wicketTests"})
    public void addAtStart_1() {
        final int itemsPerRequest = 2;
        final WebMarkupContainer parent = Mockito.mock(WebMarkupContainer.class);
        IDataProvider dataProvider = Mockito.mock(IDataProvider.class);
        final IRepeaterUtil util = mockRepeaterUtil();
        final TestObj to = Mockito.mock(TestObj.class);
        List<String> scripts = Mockito.mock(List.class);
        final QuickViewBase.Synchronizer synchronizer = Mockito.mock(QuickViewBase.Synchronizer.class);
        Mockito.when(synchronizer.getPrependScripts()).thenReturn(scripts);
        final Item c = Mockito.mock(Item.class);

        QuickView<TestObj> quickView = new QuickView<TestObj>("repeater", dataProvider, itemsPerRequest) {

            public void populate(Item<TestObj> item) {
            }

            public MarkupContainer _getParent() {
                return parent;
            }

            public MarkupContainer simpleAdd(Component... c) {
                return this;
            }

            public boolean isAjax() {
                return true;
            }

            @Override
            public Synchronizer getSynchronizer() {
                return synchronizer;
            }

            @Override
            public IRepeaterUtil getRepeaterUtil() {
                return util;
            }

        };
        final String script = "script..";
        Mockito.when(util.prepend(c, parent, null, null)).thenReturn(script);
        QuickView sparc = Mockito.spy(quickView);
        sparc.addAtStart(c);
        Mockito.verify(sparc, Mockito.times(1)).simpleAdd(c);
        Mockito.verify(synchronizer, Mockito.times(1)).add(c);
        Mockito.verify(scripts, Mockito.times(1)).add(script);

    }


    /**
     * one component added ,ajax=true ,parent added=false
     */
    @Test(groups = {"wicketTests"})
    public void addAtStart_1_1() {
        final int itemsPerRequest = 2;
        final WebMarkupContainer parent = Mockito.mock(WebMarkupContainer.class);
        IDataProvider dataProvider = Mockito.mock(IDataProvider.class);
        final IRepeaterUtil util = mockRepeaterUtil();
        final TestObj to = Mockito.mock(TestObj.class);
        List<String> scripts = Mockito.mock(List.class);
        final QuickViewBase.Synchronizer synchronizer = Mockito.mock(QuickViewBase.Synchronizer.class);
        Mockito.when(synchronizer.getPrependScripts()).thenReturn(scripts);
        final Item c = Mockito.mock(Item.class);
        Component start = new Label("start");
        Component end = new Label("end");
        QuickView<TestObj> quickView = new QuickView<TestObj>("repeater", dataProvider, itemsPerRequest, start, end) {

            public void populate(Item<TestObj> item) {
            }

            public MarkupContainer _getParent() {
                return parent;
            }

            public MarkupContainer simpleAdd(Component... c) {
                return this;
            }

            public boolean isAjax() {
                return true;
            }

            @Override
            public Synchronizer getSynchronizer() {
                return synchronizer;
            }

            @Override
            public IRepeaterUtil getRepeaterUtil() {
                return util;
            }

        };
        final String script = "script..";
        Mockito.when(util.prepend(c, parent, start, end)).thenReturn(script);
        QuickView sparc = Mockito.spy(quickView);
        sparc.addAtStart(c);
        Mockito.verify(sparc, Mockito.times(1)).simpleAdd(c);
        Mockito.verify(synchronizer, Mockito.times(1)).add(c);
        Mockito.verify(scripts, Mockito.times(1)).add(script);

    }


    /**
     * add two components ,isajax=true
     */
    @Test(groups = {"wicketTests"})
    public void addAtStart_2() {
        int oneBlock = 2;
        List<String> scripts = Mockito.mock(List.class);
        final QuickViewBase.Synchronizer synchronizer = Mockito.mock(QuickViewBase.Synchronizer.class);
        Mockito.when(synchronizer.getPrependScripts()).thenReturn(scripts);
        final WebMarkupContainer parent = Mockito.mock(WebMarkupContainer.class);
        IDataProvider dataProvider = Mockito.mock(IDataProvider.class);
        final IRepeaterUtil util = mockRepeaterUtil();
        final TestObj to = Mockito.mock(TestObj.class);
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

            public boolean isAjax() {
                return true;
            }


            @Override
            public IRepeaterUtil getRepeaterUtil() {
                return util;
            }

            @Override
            public Synchronizer getSynchronizer() {
                return synchronizer;
            }
        };
        final String script = "script..", script2 = "script2!";
        Mockito.when(util.prepend(c, parent, null, null)).thenReturn(script);
        Mockito.when(util.prepend(c2, parent, null, null)).thenReturn(script2);
        QuickView sparc = Mockito.spy(arc);
        sparc.addAtStart(c, c2);
        Mockito.verify(sparc, Mockito.times(1)).simpleAdd(c, c2);
        Mockito.verify(synchronizer, Mockito.times(1)).add(c, c2);
        //
        // verifying scripts for components added in reverse order
        // as the script simply prepends the element at top
        // so the script for first elemnt should be added later
        //
        //
        InOrder order = Mockito.inOrder(scripts);
        order.verify(scripts).add(script2);
        order.verify(scripts).add(script);
    }


    /**
     * add two components ,isajax=true
     */
    @Test(groups = {"wicketTests"})
    public void addAtStart_2_2() {
        int oneBlock = 2;
        List<String> scripts = Mockito.mock(List.class);
        final QuickViewBase.Synchronizer synchronizer = Mockito.mock(QuickViewBase.Synchronizer.class);
        Mockito.when(synchronizer.getPrependScripts()).thenReturn(scripts);
        final WebMarkupContainer parent = Mockito.mock(WebMarkupContainer.class);
        IDataProvider dataProvider = Mockito.mock(IDataProvider.class);
        final IRepeaterUtil util = mockRepeaterUtil();
        final TestObj to = Mockito.mock(TestObj.class);
        final Item c = Mockito.mock(Item.class);
        final Item c2 = Mockito.mock(Item.class);
        Component start = new Label("start");
        Component end = new Label("end");
        QuickView<TestObj> arc = new QuickView<TestObj>("repeater", dataProvider, oneBlock, start, end) {

            public void populate(Item<TestObj> item) {
            }

            public MarkupContainer _getParent() {
                return parent;
            }

            public MarkupContainer simpleAdd(Component... c) {
                return this;
            }

            public boolean isAjax() {
                return true;
            }


            @Override
            public IRepeaterUtil getRepeaterUtil() {
                return util;
            }

            @Override
            public Synchronizer getSynchronizer() {
                return synchronizer;
            }
        };
        final String script = "script..", script2 = "script2!";
        Mockito.when(util.prepend(c, parent, start, end)).thenReturn(script);
        Mockito.when(util.prepend(c2, parent, start, end)).thenReturn(script2);
        QuickView sparc = Mockito.spy(arc);
        sparc.addAtStart(c, c2);
        Mockito.verify(sparc, Mockito.times(1)).simpleAdd(c, c2);
        Mockito.verify(synchronizer, Mockito.times(1)).add(c, c2);
        Mockito.verify(scripts, Mockito.times(1)).add(script);
        Mockito.verify(scripts, Mockito.times(1)).add(script2);

    }


    /**
     * current page=5  ,reuse#getPageCreatedOnRender() is zero  ,reuse#isAddItemsSuypported() is true
     *
     *  for eg. ItemsNaviagtionStrategy
     */
    @Test(groups = {"wicketTests"})
    public void onPopulate_1() {
        IDataProvider provider = Mockito.mock(IDataProvider.class);
        final int currentPage = 5;
        final IRepeaterUtil util = Mockito.mock(IRepeaterUtil.class);
        IQuickReuseStrategy reuse = Mockito.mock(IQuickReuseStrategy.class);
        //
        // add items supported
        //
        Mockito.when(reuse.isPartialUpdatesSupported()).thenReturn(true);
        //
        //page not set from reuse strategy
        //
        Mockito.when(reuse.getPageCreatedOnRender()).thenReturn(0l);
        final IItemFactory factory = Mockito.mock(IItemFactory.class);
        final Iterator existing = Mockito.mock(Iterator.class);
        final Iterator newModels = Mockito.mock(Iterator.class);
        final Iterator newItems = Mockito.mock(Iterator.class);
        Iterator data = Mockito.mock(Iterator.class);
        final int itemsPerRequest = 2;
        final int offset = currentPage * itemsPerRequest;
        Mockito.when(reuse.getItems(factory, newModels, existing)).thenReturn(newItems);
        QuickView repeater = new QuickView("repeater", provider, reuse, itemsPerRequest) {

            @Override
            public Iterator<Component> itemsIterator() {
                return existing;
            }

            @Override
            public IRepeaterUtil getRepeaterUtil() {
                return util;
            }

            @Override
            protected void populate(Item item) {
            }

            @Override
            public MarkupContainer simpleAdd(Component... c) {
                return this;
            }


            @Override
            public MarkupContainer simpleRemoveAll() {
                return this;
            }

            @Override
            protected void createChildren(Iterator iterator) {
            }

            @Override
            public IItemFactory factory() {
                return factory;
            }
        };
        QuickView spy = Mockito.spy(repeater);
        Mockito.doReturn(newModels).when(spy).newModels(0, itemsPerRequest);
        spy.onPopulate();

        InOrder order = Mockito.inOrder(reuse, spy, provider);
        order.verify(spy).newModels(0, itemsPerRequest);
        order.verify(reuse).getItems(factory, newModels, existing);
        order.verify(spy).simpleRemoveAll();
        order.verify(spy).createChildren(newItems);
        Mockito.verify(spy)._setCurrentPage(0);

    }

    /**
     * current page=5  ,reuse#getPageCreatedOnRender() is -1 ,reuse#isAddItemsSuppored()==false
     * for eg. Any AbstractPagingNavigationStrategy
     *
     */
    @Test(groups = {"wicketTests"})
    public void onPopulate_2() {
        IDataProvider provider = Mockito.mock(IDataProvider.class);
        final long currentPage = 5;
        final IRepeaterUtil util = Mockito.mock(IRepeaterUtil.class);
        IQuickReuseStrategy reuse = Mockito.mock(IQuickReuseStrategy.class);
        //
        // add items supported
        //
        Mockito.when(reuse.isPartialUpdatesSupported()).thenReturn(false);
        //
        //page not set from reuse strategy
        //
        Mockito.when(reuse.getPageCreatedOnRender()).thenReturn(-1l);
        final IItemFactory factory = Mockito.mock(IItemFactory.class);
        final Iterator existing = Mockito.mock(Iterator.class);
        final Iterator newModels = Mockito.mock(Iterator.class);
        final Iterator newItems = Mockito.mock(Iterator.class);
        Iterator data = Mockito.mock(Iterator.class);
        final int itemsPerRequest = 2;
        // final int offset = currentPage * itemsPerRequest;
        Mockito.when(reuse.getItems(factory, newModels, existing)).thenReturn(newItems);

        QuickView repeater = new QuickView("repeater", provider, reuse, itemsPerRequest) {

            @Override
            public Iterator<Component> itemsIterator() {
                return existing;
            }

            @Override
            public IRepeaterUtil getRepeaterUtil() {
                return util;
            }

            @Override
            protected void populate(Item item) {
            }

            @Override
            public MarkupContainer simpleAdd(Component... c) {
                return this;
            }


            @Override
            public MarkupContainer simpleRemoveAll() {
                return this;
            }

            @Override
            protected void createChildren(Iterator iterator) {
            }

            @Override
            public IItemFactory factory() {
                return factory;
            }
        };
        QuickView spy = Mockito.spy(repeater);
        Mockito.doReturn(newModels).when(spy).newModels((currentPage * itemsPerRequest), itemsPerRequest);
        Mockito.doReturn(currentPage).when(spy)._getCurrentPage();
        spy.onPopulate();

        InOrder order = Mockito.inOrder(reuse, spy, provider);
        order.verify(spy).newModels((currentPage * itemsPerRequest), itemsPerRequest);
        order.verify(reuse).getItems(factory, newModels, existing);
        order.verify(spy).simpleRemoveAll();
        order.verify(spy).createChildren(newItems);
        Mockito.verify(spy, Mockito.never())._setCurrentPage(Mockito.anyLong());
    }


    /**
     * current page=5  ,reuse#getPageCreatedOnRender() is -1 ,reuse#isAddItemsSuppored()==false
     * foreg. ReuseAllStrategy
     */
    @Test(groups = {"wicketTests"})
    public void onPopulate_3() {
        IDataProvider provider = Mockito.mock(IDataProvider.class);
        final long currentPage = 5;
        final IRepeaterUtil util = Mockito.mock(IRepeaterUtil.class);
        IQuickReuseStrategy reuse = Mockito.mock(IQuickReuseStrategy.class);
        //
        // add items supported
        //
        Mockito.when(reuse.isPartialUpdatesSupported()).thenReturn(true);
        //
        //page not set from reuse strategy
        //
        Mockito.when(reuse.getPageCreatedOnRender()).thenReturn(-1l);
        final IItemFactory factory = Mockito.mock(IItemFactory.class);
        final Iterator existing = Mockito.mock(Iterator.class);
        final Iterator newModels = Mockito.mock(Iterator.class);
        final Iterator newItems = Mockito.mock(Iterator.class);
        Iterator data = Mockito.mock(Iterator.class);
        final int itemsPerRequest = 2;
        Mockito.when(reuse.getItems(factory, newModels, existing)).thenReturn(newItems);

        QuickView repeater = new QuickView("repeater", provider, reuse, itemsPerRequest) {

            @Override
            public Iterator<Component> itemsIterator() {
                return existing;
            }

            @Override
            public IRepeaterUtil getRepeaterUtil() {
                return util;
            }

            @Override
            protected void populate(Item item) {
            }

            @Override
            public MarkupContainer simpleAdd(Component... c) {
                return this;
            }


            @Override
            public MarkupContainer simpleRemoveAll() {
                return this;
            }

            @Override
            protected void createChildren(Iterator iterator) {
            }

            @Override
            public IItemFactory factory() {
                return factory;
            }
        };
        QuickView spy = Mockito.spy(repeater);
        Mockito.doReturn(newModels).when(spy).newModels(0, (currentPage + 1) * itemsPerRequest);
        Mockito.doReturn(currentPage).when(spy)._getCurrentPage();
        spy.onPopulate();

        InOrder order = Mockito.inOrder(reuse, spy, provider);
        order.verify(spy).newModels(0, (currentPage + 1) * itemsPerRequest);
        order.verify(reuse).getItems(factory, newModels, existing);
        order.verify(spy).simpleRemoveAll();
        order.verify(spy).createChildren(newItems);
        Mockito.verify(spy, Mockito.never())._setCurrentPage(Mockito.anyLong());

    }


    @Test(groups = {"wicketTests"})
    public void createChildren_1() {
        IDataProvider provider = Mockito.mock(IDataProvider.class);
        Iterator<Item> children = Mockito.mock(Iterator.class);
        final Item item1 = new Item("1", 1);
        final Item item2 = new Item("2", 2);
        Mockito.when(children.next()).thenReturn(item1).thenReturn(item2);
        Mockito.when(children.hasNext()).thenReturn(true).thenReturn(true).thenReturn(false);
        QuickView repeater = new QuickView("repeater", provider) {

            @Override
            protected void populate(Item item) {
            }

            @Override
            public MarkupContainer simpleAdd(Component... c) {
                return this;
            }


            @Override
            public MarkupContainer simpleRemoveAll() {
                return this;
            }


        };

        QuickView spy = Mockito.spy(repeater);
        spy.createChildren(children);
        Mockito.verify(spy, Mockito.times(1)).simpleAdd(item1);
        Mockito.verify(spy, Mockito.times(1)).simpleAdd(item2);

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
        QuickView spy = Mockito.spy(repeater);
        spy.setItemsPerRequest(itemsPerRequest);
        Assert.assertEquals(spy.getItemsPerRequest(), 3);
        Mockito.verify(spy, Mockito.times(1))._setCurrentPage(0);
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

    /**
     * itemsPerRequest changed
     */
    @Test(groups = {"wicketTests"})
    public void setItemsPerRequest_3() {
        final int oldItemsPerRequest = 3;
        final int newItemsPerRequest = 5;
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
        repeater.setItemsPerRequest(oldItemsPerRequest);
        QuickView spy = Mockito.spy(repeater);
        spy.setItemsPerRequest(newItemsPerRequest);
        Assert.assertEquals(spy.getItemsPerRequest(), newItemsPerRequest);
        Mockito.verify(spy, Mockito.times(1)).setItemsPerRequest(newItemsPerRequest);
        Mockito.verify(spy, Mockito.times(1))._setCurrentPage(0);
    }

    /**
     * itemsPerRequest not changed  ie. if itemsPerRequest is not changed but it's set again
     */
    @Test(groups = {"wicketTests"})
    public void setItemsPerRequest_4() {
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
        QuickView spy = Mockito.spy(repeater);
        spy.setItemsPerRequest(itemsPerRequest);
        Assert.assertEquals(spy.getItemsPerRequest(), itemsPerRequest);
        Mockito.verify(spy, Mockito.never())._setCurrentPage(0);
    }

    /**
     * components not empty  and repeater's parent is added to another component which is added to A.R.T
     */
    @Test(groups = {"wicketTests"})
    public void Synchronizer_isParentAdded_1() {

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
        QuickViewBase.Synchronizer synchronizer = new QuickViewBase.Synchronizer(parent) {
            @Override
            protected Boolean addNewChildVisitor(MarkupContainer parent, Component searchFor) {
                return true;
            }

        };
        QuickViewBase.Synchronizer spy = Mockito.spy(synchronizer);
        spy.isParentAddedInAjaxRequestTarget(target);
        Mockito.verify(spy, Mockito.times(1)).addNewChildVisitor(one, parent);
        Mockito.verify(spy, Mockito.never()).addNewChildVisitor(two, parent);

    }


    /**
     * components not empty  and repeater's parent is added directly in A.R.T
     */
    @Test(groups = {"wicketTests"})
    public void Synchronizer_isParentAdded_2() {
        IDataProvider provider = Mockito.mock(IDataProvider.class);
        final AjaxRequestTarget target = Mockito.mock(AjaxRequestTarget.class);
        List cs = new ArrayList();
        Mockito.when(target.getComponents()).thenReturn(cs);
        final WebMarkupContainer one = Mockito.mock(WebMarkupContainer.class);
        WebMarkupContainer two = Mockito.mock(WebMarkupContainer.class);
        // Label three = Mockito.mock(Label.class);
        cs.add(one);
        cs.add(two);
        WebMarkupContainer parent = Mockito.mock(WebMarkupContainer.class);
        QuickViewBase.Synchronizer synchronizer = new QuickViewBase.Synchronizer(parent) {
            @Override
            protected Boolean addNewChildVisitor(MarkupContainer parent, Component searchFor) {
                return true;
            }

        };
        QuickViewBase.Synchronizer spy = Mockito.spy(synchronizer);
        spy.isParentAddedInAjaxRequestTarget(target);
        Mockito.verify(spy, Mockito.never()).addNewChildVisitor(one, one);
        Mockito.verify(spy, Mockito.never()).addNewChildVisitor(two, one);

    }


    /**
     * components is empty  and repeater's parent is added directly in A.R.T
     */
    @Test(groups = {"wicketTests"})
    public void Synchronizer_isParentAdded_3() {
        IDataProvider provider = Mockito.mock(IDataProvider.class);
        final AjaxRequestTarget target = Mockito.mock(AjaxRequestTarget.class);
        List cs = new ArrayList();
        Mockito.when(target.getComponents()).thenReturn(cs);
        WebMarkupContainer parent = Mockito.mock(WebMarkupContainer.class);
        QuickViewBase.Synchronizer synchronizer = new QuickViewBase.Synchronizer(parent) {
            @Override
            protected Boolean addNewChildVisitor(MarkupContainer parent, Component searchFor) {
                return true;
            }

        };
        QuickViewBase.Synchronizer spy = Mockito.spy(synchronizer);
        spy.isParentAddedInAjaxRequestTarget(target);
        Mockito.verify(spy, Mockito.never()).addNewChildVisitor(Mockito.any(MarkupContainer.class), Mockito.any(MarkupContainer.class));
    }

    /**
     * parent not added in ARjaxRequestTarget
     */
    @Test(groups = {"utilTests"})
    public void Synchronizer_onBeforeRespond_1() {
        final MarkupContainer parent = Mockito.mock(MarkupContainer.class);
        QuickViewBase.Synchronizer synchronizer = new QuickViewBase.Synchronizer(parent) {
            @Override
            public boolean isParentAddedInAjaxRequestTarget(AjaxRequestTarget target) {
                return false;
            }
        };
        final Item item1 = new Item("1", 1, new Model(1));
        final Item item2 = new Item("2", 2, new Model(2));

        final String script1 = "script1", script2 = "script2..";
        synchronizer.getPrependScripts().add(script1);
        synchronizer.getPrependScripts().add(script2);
        synchronizer.add(item1, item2);
        Map map = new HashMap();
        AjaxRequestTarget target = Mockito.mock(AjaxRequestTarget.class);
        synchronizer.onBeforeRespond(map, target);
        Mockito.verify(target, Mockito.times(1)).add(item1, item2);
        Mockito.verify(target, Mockito.times(1)).prependJavaScript(script1);
        Mockito.verify(target, Mockito.times(1)).prependJavaScript(script2);
    }

    /**
     * parent added in ARjaxRequestTarget
     */
    @Test(groups = {"utilTests"})
    public void Synchronizer_onBeforeRespond_2() {
        final MarkupContainer parent = Mockito.mock(MarkupContainer.class);
        QuickViewBase.Synchronizer synchronizer = new QuickViewBase.Synchronizer(parent) {
            @Override
            public boolean isParentAddedInAjaxRequestTarget(AjaxRequestTarget target) {
                return true;
            }
        };
        final Item item1 = new Item("1", 1, new Model(1));
        final Item item2 = new Item("2", 2, new Model(2));

        final String script1 = "script1", script2 = "script2..";
        synchronizer.getPrependScripts().add(script1);
        synchronizer.getPrependScripts().add(script2);
        synchronizer.add(item1, item2);
        Map map = new HashMap();
        AjaxRequestTarget target = Mockito.mock(AjaxRequestTarget.class);
        synchronizer.onBeforeRespond(map, target);
        Mockito.verify(target, Mockito.never()).add(item1, item2);
        Mockito.verify(target, Mockito.never()).prependJavaScript(script1);
        Mockito.verify(target, Mockito.never()).prependJavaScript(script2);
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


    @Test(groups = {"wicketTests"})
    public void simpleRemove() {
        IDataProvider data = Mockito.mock(IDataProvider.class);
        QuickView quickView = new QuickView("id", data) {
            @Override
            protected void populate(Item item) {
            }
        };
        Item one = quickView.buildItem(89, 67);
        Item two = quickView.buildItem(90, 68);
        quickView.simpleAdd(one, two);
        quickView.simpleRemove(one);
        Assert.assertEquals(quickView.size(), 1);
    }

    /**
     * added two,removed all
     */
    @Test(groups = {"wicketTests"})
    public void simpleRemoveAll() {
        IDataProvider data = Mockito.mock(IDataProvider.class);
        QuickView quickView = new QuickView("id", data) {
            @Override
            protected void populate(Item item) {
            }
        };
        int index = 178;
        int index2 = 179;
        Item one = quickView.buildItem(index, 67);
        Item two = quickView.buildItem(index2, 68);
        quickView.simpleAdd(one, two);
        quickView.simpleRemoveAll();
        Assert.assertEquals(quickView.size(), 0);
    }

    @Test(groups = {"wicketTests"})
    public void simpleAdd() {
        IDataProvider data = Mockito.mock(IDataProvider.class);
        QuickView quickView = new QuickView("id", data) {
            @Override
            protected void populate(Item item) {
            }
        };
        Item one = quickView.buildItem(78, 67);
        quickView.simpleAdd(one);
        Assert.assertEquals(quickView.size(), 1);
        Item two = quickView.buildItem(79, 68);
        quickView.simpleAdd(two);
        Assert.assertEquals(quickView.size(), 2);

    }


    @Test(groups = {"wicketTests"})
    public void newItem_1() {
        final int object = 89;
        final Model<Integer> model = new Model<Integer>(object);
        IDataProvider<Integer> data = Mockito.mock(IDataProvider.class);
        Mockito.when(data.model(object)).thenReturn(model);
        QuickView<Integer> quickView = new QuickView<Integer>("id", data) {
            @Override
            protected void populate(Item<Integer> item) {
            }
        };
        final long index = 9l;
        final String id = "67";
        Item<Integer> item = quickView.newItem(id, index, model);
        Assert.assertEquals(item.getModelObject().intValue(), 89);
        Assert.assertEquals(item.getId(), id);
        Assert.assertEquals(item.getIndex(), index);
        Assert.assertTrue(item.getOutputMarkupId());
    }


    /**
     * test for  {@link QuickViewBase#buildItem(String, long, IModel)} )}
     */
    @Test(groups = {"wicketTests"})
    public void buildItem_1() {
        IDataProvider data = Mockito.mock(IDataProvider.class);
        IModel model = Mockito.mock(IModel.class);

        final Item item = Mockito.mock(Item.class);
        QuickView<TestObj> quickView = new QuickView<TestObj>("id", data) {
            @Override
            protected void populate(Item item) {
            }

            @Override
            protected Item<TestObj> newItem(String id, long index, IModel<TestObj> model) {
                return item;
            }
        };
        final long index = 9l;
        final String id = "87";
        final TestObj object = Mockito.mock(TestObj.class);
        Mockito.when(model.getObject()).thenReturn(object);
        Mockito.when(data.model(object)).thenReturn(model);
        QuickView<TestObj> spy = Mockito.spy(quickView);
        Item<TestObj> actual = spy.buildItem(id, index, object);
        Assert.assertEquals(actual, item);
        InOrder order = Mockito.inOrder(spy, item);
        order.verify(spy, Mockito.times(1)).newItem(id, index, model);
        order.verify(spy, Mockito.times(1)).populate(item);
    }

    /**
     * test for  {@link QuickViewBase#buildItem(String, long, IModel)} )}
     */
    @Test(groups = {"wicketTests"})
    public void buildItem_2() {
        IDataProvider data = Mockito.mock(IDataProvider.class);
        IModel model = Mockito.mock(IModel.class);

        final Item item = Mockito.mock(Item.class);
        QuickView<TestObj> quickView = new QuickView<TestObj>("id", data) {
            @Override
            protected void populate(Item item) {
            }

            @Override
            protected Item<TestObj> newItem(String id, long index, IModel<TestObj> model) {
                return item;
            }
        };
        final long index = 9l;
        final String id = "87";
        final TestObj object = Mockito.mock(TestObj.class);
        Mockito.when(model.getObject()).thenReturn(object);
        Mockito.when(data.model(object)).thenReturn(model);
        QuickView<TestObj> spy = Mockito.spy(quickView);
        Item<TestObj> actual = spy.buildItem(id, index, model);
        Assert.assertEquals(actual, item);
        InOrder order = Mockito.inOrder(spy, item);
        order.verify(spy, Mockito.times(1)).newItem(id, index, model);
        order.verify(spy, Mockito.times(1)).populate(item);
    }


    /**
     * test for  {@link QuickViewBase#buildItem(long, Object)}
     */
    @Test(groups = {"wicketTests"})
    public void buildItem_3() {
        IDataProvider data = Mockito.mock(IDataProvider.class);
        final Item item = Mockito.mock(Item.class);
        final String childId = "78";
        QuickView<TestObj> quickView = new QuickView<TestObj>("id", data) {
            @Override
            protected void populate(Item<TestObj> item) {
            }

            @Override
            public Item<TestObj> buildItem(String id, long index, TestObj object) {
                return item;
            }

            @Override
            public String newChildId() {
                return childId;
            }


        };
        QuickView<TestObj> spy = Mockito.spy(quickView);
        TestObj obj = Mockito.mock(TestObj.class);
        spy.buildItem(234, obj);
        Mockito.verify(spy).buildItem(childId, 234, obj);

    }


    @Test(groups = {"wicketTests"})
    public void addItemsForNextPage_1() {
        final long dataProviderSize = 12;
        final long current = 5, next = 6, pages = 7;
        IDataProvider dataProvider = Mockito.mock(IDataProvider.class);
        Mockito.when(dataProvider.size()).thenReturn(dataProviderSize);
        final AjaxRequestTarget target = Mockito.mock(AjaxRequestTarget.class);
        final List<Item> items = Mockito.mock(List.class);
        QuickView quickview = new QuickView("quick", dataProvider) {
            @Override
            protected void populate(Item item) {
            }

            @Override
            protected long _getPageCount() {
                return pages;
            }

            @Override
            protected long _getCurrentPage() {
                return current;
            }

            @Override
            public AjaxRequestTarget getAjaxRequestTarget() {
                return target;
            }

            @Override
            public List addItemsForPage(long page) {
                return items;
            }
        };

        QuickView spy = Mockito.spy(quickview);
        List<Item> actual = spy.addItemsForNextPage();
        Mockito.verify(spy, Mockito.times(1))._setCurrentPage(next);
        Mockito.verify(spy, Mockito.times(1)).addItemsForPage(next);
        Assert.assertEquals(actual, items);
    }

    /**
     * when current page= pages count
     */
    @Test(groups = {"wicketTests"})
    public void addItemsForNextPage_2() {

        final long dataProviderSize = 12;
        final long current = 5, next = 6, pages = 6;
        IDataProvider dataProvider = Mockito.mock(IDataProvider.class);
        Mockito.when(dataProvider.size()).thenReturn(dataProviderSize);
        final AjaxRequestTarget target = Mockito.mock(AjaxRequestTarget.class);
        final List<Item> items = Mockito.mock(List.class);
        QuickView quickview = new QuickView("quick", dataProvider) {
            @Override
            protected void populate(Item item) {
            }

            @Override
            protected long _getPageCount() {
                return pages;
            }

            @Override
            protected long _getCurrentPage() {
                return current;
            }

            @Override
            public AjaxRequestTarget getAjaxRequestTarget() {
                return target;
            }

            @Override
            public List addItemsForPage(long page) {
                return items;
            }
        };
        QuickView spy = Mockito.spy(quickview);
        List<Item> actual = spy.addItemsForNextPage();
        Mockito.verify(spy, Mockito.never())._setCurrentPage(next);
        Mockito.verify(spy, Mockito.never()).addItemsForPage(next);
        Assert.assertTrue(actual.isEmpty());
    }

    /**
     * when current page> pages count
     */
    @Test(groups = {"wicketTests"})
    public void addItemsForNextPage_3() {
        final long dataProviderSize = 12;
        final long current = 7, next = 6, pages = 6;
        final AjaxRequestTarget target = Mockito.mock(AjaxRequestTarget.class);
        final List<Item> items = Mockito.mock(List.class);
        IDataProvider dataProvider = Mockito.mock(IDataProvider.class);
        Mockito.when(dataProvider.size()).thenReturn(dataProviderSize);
        QuickView quickview = new QuickView("quick", dataProvider) {
            @Override
            protected void populate(Item item) {
            }

            @Override
            protected long _getPageCount() {
                return pages;
            }

            @Override
            protected long _getCurrentPage() {
                return current;
            }

            @Override
            public AjaxRequestTarget getAjaxRequestTarget() {
                return target;
            }

            @Override
            public List addItemsForPage(long page) {
                return items;
            }
        };
        QuickView spy = Mockito.spy(quickview);
        List<Item> actual = spy.addItemsForNextPage();
        Mockito.verify(spy, Mockito.never())._setCurrentPage(next);
        Mockito.verify(spy, Mockito.never()).addItemsForPage(next);
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
        IModel<Integer> model1 = new Model<Integer>(11);
        IModel<Integer> model2 = new Model<Integer>(55);

        final Iterator<IModel<Integer>> newModels = Mockito.mock(Iterator.class);
        Mockito.when(newModels.next()).thenReturn(model1);
        Mockito.when(newModels.next()).thenReturn(model2);
        Mockito.when(newModels.hasNext()).thenReturn(true).thenReturn(true).thenReturn(false);
        Item<Integer> item1 = new Item("1", 1, model1);
        Item<Integer> item2 = new Item("2", 2, model2);
        List<Item<Integer>> list = new ArrayList<Item<Integer>>();
        list.add(item1);
        list.add(item2);
        final Iterator newIterator = list.iterator();
        final IQuickReuseStrategy reuseStrategy = Mockito.mock(IQuickReuseStrategy.class);
        final IItemFactory factory = Mockito.mock(IItemFactory.class);
        Mockito.when(factory.newItem(1, model1)).thenReturn(item1);
        Mockito.when(factory.newItem(2, model2)).thenReturn(item2);
        Mockito.when(reuseStrategy.addItems(4, factory, newModels)).thenReturn(list.iterator());
        QuickView repeater = new QuickView("repeater", dataProvider, new ItemsNavigationStrategy(), itemsPerRequest) {

            public void populate(Item item) {
            }

            @Override
            public MarkupContainer add(Component... c) {
                return this;
            }

            @Override
            protected Iterator buildItems(long index, Iterator iterator) {
                return newIterator;
            }

            @Override
            protected Iterator newModels(long offset, long count) {
                return newModels;
            }

            @Override
            public IItemFactory factory() {
                return factory;
            }
        };
        repeater.setReuseStrategy(reuseStrategy);
        QuickView spy = Mockito.spy(repeater);
        List<Item<TestObj>> items = spy.addItemsForPage(2);
        Mockito.verify(reuseStrategy, Mockito.times(1)).addItems(4, factory, newModels);

        Assert.assertEquals(items.size(), list.size());
        Mockito.verify(spy, Mockito.times(1)).add(items.get(0));
        Mockito.verify(spy, Mockito.times(1)).add(items.get(1));


    }


    /*
     *start index=0
     */
    @Test(groups = {"wicketTests"})
    public void buildItems_1() {
        List<Integer> data = data(10);
        IDataProvider<Integer> dataProvider = new ListDataProvider<Integer>(data);
        QuickView quickView = new QuickView("quickview", dataProvider) {
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
        QuickView<Integer> quickView = new QuickView<Integer>("quickview", dataProvider) {
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

    /*
     *start index=0
     */
    @Test(groups = {"wicketTests"})
    public void buildItemsList_1() {
        List<Integer> data = data(10);
        IDataProvider<Integer> dataProvider = new ListDataProvider<Integer>(data);
        QuickView quickView = new QuickView("quickview", dataProvider) {
            @Override
            protected void populate(Item item) {
            }
        };
        Iterator<? extends Integer> dataIterator = dataProvider.iterator(0, 2);
        List<Item<Integer>> items = quickView.buildItemsList(0, dataIterator);
        Item<Integer> item1 = items.get(0);
        Item<Integer> item2 = items.get(1);
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
    public void buildItemsList_2() {
        List<Integer> data = data(10);
        IDataProvider<Integer> dataProvider = new ListDataProvider<Integer>(data);
        QuickView<Integer> quickView = new QuickView<Integer>("quickview", dataProvider) {
            @Override
            protected void populate(Item item) {
            }
        };
        Iterator<? extends Integer> dataIterator = dataProvider.iterator(5, 2);
        List<Item<Integer>> items = quickView.buildItemsList(10, dataIterator);
        Item<Integer> item1 = items.get(0);
        Item<Integer> item2 = items.get(1);
        Assert.assertEquals(item1.getIndex(), 10);
        Assert.assertEquals(item1.getModelObject().intValue(), 5);
        Assert.assertEquals(item2.getIndex(), 11);
        Assert.assertEquals(item2.getModelObject().intValue(), 6);
        Assert.assertTrue(Long.parseLong(item2.getId()) > Long.parseLong(item1.getId()));
    }

    public AjaxRequestTarget mockTarget() {
        AjaxRequestTarget target = mock(AjaxRequestTarget.class);
        return target;
    }

    public Iterator<TestObj> mockIterator() {
        Iterator<TestObj> it = mock(Iterator.class);
        return it;
    }

    List<Integer> data(int size) {
        List<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < size; i++) {
            list.add(i);
        }
        return list;
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
