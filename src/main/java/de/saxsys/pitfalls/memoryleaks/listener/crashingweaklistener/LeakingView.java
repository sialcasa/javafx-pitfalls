package de.saxsys.pitfalls.memoryleaks.listener.crashingweaklistener;



import javafx.beans.value.ChangeListener;
import javafx.beans.value.WeakChangeListener;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import de.saxsys.pitfalls.memoryleaks.listener.util.Car;

//This could be a view
public class LeakingView extends StackPane {
	
	
	private final Car car;
	private final ChangeListener<String> carChangedListener;
	
	private final Label carLabel = new Label();
	
	public LeakingView(Car car) {
		this.car = car;
		
		carLabel.textProperty().bind(car.name);
		
		// Leaking - (If you use lambdas instead of anonymous classes you have to reference a class member from the
		// lambda, otherwise the compile will optimize the lambda to a static method and a leak is avoided)
		this.carChangedListener = (observable, oldValue, newValue) -> {
			
			if (newValue.equals("remove")) {
				((VBox) getParent()).getChildren().remove(this);
			}
			System.out.println(getScene().toString());
		};
		
		car.name.addListener(new WeakChangeListener<>(carChangedListener));
		
		getChildren().add(carLabel);
	}
	
}
