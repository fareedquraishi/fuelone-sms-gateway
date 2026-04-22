with open('app/src/main/java/com/fuelone/smsgateway/data/Database.kt', 'r') as f:
    content = f.read()

# Make customerName nullable with default
content = content.replace(
    'val customerName: String = "",',
    'val customerName: String? = "",'
)

with open('app/src/main/java/com/fuelone/smsgateway/data/Database.kt', 'w') as f:
    f.write(content)

# Fix SmsGatewayService to handle null customerName
with open('app/src/main/java/com/fuelone/smsgateway/service/SmsGatewayService.kt', 'r') as f:
    content = f.read()

content = content.replace(
    'customerName = req.customerName,',
    'customerName = req.customerName ?: "",'
)

with open('app/src/main/java/com/fuelone/smsgateway/service/SmsGatewayService.kt', 'w') as f:
    f.write(content)

# Fix SendRequest model
with open('app/src/main/java/com/fuelone/smsgateway/model/Models.kt', 'r') as f:
    content = f.read()

content = content.replace(
    'val customerName: String = ""',
    'val customerName: String? = ""'
)

with open('app/src/main/java/com/fuelone/smsgateway/model/Models.kt', 'w') as f:
    f.write(content)

print('Done!')
