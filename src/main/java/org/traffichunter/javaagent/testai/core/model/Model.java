package org.traffichunter.javaagent.testai.core.model;

public interface Model {

    String execute(String question, boolean isStreaming);

    String getModel();
}
