# SISD example (Serial Computing)

total = 0
print("SISD - Serial Computation")

for i in range(1, 6):
    total += i
    print(f"step {i}: total = {total}")

print("Final result:", total)