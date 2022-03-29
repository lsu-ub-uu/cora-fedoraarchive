package se.uu.ub.cora.fedoraarchive.spy;

import se.uu.ub.cora.converter.ConverterFactory;
import se.uu.ub.cora.converter.ExternallyConvertibleToStringConverter;
import se.uu.ub.cora.converter.StringToExternallyConvertibleConverter;
import se.uu.ub.cora.testutils.mcr.MethodCallRecorder;

public class ConverterFactorySpy implements ConverterFactory {
	public MethodCallRecorder MCR = new MethodCallRecorder();

	@Override
	public ExternallyConvertibleToStringConverter factorExternallyConvertableToStringConverter() {
		MCR.addCall();

		ExternallyConvertibleToStringConverter converterSpy = new ExternallyConvertibleToStringConverterSpy();

		MCR.addReturned(converterSpy);
		return converterSpy;
	}

	@Override
	public StringToExternallyConvertibleConverter factorStringToExternallyConvertableConverter() {
		MCR.addCall();

		return null;
	}

	@Override
	public String getName() {

		MCR.addReturned(MCR);
		return null;
	}

}
