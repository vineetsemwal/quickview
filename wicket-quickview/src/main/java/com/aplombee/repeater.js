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

/**
 * to use a method use QuickView.methodName(parameter) foreg. QuickView.createItem("div","someid");
 *  
 * 
 * @author Vineet Semwal
 */
var QuickView = {
    createItem: function(tag, theId) {
        return $("<" + tag + ">").attr("id", theId);
    },
    removeItem: function(id) {
        $("#" + id).remove();
    },
    insertAfter: function(tag, theId, parentContainerId) {
        var item = QuickView.createItem(tag, theId);
        $("#" + parentContainerId).append(item);
    },
    insertBefore: function(tag, theId, parentContainerId) {
        var item = QuickView.createItem(tag, theId);
        $("#" + parentContainerId).prepend(item);
    },
    scrollToBottom: function(id)
    {
        var element = $("#" + id);
        scrollTo(id, $(element).height());
    },
    scrollToTop: function(id) {
        scrollTo(id, 0);
    },
    scrollTo: function(id, height) {
        var element = $("#" + id);
        $(element).scrollTop(height);
    },
    isComponentScrollBarAtBottom: function(id) {
        var el = $("#" + id);
        return ($(el).prop("offsetHeight") + $(el).scrollTop() >= $(el).prop("scrollHeight"));
    },
    isPageScrollBarAtBottom: function() {
        return (($(window).height() + $(window).scrollTop()) >= $(document).height());
    }

};


