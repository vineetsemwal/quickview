package com.aplombee;

import org.apache.wicket.Application;
import org.apache.wicket.request.resource.PackageResourceReference;

/**
 * packageResourceReference to compressed jquery
 *
 * @author Vineet Semwal
 *
 */
public class JqueryCompressedReference extends PackageResourceReference {
    private static JqueryCompressedReference instance=new JqueryCompressedReference();
    private JqueryCompressedReference(){
        super(JqueryResourceReference.class,"jquery-1.7.2.min.js");
    }
    public static JqueryCompressedReference get(){
        return instance;
    }
}
