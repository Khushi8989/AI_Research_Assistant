package com.ResearchAssistant.Services;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.ResearchAssistant.Entities.GeminiResponse;
import com.ResearchAssistant.Entities.ResearchRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ResearchService {
	
	@Value("${gemini.api.url}")
	private String geminiApiUrl;
	@Value("${gemini.api.key}")
	private String geminiApiKey;
	
	private final WebClient webClient ;
	private final ObjectMapper objectMapper;
	
	public ResearchService (WebClient.Builder webClientBuilder,ObjectMapper objectMapper) {
		this.webClient=webClientBuilder.build();
		this.objectMapper= objectMapper;
	}

	public String processContent(ResearchRequest request) {
		
		//Build the prompt
		String prompt= buildPrompt(request);
		
		//Query the AI model API
		Map<String,Object>requestBody= Map.of(
				"contents",new Object[] {
						Map.of("parts",new Object[] {
								Map.of("text",prompt)
						})
				});
		//Parse the response
		
		String response= webClient.post()
				.uri(geminiApiUrl + geminiApiKey)
				.bodyValue(requestBody)
				.retrieve()
				.bodyToMono(String.class)
				.block();
		//Return response
		return extractTextFromResponse(response);
		
		
		
	}
	private String extractTextFromResponse(String response) {
		try {
			GeminiResponse geminiResponse= objectMapper.readValue(response, GeminiResponse.class);
			if (geminiResponse.getCandidates() != null && !geminiResponse.getCandidates().isEmpty()) {
			    GeminiResponse.Candidate firstCandidate = geminiResponse.getCandidates().get(0);
			    if (firstCandidate.getContent() != null &&
			        firstCandidate.getContent().getParts() != null &&
			        !firstCandidate.getContent().getParts().isEmpty()) {
			        return firstCandidate.getContent().getParts().get(0).getText();
			    }
			}

			
		}catch(Exception e) {
			return "Error Parsing: " +e.getMessage();
		}
		return response;
		
	}

	private String buildPrompt(ResearchRequest request) {
		StringBuilder prompt= new StringBuilder();
		switch(request.getOperation()) {
		case "summarize":
			prompt.append("provide a clear and concise summary of the following text in few sentences : \n\n");
			break;
		case "suggest":
			prompt.append("Based on the following content: suggest related topics and further reading.Formate the response with clear heading and bullet points:\n\n");
			break;
		default:
			throw new IllegalArgumentException("Unknown Operation:"+ request.getOperation());
		}
		prompt.append(request.getContent());
		return prompt.toString();
		
		
	}

}
