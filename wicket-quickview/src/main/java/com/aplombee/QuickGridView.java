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
import org.apache.wicket.markup.repeater.IItemFactory;
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
    public QuickGridView(String id, IDataProvider<T> dataProvider, IQuickReuseStrategy reuseStrategy) {
        super(id, dataProvider, reuseStrategy);
    }

    /**
     * @param id           component id
     * @param dataProvider data provider
     */
    public QuickGridView(String id, IDataProvider<T> dataProvider) {
        super(id, dataProvider, new DefaultQuickReuseStrategy());
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
       int prevent= (items > Integer.MAX_VALUE) ? Integer.MAX_VALUE : (int)items;
        setItemsPerRequest(prevent);
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

    @Override
    public IItemFactory<T> factory() {
        return new IItemFactory<T>() {
            @Override
            public Item<T> newItem(int index, IModel<T> model) {
                return buildCellItem(index, model);
            }
        } ;
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

        String call = getRepeaterUtil().prepend(rowItem, _getParent());
        getSynchronizer().getPrependScripts().add(call);
        getSynchronizer().add(rowItem);
        return this;
    }

    public QuickGridView<T> addRow(RowItem<T> rowItem) {
        Args.notNull(rowItem, "rowItem can't be null");
        simpleAdd(rowItem);
        if (!isAjax()) {
            return this;
        }
        String call = getRepeaterUtil().append(rowItem, _getParent());
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
            String call = getRepeaterUtil().removeItem(rowItem,_getParent());
            getSynchronizer().getPrependScripts().add(call);
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
     * @param index    cellindex from where new cell items should be added
     * @param iterator data
     * @return iterator of RowItem which are created with their corresponding cells attached to them
     */

   @Override
    protected Iterator<Item<T>> buildItems(final long index, Iterator<? extends T> iterator) {
        Iterator<CellItem<T>> cells = buildCells(index, iterator);
        long rowIndex = index / columns;
        return (Iterator) buildRows(rowIndex, cells);
    }

    /**
     * @param iterator data
     * @return iterator of RowItem which are created with their corresponding cells attached to them
     */
    public Iterator<RowItem<T>> buildRows(Iterator<? extends T> iterator) {
        long cellindex = gridSize();
        return (Iterator) buildItems(cellindex, iterator);
    }

    @Override
    public void createChildren(Iterator<Item<T>>itemIterator){
           Iterator<RowItem<T>>rows= buildRows(0,(Iterator)itemIterator);
            while (rows.hasNext()){
                simpleAdd(rows.next()) ;
            }
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
                    CellItem<T> cell = buildEmptyCellItem(newChildId(),i);
                    rowItem.getRepeater().add(cell);
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



    @Override
    public List<Item<T>> addItemsForPage(long page) {
        long startIndex=page*getItemsPerRequest();
        Iterator<IModel<T>>newModels=newModels(startIndex, getItemsPerRequest());
        Iterator<CellItem<T>> newIterator= (Iterator)getReuseStrategy().addItems((int)startIndex, factory(), newModels);
        Iterator<RowItem<T>>rows= buildRows(startIndex/getRows(),newIterator);
         List<Item<T>>items=new ArrayList<Item<T>>();

        while (rows.hasNext()){
            RowItem<T>rowItem=rows.next();
            addRow(rowItem);
            items.add(rowItem);
        }

        return items;
    }

    public long gridSize() {
        int rows = size();
        long grid = rows * columns;
        return grid;
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
        return buildCellItem(id,index,getDataProvider().model(object));
    }


    protected CellItem<T> buildCellItem(String id, long index, IModel<T>model) {
        CellItem<T> cell = newCellItem(id, getRepeaterUtil().safeLongToInt(index), model);
        populate(cell);
        return cell;
    }

    /**
     * builds cell item by creating new cellitem and then populating by populateEmptyItem(cell)
     *
     * @return CellItem
     */
    public CellItem<T> buildEmptyCellItem( long index) {
      return buildEmptyCellItem(newChildId(),index);
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
    public CellItem<T> buildCellItem(long index,T object) {
        return buildCellItem(newChildId(), index, object);
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

    @Override
    public Iterator<Component> itemsIterator() {
        return (Iterator) cells();
    }


    protected CellItem<T> newCellItem(String id, long index, IModel<T> model) {
        return new CellItem<T>(id, getRepeaterUtil().safeLongToInt(index),model);
    }


    public CellItem<T> buildCellItem(long index,IModel<T>model) {
        return buildCellItem(newChildId(),index,model);
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
