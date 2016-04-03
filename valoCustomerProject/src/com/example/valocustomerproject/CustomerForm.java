package com.example.valocustomerproject;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

public class CustomerForm extends FormLayout {
	private TextField firstName = new TextField();
	private TextField lastName = new TextField();
	private TextField email = new TextField("Email");
	private NativeSelect status = new NativeSelect("Status");
	private PopupDateField birthDate = new PopupDateField("DOB");
	private Button clear = new Button("clear");

	private CustomerService service = CustomerService.getInstance();
	private Customer customer;
	private ValocustomerprojectUI myui;

	public CustomerForm(ValocustomerprojectUI ui) {
		this.myui = ui;
		status.addItems(CustomerStatus.values());
		firstName.setInputPrompt("First Name");
		lastName.setInputPrompt("Last Name");
		setSizeUndefined();

		HorizontalLayout names = new HorizontalLayout();
		HorizontalLayout buttons = new HorizontalLayout();

		names.addComponents(firstName, lastName);
		names.setCaption("Name");
		buttons.addComponents(clear);

		names.setSpacing(true);
		buttons.setSpacing(true);
		
		clear.setClickShortcut(KeyCode.ENTER);
		clear.setStyleName(ValoTheme.BUTTON_FRIENDLY);

		clear.addClickListener(e -> clearField());
		
		names.setEnabled(false);
		email.setEnabled(false);
		status.setEnabled(false);
		birthDate.setEnabled(false);

		addComponents(names, email, status, birthDate, buttons);
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
		BeanFieldGroup.bindFieldsUnbuffered(customer, this);

		// enable delete button for only customers already in the database
		if (customer.isPersisted()) {
			clear.setEnabled(true);
		} else {
			clear.setEnabled(false);
		}

		setEnabled(true);
	}

	public void clearField() {
		firstName.clear();
		lastName.clear();
		email.clear();
		status.clear();
		birthDate.clear();
	}
}