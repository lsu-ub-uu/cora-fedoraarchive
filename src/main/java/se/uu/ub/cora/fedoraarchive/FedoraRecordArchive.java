package se.uu.ub.cora.fedoraarchive;

import se.uu.ub.cora.converter.ConverterException;
import se.uu.ub.cora.converter.ExternallyConvertibleToStringConverter;
import se.uu.ub.cora.data.DataGroup;
import se.uu.ub.cora.fedora.FedoraException;
import se.uu.ub.cora.fedora.FedoraWrapper;
import se.uu.ub.cora.storage.RecordConflictException;
import se.uu.ub.cora.storage.archive.RecordArchive;

public class FedoraRecordArchive implements RecordArchive {

	private FedoraWrapper fedoraWrapper;
	private ExternallyConvertibleToStringConverter xmlConverter;

	public FedoraRecordArchive(ExternallyConvertibleToStringConverter xmlConverter,
			FedoraWrapper fedoraWrapper) {
		this.xmlConverter = xmlConverter;
		this.fedoraWrapper = fedoraWrapper;
	}

	@Override
	public void create(String type, String id, DataGroup dataRecord) {
		try {
			// TODO: do we need to store type in Fedora??? If yes, how???

			String xml = xmlConverter.convert(dataRecord);
			fedoraWrapper.create(id, xml);

		} catch (FedoraException e) {
			throw RecordConflictException
					.withMessage("Record could not be created in Fedora Archive");
		} catch (ConverterException e) {
			throw RecordConflictException.withMessage(
					"Record could not be converted to xml and therefore could not be stored "
							+ "in Fedora Archive");
		}
	}

}
