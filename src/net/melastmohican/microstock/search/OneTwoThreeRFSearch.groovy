//JAVA_OPTS=-DproxyHost=proxy.useastgw.xerox.com -DproxyPort=8000 -DproxySet=true
package net.melastmohican.microstock.search;

class OneTwoThreeRFSearch extends BaseSearch {
	final def HOST = "http://www.123rf.com"
	
	public Set search(String input) {
		keywords.clear()
		def searchterm = input.tokenize().join("+")
		def searchPage = slurper.parse("${HOST}/search.php?word=${searchterm}&t_lang=en&search_std_sub=1&search_evo=1&srch_lang=en&imgtype=1&itemsperpage=25&orderby=2")
		searchPage.'**'.findAll{ it.@class == 'img_th' }.a.each {
			def page = HOST + it.@href.toString()
			println page
			def imagePage = slurper.parse(page)
			imagePage."**".findAll { it.@id =='pageno' }.each {
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
		def fs = new OneTwoThreeRFSearch()
		println fs.search("cute baby girl maya")
	}
}

