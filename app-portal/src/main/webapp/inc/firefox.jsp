<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<script>
/**
 * 处理firefox没有outerHTML属性的问题
 */
if(window.HTMLElement) {  
    HTMLElement.prototype.__defineSetter__("outerHTML",function(sHTML){  
        var r=this.ownerDocument.createRange();  
        r.setStartBefore(this);  
        var df=r.createContextualFragment(sHTML);  
        this.parentNode.replaceChild(df,this);  
        return sHTML;  
        });  
    HTMLElement.prototype.__defineGetter__("outerHTML",function(){  
     var attr;  
        var attrs=this.attributes;  
        var str="<"+this.tagName.toLowerCase();  
        for(var i=0;i<attrs.length;i++){  
            attr=attrs[i];  
            if(attr.specified)  
                str+=" "+attr.name+'="'+attr.value+'"';  
            }  
        if(!this.canHaveChildren)  
            return str+">";  
        return str+">"+this.innerHTML+"</"+this.tagName.toLowerCase()+">";  
        });  
         
 HTMLElement.prototype.__defineGetter__("canHaveChildren",function(){  
  switch(this.tagName.toLowerCase()){  
            case "area":  
            case "base":  
         case "basefont":  
            case "col":  
            case "frame":  
            case "hr":  
            case "img":  
            case "br":  
            case "input":  
            case "isindex":  
            case "link":  
            case "meta":  
            case "param":  
            return false;  
        }  
        return true;  
     });  
}  
</script>