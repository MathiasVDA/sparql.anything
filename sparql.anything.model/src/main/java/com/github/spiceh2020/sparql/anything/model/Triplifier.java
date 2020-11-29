package com.github.spiceh2020.sparql.anything.model;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.Set;

import org.apache.jena.sparql.core.DatasetGraph;

public interface Triplifier {

	public DatasetGraph triplify(URL url, Properties properties) throws IOException;

	public Set<String> getMimeTypes();

	public Set<String> getExtensions();

}
