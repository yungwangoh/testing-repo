package org.traffichunter.javaagent.testai.core.model.claude;

import org.springframework.stereotype.Component;
import org.traffichunter.javaagent.testai.core.model.Model;

@Component
public class ClaudeModel implements Model {

    @Override
    public String getModel() {
        return "claude";
    }

    @Override
    public String execute(final String question, final boolean isStreaming) {

        if(isStreaming) {
            return question;
        }

        return question;
    }
}
