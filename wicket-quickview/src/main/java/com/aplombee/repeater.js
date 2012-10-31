/* 
 *  created by Vineet Semwal
 */



function removeItem(id) {
    $("#" + id).remove();
}

function insertAfter(tag, theId, parentContainerId) {
    var item = document.createElement(tag);
    item.id = theId;
    $("#" + parentContainerId).append(item);
}

function insertBefore(tag, theId, parentContainerId) {
    var item = document.createElement(tag);
    item.id = theId;
    $("#" + parentContainerId).prepend(item);
}

function scrollToBottom(id)
{
    var element = $("#" + id);
    scrollTo(id, $(element).height());
}

function scrollToTop(id) {
    scrollTo(id, 0);
}

function scrollTo(id, height) {
    var element = $("#" + id);
    $(element).scrollTop(height);
}


function isComponentScrollBarAtBottom(id) {
    var el = $("#" + id);
    if ($(el).prop("offsetHeight") + $(el).scrollTop() >= $(el).prop("scrollHeight")) {
        return true;
    }
    else {
        return false;
    }
}

function isPageScrollBarAtBottom() {
    if (($(window).height() + $(window).scrollTop()) >= $(document).height()) {
        return true;
    }
    else {
        return false;
    }
}


