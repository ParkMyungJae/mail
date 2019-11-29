package domain;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

public class TableVO {
	private IntegerProperty id;
	private StringProperty owner;
	private StringProperty title;
	private StringProperty date;

	public TableVO(IntegerProperty id, StringProperty owner, StringProperty title, StringProperty date) {
		this.id = id;
		this.owner = owner;
		this.title = title;
		this.date = date;
	}
	
	public IntegerProperty idProperty() {
		return id;
	}

	public StringProperty ownerProperty() {
		return owner;
	}

	public StringProperty titleProperty() {
		return title;
	}

	public StringProperty dateProperty() {
		return date;
	}
}
