//JAVA_OPTS=-DproxyHost=proxy.useastgw.xerox.com -DproxyPort=8000 -DproxySet=true
package net.melastmohican.microstock.search;

import groovy.xml.StreamingMarkupBuilder
import groovy.xml.XmlUtil

class IStockPhotoSearch extends BaseSearch {
	final def HOST = "http://www.istockphoto.com"

	public Set search(String input) {
		keywords.clear()
		def searchterm = input.tokenize().join("+")
		def searchPage = slurper.parse("${HOST}/search/text/${searchterm}")
		// TODO: search still broken, need to find the way to search results loaded from Javascript
		//new File("test.xml") << XmlUtil.serialize(searchPage)
		searchPage.'**'.findAll{ it.@class == 'srFile' }.each {
			def page = it.@href.toString()
			println page
			def imagePage = slurper.parse(page)
			imagePage."**".find{ it.text() == 'Keywords:' }."..".td.a.each {
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
		def fs = new IStockPhotoSearch()
		println fs.search("cute baby girl maya")
	}
}

