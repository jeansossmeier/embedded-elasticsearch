package pl.allegro.tech.embeddedelasticsearch;

import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class InstallFromDirectUrl implements InstallationSource {
    
    private final URL downloadUrl;
    private final String version;

    InstallFromDirectUrl(URL downloadUrl) {
        this.downloadUrl = downloadUrl;
        this.version = versionFromUrl(downloadUrl);
    }

    @Override
    public URL resolveDownloadUrl() {
        return downloadUrl;
    }

    @Override
    public String determineVersion() {
        return version;
    }

    private String versionFromUrl(URL url) {
        String regex = "-(\\d+(?:\\.\\d+)*)\\.(?:zip|tar\\.gz)$";
        Pattern versionPattern = Pattern.compile(regex);
        Matcher matcher = versionPattern.matcher(url.toString());
        if (matcher.find()) {
            return matcher.group(1);
        }

        throw new IllegalArgumentException("Cannot find version in this url. Note that I was looking for zip archive with name in format: \"anyArchiveName-versionInAnyFormat.zip\". Examples of valid urls:\n" +
                "- http://example.com/elasticsearch-2.3.0.zip\n" +
                "- http://example.com/myDistributionOfElasticWithChangedName-1.0.0.tar.gz");
    }

    public static void main(String[] args) {
        // Define the regex pattern
        String regex = "-(\\d+(?:\\.\\d+)*)\\.(?:zip|tar\\.gz)$";
        Pattern pattern = Pattern.compile(regex);

        // Example URLs to test
        String[] urls = {
                "https://downloads.example.com/elasticsearch-8.6.2.zip",
                "https://downloads.example.com/elasticsearch-8.6.2.tar.gz",
                "https://downloads.example.com/elasticsearch-10.12.3.zip",
                "https://downloads.example.com/elasticsearch-1.0.tar.gz",
                "https://downloads.example.com/elasticsearch-1.0.0.1.zip",
                "https://downloads.example.com/elasticsearch-8.6.zip",
                "https://downloads.example.com/elasticsearch-8.6.2.tar.bz2", // Should not match
                "https://downloads.example.com/elasticsearch8.6.2.zip",      // Should not match
                "https://downloads.example.com/elasticsearch-8.6.2zip"       // Should not match
        };

        // Iterate over each URL and extract the version if it matches
        for (String url : urls) {
            Matcher matcher = pattern.matcher(url);
            if (matcher.find()) {
                String version = matcher.group(1);
                System.out.println("URL: " + url);
                System.out.println("Extracted Version: " + version);
                System.out.println("-----------------------------------");
            } else {
                System.out.println("URL: " + url);
                System.out.println("No Match Found.");
                System.out.println("-----------------------------------");
            }
        }
    }
}
