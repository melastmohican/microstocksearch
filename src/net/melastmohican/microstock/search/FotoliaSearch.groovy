//JAVA_OPTS=-DproxyHost=proxy.useastgw.xerox.com -DproxyPort=8000 -DproxySet=true
package net.melastmohican.microstock.search;

class FotoliaSearch {
	final def HOST = "http://www.fotolia.com"
	@Grab(group='org.ccil.cowan.tagsoup', module='tagsoup', version='1.2' )
	final tagsoupParser = new org.ccil.cowan.tagsoup.Parser()
	final slurper = new XmlSlurper(tagsoupParser)
	private final input
	private final keywords = [:]

	FotoliaSearch(String input) {
		this.input = input.tokenize()
	}

	Set search() {
		keywords.clear()
		def searchterm = input.join("+")
		def searchPage = slurper.parse("${HOST}/search?k=${searchterm}&filters[collection]=all&filters[orientation]=all&order=relevance&filters[content_type:photo]=1&limit=25")
		searchPage.'**'.findAll{ it.@class == 'item-id'}.each {
			def page = HOST + it.@href
			println page
			def imagePage = slurper.parse(page)
			imagePage."**".findAll { it.@class.text().startsWith("tags") }.each {
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
		def fs = new FotoliaSearch("cute baby girl maya")
		println fs.search()
	}
}

