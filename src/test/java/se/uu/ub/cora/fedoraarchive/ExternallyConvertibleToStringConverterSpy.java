package se.uu.ub.cora.fedoraarchive;

import se.uu.ub.cora.converter.ConverterException;
import se.uu.ub.cora.converter.ExternallyConvertibleToStringConverter;
import se.uu.ub.cora.data.ExternallyConvertible;
import se.uu.ub.cora.testutils.mcr.MethodCallRecorder;

public class ExternallyConvertibleToStringConverterSpy
		implements ExternallyConvertibleToStringConverter {

	MethodCallRecorder MCR = new MethodCallRecorder();
	public String transformedXml = "<xml>someXml</xml>";
	public boolean throwExceptionOnConvert = false;

	@Override
	public String convert(ExternallyConvertible externallyConvertible) {
		MCR.addCall("externallyConvertible", externallyConvertible);

		if (throwExceptionOnConvert) {
			throw new ConverterException("Spy exception, error con xml convertion");
		}

		MCR.addReturned(transformedXml);
		return transformedXml;
	}

	@Override
	public String convertWithLinks(ExternallyConvertible externallyConvertible, String baseUrl) {
		// TODO Auto-generated method stub
		return null;
	}

}
