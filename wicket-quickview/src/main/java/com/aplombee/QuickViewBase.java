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
import org.apache.wicket.Application;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.MetaDataKey;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.util.lang.Args;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;

import java.util.*;

/**
 * base class for {@link QuickView}
 *
 * @author Vineet Semwal
 */
public abstract class QuickViewBase<T> extends RepeatingView implements IQuickView {

      //items created per request ,if used with PagingNavigator/AjaxPagingNavigator then it's the items per page
    private long itemsPerRequest=Integer.MAX_VALUE;

    public long getItemsPerRequest() {
        return itemsPerRequest;
    }

    private long childId =0;

    public long getChildId(){
        return childId;
    }

    @Override
    public String newChildId() {
        childId++;
        return String.valueOf(childId);
    }

    public void setItemsPerRequest(long items) {
        if (items < 1)
        {
            throw new IllegalArgumentException("itemsPerRequest cannot be less than 1");
        }

        if (this.itemsPerRequest != items)
        {
            if (isVersioned())
            {
                addStateChange();
            }
            this.itemsPerRequest = items;

            // because items per page can effect the total number of pages we always
            // reset the current page back to zero
            _setCurrentPage(0);
        }

    }

    private transient long itemsCount=-1;

    private IDataProvider<T> dataProvider;

    public IDataProvider<T> getDataProvider() {
        return dataProvider;
    }

    /**
     * reuse strategy
     */
    private ReUse reuse;

    @Override
    public ReUse getReuse() {
        return reuse;
    }

   /**
     * set reuse strategy
     * <p/>
     * for paging ie. when used with {@link org.apache.wicket.markup.html.navigation.paging.PagingNavigator} or {@link org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator}  the
     * {@link ReUse.DEFAULT_PAGING} is preferred
     * <p/>
     * for rows navigation purpose {@link ReUse.DEFAULT_ROWSNAVIGATOR} is preferred
     *
     * @param reuse
     */
    public void setReuse(ReUse reuse) {
        Args.notNull(reuse,"reuse");
        if(reuse==ReUse.NOT_INITIALIZED){
            throw  new IllegalArgumentException("reuse can't be set to NOT_INITIALIZED ");
        }
        this.reuse = reuse;
    }

    protected IRepeaterUtil getRepeaterUtil() {
       return RepeaterUtil.get();
    }

    private long currentPage;


    /**
     * @param id              component id
     * @param dataProvider    dataprovider of objects
     * @param reuse           children are created again on render
     *
     */
    public QuickViewBase(String id, IDataProvider<T> dataProvider, ReUse reuse) {
        super(id);
        Args.notNull(dataProvider, "dataProvider");
        Args.notNull(reuse, "reuse");
        this.reuse = reuse;
         this.dataProvider = dataProvider;
    }


    protected Item<T> newItem(String id,long index, T object) {
        Item<T> item = new Item<T>(id, getRepeaterUtil().safeLongToInt(index), getDataProvider().model(object));
        item.setMarkupId(String.valueOf(id));
        item.setOutputMarkupId(true);
        return item;
    }


    public Item<T> buildItem(String id,long index, T object) {
        Item<T> item = newItem(id,index, object);
        populate(item);
        return item;
       }


    /**
     *
     * creates new item,for stateless environment,you can use {@link QuickViewBase#buildItem} or {@link QuickViewBase#buildItem}
     *
     * @param object model object
     * @return    item
     */
    public Item buildItem(T object) {
       return buildItem(newChildId(), getChildId(), object);
    }

    public boolean isAjax() {
        return getWebRequest().isAjax();
    }

    /**
     *  it's a simple add,new item is not drawn just added,no js fired
     * @param c component to be added
     * @return this
     */
    public MarkupContainer simpleAdd(Component... c) {
         super.add(c);
         return this;
    }


    /**
     * it's a simple remove,the item is just removed from quickview ,no js fired
     * @param c
     * @return this
     */
    public MarkupContainer simpleRemove(Component c) {
        super.remove(c);
        return this;
    }

    public MarkupContainer simpleRemoveAll() {
         return super.removeAll();
    }


    @Override
    protected void onPopulate() {
        super.onPopulate();
        clearCachedItemCount();
       getRepeaterUtil().reuseNotInitialized(this);
       getRepeaterUtil().parentNotSuitable(this);
        simpleRemoveAllIfNotReuse();
        long current=_getCurrentPage();
        if (size() == 0) {

             // all children might have got removed ,if true then create children of last page
             //for first render currentpage will be 0

            if (ReUse.PAGING == reuse) {
                createChildren(current);
            }


             //   not first render but items were removed

            if ((ReUse.ITEMSNAVIGATION == reuse) )  {
                createChildren(0);
                _setCurrentPage(0);
            }


             // first render,no children were added,so populated with first page

            if((ReUse.ALL == reuse) || (ReUse.CURRENTPAGE == reuse)){
               createChildren(0);
            }
        }
        /**
         * stategy is reuse and it's not first render
         */
        else {
            /**
             * if reuse=CURRENTPAGE ,reuse the current page elements and remove all others
             */
            if (ReUse.CURRENTPAGE == reuse) {
                /**
                 * remove first page till the current page -1
                 */
                removePages(0, current - 1);
                /**
                 * remove the page after current page till the lastpage is found
                 */
                removePages(current + 1, _getPageCount() - 1);
            }
        }

    }

    public Item<T> getItem(long index) {
        return (Item) get(getRepeaterUtil().safeLongToInt(index));
    }

    /**
     * remove pages from startpage till stop page, including stopPage
     *
     * @param startPage
     * @param stopPage
     */
    public MarkupContainer removePages(long startPage, long stopPage) {
        for (long i = startPage; i <= stopPage; i++) {
            long startIndex = i * itemsPerRequest;
            long endIndex = startIndex + itemsPerRequest;
            for (long itemIndex = startIndex; itemIndex < endIndex; itemIndex++) {
                Item item = getItem(itemIndex);
                if (item != null) {
                    simpleRemove(item);
                }
            }
        }
        return this;
    }

    /**
     * creates children for the page provided
     *
     * @param page
     */
    protected void createChildren(long page) {
        long items = page * getItemsPerRequest();
        Iterator<? extends T> iterator = getDataProvider().iterator(items, getItemsPerRequest());
        for (long i = items; iterator.hasNext(); i++) {
            T obj = iterator.next();
            Component item = buildItem(newChildId(), i,obj);
            simpleAdd(item);
        }
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
         response.render(JavaScriptHeaderItem.forReference(RepeaterUtilReference.get()));
        /**
         * jquery reference added,it's not important as wicket itself uses jquery in ajax use case but still added to be safe
         */
        response.render(JavaScriptHeaderItem.forReference(Application.get().getJavaScriptLibrarySettings().getJQueryReference()));
         }

    public final long getItemsCount(){
        if(itemsCount>=0){
            return itemsCount;
        }
           itemsCount=getDataProvider().size();
            return itemsCount;
    }

    private void clearCachedItemCount()
    {
        itemsCount= -1;
    }

    public long getRowsCount(){
        if(!isVisibleInHierarchy()){
            return 0;
        }
        return getItemsCount();
    }

    /**
     * calculates the number of pages
     *
     *
     * @return  number of pages
     */

    @Override
    public final long getPageCount() {
        return _getPageCount();
    }

    /**
     * don't override ,it's used for testing purpose
     *
     * @return number of pages
     */
    protected long _getPageCount() {
        long total = getRowsCount();

        long count = total / itemsPerRequest;
        if ((itemsPerRequest * count) < total) {
            count++;
        }
        return count;
    }

    /**
     * @see org.apache.wicket.markup.html.navigation.paging.IPageable#getCurrentPage()
     *
     * don't override
     */

    @Override
    public final long getCurrentPage() {
      return   _getCurrentPage();
    }

    /**
     * don't override,it's for internal use
     *
     */
    protected  long _getCurrentPage() {
        long page = currentPage;

        /*
        * trim current page if its out of bounds this can happen if items are added/deleted between
        * requests
        */

        final long count=_getPageCount();
        if (page > 0 && page >= count) {
            page = Math.max(count - 1, 0);
            currentPage = page;
            return page;
        }
        return page;
    }

    /**
     * @see org.apache.wicket.markup.html.navigation.paging.IPageable#setCurrentPage(long)
     */

    public final void setCurrentPage(long page) {
        _setCurrentPage(page);
    }

    /**
     * don't override,it's for internal use
     *
     */
    protected void _setCurrentPage(long page) {
        if (currentPage != page) {
            if (isVersioned()) {
                addStateChange();

            }
        }
        currentPage = page;
    }

    public AjaxRequestTarget getAjaxRequestTarget() {
        return RequestCycle.get().find(AjaxRequestTarget.class);
    }


    protected abstract void populate(Item<T> item);




    public static class ChildVisitor implements IVisitor<Component, Boolean> {

        private Component searchFor;

        public ChildVisitor(Component searchFor) {
            this.searchFor = searchFor;
        }

        public void component(Component c, IVisit<Boolean> visit) {
            if (searchFor.getPageRelativePath().equals(c.getPageRelativePath())) {
                visit.stop(true);
            }
        }
    }

    /**
     * don't override,it's for internal use
     *
     */
    protected MarkupContainer _getParent() {
        return getParent();
    }



    @Override
    public MarkupContainer add(final Component... components) {
        simpleAdd(components);
        if (!isAjax()) {
            return this;
        }

        for (int i = 0; i < components.length; i++) {
            MarkupContainer parent = _getParent();
            String script = getRepeaterUtil().insertAfter((Item)components[i], parent);
            getSynchronizer().getPrependScripts().add(script);
          }
        getSynchronizer().add(components);

        return this;
    }


    /**
     * {@inheritDoc}
     */
    public  List<Item<T>> addItemsForNextPage(){
        List<Item<T>> list = new ArrayList<Item<T>>();
        long current = getCurrentPage();

        // page for which new items have to created

        long next = current + 1;
        if (next < _getPageCount()) {
            list = addItemsForPage(next);
           _setCurrentPage(next);
        }
        return list;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<Item<T>> addItemsForPage(final long page) {
        long newIndex=page* getItemsPerRequest();
        return addItemsFromIndex(newIndex);
    }

    /**
     * create and draw children from the provided childId ,number of
     * children created are smaller than equal to getItemsPerRequest()
     *
     * @return list of components created
     */

    public List<Item<T>> addItemsFromIndex(final long index) {
        clearCachedItemCount();
        simpleRemoveAllIfNotReuse();
        long newIndex=index;
        Iterator<? extends T> iterator = getDataProvider().iterator(newIndex, getItemsPerRequest());
        List<Item<T>> components = new ArrayList<Item<T>>();
        // long i = newIndex;
        while (iterator.hasNext()) {
            T t = iterator.next();
            Item<T> c = buildItem(newChildId(),newIndex, t);
            components.add(c);
            add(c);
            newIndex++;
        }

        return components;
    }


    @Override
    public MarkupContainer remove(final Component component) {
        Args.notNull(component, "component can't be null");
        if (isAjax()) {
          String removeScript = getRepeaterUtil().removeItem(component);
           getSynchronizer().getPrependScripts().add(removeScript);
           getSynchronizer().add(component);
        }
        return simpleRemove(component);
    }


    @Override
    public MarkupContainer remove(final String id) {
        final Component component = get(id);
        return remove(component);
    }

    /**
     * draws a new element at start but the actually element is added at last in repeater,
     * this should not pose problem when whole repeater is rendered and if data is sorted
     *
     * @param components
     * @return this
     */
    public MarkupContainer addAtStart(final Component... components) {
        simpleAdd(components);
        if (!isAjax()) {
            return this;
        }

        for (int i = 0; i < components.length; i++) {
            MarkupContainer parent = _getParent();
            String updateBeforeScript = getRepeaterUtil().insertBefore((Item)components[i], parent);
            getSynchronizer().getPrependScripts().add(updateBeforeScript);
        }
        getSynchronizer().add(components);
        return this;
    }

    /**
     * removes all children if reuse is not true
     */
    public void simpleRemoveAllIfNotReuse() {
        if (reuse == ReUse.PAGING || reuse == ReUse.ITEMSNAVIGATION) {
            simpleRemoveAll();
        }
    }

    /**
     * when called on ajax event ,this method moves navigation-bar to bottom,
     * this works when parent has scroll specified in css by defining overflow-y property
     *
     */
    public void scrollToBottom(){
     if(isAjax()){
        AjaxRequestTarget target= this.getAjaxRequestTarget();
         target.appendJavaScript(getRepeaterUtil().scrollToBottom(this));
     }
    }

    /**
     * when called on ajax event, this method moves navigation-bar to top ,
     * this works when parent has scroll specified in css by defining overflow-y property
     *
     */
    public void scrollToTop(){
        if(isAjax()){
            AjaxRequestTarget target= this.getAjaxRequestTarget();
            target.appendJavaScript(getRepeaterUtil().scrollToTop(this));
        }
    }

    /**
     * when called on ajax event, this method moves navigation-bar to height passed in method ,
     * this works when parent has scroll specified in css by defining overflow-y property
     *
     */
    public void scrollTo(int height){
        if(isAjax()){
            AjaxRequestTarget target= this.getAjaxRequestTarget();
            target.appendJavaScript(getRepeaterUtil().scrollTo(this, height));
        }
    }

    /**
     *
     * less complex/preferred/clear solution would have been checking if listener is already added in AjaxRequestTarget but
     * since there is no getter for IListeners,there is no way to know listener is added ,it might be added in later versions
     * see WICKET-4800
     *
     * @return  Synchronizer
     */

    public Synchronizer getSynchronizer(){
        AjaxRequestTarget target=getAjaxRequestTarget();
        if(target==null){
            return null;
        }
        Synchronizer listener=(Synchronizer)getRequestCycle().getMetaData(synchronizerKey);
          if(listener==null){
             listener=new Synchronizer(_getParent());
              getRequestCycle().setMetaData(synchronizerKey,listener);
              target.addListener(listener);
           }

        return listener;
    }

    /**
     Synchronizer basically adds components(repeater's items) and scripts to the the AjaxRequestTarget after
     *  checking parent is not added to AjaxRequestTarget .If parent is added scripts and
     *  items are not added to the AjaxRequestTarget
     *
     *
     * @author Vineet Semwal
     */
    public static class Synchronizer implements AjaxRequestTarget.IListener{
        private List<String> prependScripts =new ArrayList<String>();
        /**
         * mostly contains items od repeater that will be added to AjaxRequestTarget
         */
        private List<Component>components=new ArrayList<Component>();
        public List<String> getPrependScripts(){
            return prependScripts;
        }
        public List<Component>getComponents(){
            return components;
        }

        private MarkupContainer searchFor;

        public Synchronizer(MarkupContainer searchFor){
            Args.notNull(searchFor,"searchFor");
            this.searchFor=searchFor;
        }

        @Override
        public void onBeforeRespond(Map<String, Component> map, AjaxRequestTarget target) {
           if(!isParentAddedInAjaxRequestTarget(target)){
                for(String script: prependScripts){
                   target.prependJavaScript(script);
                }

               target.add(components.toArray(new Component[0]));
            }
        }

        public void add(Component... cs)
        {
            for (final Component component : cs)
            {
                Args.notNull(component, "component");
                components.add(component);
            }
        }

        @Override
        public void onAfterRespond(Map<String, Component> map, AjaxRequestTarget.IJavaScriptResponse response) {
        }

        /**
         * checks if parent of repeater is added to the components added to
         * A.R.T(ajaxrequesttarget)
         *
         * @return true if parent of repeatingview is added to A.R.T
         */
        public boolean isParentAddedInAjaxRequestTarget(AjaxRequestTarget target) {
          Collection<? extends Component> cs = target.getComponents();
            if (cs == null) {
                return false;
            }
            if (cs.isEmpty()) {
                return false;
            }
            //if repeater's parent is added to component return true
            if (cs.contains(searchFor)) {
                return true;
            }
            //search repeater's parent in children of components added in A.R.T
            boolean found = false;
            for (Component c : cs) {
                if (c instanceof MarkupContainer) {
                    MarkupContainer mc = (MarkupContainer) c;
                    Boolean result = addNewChildVisitor(mc, searchFor);
                    if (Boolean.TRUE.equals(result)) {
                        found = true;
                        break;
                    }
                }
            }
            return found;
        }

        /**
         * @param parent    parent on which ChildVisitor is added
         * @param searchFor ,searchFor is the component which visitor search for
         * @return true if searchFor is found
         */
        protected Boolean addNewChildVisitor(MarkupContainer parent, Component searchFor) {
            return parent.visitChildren(new ChildVisitor(searchFor));
        }

    }

    /**
     * key corresponding to AjaxRequestTarget.IListener in request metadata
     */
    private MetaDataKey<AjaxRequestTarget.IListener> synchronizerKey =new MetaDataKey<AjaxRequestTarget.IListener>() {
    };

    @Override
    protected void onDetach() {
        dataProvider.detach();
        super.onDetach();
    }



}
