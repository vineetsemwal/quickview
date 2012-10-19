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
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.lang.Args;
import org.apache.wicket.util.lang.Generics;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * it renders items in rows and columns in table/grid format(same as GridView).it also lets you add and remove rows in ajax cases
 * without the need to repaint parent(adding parent to AjaxRequestTarget causes re-rendering of whole repeater)
 *
 * @author Vineet Semwal
 */
public abstract class QuickGridView<T> extends QuickViewBase<T> {

    /**
     * @param id           component id
     * @param dataProvider data provider
     */
    public QuickGridView(String id, IDataProvider<T> dataProvider, ReUse reUse) {
        super(id, dataProvider, reUse);
    }

    /**
     * @param id           component id
     * @param dataProvider data provider
     */
    public QuickGridView(String id, IDataProvider<T> dataProvider) {
        super(id, dataProvider, ReUse.NOT_INITIALIZED);
    }


    public static final String COLUMNS_REPEATER_ID = "cols";
    private int columns = 1;

    public int getColumns() {
        return columns;
    }

    /**
     * Sets number of columns
     *
     * @param cols number of columns
     * @return this for chaining
     */
    public QuickGridView<T> setColumns(int cols) {
        if (cols < 1) {
            throw new IllegalArgumentException("columns can't be smaller than 1");
        }

        if (columns != cols) {
            if (isVersioned()) {
                addStateChange();
            }
            columns = cols;
            updateItemsPerPage();
        }

        return this;
    }

    protected void updateItemsPerPage() {
        int items = Integer.MAX_VALUE;

        long result = (long)rows * (long)columns;

        // overflow check
        int desiredHiBits = -((int)(result >>> 31) & 1);
        int actualHiBits = (int)(result >>> 32);

        if (desiredHiBits == actualHiBits)
        {
            items = (int)result;
        }
        setItemsPerRequest(items);
    }



    private int rows = Integer.MAX_VALUE;

    public int getRows() {
        return rows;
    }

    /**
     * Sets number of rows per page
     *
     * @param rows number of rows
     * @return this for chaining
     */
    public QuickGridView<T> setRows(int rows) {
        if (rows < 1) {
            throw new IllegalArgumentException("rows can't be set smaller than 1");
        }

        if (this.rows != rows) {
            if (isVersioned()) {
                addStateChange();
            }
            this.rows = rows;
            updateItemsPerPage();
        }

        return this;
    }

    protected final void populate(Item<T> item) {
        populate((CellItem) item);
    }

    protected abstract void populate(CellItem<T> item);

    abstract protected void populateEmptyItem(CellItem<T> item);


    public QuickGridView<T> addRowAtStart(RowItem<T> rowItem) {
        Args.notNull(rowItem, "rowItem can't be null");
        simpleAdd(rowItem);
        if (!isAjax()) {
            return this;
        }

        String call = getRepeaterUtil().insertBefore(rowItem, _getParent());
        getSynchronizer().getPrependScripts().add(call);
        getSynchronizer().add(rowItem);
        return this;
    }

    public QuickGridView addRow(RowItem<T> rowItem) {
        Args.notNull(rowItem, "rowItem can't be null");
        simpleAdd(rowItem);
        if (!isAjax()) {
            return this;
        }
        String call = getRepeaterUtil().insertAfter(rowItem, _getParent());
        Synchronizer listener = getSynchronizer();
        listener.getPrependScripts().add(call);
        listener.add(rowItem);
        return this;
    }

    /**
     *   adds rows and their corresponding cells
     *
     * @param iterator data for which rows and their corresponding cells will be added
     * @return    this
     */
    public QuickGridView<T> addRows(Iterator<? extends T> iterator) {
        Iterator<RowItem<T>> rows = buildRows(iterator);
        while (rows.hasNext()) {
            addRow(rows.next());
        }
        return this;
    }

    public QuickGridView<T> addRowsAtStart(Iterator<? extends T> iterator) {
        Iterator<RowItem<T>> rows = buildRows(iterator);
        while (rows.hasNext()) {
            addRowAtStart(rows.next());
        }
        return this;
    }

    public void removeRow(RowItem<T> rowItem) {
        Args.notNull(rowItem, "rowItem can't be null");
        if (isAjax()) {
            String call = getRepeaterUtil().removeItem(rowItem);
            getSynchronizer().getPrependScripts().add(call);
            getSynchronizer().add(rowItem);
        }
        simpleRemove(rowItem);
    }

    public void removeRow(int row) {
        removeRow(getRow(row));
    }

    /**
     * retrieves RowItem corresponding to the  passed row argument
     *
     * @param row starts from 0
     * @return rowitem retrieved
     */
    public RowItem<T> getRow(int row) {
        if (row < 0) {
            throw new IllegalArgumentException("row <0");
        }
        int last = size() - 1;
        if (row > last) {
            throw new IndexOutOfBoundsException("row >size");
        }
        RowItem<T> rowItem = (RowItem<T>) get(row);
        return rowItem;
    }

    public RowItem<T> getLastRowItem() {
        int size = size();
        if (size < 1) {
            return null;
        }
        RowItem<T> rowItem = getRow(size - 1);
        return rowItem;
    }


    /**
     * reuse items if the models are equal ,iterator of RowItems is returned
     */
    protected Iterator<Item<T>> reuseItemsForCurrentPage(final int currentPage) {
        final long items = currentPage * getItemsPerRequest();
        Iterator<? extends T> objects = getDataProvider().iterator(getRepeaterUtil().safeLongToInt(items), getItemsPerRequest());
        Iterator<RowItem<T>> newRowsIterator = (Iterator) buildItems(0, objects);
        Iterator oldCells = cells();
        Iterator newCells = new ItemsIterator((Iterator) newRowsIterator);
        Iterator reuseItems = getRepeaterUtil().reuseItemsIfModelsEqual(oldCells, newCells);
        return (Iterator) buildRows(0, reuseItems);
    }

    /**
     * @param index    cellindex from where new cell items should be added
     * @param iterator data
     * @return iterator of RowItem which are created with their corresponding cells attached to them
     */
    @Override
    protected Iterator<Item<T>> buildItems(final int index, Iterator<? extends T> iterator) {
        Iterator<CellItem<T>> cells = buildCells(index, iterator);
        long rowIndex = index / columns;
        return (Iterator) buildRows(rowIndex, cells);
    }

    /**
     * @param iterator data
     * @return iterator of RowItem which are created with their corresponding cells attached to them
     */
    public Iterator<RowItem<T>> buildRows(Iterator<? extends T> iterator) {
        int cellindex = 0;
        if (ReUse.ALL == getReuse()) {
            cellindex = gridSize();
        }
        return (Iterator) buildItems(cellindex, iterator);
    }


    protected Iterator<RowItem<T>> buildRows(final long rowIndex, Iterator<CellItem<T>> iterator) {
        List<RowItem<T>> rowItems = new ArrayList<RowItem<T>>();
        for (long row = rowIndex; iterator.hasNext(); row++) {
            RowItem<T> rowItem = buildRowItem(newChildId(), row);
            rowItems.add(rowItem);
            for (long i = 0; i < columns; i++) {
                if (iterator.hasNext()) {
                    CellItem<T> cell = iterator.next();
                    rowItem.getRepeater().add(cell);
                } else {
                    CellItem<T> item = buildEmptyCellItem();
                    rowItem.getRepeater().add(item);
                }
            }

        }
        return rowItems.iterator();
    }


    protected Iterator<CellItem<T>> buildCells(final long index, Iterator<? extends T> iterator) {
        List<CellItem<T>> cells = new ArrayList<CellItem<T>>();
        for (long i = index; iterator.hasNext(); i++) {
            T object = iterator.next();
            CellItem<T> cell = buildCellItem(newChildId(), i, object);
            cells.add(cell);
        }
        return cells.iterator();
    }

    public int gridSize() {
        int rows = size();
        long grid = rows * columns;
        return getRepeaterUtil().safeLongToInt(grid);
    }


    /**
     * adds {@link CellItem} to row's repeater ,if the iterator iterates less than the columns then the left cells are
     * filled as empty cells
     */
    public List<Item<T>> addCells(RowItem<T> rowItem, Iterator<? extends T> iterator) {
        List<Item<T>> cells = new ArrayList<Item<T>>();
        for (int i = 0; i < columns; i++) {
            if (iterator.hasNext()) {
                T next = iterator.next();
                CellItem<T> item = buildCellItem(next);
                rowItem.getRepeater().add(item);
                cells.add(item);
            } else {
                CellItem<T> item = buildEmptyCellItem();
                rowItem.getRepeater().add(item);
                cells.add(item);
            }
        }
        return cells;
    }

    /**
     * adds {@link CellItem} to row's repeater ,if the cells passed are less than columns then
     * the left cells int the row are filled as empty cells
     *
     * @param rowItem rowItem
     * @param cell
     * @return this
     */
    public QuickGridView addCells(RowItem<T> rowItem, CellItem<T>... cell) {
        for (int i = 0; i < columns; i++) {
            if (i < cell.length) {
                rowItem.getRepeater().add(cell[i]);
            } else {
                CellItem<T> item = buildEmptyCellItem();
                rowItem.getRepeater().add(item);
            }
        }
        return this;
    }


    /**
     * new rowItem
     *
     * @param id    childid
     * @param index index
     * @return RowItem
     */
    protected RowItem<T> newRowItem(String id, long index) {
        RowItem<T> item = new RowItem<T>(id, getRepeaterUtil().safeLongToInt(index), new Model());
        return item;
    }

    /**
     * build cell item means the new cellitem is created and then populated with  populate(cell)
     *
     * @param id     cell's  id
     * @param index  cell's index
     * @param object model object set to the cellitem
     * @return CellItem
     */
    public CellItem<T> buildCellItem(String id, long index, T object) {
        CellItem<T> cell = newCellItem(id, getRepeaterUtil().safeLongToInt(index), object);
        populate(cell);
        return cell;
    }

    /**
     * builds cell item by creating new cellitem and then populating by populate(cell)
     *
     * @return CellItem
     */
    public CellItem<T> buildEmptyCellItem() {
        return buildEmptyCellItem(newChildId(), getChildId());
    }

    /**
     * builds cell item by creating new cellitem and then populating by populateEmptyItem(cell)
     *
     * @return CellItem
     */
    public CellItem<T> buildEmptyCellItem(String id, long index) {
        CellItem<T> cell = newEmptyCellItem(id, getRepeaterUtil().safeLongToInt(index));
        populateEmptyItem(cell);
        return cell;
    }

    /**
     * builds cell item by creating new cellitem and then populating by populateEmptyItem(cell)
     *
     * @return CellItem
     */
    public CellItem<T> buildCellItem(T object) {
        return buildCellItem(newChildId(), getChildId(), object);
    }

    public RowItem buildRowItem(String id, long index) {
        RowItem<T> item = newRowItem(id, index);
        RepeatingView rowView = new RepeatingView(COLUMNS_REPEATER_ID);
        item.add(rowView);
        return item;
    }

    /**
     * build  row item
     *
     * @return new row item
     */
    public RowItem buildRowItem() {
        return buildRowItem(newChildId(), size());
    }

    public CellItem<T> findFirstEmptyCell() {
        CellItem<T> emptyCell = null;
        Iterator<CellItem> iterator = getLastRowItem().cellItemItems();
        while (iterator.hasNext()) {
            CellItem<T> cellItem = iterator.next();
            if (cellItem.isEmpty()) {
                emptyCell = cellItem;
                break;
            }
        }
        return emptyCell;
    }

    public Iterator<RowItem<T>> rows() {
        return (Iterator) this.iterator();
    }


    public Iterator<CellItem<T>> cells() {
        Iterator<MarkupContainer> rows = (Iterator) rows();
        return new ItemsIterator(rows);
    }

    @Override
    public Iterator<Component> itemsIterator() {
        return (Iterator) cells();
    }

    /**
     * @param id     cell's id
     * @param index  cell's index
     * @param object cell's modelobject
     * @return cellitem
     */
    public CellItem<T> newCellItem(String id, long index, T object) {
        return new CellItem<T>(id, getRepeaterUtil().safeLongToInt(index), getDataProvider().model(object));
    }

    public CellItem<T> newEmptyCellItem(String id, long index) {
        return new CellItem<T>(id, getRepeaterUtil().safeLongToInt(index), new Model(), true);
    }


    /*
    *  a rowitem represents one row
    *
    *   @author Vineet Semwal
    *
    */
    public static class RowItem<T> extends Item<T> {
        public RowItem(String id, int index, IModel<T> model) {
            super(id, index, model);
            setOutputMarkupId(true);
            setMarkupId(id);
        }

        /**
         * cells repeater
         *
         * @return repeater
         */
        public RepeatingView getRepeater() {
            return (RepeatingView) get(COLUMNS_REPEATER_ID);
        }

        /**
         * @return cells iterator
         */
        public Iterator cellItemItems() {
            return getRepeater().iterator();
        }

        /**
         * retrieves item corresponding to a particular column in a row
         *
         * @param column
         * @return CellItem
         */
        public CellItem<T> getCellItem(int column) {
            return (CellItem) getRepeater().get(column);
        }

    }

    /**
     * a cell item represents one cell of the QuickGridView
     *
     * @param <T>
     * @author Vineet Semwal
     */
    public static class CellItem<T> extends Item<T> {
        public CellItem(String id, int index, IModel<T> model) {
            this(id, index, model, false);
        }

        public CellItem(String id, int index, IModel<T> model, boolean empty) {
            super(id, index, model);
            setOutputMarkupId(true);
            setMarkupId(id);
            this.empty = empty;
        }

        public RowItem<T> getRowItem() {
            if (getParent() == null) {
                throw new RuntimeException("cellItem not yet attached to parent");
            }
            return (RowItem<T>) getParent().getParent();
        }


        private boolean empty = false;

        public boolean isEmpty() {
            return empty;
        }

    }


    /**
     * Iterator that iterates over all items in the cells
     * (borrowed  from wicket since in 1.5.8 it's not public,it will be removed for 1.5.9 release since there it's public)
     *
     * @author igor
     * @param <T>
     */
    private static class ItemsIterator<T> implements Iterator<Item<T>>
    {
        private final Iterator<MarkupContainer> rows;
        private Iterator<Item<T>> cells;

        private Item<T> next;

        /**
         * @param rows
         *            iterator over child row views
         */
        public ItemsIterator(Iterator<MarkupContainer> rows)
        {
            this.rows = Args.notNull(rows, "rows");
            findNext();
        }

        /**
         * @see java.util.Iterator#remove()
         */
        @Override
        public void remove()
        {
            throw new UnsupportedOperationException();
        }

        /**
         * @see java.util.Iterator#hasNext()
         */
        @Override
        public boolean hasNext()
        {
            return next != null;
        }

        /**
         * @see java.util.Iterator#next()
         */
        @Override
        public Item<T> next()
        {
            Item<T> item = next;
            findNext();
            return item;
        }

        private void findNext()
        {
            next = null;

            if (cells != null && cells.hasNext())
            {
                next = cells.next();
            }
            else
            {
                while (rows.hasNext())
                {
                    MarkupContainer row = rows.next();

                    final Iterator<? extends Component> rawCells;
                    rawCells = ((MarkupContainer)row.iterator().next()).iterator();
                    cells = Generics.iterator(rawCells);
                    if (cells.hasNext())
                    {
                        next = cells.next();
                        break;
                    }
                }
            }
        }

    }


}
