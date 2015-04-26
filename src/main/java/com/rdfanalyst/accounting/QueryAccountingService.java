package com.rdfanalyst.accounting;

import java.util.Collection;

public interface QueryAccountingService {

    void registerQuery(Query query);

    Collection<Query> getArchivedQueries();

    Query findQueryByTopic(String topic);

    boolean areWeCurrentlyListeningTopic(String topic);
}
