//JAVA_OPTS=-DproxyHost=proxy.useastgw.xerox.com -DproxyPort=8000 -DproxySet=true
package net.melastmohican.microstock.search;

class FotoliaSearch extends BaseSearch {
	final def HOST = "http://www.fotolia.com"

	public Set search(String input) {
		keywords.clear()
		def searchterm = input.tokenize().join("+")
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
	

	public static void main(String[] args) {
		def fs = new FotoliaSearch()
		println fs.search("cute baby girl maya")
	}
}

