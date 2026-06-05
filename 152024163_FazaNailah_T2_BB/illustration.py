from mpi4py import MPI
import time

# Inisialisasi MPI
comm = MPI.COMM_WORLD
rank = comm.Get_rank()
size = comm.Get_size()

# Hanya proses utama (rank 0) yang memulai di awal
if rank == 0:
    print("Program starts with single main process\n", flush=True)

# Setiap process bekerja secara paralel
print(f"Process {rank} started", flush=True)
time.sleep(2)
print(f"Process {rank} finished", flush=True)

# Sinkronisasi semua process
comm.Barrier()

# Hanya process utama (rank 0) yang menampilkan pesan akhir setelah sinkronisasi
if rank == 0:
    print("\nAll processes synchronized. Back to main process.", flush=True)