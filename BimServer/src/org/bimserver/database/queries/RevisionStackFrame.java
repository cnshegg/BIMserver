package org.bimserver.database.queries;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.bimserver.BimserverDatabaseException;
import org.bimserver.database.Query;
import org.bimserver.database.actions.ConcreteRevisionStackFrame;
import org.bimserver.models.store.ConcreteRevision;
import org.bimserver.models.store.Revision;
import org.bimserver.models.store.StorePackage;

public class RevisionStackFrame implements StackFrame {

	private Revision currentRevision;
	private Map<Integer, Long> pidRoidMap = new HashMap<>();
	private Iterator<ConcreteRevision> concreteRevisionIterator;
	private QueryObjectProvider queryObjectProvider;

	public RevisionStackFrame(QueryObjectProvider queryObjectProvider, long roid) throws BimserverDatabaseException {
		this.queryObjectProvider = queryObjectProvider;
		currentRevision = (Revision) queryObjectProvider.getDatabaseSession().get(StorePackage.eINSTANCE.getRevision(), roid, Query.getDefault());
		pidRoidMap.put(currentRevision.getProject().getId(), currentRevision.getOid());
		concreteRevisionIterator = currentRevision.getConcreteRevisions().iterator();
	}

	@Override
	public StackFrame process() {
		if (concreteRevisionIterator.hasNext()) {
			return new ConcreteRevisionStackFrame(queryObjectProvider, this.concreteRevisionIterator.next());
		}
		return null;
	}
}