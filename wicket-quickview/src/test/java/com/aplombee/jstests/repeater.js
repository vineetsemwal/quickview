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
    /**
     creates a new dom element with tag and id provided in parameters
     */
    createItem: function(tag, theId) {
        return $("<" + tag + ">").prop("id", theId);
    },
            
    /**
     * removes element with 'id' of parent with id 'parentId'
     */        
    removeItem: function(id,parentId) {
        $("#" + parentId).children().remove("#"+id);
    },
    /**
     creates a new dom tag element as the last element of  dome element with id  parentContainerId
     */
    append: function(tag, theId, parentContainerId) {
        var item = QuickView.createItem(tag, theId);
        $("#" + parentContainerId).append(item);
    },
    /**
     creates a new dom tag element as the first element of element with  parentContainerId
     */
    prepend: function(tag, theId, parentContainerId) {
        var item = QuickView.createItem(tag, theId);
        $("#" + parentContainerId).prepend(item);
    },
    /**
     position the scrollbar to bottom
     */
    scrollToBottom: function(id)
    {
        var element = $("#" + id);
        scrollTo(id, $(element).height());
    },
    /**
     position the scrollbar to top
     */
    scrollToTop: function(id) {
        scrollTo(id, 0);
    },
    /**
     position the scrollbar to provided height
     */
    scrollTo: function(id, height) {
        var element = $("#" + id);
        $(element).scrollTop(height);
    },
    /**
     returns true if the scrollbar of element with parameter id is moved at the bottom else it returns false
     */
    isComponentScrollBarAtBottom: function(id) {
        var el = $("#" + id);
        // offsetheight : element height	
        // scrolltop : height of scroll from top
        // scroll-height:height of the scroll view of an element
        return ($(el).prop("offsetHeight") + $(el).scrollTop() >= $(el).prop("scrollHeight"));
    },
    /**
     returns true if the scrollbar of the page is moved at the bottom else it returns false
     */
    isPageScrollBarAtBottom: function() {
        return (($(window).height() + $(window).scrollTop()) >= $(document).height());
    }

};

