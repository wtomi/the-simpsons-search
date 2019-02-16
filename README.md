# The Simpsons Search

## Overview

This is a simple REST application for searching phrases in the episodes' scripts of The Simpsons series.
The application fetches scripts from a web page by parsing html markup.
The application uses Lucene for creating the index of the scripts and Ratpack for creating simple web service.

## Running

To run the application use command:

```./gradlew rum```

The server will be running on port 5050

## Endpoints

### Querying

http://localhost:5050/query?phrase= <your-phrase>

For example, if you want to search for a phrase "hearing things", the request should look like this:

http://localhost:5050/query?phrase=hearing%20things

The endpoint returns list of URLs linking to pages whose scripts contain the searched phrase.

### Removing index

http://localhost:5050/remove-index

### Creating index

http://localhost:5050/create-index

This operating takes a while. Look at server logs to see information about indexing consecutive episodes.