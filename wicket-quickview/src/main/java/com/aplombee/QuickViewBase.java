/**
 * Copyright 2012 Vineet Semwal
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.aplombee;

import org.apache.wicket.*;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.repeater.IItemFactory;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
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
    private IQuickReuseStrategy reuseStrategy7;

    public void setReuseStrategy(final IQuickReuseStrategy reuseStrategy) {
        Args.notNull(reuseStrategy, "reuseStrategy");
        this.reuseStrategy7 = reuseStrategy;
    }


    public IQuickReuseStrategy getReuseStrategy() {
        return reuseStrategy7;
    }

    //items created per request ,if used with PagingNavigator/AjaxPagingNavigator then it's the items per page
    private long itemsPerRequest7 = Integer.MAX_VALUE;

    public long getItemsPerRequest() {
        return itemsPerRequest7;
    }

    private long childId = 0;

    @Override
    public String newChildId() {
        childId++;
        return String.valueOf(childId);
    }

    public void setItemsPerRequest(long items) {
        if (items < 1) {
            throw new IllegalArgumentException("itemsPerRequest cannot be less than 1");
        }

        if (this.itemsPerRequest7 != items) {
            if (isVersioned()) {
                addStateChange();
            }
            this.itemsPerRequest7 = items;

            // because items per page can effect the total number of pages we always
            // reset the current page back to zero
            _setCurrentPage(0);
        }

    }

    /**
     * cached only for a request
     */
    private transient Long itemsCount = null;

    private IDataProvider<T> dataProvider;

    public IDataProvider<T> getDataProvider() {
        return dataProvider;
    }


    protected IRepeaterUtil getRepeaterUtil() {
        return RepeaterUtil.get();
    }

    private long currentPage;

    private Component start, end;

    /**
     * represents start of view ,can be any component it's position in the markup should be just before view
     * this is done so that the new children doesn't get mixedup with the other markup or another components
     * specified in immediate parent
     *
     * @return
     */
    public final Component getStart() {
        return start;
    }

    /**
     * represents end of view ,can be any component it's position in the markup should be just after the view
     * this is done so that the new children doesn't get mixedup with the other markup or another components
     * specified in immediate parent
     *
     * @return
     */
    public final Component getEnd() {
        return end;
    }

    /**
     * @param id            component id
     * @param dataProvider  dataprovider of objects
     * @param reuseStrategy children are created again on render
     */
    public QuickViewBase(String id, IDataProvider<T> dataProvider, IQuickReuseStrategy reuseStrategy) {
        super(id);
        Args.notNull(dataProvider, "dataProvider");
        Args.notNull(reuseStrategy, "reuseStrategy");
        this.dataProvider = dataProvider;
        setReuseStrategy(reuseStrategy);
    }


    /**
     * @param id            component id
     * @param dataProvider  dataprovider of objects
     * @param reuseStrategy children are created again on render
     * @param start         start of view
     * @param end           end of view
     */
    public QuickViewBase(String id, IDataProvider<T> dataProvider, IQuickReuseStrategy reuseStrategy, Component start, Component end) {
        super(id);
        Args.notNull(dataProvider, "dataProvider");
        Args.notNull(reuseStrategy, "reuseStrategy");
        this.dataProvider = dataProvider;
        this.start = start;
        if (start != null) {
            start.setOutputMarkupPlaceholderTag(true);
        }
        this.end = end;
        if (end != null) {
            end.setOutputMarkupPlaceholderTag(true);
        }
        setReuseStrategy(reuseStrategy);

    }


    protected Item<T> newItem(String id, long index, IModel<T> model) {
        Item<T> item = new Item<T>(id, getRepeaterUtil().safeLongToInt(index), model);
        item.setOutputMarkupId(true);
        return item;
    }

    /**
     * use in stateless environment as there is no state ,it's user's responsiblity to give unique id and index
     *
     * @param id
     * @param index
     * @param object
     * @return
     */
    public Item<T> buildItem(String id, long index, T object) {
        return buildItem(id, index, getDataProvider().model(object));
    }

    protected Item<T> buildItem(String id, long index, IModel<T> model) {
        Item<T> item = newItem(id, index, model);
        populate(item);
        return item;
    }


    /**
     * creates new item,for stateless environment,you can use {@link QuickViewBase#buildItem} or {@link QuickViewBase#buildItem}
     *
     * @param object model object
     * @return item
     */
    public Item buildItem(long index, T object) {
        return buildItem(newChildId(), index, object);
    }

    protected Item buildItem(long index, IModel<T> model) {
        return buildItem(newChildId(), index, model);
    }


    public boolean isAjax() {
        return getWebRequest().isAjax();
    }


    /**
     * it's a simple add,new item is not drawn just added,no js fired
     *
     * @param components component to be added
     * @return this
     */
    public MarkupContainer simpleAdd(Component... components) {
        super.add(components);
        return this;
    }


    /**
     * it's a simple remove,the item is just removed from quickview ,no js fired
     *
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

    public Iterator<Component> itemsIterator() {
        return iterator();
    }

    @Override
    protected void onPopulate() {
        super.onPopulate();
        clearCachedItemCount();
        long pageToBeCreated = getReuseStrategy().getPageCreatedOnRender();
        //
        // if page to be created is different then the last current page rendered
        //
        if (pageToBeCreated >= 0) {
            _setCurrentPage(pageToBeCreated);
        }
        long page = _getCurrentPage();
        Iterator<Item<T>> existing = (Iterator) itemsIterator();

        //
        // if add items supported and page to be created is not defined
        // then all models for items upto the page last set should be fetched
        //this is useful for ReuseAllSStrategy
        //
        Iterator<IModel<T>> newModels;
        if (getReuseStrategy().isPartialUpdatesSupported() && pageToBeCreated < 0) {
            long modelsCount = (page + 1) * getItemsPerRequest();
            newModels = newModels(0, modelsCount);
        } else {
            //
            //create models only for the desired page
            //
            long offset = page * getItemsPerRequest();
            newModels = newModels(offset, getItemsPerRequest());
        }
        Iterator<Item<T>> newIterator = getReuseStrategy().getItems(factory(), newModels, existing);
        simpleRemoveAll();

        createChildren(newIterator);
    }


    public IItemFactory<T> factory() {
        return new IItemFactory<T>() {
            @Override
            public Item<T> newItem(int index, IModel<T> model) {
                return buildItem(index, model);
            }
        };
    }


    @Override
    public List<Item<T>> addItemsForPage(final long page) {
        long offset = page * getItemsPerRequest();
        Iterator<IModel<T>> newModels = newModels(offset, getItemsPerRequest());
        Iterator<Item<T>> newIterator = getReuseStrategy().addItems(getRepeaterUtil().safeLongToInt(offset), factory(), newModels);
        List<Item<T>> components = new ArrayList<Item<T>>();
        while (newIterator.hasNext()) {
            Item<T> temp = newIterator.next();
            components.add(temp);
            add(temp);
        }
        return components;
    }


    /**
     * Helper class that converts input from IDataProvider to an iterator over view items.
     *
     * @param <T> Model object type
     * @author Igor Vaynberg (ivaynberg)
     */
    protected static final class ModelIterator<T> implements Iterator<IModel<T>> {
        private final Iterator<? extends T> items;
        private final IDataProvider<T> dataProvider;
        private final long max;
        private long index;

        /**
         * Constructor
         *
         * @param dataProvider data provider
         * @param offset       index of first item
         * @param count        max number of items to return
         */
        public ModelIterator(IDataProvider<T> dataProvider, long offset, long count) {
            this.dataProvider = dataProvider;
            max = count;

            items = count > 0 ? dataProvider.iterator(offset, count) : null;
        }

        /**
         * @see java.util.Iterator#remove()
         */
        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        /**
         * @see java.util.Iterator#hasNext()
         */
        @Override
        public boolean hasNext() {
            return items != null && items.hasNext() && (index < max);
        }

        /**
         * @see java.util.Iterator#next()
         */
        @Override
        public IModel<T> next() {
            index++;
            return dataProvider.model(items.next());
        }
    }

    protected Iterator<IModel<T>> newModels(long offset, long count) {
        return new ModelIterator<T>(dataProvider, offset, count);
    }


    protected void createChildren(Iterator<Item<T>> iterator) {
        Args.notNull(iterator, "iterator");
        while (iterator.hasNext()) {
            Item<T> item = iterator.next();
            simpleAdd(item);
        }
    }

    protected Iterator<Item<T>> buildItems(final long index, Iterator<? extends T> iterator) {
        return buildItemsList(index, iterator).iterator();
    }


    protected List<Item<T>> buildItemsList(final long index, Iterator<? extends T> iterator) {
        List<Item<T>> items = new ArrayList<Item<T>>();
        for (long i = index; iterator.hasNext(); i++) {
            T object = iterator.next();
            Item<T> item = buildItem(i, object);
            items.add(item);
        }
        return items;
    }

    /*
     * build items from index=size
     */

    protected Iterator<Item<T>> buildItems(Iterator<? extends T> iterator) {
        long index = size();
        return buildItems(index, iterator);
    }


    /*
     * build items from index=size
     */

    protected List<Item<T>> buildItemsList(Iterator<? extends T> iterator) {
        long index = size();
        return buildItemsList(index, iterator);
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

    /**
     * same as dataprovider size but cached for request to improve performance  in case of multiple call to avoid
     * unnecessary expensive call of {@link org.apache.wicket.markup.repeater.data.IDataProvider#size()}
     *
     * @return dataprovider's size
     */

    public final long getItemsCount() {
        if (itemsCount != null) {
            return itemsCount.longValue();
        }
        itemsCount = getDataProvider().size();
        return itemsCount;
    }

    private void clearCachedItemCount() {
        itemsCount = null;
    }

    /**
     * same as {@link this#getItemsCount()} but takes into account hierarchy so if the view is not visible in hierarchy
     * the returned value is zero else return the getItemsCount() value
     *
     * @return
     */
    public final long getRowsCount() {
        if (!isVisibleInHierarchy()) {
            return 0;
        }
        return getItemsCount();
    }

    /**
     * calculates the number of pages
     *
     * @return number of pages
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

        long count = total / getItemsPerRequest();
        if ((getItemsPerRequest() * count) < total) {
            count++;
        }
        return count;
    }

    /**
     * @see org.apache.wicket.markup.html.navigation.paging.IPageable#getCurrentPage()
     * <p/>
     * don't override
     */

    @Override
    public final long getCurrentPage() {
        return _getCurrentPage();
    }

    /**
     * don't override,it's for internal use
     */
    protected long _getCurrentPage() {
        long page = currentPage;

        /*
         * trim current page if its out of bounds this can happen if items are added/deleted between
         * requests
         */

        final long count = _getPageCount();
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
        Optional<AjaxRequestTarget> target = RequestCycle.get().find(AjaxRequestTarget.class);
        if (target.isPresent()) {
            return target.get();
        }
        return null;
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
            String script = getRepeaterUtil().append((Item) components[i], parent, start, end);
            getSynchronizer().getPrependScripts().add(script);
        }
        getSynchronizer().add(components);

        return this;
    }

    /**
     * this does 2 steps
     * <p/>
     * 1)creates children ,children will get the model object after iterating over objects passed as argument
     * 2)adds children to View using {@link this#add(org.apache.wicket.Component...)}
     *
     * @param objects iterator of model objects for children
     * @return this
     */


    public MarkupContainer addNewItems(T... objects) {
        List<T> list = new ArrayList<T>();
        for (T obj : objects) {
            list.add(obj);
        }
        Iterator<Item<T>> items = buildItems(list.iterator());
        while (items.hasNext()) {
            add(items.next());
        }
        return this;
    }


    /**
     * this does 2 steps
     * <p/>
     * 1)creates children ,children will get the model object after iterating over objects passed as argument
     * 2)adds children to View using {@link this#addAtStart(org.apache.wicket.Component...)}
     * <p>
     * the respective items for objects will be displayed at start of the view in the order of passed objects
     *
     * @param objects iterator of model objects for children
     * @return this
     */

    public MarkupContainer addNewItemsAtStart(T... objects) {
        List<T> list = new ArrayList<T>();
        for (T object : objects) {
            list.add(object);
        }
        List<Item<T>> items = buildItemsList(list.iterator());
        addAtStart(items.toArray(new Item[0]));
        return this;
    }


    /**
     * {@inheritDoc}
     */
    public List<Item<T>> addItemsForNextPage() {
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

    @Override
    public MarkupContainer remove(final Component component) {
        Args.notNull(component, "component can't be null");
        if (isAjax()) {
            String removeScript = getRepeaterUtil().removeItem(component, _getParent());
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
     * draws a new element at start but actually the element is added at last in repeater,
     * this should not pose problem when whole repeater is re-rendered and if data is sorted
     * <p>
     * the item will be displayed at start of the view in the passed order
     * <p>
     * <p>
     * <p/>
     * actually it can be handled properly which means new item(s) created at start  using addAtStart
     * it is mentioned in issue quickview#15 (https://github.com/vineetsemwal/quickview/issues/15)
     *
     * @param components
     * @return this
     */
    public MarkupContainer addAtStart(final Component... components) {
        simpleAdd(components);
        if (!isAjax()) {
            return this;
        }

        for (int i = components.length - 1; i >= 0; i--) {
            MarkupContainer parent = _getParent();
            String updateBeforeScript = getRepeaterUtil().prepend((Item) components[i], parent, start, end);
            getSynchronizer().getPrependScripts().add(updateBeforeScript);
        }
        getSynchronizer().add(components);
        return this;
    }


    /**
     * when called on ajax event ,this method moves navigation-bar to bottom,
     * this works when parent has scroll specified in css by defining overflow-y property
     */
    public void scrollToBottom() {
        if (isAjax()) {
            AjaxRequestTarget target = this.getAjaxRequestTarget();
            target.appendJavaScript(getRepeaterUtil().scrollToBottom(this));
        }
    }

    /**
     * when called on ajax event, this method moves navigation-bar to top ,
     * this works when parent has scroll specified in css by defining overflow-y property
     */
    public void scrollToTop() {
        if (isAjax()) {
            AjaxRequestTarget target = this.getAjaxRequestTarget();
            target.appendJavaScript(getRepeaterUtil().scrollToTop(this));
        }
    }

    /**
     * when called on ajax event, this method moves navigation-bar to height passed in method ,
     * this works when parent has scroll specified in css by defining overflow-y property
     */
    public void scrollTo(int height) {
        if (isAjax()) {
            AjaxRequestTarget target = this.getAjaxRequestTarget();
            target.appendJavaScript(getRepeaterUtil().scrollTo(this, height));
        }
    }

    /**
     * less complex/preferred/clear solution would have been checking if listener is already added in AjaxRequestTarget but
     * since there is no getter for IListeners,there is no way to know listener is added ,it might be added in later versions
     * see issue WICKET-4800
     *
     * @return Synchronizer
     */

    public Synchronizer getSynchronizer() {
        AjaxRequestTarget target = getAjaxRequestTarget();
        if (target == null) {
            return null;
        }
        Synchronizer listener = (Synchronizer) getRequestCycle().getMetaData(synchronizerKey);
        if (listener == null) {
            listener = new Synchronizer(_getParent());
            getRequestCycle().setMetaData(synchronizerKey, listener);
            target.addListener(listener);
        }

        return listener;
    }

    /**
     * Synchronizer basically adds components(repeater's items) and scripts to the the AjaxRequestTarget after
     * checking parent is not added to AjaxRequestTarget .If parent is added scripts and
     * items are not added to the AjaxRequestTarget
     *
     * @author Vineet Semwal
     */
    public static class Synchronizer implements AjaxRequestTarget.IListener {
        private List<String> prependScripts = new ArrayList<String>();
        /**
         * mostly contains items od repeater that will be added to AjaxRequestTarget
         */
        private List<Component> components = new ArrayList<Component>();

        public List<String> getPrependScripts() {
            return prependScripts;
        }

        private List<String> appendScripts = new ArrayList<String>();

        public List<String> getAppendScripts() {
            return appendScripts;
        }

        public List<Component> getComponents() {
            return components;
        }

        private MarkupContainer searchFor;

        public Synchronizer(MarkupContainer searchFor) {
            Args.notNull(searchFor, "searchFor");
            this.searchFor = searchFor;
        }

        @Override
        public void onBeforeRespond(Map<String, Component> map, AjaxRequestTarget target) {
            if (!isParentAddedInAjaxRequestTarget(target)) {
                for (String script : prependScripts) {
                    target.prependJavaScript(script);
                }

                for (String script : appendScripts) {
                    target.appendJavaScript(script);
                }

                target.add(components.toArray(new Component[0]));
            }
        }

        public void add(Component... cs) {
            for (final Component component : cs) {
                Args.notNull(component, "component");
                components.add(component);
            }
        }

        @Override
        public void onAfterRespond(Map<String, Component> map, AjaxRequestTarget.IJavaScriptResponse response) {
        }

        @Override
        public void updateAjaxAttributes(AbstractDefaultAjaxBehavior behavior, AjaxRequestAttributes attributes) {
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
    private MetaDataKey<AjaxRequestTarget.IListener> synchronizerKey = new MetaDataKey<AjaxRequestTarget.IListener>() {
    };

    @Override
    protected void onDetach() {
        dataProvider.detach();
        super.onDetach();
    }


}
