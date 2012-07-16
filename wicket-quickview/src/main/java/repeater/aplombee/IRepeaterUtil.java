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

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.ComponentTag;

/**
 * @author Vineet Semwal
 */
public interface IRepeaterUtil {
    /**
     *    insertBefore js call
     *
     * @param tag       repeater tag
     * @param id        repeater markupid
     * @param parentId    parent markupid
     * @return    insertBefore call of js
     */
    String insertBefore(String tag, String id, String parentId);

    /**
     * insertBefore js call for the repeeater and parent passed
     *
     * @param component repeater
     * @param parent    parent to which repeater is added
     * @return insertBefore call of js
     */
    String insertBefore(MarkupContainer component, MarkupContainer parent);

    /**
     *
     * @param tag      repeater tag
     * @param id       repeater markupid
     * @param parentId parent markupid
     * @return insertAfter call of js
     */
    String insertAfter(String tag, String id, String parentId);

    /**
     * finds {@link ComponentTag} of the component passed
     *
     * @param c component whose componenttag has to be found
     *
     * @return {@link ComponentTag}
     */
    ComponentTag getComponentTag(Component c);

    /**
     *
     * @param c      repeater
     * @param parent parent
     * @return insertAfter js call
     */
    String insertAfter(MarkupContainer c, MarkupContainer parent);

    /**
     * removes js call for item whose markupid is passed
     *
     * @param id markupid of the element which needs to be removed
     * @return remove js call
     */
    String removeItem(String id);

    /**
     * removes js call for component which is provided
     *
     * @param component
     * @return remove js call
     */
    String removeItem(Component component);

    /**
     * safely converts long to int
     *
     * @param l
     * @return int value for long passed
     */
    int safeLongToInt(long l);


    /**
     * throws exception if no suitable unary parent is found,unary parent is one which only has one child
     * @param repeater
     */
    void parentNotSuitable(IQuickView repeater);

    /**
     * throws exception if reuse strategy is not supported  for items navigation
     * @param repeater
     */
    void reuseStategyNotSupportedForItemsNavigation(IQuickView repeater);

    /**
     *  throws exception if outmarkupid of parent is not set true and outputMarkupPlaceholderTag is not set true
     *
     * @param repeater
     */
    void outPutMarkupIdNotTrue(IQuickView repeater);

    /**
     * ruse nt initialized or initialized with {@link ReUse.NOT_INITIALIZED}
     * @param repeater
     */
    void reuseNotInitialized(IQuickView repeater);

    /**
     *
     * @param input is {@link org.apache.wicket.ajax.AjaxRequestTarget#toString()}
     *
     * @return  prependedscripts string as displayed by List.toString() of prependedscripts
     *
     */
    String prependedScripts(String input);

    /**
     *
     * @param input is {@link org.apache.wicket.ajax.AjaxRequestTarget#toString()}
     *
     *  @return  appendedscripts string as displayed by List.toString() of appendedscripts
     */
    String appendedScripts(String input);
}
