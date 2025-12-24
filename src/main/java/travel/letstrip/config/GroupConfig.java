package travel.letstrip.config;

public class GroupConfig {
    private Long chatId;
    private Integer topicId;
    private String name;
    
    public GroupConfig() {}
    
    public GroupConfig(Long chatId, Integer topicId, String name) {
        this.chatId = chatId;
        this.topicId = topicId;
        this.name = name;
    }
    
    public Long getChatId() { return chatId; }
    public void setChatId(Long chatId) { this.chatId = chatId; }
    
    public Integer getTopicId() { return topicId; }
    public void setTopicId(Integer topicId) { this.topicId = topicId; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public boolean hasTopic() { return topicId != null && topicId > 0; }
}