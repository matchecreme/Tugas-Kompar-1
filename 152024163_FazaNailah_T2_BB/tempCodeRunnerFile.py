import time

# jumlah process simulasi
size = 3

print("Program starts with main process\n")

messages = []

# Fork process & parallel simulation
for rank in range(size):
    print(f"Process {rank} is running")
    time.sleep(1)
    
    message = f"Hello from process {rank}"
    messages.append(message)

# Join process
print("\nAll processes joined.")

for msg in messages:
    print(msg)