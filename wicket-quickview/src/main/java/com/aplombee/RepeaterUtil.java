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
import org.apache.wicket.Page;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.IMarkupFragment;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.util.lang.Args;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Vineet Semwal
 */
public class RepeaterUtil implements IRepeaterUtil {
    private static RepeaterUtil instance = new RepeaterUtil();

    public static RepeaterUtil get() {
        return instance;
    }

    private RepeaterUtil(){}
    /**
     * {@inheritDoc}
     */
    public String insertBefore(String tag, String markupId, String parentMarkupId) {
        String script = String.format("insertBefore('%s','%s','%s');", tag, markupId, parentMarkupId);
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
        String script = String.format("insertAfter('%s','%s','%s');", tag, markupId, parentMarkupId);
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
        String script = String.format("removeItem('%s');", markupId);
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

    /**
     * {@inheritDoc}
     */
    @Override
    public final void parentNotSuitable(IQuickView quickView) {
        Args.notNull(quickView,"quickview") ;
        if (quickView.getReuse() == ReUse.DEFAULT_PAGING || quickView.getReuse() == ReUse.CURRENTPAGE) {
            return;
        }
        MarkupContainer parent = quickView.getParent();
        if (parent == null) {
            throw new QuickViewNotAddedToParentException("add quickview to a markupcontainer");
        }
        if (parent instanceof Page) {
            throw new QuickViewNotAddedToParentException("add quickview to a markupcontainer");
        }
        if (parent.size() > 1) {
            throw new ParentNotUnaryException("the markupcontainer to which quickview is attached should have quickview as its only child");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void outPutMarkupIdNotTrue(IQuickView quickView) {
        Args.notNull(quickView,"quickview") ;
        MarkupContainer container = quickView.getParent();
        if (container.getOutputMarkupId() == false && container.getOutputMarkupPlaceholderTag() == false) {
            throw new OutputMarkupIdNotTrueException("parent doesn't have setOutputMarkupId to true");
        }
    }

    /**
     * {@inheritDoc}
     */
    public final void reuseNotInitialized(IQuickView quickView) {
        Args.notNull(quickView,"quickview") ;
        if (ReUse.NOT_INITIALIZED == quickView.getReuse()) {
            throw new ReuseNotInitializedException("reuse strategy is not set or you have set  ReUse.NOT_INITIALIZED ");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void reuseStategyNotSupportedForItemsNavigation(IQuickView quickView) {
        Args.notNull(quickView,"quickview") ;
        reuseNotInitialized(quickView);
        if (ReUse.DEFAULT_PAGING == quickView.getReuse() || ReUse.CURRENTPAGE==quickView.getReuse()) {
            throw new ReuseStrategyNotSupportedException(ReUse.DEFAULT_PAGING + " stategy is not supported for itemsnavigator ");
        }
    }


    protected String scripts(String regex,String input){
        int regexEnd=end(regex,input);
        String afterPrepend=input.substring(regexEnd);
        final String openBracket="\\[",closeBracket="]";
        int openBracketEnd=end(openBracket, afterPrepend);
        String afterOpen=afterPrepend.substring(openBracketEnd);
        int closeBracketEnd=end(closeBracket,afterOpen);
        String scriptsString=afterOpen.substring(0,closeBracketEnd);
        return scriptsString;
    }

    /**
     * {@inheritDoc}
     */
    public String prependedScripts(String input){
        final String regex="prependJavaScript";
        return scripts(regex,input);
    }

    /**
     * {@inheritDoc}
     */
    public String appendedScripts(String input){
        final String regex="appendJavaScript";
        return scripts(regex,input);
    }

    public int end(final String regex,final String input){
        Pattern pattern=Pattern.compile(regex);
        Matcher matcher=pattern.matcher(input);
        int end=0;
        if( matcher.find()){
            end=matcher.end();
        }
        return end;
    }

    @Override
    public String scrollToBottom(String markupId){
      return String.format("scrollToBottom('%s');",markupId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String scrollToBottom(IQuickView quickView){
      return scrollToBottom(quickView.getParent().getMarkupId());
    }

    @Override
    public String scrollToTop(String markupId){
        return String.format("scrollToTop('%s');",markupId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String scrollToTop(IQuickView quickView){
        return  scrollToTop(quickView.getParent().getMarkupId());
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String scrollTo(IQuickView quickView,int height){
            return scrollTo(quickView.getParent().getMarkupId(),height);
    }

    @Override
    public String scrollTo(String markupId,int height){
        return String.format("scrollTo('%s',%d);",markupId,height);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String isComponentScrollBarAtBottom(MarkupContainer component) {
        return String.format("isComponentScrollBarAtBottom('%s');",component.getMarkupId());
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String isPageScrollBarAtBottom(){
       return "isPageScrollBarAtBottom();";
    }

    /**
     *  throw this exception if quickview's parent is not found
     *
     * @author Vineet Semwal
     */
    public static class QuickViewNotAddedToParentException extends RuntimeException {
        public QuickViewNotAddedToParentException(String message) {
            super(message);
        }
    }

    /**
     * throw this exception if outmarkupid is not set to true
     * @author Vineet Semwal
     */
    public static class OutputMarkupIdNotTrueException extends RuntimeException {
        public OutputMarkupIdNotTrueException(String message) {
            super(message);
        }
    }

    /**
     *  throw this exception if reuse constant set is not supported
     * @author Vineet Semwal
     */
    public static class ReuseStrategyNotSupportedException extends RuntimeException {
        public ReuseStrategyNotSupportedException(String message) {
            super(message);
        }
    }

    /**
     *  throw this exception if quickview's parent has more than one child in case of items navigation
     * @author Vineet Semwal
     */
    public static class ParentNotUnaryException extends RuntimeException {
        public ParentNotUnaryException(String message) {
            super(message);
        }
    }


    /**
     *  throw this exception if quickview is not initialized with reuse constant
     * @author Vineet Semwal
     */
    public static class ReuseNotInitializedException extends RuntimeException {
        public ReuseNotInitializedException(String message) {
            super(message);
        }
    }




}
