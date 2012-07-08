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

import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.WebPage;

public class HomePage extends WebPage {
	private static final long serialVersionUID = 1L;

    public HomePage(final PageParameters parameters) {
	super(parameters);
       BookmarkablePageLink rowsNavLink=new BookmarkablePageLink("rowsNavLink",RowsNavigatorPage.class);

        BookmarkablePageLink ajaxPagingLink=new BookmarkablePageLink("ajaxPagingLink",AjaxPagingNavigatorPage.class);
        add(rowsNavLink,ajaxPagingLink);

        BookmarkablePageLink ajaxLink=new BookmarkablePageLink("ajaxLink",AjaxLinkPage.class);
        add(ajaxLink);
    }
}
