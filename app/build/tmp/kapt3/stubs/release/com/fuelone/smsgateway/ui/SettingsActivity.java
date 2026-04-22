package com.fuelone.smsgateway.ui;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000R\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\b\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\f\u001a\u00020\rH\u0003J\b\u0010\u000e\u001a\u00020\u000fH\u0002J\u0012\u0010\u0010\u001a\u00020\u000f2\b\u0010\u0011\u001a\u0004\u0018\u00010\u0012H\u0014J\u0010\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u0016H\u0016J\b\u0010\u0017\u001a\u00020\u000fH\u0002J\b\u0010\u0018\u001a\u00020\u000fH\u0002J\b\u0010\u0019\u001a\u00020\u000fH\u0002J\u001e\u0010\u001a\u001a\u00020\u000f2\n\b\u0002\u0010\u001b\u001a\u0004\u0018\u00010\u000b2\b\b\u0002\u0010\u001c\u001a\u00020\u001dH\u0002J\b\u0010\u001e\u001a\u00020\u000fH\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082.\u00a2\u0006\u0002\n\u0000R\u0014\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001f"}, d2 = {"Lcom/fuelone/smsgateway/ui/SettingsActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "binding", "Lcom/fuelone/smsgateway/databinding/ActivitySettingsBinding;", "prefs", "Lcom/fuelone/smsgateway/data/AppPreferences;", "rulesAdapter", "Lcom/fuelone/smsgateway/ui/AutoReplyRulesAdapter;", "rulesList", "", "Lcom/fuelone/smsgateway/model/AutoReplyRule;", "detectSimNumber", "", "loadSettings", "", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onOptionsItemSelected", "", "item", "Landroid/view/MenuItem;", "saveSettings", "setupAutoReplyRules", "setupListeners", "showAddRuleDialog", "existing", "position", "", "testEndpoint", "app_release"})
public final class SettingsActivity extends androidx.appcompat.app.AppCompatActivity {
    private com.fuelone.smsgateway.databinding.ActivitySettingsBinding binding;
    private com.fuelone.smsgateway.data.AppPreferences prefs;
    private com.fuelone.smsgateway.ui.AutoReplyRulesAdapter rulesAdapter;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.fuelone.smsgateway.model.AutoReplyRule> rulesList = null;
    
    public SettingsActivity() {
        super();
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void loadSettings() {
    }
    
    @android.annotation.SuppressLint(value = {"MissingPermission"})
    private final java.lang.String detectSimNumber() {
        return null;
    }
    
    private final void setupAutoReplyRules() {
    }
    
    private final void showAddRuleDialog(com.fuelone.smsgateway.model.AutoReplyRule existing, int position) {
    }
    
    private final void setupListeners() {
    }
    
    private final void saveSettings() {
    }
    
    private final void testEndpoint() {
    }
    
    @java.lang.Override()
    public boolean onOptionsItemSelected(@org.jetbrains.annotations.NotNull()
    android.view.MenuItem item) {
        return false;
    }
}