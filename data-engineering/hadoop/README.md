# Hadoop distributed file system (HDFS)

## Commonly used hadoop commands

- Here `hdfs` stands for HDFS client and `dfs` stands for distributed file system API.

| Command | Description
| --- | ---
| `hdfs dfs -help` | Get help
| `hdfs dfs -usage <utility_name>` | See usage for a command
| `hdfs dfs -ls -R -h /data/wiki` | List all files for the given directory, (-R) list files recursively flag, (-h) print file sizes in human readable format
| `hdfs dfs -df -h /data/wiki` | Shows consumed and available storage in the distributed storage
| `hdfs dfs -mkdir -p deep/nested/path` | Create new directory with the given name, (-p) creates nested directories if they don't exist
| `hdfs dfs -rm -r /data/wiki` | Remove a directory (Data is moved to trash, `-skipTrash` deletes it permanently)
| `hdfs dfs -mv file.txt another_file.txt` | Move or rename a file
| `hdfs dfs -put <source location> <HDFS location>` | Move a file from local file system to HDFS
| `hdfs dfs -cat hdfs_test_file.txt` | Prints the file stored at HDFS (use `| tail -n` for printing lines from end)
| `hdfs dfs -tail hdfs_test_file.txt` | Prints last 1KB of the file
| `hdfs dfs -cp hdfs_test_file.txt hdfs_test_file_copy.txt` | Copy file from HDFS to local
| `hdfs dfs -get hdfs_test* .` | Download files starting with `hdfs_test` to local
| `hdfs dfs -getmerge hdfs_test* hdfs_merged.txt` | Download and merge all the files to one file
| `hdfs dfs -find /data/wiki -name ''*part*''` | Find the files matching a specified pattern
