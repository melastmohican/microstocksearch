package net.melastmohican.microstock.search

import groovy.lang.Grab
import groovy.util.XmlSlurper

import java.util.Map
import java.util.Set

//@Grab(group='org.ccil.cowan.tagsoup', module='tagsoup', version='1.2' )
abstract class BaseSearch {	
	protected final slurper = new XmlSlurper(new org.ccil.cowan.tagsoup.Parser())
	protected final keywords = [:]

	abstract public Set search(String input)
	
	public Map getKeywords() {
		return keywords
	}

}
