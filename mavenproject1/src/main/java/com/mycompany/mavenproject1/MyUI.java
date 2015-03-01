package com.mycompany.mavenproject1;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import javax.servlet.annotation.WebServlet;

/**
 *
 */
@Theme("mytheme")
@Widgetset("com.mycompany.mavenproject1.MyAppWidgetset")
public class MyUI extends UI {
    
    private Table catList = new Table();
    private FormLayout editorLayout = new FormLayout();
    private FieldGroup editorFields = new FieldGroup();
    
    IndexedContainer contactContainer = createDummyDatasource();
    private static final String NUMBER = "Number";
    private static final String NAME = "Name";
    private static final String ART = "Art";
    private static final String[] fieldNames = new String[] { NUMBER, NAME, ART,
                        "Weight", "Toy", "Besitzer" };

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        
        
        initLayout();
        initCatList();

    }
    
     private void initLayout() {

                HorizontalSplitPanel splitPanel = new HorizontalSplitPanel();
                setContent(splitPanel);

                VerticalLayout leftLayout = new VerticalLayout();
                splitPanel.addComponent(leftLayout);
                splitPanel.addComponent(editorLayout);
                leftLayout.addComponent(catList);

                leftLayout.setSizeFull();

                leftLayout.setExpandRatio(catList, 1);
                catList.setSizeFull();

                editorLayout.setMargin(true);
                editorLayout.setVisible(false);
        }
    
    private void initCatList() {
                catList.setContainerDataSource(contactContainer);
                catList.setVisibleColumns(new String[] { NUMBER, NAME, ART});
                catList.setSelectable(true);
                catList.setImmediate(true);

                catList.addValueChangeListener(new Property.ValueChangeListener() {
                        public void valueChange(ValueChangeEvent event) {
                                Object contactId = catList.getValue();
                                                       if (contactId != null)
                                        editorFields.setItemDataSource(catList
                                                        .getItem(contactId));

                                editorLayout.setVisible(contactId != null);
                        }
                });
    }

    private IndexedContainer createDummyDatasource() {
            IndexedContainer ic = new IndexedContainer();

                for (String p : fieldNames) {
                        ic.addContainerProperty(p, String.class, "");
                }

                String[] names = { "Kesha", "Sherlock", "Savik", "Goshik", "Ljustrik" };
                String[] arts = { "Cat", "Cow", "Elefant", "Sneak", "Girl" };
                for (int i = 0; i < 5; i++) {
                        Object id = ic.addItem();
                        ic.getContainerProperty(id, NUMBER).setValue("1");
                        ic.getContainerProperty(id, NAME).setValue(names[(int) (names.length * Math.random())]);
                        ic.getContainerProperty(id, ART).setValue(
                                        arts[(int) (arts.length * Math.random())]);
                }

                return ic;
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
