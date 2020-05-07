fruits = [
    {
        "name": "Mango",
        "price_per_kg": 25
    },
    {
        "name": "Banana",
        "price_per_kg": 60
    }
]

# Lambdas are used to inline single line functions

# Sort alphabetically
print("Sorting alphabetically")
fruits.sort(key=lambda fruit: fruit["name"])
print(fruits)

print()

# Sort by price
print("Sorting by price")
fruits.sort(key=lambda fruit: fruit["price_per_kg"])
print(fruits)
