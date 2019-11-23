package webcrawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.HashSet;

public class BasicWebCrawler {

    private HashSet<String> links;

    public BasicWebCrawler() {
        links = new HashSet<String>();
    }

    public void getPageLinks(String url) {
        //4. Check if you have already crawled the URLs 
        //(we are intentionally not checking for duplicate content in this example)
        if (!links.contains(url)) {
            try {
                //4. (i) If not add it to the index
                if (links.add(url)) {
                    System.out.println(url);
                }

                //2. Fetch the HTML code
                Document document = Jsoup.connect(url).get();
                //3. Parse the HTML to extract links to other URLs
                Elements linksOnPage = document.select("a[href]");

                //5. For each extracted url... go back to Step 4.
                URL parseURL = new URL(url);
                for (Element page : linksOnPage) {
                    String href = page.attr("abs:href");
                    URL hrefAsURL = new URL(href);
                    if(hrefAsURL.getHost().equals(parseURL.getHost())) {
                        getPageLinks(page.attr("abs:href"));
                    }
                }
            } catch (IOException | IllegalArgumentException e) {
                System.err.println("For '" + url + "': " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        //1. Pick a URL from the frontier
        new BasicWebCrawler().getPageLinks(args[0]);
    }

}
