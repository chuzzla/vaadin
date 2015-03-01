package com.mycompany.mavenproject1;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.data.Property;
import com.vaadin.data.util.converter.StringToIntegerConverter;
import com.vaadin.data.validator.IntegerRangeValidator;
import com.vaadin.event.FieldEvents;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;

/**
 *
 */
@Theme("mytheme")
@Widgetset("com.mycompany.mavenproject1.MyAppWidgetset")
public class MyUI extends UI {
    
    

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        final VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        setContent(layout);

        Button button = new Button("Click Me");
        button.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                //layout.addComponent(new Label("Thank you for clicking"));
            }
        });
        layout.addComponent(button);
        
        List<Cat> cats = new ArrayList<>();
        cats.add(new Cat(2, "Slavik"));
        cats.add(new Cat(1, "Sherlock"));
        cats.add(new Cat(3, "Kesha"));
        
        Table table = new Table("table title");
        table.addContainerProperty("Id", TextField.class, null);
        table.addContainerProperty("Name", String.class, null);
        table.setSortContainerPropertyId("Id");
        table.setSortAscending(true);
        
        for (Cat cat : cats) {
            TextField tf = new TextField("caption", String.valueOf(cat.getId()));
            tf.setConverter(new StringToIntegerConverter());
            tf.addValidator(new IntegerRangeValidator("integer only", Integer.MIN_VALUE, Integer.MAX_VALUE));
            
            tf.addShortcutListener(new ShortcutListener("enter", ShortcutAction.KeyCode.ENTER, null) {
                @Override
                public void handleAction(Object sender, Object target) {
                    System.out.println("ENTER by " + ((TextField)target).getValue());
                    //table.getIte
                }
            });
            
            table.addItem(new Object[]{tf, cat.getText()}, cat.getId());
        }

        table.setPageLength(table.size());
        table.sort(new Object[]{"Id"}, new boolean[]{true});
        layout.addComponent(table);
        

    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
