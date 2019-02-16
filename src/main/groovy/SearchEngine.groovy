import org.apache.lucene.analysis.Analyzer
import org.apache.lucene.document.Document
import org.apache.lucene.index.IndexReader
import org.apache.lucene.queryparser.classic.QueryParser
import org.apache.lucene.search.IndexSearcher
import org.apache.lucene.search.Query
import org.apache.lucene.search.TopDocs

import java.util.stream.Collectors

class SearchEngine {
    Analyzer analyzer
    IndexSearcher searcher

    SearchEngine(Analyzer analyzer, IndexReader reader) {
        this.analyzer = analyzer
        this.searcher = new IndexSearcher(reader)
    }

    List<Document> searchFiles(String queryString, String field = 'script', int n = 1000) {
        Query query = new QueryParser(field, analyzer).parse("\"$queryString\"")
        TopDocs topDocs = searcher.search(query, n)
        return topDocs.scoreDocs.stream()
                .map { searcher.doc(it.doc) }
                .collect(Collectors.toList())
    }
}
