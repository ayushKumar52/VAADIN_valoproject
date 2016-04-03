package com.example.valocustomerproject;

import java.util.List;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.Sizeable;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Table;
import com.vaadin.ui.Tree;
import com.vaadin.ui.Tree.TreeDragMode;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@Theme("valocustomerproject")
public class ValocustomerprojectUI extends UI {

	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = ValocustomerprojectUI.class)
	public static class Servlet extends VaadinServlet {
	}

	private CustomerService service;
	private CustomerForm form;
	private Tree menuTree;
	private Table myTable;
	
	private Label name;
	private Label email;
	private Label status;
	private Label DOB;

	@Override
	protected void init(VaadinRequest request) {

		// some initialisations
		service = CustomerService.getInstance();
		form = new CustomerForm(this);
		menuTree = new Tree();
		myTable = new Table();
		
		name = new Label();
		email = new Label();
		status = new Label();
		DOB = new Label();
		
		
		// H-split panel in the UI
		HorizontalSplitPanel splitUI = new HorizontalSplitPanel();
		splitUI.setSplitPosition(20, Sizeable.UNITS_PERCENTAGE);

		VerticalLayout menuField = new VerticalLayout();
		VerticalLayout mainField = new VerticalLayout();

		splitUI.setFirstComponent(menuField); // left panel
		splitUI.setSecondComponent(mainField); // right panel
		
		splitUI.setLocked(true);
		setContent(splitUI);

		// left panel components
		menuField.setMargin(true);
		menuField.setSpacing(true);

		Label menuLabel = new Label("Menu");
		menuLabel.setStyleName("h2");

		updateList();
		menuTree.setSelectable(true);
		menuTree.setMultiSelect(false);
		menuTree.setResponsive(true);
		menuTree.setDragMode(TreeDragMode.NODE);
		
		menuField.addComponents(menuLabel, menuTree);
		
		

		// right panel components
		mainField.setMargin(true);
		mainField.setSpacing(true);

		Label mainLabel = new Label("Customers");
		mainLabel.setStyleName("h2");

		TabSheet tab = new TabSheet();

		VerticalLayout tab1 = new VerticalLayout();
		VerticalLayout tab2 = new VerticalLayout();

		tab.addTab(tab1,"Profile");
		tab.addTab(tab2,"New");

		// tab1 components
		tab1.setCaption("Profile");
		tab1.setMargin(true);
		tab1.setSpacing(true);
		tab1.setSizeFull();

		Panel p1 = new Panel("Customer Profile");
		
		HorizontalLayout profile = new HorizontalLayout();
		
		Label nameLabel = new Label("Name:  ");
		Label emailLabel = new Label("Email:  ");
		Label statusLabel = new Label("Status:  ");
		Label DOBlabel = new Label("DOB:  ");
		
		HorizontalLayout row1 = new HorizontalLayout(nameLabel,name);
		HorizontalLayout row2 = new HorizontalLayout(emailLabel,email);
		HorizontalLayout row3 = new HorizontalLayout(statusLabel,status);
		HorizontalLayout row4 = new HorizontalLayout(DOBlabel,DOB);
		
		row1.setSpacing(true);
		row2.setSpacing(true);
		row3.setSpacing(true);
		row4.setSpacing(true);
		
		VerticalLayout detailField = new VerticalLayout(row1, row2, row3, row4);
		detailField.setMargin(true);
		detailField.setSpacing(true);
		detailField.setSizeFull();
		
		Label iconField = new Label();
		iconField.setIcon(new ThemeResource("img/account_and_control.png"));
		iconField.setSizeFull();
		
		profile.addComponents(detailField,iconField);
		profile.setMargin(true);
		profile.setSpacing(true);
		profile.setComponentAlignment(iconField, Alignment.MIDDLE_RIGHT);
		profile.setSizeFull();
		p1.setContent(profile);
		
		
		

		Panel p2 = new Panel("Other Customers");
		myTable.setSelectable(true);
        myTable.setMultiSelect(false);
        myTable.setColumnCollapsingAllowed(true);
        myTable.setColumnReorderingAllowed(true);
        myTable.setSizeFull();
        myTable.setVisibleColumns("firstName","lastName","email","status","birthDate");
		p2.setContent(myTable);
		
		tab1.addComponents(p1,p2);
		
		
		// tab2 components
		tab2.setCaption("New");
		tab2.setMargin(true);
		tab2.setSpacing(true);
		tab2.setSizeFull();
		
		tab2.addComponent(form);
		form.setMargin(true);
		form.setSpacing(true);
		form.setSizeFull();
		
		mainField.addComponents(mainLabel, tab);
		
		//action listeners...
		
		menuTree.addValueChangeListener(e -> {       // tree change-value listener
			Customer customer = service.findAll(e.getProperty().getValue().toString().replace("[", "").replace("]", "")).get(0);
			form.setCustomer(customer);
			
			menuTree.select(customer);
			
			name.setValue(customer.getFirstName()+" "+customer.getLastName());
			email.setValue(customer.getEmail());
			status.setValue(customer.getStatus().toString());
			DOB.setValue(customer.getBirthDate().toGMTString());
			
			myTable.select(customer);
		});
		
		myTable.addValueChangeListener(e -> {
			Customer customer = service.findAll(e.getProperty().getValue().toString().replace("[", "").replace("]", "")).get(0);
			form.setCustomer(customer);
			
			menuTree.select(customer);
			
			name.setValue(customer.getFirstName()+" "+customer.getLastName());
			email.setValue(customer.getEmail());
			status.setValue(customer.getStatus().toString());
			DOB.setValue(customer.getBirthDate().toGMTString());
			
			myTable.select(customer);
		});
	}

	public void updateList() {
		List<Customer> customerList = service.findAll();
		Customer customer = customerList.iterator().next();
		form.setCustomer(customer);
		
		name.setValue(customer.getFirstName()+" "+customer.getLastName());
		email.setValue(customer.getEmail());
		status.setValue(customer.getStatus().toString());
		DOB.setValue(customer.getBirthDate().toGMTString());
		
		
		menuTree.setContainerDataSource(new BeanItemContainer<>(Customer.class, customerList));
		menuTree.select(customer);
		
		myTable.setContainerDataSource(new BeanItemContainer<>(Customer.class, customerList));
		myTable.select(customer);
	}
}