/*
 * Copyright 2023 Uppsala University Library
 *
 * This file is part of Cora.
 *
 *     Cora is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Cora is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Cora.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.uu.ub.cora.fedoraarchive.spy;

import se.uu.ub.cora.converter.ConverterFactory;
import se.uu.ub.cora.converter.ExternallyConvertibleToStringConverter;
import se.uu.ub.cora.converter.StringToExternallyConvertibleConverter;
import se.uu.ub.cora.testutils.mcr.MethodCallRecorder;
import se.uu.ub.cora.testutils.mrv.MethodReturnValues;

public class ConverterFactorySpy implements ConverterFactory {
	public MethodCallRecorder MCR = new MethodCallRecorder();
	public MethodReturnValues MRV = new MethodReturnValues();

	public ConverterFactorySpy() {
		MCR.useMRV(MRV);
		MRV.setDefaultReturnValuesSupplier("factorExternallyConvertableToStringConverter",
				ExternallyConvertibleToStringConverterSpy::new);
		MRV.setDefaultReturnValuesSupplier("getName", String::new);
	}

	@Override
	public ExternallyConvertibleToStringConverter factorExternallyConvertableToStringConverter() {
		return (ExternallyConvertibleToStringConverter) MCR.addCallAndReturnFromMRV();
	}

	@Override
	public StringToExternallyConvertibleConverter factorStringToExternallyConvertableConverter() {
		return (StringToExternallyConvertibleConverter) MCR.addCallAndReturnFromMRV();
	}

	@Override
	public String getName() {
		return (String) MCR.addCallAndReturnFromMRV();
	}

}
