# The Simpsons Search

## Overview

is a simple REST application for searching phrases in the episodes' scripts of The Simpsons series.
To obtained all the scripts the application parse data from a web page containing the scripts.
The application uses Lucene for creating the index of the scripts and Ratpack for creating endpoints.

## Running

To tun the application use command:

```./gradlew rum```

The server will be running on port 5050

## Endpoints

### Querying

http://localhost:5050/query?phrase=<your-phrase>

For example, if you want to search for a phrase "hearing things", the request should look like this:

http://localhost:5050/query?phrase=hearing%20things

The endpoint returns list of URLs linking to a page with a script containing the searched phrase.

### Removing index

http://localhost:5050/remove-index

### Creating index

http://localhost:5050/create-index

This operating takes a while. Look ar logs to see information about indexing consecutive episodes' scripts.