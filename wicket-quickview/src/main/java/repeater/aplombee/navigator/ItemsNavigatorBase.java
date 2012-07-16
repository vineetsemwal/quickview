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
package repeater.aplombee.navigator;


import org.apache.log4j.Logger;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.lang.Args;
import org.apache.wicket.util.string.Strings;
import repeater.aplombee.IQuickView;
import repeater.aplombee.IRepeaterUtil;
import repeater.aplombee.RepeaterUtil;
import java.util.ArrayList;
import java.util.List;


/**
 * base navigator which is inherited by {@link  AjaxItemsNavigator}
 *
 * @author Vineet Semwal
 */
public abstract class ItemsNavigatorBase extends Panel {

    private IQuickView repeater;

    public IQuickView getRepeater() {
        return repeater;
    }

    public IRepeaterUtil getRepeaterUtil() {
        return RepeaterUtil.get();
    }

    private static Logger logger = Logger.getLogger(ItemsNavigatorBase.class);

    private String cssClass;

    public void setCssClass(String cssClass) {
        this.cssClass = cssClass;
    }

    public String getCssClass() {
        return cssClass;
    }

    private Component more;

    protected boolean properInitializationCheckDone = false;

    /**
     * don't override this,for internal use only
     */
    protected boolean isProperInitializationCheckDone() {
        return properInitializationCheckDone;
    }

    /**
     * don't override this,for internal use only
     */
    protected void doProperInitializationCheck() {
        if (!properInitializationCheckDone) {
            repeaterNotProperlyInitializedForItemsNavigation(repeater);
            properInitializationCheckDone = true;
        }
    }

    public ItemsNavigatorBase(String id, IModel model, IQuickView repeater) {
        super(id, model);
        Args.notNull(repeater, "repeater");
        setOutputMarkupPlaceholderTag(true);
        this.repeater = repeater;
    }


    @Override
    protected void onInitialize() {
        super.onInitialize();
        add(more = newMore());
        if (!Strings.isEmpty(getCssClass())) {
            AttributeModifier cssClassModifier = new AttributeModifier("class ", getCssClass());
            more.add(cssClassModifier);
        }
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();
        doProperInitializationCheck();
    }

    /**
     * checks if quickview is properly initialized for items navigation
     * <p/>
     * don't override,it's for internal use
     */
    protected void repeaterNotProperlyInitializedForItemsNavigation(IQuickView quickView) {
        getRepeaterUtil().reuseStategyNotSupportedForItemsNavigation(repeater);
        getRepeaterUtil().parentNotSuitable(repeater);
        getRepeaterUtil().outPutMarkupIdNotTrue(repeater);
    }

    public abstract Component newMore();

    public Component getMore() {
        return more;
    }


    /**
     * on a stateful event say onclick ,this method creates new elements for the page
     *
     * @return list of items created
     */
    public List<Item> onStatefulEvent() {
        AjaxRequestTarget target = getAjaxRequestTarget();
        List<Item> list = new ArrayList<Item>();
        int current = getRepeater().getCurrentPage();

         // page for which new items have to created

        int next = current + 1;
        if (next < getRepeater().getPageCount()) {
            list = getRepeater().addComponentsForPage(next);
            getRepeater().setCurrentPage(next);
        }
        target.add(getMore());
        return list;
    }

    public AjaxRequestTarget getAjaxRequestTarget() {
        return AjaxRequestTarget.get();
    }

}
