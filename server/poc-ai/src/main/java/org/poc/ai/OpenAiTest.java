package org.poc.ai;

import java.util.List;

public class OpenAiTest {
    public static void main(String[] args) {
        System.out.println("Working");
        Advisor advisor = new Advisor(System.getenv("AZURE_OPENAI_API_KEY"), System.getenv("AZURE_OPENAI_ENDPOINT"));
        List<String> responses = advisor.advise("When was Microsoft founded?");
        responses.forEach(System.out::println);
    }
}
