/*
 * Copyright 2022 Uppsala University Library
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

import se.uu.ub.cora.converter.ConverterException;
import se.uu.ub.cora.converter.ExternallyConvertibleToStringConverter;
import se.uu.ub.cora.data.ExternallyConvertible;
import se.uu.ub.cora.testutils.mcr.MethodCallRecorder;

public class ExternallyConvertibleToStringConverterSpy
		implements ExternallyConvertibleToStringConverter {

	public MethodCallRecorder MCR = new MethodCallRecorder();
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
