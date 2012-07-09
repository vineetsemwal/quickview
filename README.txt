 QuickView
-----------------------------
QuickView is a wicket component(RepeatingView) ,it works with wicket 6.0.0-beta2 currently,it lets you adds new rows or remove the rows without the need to
 re-render the whole repeater again when used with ajax components like AjaxLink or AjaxItemsNavigator.AjaxItemsNavigator which is provided in this package
 creates new rows on click.
Quickview can be also be used with PagingNavigator or AjaxPagingNavigator .it should work in all the scenarios where the DataView works.
the way of using it is exactly like DataView .the only difference is you will have to set a reuse constant which is just an enum,
the behavior of the quickview depends on that enum.

Please use the simple examples which comes with the package .

thank you !

Please note the idea/inspiration to write quickview came from Igor Vaynberg's article 
http://wicketinaction.com/2008/10/repainting-only-newly-created-repeater-items-via-ajax/



QuickView uses wicket 6.0.0-beta2 currently.


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