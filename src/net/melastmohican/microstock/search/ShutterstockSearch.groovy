//JAVA_OPTS=-DproxyHost=proxy.useastgw.xerox.com -DproxyPort=8000 -DproxySet=true
package net.melastmohican.microstock.search;

import java.util.Map;

class ShutterstockSearch extends BaseSearch {
	final def HOST = "http://www.shutterstock.com"

	public Set search(String input) {
		keywords.clear()
		def searchterm = input.tokenize().join("+")
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
	
	public static void main(String[] args) {
		def ss = new ShutterstockSearch()
		println ss.search("cute baby girl maya")
	}
}

