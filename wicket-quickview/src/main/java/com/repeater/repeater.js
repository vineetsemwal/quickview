/* 
 *  created by Vineet Semwal
 */



function removeItem(id){
   $("#"+id).remove();
}

function insertAfter(tag,theId,parentContainerId){
        var item=document.createElement(tag);
        item.id=theId;    
        $("#"+parentContainerId).append(item);
}

function insertBefore(tag,theId,parentContainerId){
    var item=document.createElement(tag);
    item.id=theId;
     $("#"+parentContainerId).prepend(item);
}

