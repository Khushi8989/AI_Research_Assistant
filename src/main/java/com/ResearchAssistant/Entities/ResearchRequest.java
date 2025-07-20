package com.ResearchAssistant.Entities;

import lombok.Data;

@Data
public class ResearchRequest {
	private String content;
	private String operation;
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public ResearchRequest(String content, String operation) {
		super();
		this.content = content;
		this.operation = operation;
	}
	@Override
	public String toString() {
		return "ResearchRequest [content=" + content + ", operation=" + operation + "]";
	}
	
	

}
