package se.uu.ub.cora.fedoraarchive;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import se.uu.ub.cora.logger.Logger;
import se.uu.ub.cora.logger.LoggerFactory;

public class LoggerFactorySpy implements LoggerFactory {

	public Map<String, LoggerSpy> createdLoggers = new HashMap<>();

	@Override
	public Logger factorForClass(Class<? extends Object> javaClass) {
		String name = javaClass.getSimpleName();
		createdLoggers.put(name, new LoggerSpy());
		return createdLoggers.get(name);
	}

	public String getFatalLogMessageUsingClassNameAndNo(String className, int messageNo) {
		List<String> fatalMessages = (createdLoggers.get(className)).fatalMessages;
		return fatalMessages.get(messageNo);
	}

	public String getInfoLogMessageUsingClassNameAndNo(String className, int messageNo) {
		List<String> fatalMessages = (createdLoggers.get(className)).infoMessages;
		return fatalMessages.get(messageNo);
	}

}
