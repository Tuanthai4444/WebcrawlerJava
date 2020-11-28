import java.io.IOException;
import java.util.*;
import org.jsoup.*;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class spider {

    private final int MAX_LINKS = 10;

    public spider() {
    }

    public List<String> startCrawl(String url) throws IOException {
        Set<String> visited = new HashSet<>();
        Queue<String> toVisit = new LinkedList<>();
        List<String> linkList = new ArrayList<>();

        toVisit.add(url);
        visited.add(url);

        while(!toVisit.isEmpty() || visited.size() == MAX_LINKS) {
            String currentUrl = toVisit.remove();
            List<String> innerLinks = getInnerLinks(currentUrl);

            for(String link : innerLinks) {
                if(visited.size() == MAX_LINKS) {
                    break;
                }

                if(!visited.contains(link)) {
                    toVisit.add(link);
                    visited.add(link);
                    linkList.add(link);
                }
            }
        }

        return linkList;
    }

    public List<String> getInnerLinks(String url) throws IOException {
        Connection conn = Jsoup.connect(url)
                            .userAgent("Mozilla/5.0 (Windows; U; Windows NT 6.1; rv:2.2) Gecko/20110201");

        Document doc = conn.get();
        List<String> links = new ArrayList<String>();

        if(conn.response().statusCode() == 200) {
            if(!conn.response().contentType().contains("text/html")) {
                return null;
            } else {
                Elements pageLinks = doc.select("a[href]");
                for(Element link : pageLinks) {
                    links.add(link.absUrl("href"));
                }
            }
            return links;
        }
        return null;
    }

}
