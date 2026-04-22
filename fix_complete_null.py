with open('app/src/main/java/com/fuelone/smsgateway/data/Database.kt', 'r') as f:
    content = f.read()

content = content.replace(
    'val customerName: String? = "",',
    'val customerName: String? = null,'
)
content = content.replace(
    'val customerName: String = "",',
    'val customerName: String? = null,'
)
content = content.replace(
    'version = 2, exportSchema = false)',
    'version = 3, exportSchema = false)'
)

with open('app/src/main/java/com/fuelone/smsgateway/data/Database.kt', 'w') as f:
    f.write(content)

with open('app/src/main/java/com/fuelone/smsgateway/service/SmsGatewayService.kt', 'r') as f:
    content = f.read()

content = content.replace('req.customerName,', 'req.customerName ?: "",')
content = content.replace('req.messageId,', 'req.messageId ?: "",')

with open('app/src/main/java/com/fuelone/smsgateway/service/SmsGatewayService.kt', 'w') as f:
    f.write(content)

print('Done!')
