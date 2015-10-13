package de.saxsys.pitfalls.memoryleaks.listener;



import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
//This could be a view
public class LeakingView extends StackPane {
	
	
	private final Car dataModel;
	private final ChangeListener<String> carChangedListener;
	
	
	public LeakingView(Car dataModel) {
		this.dataModel = dataModel;
		//THIS CREATES THE LEAK - The anonymous implementation of the listener references the LeakingView object
		this.carChangedListener =  new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				//Do Something 
			}
		};
		
		getChildren().add(new Label(dataModel.name.get()));
		dataModel.name.addListener(carChangedListener);
	}
	
	
	public void removeListener() {
		dataModel.name.removeListener(carChangedListener);
	}
}
