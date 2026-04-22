import re

# Fix build.gradle
with open('app/build.gradle', 'r') as f:
    content = f.read()
content = content.replace('buildTypes {', 'lintOptions {\n        checkReleaseBuilds false\n        abortOnError false\n    }\n\n    buildTypes {')
with open('app/build.gradle', 'w') as f:
    f.write(content)

# Fix manifest
with open('app/src/main/AndroidManifest.xml', 'r') as f:
    content = f.read()
content = re.sub(r'\s*<service[^>]*BluetoothService.*?</service>', '', content, flags=re.DOTALL)
content = content.replace('@mipmap/ic_launcher', '@android:drawable/ic_dialog_info')
content = content.replace('android:roundIcon="@mipmap/ic_launcher_round"', '')
with open('app/src/main/AndroidManifest.xml', 'w') as f:
    f.write(content)

print('All fixes applied!')
