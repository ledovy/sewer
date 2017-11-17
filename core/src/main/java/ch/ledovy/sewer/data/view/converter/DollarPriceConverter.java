package ch.ledovy.sewer.data.view.converter;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

import com.vaadin.data.Result;
import com.vaadin.data.ValueContext;
import com.vaadin.data.converter.StringToBigDecimalConverter;
import com.vaadin.data.converter.StringToDoubleConverter;
import com.vaadin.spring.annotation.SpringComponent;

@SpringComponent
public class DollarPriceConverter extends StringToBigDecimalConverter {
	
	private static final String		ERROR_MSG			= "Invalid prices, please re-check the value";
	private StringToDoubleConverter	doubleConverter		= new StringToDoubleConverter(ERROR_MSG);
	private StringToDoubleConverter	currencyConverter	= new StringToDoubleConverter(ERROR_MSG) {
															@Override
															protected NumberFormat getFormat(Locale locale) {
																return NumberFormat.getCurrencyInstance(Locale.US);
															}
														};
	
	public DollarPriceConverter() {
		super(ERROR_MSG);
	}
	
	@Override
	public Result<BigDecimal> convertToModel(String value, ValueContext context) {
		Result<Double> price = this.currencyConverter.convertToModel(value, context);
		if (price.isError()) {
			// Try without dollar sign
			price = this.doubleConverter.convertToModel(value, context);
		}
		return price.map(dbl -> dbl == null ? null : new BigDecimal(dbl));
	}
	
	@Override
	public String convertToPresentation(BigDecimal value, ValueContext context) {
		if (value == null) {
			return "";
		} else {
			Double price = value.doubleValue();
			return this.currencyConverter.convertToPresentation(price, context);
		}
	}
}
