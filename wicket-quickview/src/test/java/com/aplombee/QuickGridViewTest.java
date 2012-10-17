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

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.markup.repeater.data.GridView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.mock.MockApplication;
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.util.tester.WicketTester;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * @author Vineet Semwal
 */
public class QuickGridViewTest {

    @BeforeTest(groups = {"wicketTests"})
    void setup() {
      WicketTester  tester = new WicketTester(createMockApplication());
    }

    private static WebApplication createMockApplication() {
        WebApplication app = new MockApplication();
        return app;
    }
    @Test(groups = {"wicketTests"})
    public void constructor_1() {
        IDataProvider provider = Mockito.mock(IDataProvider.class);
        QuickGridView grid = new QuickGridView("grid", provider) {
            @Override
            protected void populate(CellItem item) {
            }

            @Override
            protected void populateEmptyItem(CellItem item) {
            }
        };
       int rows= grid.getRows();
        int columns=grid.getColumns();
        ReUse reuse=grid.getReuse();
        Assert.assertEquals(columns,1);
        Assert.assertEquals(grid.getRows(),Integer.MAX_VALUE);
        Assert.assertEquals(grid.getReuse(),ReUse.NOT_INITIALIZED);
    }

    @Test(groups = {"wicketTests"})
    public void constructor_2() {
        IDataProvider provider = Mockito.mock(IDataProvider.class);
        QuickGridView grid = new QuickGridView("grid", provider,ReUse.ITEMSNAVIGATION) {
            @Override
            protected void populate(CellItem item) {
            }

            @Override
            protected void populateEmptyItem(CellItem item) {
            }
        };
        int rows= grid.getRows();
        int columns=grid.getColumns();
        ReUse reuse=grid.getReuse();
        Assert.assertEquals(columns,1);
        Assert.assertEquals(grid.getRows(),Integer.MAX_VALUE);
        Assert.assertEquals(grid.getReuse(),ReUse.ITEMSNAVIGATION);
    }

    @Test(groups = {"wicketTests"})
    public void setRows_1(){
        IDataProvider provider = Mockito.mock(IDataProvider.class);
        QuickGridView grid = new QuickGridView("grid", provider,ReUse.ITEMSNAVIGATION) {
            @Override
            protected void populate(CellItem item) {
            }

            @Override
            protected void populateEmptyItem(CellItem item) {
            }
        };

        final int rows=456;
        grid.setRows(rows);
        Assert.assertEquals(grid.getRows(),rows);
        Assert.assertEquals(grid.getItemsPerRequest(),456);
    }

    @Test(groups = {"wicketTests"},expectedExceptions = IllegalArgumentException.class)
    public void setRows_2(){
        IDataProvider provider = Mockito.mock(IDataProvider.class);
        QuickGridView grid = new QuickGridView("grid", provider,ReUse.ITEMSNAVIGATION) {
            @Override
            protected void populate(CellItem item) {
            }

            @Override
            protected void populateEmptyItem(CellItem item) {
            }
        };

        final int rows=-1;
        grid.setRows(rows);

    }

    @Test(groups = {"wicketTests"},expectedExceptions = IllegalArgumentException.class)
    public void setRows_3(){
        IDataProvider provider = Mockito.mock(IDataProvider.class);
        QuickGridView grid = new QuickGridView("grid", provider,ReUse.ITEMSNAVIGATION) {
            @Override
            protected void populate(CellItem item) {
            }

            @Override
            protected void populateEmptyItem(CellItem item) {
            }
        };

        final int rows=0;
        grid.setRows(rows);

    }

    @Test(groups = {"wicketTests"})
    public void setColumns_1(){
        IDataProvider provider = Mockito.mock(IDataProvider.class);
        QuickGridView grid = new QuickGridView("grid", provider,ReUse.ITEMSNAVIGATION) {
            @Override
            protected void populate(CellItem item) {
            }

            @Override
            protected void populateEmptyItem(CellItem item) {
            }
        };

        final int cols=10;
        grid.setColumns(cols);
        Assert.assertEquals(grid.getColumns(),cols);
        final long expected=(long)Integer.MAX_VALUE*(long)cols;
        Assert.assertEquals(grid.getItemsPerRequest(),expected);
    }

    @Test(groups = {"wicketTests"},expectedExceptions = IllegalArgumentException.class)
    public void setColumns_2(){
        IDataProvider provider = Mockito.mock(IDataProvider.class);
        QuickGridView grid = new QuickGridView("grid", provider,ReUse.ITEMSNAVIGATION) {
            @Override
            protected void populate(CellItem item) {
            }

            @Override
            protected void populateEmptyItem(CellItem item) {
            }
        };

        final int cols=0;
        grid.setColumns(cols);

    }

    @Test(groups = {"wicketTests"})
    public void newRowItem_1(){
        IDataProvider provider = Mockito.mock(IDataProvider.class);
        QuickGridView grid = new QuickGridView("grid", provider,ReUse.ITEMSNAVIGATION) {
            @Override
            protected void populate(CellItem item) {
            }

            @Override
            protected void populateEmptyItem(CellItem item) {
            }
        };
        final String id="id";
        final long index=845;
       QuickGridView.RowItem row= grid.newRowItem(id, 845);
        Assert.assertEquals(row.getMarkupId(),id);
        Assert.assertEquals(row.getIndex(),index);
        Assert.assertEquals(row.getId(),id);
    }

    @Test(groups = {"wicketTests"})
    public void buildRowItem(){
        IDataProvider provider = Mockito.mock(IDataProvider.class);
        QuickGridView grid = new QuickGridView("grid", provider,ReUse.ITEMSNAVIGATION) {
            @Override
            protected void populate(CellItem item) {
            }

            @Override
            protected void populateEmptyItem(CellItem item) {
            }
        };
        final String id="id";
        final long index=845;
        QuickGridView.RowItem row=grid.buildRowItem(id, index);
        RepeatingView repeater=(RepeatingView)row.get(QuickGridView.COLUMNS_REPEATER_ID);
        Assert.assertNotNull(repeater);
    }
    @Test(groups = {"wicketTests"})
    public void rowItem_getRepeater(){
        IDataProvider provider = Mockito.mock(IDataProvider.class);
        QuickGridView grid = new QuickGridView("grid", provider,ReUse.ITEMSNAVIGATION) {
            @Override
            protected void populate(CellItem item) {
            }

            @Override
            protected void populateEmptyItem(CellItem item) {
            }
        };
        final String id="id";
        final long index=845;
        QuickGridView.RowItem row=grid.buildRowItem(id, index);
        RepeatingView repeater=row.getRepeater();
        Assert.assertNotNull(repeater);
       }

    @Test(groups = {"wicketTests"})
    public void newCellItem_1(){
        final  int object=89;
        IDataProvider data = Mockito.mock(IDataProvider.class);
        final Model<Integer>model=new Model<Integer>(object);
       Mockito.when(data.model(object)).thenReturn(model);
        QuickGridView<Integer> grid = new QuickGridView<Integer>("grid", data,ReUse.ITEMSNAVIGATION) {
            @Override
            protected void populate(CellItem item) {
            }

            @Override
            protected void populateEmptyItem(CellItem item) {
            }
        };

        final String id="id";
        final long index=845;
        WicketTester tester=new WicketTester(createMockApplication());
        QuickGridView.CellItem cell=grid.newCellItem(id, index, object);
        tester.startComponentInPage(cell);
        Assert.assertEquals(cell.getMarkupId(),id);
        Assert.assertEquals(cell.getId(),id);
        Assert.assertEquals(cell.getIndex(),index);
        Assert.assertEquals(cell.getModelObject(),object);
        Assert.assertFalse(cell.isEmpty());
    }

    @Test(groups = {"wicketTests"})
    public void newEmptyCellItem_1(){
        final  int object=89;
        IDataProvider data = Mockito.mock(IDataProvider.class);
        final Model<Integer>model=new Model<Integer>(object);
        Mockito.when(data.model(object)).thenReturn(model);
        QuickGridView<Integer> grid = new QuickGridView<Integer>("grid", data,ReUse.ITEMSNAVIGATION) {
            @Override
            protected void populate(CellItem item) {
            }

            @Override
            protected void populateEmptyItem(CellItem item) {
            }
        };

        final String id="id";
        final long index=845;
        QuickGridView.CellItem cell=grid.newEmptyCellItem(id, index);
        Assert.assertEquals(cell.getMarkupId(),id);
        Assert.assertEquals(cell.getId(),id);
        Assert.assertEquals(cell.getIndex(),index);
        Assert.assertNull(cell.getModelObject());
        Assert.assertTrue(cell.isEmpty());
    }

    /**
     * buildEmptyCellItem(id, index)
     */

    @Test(groups = {"wicketTests"})
    public void buildEmptyCellItem_1(){
        final long childId=845;
       final QuickGridView.CellItem cell= Mockito.mock(QuickGridView.CellItem.class);
        IDataProvider data = Mockito.mock(IDataProvider.class);
        QuickGridView<Integer> grid = new QuickGridView<Integer>("grid", data,ReUse.ITEMSNAVIGATION) {
            @Override
            protected void populate(CellItem item) {
            }

            @Override
            protected void populateEmptyItem(CellItem item) {
            }

            @Override
            public CellItem<Integer> newEmptyCellItem(String id, long index) {
                return cell;
            }

            @Override
            public String newChildId() {
                return String.valueOf(childId);
            }

            @Override
            public long getChildId() {
                return childId;
            }
        };
         QuickGridView spy=Mockito.spy(grid);

        QuickGridView.CellItem actual=spy.buildEmptyCellItem(String.valueOf(childId), childId);
        Mockito.verify(spy,Mockito.times(1)).newEmptyCellItem(String.valueOf(childId),childId);
        Mockito.verify(spy,Mockito.times(1)).populateEmptyItem(cell);
    }


    /**
     * buildEmptyCellItem()
     */

    @Test(groups = {"wicketTests"})
    public void buildEmptyCellItem_2(){
        final long childId=845;
        final QuickGridView.CellItem cell= Mockito.mock(QuickGridView.CellItem.class);
        IDataProvider data = Mockito.mock(IDataProvider.class);
        QuickGridView<Integer> grid = new QuickGridView<Integer>("grid", data,ReUse.ITEMSNAVIGATION) {
            @Override
            protected void populate(CellItem item) {
            }

            @Override
            protected void populateEmptyItem(CellItem item) {
            }

            @Override
            public CellItem<Integer> newEmptyCellItem(String id, long index) {
                return cell;
            }
            @Override
            public CellItem<Integer> newCellItem(String id, long index,Integer object) {
                return cell;
            }

            @Override
            public String newChildId() {
                return String.valueOf(childId);
            }

            @Override
            public long getChildId() {
                return childId;
            }
        };
        QuickGridView spy=Mockito.spy(grid);
        QuickGridView.CellItem actual=spy.buildEmptyCellItem();
        Mockito.verify(spy,Mockito.times(1)).newEmptyCellItem(String.valueOf(childId),childId);
        Mockito.verify(spy,Mockito.times(1)).populateEmptyItem(cell);
    }

    /**
     * buildCellItem(id, index,object)
     */
    @Test(groups = {"wicketTests"})
    public void buildCellItem_1(){
        final long childId=845;
        final QuickGridView.CellItem cell= Mockito.mock(QuickGridView.CellItem.class);
        IDataProvider data = Mockito.mock(IDataProvider.class);
        QuickGridView<Integer> grid = new QuickGridView<Integer>("grid", data,ReUse.ITEMSNAVIGATION) {
            @Override
            protected void populate(CellItem item) {
            }

            @Override
            protected void populateEmptyItem(CellItem item) {
            }

            @Override
            public CellItem<Integer> newCellItem(String id, long index,Integer object) {
                return cell;
            }

            @Override
            public String newChildId() {
                return String.valueOf(childId);
            }

            @Override
            public long getChildId() {
                return childId;
            }
        };
        QuickGridView spy=Mockito.spy(grid);

        final int object=876;//any object/number
        QuickGridView.CellItem actual=spy.buildCellItem(String.valueOf(childId), childId,object);
        Mockito.verify(spy,Mockito.times(1)).newCellItem(String.valueOf(childId), childId,object);
        Mockito.verify(spy,Mockito.times(1)).populate(cell);
    }


    /**
     * buildCellItem(object)
     */
    @Test(groups = {"wicketTests"})
    public void buildCellItem_2(){
        final QuickGridView.CellItem cell= Mockito.mock(QuickGridView.CellItem.class);
        IDataProvider data = Mockito.mock(IDataProvider.class);
        final long childId=89l;

        QuickGridView<Integer> grid = new QuickGridView<Integer>("grid", data,ReUse.ITEMSNAVIGATION) {
            @Override
            protected void populate(CellItem item) {
            }

            @Override
            protected void populateEmptyItem(CellItem item) {
            }

            @Override
            public CellItem<Integer> newCellItem(String id, long index,Integer object) {
                return cell;
            }

            @Override
            public String newChildId() {
                return String.valueOf(childId);
            }

            @Override
            public long getChildId() {
                return childId;
            }
        };
        QuickGridView spy=Mockito.spy(grid);
         final int object=908;
        QuickGridView.CellItem actual=spy.buildCellItem(object);
        Mockito.verify(spy,Mockito.times(1)).newCellItem(String.valueOf(childId),childId,object);
        Mockito.verify(spy,Mockito.times(1)).populate(cell);
    }


    /*
     *start index=0
    */
    @Test(groups = {"wicketTests"})
    public void buildCells_1(){
        final long itemsPerRequest=2;
             List<Integer> data = data(10);
            IDataProvider<Integer> dataProvider = new ListDataProvider<Integer>(data);
            QuickGridView<Integer> gridView = new QuickGridView<Integer>("quickview", dataProvider, ReUse.ITEMSNAVIGATION) {
                @Override
                protected void populate(CellItem<Integer> item) {
                }

                @Override
                protected void populateEmptyItem(CellItem<Integer> item) {
                }
            };
            gridView.setItemsPerRequest(itemsPerRequest);
            Iterator<? extends Integer> dataIterator = dataProvider.iterator(0, itemsPerRequest);
            Iterator<QuickGridView.CellItem<Integer>> itemsIterator = gridView.buildCells(0, dataIterator);
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
    public void buildCells_2() {
        final long itemsPerRequest=2;
        List<Integer> data = data(20);
        IDataProvider<Integer> dataProvider = new ListDataProvider<Integer>(data);
        QuickGridView<Integer> gridView = new QuickGridView<Integer>("quickview", dataProvider, ReUse.ITEMSNAVIGATION) {
            @Override
            protected void populate(CellItem<Integer> item) {
            }

            @Override
            protected void populateEmptyItem(CellItem<Integer> item) {
            }
        };
        Iterator<? extends Integer> dataIterator = dataProvider.iterator(6, itemsPerRequest);
        Iterator<QuickGridView.CellItem<Integer>> itemsIterator = gridView.buildCells(10, dataIterator);
        Item<Integer> item1 = itemsIterator.next();
        Item<Integer> item2 = itemsIterator.next();
        Assert.assertEquals(item1.getIndex(), 10);
        Assert.assertEquals(item1.getModelObject().intValue(), 6);
        Assert.assertEquals(item2.getIndex(), 11);
        Assert.assertEquals(item2.getModelObject().intValue(), 7);
        Assert.assertTrue(Long.parseLong(item2.getId()) > Long.parseLong(item1.getId()));
    }

    /**
     * rowIndex=0   buildRows(final long rowIndex,  Iterator<CellItem<T>> iterator)
     */
    @Test(groups = {"wicketTests"})
    public void buildRows_1(){
        final long itemsPerRequest=2;
        List<Integer> data = data(20);
        IDataProvider<Integer> dataProvider = new ListDataProvider<Integer>(data);
        QuickGridView<Integer> gridView = new QuickGridView<Integer>("quickview", dataProvider, ReUse.ITEMSNAVIGATION) {
            @Override
            protected void populate(CellItem<Integer> item) {
            }

            @Override
            protected void populateEmptyItem(CellItem<Integer> item) {
            }
        };
        gridView.setColumns(2);
        gridView.setItemsPerRequest(itemsPerRequest);
        QuickGridView.CellItem<Integer>cell1=new QuickGridView.CellItem<Integer>("1",1,new Model<Integer>(1));
        QuickGridView.CellItem<Integer>cell2=new QuickGridView.CellItem<Integer>("2",2,new Model<Integer>(2));
        QuickGridView.CellItem<Integer>cell3=new QuickGridView.CellItem<Integer>("3",3,new Model<Integer>(3));
        QuickGridView.CellItem<Integer>cell4=new QuickGridView.CellItem<Integer>("4",4,new Model<Integer>(4));

         List<QuickGridView.CellItem<Integer>>cells=new ArrayList<QuickGridView.CellItem<Integer>>();
        cells.add(cell1);
        cells.add(cell2);
        cells.add(cell3);
        cells.add(cell4);
        Iterator<QuickGridView.CellItem<Integer>>cellsIterator=cells.iterator();
        Iterator<QuickGridView.RowItem<Integer>> rowsIterator = gridView.buildRows(0, cellsIterator);
        QuickGridView.RowItem<Integer>rowItem1=rowsIterator.next();
        QuickGridView.RowItem<Integer>rowItem2=rowsIterator.next();
        Assert.assertFalse(rowsIterator.hasNext());
         Assert.assertEquals(rowItem1.getRepeater().size(),2);
        Assert.assertEquals(rowItem1.getCellItem(0).getIndex(),cell1.getIndex());
        Assert.assertEquals(rowItem1.getCellItem(1).getModelObject(),cell2.getModelObject());

        Assert.assertEquals(rowItem2.getCellItem(0).getIndex(),cell3.getIndex());
        Assert.assertEquals(rowItem2.getCellItem(0).getModelObject(),cell3.getModelObject());
        Assert.assertEquals(rowItem2.getCellItem(1).getIndex(),cell4.getIndex());
        Assert.assertEquals(rowItem2.getCellItem(1).getModelObject(),cell4.getModelObject());
        Assert.assertEquals(rowItem2.getRepeater().size(),2);

        Assert.assertEquals(rowItem1.getIndex(),0);
        Assert.assertEquals(rowItem2.getIndex(),1);

    }
    /**
     * rowIndex=10     buildRows(final long rowIndex,  Iterator<CellItem<T>> iterator)
     */
    @Test(groups = {"wicketTests"})
    public void buildRows_2(){
        final long itemsPerRequest=2;
        List<Integer> data = data(20);
        IDataProvider<Integer> dataProvider = new ListDataProvider<Integer>(data);
        QuickGridView<Integer> gridView = new QuickGridView<Integer>("quickview", dataProvider, ReUse.ITEMSNAVIGATION) {
            @Override
            protected void populate(CellItem<Integer> item) {
            }

            @Override
            protected void populateEmptyItem(CellItem<Integer> item) {
            }
        };
        gridView.setColumns(2);
        gridView.setItemsPerRequest(itemsPerRequest);
        QuickGridView.CellItem<Integer>cell1=new QuickGridView.CellItem<Integer>("1",1,new Model<Integer>(1));
        QuickGridView.CellItem<Integer>cell2=new QuickGridView.CellItem<Integer>("2",2,new Model<Integer>(2));
        QuickGridView.CellItem<Integer>cell3=new QuickGridView.CellItem<Integer>("3",3,new Model<Integer>(3));
        QuickGridView.CellItem<Integer>cell4=new QuickGridView.CellItem<Integer>("4",4,new Model<Integer>(4));

        List<QuickGridView.CellItem<Integer>>cells=new ArrayList<QuickGridView.CellItem<Integer>>();
        cells.add(cell1);
        cells.add(cell2);
        cells.add(cell3);
        cells.add(cell4);
        Iterator<QuickGridView.CellItem<Integer>>cellsIterator=cells.iterator();
        Iterator<QuickGridView.RowItem<Integer>> rowsIterator = gridView.buildRows(10, cellsIterator);
        QuickGridView.RowItem<Integer>rowItem1=rowsIterator.next();
        QuickGridView.RowItem<Integer>rowItem2=rowsIterator.next();
        Assert.assertFalse(rowsIterator.hasNext());
        Assert.assertEquals(rowItem1.getRepeater().size(),2);
        Assert.assertEquals(rowItem1.getCellItem(0).getIndex(),cell1.getIndex());
        Assert.assertEquals(rowItem1.getCellItem(1).getModelObject(),cell2.getModelObject());

        Assert.assertEquals(rowItem2.getCellItem(0).getIndex(),cell3.getIndex());
        Assert.assertEquals(rowItem2.getCellItem(0).getModelObject(),cell3.getModelObject());
        Assert.assertEquals(rowItem2.getCellItem(1).getIndex(),cell4.getIndex());
        Assert.assertEquals(rowItem2.getCellItem(1).getModelObject(),cell4.getModelObject());
        Assert.assertEquals(rowItem2.getRepeater().size(),2);

        Assert.assertEquals(rowItem1.getIndex(),10);
        Assert.assertEquals(rowItem2.getIndex(),11);

    }

    /**
     *
     * buildRows( Iterator<<? extends T>> iterator)   ,reuse=ITEMSNAVIGATION
    */
    @Test(groups = {"wicketTests"})
    public void buildRows_3(){
        final long itemsPerRequest=4;
        IDataProvider dataProvider =Mockito.mock(IDataProvider.class);
        final Iterator<QuickGridView.CellItem>cells=Mockito.mock(Iterator.class);
        final Iterator rows=Mockito.mock(Iterator.class);
        QuickGridView gridView = new QuickGridView("quickview", dataProvider, ReUse.ITEMSNAVIGATION) {
            @Override
            protected void populate(CellItem item) {
            }

            @Override
            protected void populateEmptyItem(CellItem item) {
            }

            @Override
            public Iterator<CellItem> buildCells(long index, Iterator iterator) {
                return cells;
            }

            @Override
            protected Iterator buildItems(long index, Iterator iterator) {
                return  rows;
            }
        };
        gridView.setItemsPerRequest(10);
        gridView.setColumns(2);
        Iterator dataIterator =Mockito.mock(Iterator.class);
        QuickGridView spy=Mockito.spy(gridView);
        spy.buildRows(dataIterator);
        Mockito.verify(spy,Mockito.times(1)).buildItems(0,dataIterator);

    }


    /**
     *         reuse=ALL
     * buildRows( Iterator<<? extends T>> iterator)
     */
    @Test(groups = {"wicketTests"})
    public void buildRows_4(){
        IDataProvider dataProvider =Mockito.mock(IDataProvider.class);
        final Iterator<QuickGridView.CellItem>cells=Mockito.mock(Iterator.class);
        final Iterator rows=Mockito.mock(Iterator.class);
        QuickGridView gridView = new QuickGridView("quickview", dataProvider, ReUse.ALL) {
            @Override
            protected void populate(CellItem item) {
            }

            @Override
            protected void populateEmptyItem(CellItem item) {
            }

            @Override
            public Iterator<CellItem> buildCells(long index, Iterator iterator) {
                return cells;
            }

            @Override
            protected Iterator buildItems(long index, Iterator iterator) {
                return  rows;
            }

            @Override
            public long gridSize() {
                return 10;
            }
        };
        gridView.setItemsPerRequest(10);
        gridView.setColumns(2);
        Iterator dataIterator =Mockito.mock(Iterator.class);
        QuickGridView spy=Mockito.spy(gridView);
        spy.buildRows(dataIterator);
        Mockito.verify(spy,Mockito.times(1)).buildItems(10,dataIterator);

    }

    /**
     * page=5
     */

    @Test(groups = {"wicketTests"})
    public void  reuseItemsForCurrentPage_1(){
        final IRepeaterUtil util=Mockito.mock(IRepeaterUtil.class);
        IDataProvider dataProvider =Mockito.mock(IDataProvider.class);
        final Iterator<QuickGridView.CellItem>cells=Mockito.mock(Iterator.class);
        final Iterator rows=Mockito.mock(Iterator.class);
        final Iterator reuseItems=Mockito.mock(Iterator.class);
        Mockito.when(util.reuseItemsIfModelsEqual(Mockito.any(Iterator.class), Mockito.any(Iterator.class))).thenReturn(reuseItems);
        QuickGridView gridView = new QuickGridView("quickview", dataProvider, ReUse.CURRENTPAGE) {
            @Override
            protected void populate(CellItem item) {
            }

            @Override
            protected void populateEmptyItem(CellItem item) {
            }

            @Override
            public Iterator<CellItem> buildCells(long index, Iterator iterator) {
                return cells;
            }

            @Override
            protected Iterator buildItems(long index, Iterator iterator) {
                return  rows;
            }

            @Override
            protected IRepeaterUtil getRepeaterUtil() {
                return util;
            }

            @Override
            public Iterator cells() {
                return cells;
            }
        };
        final long itemsPerRequest=6;
        gridView.setRows(2);
        gridView.setItemsPerRequest(itemsPerRequest);
        gridView.setColumns(3);
        final long page=5;
        final long start=6*page;
        Iterator data=Mockito.mock(Iterator.class);
        Mockito.when(dataProvider.iterator(start,itemsPerRequest)).thenReturn(data);
        QuickGridView spy=  Mockito.spy(gridView);
        spy.reuseItemsForCurrentPage(5);
        Mockito.verify(spy,Mockito.times(1)).buildItems(0,data);
        Mockito.verify(spy,Mockito.times(1)).buildRows(0,reuseItems);
    }

    /*
     *start index=10
    */
    @Test(groups = {"wicketTests"})
    public void buildItems_1(){
        final long itemsPerRequest=4;
        IDataProvider dataProvider =Mockito.mock(IDataProvider.class);
        final Iterator<QuickGridView.CellItem<Integer>>cells=Mockito.mock(Iterator.class);
        final Iterator<QuickGridView.RowItem<Integer>>rows=Mockito.mock(Iterator.class);
        QuickGridView<Integer> gridView = new QuickGridView<Integer>("quickview", dataProvider, ReUse.ITEMSNAVIGATION) {
            @Override
            protected void populate(CellItem<Integer> item) {
            }

            @Override
            protected void populateEmptyItem(CellItem<Integer> item) {
            }

            @Override
            public Iterator<CellItem<Integer>> buildCells(long index, Iterator<? extends Integer> iterator) {
                    return cells;
            }

            @Override
            public Iterator<RowItem<Integer>> buildRows(long rowIndex, Iterator<CellItem<Integer>> iterator) {
                return rows ;
            }
        };
        gridView.setItemsPerRequest(itemsPerRequest);
        gridView.setColumns(2);
        Iterator dataIterator =Mockito.mock(Iterator.class);
        QuickGridView spy=Mockito.spy(gridView);
        spy.buildItems(10, dataIterator);
        Mockito.verify(spy,Mockito.times(1)).buildRows(5,cells);
        Mockito.verify(spy,Mockito.times(1)).buildCells(10,dataIterator);

    }


    /**
     * buildCellItem(object)   columns< list
     */
    @Test(groups = {"wicketTests"})
    public void addCells_1(){
        final List<Integer>list=new ArrayList<Integer>();
        list.add(10);
        list.add(20);
        list.add(30);
        list.add(40);

        IDataProvider provider = new ListDataProvider(list);
         final int columns=3;
        QuickGridView<Integer> grid=new QuickGridView<Integer>("grid",provider) {
            @Override
            protected void populate(CellItem<Integer> item) {
            }

            @Override
            protected void populateEmptyItem(CellItem<Integer> item) {
            }


        } ;

        grid.setColumns(columns);

        Iterator<? extends Integer>iterator=list.iterator();
        QuickGridView.RowItem rowItem=grid.buildRowItem("20",20);
       List<Item<Integer>>cells=grid.addCells(rowItem, iterator);
       Assert.assertEquals(cells.size(),columns);

       QuickGridView.CellItem cell=(QuickGridView.CellItem)cells.get(0);
        Assert.assertEquals(cell.getModelObject(),list.get(0));
        Assert.assertFalse(cell.isEmpty());

        QuickGridView.CellItem cell2=(QuickGridView.CellItem)cells.get(1);
        Assert.assertEquals(cell2.getModelObject(),list.get(1));
        Assert.assertFalse(cell2.isEmpty());

        QuickGridView.CellItem cell3=(QuickGridView.CellItem)cells.get(2);
        Assert.assertEquals(cell3.getModelObject(),list.get(2));
        Assert.assertFalse(cell3.isEmpty());

        Assert.assertEquals(rowItem.getRepeater().size(),columns);
        for(int c=0;c<columns;c++){
            QuickGridView.CellItem item= (QuickGridView.CellItem)rowItem.getRepeater().get(c);
            Assert.assertEquals(item.getModelObject(), list.get(c));
        }

    }


    /**
     * buildCellItem(object)  ,columns>list
     */

    @Test(groups = {"wicketTests"})
    public void addCells_2(){
        final List<Integer>list=new ArrayList<Integer>();
        list.add(10);
        list.add(20);

        IDataProvider provider = new ListDataProvider(list);
        final int columns=3;
        QuickGridView<Integer> grid=new QuickGridView<Integer>("grid",provider) {
            @Override
            protected void populate(CellItem<Integer> item) {
            }

            @Override
            protected void populateEmptyItem(CellItem<Integer> item) {
            }


        } ;

        grid.setColumns(columns);

        Iterator<? extends Integer>iterator=list.iterator();
        QuickGridView.RowItem rowItem=grid.buildRowItem("20",20);
        List<Item<Integer>>cells=grid.addCells(rowItem, iterator);
        Assert.assertEquals(cells.size(),columns);

        QuickGridView.CellItem cell=(QuickGridView.CellItem)cells.get(0);
        Assert.assertEquals(cell.getModelObject(),list.get(0));
        Assert.assertFalse(cell.isEmpty());

        QuickGridView.CellItem cell2=(QuickGridView.CellItem)cells.get(1);
        Assert.assertEquals(cell2.getModelObject(),list.get(1));
        Assert.assertFalse(cell2.isEmpty());

        QuickGridView.CellItem cell3=(QuickGridView.CellItem)cells.get(2);
        Assert.assertNull(cell3.getModelObject());
        Assert.assertTrue(cell3.isEmpty());

        Assert.assertEquals(rowItem.getRepeater().size(),columns);
        for(int c=0;c<columns;c++){
            QuickGridView.CellItem item= (QuickGridView.CellItem)rowItem.getRepeater().get(c);
           // Assert.assertEquals(item.getModelObject(),list.get(c));
        }

    }

    /**
     * buildCellItem(object)  ,columns==list
     */
    @Test(groups = {"wicketTests"})
    public void addCells_3(){
        final List<Integer>list=new ArrayList<Integer>();
        list.add(10);
        list.add(20);
        IDataProvider provider = new ListDataProvider(list);
        final int columns=2;
        QuickGridView<Integer> grid=new QuickGridView<Integer>("grid",provider) {
            @Override
            protected void populate(CellItem<Integer> item) {
            }

            @Override
            protected void populateEmptyItem(CellItem<Integer> item) {
            }


        } ;

        grid.setColumns(columns);

        Iterator<? extends Integer>iterator=list.iterator();
        QuickGridView.RowItem rowItem=grid.buildRowItem("20",20);
        List<Item<Integer>>cells=grid.addCells(rowItem, iterator);
        Assert.assertEquals(cells.size(),columns);

        QuickGridView.CellItem cell=(QuickGridView.CellItem)cells.get(0);
        Assert.assertEquals(cell.getModelObject(),list.get(0));
        Assert.assertFalse(cell.isEmpty());

        QuickGridView.CellItem cell2=(QuickGridView.CellItem)cells.get(1);
        Assert.assertEquals(cell2.getModelObject(),list.get(1));
        Assert.assertFalse(cell2.isEmpty());

        Assert.assertEquals(rowItem.getRepeater().size(),columns);
        for(int c=0;c<columns;c++){
            QuickGridView.CellItem item= (QuickGridView.CellItem)rowItem.getRepeater().get(c);
            Assert.assertEquals(item.getModelObject(),list.get(c));
        }
    }

    /**
     * addCells(RowItem row,CellItem...cell);
     */
    @Test(groups = {"wicketTests"})
    public void  addCells_4(){
        List<Integer>list=new ArrayList<Integer>();
        IDataProvider provider = new ListDataProvider(list);
        QuickGridView<Integer> grid=new QuickGridView<Integer>("grid",provider) {
            @Override
            protected void populate(CellItem<Integer> item) {
            }

            @Override
            protected void populateEmptyItem(CellItem<Integer> item) {
            }
        };
         grid.setColumns(3);
        QuickGridView.CellItem cell1=grid.buildCellItem(10);
        QuickGridView.CellItem cell2=grid.buildCellItem(20);

        QuickGridView.RowItem rowItem=grid.buildRowItem();
       grid.addCells(rowItem,cell1,cell2);
       QuickGridView.CellItem actual1=(QuickGridView.CellItem) rowItem.getRepeater().get(0);
        Assert.assertEquals(actual1.getModelObject(),cell1.getModelObject());
        Assert.assertEquals(actual1.getMarkupId(),cell1.getMarkupId());
        Assert.assertEquals(actual1.getIndex(),cell1.getIndex());
        QuickGridView.CellItem actual2=(QuickGridView.CellItem) rowItem.getRepeater().get(1);
        Assert.assertEquals(actual2.getModelObject(),cell2.getModelObject());
        Assert.assertEquals(actual2.getMarkupId(),cell2.getMarkupId());
        Assert.assertEquals(actual2.getIndex(),cell2.getIndex());
        QuickGridView.CellItem actual3=(QuickGridView.CellItem) rowItem.getRepeater().get(2);
         Assert.assertNull(actual3.getModelObject());
        Assert.assertTrue(actual3.isEmpty());
        Assert.assertEquals(rowItem.getRepeater().size(),3);
    }


    /**
     *    ajax=true
     */
    @Test(groups = {"wicketTests"})
    public void addRow_1(){
        IDataProvider provider = Mockito.mock(IDataProvider.class);
        List<String> scripts=Mockito.mock(List.class);
        final QuickViewBase.Synchronizer synchronizer=Mockito.mock(QuickViewBase.Synchronizer.class);
        Mockito.when(synchronizer.getPrependScripts()).thenReturn(scripts);
        final IRepeaterUtil util=Mockito.mock(IRepeaterUtil.class);
        final String call="insert after";
        QuickGridView.RowItem row=Mockito.mock(QuickGridView.RowItem.class);
        final MarkupContainer parent=Mockito.mock(MarkupContainer.class);
        Mockito.when(util.insertAfter(row,parent)).thenReturn(call);
        QuickGridView<Integer> grid=new QuickGridView<Integer>("grid",provider) {
            @Override
            protected void populate(CellItem<Integer> item) {
            }

            @Override
            protected void populateEmptyItem(CellItem<Integer> item) {
            }

            @Override
            public Synchronizer getSynchronizer() {
                return synchronizer;
            }

            @Override
            public boolean isAjax() {
                return true;
            }

            @Override
            public MarkupContainer simpleAdd(Component... c) {
                return this;
            }

            @Override
            protected IRepeaterUtil getRepeaterUtil() {
                return util;
            }

            @Override
            protected MarkupContainer _getParent() {
                return parent;
            }

        } ;
        QuickGridView spy=Mockito.spy(grid);

        spy.addRow(row);
          Mockito.verify(spy,Mockito.times(1)).simpleAdd(row);
           Mockito.verify(scripts,Mockito.times(1)).add(call);
        Mockito.verify(synchronizer,Mockito.times(1)).add(row);
    }


    /**
     *    ajax=false
     */
    @Test(groups = {"wicketTests"})
    public void addRow_2(){
        IDataProvider provider = Mockito.mock(IDataProvider.class);
        final IRepeaterUtil util=Mockito.mock(IRepeaterUtil.class);
        final String call="insert after";
        QuickGridView.RowItem row=Mockito.mock(QuickGridView.RowItem.class);
        final MarkupContainer parent=Mockito.mock(MarkupContainer.class);
        Mockito.when(util.insertAfter(row,parent)).thenReturn(call);
        QuickGridView<Integer> grid=new QuickGridView<Integer>("grid",provider) {
            @Override
            protected void populate(CellItem<Integer> item) {
            }

            @Override
            protected void populateEmptyItem(CellItem<Integer> item) {
            }



            @Override
            public boolean isAjax() {
                return false;
            }

            @Override
            public MarkupContainer simpleAdd(Component... c) {
                return this;
            }

            @Override
            protected IRepeaterUtil getRepeaterUtil() {
                return util;
            }

            @Override
            protected MarkupContainer _getParent() {
                return parent;
            }

          } ;
        QuickGridView spy=Mockito.spy(grid);

        spy.addRow(row);
        Mockito.verify(spy,Mockito.times(1)).simpleAdd(row);

    }



    /**
     *    ajax=true
     */
    @Test(groups = {"wicketTests"})
    public void addRowAtStart_1(){
        IDataProvider provider = Mockito.mock(IDataProvider.class);
        List<String> scripts=Mockito.mock(List.class);
        final QuickViewBase.Synchronizer synchronizer=Mockito.mock(QuickViewBase.Synchronizer.class);
        Mockito.when(synchronizer.getPrependScripts()).thenReturn(scripts);
        final IRepeaterUtil util=Mockito.mock(IRepeaterUtil.class);
        final String call="insert before";
        QuickGridView.RowItem row=Mockito.mock(QuickGridView.RowItem.class);
        final MarkupContainer parent=Mockito.mock(MarkupContainer.class);
        Mockito.when(util.insertBefore(row,parent)).thenReturn(call);
        QuickGridView<Integer> grid=new QuickGridView<Integer>("grid",provider) {
            @Override
            protected void populate(CellItem<Integer> item) {
            }

            @Override
            protected void populateEmptyItem(CellItem<Integer> item) {
            }

            @Override
            public Synchronizer getSynchronizer() {
                return synchronizer;
            }

            @Override
            public boolean isAjax() {
                return true;
            }

            @Override
            public MarkupContainer simpleAdd(Component... c) {
                return this;
            }

            @Override
            protected IRepeaterUtil getRepeaterUtil() {
                return util;
            }

            @Override
            protected MarkupContainer _getParent() {
                return parent;
            }

        } ;
        QuickGridView spy=Mockito.spy(grid);
        spy.addRowAtStart(row);
        Mockito.verify(spy,Mockito.times(1)).simpleAdd(row);
        Mockito.verify(scripts,Mockito.times(1)).add(call);
        Mockito.verify(synchronizer,Mockito.times(1)).add(row);
    }



    /**
     *    ajax=true
     */
    @Test(groups = {"wicketTests"})
    public void removeRow_1(){
        IDataProvider provider = Mockito.mock(IDataProvider.class);
        List<String> scripts=Mockito.mock(List.class);
        final QuickViewBase.Synchronizer synchronizer=Mockito.mock(QuickViewBase.Synchronizer.class);
        Mockito.when(synchronizer.getPrependScripts()).thenReturn(scripts);
        final IRepeaterUtil util=Mockito.mock(IRepeaterUtil.class);
        final String call="remove row";
        QuickGridView.RowItem row=Mockito.mock(QuickGridView.RowItem.class);
        final MarkupContainer parent=Mockito.mock(MarkupContainer.class);
        Mockito.when(util.removeItem(row)).thenReturn(call);
        QuickGridView<Integer> grid=new QuickGridView<Integer>("grid",provider) {
            @Override
            protected void populate(CellItem<Integer> item) {
            }

            @Override
            protected void populateEmptyItem(CellItem<Integer> item) {
            }

            @Override
            public Synchronizer getSynchronizer() {
                return  synchronizer;
            }

            @Override
            public boolean isAjax() {
                return true;
            }

            @Override
            public MarkupContainer simpleRemove(Component c) {
                return this;
            }

            @Override
            protected IRepeaterUtil getRepeaterUtil() {
                return util;
            }

            @Override
            protected MarkupContainer _getParent() {
                return parent;
            }

          } ;
        QuickGridView spy=Mockito.spy(grid);
        spy.removeRow(row);
        Mockito.verify(spy,Mockito.times(1)).simpleRemove(row);
        Mockito.verify(scripts,Mockito.times(1)).add(call);
        Mockito.verify(synchronizer,Mockito.times(1)).add(row);
    }


    /**
     *    ajax=true  ,parrentaddedtotarget=true
     */
    @Test(groups = {"wicketTests"})
    public void removeRow_2(){
        IDataProvider provider = Mockito.mock(IDataProvider.class);
        final AjaxRequestTarget target=Mockito.mock(AjaxRequestTarget.class);
        final IRepeaterUtil util=Mockito.mock(IRepeaterUtil.class);
        final String call="remove row";
        QuickGridView.RowItem row=Mockito.mock(QuickGridView.RowItem.class);
        final MarkupContainer parent=Mockito.mock(MarkupContainer.class);
        Mockito.when(util.removeItem(row)).thenReturn(call);
        QuickGridView<Integer> grid=new QuickGridView<Integer>("grid",provider) {
            @Override
            protected void populate(CellItem<Integer> item) {
            }

            @Override
            protected void populateEmptyItem(CellItem<Integer> item) {
            }

            @Override
            public AjaxRequestTarget getAjaxRequestTarget() {
                return target;
            }

            @Override
            public boolean isAjax() {
                return true;
            }

            @Override
            public MarkupContainer simpleRemove(Component c) {
                return this;
            }

            @Override
            protected IRepeaterUtil getRepeaterUtil() {
                return util;
            }

            @Override
            protected MarkupContainer _getParent() {
                return parent;
            }

        } ;
        QuickGridView spy=Mockito.spy(grid);
        spy.removeRow(row);
        Mockito.verify(spy,Mockito.times(1)).simpleRemove(row);
        Mockito.verify(target,Mockito.never()).prependJavaScript(call);
        Mockito.verify(target,Mockito.never()).add(row);
    }

    @Test(groups = {"wicketTests"})
    public void removeRow_3(){
        IDataProvider data=Mockito.mock(IDataProvider.class);
        final QuickGridView.RowItem rowItem=Mockito.mock(QuickGridView.RowItem.class);
        QuickGridView<Integer> grid=new QuickGridView<Integer>("grid",data) {
            @Override
            protected void populate(CellItem<Integer> item) {
            }

            @Override
            protected void populateEmptyItem(CellItem<Integer> item) {
            }

            @Override
            public void removeRow(RowItem<Integer> rowItem) {                
            }

            @Override
            public RowItem<Integer> getRow(int row) {
                return  rowItem;
            }
        };
       QuickGridView spy= Mockito.spy(grid);
        spy.removeRow(3);
        Mockito.verify(spy,Mockito.times(1)).getRow(3);
        Mockito.verify(spy,Mockito.times(1)).removeRow(rowItem);

    }

    @Test(groups = {"wicketTests"})
    public void getRowItem_1(){
        List<Integer>list=new ArrayList<Integer>();
        IDataProvider provider = new ListDataProvider(list);
        final AjaxRequestTarget target=Mockito.mock(AjaxRequestTarget.class);
        final IRepeaterUtil util=Mockito.mock(IRepeaterUtil.class);
        final String call="remove row";
        QuickGridView.RowItem row=Mockito.mock(QuickGridView.RowItem.class);
        final MarkupContainer parent=Mockito.mock(MarkupContainer.class);
        Mockito.when(util.removeItem(row)).thenReturn(call);
        QuickGridView<Integer> grid=new QuickGridView<Integer>("grid",provider) {
            @Override
            protected void populate(CellItem<Integer> item) {
            }

            @Override
            protected void populateEmptyItem(CellItem<Integer> item) {
            }

        } ;
        QuickGridView.RowItem row1=grid.buildRowItem(grid.newChildId(),0);
        grid.add(row1);
        QuickGridView.RowItem row2=grid.buildRowItem(grid.newChildId(),1);
        grid.add(row2);

         QuickGridView.RowItem actual1=grid.getRow(0);

        Assert.assertEquals(row1.getMarkupId(),actual1.getMarkupId());
        Assert.assertEquals(row1.getIndex(),actual1.getIndex());
        QuickGridView.RowItem actual2=grid.getRow(1);
        Assert.assertEquals(row2.getMarkupId(),actual2.getMarkupId());
        Assert.assertEquals(row2.getIndex(),actual2.getIndex());
    }

    @Test(groups = {"wicketTests"})
    public void getLastRow_1(){
        final QuickGridView.RowItem rowItem=Mockito.mock(QuickGridView.RowItem.class);
        List<Integer>list=new ArrayList<Integer>();
        IDataProvider provider = new ListDataProvider(list);
        QuickGridView<Integer> grid=new QuickGridView<Integer>("grid",provider) {
            @Override
            protected void populate(CellItem<Integer> item) {
            }

            @Override
            protected void populateEmptyItem(CellItem<Integer> item) {
            }

            @Override
            public RowItem<Integer> getRow(int row) {
                return rowItem;
            }

            @Override
            public int size() {
                return 0;
            }
        } ;


        QuickGridView spy=Mockito.spy(grid);
      QuickGridView.RowItem actual= spy.getLastRowItem();
      Assert.assertNull(actual);

    }

    @Test(groups = {"wicketTests"})
    public void getLastRow_2(){
        final QuickGridView.RowItem rowItem=Mockito.mock(QuickGridView.RowItem.class);
        List<Integer>list=new ArrayList<Integer>();
        IDataProvider provider = new ListDataProvider(list);
        QuickGridView<Integer> grid=new QuickGridView<Integer>("grid",provider) {
            @Override
            protected void populate(CellItem<Integer> item) {
            }

            @Override
            protected void populateEmptyItem(CellItem<Integer> item) {
            }

            @Override
            public RowItem<Integer> getRow(int row) {
                return rowItem;
            }

            @Override
            public int size() {
                return 1;
            }
        } ;

        QuickGridView spy=Mockito.spy(grid);
        QuickGridView.RowItem actual= spy.getLastRowItem();
        Assert.assertEquals(actual, rowItem);

    }

    @Test(groups = {"wicketTests"})
    public void findFirstEmptyCell_1(){
        List<Integer>list=new ArrayList<Integer>();
        IDataProvider provider = new ListDataProvider(list);
        QuickGridView<Integer> grid=new QuickGridView<Integer>("grid",provider) {
            @Override
            protected void populate(CellItem<Integer> item) {
            }

            @Override
            protected void populateEmptyItem(CellItem<Integer> item) {
            }
        };
        grid.setColumns(3);
        QuickGridView.CellItem cell1=grid.buildCellItem(10);
        QuickGridView.RowItem rowItem=grid.buildRowItem("56",0);
       rowItem.getRepeater().add(cell1);
        QuickGridView.CellItem cell2=grid.buildCellItem(20);
        rowItem.getRepeater().add(cell2);
        QuickGridView.CellItem cell3=grid.buildEmptyCellItem();
        rowItem.getRepeater().add(cell3);
        grid.addRow(rowItem);
         QuickGridView.CellItem actual=grid.findFirstEmptyCell();
        QuickGridView.CellItem expected=(QuickGridView.CellItem)rowItem.getRepeater().get(2);
        Assert.assertTrue(actual.isEmpty());
        Assert.assertEquals(actual.getMarkupId(), expected.getMarkupId());
        Assert.assertEquals(actual.getIndex(), expected.getIndex());

    }

    @Test(groups = {"wicketTests"})
    public void findFirstEmptyCell_2(){
        List<Integer>list=new ArrayList<Integer>();
        IDataProvider provider = new ListDataProvider(list);
        QuickGridView<Integer> grid=new QuickGridView<Integer>("grid",provider) {
            @Override
            protected void populate(CellItem<Integer> item) {
            }

            @Override
            protected void populateEmptyItem(CellItem<Integer> item) {
            }
        };
        grid.setColumns(3);
        QuickGridView.CellItem cell1=grid.buildCellItem(10);
        QuickGridView.RowItem rowItem=grid.buildRowItem("56",0);
        rowItem.getRepeater().add(cell1);
        QuickGridView.CellItem cell2=grid.buildCellItem(20);
        rowItem.getRepeater().add(cell2);
        QuickGridView.CellItem cell3=grid.buildCellItem(30);
        rowItem.getRepeater().add(cell3);
        grid.addRow(rowItem);

        QuickGridView.CellItem actual=grid.findFirstEmptyCell();
       Assert.assertNull(actual);
    }

    @Test(groups = {"wicketTests"})
      public void RowItem_getCellItem(){
        IDataProvider data=Mockito.mock(IDataProvider.class);
        QuickGridView<Integer> grid=new QuickGridView<Integer>("grid",data) {
            @Override
            protected void populate(CellItem<Integer> item) {
            }

            @Override
            protected void populateEmptyItem(CellItem<Integer> item) {
            }
        };
        grid.setColumns(2);
       QuickGridView.RowItem row= grid.buildRowItem();
        QuickGridView.CellItem cell1=grid.buildCellItem(10);
        QuickGridView.CellItem cell2=grid.buildCellItem(20);
         grid.addCells(row,cell1,cell2) ;

      QuickGridView.CellItem actual1= row.getCellItem(0);
        Assert.assertEquals(actual1.getMarkupId(),cell1.getMarkupId());
         Assert.assertEquals(actual1.getIndex(),cell1.getIndex());
        Assert.assertEquals(actual1.getModelObject(),cell1.getModelObject());

        QuickGridView.CellItem actual2= row.getCellItem(1);
        Assert.assertEquals(actual2.getMarkupId(),cell2.getMarkupId());
        Assert.assertEquals(actual2.getIndex(),cell2.getIndex());
        Assert.assertEquals(actual2.getModelObject(),cell2.getModelObject());
    }

    @Test(groups = {"wicketTests"})
    public void cells_1(){
        List<Integer>list=new ArrayList<Integer>();
        IDataProvider provider = new ListDataProvider(list);
        QuickGridView<Integer> grid=new QuickGridView<Integer>("grid",provider) {
            @Override
            protected void populate(CellItem<Integer> item) {
            }

            @Override
            protected void populateEmptyItem(CellItem<Integer> item) {
            }
        };
        grid.setColumns(3);
        QuickGridView.CellItem cell1=grid.buildCellItem(10);
        QuickGridView.RowItem rowItem=grid.buildRowItem("56",0);
        rowItem.getRepeater().add(cell1);
        QuickGridView.CellItem cell2=grid.buildCellItem(20);
        rowItem.getRepeater().add(cell2);
        QuickGridView.CellItem cell3=grid.buildEmptyCellItem();
        rowItem.getRepeater().add(cell3);
        grid.addRow(rowItem);

       Iterator<QuickGridView.CellItem<Integer>> it= grid.cells();
        QuickGridView.CellItem<Integer>actual1= it.next();
        Assert.assertEquals(actual1.getMarkupId(),cell1.getMarkupId());
        Assert.assertEquals(actual1.getModelObject(),cell1.getModelObject());
        Assert.assertEquals(actual1.getIndex(),cell1.getIndex());
        QuickGridView.CellItem<Integer>actual2= it.next();
        Assert.assertEquals(actual2.getMarkupId(),cell2.getMarkupId());
        Assert.assertEquals(actual2.getModelObject(),cell2.getModelObject());
        Assert.assertEquals(actual2.getIndex(),cell2.getIndex());
        QuickGridView.CellItem<Integer>actual3= it.next();
        Assert.assertEquals(actual3.getMarkupId(),cell3.getMarkupId());
        Assert.assertNull(actual3.getModelObject());
        Assert.assertEquals(actual1.getIndex(),cell1.getIndex());
    }

    @Test(groups = {"wicketTests"})
    public void rows_1(){
        List<Integer>list=new ArrayList<Integer>();
        IDataProvider provider = new ListDataProvider(list);
        QuickGridView<Integer> grid=new QuickGridView<Integer>("grid",provider) {
            @Override
            protected void populate(CellItem<Integer> item) {
            }

            @Override
            protected void populateEmptyItem(CellItem<Integer> item) {
            }
        };
        grid.setRows(2);
        QuickGridView.RowItem row1=grid.buildRowItem();
        QuickGridView.RowItem row2=grid.buildRowItem();
        grid.addRow(row1);
        grid.addRow(row2);
        Iterator<QuickGridView.RowItem<Integer>>rows=  grid.rows();
         QuickGridView.RowItem actual1=rows.next();
        QuickGridView.RowItem actual2=rows.next();
        Assert.assertEquals(actual1.getMarkupId(),row1.getMarkupId());
        Assert.assertEquals(actual1.getIndex(),row1.getIndex());

        Assert.assertEquals(actual2.getMarkupId(),row2.getMarkupId());
        Assert.assertEquals(actual2.getIndex(),row2.getIndex());

    }

    @Test(groups = {"wicketTests"})
    public void addRows_1(){
        IDataProvider dataProvider=Mockito.mock(IDataProvider.class);
        QuickGridView.RowItem rowItem1=new QuickGridView.RowItem("1",1,new Model());
        QuickGridView.RowItem rowItem2=new QuickGridView.RowItem("2",2,new Model());
        final Iterator<QuickGridView.RowItem>rows=Mockito.mock(Iterator.class);
        Mockito.when(rows.hasNext()).thenReturn(true).thenReturn(true).thenReturn(false);
        Mockito.when(rows.next()).thenReturn(rowItem1).thenReturn(rowItem2);
        QuickGridView gridView=new QuickGridView("grid",dataProvider){
         @Override
         protected void populateEmptyItem(CellItem item) {
                      }

         @Override
         protected void populate(CellItem item) {
         }

         @Override
         public Iterator buildRows(Iterator iterator) {
              return rows;
         }

            @Override
            public QuickGridView addRow(RowItem rowItem) {
                return this;
            }
        };
        gridView.setRows(2);
        gridView.setColumns(2);
        gridView.setItemsPerRequest(4);
        Iterator data=Mockito.mock(Iterator.class);        
        QuickGridView spy=Mockito.spy(gridView);
        spy.addRows(data);
        Mockito.verify(spy,Mockito.times(1)).buildRows(data);
        Mockito.verify(spy,Mockito.times(1)).addRow(rowItem1);
        Mockito.verify(spy,Mockito.times(1)).addRow(rowItem2);
    }

    List<Integer>data(int size){
        List<Integer>list=new ArrayList<Integer>();
        for(int i=0;i<size;i++){
            list.add(i) ;
        }
        return list;
    }

}
