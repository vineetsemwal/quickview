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
package repeater.aplombee;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.markup.repeater.Item;
import java.util.List;

/**
 * @author Vineet Semwal
 * 
 */

public interface IQuickView<T> extends IPageable {
	ReUse getReuse();

	MarkupContainer getParent();

	List<Item<T>> addComponentsForPage(final int page);
}