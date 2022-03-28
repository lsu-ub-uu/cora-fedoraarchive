package se.uu.ub.cora.fedoraarchive;

import org.testng.annotations.Test;

import se.uu.ub.cora.storage.archive.RecordArchiveProvider;

public class FedoraRecordArchiveProviderTest {

	@Test
	public void testInit() throws Exception {
		RecordArchiveProvider fedoraRecordArchiveProvider = new FedoraRecordArchiveProvider();
	}

}
