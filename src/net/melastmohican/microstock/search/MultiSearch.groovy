package net.melastmohican.microstock.search

import java.util.Set;
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.Future

class MultiSearch {
	private final searches = [
		new BigStockPhotoSearch(),
		new CanStockPhotoSearch(),
		new FotoliaSearch(),
		new OneTwoThreeRFSearch(),
		new ShutterstockSearch(),
		new DreamstimeSearch()
	]

	public Set search(String input) {
		def myTask = {it ->
			def result = null
			try{
				BaseSearch search =  searches[it]
				search.search(input)
				result = search.getKeywords()
			}catch(Throwable t){
				System.err << t.toString()
			}
		}
		def total = [:]
		def threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())
		try {
			int max = (searches.size()-1)
			List<Future> futures = (0..max).collect { it -> threadPool.submit( { -> myTask it } as Callable ); }

			futures.each {
				def keywords = it.get()
				keywords.each {
					def count = total[it.key]
					if(count == null) {
						count = 0
					}
					count += it.value
					total.put(it.key, count)
				}
			}

			def results = total.sort { a, b -> b.value <=> a.value }
			println results
			return results.keySet()
		} finally {
			threadPool.shutdown()
		}
	}

	static main(args) {
		def ms = new MultiSearch()
		println ms.search("cute baby girl maya")
	}
}
