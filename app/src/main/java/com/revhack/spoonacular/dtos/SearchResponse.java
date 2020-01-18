package com.revhack.spoonacular.dtos;

import java.util.ArrayList;
import java.util.List;

public class SearchResponse {

    private List<ResultsDto> results = new ArrayList<>();

    private String baseUri;

    private int offset;

    private int number;

    private int totalResults;

    private Long processingTimeMs;

    private Long expires;

    private boolean isStale;

    public List<ResultsDto> getResults() {
        return results;
    }

    public void setResults(List<ResultsDto> results) {
        this.results = results;
    }

    public String getBaseUri() {
        return baseUri;
    }

    public void setBaseUri(String baseUri) {
        this.baseUri = baseUri;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public Long getProcessingTimeMs() {
        return processingTimeMs;
    }

    public void setProcessingTimeMs(Long processingTimeMs) {
        this.processingTimeMs = processingTimeMs;
    }

    public Long getExpires() {
        return expires;
    }

    @Override
    public String toString() {
        return "SearchResponse{" +
                "results=" + results +
                ", baseUri='" + baseUri + '\'' +
                ", offset=" + offset +
                ", number=" + number +
                ", totalResults=" + totalResults +
                ", processingTimeMs=" + processingTimeMs +
                ", expires=" + expires +
                ", isStale=" + isStale +
                '}';
    }

    public void setExpires(Long expires) {
        this.expires = expires;
    }

    public boolean isStale() {
        return isStale;
    }

    public void setStale(boolean stale) {
        isStale = stale;
    }
}
