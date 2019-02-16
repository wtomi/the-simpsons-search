import java.util.stream.Collectors

import static ratpack.groovy.Groovy.ratpack
import static ratpack.jackson.Jackson.json

ratpack {

    def app = new Application()

    handlers {
        get("create-index") {
            app.buildIndex()
            render "Index  built successfully"
        }
        get("remove-index") {
            if (app.deleteIndex()) {
                render "Index deleted successfully"
            } else {
                render "Index could not be deleted"
            }
        }
        get("query") {
            def phrase = request.getQueryParams().get("phrase")
            def documents = null
            if (phrase) {
                documents = app.query(phrase).stream()
                        .map { it.getField("url").stringValue() }
                        .collect(Collectors.toList())
            }
            def output = documents ?: "No documents found"
            render json(output)
        }
    }
}