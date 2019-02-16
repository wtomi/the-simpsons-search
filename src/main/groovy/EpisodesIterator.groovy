import org.ccil.cowan.tagsoup.Parser

class EpisodesIterator implements Iterator<Episode> {

    static def tagsoupParser = new Parser()
    static def slurper = new XmlSlurper(tagsoupParser)

    private List<Episode> episodes
    private String baseeUrl

    EpisodesIterator(String url) {
        //This regex splits string on single slash without removing it
        baseeUrl = url.split("(?<=((?<!/)/(?!/)))")[0]
        def htmlParser = slurper.parse(url)
        episodes = htmlParser.'**'.findAll { it.@class == 'season-episodes' }.collect {
            def season = it.h3
            return it.'*'.findAll { it.@class == 'season-episode-title' }.collect {
                def title = it
                def episodeUrl = it.@href
                return new Episode(season: season, title: title, url: episodeUrl)
            }
        }.flatten() as List<Episode>
    }

    @Override
    boolean hasNext() {
        return !episodes.isEmpty()
    }

    @Override
    Episode next() {
        def episode = episodes.pop()
        episode.script = getEpisodeScriptFromPage(baseeUrl + episode.url)
        return episode
    }

    static def getEpisodeScriptFromPage(String url) {
        def text = url.toURL().text
        def escapedText = text.replaceAll("&", "&amp;")
        def episodeParser = slurper.parseText(escapedText)
        return episodeParser.'**'.find { it.@class as String == "scrolling-script-container" } as String
    }
}
