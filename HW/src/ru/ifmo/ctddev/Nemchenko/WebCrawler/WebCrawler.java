package ru.ifmo.ctddev.Nemchenko.WebCrawler;

import info.kgeorgiy.java.advanced.crawler.Crawler;
import info.kgeorgiy.java.advanced.crawler.Document;
import info.kgeorgiy.java.advanced.crawler.Downloader;
import info.kgeorgiy.java.advanced.crawler.Result;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by eugene on 2015/04/21.
 */
public class WebCrawler implements Crawler {
    private final Downloader downloader;
    private final int downloaders;
    private final int extractors;
    private final int perHost;

    public WebCrawler(Downloader downloader, int downloaders, int extractors, int perHost) {
        this.downloader = downloader;
        this.downloaders = downloaders;
        this.extractors = extractors;
        this.perHost = perHost;

        // create priority thread pool downloaders + extractors
    }

    @Override
    public Result download(String url, int depth) {
        return null;
    }
//    @Override
//    public Result download(String url, int depth) {
//        List<String> links = new ArrayList<String>();
//        links.add(url);
//        while (depth > 0) {
//            for (String curUrl : links) {
//                Document document = downloader.download(url);
//                links.addAll(document.extractLinks());
//            }
//        }
//
//        return null;
//    }

    @Override
    public void close() {

    }
}
