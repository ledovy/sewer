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
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String ERROR_MSG = "Invalid prices, please re-check the value";
	private StringToDoubleConverter doubleConverter = new StringToDoubleConverter(DollarPriceConverter.ERROR_MSG);
	private StringToDoubleConverter currencyConverter = new StringToDoubleConverter(DollarPriceConverter.ERROR_MSG) {
		/**
		* 
		*/
		private static final long serialVersionUID = 1L;
		
		@Override
		protected NumberFormat getFormat(final Locale locale) {
			return NumberFormat.getCurrencyInstance(Locale.US);
		}
	};
	
	public DollarPriceConverter() {
		super(DollarPriceConverter.ERROR_MSG);
	}
	
	@Override
	public Result<BigDecimal> convertToModel(final String value, final ValueContext context) {
		Result<Double> price = this.currencyConverter.convertToModel(value, context);
		if (price.isError()) {
			// Try without dollar sign
			price = this.doubleConverter.convertToModel(value, context);
		}
		return price.map(dbl -> dbl == null ? null : new BigDecimal(dbl));
	}
	
	@Override
	public String convertToPresentation(final BigDecimal value, final ValueContext context) {
		if (value == null) {
			return "";
		} else {
			Double price = value.doubleValue();
			return this.currencyConverter.convertToPresentation(price, context);
		}
	}
}
