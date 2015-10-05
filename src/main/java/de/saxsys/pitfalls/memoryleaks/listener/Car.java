package de.saxsys.pitfalls.memoryleaks.listener;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Car {
	// Normally you have setters / getters
	StringProperty name = new SimpleStringProperty("Audi");
}
