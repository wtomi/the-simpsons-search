import groovy.cli.commons.CliBuilder

class App {

    static final def app = new Application()

    static void main(String[] args) {
        def cli = new CliBuilder()
        cli.with {
            i 'Create index', type: Boolean
            p 'Phrase to be searched', type: String
            r 'Remove index', type: Boolean
        }
        def options = cli.parse(args)

        if (options.r) {
            app.deleteIndex()
        }

        if (options.i) {
            app.buildIndex()
        }

        if (options.p) {
            app.query(options.p as String)
        }
    }
}
