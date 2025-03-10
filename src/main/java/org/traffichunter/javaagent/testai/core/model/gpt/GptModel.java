package org.traffichunter.javaagent.testai.core.model.gpt;

import org.springframework.stereotype.Component;
import org.traffichunter.javaagent.testai.core.model.Model;

@Component
public class GptModel implements Model {

    @Override
    public String getModel() {
        return "gpt";
    }

    @Override
    public String execute(final String question, final boolean isStreaming) {

        if(isStreaming) {
            return question;
        }

        return question;
    }
}
