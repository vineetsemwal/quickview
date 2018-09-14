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
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.model.IModel;
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
        WicketTester tester = new WicketTester(createMockApplication());
    }

    private static WebApplication createMockApplication() {
        WebApplication app = new QuickMockApplication();
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
        int rows = grid.getRows();
        int columns = grid.getColumns();
        Assert.assertEquals(columns, 1);
        Assert.assertEquals(grid.getRows(), Integer.MAX_VALUE);
        Assert.assertTrue(grid.getReuseStrategy() instanceof DefaultQuickReuseStrategy);
    }

    @Test(groups = {"wicketTests"})
    public void constructor_2() {
        IDataProvider provider = Mockito.mock(IDataProvider.class);
        QuickGridView grid = new QuickGridView("grid", provider, new ItemsNavigationStrategy()) {
            @Override
            protected void populate(CellItem item) {
            }

            @Override
            protected void populateEmptyItem(CellItem item) {
            }
        };
        int rows = grid.getRows();
        int columns = grid.getColumns();
        Assert.assertEquals(columns, 1);
        Assert.assertEquals(grid.getRows(), Integer.MAX_VALUE);
        Assert.assertTrue(grid.getReuseStrategy() instanceof ItemsNavigationStrategy);
    }

    @Test(groups = {"wicketTests"})
    public void setRows_1() {
        IDataProvider provider = Mockito.mock(IDataProvider.class);
        QuickGridView grid = new QuickGridView("grid", provider) {
            @Override
            protected void populate(CellItem item) {
            }

            @Override
            protected void populateEmptyItem(CellItem item) {
            }
        };

        final int rows = 456;
        grid.setRows(rows);
        Assert.assertEquals(grid.getRows(), rows);
        Assert.assertEquals(grid.getItemsPerRequest(), 456);
    }

    @Test(groups = {"wicketTests"}, expectedExceptions = IllegalArgumentException.class)
    public void setRows_2() {
        IDataProvider provider = Mockito.mock(IDataProvider.class);
        QuickGridView grid = new QuickGridView("grid", provider) {
            @Override
            protected void populate(CellItem item) {
            }

            @Override
            protected void populateEmptyItem(CellItem item) {
            }
        };

        final int rows = -1;
        grid.setRows(rows);

    }

    @Test(groups = {"wicketTests"}, expectedExceptions = IllegalArgumentException.class)
    public void setRows_3() {
        IDataProvider provider = Mockito.mock(IDataProvider.class);
        QuickGridView grid = new QuickGridView("grid", provider) {
            @Override
            protected void populate(CellItem item) {
            }

            @Override
            protected void populateEmptyItem(CellItem item) {
            }
        };

        final int rows = 0;
        grid.setRows(rows);

    }

    @Test(groups = {"wicketTests"})
    public void setColumns_1() {
        IDataProvider provider = Mockito.mock(IDataProvider.class);
        QuickGridView grid = new QuickGridView("grid", provider) {
            @Override
            protected void populate(CellItem item) {
            }

            @Override
            protected void populateEmptyItem(CellItem item) {
            }
        };

        final int cols = 10;
        grid.setColumns(cols);
        Assert.assertEquals(grid.getColumns(), cols);
        Assert.assertEquals(grid.getItemsPerRequest(), Integer.MAX_VALUE);
    }

    @Test(groups = {"wicketTests"}, expectedExceptions = IllegalArgumentException.class)
    public void setColumns_2() {
        IDataProvider provider = Mockito.mock(IDataProvider.class);
        QuickGridView grid = new QuickGridView("grid", provider, new ItemsNavigationStrategy()) {
            @Override
            protected void populate(CellItem item) {
            }

            @Override
            protected void populateEmptyItem(CellItem item) {
            }
        };

        final int cols = 0;
        grid.setColumns(cols);

    }

    @Test(groups = {"wicketTests"})
    public void newRowItem_1() {
        IDataProvider provider = Mockito.mock(IDataProvider.class);
        QuickGridView grid = new QuickGridView("grid", provider) {
            @Override
            protected void populate(CellItem item) {
            }

            @Override
            protected void populateEmptyItem(CellItem item) {
            }
        };
        final String id = "id";
        final long index = 845;
        QuickGridView.RowItem row = grid.newRowItem(id, 845);
        Assert.assertEquals(row.getIndex(), index);
        Assert.assertEquals(row.getId(), id);
    }

    @Test(groups = {"wicketTests"})
    public void buildRowItem() {
        IDataProvider provider = Mockito.mock(IDataProvider.class);
        QuickGridView grid = new QuickGridView("grid", provider, new ItemsNavigationStrategy()) {
            @Override
            protected void populate(CellItem item) {
            }

            @Override
            protected void populateEmptyItem(CellItem item) {
            }
        };
        final String id = "id";
        final long index = 845;
        QuickGridView.RowItem row = grid.buildRowItem(id, index);
        RepeatingView repeater = (RepeatingView) row.get(QuickGridView.COLUMNS_REPEATER_ID);
        Assert.assertNotNull(repeater);
    }

    @Test(groups = {"wicketTests"})
    public void rowItem_getRepeater() {
        IDataProvider provider = Mockito.mock(IDataProvider.class);
        QuickGridView grid = new QuickGridView("grid", provider, new ItemsNavigationStrategy()) {
            @Override
            protected void populate(CellItem item) {
            }

            @Override
            protected void populateEmptyItem(CellItem item) {
            }
        };
        final String id = "id";
        final long index = 845;
        QuickGridView.RowItem row = grid.buildRowItem(id, index);
        RepeatingView repeater = row.getRepeater();
        Assert.assertNotNull(repeater);
    }

    @Test(groups = {"wicketTests"})
    public void newCellItem_1() {
        final int object = 89;
        IDataProvider data = Mockito.mock(IDataProvider.class);
        final Model<Integer> model = new Model<Integer>(object);
        Mockito.when(data.model(object)).thenReturn(model);
        QuickGridView<Integer> grid = new QuickGridView<Integer>("grid", data, new ItemsNavigationStrategy()) {
            @Override
            protected void populate(CellItem item) {
            }

            @Override
            protected void populateEmptyItem(CellItem item) {
            }
        };

        final String id = "id";
        final long index = 845;
        WicketTester tester = new WicketTester(createMockApplication());
        QuickGridView.CellItem cell = grid.newCellItem(id, index, model);
        tester.startComponentInPage(cell);
        Assert.assertEquals(cell.getId(), id);
        Assert.assertEquals(cell.getIndex(), index);
        Assert.assertEquals(cell.getModelObject(), object);
        Assert.assertFalse(cell.isEmpty());
    }

    @Test(groups = {"wicketTests"})
    public void newEmptyCellItem_1() {
        final int object = 89;
        IDataProvider data = Mockito.mock(IDataProvider.class);
        final Model<Integer> model = new Model<Integer>(object);
        Mockito.when(data.model(object)).thenReturn(model);
        QuickGridView<Integer> grid = new QuickGridView<Integer>("grid", data) {
            @Override
            protected void populate(CellItem item) {
            }

            @Override
            protected void populateEmptyItem(CellItem item) {
            }
        };

        final String id = "id";
        final long index = 845;
        QuickGridView.CellItem cell = grid.newEmptyCellItem(id, index);
        Assert.assertEquals(cell.getId(), id);
        Assert.assertEquals(cell.getIndex(), index);
        Assert.assertNull(cell.getModelObject());
        Assert.assertTrue(cell.isEmpty());
    }

    /**
     * buildEmptyCellItem(id, index)
     */

    @Test(groups = {"wicketTests"})
    public void buildEmptyCellItem_1() {
        final long childId = 845;
        final QuickGridView.CellItem cell = Mockito.mock(QuickGridView.CellItem.class);
        IDataProvider data = Mockito.mock(IDataProvider.class);
        QuickGridView<Integer> grid = new QuickGridView<Integer>("grid", data) {
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


        };
        QuickGridView spy = Mockito.spy(grid);

        QuickGridView.CellItem actual = spy.buildEmptyCellItem(String.valueOf(childId), childId);
        Mockito.verify(spy, Mockito.times(1)).newEmptyCellItem(String.valueOf(childId), childId);
        Mockito.verify(spy, Mockito.times(1)).populateEmptyItem(cell);
    }


    /**
     * buildEmptyCellItem()
     */

    @Test(groups = {"wicketTests"})
    public void buildEmptyCellItem_2() {
        final long childId = 845;
        final QuickGridView.CellItem cell = Mockito.mock(QuickGridView.CellItem.class);
        IDataProvider data = Mockito.mock(IDataProvider.class);
        QuickGridView<Integer> grid = new QuickGridView<Integer>("grid", data) {
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
            public CellItem<Integer> newCellItem(String id, long index, IModel<Integer> model) {
                return cell;
            }

            @Override
            public String newChildId() {
                return String.valueOf(childId);
            }


        };
        QuickGridView spy = Mockito.spy(grid);
        final long index = 6787;
        QuickGridView.CellItem actual = spy.buildEmptyCellItem(index);
        Mockito.verify(spy, Mockito.times(1)).newEmptyCellItem(String.valueOf(childId), index);
        Mockito.verify(spy, Mockito.times(1)).populateEmptyItem(cell);
    }

    /**
     * buildCellItem(id, index,object)
     */
    @Test(groups = {"wicketTests"})
    public void buildCellItem_1() {
        final long childId = 845;
        final QuickGridView.CellItem cell = Mockito.mock(QuickGridView.CellItem.class);
        IDataProvider data = Mockito.mock(IDataProvider.class);

        QuickGridView<Integer> grid = new QuickGridView<Integer>("grid", data) {
            @Override
            protected void populate(CellItem item) {
            }

            @Override
            protected void populateEmptyItem(CellItem item) {
            }

            @Override
            public CellItem<Integer> newCellItem(String id, long index, IModel<Integer> model) {
                return cell;
            }

            @Override
            public String newChildId() {
                return String.valueOf(childId);
            }


        };
        QuickGridView spy = Mockito.spy(grid);

        final int object = 876;//any object/number
        final long index = 34556;
        Model<Integer> model = new Model<Integer>(object);
        Mockito.when(data.model(object)).thenReturn(model);
        QuickGridView.CellItem actual = spy.buildCellItem(String.valueOf(childId), index, object);
        Mockito.verify(spy, Mockito.times(1)).newCellItem(String.valueOf(childId), index, model);
        Mockito.verify(spy, Mockito.times(1)).populate(cell);
    }


    /**
     * buildCellItem(index,object)
     */
    @Test(groups = {"wicketTests"})
    public void buildCellItem_2() {
        final QuickGridView.CellItem cell = Mockito.mock(QuickGridView.CellItem.class);
        IDataProvider data = Mockito.mock(IDataProvider.class);
        final long childId = 89l;

        QuickGridView<Integer> grid = new QuickGridView<Integer>("grid", data) {
            @Override
            protected void populate(CellItem item) {
            }

            @Override
            protected void populateEmptyItem(CellItem item) {
            }

            @Override
            public CellItem<Integer> newCellItem(String id, long index, IModel<Integer> model) {
                return cell;
            }

            @Override
            public String newChildId() {
                return String.valueOf(childId);
            }

        };
        QuickGridView spy = Mockito.spy(grid);
        final int object = 908;
        final long index = 345;
        Model<Integer> model = new Model<Integer>(object);
        Mockito.when(data.model(object)).thenReturn(model);
        QuickGridView.CellItem actual = spy.buildCellItem(index, object);
        Mockito.verify(spy, Mockito.times(1)).newCellItem(String.valueOf(childId), index, model);
        Mockito.verify(spy, Mockito.times(1)).populate(cell);
    }


    /**
     * buildCellItem(index,object)
     */
    @Test(groups = {"wicketTests"})
    public void buildCellItem_3() {
        final QuickGridView.CellItem cell = Mockito.mock(QuickGridView.CellItem.class);
        IDataProvider data = Mockito.mock(IDataProvider.class);
        final long childId = 89l;

        QuickGridView<Integer> grid = new QuickGridView<Integer>("grid", data) {
            @Override
            protected void populate(CellItem item) {
            }

            @Override
            protected void populateEmptyItem(CellItem item) {
            }

            @Override
            public CellItem<Integer> newCellItem(String id, long index, IModel<Integer> model) {
                return cell;
            }

            @Override
            public String newChildId() {
                return String.valueOf(childId);
            }


        };
        QuickGridView spy = Mockito.spy(grid);
        final int object = 908;
        final long index = 345;
        Model<Integer> model = new Model<Integer>(object);
        Mockito.when(data.model(object)).thenReturn(model);
        QuickGridView.CellItem actual = spy.buildCellItem(index, model);
        Mockito.verify(spy, Mockito.times(1)).newCellItem(String.valueOf(childId), index, model);
        Mockito.verify(spy, Mockito.times(1)).populate(cell);
    }

    /*
     *start index=0
     */
    @Test(groups = {"wicketTests"})
    public void buildCells_1() {
        final long itemsPerRequest = 2;
        List<Integer> data = data(10);
        IDataProvider<Integer> dataProvider = new ListDataProvider<Integer>(data);
        QuickGridView<Integer> gridView = new QuickGridView<Integer>("quickview", dataProvider) {
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
        final long itemsPerRequest = 2;
        List<Integer> data = data(20);
        IDataProvider<Integer> dataProvider = new ListDataProvider<Integer>(data);
        QuickGridView<Integer> gridView = new QuickGridView<Integer>("quickview", dataProvider) {
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
    public void buildRows_1() {
        final long itemsPerRequest = 2;
        List<Integer> data = data(20);
        IDataProvider<Integer> dataProvider = new ListDataProvider<Integer>(data);
        QuickGridView<Integer> gridView = new QuickGridView<Integer>("quickview", dataProvider) {
            @Override
            protected void populate(CellItem<Integer> item) {
            }

            @Override
            protected void populateEmptyItem(CellItem<Integer> item) {
            }
        };
        gridView.setColumns(2);
        gridView.setItemsPerRequest(itemsPerRequest);
        QuickGridView.CellItem<Integer> cell0 = new QuickGridView.CellItem<Integer>("1", 1, new Model<Integer>(1));
        QuickGridView.CellItem<Integer> cell1 = new QuickGridView.CellItem<Integer>("2", 2, new Model<Integer>(2));
        QuickGridView.CellItem<Integer> cell2 = new QuickGridView.CellItem<Integer>("3", 3, new Model<Integer>(3));
        QuickGridView.CellItem<Integer> cell3 = new QuickGridView.CellItem<Integer>("4", 4, new Model<Integer>(4));

        List<QuickGridView.CellItem<Integer>> cells = new ArrayList<QuickGridView.CellItem<Integer>>();
        cells.add(cell0);
        cells.add(cell1);
        cells.add(cell2);
        cells.add(cell3);
        Iterator<QuickGridView.CellItem<Integer>> cellsIterator = cells.iterator();
        Iterator<QuickGridView.RowItem<Integer>> rowsIterator = gridView.buildRows(0, cellsIterator);
        QuickGridView.RowItem<Integer> expectedRowItem0 = rowsIterator.next();
        QuickGridView.RowItem<Integer> expectedRowItem1 = rowsIterator.next();
        Assert.assertFalse(rowsIterator.hasNext());
        Assert.assertEquals(expectedRowItem0.getRepeater().size(), 2);
        Iterator<QuickGridView.CellItem<Integer>> expectedRow0Cells = expectedRowItem0.cellItems();
        QuickGridView.CellItem<Integer> expectedRow0Cell0 = expectedRow0Cells.next();
        QuickGridView.CellItem<Integer> expectedRow0Cell1 = expectedRow0Cells.next();
        Assert.assertEquals(expectedRow0Cell0.getId(), cell0.getId());
        Assert.assertEquals(expectedRow0Cell0.getModelObject(), cell0.getModelObject());
        Assert.assertEquals(expectedRow0Cell1.getId(), cell1.getId());

        Iterator<QuickGridView.CellItem<Integer>> expectedRow1Cells2 = expectedRowItem1.cellItems();
        QuickGridView.CellItem<Integer> expectedRow1Cell0 = expectedRow1Cells2.next();
        QuickGridView.CellItem<Integer> expectedRow1Cell1 = expectedRow1Cells2.next();
        Assert.assertEquals(expectedRow1Cell0.getId(), cell2.getId());
        Assert.assertEquals(expectedRow1Cell0.getModelObject(), cell2.getModelObject());
        Assert.assertEquals(expectedRow1Cell1.getId(), cell3.getId());
        Assert.assertEquals(expectedRow1Cell1.getModelObject(), cell3.getModelObject());
        Assert.assertEquals(expectedRowItem1.getRepeater().size(), 2);

        Assert.assertEquals(expectedRowItem0.getIndex(), 0);
        Assert.assertEquals(expectedRowItem1.getIndex(), 1);

    }

    /**
     * rowIndex=10     buildRows(final long rowIndex,  Iterator<CellItem<T>> iterator)
     */

    @Test(groups = {"wicketTests"})
    public void buildRows_2() {
        final long itemsPerRequest = 2;
        List<Integer> data = data(20);
        IDataProvider<Integer> dataProvider = new ListDataProvider<Integer>(data);
        QuickGridView<Integer> gridView = new QuickGridView<Integer>("quickview", dataProvider) {
            @Override
            protected void populate(CellItem<Integer> item) {
            }

            @Override
            protected void populateEmptyItem(CellItem<Integer> item) {
            }
        };
        gridView.setColumns(2);
        gridView.setItemsPerRequest(itemsPerRequest);
        QuickGridView.CellItem<Integer> cell0 = new QuickGridView.CellItem<Integer>("1", 1, new Model<Integer>(1));
        QuickGridView.CellItem<Integer> cell1 = new QuickGridView.CellItem<Integer>("2", 2, new Model<Integer>(2));
        QuickGridView.CellItem<Integer> cell2 = new QuickGridView.CellItem<Integer>("3", 3, new Model<Integer>(3));
        QuickGridView.CellItem<Integer> cell3 = new QuickGridView.CellItem<Integer>("4", 4, new Model<Integer>(4));

        List<QuickGridView.CellItem<Integer>> cells = new ArrayList<QuickGridView.CellItem<Integer>>();
        cells.add(cell0);
        cells.add(cell1);
        cells.add(cell2);
        cells.add(cell3);
        Iterator<QuickGridView.CellItem<Integer>> cellsIterator = cells.iterator();
        Iterator<QuickGridView.RowItem<Integer>> expectedRowsIterator = gridView.buildRows(10, cellsIterator);
        QuickGridView.RowItem<Integer> expectedRowItem0 = expectedRowsIterator.next();
        QuickGridView.RowItem<Integer> expectedRowItem1 = expectedRowsIterator.next();
        Assert.assertFalse(expectedRowsIterator.hasNext());
        Assert.assertEquals(expectedRowItem0.getRepeater().size(), 2);
        Iterator<QuickGridView.CellItem<Integer>> expectedRow0CellItemIterator = expectedRowItem0.cellItems();
        QuickGridView.CellItem<Integer> expectedRow0Cell0 = expectedRow0CellItemIterator.next();
        QuickGridView.CellItem<Integer> expectedRow0Cell1 = expectedRow0CellItemIterator.next();
        Assert.assertEquals(expectedRow0Cell0.getId(), cell0.getId());
        Assert.assertEquals(expectedRow0Cell0.getModelObject(), cell0.getModelObject());

        Assert.assertEquals(expectedRow0Cell1.getId(), cell1.getId());
        Assert.assertEquals(expectedRow0Cell1.getModelObject(), cell1.getModelObject());
        Assert.assertEquals(expectedRowItem1.getRepeater().size(), 2);

        Iterator<QuickGridView.CellItem<Integer>> expectedRow1CellsIterator = expectedRowItem1.cellItems();
        QuickGridView.CellItem<Integer> expectedRow1Cell0 = expectedRow1CellsIterator.next();
        QuickGridView.CellItem<Integer> expectedRow1Cell1 = expectedRow1CellsIterator.next();

        Assert.assertEquals(expectedRow1Cell0.getId(), cell2.getId());
        Assert.assertEquals(expectedRow1Cell0.getModelObject(), cell2.getModelObject());


        Assert.assertEquals(expectedRow1Cell1.getId(), cell3.getId());
        Assert.assertEquals(expectedRow1Cell1.getModelObject(), cell3.getModelObject());


    }

    /**
     * buildRows( Iterator<<? extends T>> iterator)
     */
    @Test(groups = {"wicketTests"})
    public void buildRows_3() {
        final long itemsPerRequest = 4;
        IDataProvider dataProvider = Mockito.mock(IDataProvider.class);
        final Iterator<QuickGridView.CellItem> cells = Mockito.mock(Iterator.class);
        final Iterator rows = Mockito.mock(Iterator.class);
        final int gridSize = 24;
        QuickGridView gridView = new QuickGridView("quickview", dataProvider) {
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
                return rows;
            }

            @Override
            public long gridSize() {
                return gridSize;
            }
        };
        gridView.setItemsPerRequest(10);
        gridView.setColumns(2);
        Iterator dataIterator = Mockito.mock(Iterator.class);
        QuickGridView spy = Mockito.spy(gridView);
        spy.buildRows(dataIterator);
        Mockito.verify(spy, Mockito.times(1)).buildItems(gridSize, dataIterator);

    }


    /*
     *start index=10
     */
    @Test(groups = {"wicketTests"})
    public void buildItems_1() {
        final long itemsPerRequest = 4;
        IDataProvider dataProvider = Mockito.mock(IDataProvider.class);
        final Iterator<QuickGridView.CellItem<Integer>> cells = Mockito.mock(Iterator.class);
        final Iterator<QuickGridView.RowItem<Integer>> rows = Mockito.mock(Iterator.class);
        QuickGridView<Integer> gridView = new QuickGridView<Integer>("quickview", dataProvider) {
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
                return rows;
            }
        };
        gridView.setItemsPerRequest(itemsPerRequest);
        gridView.setColumns(2);
        Iterator dataIterator = Mockito.mock(Iterator.class);
        QuickGridView spy = Mockito.spy(gridView);
        spy.buildItems(10, dataIterator);
        Mockito.verify(spy, Mockito.times(1)).buildRows(5, cells);
        Mockito.verify(spy, Mockito.times(1)).buildCells(10, dataIterator);

    }


    /**
     * ajax=true
     */
    @Test(groups = {"wicketTests"})
    public void addRow_1() {
        IDataProvider provider = Mockito.mock(IDataProvider.class);
        List<String> scripts = Mockito.mock(List.class);
        final QuickViewBase.Synchronizer synchronizer = Mockito.mock(QuickViewBase.Synchronizer.class);
        Mockito.when(synchronizer.getPrependScripts()).thenReturn(scripts);
        final IRepeaterUtil util = Mockito.mock(IRepeaterUtil.class);
        final String call = "insert after";
        QuickGridView.RowItem row = Mockito.mock(QuickGridView.RowItem.class);
        final MarkupContainer parent = Mockito.mock(MarkupContainer.class);
        Mockito.when(util.append(row, parent, null, null)).thenReturn(call);
        QuickGridView<Integer> grid = new QuickGridView<Integer>("grid", provider) {
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

        };
        QuickGridView spy = Mockito.spy(grid);

        spy.addRow(row);
        Mockito.verify(spy, Mockito.times(1)).simpleAdd(row);
        Mockito.verify(scripts, Mockito.times(1)).add(call);
        Mockito.verify(synchronizer, Mockito.times(1)).add(row);
    }


    /**
     * ajax=true
     */
    @Test(groups = {"wicketTests"})
    public void addRow_1_1() {
        IDataProvider provider = Mockito.mock(IDataProvider.class);
        List<String> scripts = Mockito.mock(List.class);
        final QuickViewBase.Synchronizer synchronizer = Mockito.mock(QuickViewBase.Synchronizer.class);
        Mockito.when(synchronizer.getPrependScripts()).thenReturn(scripts);
        final IRepeaterUtil util = Mockito.mock(IRepeaterUtil.class);
        final String call = "insert after";
        QuickGridView.RowItem row = Mockito.mock(QuickGridView.RowItem.class);
        Component start = new Label("start");
        Component end = new Label("start");
        final MarkupContainer parent = Mockito.mock(MarkupContainer.class);
        Mockito.when(util.append(row, parent, start, end)).thenReturn(call);
        QuickGridView<Integer> grid = new QuickGridView<Integer>("grid", provider, start, end) {
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

        };
        QuickGridView spy = Mockito.spy(grid);

        spy.addRow(row);
        Mockito.verify(spy, Mockito.times(1)).simpleAdd(row);
        Mockito.verify(scripts, Mockito.times(1)).add(call);
        Mockito.verify(synchronizer, Mockito.times(1)).add(row);
    }


    /**
     * ajax=false
     */
    @Test(groups = {"wicketTests"})
    public void addRow_2() {
        IDataProvider provider = Mockito.mock(IDataProvider.class);
        final IRepeaterUtil util = Mockito.mock(IRepeaterUtil.class);
        final String call = "insert after";
        QuickGridView.RowItem row = Mockito.mock(QuickGridView.RowItem.class);
        final MarkupContainer parent = Mockito.mock(MarkupContainer.class);
        Mockito.when(util.append(row, parent, null, null)).thenReturn(call);
        QuickGridView<Integer> grid = new QuickGridView<Integer>("grid", provider) {
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

        };
        QuickGridView spy = Mockito.spy(grid);

        spy.addRow(row);
        Mockito.verify(spy, Mockito.times(1)).simpleAdd(row);

    }


    /**
     * ajax=true
     */
    @Test(groups = {"wicketTests"})
    public void addRowAtStart_1() {
        IDataProvider provider = Mockito.mock(IDataProvider.class);
        List<String> scripts = Mockito.mock(List.class);
        final QuickViewBase.Synchronizer synchronizer = Mockito.mock(QuickViewBase.Synchronizer.class);
        Mockito.when(synchronizer.getPrependScripts()).thenReturn(scripts);
        final IRepeaterUtil util = Mockito.mock(IRepeaterUtil.class);
        final String call = "insert before";
        QuickGridView.RowItem row = Mockito.mock(QuickGridView.RowItem.class);
        final MarkupContainer parent = Mockito.mock(MarkupContainer.class);
        Mockito.when(util.prepend(row, parent, null, null)).thenReturn(call);
        QuickGridView<Integer> grid = new QuickGridView<Integer>("grid", provider) {
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

        };
        QuickGridView spy = Mockito.spy(grid);
        spy.addRowAtStart(row);
        Mockito.verify(spy, Mockito.times(1)).simpleAdd(row);
        Mockito.verify(scripts, Mockito.times(1)).add(call);
        Mockito.verify(synchronizer, Mockito.times(1)).add(row);
    }

    /**
     * ajax=true
     */
    @Test(groups = {"wicketTests"})
    public void addRowAtStart_1_1() {
        IDataProvider provider = Mockito.mock(IDataProvider.class);
        List<String> scripts = Mockito.mock(List.class);
        final QuickViewBase.Synchronizer synchronizer = Mockito.mock(QuickViewBase.Synchronizer.class);
        Mockito.when(synchronizer.getPrependScripts()).thenReturn(scripts);
        final IRepeaterUtil util = Mockito.mock(IRepeaterUtil.class);
        final String call = "insert before";
        QuickGridView.RowItem row = Mockito.mock(QuickGridView.RowItem.class);
        final MarkupContainer parent = Mockito.mock(MarkupContainer.class);
        Component start = new Label("start");
        Component end = new Label("end");
        Mockito.when(util.prepend(row, parent, start, end)).thenReturn(call);
        QuickGridView<Integer> grid = new QuickGridView<Integer>("grid", provider, start, end) {
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

        };
        QuickGridView spy = Mockito.spy(grid);
        spy.addRowAtStart(row);
        Mockito.verify(spy, Mockito.times(1)).simpleAdd(row);
        Mockito.verify(scripts, Mockito.times(1)).add(call);
        Mockito.verify(synchronizer, Mockito.times(1)).add(row);
    }


    /**
     * ajax=true
     */
    @Test(groups = {"wicketTests"})
    public void removeRow_1() {
        IDataProvider provider = Mockito.mock(IDataProvider.class);
        List<String> scripts = Mockito.mock(List.class);
        final QuickViewBase.Synchronizer synchronizer = Mockito.mock(QuickViewBase.Synchronizer.class);
        Mockito.when(synchronizer.getPrependScripts()).thenReturn(scripts);
        final IRepeaterUtil util = Mockito.mock(IRepeaterUtil.class);
        final String call = "remove row";
        QuickGridView.RowItem row = Mockito.mock(QuickGridView.RowItem.class);
        final MarkupContainer parent = Mockito.mock(MarkupContainer.class);
        Mockito.when(util.removeItem(row, parent)).thenReturn(call);
        QuickGridView<Integer> grid = new QuickGridView<Integer>("grid", provider) {
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

        };
        QuickGridView spy = Mockito.spy(grid);
        spy.removeRow(row);
        Mockito.verify(spy, Mockito.times(1)).simpleRemove(row);
        Mockito.verify(scripts, Mockito.times(1)).add(call);
        Mockito.verify(synchronizer, Mockito.never()).add(row);
    }


    /**
     * ajax=true  ,parrentaddedtotarget=true
     */
    @Test(groups = {"wicketTests"})
    public void removeRow_2() {
        IDataProvider provider = Mockito.mock(IDataProvider.class);
        final AjaxRequestTarget target = Mockito.mock(AjaxRequestTarget.class);
        final IRepeaterUtil util = Mockito.mock(IRepeaterUtil.class);
        final String call = "remove row";
        QuickGridView.RowItem row = Mockito.mock(QuickGridView.RowItem.class);
        final MarkupContainer parent = Mockito.mock(MarkupContainer.class);
        Mockito.when(util.removeItem(row, parent)).thenReturn(call);
        QuickGridView<Integer> grid = new QuickGridView<Integer>("grid", provider) {
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

        };
        QuickGridView spy = Mockito.spy(grid);
        spy.removeRow(row);
        Mockito.verify(spy, Mockito.times(1)).simpleRemove(row);
        Mockito.verify(target, Mockito.never()).prependJavaScript(call);
        Mockito.verify(target, Mockito.never()).add(row);
    }

    @Test(groups = {"wicketTests"})
    public void getLastRow_2() {
        List<Integer> list = new ArrayList<Integer>();
        IDataProvider provider = new ListDataProvider(list);
        final QuickGridView.RowItem rowItem = Mockito.mock(QuickGridView.RowItem.class);
        QuickGridView<Integer> grid = new QuickGridView<Integer>("grid", provider) {
            @Override
            protected void populate(CellItem<Integer> item) {
            }

            @Override
            protected void populateEmptyItem(CellItem<Integer> item) {
            }

            @Override
            public Component _lastRenderedItem() {
                return rowItem;
            }

            @Override
            public int size() {
                return 1;
            }
        };

        QuickGridView spy = Mockito.spy(grid);
        QuickGridView.RowItem actual = spy.getLastRowItem();
        Assert.assertEquals(actual, rowItem);

    }

    @Test(groups = {"wicketTests"})
    public void findFirstEmptyCell_1() {
        List<Integer> list = new ArrayList<Integer>();
        IDataProvider provider = new ListDataProvider(list);
        IQuickReuseStrategy reuseStrategy=Mockito.mock(IQuickReuseStrategy.class);
        Mockito.when(reuseStrategy.isPartialUpdatesSupported()).thenReturn(true);
        QuickGridView<Integer> grid = new QuickGridView<Integer>("grid", provider,reuseStrategy) {
            @Override
            protected void populate(CellItem<Integer> item) {
            }

            @Override
            protected void populateEmptyItem(CellItem<Integer> item) {
            }
        };
        grid.setColumns(3);
        QuickGridView.CellItem cell1 = grid.buildCellItem(89789089, 10);
        QuickGridView.RowItem rowItem = grid.buildRowItem("56", 0);
        rowItem.getRepeater().add(cell1);
        QuickGridView.CellItem cell2 = grid.buildCellItem(89789, 20);
        rowItem.getRepeater().add(cell2);
        QuickGridView.CellItem cell3 = grid.buildEmptyCellItem(897745);
        rowItem.getRepeater().add(cell3);
        grid.addRow(rowItem);
        QuickGridView.CellItem actual = grid.findFirstEmptyCell();
        Assert.assertTrue(actual.isEmpty());
        Assert.assertEquals(actual.getMarkupId(), cell3.getMarkupId());

    }


    @Test(groups = {"wicketTests"})
    public void findFirstEmptyCell_2() {
        List<Integer> list = new ArrayList<Integer>();
        IDataProvider provider = new ListDataProvider(list);
        IQuickReuseStrategy reuseStrategy=Mockito.mock(IQuickReuseStrategy.class);
        Mockito.when(reuseStrategy.isPartialUpdatesSupported()).thenReturn(true);
        QuickGridView<Integer> grid = new QuickGridView<Integer>("grid", provider,reuseStrategy) {
            @Override
            protected void populate(CellItem<Integer> item) {
            }

            @Override
            protected void populateEmptyItem(CellItem<Integer> item) {
            }
        };
        grid.setColumns(3);
        QuickGridView.CellItem cell1 = grid.buildCellItem(10, 345);
        QuickGridView.RowItem rowItem = grid.buildRowItem("56", 0);
        rowItem.getRepeater().add(cell1);
        QuickGridView.CellItem cell2 = grid.buildCellItem(20, 546);
        rowItem.getRepeater().add(cell2);
        QuickGridView.CellItem cell3 = grid.buildCellItem(30, 3098);
        rowItem.getRepeater().add(cell3);
        grid.addRow(rowItem);

        QuickGridView.CellItem actual = grid.findFirstEmptyCell();
        Assert.assertNull(actual);
    }


    @Test(groups = {"wicketTests"})
    public void cells_1() {
        List<Integer> list = new ArrayList<Integer>();
        IDataProvider provider = new ListDataProvider(list);
        QuickGridView<Integer> grid = new QuickGridView<Integer>("grid", provider) {
            @Override
            protected void populate(CellItem<Integer> item) {
            }

            @Override
            protected void populateEmptyItem(CellItem<Integer> item) {
            }
        };
        grid.setColumns(3);
        QuickGridView.CellItem cell1 = grid.buildCellItem(89780989, 10);
        QuickGridView.RowItem rowItem = grid.buildRowItem("56", 0);
        rowItem.getRepeater().add(cell1);
        QuickGridView.CellItem cell2 = grid.buildCellItem(897980, 20);
        rowItem.getRepeater().add(cell2);
        QuickGridView.CellItem cell3 = grid.buildEmptyCellItem(98989);
        rowItem.getRepeater().add(cell3);
        grid.addRow(rowItem);

        Iterator<QuickGridView.CellItem<Integer>> it = grid.cells();
        QuickGridView.CellItem<Integer> actual1 = it.next();
        Assert.assertEquals(actual1.getMarkupId(), cell1.getMarkupId());
        Assert.assertEquals(actual1.getModelObject(), cell1.getModelObject());
        Assert.assertEquals(actual1.getIndex(), cell1.getIndex());
        QuickGridView.CellItem<Integer> actual2 = it.next();
        Assert.assertEquals(actual2.getMarkupId(), cell2.getMarkupId());
        Assert.assertEquals(actual2.getModelObject(), cell2.getModelObject());
        Assert.assertEquals(actual2.getIndex(), cell2.getIndex());
        QuickGridView.CellItem<Integer> actual3 = it.next();
        Assert.assertEquals(actual3.getMarkupId(), cell3.getMarkupId());
        Assert.assertNull(actual3.getModelObject());
        Assert.assertEquals(actual1.getIndex(), cell1.getIndex());
    }

    @Test(groups = {"wicketTests"})
    public void rows_1() {
        List<Integer> list = new ArrayList<Integer>();
        IDataProvider provider = new ListDataProvider(list);
        QuickGridView<Integer> grid = new QuickGridView<Integer>("grid", provider) {
            @Override
            protected void populate(CellItem<Integer> item) {
            }

            @Override
            protected void populateEmptyItem(CellItem<Integer> item) {
            }
        };
        grid.setRows(2);
        QuickGridView.RowItem row1 = grid.buildRowItem();
        QuickGridView.RowItem row2 = grid.buildRowItem();
        grid.addRow(row1);
        grid.addRow(row2);
        Iterator<QuickGridView.RowItem<Integer>> rows = grid.rows();
        QuickGridView.RowItem actual1 = rows.next();
        QuickGridView.RowItem actual2 = rows.next();
        Assert.assertEquals(actual1.getMarkupId(), row1.getMarkupId());
        Assert.assertEquals(actual1.getIndex(), row1.getIndex());

        Assert.assertEquals(actual2.getMarkupId(), row2.getMarkupId());
        Assert.assertEquals(actual2.getIndex(), row2.getIndex());

    }

    @Test(groups = {"wicketTests"})
    public void addRows_1() {
        IDataProvider dataProvider = Mockito.mock(IDataProvider.class);
        QuickGridView.RowItem rowItem1 = new QuickGridView.RowItem("1", 1, new Model());
        QuickGridView.RowItem rowItem2 = new QuickGridView.RowItem("2", 2, new Model());
        final Iterator<QuickGridView.RowItem> rows = Mockito.mock(Iterator.class);
        Mockito.when(rows.hasNext()).thenReturn(true).thenReturn(true).thenReturn(false);
        Mockito.when(rows.next()).thenReturn(rowItem1).thenReturn(rowItem2);
        QuickGridView gridView = new QuickGridView("grid", dataProvider) {
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
        Iterator data = Mockito.mock(Iterator.class);
        QuickGridView spy = Mockito.spy(gridView);
        spy.addRows(data);
        Mockito.verify(spy, Mockito.times(1)).buildRows(data);
        Mockito.verify(spy, Mockito.times(1)).addRow(rowItem1);
        Mockito.verify(spy, Mockito.times(1)).addRow(rowItem2);
    }


    @Test(groups = {"wicketTests"})
    public void addRowsAtStart_1() {
        IDataProvider dataProvider = Mockito.mock(IDataProvider.class);
        QuickGridView.RowItem rowItem1 = new QuickGridView.RowItem("1", 1, new Model());
        QuickGridView.RowItem rowItem2 = new QuickGridView.RowItem("2", 2, new Model());
        final Iterator<QuickGridView.RowItem> rows = Mockito.mock(Iterator.class);
        Mockito.when(rows.hasNext()).thenReturn(true).thenReturn(true).thenReturn(false);
        Mockito.when(rows.next()).thenReturn(rowItem1).thenReturn(rowItem2);
        QuickGridView gridView = new QuickGridView("grid", dataProvider) {
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
            public QuickGridView addRowAtStart(RowItem rowItem) {
                return this;
            }
        };
        gridView.setRows(2);
        gridView.setColumns(2);
        gridView.setItemsPerRequest(4);
        Iterator data = Mockito.mock(Iterator.class);
        QuickGridView spy = Mockito.spy(gridView);
        spy.addRowsAtStart(data);
        Mockito.verify(spy, Mockito.times(1)).buildRows(data);
        Mockito.verify(spy, Mockito.times(1)).addRowAtStart(rowItem1);
        Mockito.verify(spy, Mockito.times(1)).addRowAtStart(rowItem2);
    }


    /**
     * one element added
     */
    @Test(groups = {"wicketTests"})
    public void testLastRendered_1() {
        List<Integer> data = data(3);
        IDataProvider<Integer> dataProvider = new ListDataProvider<Integer>(data);
        IQuickReuseStrategy reuseStrategy = new ItemsNavigationStrategy();
        QuickGridView<Integer> quickView = new QuickGridView<Integer>("qv", dataProvider, reuseStrategy) {
            @Override
            protected void populate(CellItem<Integer> item) {

            }

            @Override
            protected void populateEmptyItem(CellItem<Integer> item) {

            }
        };
        QuickGridView<Integer>spy=Mockito.spy(quickView);
        Item<Integer>item0=new Item<>("1",1,new Model<>(10));
        spy.simpleAddRow(item0);
        Component lastRenderedItem=spy._lastRenderedItem();
        Assert.assertEquals(lastRenderedItem.getId(),item0.getId());
    }

    /**
     * two elements added
     */
    @Test(groups = {"wicketTests"})
    public void testLastRendered_2() {
        List<Integer> data = data(3);
        IDataProvider<Integer> dataProvider = new ListDataProvider<Integer>(data);
        IQuickReuseStrategy reuseStrategy = new ItemsNavigationStrategy();
        QuickGridView<Integer> quickView = new QuickGridView<Integer>("qv", dataProvider, reuseStrategy) {
            @Override
            protected void populate(CellItem<Integer> item) {

            }

            @Override
            protected void populateEmptyItem(CellItem<Integer> item) {

            }
        };
        QuickGridView<Integer>spy=Mockito.spy(quickView);
        Item<Integer>item0=new Item<>("1",1,new Model<>(10));
        spy.simpleAddRow(item0);
        Item<Integer>item1=new Item<>("2",2,new Model<>(20));
        spy.simpleAddRow(item1);
        Component lastRenderedItem=spy._lastRenderedItem();
        Assert.assertEquals(lastRenderedItem.getId(),item1.getId());
    }


    List<Integer> data(int size) {
        List<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < size; i++) {
            list.add(i);
        }
        return list;
    }

}
