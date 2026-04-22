with open('app/src/main/AndroidManifest.xml', 'r') as f:
    content = f.read()
content = content.replace('android:roundIcon="@android:drawable/ic_dialog_info_round"', '')
content = content.replace('android:roundIcon="@mipmap/ic_launcher_round"', '')
with open('app/src/main/AndroidManifest.xml', 'w') as f:
    f.write(content)
print('Done!')
