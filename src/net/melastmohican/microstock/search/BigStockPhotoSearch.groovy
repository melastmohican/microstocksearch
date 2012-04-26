//JAVA_OPTS=-DproxyHost=proxy.useastgw.xerox.com -DproxyPort=8000 -DproxySet=true
package net.melastmohican.microstock.search;

class BigStockPhotoSearch extends BaseSearch {
	final def HOST = "http://www.bigstockphoto.com"

	public Set search(String input) {
		keywords.clear()
		def searchterm = input.tokenize().join("+")
		def searchPage = slurper.parse("${HOST}/search/?q[st]=${searchterm}&x=40&y=47&q[color]=&q[si]=most&q[cat][main]=&q[ill]=hide&q[uid]=&q[ori]=&q[fmt]=&q[rel]=Both&q[adl]=n&q[pm]=show&q[lp]=Any&queryprpp=25")
		searchPage.'**'.findAll{ it.@class.text().startsWith("image-preview")}.each {
			def page = HOST + it.@href
			println page
			def imagePage = slurper.parse(page)
			imagePage."**".findAll { it.@id.text().startsWith("kw_") }.each {
				def keyword = it."..".a.text().toLowerCase()
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
		def fs = new BigStockPhotoSearch()
		println fs.search("cute baby girl maya")
	}
}

