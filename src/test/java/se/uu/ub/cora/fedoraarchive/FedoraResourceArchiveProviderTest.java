package se.uu.ub.cora.fedoraarchive;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.fedoraarchive.internal.FedoraResourceArchive;
import se.uu.ub.cora.storage.archive.ResourceArchiveInstanceProvider;

public class FedoraResourceArchiveProviderTest {

	FedoraResourceArchiveProvider provider;

	@BeforeMethod
	public void beforeMethod() {
		provider = new FedoraResourceArchiveProvider();
	}

	@Test
	public void testiImplementsResourceArchiveInstanceProvider() throws Exception {
		assertTrue(provider instanceof ResourceArchiveInstanceProvider);
	}

	@Test
	public void testGetResourceArchive() throws Exception {
		FedoraResourceArchive resourceArchive = (FedoraResourceArchive) provider
				.getResourceArchive();

		assertNotNull(resourceArchive);

	}
}
