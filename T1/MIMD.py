from multiprocessing import Process

def task1():
    print("Task 1: Matrix Addition")

def task2():
    print("Task 2: Matrix Multiplication")

def task3():
    print("Task 3: Matrix Transpose")

if __name__ == "__main__":
    p1 = Process(target=task1)
    p2 = Process(target=task2)
    p3 = Process(target=task3)

    p1.start()
    p2.start()
    p3.start()

    p1.join()
    p2.join()
    p3.join()

    print("All tasks finished")