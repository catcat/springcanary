package com.orcl.webservice;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.orcl.dao.ProductsDao;
import org.jdom.Document;
import org.jdom.output.XMLOutputter;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.xpath.XPath;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;


@Endpoint
public class MyEndpoint {

    private static final String NAMESPACE_URI = "http://example.com/myep/schemas";
    private Namespace namespace;

    private XPath priceExpression, dateExpression, messageExpression;

    private ProductsDao dao;
    private SimpleDateFormat dateFormat;

    @Autowired
    public void setDao(ProductsDao dao) throws JDOMException {
        this.dao = dao;

        namespace = Namespace.getNamespace("myep", NAMESPACE_URI);

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
    @ResponsePayload
    public Element handleProductRequest(@RequestPayload Element productRequest)
            throws Exception {
        System.out.println(new XMLOutputter().outputString(productRequest));

        Date date = dateFormat.parse(dateExpression.valueOf(productRequest));
        Integer price = Integer.parseInt(priceExpression.valueOf(productRequest));
        String message = messageExpression.valueOf(productRequest);
        
        System.out.println("GOT IT:" + date + " " + price + " " + message);

        return createFakeResponse();
    }

    protected Element createFakeResponse() {
        Element resp = new Element("ProductResponse", namespace);

        Document myDocument = new Document(resp);


        Element id = new Element("id", namespace);
        id.setText("123");
        Element name = new Element("ProductName", namespace);
        name.setText("Lol "+ Math.random()*10000);
        Element details = new Element("Details", namespace);
        details.setText("Something about the product");

        resp
            .addContent(id)
            .addContent(name)
            .addContent(details);
        
        System.out.println(new XMLOutputter().outputString(resp));

        return resp;
    }
}
