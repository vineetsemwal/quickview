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

import org.apache.wicket.markup.repeater.IItemFactory;
import org.mockito.Mockito;
import org.testng.Assert;

import java.util.Iterator;

/**
 * @author Vineet Semwal
 */
public class AbstractPagingNavigationStrategyTest {

    public void assertIsAddItemsSupported(IQuickReuseStrategy strategy){
       Assert.assertFalse(strategy.isAddItemsSupported());
    }

   public void assertAddItems(IQuickReuseStrategy strategy){
        Iterator newModels= Mockito.mock(Iterator.class);
        IItemFactory factory=Mockito.mock(IItemFactory.class);
        int startIndex=345;
        strategy.addItems(startIndex,factory,newModels);
    }

    public void assertZeroPageCreatedOnReRender(IQuickReuseStrategy strategy){
        Assert.assertFalse(strategy.isAlwaysZeroPageCreatedOnRender());
    }
}
