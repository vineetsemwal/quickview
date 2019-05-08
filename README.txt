
****The Project has moved to wicketstuff now****
since it is a part of wicketstuff now and will therefore see a maven release with every new wicket version from 8.3.0
The wiki is now available on https://github.com/wicketstuff/core/wiki/quickview


-------------------------------------


  QuickView and QuickGridView
-----------------------------
QuickView is a wicket component(RepeatingView) ,it lets you add or remove the rows without the need to re-render the whole repeater again in ajax use cases.
AjaxItemsNavigator which is provided in this package creates new rows on click.Quickview can be also be used with PagingNavigator or AjaxPagingNavigator .


QuickGridView extends QuickView ,it renders items in rows and columns in table/grid format.it also lets you add or remove the rows without the need to 
re-render the whole repeater again in ajax use cases.it works well with AjaxItemsNavigator or PagingNavigator.


read more about QuickView and QuickGridView at https://github.com/vineetsemwal/quickview/wiki

Please use simple examples which comes with the package .

thank you !

Please note the idea/inspiration to write quickview came from Igor Vaynberg's article 
http://wicketinaction.com/2008/10/repainting-only-newly-created-repeater-items-via-ajax/


QuickView is based on Apache Wicket.


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
