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

import com.aplombee.navigator.AjaxItemsNavigator;
import com.aplombee.navigator.ItemsNavigatorBase;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.IMarkupResourceStreamProvider;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.IMarkupSourcingStrategy;
import org.apache.wicket.markup.html.panel.PanelMarkupSourcingStrategy;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.mock.MockApplication;
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.StringResourceStream;
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
        Assert.assertEquals(grid.getColumns(),cols);
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
        final int childId=845;
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
            public int getChildId() {
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
        final int childId=845;
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
            public int getChildId() {
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
        final int childId=845;
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
            public int getChildId() {
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
        final int childId=89;

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
            public int getChildId() {
                return childId;
            }
        };
        QuickGridView spy=Mockito.spy(grid);
         final int object=908;
        QuickGridView.CellItem actual=spy.buildCellItem(object);
        Mockito.verify(spy,Mockito.times(1)).newCellItem(String.valueOf(childId),childId,object);
        Mockito.verify(spy,Mockito.times(1)).populate(cell);
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
     * iterator runs 2 times  ,page=1 ,rows=1,columns=2
     **/
     @Test(groups = {"wicketTests"})
    public void createChildren_1(){
        final List<Item<Integer>>cells=Mockito.mock(List.class);
        IDataProvider provider = Mockito.mock(IDataProvider.class);
        final QuickGridView.RowItem rowItem=Mockito.mock(QuickGridView.RowItem.class);
        final long childId=56;
        Iterator iterator=Mockito.mock(Iterator.class);
        Mockito.when(iterator.hasNext()).thenReturn(true).thenReturn(true).thenReturn(false);
        QuickGridView<Integer> grid=new QuickGridView<Integer>("grid",provider) {

            @Override
            protected void populate(CellItem<Integer> item) {
            }

            @Override
            protected void populateEmptyItem(CellItem<Integer> item) {
            }

            @Override
            public MarkupContainer simpleAdd(Component... c) {
                return this;
            }

            @Override
            public List<Item<Integer>> addCells(RowItem<Integer> rowItem, Iterator<? extends Integer> iterator) {
                 iterator.next();
                return cells;
            }

            @Override
            public String newChildId() {
                return String.valueOf(childId);
            }

            @Override
            public RowItem buildRowItem(String id, long index) {
                return   rowItem;
            }

        } ;


        QuickGridView spy=Mockito.spy(grid);
        spy.setColumns(2) ;
        spy.setRows(1);
        final int itemsPerRequest=spy.getItemsPerRequest();
        int index=itemsPerRequest*1;// page*itemsperequest
        Mockito.when(provider.iterator(index,itemsPerRequest)).thenReturn(iterator);
        spy.createChildren(1);
        final int times=2 ;
         Mockito.verify(spy,Mockito.times(times)).simpleAdd(rowItem);
        Mockito.verify(spy,Mockito.times(times)).addCells(rowItem,iterator);
        for(long i=0;i<2;i++){
            Mockito.verify(spy,Mockito.times(1)).buildRowItem(String.valueOf(childId),i);
        }
    }

    //iterator runs 2 times  ,page=0 ,rows=2,columns=1
    @Test(groups = {"wicketTests"})
    public void createChildren_2(){
        final List<Item<Integer>>cells=Mockito.mock(List.class);
        IDataProvider provider = Mockito.mock(IDataProvider.class);
        final QuickGridView.RowItem rowItem=Mockito.mock(QuickGridView.RowItem.class);
        final long childId=56;
        Iterator iterator=Mockito.mock(Iterator.class);
        Mockito.when(iterator.hasNext()).thenReturn(true).thenReturn(true).thenReturn(false);
        QuickGridView<Integer> grid=new QuickGridView<Integer>("grid",provider) {

            @Override
            protected void populate(CellItem<Integer> item) {
            }

            @Override
            protected void populateEmptyItem(CellItem<Integer> item) {
            }

            @Override
            public MarkupContainer simpleAdd(Component... c) {
                return this;
            }

            @Override
            public List<Item<Integer>> addCells(RowItem<Integer> rowItem, Iterator<? extends Integer> iterator) {
                iterator.next();  //consume next element
                return cells;
            }

            @Override
            public String newChildId() {
                return String.valueOf(childId);
            }

            @Override
            public RowItem buildRowItem(String id, long index) {
                return   rowItem;
            }

        } ;


        QuickGridView spy=Mockito.spy(grid);
        spy.setColumns(1) ;
        spy.setRows(2);
        final int itemsPerRequest=spy.getItemsPerRequest();
        int index=itemsPerRequest*0;// page*itemsperequest
        Mockito.when(provider.iterator(index,itemsPerRequest)).thenReturn(iterator);
        spy.createChildren(0);
        final int times=2 ;
        Mockito.verify(spy,Mockito.times(times)).simpleAdd(rowItem);
        Mockito.verify(spy,Mockito.times(times)).addCells(rowItem,iterator);
        for(long i=0;i<2;i++){
            Mockito.verify(spy,Mockito.times(1)).buildRowItem(String.valueOf(childId),i);
        }
    }

    @Test(groups = {"wicketTests"})
    public void addItemsFromIndex_1(){
        final List<Item<Integer>>cells=new ArrayList<Item<Integer>>();
        QuickGridView.CellItem cell1=Mockito.mock(QuickGridView.CellItem.class);
        cells.add(cell1);

        IDataProvider provider = Mockito.mock(IDataProvider.class);
        final QuickGridView.RowItem rowItem=Mockito.mock(QuickGridView.RowItem.class);
        final long childId=56;
        Iterator iterator=Mockito.mock(Iterator.class);
        Mockito.when(iterator.hasNext()).thenReturn(true).thenReturn(true).thenReturn(false);
        final int rows=1,columns=2;
        QuickGridView<Integer> grid=new QuickGridView<Integer>("grid",provider) {

            @Override
            protected void populate(CellItem<Integer> item) {
            }

            @Override
            protected void populateEmptyItem(CellItem<Integer> item) {
            }

            @Override
            public QuickGridView addRow(RowItem row) {
                return this;
            }

            @Override
            public List<Item<Integer>> addCells(RowItem<Integer> rowItem, Iterator<? extends Integer> iterator) {
                iterator.next();
                return cells;
            }

            @Override
            public String newChildId() {
                return String.valueOf(childId);
            }

            @Override
            public RowItem buildRowItem(String id, long index) {
                return   rowItem;
            }

            @Override
            public int getRows() {
                return  rows;
            }

            @Override
            public int getColumns() {
                return columns;
            }
        } ;

        QuickGridView spy=Mockito.spy(grid);

        spy.setColumns(columns) ;
        spy.setRows(rows);
        final int itemsPerRequest=columns*rows;
        int index=itemsPerRequest*1;// page*itemsperequest
        Mockito.when(provider.iterator(index,itemsPerRequest)).thenReturn(iterator);
        List<Item>actualCells=spy.addItemsFromIndex(index);
        final int times=2 ;
        Mockito.verify(spy,Mockito.times(times)).addRow(rowItem);
        Mockito.verify(spy,Mockito.times(times)).addCells(rowItem,iterator);

        Mockito.verify(spy,Mockito.times(1)).buildRowItem(String.valueOf(childId),1);
        Mockito.verify(spy,Mockito.times(1)).buildRowItem(String.valueOf(childId),2);
        Assert.assertEquals(actualCells.size(),2);
        Assert.assertTrue(actualCells.contains(cell1));

    }


    @Test(groups = {"wicketTests"})
    public void addItemsFromIndex_2(){
        final List<Item<Integer>>cells=new ArrayList<Item<Integer>>();
        QuickGridView.CellItem cell1=Mockito.mock(QuickGridView.CellItem.class);
        cells.add(cell1);

        IDataProvider provider = Mockito.mock(IDataProvider.class);
        final QuickGridView.RowItem rowItem=Mockito.mock(QuickGridView.RowItem.class);
        final long childId=56;
        Iterator iterator=Mockito.mock(Iterator.class);
        Mockito.when(iterator.hasNext()).thenReturn(true).thenReturn(true).thenReturn(false);
        final int rows=2,columns=2;
        QuickGridView<Integer> grid=new QuickGridView<Integer>("grid",provider) {

            @Override
            protected void populate(CellItem<Integer> item) {
            }

            @Override
            protected void populateEmptyItem(CellItem<Integer> item) {
            }

            @Override
            public QuickGridView addRow(RowItem row) {
                return this;
            }

            @Override
            public List<Item<Integer>> addCells(RowItem<Integer> rowItem, Iterator<? extends Integer> iterator) {
                iterator.next();
                return cells;
            }

            @Override
            public String newChildId() {
                return String.valueOf(childId);
            }

            @Override
            public RowItem buildRowItem(String id, long index) {
                return   rowItem;
            }


        } ;

        QuickGridView spy=Mockito.spy(grid);

        spy.setColumns(columns) ;
        spy.setRows(rows);
        final int itemsPerRequest=columns*rows;
        int index=itemsPerRequest*2;// page*itemsperequest
        Mockito.when(provider.iterator(index,itemsPerRequest)).thenReturn(iterator);
        List<Item>actualCells=spy.addItemsFromIndex(index);
        final int times=2 ;
        Mockito.verify(spy,Mockito.times(times)).addRow(rowItem);
        Mockito.verify(spy,Mockito.times(times)).addCells(rowItem,iterator);

        Mockito.verify(spy,Mockito.times(1)).buildRowItem(String.valueOf(childId),4);
        Mockito.verify(spy,Mockito.times(1)).buildRowItem(String.valueOf(childId),5);
        Assert.assertEquals(actualCells.size(),2);
        Assert.assertTrue(actualCells.contains(cell1));

    }


    @Test(groups = {"wicketTests"})
    public void addItemsForPage_1(){
        final List<Item<Integer>>cells=Mockito.mock(List.class);
        IDataProvider provider = Mockito.mock(IDataProvider.class);
        final QuickGridView.RowItem rowItem=Mockito.mock(QuickGridView.RowItem.class);
        final long childId=56;
        Iterator iterator=Mockito.mock(Iterator.class);
        Mockito.when(iterator.hasNext()).thenReturn(true).thenReturn(true).thenReturn(false);
        final int rows=1,columns=2;
        QuickGridView<Integer> grid=new QuickGridView<Integer>("grid",provider) {

            @Override
            protected void populate(CellItem<Integer> item) {
            }

            @Override
            protected void populateEmptyItem(CellItem<Integer> item) {
            }

            @Override
            public List<Item<Integer>> addItemsFromIndex(int itemIndex) {
                 return  cells;
            }
        } ;
      QuickGridView spy=Mockito.spy(grid);
        spy.setRows(1);
        spy.setColumns(2);
        spy.addItemsForPage(1);
        final int itemIndex=1*2*1;
        Mockito.verify(spy,Mockito.times(1)).addItemsFromIndex(itemIndex);
    }


    @Test(groups = {"wicketTests"})
    public void addItemsForPage_2(){
        final List<Item<Integer>>cells=Mockito.mock(List.class);
        IDataProvider provider = Mockito.mock(IDataProvider.class);
        final QuickGridView.RowItem rowItem=Mockito.mock(QuickGridView.RowItem.class);
        final long childId=56;
        Iterator iterator=Mockito.mock(Iterator.class);
        Mockito.when(iterator.hasNext()).thenReturn(true).thenReturn(true).thenReturn(false);
        final int rows=1,columns=2;
        QuickGridView<Integer> grid=new QuickGridView<Integer>("grid",provider) {

            @Override
            protected void populate(CellItem<Integer> item) {
            }

            @Override
            protected void populateEmptyItem(CellItem<Integer> item) {
            }

            @Override
            public List<Item<Integer>> addItemsFromIndex(int itemIndex) {
                return  cells;
            }
        } ;
        QuickGridView spy=Mockito.spy(grid);
        spy.setRows(1);
        spy.setColumns(2);
        spy.addItemsForPage(2);
        final int itemIndex=1*2*2;
        Mockito.verify(spy,Mockito.times(1)).addItemsFromIndex(itemIndex);
    }



    /**
     *    ajax=true  ,parrentaddedtotarget=false
     */
    @Test(groups = {"wicketTests"})
    public void addRow_1(){
        IDataProvider provider = new ListDataProvider(data(4));
        QuickMockApplication app=new QuickMockApplication();
        WicketTester tester=new WicketTester(app);
        final QuickGridView<Integer> grid=new QuickGridView<Integer>("grid",provider) {
            @Override
            protected void populate(CellItem<Integer> item) {
            }

            @Override
            protected void populateEmptyItem(CellItem<Integer> item) {
            }

        } ;
        grid.setReuse(ReUse.ITEMSNAVIGATION);

       final QuickViewParent parent=new QuickViewParent("parent");
        parent.setOutputMarkupPlaceholderTag(true);
        parent.add(grid);
        grid.setColumns(2);
        final QuickGridView.RowItem rowItem=grid.buildRowItem();
       final AjaxLink link=new AjaxLink("link") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                grid.addRow(rowItem);

            }
        };
        TestQuickGridViewContainer container=new TestQuickGridViewContainer("panel") {
            @Override
            public QuickViewParent newParent() {
                return parent;
            }

            @Override
            public AbstractLink newLink() {
                return  link;
            }
        } ;
       
            tester.startComponentInPage(container);
          tester.executeAjaxEvent(link,"onclick");
          tester.assertComponentOnAjaxResponse(rowItem);
          QuickGridView.RowItem expected=grid.getRow(2);
          Assert.assertTrue(expected==rowItem);
        AjaxRequestTarget target=app.getLastAjaxRequestTarget();
        String actualPrependedScripts=grid.getRepeaterUtil().prependedScripts(target.toString());
        String expectedPrependedScript1=RepeaterUtil.get().insertAfter(rowItem,parent) ;
        List<String>expectedPrependedScripts=new ArrayList<String>() ;
        expectedPrependedScripts.add(expectedPrependedScript1);
        Assert.assertEquals(actualPrependedScripts,expectedPrependedScripts.toString());

    }


    /**
     *    ajax=true  ,parrentaddedtotarget=true
     */
    @Test(groups = {"wicketTests"})
    public void addRow_2(){
        final List <Integer>data=data(4);
        IDataProvider provider = new ListDataProvider(data);
        QuickMockApplication app=new QuickMockApplication();
        WicketTester tester=new WicketTester(app);
        final QuickGridView<Integer> grid=new QuickGridView<Integer>("grid",provider) {
            @Override
            protected void populate(CellItem<Integer> item) {
            }

            @Override
            protected void populateEmptyItem(CellItem<Integer> item) {
            }

        } ;
        grid.setReuse(ReUse.ITEMSNAVIGATION);

        final QuickViewParent parent=new QuickViewParent("parent");
        parent.setOutputMarkupPlaceholderTag(true);
        parent.add(grid);
        grid.setColumns(2);

        final QuickGridView.RowItem rowItem=grid.buildRowItem();
        final AjaxLink link=new AjaxLink("link") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                grid.addRow(rowItem);
                data.add(78);
                data.add(69);
                target.add(parent);
            }
        };
        TestQuickGridViewContainer container=new TestQuickGridViewContainer("panel") {
            @Override
            public QuickViewParent newParent() {
                return parent;
            }

            @Override
            public AbstractLink newLink() {
                return  link;
            }
        } ;
        tester.startComponentInPage(container);
        tester.executeAjaxEvent(link, "onclick");
       AjaxRequestTarget target=app.getLastAjaxRequestTarget();
        Assert.assertTrue(target.getComponents().contains(parent));
        String actualPrependedScripts=grid.getRepeaterUtil().prependedScripts(target.toString());
       List<String>expectedPrependedScripts=new ArrayList<String>() ;
       Assert.assertEquals(actualPrependedScripts, expectedPrependedScripts.toString());

    }


    /**
     *    ajax=false
     */
    @Test(groups = {"wicketTests"})
    public void addRow_3(){
        final List <Integer>data=data(4);
        IDataProvider provider = new ListDataProvider(data);
        QuickMockApplication app=new QuickMockApplication();
        WicketTester tester=new WicketTester(app);
        final QuickGridView<Integer> grid=new QuickGridView<Integer>("grid",provider) {
            @Override
            protected void populate(CellItem<Integer> item) {
            }

            @Override
            protected void populateEmptyItem(CellItem<Integer> item) {
            }

        } ;
        grid.setReuse(ReUse.ITEMSNAVIGATION);

        final QuickViewParent parent=new QuickViewParent("parent");
        parent.setOutputMarkupPlaceholderTag(true);
        parent.add(grid);
        grid.setColumns(2);

        final QuickGridView.RowItem rowItem=grid.buildRowItem();
        final Link link=new Link("link") {
            @Override
            public void onClick() {
                grid.addRow(rowItem);
                data.add(78);
                data.add(69);
            }
        };

        TestQuickGridViewContainer container=new TestQuickGridViewContainer("panel") {
            @Override
            public QuickViewParent newParent() {
                return parent;
            }

            @Override
            public AbstractLink newLink() {
                return  link;
            }
        } ;
        tester.startComponentInPage(container);
       tester.clickLink(link);
       Iterator<QuickGridView.RowItem<Integer>> iterator= grid.rows();
        int actualSize=0;
        while (iterator.hasNext()){
          iterator.next();
          actualSize++;
        }
       Assert.assertEquals(actualSize,3);

    }



    /**
     *    ajax=true  ,parrentaddedtotarget=false
     */
    @Test(groups = {"wicketTests"})
    public void addRowAtStart_1(){
        IDataProvider provider = new ListDataProvider(data(4));
        QuickMockApplication app=new QuickMockApplication();
        WicketTester tester=new WicketTester(app);
        final QuickGridView<Integer> grid=new QuickGridView<Integer>("grid",provider) {
            @Override
            protected void populate(CellItem<Integer> item) {
            }

            @Override
            protected void populateEmptyItem(CellItem<Integer> item) {
            }

        } ;
        grid.setReuse(ReUse.ITEMSNAVIGATION);

        final QuickViewParent parent=new QuickViewParent("parent");
        parent.setOutputMarkupPlaceholderTag(true);
        parent.add(grid);
        grid.setColumns(2);
        final QuickGridView.RowItem rowItem=grid.buildRowItem();
        final AjaxLink link=new AjaxLink("link") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                grid.addRowAtStart(rowItem);

            }
        };
        TestQuickGridViewContainer container=new TestQuickGridViewContainer("panel") {
            @Override
            public QuickViewParent newParent() {
                return parent;
            }

            @Override
            public AbstractLink newLink() {
                return  link;
            }
        } ;

        tester.startComponentInPage(container);
        tester.executeAjaxEvent(link,"onclick");
        tester.assertComponentOnAjaxResponse(rowItem);
        QuickGridView.RowItem expected=grid.getRow(2);
        Assert.assertTrue(expected==rowItem);
        AjaxRequestTarget target=app.getLastAjaxRequestTarget();
        String actualPrependedScripts=grid.getRepeaterUtil().prependedScripts(target.toString());
        String expectedPrependedScript1=RepeaterUtil.get().insertBefore(rowItem, parent) ;
        List<String>expectedPrependedScripts=new ArrayList<String>() ;
        expectedPrependedScripts.add(expectedPrependedScript1);
        Assert.assertEquals(actualPrependedScripts,expectedPrependedScripts.toString());

    }




    /**
     *    ajax=true  ,parrentaddedtotarget=true
     */
    @Test(groups = {"wicketTests"})
    public void addRowAtStart_2(){
        final List <Integer>data=data(4);
        IDataProvider provider = new ListDataProvider(data);
        QuickMockApplication app=new QuickMockApplication();
        WicketTester tester=new WicketTester(app);
        final QuickGridView<Integer> grid=new QuickGridView<Integer>("grid",provider) {
            @Override
            protected void populate(CellItem<Integer> item) {
            }

            @Override
            protected void populateEmptyItem(CellItem<Integer> item) {
            }

        } ;
        grid.setReuse(ReUse.ITEMSNAVIGATION);

        final QuickViewParent parent=new QuickViewParent("parent");
        parent.setOutputMarkupPlaceholderTag(true);
        parent.add(grid);
        grid.setColumns(2);

        final QuickGridView.RowItem rowItem=grid.buildRowItem();
        final AjaxLink link=new AjaxLink("link") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                grid.addRowAtStart(rowItem);
                data.add(78);
                data.add(69);
                target.add(parent);
            }
        };
        TestQuickGridViewContainer container=new TestQuickGridViewContainer("panel") {
            @Override
            public QuickViewParent newParent() {
                return parent;
            }

            @Override
            public AbstractLink newLink() {
                return  link;
            }
        } ;
        tester.startComponentInPage(container);
        tester.executeAjaxEvent(link, "onclick");
        AjaxRequestTarget target=app.getLastAjaxRequestTarget();
        Assert.assertTrue(target.getComponents().contains(parent));
        String actualPrependedScripts=grid.getRepeaterUtil().prependedScripts(target.toString());
        List<String>expectedPrependedScripts=new ArrayList<String>() ;
        Assert.assertEquals(actualPrependedScripts, expectedPrependedScripts.toString());

    }



    /**
     *    ajax=true  ,parrentaddedtotarget=false
     */
    @Test(groups = {"wicketTests"})
    public void removeRow_1(){
        IDataProvider provider = new ListDataProvider(data(4));
        QuickMockApplication app=new QuickMockApplication();
        WicketTester tester=new WicketTester(app);
        final QuickGridView<Integer> grid=new QuickGridView<Integer>("grid",provider) {
            @Override
            protected void populate(CellItem<Integer> item) {
            }

            @Override
            protected void populateEmptyItem(CellItem<Integer> item) {
            }

        } ;
        grid.setReuse(ReUse.ITEMSNAVIGATION);

        final QuickViewParent parent=new QuickViewParent("parent");
        parent.setOutputMarkupPlaceholderTag(true);
        parent.add(grid);
        //grid.setRows(2);
        grid.setColumns(2);


        final AjaxLink link=new AjaxLink("link") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                grid.removeRow(0);
            }
        };
        TestQuickGridViewContainer container=new TestQuickGridViewContainer("panel") {
            @Override
            public QuickViewParent newParent() {
                return parent;
            }

            @Override
            public AbstractLink newLink() {
                return  link;
            }
        } ;

        tester.startComponentInPage(container);
        final QuickGridView.RowItem rowItem=grid.getRow(0);
        tester.executeAjaxEvent(link, "onclick");
        Iterator<QuickGridView.RowItem<Integer>>iterator=grid.rows();
        int rows=0;
        while (iterator.hasNext()){
            iterator.next();
            rows++;
        }
        Assert.assertEquals(rows,1);
        AjaxRequestTarget target=app.getLastAjaxRequestTarget();
        Assert.assertTrue(target.getComponents().contains(rowItem));
        String actualPrependedScripts=grid.getRepeaterUtil().prependedScripts(target.toString());
        String expectedPrependedScript1=RepeaterUtil.get().removeItem(rowItem) ;
        List<String>expectedPrependedScripts=new ArrayList<String>() ;
        expectedPrependedScripts.add(expectedPrependedScript1);
        Assert.assertEquals(actualPrependedScripts,expectedPrependedScripts.toString());
    }


    /**
     *    ajax=true  ,parrentaddedtotarget=true
     */
    @Test(groups = {"wicketTests"})
    public void removeRow_2(){
        IDataProvider provider = new ListDataProvider(data(4));
        QuickMockApplication app=new QuickMockApplication();
        WicketTester tester=new WicketTester(app);
        final QuickGridView<Integer> grid=new QuickGridView<Integer>("grid",provider) {
            @Override
            protected void populate(CellItem<Integer> item) {
            }

            @Override
            protected void populateEmptyItem(CellItem<Integer> item) {
            }

        } ;
        grid.setReuse(ReUse.ITEMSNAVIGATION);

        final QuickViewParent parent=new QuickViewParent("parent");
        parent.setOutputMarkupPlaceholderTag(true);
        parent.add(grid);
        //grid.setRows(2);
        grid.setColumns(2);


        final AjaxLink link=new AjaxLink("link") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                grid.removeRow(0);
                target.add(parent);
            }
        };
        TestQuickGridViewContainer container=new TestQuickGridViewContainer("panel") {
            @Override
            public QuickViewParent newParent() {
                return parent;
            }

            @Override
            public AbstractLink newLink() {
                return  link;
            }
        } ;

        tester.startComponentInPage(container);
        final QuickGridView.RowItem rowItem=grid.getRow(0);
        tester.executeAjaxEvent(link, "onclick");
        AjaxRequestTarget target=app.getLastAjaxRequestTarget();
        Assert.assertFalse(target.getComponents().contains(rowItem));
        String actualPrependedScripts=grid.getRepeaterUtil().prependedScripts(target.toString());
        List<String>expectedPrependedScripts=new ArrayList<String>() ;
        Assert.assertEquals(actualPrependedScripts,expectedPrependedScripts.toString());
    }

    /**
     *    ajax=false
     */
    @Test(groups = {"wicketTests"})
    public void removeRow_3(){
        IDataProvider provider = new ListDataProvider(data(4));
        QuickMockApplication app=new QuickMockApplication();
        WicketTester tester=new WicketTester(app);
        final QuickGridView<Integer> grid=new QuickGridView<Integer>("grid",provider) {
            @Override
            protected void populate(CellItem<Integer> item) {
            }

            @Override
            protected void populateEmptyItem(CellItem<Integer> item) {
            }

        } ;
        grid.setReuse(ReUse.ITEMSNAVIGATION);

        final QuickViewParent parent=new QuickViewParent("parent");
        parent.setOutputMarkupPlaceholderTag(true);
        parent.add(grid);
        //grid.setRows(2);
        grid.setColumns(2);


        final Link link=new Link("link") {
            @Override
            public void onClick() {
                grid.removeRow(0);
              }
        };
        TestQuickGridViewContainer container=new TestQuickGridViewContainer("panel") {
            @Override
            public QuickViewParent newParent() {
                return parent;
            }

            @Override
            public AbstractLink newLink() {
                return  link;
            }
        } ;

        tester.startComponentInPage(container);
        final QuickGridView.RowItem rowItem=grid.getRow(0);
        tester.clickLink(link);


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

    List<Integer>data(int size){
        List<Integer>list=new ArrayList<Integer>();
        for(int i=0;i<size;i++){
            list.add(i) ;
        }
        return list;
    }

}
