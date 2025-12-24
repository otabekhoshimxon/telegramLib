package travel.letstrip.config;

import java.util.ArrayList;
import java.util.List;

public class BotConfig {
    private String botToken;
    private String botUsername;
    private List<GroupConfig> groups;
    
    public BotConfig() {
        this.groups = new ArrayList<>();
    }
    
    public BotConfig(String botToken, String botUsername) {
        this.botToken = botToken;
        this.botUsername = botUsername;
        this.groups = new ArrayList<>();
    }
    
    public String getBotToken() { return botToken; }
    public void setBotToken(String botToken) { this.botToken = botToken; }
    
    public String getBotUsername() { return botUsername; }
    public void setBotUsername(String botUsername) { this.botUsername = botUsername; }
    
    public List<GroupConfig> getGroups() { return groups; }
    public void setGroups(List<GroupConfig> groups) { this.groups = groups; }
    
    public void addGroup(GroupConfig group) { this.groups.add(group); }
    
    public GroupConfig getGroupByName(String name) {
        return groups.stream()
            .filter(g -> g.getName().equals(name))
            .findFirst()
            .orElse(null);
    }
}