//JAVA_OPTS=-DproxyHost=proxy.useastgw.xerox.com -DproxyPort=8000 -DproxySet=true
package net.melastmohican.microstock.search;

import java.util.Map;

class ShutterstockSearch {
	final def HOST = "http://www.shutterstock.com"
	@Grab(group='org.ccil.cowan.tagsoup', module='tagsoup', version='1.2' )
	final tagsoupParser = new org.ccil.cowan.tagsoup.Parser()
	final slurper = new XmlSlurper(tagsoupParser)
	private final input
	private final keywords = [:]

	ShutterstockSearch(String input) {
		this.input = input.tokenize()
	}

	Set search() {
		keywords.clear()
		def searchterm = input.join("+")
		def searchPage = slurper.parse("${HOST}/cat.mhtml?lang=en&searchterm=${searchterm}&anyorall=all&search_group=all&orient=all&images_per_page=25")
		
		searchPage.'**'.findAll{ it.@class == 'gc_thumb'}.each {
			def page = HOST + it.@href
			println page
			def imagePage = slurper.parse(page)
			imagePage."**".find { it.@id =='keywords-listing' }.a.each {
				def keyword = it.text().toLowerCase()
				def count = keywords[keyword]
				if( count == null) {
					count = 0
				}
				keywords.put(keyword.toLowerCase(), ++count)
			}
		}
		def results = keywords.sort { a, b -> b.value <=> a.value }
		return results.take(50).keySet()
	}
	
	public Map getKeywords() {
		return keywords
	}

	public static void main(String[] args) {
		def ss = new ShutterstockSearch("cute baby girl maya")
		println ss.search()
	}
}
