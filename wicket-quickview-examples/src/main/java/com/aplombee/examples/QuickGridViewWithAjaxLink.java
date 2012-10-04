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

package com.aplombee.examples;

import com.aplombee.QuickGridView;
import com.aplombee.ReUse;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import java.util.ArrayList;
import java.util.List;

/**
 * QuickGridView with AjaxLink
 *
 * @author Vineet Semwal
 */
public class QuickGridViewWithAjaxLink extends WebPage {
    private List<Integer> list = new ArrayList<Integer>();

    public QuickGridViewWithAjaxLink() {
        for (int i = 0; i < 10; i++) {
            list.add(i);
        }
    }


    @Override
    protected void onInitialize() {
        super.onInitialize();

        IDataProvider<Integer> data = new ListDataProvider<Integer>(list);
        WebMarkupContainer numbers = new WebMarkupContainer("numbers");   //parent for quickview
        numbers.setOutputMarkupId(true);  //needed for ajax
        final QuickGridView<Integer> number = new QuickGridView<Integer>("number", data, ReUse.ITEMSNAVIGATION) {
            @Override
            protected void populate(CellItem<Integer> item) {
                item.add(new Label("display", item.getModel()));
            }

            @Override
            protected void populateEmptyItem(CellItem<Integer> item) {
                item.add(new Label("display"));
            }
        };
        number.setColumns(2);
        numbers.add(number);
        add(numbers);

        AjaxLink addLink = new AjaxLink("addLink") {

            @Override
            public void onClick(AjaxRequestTarget target) {
                int newObject=list.get(list.size()-1) +1;
                list.add( newObject);
                QuickGridView.RowItem<Integer> row=number.buildRowItem(); //new row created
                QuickGridView.CellItem<Integer> cell1 = number.buildCellItem(newObject); // cell created

                int newObject2=list.get(list.size()-1) +1;
                list.add( newObject2);
                QuickGridView.CellItem<Integer> cell2 = number.buildCellItem(newObject2); //another cell created
                number.addCells(row,cell1,cell2);    //add cells to row
                number.addRow(row);    //just enough to add row and render it

            }

        };
        addLink.setOutputMarkupPlaceholderTag(true);
        add(addLink);




        AjaxLink addAtStartLink = new AjaxLink("addAtStartLink") {


            @Override
            public void onClick(AjaxRequestTarget target) {
                int newObject=list.get(0) -1;
                list.add(0, newObject);
                QuickGridView.RowItem<Integer> row=number.buildRowItem(); //new row created
                QuickGridView.CellItem<Integer> cell1 = number.buildCellItem(newObject); // cell created

                int newObject2=list.get(0) -1;
                list.add(0, newObject2);
                QuickGridView.CellItem<Integer> cell2 = number.buildCellItem(newObject2); //another cell created
                number.addCells(row,cell1,cell2);         //add cells to row
                number.addRowAtStart(row);    //just enough to add row  and render it  at start

            }

        };
        addAtStartLink.setOutputMarkupPlaceholderTag(true);
        add(addAtStartLink);


    }

}
