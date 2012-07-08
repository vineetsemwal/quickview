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
import org.apache.wicket.MarkupContainer;
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

    /**
     * {@inheritDoc}
     */
    public String insertBefore(String tag, String markupId, String parentMarkupId) {
        String script = String.format("insertBefore('%s','%s','%s')", tag, markupId, parentMarkupId);
        return script;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String insertBefore(MarkupContainer component, MarkupContainer parent) {
        String script = insertBefore(getComponentTag(component).getName(), component.getMarkupId(), parent.getMarkupId());
        return script;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String insertAfter(String tag, String markupId, String parentMarkupId) {
        String script = String.format("insertAfter('%s','%s','%s')", tag, markupId, parentMarkupId);
        return script;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ComponentTag getComponentTag(Component c) {
        IMarkupFragment markup = c.getMarkup();
        MarkupStream stream = new MarkupStream(markup);
        return stream.getTag();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String insertAfter(MarkupContainer c, MarkupContainer parent) {
      return insertAfter(getComponentTag(c).getName(), c.getMarkupId(), parent.getMarkupId());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String removeItem(String markupId) {
        String script = String.format("removeItem('%s')", markupId);
        return script;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String removeItem(Component component) {
        Args.notNull(component, "component");
        return removeItem(component.getMarkupId());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int safeLongToInt(long value) {
        if (value < Integer.MIN_VALUE || value > Integer.MAX_VALUE) {
            throw new IllegalArgumentException(value + " cannot be cast to int ");
        }
        return (int) value;
    }
}
