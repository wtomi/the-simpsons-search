import org.apache.lucene.analysis.Analyzer
import org.apache.lucene.document.Document
import org.apache.lucene.index.IndexWriter
import org.apache.lucene.index.IndexWriterConfig
import org.apache.lucene.store.Directory

class IndexBuilder {

    Analyzer analyzer
    Directory directory
    IndexWriter writer

    IndexBuilder(Analyzer analyzer, Directory directory) {
        this.analyzer = analyzer
        this.directory = directory
    }

    def addDocument(Document document) {
        getWriter().addDocument(document)
        return this
    }

    IndexWriter build() {
        getWriter().close()
        return getWriter()
    }

    def getWriter() {
        if (!writer) {
            writer = buildWriter()
        }
        return writer
    }

    def buildWriter() {
        if (!analyzer) throw new Error("Analyzer needs to be specified")
        if (!directory) throw new Error("Directory needs to be specified")
        def config = new IndexWriterConfig(analyzer)
        return new IndexWriter(directory, config)
    }
}
