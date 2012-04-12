package com.orcl.controller;


import com.orcl.dao.ProductsDao;
import com.orcl.entity.*;
import com.orcl.logic.Apple;
import com.orcl.logic.Orange;
import com.orcl.logic.Pear;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.CustomEditorConfigurer;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;



import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.beans.PropertyEditorSupport;
import java.io.*;
import java.math.BigDecimal;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Controller
public class ProductsController {

    
    protected final int ITEMS_ON_PAGE_COUNT = 5;
    protected final Logger log = Logger.getLogger(ProductsController.class.getName());


    @Autowired
    protected ProductsDao dao;



    /*
    @InitBinder
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
        binder.registerCustomEditor(User.class, "user", new PropertyEditorSupport() {
            @Override
            public void setAsText(String string) {
                User t = new User();
                t.setId(Long.parseLong(string));
                setValue(t);
            }
        });

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        CustomDateEditor editor = new CustomDateEditor(dateFormat, true);
        binder.registerCustomEditor(Date.class, editor);
    }
    */

    @Autowired
    CustomEditorConfigurer cec;

    @Autowired
    private PropertyEditorRegistrar per;

    @InitBinder
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
        per.registerCustomEditors(binder);
    }

    // Fill
    @RequestMapping("products/fill.htm")
    public ModelAndView fill(@RequestParam(required=true,value="count") Integer count) {

        if(count<10) count = 10;

        User u = new User();
        u.setName(UUID.randomUUID().toString().substring(0, 8));
        u.setPerformance((int)(Math.random()*2000));
        u.setSalary((int)(Math.random()*10000));
        u = (User)dao.merge(u);

        while(count-->0) {
            Product p = new Product();
            double z = (int)(Math.random()*1000.)+10000*(int)(Math.random()*2.);
            p.setPrice((int)(z));
            p.setQuality((int) (z / (4 + 5 * Math.random())));
            p.setName(UUID.randomUUID().toString().substring(0, 15));
            p.setUser(u);
            p.setCreated(new Date());
            dao.merge(p);
        }

        ModelAndView mav = new ModelAndView("products/list");
        mav.addObject("rows", dao.getAll());

        return mav;
    }


    //List

    @RequestMapping("/products.htm")
    public ModelAndView list(@RequestParam(required=false,value="page") Integer page) {
        if(page == null) {
            page = 1;
        }
        /*
        User u = new User();
        u.setId(21);
        int i = 1000;
        while(i-->0) {
            Product p = new Product();
            double z = (int)(Math.random()*1000.)+10000*(int)(Math.random()*2.);
            p.setPrice(new BigDecimal(z));
            p.setQuality(new BigDecimal(z/(4+5*Math.random())));
            p.setName(UUID.randomUUID().toString().substring(0, 15));
            p.setUser(u);
            dao.merge(p);
        }
        */

        ModelAndView mav = new ModelAndView("products/list");
        //ModelAndView mav = new ModelAndView("uncaughtException");
        mav.addObject("rows", dao.getAll());

        return mav;
    }

    //Create

    @RequestMapping(value = "products/create.htm", method = RequestMethod.GET)
    public ModelAndView createDoGet(ModelMap model) {
        Product row= new Product();
        return createMAV(row, model);
    }

    @RequestMapping(value = "products/create.htm", method = RequestMethod.POST)
    public ModelAndView createDoPost(@ModelAttribute("feed") @Valid Product row, BindingResult bindingResult, ModelMap model) {
        if(!bindingResult.hasErrors()){
            row.setPrice(row.getQuality()*7);
            Product newRow = (Product)dao.merge(row);
            dao.refresh(newRow);
            return new ModelAndView("redirect:/products/" + newRow.getId() + ".htm");
        }
        return createMAV(row, model);
    }

    protected ModelAndView createMAV(Product row, ModelMap model) {
        ModelAndView mav = new ModelAndView("products/create");
        model.addAttribute("row", row);
        model.addAttribute("users", dao.getAllUsers());
        return mav;
    }


    //private @Autowired HttpServletRequest request;

    //todo: @RequestPart for files
    //Edit
    @RequestMapping(value = "products/{id}.htm", method = RequestMethod.POST)
    public ModelAndView editDoPost(
            @ModelAttribute("row") @Valid Product row,
            BindingResult bindingResult,
            ModelMap model,
            @RequestParam(required=false,value="prop") List<String> props,
            @RequestParam(required=false,value="dummy") Object dummy,
            @RequestParam(required=false,value="picture") CommonsMultipartFile fileData,
            HttpServletRequest request
            ) {
        /*
        System.out.println("############");
        System.out.println(fileData.getName());
        System.out.println(fileData.getContentType());
        System.out.println(fileData.getSize());

        /*
        System.out.println("############");
        System.out.println(props);
        System.out.println(dummy);
        System.out.println(request.getParameter("prop"));
        System.out.println(request.getParameterMap());
        System.out.println(request.getParameter("dummy"));
        */
        //DecimalFormat myFormatter = new DecimalFormat("prop[]");


        if(!bindingResult.hasErrors()){

            if(!fileData.isEmpty()) {
                File f = getFile(row.getId());
                if(f.isFile()){
                    f.delete();
                }
                try {
                    fileData.transferTo(f);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                f.setWritable(true);
            }
            /*
            Product oldRow = dao.findById(row.getId());


            oldFeed.setName(feed.getName());
            oldFeed.setUrl(feed.getUrl());
            oldFeed.setTimeout(feed.getTimeout());
            oldFeed.setLastUpdate(feed.getLastUpdate());
            feed = feedsDAO.merge(oldFeed);
            (/


            if(feedsSheduleManager!=null) {
                feedsSheduleManager.refreshFeedTimeoutTriggers();
            }
            */

            Product oldRow = dao.findById(row.getId());

            dao.transactionalUpdate(row.getId(), row);
            /*
            oldRow.setName(row.getName());
            oldRow.setUser(row.getUser());
            oldRow.setQuality(row.getQuality());
            oldRow.setPrice(row.getPrice());
            oldRow.setCreated(row.getCreated());

            row = (Product)dao.merge(oldRow);
            */

            dao.clearTextsOfProduct(oldRow.getId());
            dao.flush();

            Pattern p = Pattern.compile("(prop_value|prop_variant)\\[(\\d+)\\]");
            for(String key: (Set<String>)request.getParameterMap().keySet()) {
                Matcher m = p.matcher(key);
                if(!m.find()) continue;

                boolean isTextValue = "prop_value".equals(m.group(1));
                int paramId = Integer.parseInt(m.group(2));
                if(paramId<1) continue;

                String value = request.getParameter(key);
                if("".equals(value)) continue;

                Property prop = new Property();
                prop.setId(paramId);
                //
                Text newText = new Text();
                newText.setProduct(row);
                newText.setProperty(prop);
                if(isTextValue) {
                    newText.setValue(value);
                } else {

                    Long variantId = Long.parseLong(value);

                    newText.setValue(null);
               
                    Variant variant = new Variant();
                    variant.setId(variantId);

                    newText.setVariant(variant);
                    /*
                    dao.refresh(variant);
                    newText.setValue(variant.getValue());
                    */
                }    
                dao.saveOrUpdate(newText);
            }

        }
        return editMAV(row, model);
    }

    @RequestMapping(value = "products/{id}.htm", method = RequestMethod.GET)
    public ModelAndView editDoGet(ModelMap model, @PathVariable int id, @RequestBody String reqBody) {
        
        log.info(reqBody);
        
        Product row = dao.findById(id);

        if(row == null) {
            throw new RuntimeException();
        }
        return editMAV(row, model);
    }

    @Autowired 
    Apple apple;
    
    @Autowired
    @Qualifier("blue")
    Pear pear;
    
    protected ModelAndView editMAV(Product row, ModelMap model) {
        long id1 = row.getId();

        dao.formProps(id1);

        ModelAndView mav = new ModelAndView("products/edit");
        model.addAttribute("row", row);
        model.addAttribute("propsHibernate", dao.getAllTexts(id1));
        model.addAttribute("propsNativeSql", dao.getAllTextsNativeSql(id1));
        model.addAttribute("props", dao.formProps(id1));
        model.addAttribute("extendedVariants", dao.formVariants(id1));
        model.addAttribute("determinant", apple.getColor()/*dao.getDeterminant(id1)*/);
        model.addAttribute("users", dao.getAllUsers());
        return mav;
    }

    @Autowired
    ResourceLoader rl;

    @Autowired
    Orange orange;
    //test
    @RequestMapping("test.htm")
    public void doTest(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws Exception {
        //UrlResource page = new UrlResource("http://ya.ru");

        Resource page = orange.getSource();
        //copy(page.getInputStream(), response.getOutputStream());
        Resource x = rl.getResource("http://ya.ru");
        copy(x.getInputStream(), response.getOutputStream());
    }

    //pic
    @RequestMapping(value = "products/pic/{id}", method = RequestMethod.GET)
    public void doPic(
            @PathVariable int id,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        File pic = getFile(id);
        if(!pic.isFile()) {
            pic = getFile("none.gif");
        }

        InputStream is = null;
        try {
            is = new FileInputStream(pic);
            copy(is, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(is!=null) try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static final int IO_BUFFER_SIZE = 4 * 1024;

    private static void copy(InputStream in, OutputStream out) throws IOException {
        byte[] b = new byte[IO_BUFFER_SIZE];
        int read;
        while ((read = in.read(b)) != -1) {
            out.write(b, 0, read);
        }
    }

    protected File getFile(long node) {
        return getFile(Long.toString(node));
    }
    
    protected File  getFile(String node) {
        return new File("C:/!trash/pics/"+node);
    }
    
}
