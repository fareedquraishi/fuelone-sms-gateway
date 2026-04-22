with open('app/src/main/java/com/fuelone/smsgateway/ui/MainActivity.kt', 'r') as f:
    content = f.read()

# Add import
content = content.replace(
    'import android.Manifest',
    '''import android.Manifest
import android.app.role.RoleManager
import android.content.Intent'''
)

# Add SMS role request after checkPermissions() call in onCreate
content = content.replace(
    '        checkPermissions()\n        startStatusPolling()',
    '''        checkPermissions()
        requestSmsRole()
        startStatusPolling()'''
)

# Add the requestSmsRole function before onResume
content = content.replace(
    '    override fun onResume() {',
    '''    private fun requestSmsRole() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val roleManager = getSystemService(RoleManager::class.java)
            if (roleManager != null && !roleManager.isRoleHeld(RoleManager.ROLE_SMS)) {
                val intent = roleManager.createRequestRoleIntent(RoleManager.ROLE_SMS)
                startActivityForResult(intent, 1001)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: android.content.Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1001) {
            if (resultCode == android.app.Activity.RESULT_OK) {
                android.widget.Toast.makeText(this, "SMS Gateway permission granted!", android.widget.Toast.LENGTH_SHORT).show()
            } else {
                android.widget.Toast.makeText(this, "SMS permission denied — gateway may not work", android.widget.Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onResume() {'''
)

with open('app/src/main/java/com/fuelone/smsgateway/ui/MainActivity.kt', 'w') as f:
    f.write(content)
print('Done!')
