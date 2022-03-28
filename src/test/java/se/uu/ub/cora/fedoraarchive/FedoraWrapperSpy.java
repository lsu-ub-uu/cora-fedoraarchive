package se.uu.ub.cora.fedoraarchive;

import java.io.InputStream;

import se.uu.ub.cora.fedora.FedoraException;
import se.uu.ub.cora.fedora.FedoraWrapper;
import se.uu.ub.cora.testutils.mcr.MethodCallRecorder;

public class FedoraWrapperSpy implements FedoraWrapper {

	public MethodCallRecorder MCR = new MethodCallRecorder();
	public boolean throwExceptionOnCreateRecordAlreadyExists = false;

	@Override
	public void create(String recordId, String recordXml) {
		MCR.addCall("recordId", recordId, "recordXml", recordXml);

		if (throwExceptionOnCreateRecordAlreadyExists) {
			throw FedoraException.withMessage("From spy, record alreadyExists");
		}

	}

	@Override
	public void createBinary(String recordId, InputStream binary, String binaryContentType) {
		MCR.addCall("recordId", recordId, "binary", binary, "binaryContentType", binaryContentType);

	}

	@Override
	public String read(String recordId) {
		MCR.addCall("recordId", recordId);

		String dataArchived = "";
		MCR.addReturned(dataArchived);
		return dataArchived;
	}

	@Override
	public InputStream readBinary(String recordId) {
		MCR.addCall("recordId", recordId);

		InputStream stream = null;
		MCR.addReturned(stream);
		return stream;
	}

	@Override
	public void update(String recordId, String recordXml) {
		MCR.addCall("recordId", recordId, "recordXml", recordXml);

	}

}
