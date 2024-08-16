package com.vi.springai.Model;

import lombok.Data;

import java.util.List;

@Data
public class Response {
    public String response;
    public List<String> booksToRefer;
}
