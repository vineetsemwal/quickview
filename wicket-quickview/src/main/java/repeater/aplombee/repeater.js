/* 
 *  created by Vineet Semwal
 */



function removeItem(id){
	var child = Wicket.$(id);
	child.parentElement.removeChild(child);
}

function insertAfter(tag,theId,parentContainerId){
        var item=document.createElement(tag);
        item.id=theId;
        Wicket.$(parentContainerId).appendChild(item);
}

function insertBefore(tag,theId,parentContainerId){
    var item=document.createElement(tag);
    item.id=theId;
    var parentContainer = Wicket.$(parentContainerId);
    var firstChild = parentContainer.firstChild;
    if (firstChild) {
		parentContainer.insertBefore(item, firstChild);
	} else {
		parentContainer.appendChild(item);
	}
}

