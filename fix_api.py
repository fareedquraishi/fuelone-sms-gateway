with open('app/src/main/java/com/fuelone/smsgateway/server/ApiServer.kt', 'r') as f:
    content = f.read()

print(content[content.find('handleSend'):content.find('handleSend')+1500])
