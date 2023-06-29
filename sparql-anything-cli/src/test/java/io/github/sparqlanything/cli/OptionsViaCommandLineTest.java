package io.github.sparqlanything.cli;

import org.junit.Assert;
import org.junit.Test;

public class OptionsViaCommandLineTest {

	@Test
	public void test() throws Exception {
		String q = "SELECT ?v { SERVICE <x-sparql-anything:> { ?root a <http://sparql.xyz/facade-x/ns/root> ;  <http://www.w3.org/1999/02/22-rdf-syntax-ns#_1> ?v } }";
		String out = SPARQLAnything.callMain(new String[]{"-q", q, "-c", "content=abc"});
		Assert.assertTrue(out.contains("abc"));
	}

	@Test
	public void testOverride() throws Exception {
		String q = "SELECT ?v { SERVICE <x-sparql-anything:content=cde> { ?root a <http://sparql.xyz/facade-x/ns/root> ;  <http://www.w3.org/1999/02/22-rdf-syntax-ns#_1> ?v } }";
		String out = SPARQLAnything.callMain(new String[]{"-q", q, "-c", "content=abc"});
		Assert.assertTrue(out.contains("cde"));
	}

	@Test
	public void testWithoutService() throws Exception {
		String q = "SELECT ?v {  ?root a <http://sparql.xyz/facade-x/ns/root> ;  <http://www.w3.org/1999/02/22-rdf-syntax-ns#_1> ?v } ";
		String out = SPARQLAnything.callMain(new String[]{"-q", q, "-c", "content=abc"});
		System.out.println(out);
		Assert.assertTrue(out.contains("abc"));
	}
}
