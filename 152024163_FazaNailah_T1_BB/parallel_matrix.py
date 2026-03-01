import numpy as np
from multiprocessing import Pool, cpu_count

# menghitung satu baris matrix
def multiply_row(args):
    row, B = args
    return np.dot(row, B)

def parallel_matrix_multiply(A, B):
    with Pool(cpu_count()) as pool:
        result = pool.map(multiply_row, [(row, B) for row in A])
    return np.array(result)

if __name__ == "__main__":
    A = np.array([[1, 2],
                  [3, 4]])

    B = np.array([[5, 6],
                  [7, 8]])

    C = parallel_matrix_multiply(A, B)

    print("Result Matrix:")
    print(C)