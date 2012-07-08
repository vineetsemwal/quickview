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

package com.repeater;

import org.apache.wicket.Component;
import org.apache.wicket.markup.ComponentTag;

/**
 * @author Vineet Semwal
 */
public interface IRepeaterUtil {
    public String insertBeforeScript(String tag, String id, String parentContainerId);

    public String insertBeforeScript(Component component, Component parentContainer);

    public String insertAfterScript(String tag, String id, String parentContainerId);

    public ComponentTag getComponentTag(Component c);

    public String insertAfterScript(Component c, Component parentContainer);

    public String removeItem(String id);

    public String removeItem(Component component);

    public int safeLongToInt(long l);

}
