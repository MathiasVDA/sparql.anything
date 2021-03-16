/*
 * Copyright (c) 2021 Enrico Daga @ http://www.enridaga.net
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.github.spiceh2020.sparql.anything.it;

import com.github.spiceh2020.sparql.anything.engine.FacadeX;
import org.apache.jena.query.*;
import org.apache.jena.sparql.engine.main.QC;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItTripleFilteringTest {
    private static final Logger log = LoggerFactory.getLogger(ItTripleFilteringTest.class);



    @Test
    public void JSON1() throws URISyntaxException {
        // a01009-14709.json
        Dataset kb = DatasetFactory.createGeneral();
        QC.setFactory(ARQ.getContext(), FacadeX.ExecutorFactory);
        String location = getClass().getClassLoader().getResource("tate-gallery/a01009-14709.json").toURI().toString();
        Query query = QueryFactory.create(
                "SELECT DISTINCT ?p WHERE { SERVICE <x-sparql-anything:namespace=http://www.example.org#,location="
                        + location + "> {graph ?g {?s ?p ?o }}} order by ?p");
        ResultSet rs = QueryExecutionFactory.create(query, kb).execSelect();
        List<String> mustInclude = new ArrayList<String>(
                Arrays.asList(new String[] { "http://www.example.org#thumbnailUrl", "http://www.example.org#title",
                        "http://www.w3.org/1999/02/22-rdf-syntax-ns#_1", "http://www.example.org#text",
                        "http://www.example.org#subjects", "http://www.example.org#subjectCount" }));
        while (rs.hasNext()) {
            int rowId = rs.getRowNumber() + 1;
            QuerySolution qs = rs.next();
            log.info("{} {}", rowId, qs.get("p").toString());
            mustInclude.remove(qs.get("p").toString());
        }
        Assert.assertTrue(mustInclude.isEmpty());
    }

}
