package com.aplombee;

import org.apache.wicket.Application;
import org.apache.wicket.request.resource.PackageResourceReference;

/**
 * packageResourceReference to jquery
 *
 * @author Vineet Semwal
 *
 */
public class JqueryResourceReference extends PackageResourceReference {
    private static JqueryResourceReference instance=new JqueryResourceReference();
    private JqueryResourceReference(){
     super(JqueryResourceReference.class,"jquery-1.7.2.js");
    }
    public static JqueryResourceReference get(){
        return instance;
    }
}
