with open('app/src/main/java/com/fuelone/smsgateway/data/Database.kt', 'r') as f:
    content = f.read()

# Bump version from 1 to 2
content = content.replace(
    '@Database(entities = [MessageEntity::class], version = 1, exportSchema = false)',
    '@Database(entities = [MessageEntity::class], version = 2, exportSchema = false)'
)

with open('app/src/main/java/com/fuelone/smsgateway/data/Database.kt', 'w') as f:
    f.write(content)
print('Done!')
