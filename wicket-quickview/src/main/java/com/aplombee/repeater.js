/* 
 *  created by Vineet Semwal
 */


var QuickView={

removeItem : function(id) {
    $("#" + id).remove();
},

insertAfter :function(tag, theId, parentContainerId) {
    var item = QuickView.createItem(tag, theId);
    $("#" + parentContainerId).append(item);
},
        
insertBefore :function(tag, theId, parentContainerId) {
    var item = QuickView.createItem(tag, theId);
    $("#" + parentContainerId).prepend(item);
},

createItem :function(tag, theId) {
    return $("<" + tag + ">").attr("id", theId);
},

scrollToBottom : function(id)
{
    var element = $("#" + id);
    scrollTo(id, $(element).height());
},

scrollToTop : function(id) {
    scrollTo(id, 0);
} ,

scrollTo :function(id, height) {
    var element = $("#" + id);
    $(element).scrollTop(height);
},


isComponentScrollBarAtBottom : function(id) {
    var el = $("#" + id);
    return ($(el).prop("offsetHeight") + $(el).scrollTop() >= $(el).prop("scrollHeight"));
},

isPageScrollBarAtBottom :function() {
    return (($(window).height() + $(window).scrollTop()) >= $(document).height());
}

};


