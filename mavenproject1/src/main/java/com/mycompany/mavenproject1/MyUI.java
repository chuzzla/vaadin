package com.mycompany.mavenproject1;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.FieldEvents;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    private static final String[] fieldNames = new String[]{NUMBER, NAME, ART,
        "Weight", "Toy", "Besitzer"};
    
    private static String oldValue = "";

    @Override
    protected void init(VaadinRequest vaadinRequest) {

        initLayout();
        initCatList();
        initEditor();

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

    private void initEditor() {

        for (String fieldName : fieldNames) {
            TextField field = new TextField(fieldName);
            editorLayout.addComponent(field);
            field.setWidth("100%");
            field.addShortcutListener(new ShortcutListener("enter", ShortcutListener.KeyCode.ENTER, null) {

                @Override
                public void handleAction(Object sender, Object target) {
                    try {
                        String old = oldValue;
                        String newObj = ((TextField) target).getValue();
                        changeOrder(Integer.valueOf(old), Integer.valueOf(newObj));
                        editorFields.commit();
                        catList.sort();
                    } catch (FieldGroup.CommitException ex) {
                        Logger.getLogger(MyUI.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                private void changeOrder(Integer old, Integer newObj) {
                    if (old < newObj) {
                        boolean first = true;
                        for (Integer item : (Collection<Integer>) catList.getItemIds()) {
                            Integer val = Integer.valueOf((String) catList.getItem(item).getItemProperty(NUMBER).getValue());
                            if (val == newObj && first) {
                                first = false;
                                continue;
                            }
                            if (val <= newObj && val > old) {
                                catList.getItem(item).getItemProperty(NUMBER).setValue(String.valueOf(val - 1));
                            }
                            if (val == old) {
                                catList.getItem(item).getItemProperty(NUMBER).setValue(String.valueOf(newObj));
                            }
                        }
                    } else if (old > newObj) {
                        boolean first = true;
                        for (Integer item : (Collection<Integer>) catList.getItemIds()) {
                            Integer val = Integer.valueOf((String) catList.getItem(item).getItemProperty(NUMBER).getValue());
                            if (val == newObj && first) {
                                first = false;
                                catList.getItem(item).getItemProperty(NUMBER).setValue(String.valueOf(val + 1));                                
                                continue;
                            }
                            if (val < old && val > newObj) {
                                catList.getItem(item).getItemProperty(NUMBER).setValue(String.valueOf(val + 1));
                            }
                        }
                    }
                }
            });
            

            editorFields.bind(field, fieldName);
        }

        editorFields.setBuffered(false);
    }

    private void initCatList() {
        catList.setContainerDataSource(contactContainer);
        catList.setVisibleColumns(new String[]{NUMBER, NAME, ART});
        catList.setSelectable(true);
        catList.setImmediate(true);

        catList.setSortContainerPropertyId(NUMBER);

        catList.addValueChangeListener(new Property.ValueChangeListener() {
            public void valueChange(ValueChangeEvent event) {
                Object contactId = catList.getValue();
                if (contactId != null) {
                    editorFields.setItemDataSource(catList
                            .getItem(contactId));
                }
                oldValue = (String)catList.getItem(catList.getValue()).getItemProperty(NUMBER).getValue();
                editorLayout.setVisible(contactId != null);
            }
        });
    }

    private IndexedContainer createDummyDatasource() {
        IndexedContainer ic = new IndexedContainer();

        for (String p : fieldNames) {
            ic.addContainerProperty(p, String.class, "");
        }

        String[] names = {"Kesha", "Sherlock", "Savik", "Goshik", "Ljustrik"};
        String[] arts = {"Cat", "Cow", "Elefant", "Sneak", "Girl"};
        for (int i = 0; i < 5; i++) {
            Object id = ic.addItem();
            ic.getContainerProperty(id, NUMBER).setValue(String.valueOf(i + 1));
            ic.getContainerProperty(id, NAME).setValue(names[i]);
            ic.getContainerProperty(id, ART).setValue(
                    arts[i]);
        }

        return ic;
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
