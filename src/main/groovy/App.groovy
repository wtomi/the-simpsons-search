import groovy.cli.commons.CliBuilder
import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.analysis.Analyzer
import org.apache.lucene.document.Document
import org.apache.lucene.document.Field
import org.apache.lucene.document.StringField
import org.apache.lucene.document.TextField
import org.apache.lucene.index.DirectoryReader
import org.apache.lucene.store.FSDirectory

import java.nio.file.Paths

class App {
    static void main(String[] args) {
        def cli = new CliBuilder()
        cli.with {
            i 'Create index', type: Boolean
            p 'Phrase to be searched', type: String
            r 'Remove index', type: Boolean
        }
        def options = cli.parse(args)

        def analyzer = new StandardAnalyzer()
        def path = Paths.get("index")

        if (options.r) {
            def file = path.toFile()
            file.deleteDir()
        }

        def directory = FSDirectory.open(path)

        if (options.i) {
            IndexBuilder builder = new IndexBuilder(analyzer, directory)
            def iterator = new EpisodesIterator("https://www.springfieldspringfield.co.uk/episode_scripts.php?tv-show=the-simpsons")
            while (iterator.hasNext()) {
                def episode = iterator.next()
                def document = new Document()
                document.add(new TextField("script", episode.script, Field.Store.YES))
                document.add(new StringField("season", episode.season, Field.Store.YES))
                document.add(new StringField("title", episode.title, Field.Store.YES))
                document.add(new StringField("url", episode.url, Field.Store.YES))
                builder.addDocument(document)
                println "S: ${episode.season} E: ${episode.title}"
            }
            builder.build()
        }

        if (options.p) {
            def reader = DirectoryReader.open(directory)
            def engine = new SearchEngine(analyzer, reader)
            def documents = engine.searchFiles(options.p as String)
            documents.each { println it }
        }
    }
}
