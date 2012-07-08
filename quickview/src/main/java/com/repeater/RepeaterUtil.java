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
import org.apache.wicket.markup.IMarkupFragment;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.util.lang.Args;

/**
 *@author Vineet Semwal
 *
 */
public class RepeaterUtil implements  IRepeaterUtil{
     public static RepeaterUtil instance=new RepeaterUtil();

    public static RepeaterUtil get(){
         return instance;
    }

    public String insertBeforeScript(String tag, String id, String parentContainerId) {
        String script = String.format("insertBefore('%s','%s','%s')", tag, id, parentContainerId);
        return script;
    }

    @Override
    public String insertBeforeScript(Component component, Component parentContainer) {
        String script = insertBeforeScript(getComponentTag(component).getName(), component.getMarkupId(), parentContainer.getMarkupId());
        return script;
    }

    @Override
    public String insertAfterScript(String tag, String id, String parentContainerId) {
        String script = String.format("insertAfter('%s','%s','%s')", tag, id, parentContainerId);
        return script;

    }
    @Override
    public ComponentTag getComponentTag(Component c) {
        IMarkupFragment markup = c.getMarkup();
        MarkupStream stream = new MarkupStream(markup);
        return stream.getTag();
    }

    @Override
    public String insertAfterScript(Component c, Component parentContainer) {
      return insertAfterScript(getComponentTag(c).getName(), c.getMarkupId(), parentContainer.getMarkupId());
    }

    @Override
    public String removeItem(String id) {
        String script = String.format(" removeItem('%s')", id);
        return script;
    }

    @Override
    public String removeItem(Component component) {
        Args.notNull(component, "component");
        return removeItem(component.getMarkupId());
    }

    @Override
    public int safeLongToInt(long value) {
        if (value < Integer.MIN_VALUE || value > Integer.MAX_VALUE) {
            throw new IllegalArgumentException(value + " cannot be cast to int ");
        }
        return (int) value;
    }
}
