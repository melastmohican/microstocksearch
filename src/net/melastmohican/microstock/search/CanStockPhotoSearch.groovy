//JAVA_OPTS=-DproxyHost=proxy.useastgw.xerox.com -DproxyPort=8000 -DproxySet=true
package net.melastmohican.microstock.search;

class CanStockPhotoSearch extends BaseSearch {
	final def HOST = "http://www.canstockphoto.com"

	public Set search(String input) {
		keywords.clear()
		def searchterm = input.tokenize().join("+")
		def searchPage = slurper.parse("${HOST}/search.php?keywords=${searchterm}&type_1=1")
		searchPage.'**'.findAll{ it.@class.text().startsWith("img_container")}.a.each {
			def page = HOST + it.@href
			println page
			def imagePage = slurper.parse(page)
			imagePage."**".find { it.text() == 'Keywords:' }"..".div.a.each {
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
		def fs = new CanStockPhotoSearch()
		println fs.search("cute baby girl maya")
	}
}

