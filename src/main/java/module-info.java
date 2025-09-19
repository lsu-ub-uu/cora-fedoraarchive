import se.uu.ub.cora.fedoraarchive.FedoraRecordArchiveProvider;
import se.uu.ub.cora.fedoraarchive.FedoraResourceArchiveProvider;

/**
 * The fedora module provides interfaces and classes to use a Fedora Commons System in a Cora based
 * system.
 */
module se.uu.ub.cora.fedoraarchive {
	requires transitive se.uu.ub.cora.storage;
	requires se.uu.ub.cora.initialize;
	requires se.uu.ub.cora.fedora;
	requires se.uu.ub.cora.converter;
	requires se.uu.ub.cora.httphandler;
	requires se.uu.ub.cora.logger;

	exports se.uu.ub.cora.fedoraarchive.path;

	provides se.uu.ub.cora.storage.archive.RecordArchiveProvider with FedoraRecordArchiveProvider;
	provides se.uu.ub.cora.storage.archive.ResourceArchiveInstanceProvider
			with FedoraResourceArchiveProvider;

}