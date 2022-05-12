
package com.github.sparqlanything.it;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.apache.jena.query.ARQ;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.query.Syntax;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.sparql.engine.main.QC;

import com.github.sparqlanything.engine.FacadeX;

public class DocumentationExampleSandbox {

	private static Map<String, String> prefixes = new HashMap<String, String>();

	public static void json1() throws URISyntaxException {
//		String location = DocumentationExampleSandbox.class.getClassLoader().getResource("DocExamples/json.json")
//				.toURI().toString();
		String location = "https://sparql-anything.cc/examples/simple.json";
		Query query = QueryFactory.create(
				"CONSTRUCT {?s ?p ?o} WHERE { SERVICE <x-sparql-anything:location=" + location + "> { ?s ?p ?o} }");

		System.out.println(query.toString(Syntax.defaultQuerySyntax));

		Dataset ds = DatasetFactory.createGeneral();

		QC.setFactory(ARQ.getContext(), FacadeX.ExecutorFactory);

		Model m = QueryExecutionFactory.create(query, ds).execConstruct();
		m.setNsPrefixes(prefixes);

		m.write(System.out, "TTL");
	}

	public static void json2() throws URISyntaxException {
		String location = "https://sparql-anything.cc/example1.json";
		Query query = QueryFactory.create("PREFIX xyz: <http://sparql.xyz/facade-x/data/>\n"
				+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
				+ "PREFIX fx: <http://sparql.xyz/facade-x/ns/>" + "SELECT * { SERVICE <x-sparql-anything:location="
				+ location + "> { " + " fx:properties fx:json.path '$[?(@.name==\"Friends\")]' . "
				+ " _:s xyz:language ?language . " + " } }");

		Query queryConstruct = QueryFactory.create("PREFIX xyz: <http://sparql.xyz/facade-x/data/>\n"
				+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
				+ "PREFIX fx: <http://sparql.xyz/facade-x/ns/>"
				+ "CONSTRUCT { ?s ?p ?o } WHERE { SERVICE <x-sparql-anything:location=" + location + "> { "
				+ " fx:properties fx:json.path '$[?(@.name==\"Friends\")]' . " + " ?s ?p ?o . " + " } }");

		System.out.println(query.toString(Syntax.defaultQuerySyntax));

		Dataset ds = DatasetFactory.createGeneral();

		QC.setFactory(ARQ.getContext(), FacadeX.ExecutorFactory);
		System.out.println(ResultSetFormatter.asText(QueryExecutionFactory.create(query, ds).execSelect()));

		System.out.println(queryConstruct.toString(Syntax.defaultSyntax));
		Model m = QueryExecutionFactory.create(queryConstruct, ds).execConstruct();
		m.setNsPrefixes(prefixes);
		m.write(System.out, "TTL");

		queryConstruct = QueryFactory.create("PREFIX xyz: <http://sparql.xyz/facade-x/data/>\n"
				+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
				+ "PREFIX fx: <http://sparql.xyz/facade-x/ns/>"
				+ "CONSTRUCT { ?s ?p ?o } WHERE { SERVICE <x-sparql-anything:location=" + location + "> { "
				+ " fx:properties fx:json.path.1 '$[?(@.name==\"Friends\")].stars' . "
				+ " fx:properties fx:json.path.2 '$[?(@.name==\"Cougar Town\")].stars' . " + " ?s ?p ?o . " + " } }");

		System.out.println(queryConstruct.toString(Syntax.defaultSyntax));
		m = QueryExecutionFactory.create(queryConstruct, ds).execConstruct();
		m.setNsPrefixes(prefixes);
		m.write(System.out, "TTL");

	}

	public static void html1() throws URISyntaxException {
//		String location = DocumentationExampleSandbox.class.getClassLoader().getResource("DocExamples/simple.html")
//				.toURI().toString();
		String location = "https://sparql-anything.cc/examples/simple.html";
		Query query = QueryFactory.create(
				"CONSTRUCT {?s ?p ?o} WHERE { SERVICE <x-sparql-anything:location=" + location + "> { ?s ?p ?o} }");

		Dataset ds = DatasetFactory.createGeneral();

		QC.setFactory(ARQ.getContext(), FacadeX.ExecutorFactory);

		System.out.println(query.toString(Syntax.defaultSyntax));

		Model m = QueryExecutionFactory.create(query, ds).execConstruct();
		m.setNsPrefixes(prefixes);

		m.write(System.out, "TTL");

		query = QueryFactory.create(
				"PREFIX whatwg: <https://html.spec.whatwg.org/#> SELECT ?text WHERE { SERVICE <x-sparql-anything:location=" + location + ",html.selector=.paragraph> { ?s whatwg:innerText ?text} }");
		System.out.println(query.toString(Syntax.defaultQuerySyntax));
		System.out.println(ResultSetFormatter.asText(QueryExecutionFactory.create(query, ds).execSelect()));
		
		
		location = "https://sparql-anything.cc/examples/Microdata1.html";
		query = QueryFactory.create(
				"CONSTRUCT {?s ?p ?o} WHERE { SERVICE <x-sparql-anything:location=" + location + ",html.metadata=true> {GRAPH ?g {?s ?p ?o}} }");
		System.out.println(query.toString(Syntax.defaultQuerySyntax));
//		System.out.println(ResultSetFormatter.asText(QueryExecutionFactory.create(query, ds).execSelect()));
		m = QueryExecutionFactory.create(query, ds).execConstruct();
		m.setNsPrefixes(prefixes);
		m.write(System.out, "TTL");
		
		location = "https://sparql-anything.cc/examples/Microdata1.html";
		query = QueryFactory.create(
				"CONSTRUCT {?s ?p ?o} WHERE { SERVICE <x-sparql-anything:location=" + location + ",html.metadata=false> {GRAPH ?g {?s ?p ?o}} }");
		System.out.println(query.toString(Syntax.defaultQuerySyntax));
//		System.out.println(ResultSetFormatter.asText(QueryExecutionFactory.create(query, ds).execSelect()));
		m = QueryExecutionFactory.create(query, ds).execConstruct();
		m.setNsPrefixes(prefixes);
		m.write(System.out, "TTL");

	}
	
	public static void csv() throws URISyntaxException {
//		String location = DocumentationExampleSandbox.class.getClassLoader().getResource("DocExamples/simple.html")
//				.toURI().toString();
		String location = "https://sparql-anything.cc/examples/simple.csv";
		Query query = QueryFactory.create(
				"CONSTRUCT {?s ?p ?o} WHERE { SERVICE <x-sparql-anything:location=" + location + "> { ?s ?p ?o} }");

		Dataset ds = DatasetFactory.createGeneral();

		QC.setFactory(ARQ.getContext(), FacadeX.ExecutorFactory);

		System.out.println(query.toString(Syntax.defaultSyntax));

		Model m = QueryExecutionFactory.create(query, ds).execConstruct();
		m.setNsPrefixes(prefixes);
		m.write(System.out, "TTL");
		
		
		location = "https://sparql-anything.cc/examples/simple.tsv";
		query = QueryFactory.create(
				"PREFIX xyz: <http://sparql.xyz/facade-x/data/> PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> SELECT (AVG(xsd:float(?petalLength)) AS ?avgPetalLength) WHERE { SERVICE <x-sparql-anything:location=" + location + ",csv.headers=true,csv.format=TDF> { "
						+ "?s xyz:Sepal_length ?length ; xyz:Petal_length ?petalLength ."
						+ "FILTER(xsd:float(?length)>4.9) "
						+ "} }");
		System.out.println(query.toString(Syntax.defaultQuerySyntax));
		System.out.println(ResultSetFormatter.asText(QueryExecutionFactory.create(query, ds).execSelect()));
		
		query = QueryFactory.create(
				"CONSTRUCT {?s ?p ?o} WHERE { SERVICE <x-sparql-anything:location=" + location + ",csv.format=TDF> {?s ?p ?o} }");
		System.out.println(query.toString(Syntax.defaultQuerySyntax));
		m = QueryExecutionFactory.create(query, ds).execConstruct();
		m.setNsPrefixes(prefixes);
		m.write(System.out, "TTL");
		
		location = DocumentationExampleSandbox.class.getClassLoader().getResource("DocExamples/simple.csv")
				.toURI().toString();
		
		query = QueryFactory.create(
				"CONSTRUCT {?s ?p ?o} WHERE { SERVICE <x-sparql-anything:location=" + location + ",csv.headers=true> {?s ?p ?o} }");
		System.out.println(query.toString(Syntax.defaultQuerySyntax));
		m = QueryExecutionFactory.create(query, ds).execConstruct();
		m.setNsPrefixes(prefixes);
		m.write(System.out, "TTL");
//		
//		location = "https://sparql-anything.cc/examples/Microdata1.html";
//		query = QueryFactory.create(
//				"CONSTRUCT {?s ?p ?o} WHERE { SERVICE <x-sparql-anything:location=" + location + ",html.metadata=false> {GRAPH ?g {?s ?p ?o}} }");
//		System.out.println(query.toString(Syntax.defaultQuerySyntax));
////		System.out.println(ResultSetFormatter.asText(QueryExecutionFactory.create(query, ds).execSelect()));
//		m = QueryExecutionFactory.create(query, ds).execConstruct();
//		m.setNsPrefixes(prefixes);
//		m.write(System.out, "TTL");

	}

	public static void main(String[] args) throws URISyntaxException {
		prefixes.put("xyz", "http://sparql.xyz/facade-x/data/");
		prefixes.put("fx", "http://sparql.xyz/facade-x/ns/");
		prefixes.put("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
		prefixes.put("xsd", "http://www.w3.org/2001/XMLSchema#");

//		json1();

//		json2();

//		prefixes.put("xhtml", "http://www.w3.org/1999/xhtml#");
//		prefixes.put("whatwg", "https://html.spec.whatwg.org/#");
//
//		html1();
		
		csv();
	}

}
