package com.orcl.webservice;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.orcl.dao.ProductsDao;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.xpath.XPath;


@Endpoint
public class MyEndpoint {

    private static final String NAMESPACE_URI = "http://example.com/myep/schemas";

    private XPath priceExpression, dateExpression, messageExpression;

    private ProductsDao dao;
    private SimpleDateFormat dateFormat;

    @Autowired
    public void setDao(ProductsDao dao) throws JDOMException {
        this.dao = dao;

        Namespace namespace = Namespace.getNamespace("myep", NAMESPACE_URI);

        priceExpression = XPath.newInstance("//myep:Price");
        priceExpression.addNamespace(namespace);

        messageExpression = XPath.newInstance("//myep:Message");
        messageExpression.addNamespace(namespace);

        dateExpression = XPath.newInstance("//myep:Date");
        dateExpression.addNamespace(namespace);

        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println("READY");
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "ProductRequest")
    public void handleHolidayRequest(@RequestPayload Element productRequest)
            throws Exception {

        Date date = dateFormat.parse(dateExpression.valueOf(productRequest));
        Integer price = Integer.parseInt(priceExpression.valueOf(productRequest));
        String message = messageExpression.valueOf(productRequest);
        
        System.out.println("GOT IT:"+date+" "+price+" "+message);
    }
}
