/*
 * Copyright (c) 2023 SPARQL Anything Contributors @ http://github.com/sparql-anything
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.sparqlanything.text;

import java.io.IOException;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.github.sparqlanything.model.SPARQLAnythingConstants;
import org.apache.commons.io.IOUtils;
import org.apache.jena.ext.com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.sparqlanything.model.FacadeXGraphBuilder;
import io.github.sparqlanything.model.Triplifier;
import io.github.sparqlanything.model.TriplifierHTTPException;

public class TextTriplifier implements Triplifier {

	public static final String REGEX = "txt.regex", GROUP = "txt.group", SPLIT = "txt.split";
	private static final Logger logger = LoggerFactory.getLogger(TextTriplifier.class);

	@Override
	public void triplify(Properties properties, FacadeXGraphBuilder builder) throws IOException, TriplifierHTTPException {

		String value;
		String dataSourceId = SPARQLAnythingConstants.DATA_SOURCE_ID;
		String rootId = SPARQLAnythingConstants.ROOT_ID;
		value = IOUtils.toString(Triplifier.getInputStream(properties), Triplifier.getCharsetArgument(properties));

		if (logger.isTraceEnabled()) {
			logger.trace("Content:\n{}\n", value);
		}

		builder.addRoot(dataSourceId);

		Pattern pattern = null;
		if (properties.containsKey(REGEX)) {
			String regexString = properties.getProperty(REGEX);
			logger.trace("Regex {}", regexString);
			try {
				pattern = Pattern.compile(regexString);
				// TODO flags
			} catch (Exception e) {
//				e.printStackTrace();
				logger.error(e.getMessage(), e);
				pattern = null;
			}

		}

		int group = -1;
		if (properties.containsKey(GROUP) && pattern != null) {
			logger.trace("Group property set");
			try {
				int gr = Integer.parseInt(properties.getProperty(GROUP));
				if (gr >= 0) {
					group = gr;
				} else {
					logger.warn("Group number is supposed to be a positive integer, using default (group 0)");
				}
			} catch (Exception e) {
				logger.error("", e);
			}
		}

		if (pattern != null) {
			logger.trace("Instantiating the matcher group {}", group);
			Matcher m = pattern.matcher(value);
			int count = 1;
			while (m.find()) {
				if (group >= 1) {
					logger.trace("Adding value from group {}: slot {} - {}", group, count, m.group(group));
					builder.addValue(dataSourceId, rootId, count, m.group(group));
				} else {
					logger.trace("Adding value {} {}", count, m.group());
					builder.addValue(dataSourceId, rootId, count, m.group());
				}
				count++;
			}
		} else {
			logger.trace("No pattern set");

			if (properties.containsKey(SPLIT)) {

				logger.trace("Splitting regex: {}", properties.getProperty(SPLIT));
				String[] split = value.split(properties.getProperty(SPLIT));
				for (int i = 0; i < split.length; i++) {
					builder.addValue(dataSourceId, rootId, i + 1, split[i]);
				}

			} else {
				builder.addValue(dataSourceId, rootId, 1, value);
			}

		}
	}

//	private static String readFromURL(URL url, Properties properties) throws IOException, TriplifierHTTPException {
//		StringWriter sw = new StringWriter();
//		InputStream is = Triplifier.getInputStream(properties);
//		IOUtils.copy(is, sw, Triplifier.getCharsetArgument(properties));
//		return sw.toString();
//
//	}

	@Override
	public Set<String> getMimeTypes() {
		return Sets.newHashSet("text/plain");
	}

	@Override
	public Set<String> getExtensions() {
		return Sets.newHashSet("txt");
	}
}
