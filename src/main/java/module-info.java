import se.uu.ub.cora.fedoraarchive.FedoraRecordArchiveProvider;

/**
 * The fedora module provides interfaces and classes to use a Fedora Commons System in a Cora based
 * system.
 */
module se.uu.ub.cora.fedoraarchive {
	requires se.uu.ub.cora.storage;
	requires se.uu.ub.cora.initialize;
	requires se.uu.ub.cora.fedora;
	requires se.uu.ub.cora.converter;
	requires se.uu.ub.cora.httphandler;
	requires se.uu.ub.cora.xmlconverter;
	requires se.uu.ub.cora.logger;

	provides se.uu.ub.cora.storage.archive.RecordArchiveProvider with FedoraRecordArchiveProvider;

}