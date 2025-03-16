package model;

import java.text.DecimalFormat;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

@XmlType(propOrder = { "value", "currency" })
public class Amount {
	private double value;	
	private String currency="€";
	
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
	
	public Amount(double value, String currency) {
		super();
		this.value = value;
		this.currency = currency;
	}

	@XmlAttribute(name = "currency")
	public String getCurrency() { 
	    return currency;
	}

	@XmlValue
	public double getValue() {
		return value;
	}

	@Override
	public String toString() {
		return df.format(value) + currency;
	}
}
