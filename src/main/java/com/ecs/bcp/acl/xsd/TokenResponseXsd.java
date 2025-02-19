package com.ecs.bcp.acl.xsd;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "RESPONSE")
public class TokenResponseXsd {

    @Element(name = "RESULT")
    private Result result;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

}


