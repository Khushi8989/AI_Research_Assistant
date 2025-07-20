package com.ResearchAssistant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ResearchAssistant.Entities.ResearchRequest;
import com.ResearchAssistant.Services.ResearchService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/research")
@CrossOrigin(origins = "*")
@AllArgsConstructor
public class ResearchController {
	@Autowired
	private  ResearchService researchService ;
	
	@PostMapping("/process")
	public ResponseEntity<String> processContent(@RequestBody ResearchRequest request){
		String result= researchService.processContent(request);
		return ResponseEntity.ok(result);
	}

}
