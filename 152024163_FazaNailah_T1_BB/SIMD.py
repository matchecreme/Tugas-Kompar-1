import numpy as np

print("SIMD Example")

A = np.array([1, 2, 3, 4])
B = np.array([5, 6, 7, 8])

C = A + B   # satu instruksi ke banyak data

print("Result:", C)