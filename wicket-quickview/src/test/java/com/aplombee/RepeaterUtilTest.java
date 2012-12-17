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


import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.mock.MockApplication;
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.util.tester.WicketTester;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Vineet Semwal
 */
public class RepeaterUtilTest {

    @Test(groups = {"utilTests"})
    public void get_1() {
     WicketTester tester=new WicketTester(createMockApplication());
       RepeaterUtil util = RepeaterUtil.get();
        RepeaterUtil util2 = RepeaterUtil.get();
       Assert.assertNotNull(util);
        Assert.assertTrue(util==util2);
    }

    @Test(groups = {"utilTests"})
    public void insertBefore_1() {
        final String child = "child", parent = "parent", tag = "div";
        String actual = RepeaterUtil.get().insertBefore(tag, child, parent);
        String expected = "insertBefore('div','child','parent');";
        Assert.assertEquals(actual.trim(), expected.trim());
    }

    @Test(groups = {"utilTests"})
    public void insertBefore_2() {
        QuickMockApplication app = new QuickMockApplication();
        WicketTester tester = new WicketTester(app);
        final QuickViewParent parent = new QuickViewParent("parent");
        List<Integer> list = new ArrayList<Integer>();
        list.add(100);
        IDataProvider<Integer> dataProvider = new ListDataProvider<Integer>(list);
        final QuickView<Integer> quickView = new QuickView<Integer>("quickview", dataProvider, 2) {
            @Override
            protected void populate(Item<Integer> item) {
            }
        };
        quickView.setReuseStrategy(new ItemsNavigationStrategy());
        parent.setOutputMarkupId(true);
        parent.add(quickView);

        TestQuickViewContainer panel = new TestQuickViewContainer("panel") {

            @Override
            public QuickViewParent newParent() {
                return parent;
            }
        };
        tester.startComponentInPage(panel);
        final Item<Integer> item = (Item) quickView.get(0);
        String expected = String.format("insertBefore('%s','%s','%s');", TestQuickViewContainer.TAG_NAME, item.getMarkupId(), parent.getMarkupId());
        String actual = RepeaterUtil.get().insertBefore(item, parent);
        Assert.assertEquals(actual.trim(), expected.trim());
    }

    /**
     * check with testpanel
     */

    @Test(groups = {"utilTests"})
    public void getComponentTag_1() {

        QuickMockApplication app = new QuickMockApplication();
        WicketTester tester = new WicketTester(app);
        final QuickViewParent parent = new QuickViewParent("parent");
        List<Integer> list = new ArrayList<Integer>();
        list.add(100);
        list.add(500);
        IDataProvider<Integer> dataProvider = new ListDataProvider<Integer>(list);
        final QuickGridView<Integer> quickView = new QuickGridView<Integer>("quickview", dataProvider) {
            @Override
            protected void populate(CellItem<Integer> item) {
            }

            @Override
            protected void populateEmptyItem(CellItem<Integer> item) {
            }
        };
        quickView.setColumns(2);
       quickView.setReuseStrategy(new ItemsNavigationStrategy());
        parent.setOutputMarkupId(true);
        parent.add(quickView);


        TestQuickGridViewContainer panel = new TestQuickGridViewContainer("panel") {

            @Override
            public QuickViewParent newParent() {
                return parent;
            }
        };
        tester.startComponentInPage(panel);
        final Item<Integer> item = (Item) quickView.getRow(0);
        ComponentTag actual=   RepeaterUtil.get().getComponentTag(item);
        Assert.assertEquals(actual.getName(),"tr");
    }

    /**
     * check with testpanel2
     */

    @Test(groups = {"utilTests"})
    public void getComponentTag_2() {

        QuickMockApplication app = new QuickMockApplication();
        WicketTester tester = new WicketTester(app);
        final QuickViewParent parent = new QuickViewParent("parent");
        List<Integer> list = new ArrayList<Integer>();
        list.add(100);
        IDataProvider<Integer> dataProvider = new ListDataProvider<Integer>(list);
        final QuickView<Integer> quickView = new QuickView<Integer>("quickview", dataProvider, 2) {
            @Override
            protected void populate(Item<Integer> item) {
            }
        };
        quickView.setReuseStrategy(new ItemsNavigationStrategy());
        parent.setOutputMarkupId(true);
        parent.add(quickView);


        TestQuickViewContainer panel = new TestQuickViewContainer("panel") {
            @Override
            public QuickViewParent newParent() {
                return parent;
            }
        };
        tester.startComponentInPage(panel);
        final Item<Integer> item = (Item) quickView.get(0);
      ComponentTag actual=   RepeaterUtil.get().getComponentTag(item);
        Assert.assertEquals(actual.getName(),TestQuickViewContainer.TAG_NAME);
    }

    @Test(groups = {"utilTests"})
    public void insertAfter_1() {
        final String child = "child", parent = "parent", tag = "div";
        String call = RepeaterUtil.get().insertAfter(tag, child, parent);
        String expected = "insertAfter('div','child','parent');";
        Assert.assertEquals(call, expected);
    }


    @Test(groups = {"utilTests"})
    public void insertAfter_2() {
        QuickMockApplication app = new QuickMockApplication();
        WicketTester tester = new WicketTester(app);
        final QuickViewParent parent = new QuickViewParent("parent");
        List<Integer> list = new ArrayList<Integer>();
        list.add(100);
        IDataProvider<Integer> dataProvider = new ListDataProvider<Integer>(list);
        final QuickView<Integer> quickView = new QuickView<Integer>("quickview", dataProvider, 2) {
            @Override
            protected void populate(Item<Integer> item) {
            }
        };
        quickView.setReuseStrategy(new ItemsNavigationStrategy());
        parent.setOutputMarkupId(true);
        parent.add(quickView);

        TestQuickViewContainer panel = new TestQuickViewContainer("panel") {
            @Override
            public QuickViewParent newParent() {
                return parent;
            }
        };
        tester.startComponentInPage(panel);
        final Item<Integer> item = (Item) quickView.get(0);
        String expected = String.format("insertAfter('%s','%s','%s');", TestQuickViewContainer.TAG_NAME, item.getMarkupId(), parent.getMarkupId());
        String actual = RepeaterUtil.get().insertAfter(item, parent);
        Assert.assertEquals(actual.trim(), expected.trim());
    }

    @Test(groups = {"utilTests"})
    public void removeItem_1() {
        final String repeaterMarkupId = "quick";
        final String expected = "removeItem('quick');";
        final String actual = RepeaterUtil.get().removeItem(repeaterMarkupId);
        Assert.assertEquals(actual.trim(), expected.trim());
    }

    @Test(groups = {"utilTests"})
    public void removeItem() {
        final String repeaterMarkupId = "quick";
        Item item = Mockito.mock(Item.class);
        Mockito.when(item.getMarkupId()).thenReturn(repeaterMarkupId);
        final String actual = RepeaterUtil.get().removeItem(item);
        final String expected = "removeItem('quick');";
        Assert.assertEquals(actual.trim(), expected.trim());
    }


    @Test(groups = {"utilTests"})
    public void scrollToTop() {
        final String repeaterMarkupId = "quick";
        final String expected = "scrollToTop('quick');";
        final String actual = RepeaterUtil.get().scrollToTop(repeaterMarkupId);
        Assert.assertEquals(actual.trim(), expected.trim());
    }

    @Test(groups = {"utilTests"})
    public void scrollToBottom() {
        final String repeaterMarkupId = "quick";
        final String expected = "scrollToBottom('quick');";
        final String actual = RepeaterUtil.get().scrollToBottom(repeaterMarkupId);
        Assert.assertEquals(actual.trim(), expected.trim());
    }

    @Test(groups = {"utilTests"})
    public void scrollTo_1() {
        final int height = 7;
        final String repeaterMarkupId = "quick";
        final String expected = "scrollTo('quick',7);";
        final String actual = RepeaterUtil.get().scrollTo(repeaterMarkupId, height);
        Assert.assertEquals(actual.trim(), expected.trim());
    }

    @Test(groups = {"utilTests"})
    public void scrollTo_2() {
        final int height = 9;
        final String repeaterMarkupId = "quick";
        final String expected = "scrollTo('quick',9);";
        final String actual = RepeaterUtil.get().scrollTo(repeaterMarkupId, height);
        Assert.assertEquals(actual.trim(), expected.trim());
    }

    /**
     * integer overflow
     */
    @Test(groups = {"utilTests"}, expectedExceptions = IllegalArgumentException.class)
    public void safeLongToInt_1() {
        long val = (long) Integer.MAX_VALUE + 1l;
        RepeaterUtil.get().safeLongToInt(val);
    }

    /**
     * integer overflow
     */
    @Test(groups = {"utilTests"}, expectedExceptions = IllegalArgumentException.class)
    public void safeLongToInt_2() {
        long val = (long) Integer.MIN_VALUE - (long) 1;
        RepeaterUtil.get().safeLongToInt(val);
    }

    /**
     * integer doesn't overflow
     */
    @Test(groups = {"utilTests"})
    public void safeLongToInt_3() {
        int min = RepeaterUtil.get().safeLongToInt(Integer.MIN_VALUE);
        Assert.assertEquals(min, Integer.MIN_VALUE);
        int max = RepeaterUtil.get().safeLongToInt(Integer.MAX_VALUE);
        Assert.assertEquals(max, Integer.MAX_VALUE);
    }

    private static WebApplication createMockApplication() {
        WebApplication app = new QuickMockApplication();
        return app;
    }

    @Test(groups = {"utilTests"}, expectedExceptions = RepeaterUtil.ReuseStrategyNotSupportedException.class)
    public void reuseStategyNotSupportedForItemsNavigation_1() {
        IQuickView quickView = Mockito.mock(IQuickView.class);
        IQuickReuseStrategy strategy=Mockito.mock(IQuickReuseStrategy.class);
        Mockito.when(strategy.isPaging()).thenReturn(true);
        Mockito.when(quickView.getReuseStrategy()).thenReturn(strategy);
        RepeaterUtil.get().reuseStategyNotSupportedForItemsNavigation(quickView);
    }


    @Test(groups = {"utilTests"})
    public void reuseStategyNotSupportedForItemsNavigation_5() {
        IQuickView quickView = Mockito.mock(IQuickView.class);
        IQuickReuseStrategy strategy=Mockito.mock(IQuickReuseStrategy.class);
        Mockito.when(strategy.isPaging()).thenReturn(false);
       Mockito.when(quickView.getReuseStrategy()).thenReturn(strategy);

        RepeaterUtil.get().reuseStategyNotSupportedForItemsNavigation(quickView);
    }


    @Test(groups = {"utilTests"}, expectedExceptions = RepeaterUtil.OutputMarkupIdNotTrueException.class)
    public void outPutMarkupIdNotTrue_1() {
        IDataProvider data = Mockito.mock(IDataProvider.class);
        IQuickReuseStrategy strategy=Mockito.mock(IQuickReuseStrategy.class);
        Mockito.when(strategy.isPaging()).thenReturn(false);
        QuickView quickView = new QuickView("id", data, strategy) {
            @Override
            protected void populate(Item item) {
            }
        };
        WebMarkupContainer parent = new WebMarkupContainer("parent");
        parent.add(quickView);
        RepeaterUtil.get().outPutMarkupIdNotTrue(quickView);
    }

    @Test(groups = {"utilTests"})
    public void outPutMarkupIdNotTrue_2() {
        IDataProvider data = Mockito.mock(IDataProvider.class);
        IQuickReuseStrategy strategy=Mockito.mock(IQuickReuseStrategy.class);
        Mockito.when(strategy.isPaging()).thenReturn(false);
        QuickView quickView = new QuickView("id", data,strategy) {
            @Override
            protected void populate(Item item) {
            }
        };
        WebMarkupContainer parent = new WebMarkupContainer("parent");
        parent.add(quickView);
        parent.setOutputMarkupId(true);
        RepeaterUtil.get().outPutMarkupIdNotTrue(quickView);
    }

   @Test(groups = {"utilTests"})
    public void outPutMarkupIdNotTrue_3() {
        IDataProvider data = Mockito.mock(IDataProvider.class);
       IQuickReuseStrategy strategy=Mockito.mock(IQuickReuseStrategy.class);
       Mockito.when(strategy.isPaging()).thenReturn(false);
        QuickView quickView = new QuickView("id", data,strategy) {
            @Override
            protected void populate(Item item) {
            }
        };

        WebMarkupContainer parent = new WebMarkupContainer("parent");
        parent.add(quickView);
        parent.setOutputMarkupPlaceholderTag(true);
        RepeaterUtil.get().outPutMarkupIdNotTrue(quickView);
    }

    /**
     * parent=null,reuse= paging
     */
    @Test(groups = {"utilTests"})
    public void parentNotSuitable_1() {
        IQuickView quickView = Mockito.mock(IQuickView.class);
        IQuickReuseStrategy strategy=Mockito.mock(IQuickReuseStrategy.class);
        Mockito.when(strategy.isPaging()).thenReturn(true);
        Mockito.when(quickView.getReuseStrategy()).thenReturn(strategy);
        RepeaterUtil.get().parentNotSuitable(quickView);
    }


    /**
     * parent=null,reuse= not paging
     */
    @Test(groups = {"utilTests"}, expectedExceptions = RepeaterUtil.QuickViewNotAddedToParentException.class)
    public void parentNotSuitable_2() {
        IQuickView quickView = Mockito.mock(IQuickView.class);
        IQuickReuseStrategy strategy=Mockito.mock(IQuickReuseStrategy.class);
        Mockito.when(strategy.isPaging()).thenReturn(false);
        Mockito.when(quickView.getReuseStrategy()).thenReturn(strategy);
        RepeaterUtil.get().parentNotSuitable(quickView);
    }

    /**
     * parent=page,reuse= not paging
     */
  @Test(groups = {"utilTests"}, expectedExceptions = RepeaterUtil.QuickViewNotAddedToParentException.class)
    public void parentNotSuitable_3() {
      IQuickReuseStrategy strategy=Mockito.mock(IQuickReuseStrategy.class);
      Mockito.when(strategy.isPaging()).thenReturn(false);
        WebPage parent = Mockito.mock(WebPage.class);
        IQuickView quickView = Mockito.mock(IQuickView.class);
        Mockito.when(quickView.getParent()).thenReturn(parent);
        Mockito.when(quickView.getReuseStrategy()).thenReturn(strategy);
        RepeaterUtil.get().parentNotSuitable(quickView);
    }

    /**
     * parent children size=2,reuse= not paging
     */
    @Test(groups = {"utilTests"}, expectedExceptions = RepeaterUtil.ParentNotUnaryException.class)
    public void parentNotSuitable_5() {
        IQuickReuseStrategy strategy=Mockito.mock(IQuickReuseStrategy.class);
        Mockito.when(strategy.isPaging()).thenReturn(false);
        WebMarkupContainer parent = Mockito.mock(WebMarkupContainer.class);
        Mockito.when(parent.size()).thenReturn(2);
        IQuickView quickView = Mockito.mock(IQuickView.class);
        Mockito.when(quickView.getParent()).thenReturn(parent);
        Mockito.when(quickView.getReuseStrategy()).thenReturn(strategy);
        RepeaterUtil.get().parentNotSuitable(quickView);
    }

    /**
     * parent children size=1,reuse= not paging
     */
   @Test(groups = {"utilTests"})
    public void parentNotSuitable_6() {
       IQuickReuseStrategy strategy=Mockito.mock(IQuickReuseStrategy.class);
       Mockito.when(strategy.isPaging()).thenReturn(false);
        WebMarkupContainer parent = Mockito.mock(WebMarkupContainer.class);
        Mockito.when(parent.size()).thenReturn(1);
        IQuickView quickView = Mockito.mock(IQuickView.class);
        Mockito.when(quickView.getParent()).thenReturn(parent);
        Mockito.when(quickView.getReuseStrategy()).thenReturn(strategy);
        RepeaterUtil.get().parentNotSuitable(quickView);
    }




    @Test(groups = {"utilTests"})
    public void isComponentScrollBarAtBottom() {
        WebMarkupContainer c = new WebMarkupContainer("parent");
        c.setMarkupId("parent");
        String actual = RepeaterUtil.get().isComponentScrollBarAtBottom(c);
        String expected = "isComponentScrollBarAtBottom('parent');";
        Assert.assertEquals(actual, expected);
    }

    @Test(groups = {"utilTests"})
    public void isPageScrollBarAtBottom() {
        String actual = RepeaterUtil.get().isPageScrollBarAtBottom();
        String expected = "isPageScrollBarAtBottom();";
        Assert.assertEquals(actual, expected);
    }

    /**
     * when all items same
     */
    @Test(groups = {"utilTests"})
    public void reuseIfModelsEqual_1() {
        List<Item> list1 = new ArrayList<Item>();
        Item<Integer> item1 = new Item<Integer>("1", 1, new Model<Integer>(1));
        Item<Integer> item2 = new Item<Integer>("2", 2, new Model<Integer>(2));
        list1.add(item1);
        list1.add(item2);
        Iterator<Item> iterator1 = list1.iterator();
        List<Item> list2 = new ArrayList<Item>();
        list2.add(item1);
        list2.add(item2);
        Iterator<Item> iterator2 = list2.iterator();
        Iterator<Item> reused = RepeaterUtil.get().reuseItemsIfModelsEqual(iterator1, iterator2);
        Item<Integer> expected1 = reused.next();
        Item<Integer> expected2 = reused.next();
        Assert.assertEquals(item1.getModelObject().intValue(), expected1.getModelObject().intValue());
        Assert.assertEquals(item1.getIndex(), expected1.getIndex());
        Assert.assertEquals(item2.getModelObject().intValue(), expected2.getModelObject().intValue());
        Assert.assertEquals(item2.getIndex(), expected2.getIndex());
        Assert.assertFalse(reused.hasNext());
    }


    /**
     * when items different
     */
    @Test(groups = {"utilTests"})
    public void reuseIfModelsEqual_2() {
        List<Item> list1 = new ArrayList<Item>();
        Item<Integer> item1 = new Item<Integer>("1", 1, new Model<Integer>(1));
        Item<Integer> item2 = new Item<Integer>("2", 2, new Model<Integer>(2));
        Item<Integer> item3 = new Item<Integer>("3", 3, new Model<Integer>(3));
        list1.add(item1);
        list1.add(item2);
        Iterator<Item> oldIt = list1.iterator();
        List<Item> list2 = new ArrayList<Item>();
        list2.add(item1);
        list2.add(item3);
        Iterator<Item> newIt = list2.iterator();
        Iterator<Item> reused =RepeaterUtil.get().reuseItemsIfModelsEqual(oldIt, newIt);
        Item<Integer> expected1 = reused.next();
        Item<Integer> expected2 = reused.next();
        Assert.assertEquals(item1.getModelObject().intValue(), expected1.getModelObject().intValue());
        Assert.assertEquals(item1.getIndex(), expected1.getIndex());
        Assert.assertEquals(item3.getModelObject().intValue(), expected2.getModelObject().intValue());
        Assert.assertEquals(item3.getIndex(), expected2.getIndex());
        Assert.assertFalse(reused.hasNext());

    }


    /**
     * when newiterator has less items
     */
    @Test(groups = {"utilTests"})
    public void reuseIfModelsEqual_3() {
        List<Item> list1 = new ArrayList<Item>();
        Item<Integer> item1 = new Item<Integer>("1", 1, new Model<Integer>(1));
        Item<Integer> item2 = new Item<Integer>("2", 2, new Model<Integer>(2));
        list1.add(item1);
        list1.add(item2);
        Iterator<Item> oldIt = list1.iterator();
        List<Item> list2 = new ArrayList<Item>();
        list2.add(item1);

        Iterator<Item> newIt = list2.iterator();

        Iterator<Item> reused = RepeaterUtil.get().reuseItemsIfModelsEqual(oldIt, newIt);
        Item<Integer> expected1 = reused.next();
        Assert.assertEquals(item1.getModelObject().intValue(), expected1.getModelObject().intValue());
        Assert.assertEquals(item1.getIndex(), expected1.getIndex());
        Assert.assertFalse(reused.hasNext());

    }

    /**
     * when newiterator has more items
     */
    @Test(groups = {"utilTests"})
    public void reuseIfModelsEqual_4() {
        List<Item> list1 = new ArrayList<Item>();
        Item<Integer> item1 = new Item<Integer>("1", 1, new Model<Integer>(1));
        Item<Integer> item2 = new Item<Integer>("2", 2, new Model<Integer>(2));
        list1.add(item1);
        Iterator<Item> oldIt = list1.iterator();
        List<Item> list2 = new ArrayList<Item>();
        list2.add(item1);
        list2.add(item2);
        Iterator<Item> newIt = list2.iterator();

        Iterator<Item> reused = RepeaterUtil.get().reuseItemsIfModelsEqual(oldIt, newIt);
        Item<Integer> expected1 = reused.next();
        Item<Integer> expected2 = reused.next();
        Assert.assertEquals(item1.getModelObject().intValue(), expected1.getModelObject().intValue());
        Assert.assertEquals(item1.getIndex(), expected1.getIndex());
        Assert.assertEquals(item2.getModelObject().intValue(), expected2.getModelObject().intValue());
        Assert.assertEquals(item2.getIndex(), expected2.getIndex());
        Assert.assertFalse(reused.hasNext());
    }



}
