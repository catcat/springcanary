package com.orcl.editors;

import com.orcl.entity.User;
import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.beans.propertyeditors.CustomDateEditor;

import java.beans.PropertyEditorSupport;
import java.text.SimpleDateFormat;
import java.util.Date;


public class CustomRegistrar implements PropertyEditorRegistrar {
    @Override
    public void registerCustomEditors(PropertyEditorRegistry registry) {
        registry.registerCustomEditor(User.class, "user", new PropertyEditorSupport() {
            @Override
            public void setAsText(String string) {
                User t = new User();
                t.setId(Long.parseLong(string));
                setValue(t);
            }
        });
        /*
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        CustomDateEditor editor = new CustomDateEditor(dateFormat, true);
        registry.registerCustomEditor(Date.class, editor);
        */
    }
}
