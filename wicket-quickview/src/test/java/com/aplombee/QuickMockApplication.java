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
import org.apache.wicket.MetaDataKey;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.mock.MockApplication;
import org.apache.wicket.request.cycle.RequestCycle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Vineet Semwal
 *
 */
public class QuickMockApplication extends MockApplication {
    private AjaxRequestTarget lastAjaxRequestTarget;

    public AjaxRequestTarget getLastAjaxRequestTarget() {
        return lastAjaxRequestTarget;
    }

   // private Map<MetaDataKey,AjaxRequestTarget.IListener>map=new HashMap<MetaDataKey, AjaxRequestTarget.IListener>();

    @Override
    protected void init() {
        super.init();
        AjaxRequestTarget.IListener listener=new AjaxRequestTarget.IListener(){
            @Override
            public void onBeforeRespond(Map<String, Component> map, AjaxRequestTarget target) {
                lastAjaxRequestTarget=target;

            }

            @Override
            public void onAfterRespond(Map<String, Component> map, AjaxRequestTarget.IJavaScriptResponse response) {

            }
        } ;
        getAjaxRequestTargetListeners().add(listener);

    }


}
