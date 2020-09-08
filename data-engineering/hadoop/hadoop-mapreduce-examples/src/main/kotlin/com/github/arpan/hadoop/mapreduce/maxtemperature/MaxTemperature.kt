package com.github.arpan.hadoop.mapreduce.maxtemperature

import org.apache.hadoop.fs.Path
import org.apache.hadoop.io.IntWritable
import org.apache.hadoop.io.Text
import org.apache.hadoop.mapreduce.Job
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat
import org.slf4j.LoggerFactory
import kotlin.system.exitProcess

class MaxTemperature {
    private val logger = LoggerFactory.getLogger(MaxTemperature::class.java)

    fun main(args: Array<String>) {
        if (args.size != 2) {
            logger.error("Usage: MaxTemperature <input path> <output path>")
            exitProcess(-1)
        }

        val job = Job.getInstance()
        job.setJarByClass(MaxTemperature::class.java)
        job.jobName = "Max temperature"

        FileInputFormat.addInputPath(job, Path(args[0]))
        FileOutputFormat.setOutputPath(job, Path(args[1]))

        job.outputKeyClass = Text::class.java
        job.outputValueClass = IntWritable::class.java

        exitProcess(if (job.waitForCompletion(true)) 0 else 1)
    }
}