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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * a gridview that can add and remove rows in ajax cases without the need to repaint parent(adding parent to AjaxRequestTarget causes re-rendering of whole repeater)
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
        long items = (long) rows * (long) columns;
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


    @Override
    public List<Item<T>> addItemsForPage(long page) {
        long itemIndex = page * rows * columns;
        return addItemsFromIndex(itemIndex);
    }

    public RowItem<T> addRowAtStart(RowItem<T> rowItem) {
        Args.notNull(rowItem, "rowItem can't be null");
        simpleAdd(rowItem);
        if (!isAjax()) {
            return rowItem;
        }
        if (isParentAddedInAjaxRequestTarget()) {
            return rowItem;
        }
        AjaxRequestTarget target = getAjaxRequestTarget();
        String call = getRepeaterUtil().insertBefore(rowItem, _getParent());
        target.prependJavaScript(call);
        target.add(rowItem);
        return rowItem;
    }

    public QuickGridView addRow(RowItem<T> rowItem) {
        Args.notNull(rowItem, "rowItem can't be null");
        simpleAdd(rowItem);
        if (!isAjax()) {
            return this;
        }
        if (isParentAddedInAjaxRequestTarget()) {
            return this;
        }
        AjaxRequestTarget target = getAjaxRequestTarget();
        String call = getRepeaterUtil().insertAfter(rowItem, _getParent());
        target.prependJavaScript(call);
        target.add(rowItem);
        return this;
    }

    public void removeRow(RowItem<T> rowItem) {
        Args.notNull(rowItem, "rowItem can't be null");
        if (isAjax() && !isParentAddedInAjaxRequestTarget()) {
            AjaxRequestTarget target = getAjaxRequestTarget();
            String call = getRepeaterUtil().removeItem(rowItem);
            target.prependJavaScript(call);
            target.add(rowItem);
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
     * @param itemIndex from where items has to be created
     * @return list of items created
     */

    @Override
    public List<Item<T>> addItemsFromIndex(long itemIndex) {
        Iterator<? extends T> iterator = getDataProvider().iterator(itemIndex, getItemsPerRequest());
        final long startrow = itemIndex / columns;
        List<Item<T>> cells = new ArrayList<Item<T>>();
        for (long ri = startrow; iterator.hasNext(); ri++) {
            RowItem<T> rowItem = buildRowItem(newChildId(), ri);
            addRow(rowItem);
            List<Item<T>> rowsCells = addCells(rowItem, iterator);
            cells.addAll(rowsCells);
        }
        return cells;
    }

    @Override
    protected void createChildren(long page) {
        long itemIndex = page * getItemsPerRequest();
        Iterator<? extends T> iterator = getDataProvider().iterator(itemIndex, getItemsPerRequest());
        for (long ri = 0; iterator.hasNext(); ri++) {
            RowItem<T> rowItem = buildRowItem(newChildId(), ri);
            simpleAdd(rowItem);
            addCells(rowItem, iterator);
        }
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
        return new GridView.ItemsIterator(rows);
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

}
