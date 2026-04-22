package com.fuelone.smsgateway.model;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\b\u0086\b\u0018\u00002\u00020\u0001:\u0001\u0017B\u001f\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\u0002\u0010\u0007J\t\u0010\r\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u000e\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u000f\u001a\u00020\u0006H\u00c6\u0003J\'\u0010\u0010\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u0006H\u00c6\u0001J\u0013\u0010\u0011\u001a\u00020\u00122\b\u0010\u0013\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0014\u001a\u00020\u0015H\u00d6\u0001J\t\u0010\u0016\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\t\u00a8\u0006\u0018"}, d2 = {"Lcom/fuelone/smsgateway/model/AutoReplyRule;", "", "keyword", "", "reply", "matchType", "Lcom/fuelone/smsgateway/model/AutoReplyRule$MatchType;", "(Ljava/lang/String;Ljava/lang/String;Lcom/fuelone/smsgateway/model/AutoReplyRule$MatchType;)V", "getKeyword", "()Ljava/lang/String;", "getMatchType", "()Lcom/fuelone/smsgateway/model/AutoReplyRule$MatchType;", "getReply", "component1", "component2", "component3", "copy", "equals", "", "other", "hashCode", "", "toString", "MatchType", "app_release"})
public final class AutoReplyRule {
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String keyword = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String reply = null;
    @org.jetbrains.annotations.NotNull()
    private final com.fuelone.smsgateway.model.AutoReplyRule.MatchType matchType = null;
    
    public AutoReplyRule(@org.jetbrains.annotations.NotNull()
    java.lang.String keyword, @org.jetbrains.annotations.NotNull()
    java.lang.String reply, @org.jetbrains.annotations.NotNull()
    com.fuelone.smsgateway.model.AutoReplyRule.MatchType matchType) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getKeyword() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getReply() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.fuelone.smsgateway.model.AutoReplyRule.MatchType getMatchType() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component1() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.fuelone.smsgateway.model.AutoReplyRule.MatchType component3() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.fuelone.smsgateway.model.AutoReplyRule copy(@org.jetbrains.annotations.NotNull()
    java.lang.String keyword, @org.jetbrains.annotations.NotNull()
    java.lang.String reply, @org.jetbrains.annotations.NotNull()
    com.fuelone.smsgateway.model.AutoReplyRule.MatchType matchType) {
        return null;
    }
    
    @java.lang.Override()
    public boolean equals(@org.jetbrains.annotations.Nullable()
    java.lang.Object other) {
        return false;
    }
    
    @java.lang.Override()
    public int hashCode() {
        return 0;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.lang.String toString() {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0004\b\u0086\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004\u00a8\u0006\u0005"}, d2 = {"Lcom/fuelone/smsgateway/model/AutoReplyRule$MatchType;", "", "(Ljava/lang/String;I)V", "EXACT", "CONTAINS", "app_release"})
    public static enum MatchType {
        /*public static final*/ EXACT /* = new EXACT() */,
        /*public static final*/ CONTAINS /* = new CONTAINS() */;
        
        MatchType() {
        }
        
        @org.jetbrains.annotations.NotNull()
        public static kotlin.enums.EnumEntries<com.fuelone.smsgateway.model.AutoReplyRule.MatchType> getEntries() {
            return null;
        }
    }
}