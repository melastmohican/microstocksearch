/**
 * 
 */
package net.melastmohican.microstock.search

import java.util.Map;
import java.util.Set;

abstract class BaseSearch {
	@Grab(group='org.ccil.cowan.tagsoup', module='tagsoup', version='1.2' )
	final tagsoupParser = new org.ccil.cowan.tagsoup.Parser()
	protected final slurper = new XmlSlurper(tagsoupParser)
	protected final keywords = [:]

	abstract public Set search(String input)
	
	public Map getKeywords() {
		return keywords
	}

}
