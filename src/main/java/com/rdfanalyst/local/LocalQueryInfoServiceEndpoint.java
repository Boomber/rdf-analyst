package com.rdfanalyst.local;

import com.rdfanalyst.accounting.*;
import com.rdfanalyst.general.GeneralOKResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.rdfanalyst.local.AddQueryResponse.*;

@Controller
public class LocalQueryInfoServiceEndpoint {

    private static final Logger logger = LoggerFactory.getLogger(LocalQueryInfoServiceEndpoint.class);

    @Autowired
    private QueryAccountingService queryAccountingService;

    @Autowired
    private ResultService resultService;

    @RequestMapping(value = "/add-query", method = RequestMethod.POST)
    public
    @ResponseBody
    AddQueryResponse addQuery(@RequestBody AddQueryRequest addQueryRequest) {
        try {
            queryAccountingService.registerQuery(new Query(addQueryRequest.getQuery()));
            return ok();
        } catch (DuplicateQueryNameException e) {
            return duplicateNameError();
        } catch (Exception e) {
            logger.error("There was an exception while adding a new query. ", e);
            return customError(e);
        }
    }

    @RequestMapping(value = "/available-local-queries", method = RequestMethod.GET)
    public
    @ResponseBody
    List<Query> localQueries() {
        return queryAccountingService.getArchivedQueries();
    }

    @RequestMapping(value = "/available-local-queries/{queryName}", method = RequestMethod.GET)
    public
    @ResponseBody
    Query queryDetails(@PathVariable String queryName) {
        return queryAccountingService.findQueryByTopic(queryName);
    }

    @RequestMapping(value = "/local-query-results/{topic}", method = RequestMethod.GET)
    public
    @ResponseBody
    List<RDFTriple> responses(@PathVariable String topic) {
        return resultService.findAllResultsForTopic(topic);
    }

    @RequestMapping(value = "/available-local-queries/{topic}", method = RequestMethod.DELETE)
    public @ResponseBody GeneralOKResponse deleteTopic(@PathVariable String topic) {
        logger.info("Deletion of local data of topic {} requested.", topic);
        resultService.clearResultsOfTopic(topic);
        queryAccountingService.deleteQuery(topic);
        return new GeneralOKResponse();
    }


}
