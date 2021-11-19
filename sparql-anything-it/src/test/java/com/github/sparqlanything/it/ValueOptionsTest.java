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

package com.github.sparqlanything.it;

import com.github.sparqlanything.csv.CSVTriplifier;
import com.github.sparqlanything.testutils.AbstractTriplifierTester;
import org.apache.jena.base.Sys;
import org.apache.jena.rdf.model.ModelFactory;
import org.junit.Test;

import java.util.Properties;

public class ValueOptionsTest extends AbstractTriplifierTester {

	public ValueOptionsTest() {
		super(new CSVTriplifier(), new Properties(), "csv");
	}

	@Override
	protected void properties(Properties properties) {
		if(name.getMethodName().equals("testTrimStringsTrue")){
			properties.setProperty("blank-nodes", "true");
			properties.setProperty("trim-strings", "true");
		}else if(name.getMethodName().equals("testTrimStringsFalse")){
			properties.setProperty("blank-nodes", "true");
			properties.setProperty("trim-strings", "false");
		}else if(name.getMethodName().equals("testNullStringsTrue")){
			properties.setProperty("blank-nodes", "true");
			properties.setProperty("null-strings", "true");
		}else if(name.getMethodName().equals("testNullStringsFalse")){
			properties.setProperty("blank-nodes", "true");
			properties.setProperty("null-strings", "false");
		}
	}

	@Test
	public void testTrimStringsTrue(){
//		ModelFactory.createModelForGraph(expected).write(System.err, "TTL");
//		ModelFactory.createModelForGraph(result).write(System.err, "TTL");
		assertResultIsIsomorphicWithExpected();
	}

	@Test
	public void testTrimStringsFalse(){
//		ModelFactory.createModelForGraph(expected).write(System.err, "TTL");
//		ModelFactory.createModelForGraph(result).write(System.err, "TTL");
		assertResultIsIsomorphicWithExpected();
	}


	@Test
	public void testNullStringsTrue(){
//		ModelFactory.createModelForGraph(expected).write(System.err, "TTL");
//		ModelFactory.createModelForGraph(result).write(System.err, "TTL");
		assertResultIsIsomorphicWithExpected();
	}

	@Test
	public void testNullStringsFalse(){
//		ModelFactory.createModelForGraph(expected).write(System.err, "TTL");
//		ModelFactory.createModelForGraph(result).write(System.err, "TTL");
		assertResultIsIsomorphicWithExpected();
	}
}
