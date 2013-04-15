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
import java.util.*;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.MetaDataKey;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.repeater.IItemFactory;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.util.lang.Args;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;

/**
 * base class for {@link QuickView}
 *
 * @author Vineet Semwal
 */
public abstract class QuickViewBase<T> extends RepeatingView implements IQuickView {
   private IQuickReuseStrategy reuseStrategy;

    public void setReuseStrategy(IQuickReuseStrategy reuseStrategy){
        this.reuseStrategy = reuseStrategy;
    }

    public IQuickReuseStrategy getReuseStrategy(){
        return reuseStrategy;
    }

      //items created per request ,if used with PagingNavigator/AjaxPagingNavigator then it's the items per page
    private int itemsPerRequest=Integer.MAX_VALUE;

    public int getItemsPerRequest() {
        return itemsPerRequest;
    }

    /**
     * child id
     */
    private int childId =0;

    /**
     * for newchildId
     */
    @Override
    public String newChildId() {
        childId++;
        return String.valueOf(childId);
    }

    public void setItemsPerRequest(int items) {
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

    private transient int itemsCount=-1;


    private IDataProvider<T> dataProvider;

    public IDataProvider<T> getDataProvider() {
        return dataProvider;
    }


    protected IRepeaterUtil getRepeaterUtil() {
       return RepeaterUtil.get();
    }

    private int currentPage;


    /**
     * @param id              component id
     * @param dataProvider    dataprovider of objects
     * @param reuse           children are created again on render
     *
     */
    public QuickViewBase(String id, IDataProvider<T> dataProvider, IQuickReuseStrategy reuseStrategy) {
        super(id);
        Args.notNull(dataProvider, "dataProvider");
        Args.notNull(reuseStrategy, "reuseStrategy");
         this.dataProvider = dataProvider;
        this.reuseStrategy=reuseStrategy;
    }


    /**
     *  override if you want to provide other version of jquery .by default 1.7.2 version is used
     *
     *  for deployment mode compressed version of jquery is used
     *  for development mode uncompressed version of jquery is used
     *
     * @return jquery reference
     */

    protected ResourceReference jqueryReference(){
     if(getApplication().usesDeploymentConfig()){
         return JqueryCompressedReference.get();
     }else{
         return JqueryResourceReference.get();
     }
    }

    /**
     * @param object model object
     * @param id     child id
     * @return Child created
     */
    protected Item<T> newItem(String id,int index, IModel<T> model) {
        Item<T> item = new Item<T>(id, index, model);
        item.setOutputMarkupId(true);
        return item;
    }

    /**
     * creates new item,this method can be used in stateless environment,unique id is what you have to provide
     *
     * @param id    id of the item
     * @param object   model object
     * @return     item
     */
    public Item<T> buildItem(String id,int index, T object) {
       return buildItem(id,index,getDataProvider().model(object));
     }

    protected Item<T> buildItem(String id,int index, IModel<T> model) {
        Item<T> item = newItem(id,index, model);
        populate(item);
        return item;
    }


    /**
     *
     * creates new item,for stateless environment,you can use {@link QuickViewBase#buildItem(int, Object)} or
     * {@link QuickViewBase#buildItem(String, Object)}
     *
     * @param object model object
     * @return    item
     */
    public Item buildItem(int index,T object) {
       return buildItem(newChildId(),index, object);
    }

    protected Item buildItem(int index,IModel<T> model) {
       return  buildItem(newChildId(),index,model);
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

    public Iterator<Component>itemsIterator(){
       return iterator();
    }

    @Override
    protected void onPopulate() {
     super.onPopulate();
        clearCachedItemCount();
        // if reuse strategy creates items for zero page every time QuickView is re-rendered
        if(reuseStrategy.isAlwaysZeroPageCreatedOnRender()){
            _setCurrentPage(0);
        }
        int page=_getCurrentPage();
        Iterator<Item<T>>existing=(Iterator)itemsIterator();
        int offset=page*getItemsPerRequest();
       Iterator<IModel<T>>newModels=newModels(offset,itemsPerRequest);
        Iterator<Item<T>> newIterator=  reuseStrategy.getItems( factory(),newModels ,existing);
        simpleRemoveAll();
        createChildren(newIterator);
    }



    public IItemFactory<T> factory(){
      return new IItemFactory<T>() {
          @Override
          public Item<T> newItem(int index, IModel<T> model) {
            return buildItem(index, model);
          }
      };
    }


    @Override
    public List<Item<T>> addItemsForPage(final int page) {
        int offset=page*getItemsPerRequest();
        Iterator<IModel<T>>newModels=newModels(offset, itemsPerRequest);
       Iterator<Item<T>>newIterator=  reuseStrategy.addItems(getRepeaterUtil().safeLongToInt(offset), factory(), newModels);
        List<Item<T>>components=new ArrayList<Item<T>>();
        while (newIterator.hasNext()){
            Item<T>temp=newIterator.next();
            components.add(temp);
            add(temp);
         }
       return components;
    }


    /**
     * Helper class that converts input from IDataProvider to an iterator over view items.
     *
     * @author Igor Vaynberg (ivaynberg)
     *
     * @param <T>
     *            Model object type
     */
    protected static final class ModelIterator<T> implements Iterator<IModel<T>>
    {
        private final Iterator<? extends T> items;
        private final IDataProvider<T> dataProvider;
        private final long max;
        private long index;

        /**
         * Constructor
         *
         * @param dataProvider
         *            data provider
         * @param offset
         *            index of first item
         * @param count
         *            max number of items to return
         */
        public ModelIterator(IDataProvider<T> dataProvider, int offset, int count)
        {
            this.dataProvider = dataProvider;
            max = count;

            items = count > 0 ? dataProvider.iterator(offset, count) : null;
        }

        /**
         * @see java.util.Iterator#remove()
         */
        @Override
        public void remove()
        {
            throw new UnsupportedOperationException();
        }

        /**
         * @see java.util.Iterator#hasNext()
         */
        @Override
        public boolean hasNext()
        {
            return items != null && items.hasNext() && (index < max);
        }

        /**
         * @see java.util.Iterator#next()
         */
        @Override
        public IModel<T> next()
        {
            index++;
            return dataProvider.model(items.next());
        }
    }

    protected Iterator<IModel<T>>newModels(int offset,int count){
       return new ModelIterator<T>(dataProvider,offset,count);

    }

    public Item<T> getItem(long index) {
        return (Item) get(getRepeaterUtil().safeLongToInt(index));
    }


    protected void createChildren(Iterator<Item<T>>iterator) {
      Args.notNull(iterator,"iterator");
       while (iterator.hasNext()){
           Item<T>item=iterator.next();
           simpleAdd(item);
       }
    }

    protected Iterator<Item<T>>buildItems(final int index, Iterator<? extends T> iterator ){
        List<Item<T>> items=new ArrayList<Item<T>>();
        for(int i=index; iterator.hasNext();i++){
            T object=iterator.next();
            Item<T>item=buildItem( i,object);
            items.add(item);
        }
           return items.iterator();
    }

    /*
     * build items from index=size
     */
    protected Iterator<Item<T>>buildItems(Iterator<? extends T> iterator ){
        int index=size();
       return buildItems(index,iterator);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderJavaScriptReference(jqueryReference());
        response.renderJavaScriptReference(RepeaterUtilReference.get());
         }


    public final int getItemsCount(){
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

    public int getRowsCount(){
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
    public final int getPageCount() {
        return _getPageCount();
    }

    /**
     * don't override ,it's used for testing purpose
     *
     * @return number of pages
     */
    protected int _getPageCount() {
        int total = getRowsCount();

        int count = total / itemsPerRequest;
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
    public final int getCurrentPage() {
      return  _getCurrentPage();
    }

    /**
     * don't override,it's for internal use
     *
     */
    protected  int _getCurrentPage() {
        int page = currentPage;

        /*
        * trim current page if its out of bounds this can happen if items are added/deleted between
        * requests
        */

        final int count=_getPageCount();
        if (page > 0 && page >= count) {
            page = ((count - 1) >= 0) ? (count - 1) : 0;
            currentPage = page;
            return page;
        }
        return page;
    }

    /**
     * @see org.apache.wicket.markup.html.navigation.paging.IPageable#setCurrentPage(int)
     */

    public final void setCurrentPage(int page) {
        _setCurrentPage(page);
    }

    /**
     * don't override,it's for internal use
     *
     */
    protected void _setCurrentPage(int page) {
        if (currentPage != page) {
            if (isVersioned()) {
                addStateChange();

            }
        }
        currentPage = page;
    }

    public AjaxRequestTarget getAjaxRequestTarget() {
    	return AjaxRequestTarget.get();
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
    public MarkupContainer add(final Component... c) {
        simpleAdd(c);
        if (!isAjax()) {
            return this;
        }

        for (int i = 0; i < c.length; i++) {
            MarkupContainer parent = _getParent();
            String script = getRepeaterUtil().append((Item)c[i], parent);
            getSynchronizer().getPrependScripts().add(script);
          }
        getSynchronizer().add(c);
        return this;
    }

    /**
     *
     *
     *  this does 2 steps
     *
     *  1)creates children ,children will get the model object after iterating over objects passed as argument
     *  2)adds children to View using {@link this#add(org.apache.wicket.Component...)}
     * @param objects  iterator of model objects for children
     * @return this
     */


    public MarkupContainer addNewItems(T... objects){
        List<T>list=new ArrayList<T>();
        for(T obj:objects){
          list.add(obj);
        }
        Iterator<Item<T>>items= buildItems(list.iterator());
        while (items.hasNext()){
            add(items.next())  ;
        }
        return this;
    }


    /**
     *  this does 2 steps
     *
     *  1)creates children ,children will get the model object after iterating over objects passed as argument
     *  2)adds children to View using {@link this#addAtStart(org.apache.wicket.Component...)}
     * @param objects  iterator of model objects for children
     * @return this
     */

    public MarkupContainer addNewItemsAtStart(T... objects){
        List<T>list=new ArrayList<T>();
        for(T obj:objects){
            list.add(obj);
        }
        Iterator<Item<T>>items= buildItems(list.iterator());
        while (items.hasNext()){
            addAtStart(items.next())  ;
        }
        return this;
    }



    /**
     * {@inheritDoc}
     */
    public  List<Item<T>> addItemsForNextPage(){
        List<Item<T>> list = new ArrayList<Item<T>>();
        int current = getCurrentPage();

        // page for which new items have to created

        int next = current + 1;
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
    public MarkupContainer remove(final Component component) {
        Args.notNull(component, "component can't be null");
        AjaxRequestTarget target = getAjaxRequestTarget();
        if (isAjax()) {
          String removeScript = getRepeaterUtil().removeItem(component,_getParent());
           getSynchronizer().getPrependScripts().add(removeScript);
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
     * this should not pose problem when whole repeater is rendered and if dataprovider is sorted
     *
     * @param c
     * @return this
     */
    public MarkupContainer addAtStart(final Component... components) {
        simpleAdd(components);
        if (!isAjax()) {
            return this;
        }

        for (int i = 0; i < components.length; i++) {
            MarkupContainer parent = _getParent();
            String updateBeforeScript = getRepeaterUtil().prepend((Item)components[i], parent);
            getSynchronizer().getPrependScripts().add(updateBeforeScript);
        }
        getSynchronizer().add(components);
        return this;
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
            target.appendJavaScript(getRepeaterUtil().scrollTo(this,height));
        }
    }

    /**
     *
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

    public final MetaDataKey<AjaxRequestTarget.IListener>getSynchronizerKey(){
        return synchronizerKey;
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
