package model;

import java.text.DecimalFormat;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = { "value", "currency" })
public class Amount {
	private double value;	
	private final String currency="€";
	
	private static final DecimalFormat df = new DecimalFormat("0.00");
	
	public void setValue(double value) {
		this.value = value;
	}
	
	public Amount() {
	}
	
	public Amount(double value) {
		super();
		this.value = value;
	}

	@XmlAttribute(name = "currency")
	public String getCurrency() { // Para ver qué valor se está devolviendo
	    return currency;
	}


	public double getValue() {
		return value;
	}

	@Override
	public String toString() {
		return df.format(value) + currency;
	}
}
