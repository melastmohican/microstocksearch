//JAVA_OPTS=-DproxyHost=proxy.useastgw.xerox.com -DproxyPort=8000 -DproxySet=true
package net.melastmohican.microstock.search;

class DreamstimeSearch extends BaseSearch {
	final def HOST = "http://www.dreamstime.com"
	
	public Set search(String input) {
		keywords.clear()
		def searchterm = input.tokenize().join("+")
		def searchPage = slurper.parse("${HOST}/search.php?srh_field=${searchterm}&items=25")
		searchPage.'**'.findAll{ it.@class == 'thb_cells thb_c1' }.a.each {
			def page = it.@href.toString()
			println page
			def imagePage = slurper.parse(page)
			imagePage."**".find { it.@class == 'item08' }.a.each {
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
		def fs = new DreamstimeSearch()
		println fs.search("cute baby girl maya")
	}
}

