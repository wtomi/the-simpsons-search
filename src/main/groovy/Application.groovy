import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.document.Document
import org.apache.lucene.document.Field
import org.apache.lucene.document.StringField
import org.apache.lucene.document.TextField
import org.apache.lucene.index.DirectoryReader
import org.apache.lucene.index.IndexNotFoundException
import org.apache.lucene.store.FSDirectory
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import java.nio.file.Paths

class Application {

    private static final String BASE_ENDPOINT = "https://www.springfieldspringfield.co.uk/"
    private static final String SCRIPS_ENDPOINT = "${BASE_ENDPOINT}episode_scripts.php?tv-show=the-simpsons"
    private static final Logger LOG = LoggerFactory.getLogger(Application.class)
    def analyzer = new StandardAnalyzer()
    def indexPath = Paths.get("index")

    def deleteIndex() {
        indexPath.deleteDir()
    }

    void buildIndex() {
        def directory = FSDirectory.open(indexPath)
        IndexBuilder builder = new IndexBuilder(this.analyzer, directory)
        def iterator = new EpisodesIterator(SCRIPS_ENDPOINT)
        while (iterator.hasNext()) {
            def episode = iterator.next()
            def document = new Document()
            document.add(new TextField("script", episode.script, Field.Store.YES))
            document.add(new StringField("season", episode.season, Field.Store.YES))
            document.add(new StringField("title", episode.title, Field.Store.YES))
            document.add(new StringField("url", "$BASE_ENDPOINT${episode.url}", Field.Store.YES))
            builder.addDocument(document)
            LOG.info("S: ${episode.season} E: ${episode.title}")
        }
        builder.build()
    }

    def query(String phrase) {
        def result = null
        try {
            def directory = FSDirectory.open(indexPath)
            def reader = DirectoryReader.open(directory)
            def engine = new SearchEngine(analyzer, reader)
            result = engine.searchFiles(phrase as String)
        } catch (IndexNotFoundException e) {
            LOG.error("Index could not be found", e)
        }
        return result
    }

}
