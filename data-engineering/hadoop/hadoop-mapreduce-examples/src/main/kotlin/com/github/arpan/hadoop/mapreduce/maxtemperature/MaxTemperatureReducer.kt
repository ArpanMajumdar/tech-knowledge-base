package com.github.arpan.hadoop.mapreduce.maxtemperature

import org.apache.hadoop.io.IntWritable
import org.apache.hadoop.io.Text
import org.apache.hadoop.mapred.JobConf
import org.apache.hadoop.mapred.OutputCollector
import org.apache.hadoop.mapred.Reducer
import org.apache.hadoop.mapred.Reporter

class MaxTemperatureReducer : Reducer<Text, IntWritable, Text, IntWritable> {
    override fun configure(job: JobConf?) {
        TODO("Not yet implemented")
    }

    override fun close() {
        TODO("Not yet implemented")
    }

    override fun reduce(
        key: Text,
        values: MutableIterator<IntWritable>,
        output: OutputCollector<Text, IntWritable>,
        reporter: Reporter
    ) {
        var maxValue = Int.MIN_VALUE
        values.forEach { value ->
            maxValue = maxValue.coerceAtLeast(value.get())
        }
        output.collect(key, IntWritable(maxValue))
    }
}