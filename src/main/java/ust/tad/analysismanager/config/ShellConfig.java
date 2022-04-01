package ust.tad.analysismanager.config;

import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import org.springframework.shell.jline.PromptProvider;
import org.springframework.stereotype.Component;

@Component
public class ShellConfig implements PromptProvider{

        @Override
        public AttributedString getPrompt() {
            return new AttributedString("tad-shell" + ":>",
                    AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW));
        }
    
}
