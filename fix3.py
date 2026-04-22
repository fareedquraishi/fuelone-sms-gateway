# Add SmsPopupActivity kotlin file
import os

activity_code = '''package com.fuelone.smsgateway.ui

import android.app.Activity
import android.os.Bundle

class SmsPopupActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        finish()
    }
}'''

os.makedirs('app/src/main/java/com/fuelone/smsgateway/ui', exist_ok=True)
with open('app/src/main/java/com/fuelone/smsgateway/ui/SmsPopupActivity.kt', 'w') as f:
    f.write(activity_code)

# Fix manifest - add SMS role intent filters
with open('app/src/main/AndroidManifest.xml', 'r') as f:
    content = f.read()

sms_activity = '''
        <activity
            android:name=".ui.SmsPopupActivity"
            android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.SENDTO" />
                <data android:scheme="sms" />
                <data android:scheme="smsto" />
                <category android:name="android.intent.category.DEFAULT" />
            </intent-filter>
        </activity>

        <receiver
            android:name=".receiver.SmsReceiver"
            android:exported="true"
            android:permission="android.permission.BROADCAST_SMS">
            <intent-filter android:priority="999">
                <action android:name="android.provider.Telephony.SMS_DELIVER" />
            </intent-filter>
        </receiver>'''

content = content.replace('</application>', sms_activity + '\n    </application>')

with open('app/src/main/AndroidManifest.xml', 'w') as f:
    f.write(content)

print('Done!')
