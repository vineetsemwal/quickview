/* 
 *  created by heapifyman
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


function scrollToBottom(id)
{
 var element=document.getElementById(id);
 var height=getDocumentHeight();
 scrollTo(id,height);
}

function scrollToTop(id){
 scrollTo(id,0);
}

function scrollTo(id,height){
 var element=document.getElementById(id);
 element.scrollTop =height;
}

function getDocumentHeight() {
    return Math.max(
        Math.max(document.body.scrollHeight, document.documentElement.scrollHeight),
        Math.max(document.body.offsetHeight, document.documentElement.offsetHeight),
        Math.max(document.body.clientHeight, document.documentElement.clientHeight)
    );
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


function isComponentScrollBarAtBottom(id){
       var el=document.getElementById(id);
    if (el.offsetHeight + el.scrollTop >= el.scrollHeight) {
       return true;
    }
    else{
    return false;
    }
}

function getInnerHeight(){
    if(window.innerHeight!= undefined){
        return  window.innerHeight;
    }
    else{
     return Math.max(document.documentElement.clientHeight, document.body.clientHeight); //for IE
    }
}

function getScrollTop(){
if(window.pageYOffset!=undefined){
return window.pageYOffset;
}else{
return document.body.scrollTop;    //for IE
}
}

function isPageScrollBarAtBottom(){
    var docHeight=getDocumentHeight();
    var iHeight=getInnerHeight();
    var sTop=getScrollTop();
   if ((iHeight + sTop) >=docHeight )  {
      return true;
       }
     else{
      return false;
      }

}

