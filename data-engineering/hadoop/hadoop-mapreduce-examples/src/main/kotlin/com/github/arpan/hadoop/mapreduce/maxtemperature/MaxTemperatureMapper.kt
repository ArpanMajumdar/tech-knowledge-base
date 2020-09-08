package com.github.arpan.hadoop.mapreduce.maxtemperature

import org.apache.hadoop.io.IntWritable
import org.apache.hadoop.io.LongWritable
import org.apache.hadoop.io.Text
import org.apache.hadoop.mapred.JobConf
import org.apache.hadoop.mapred.Mapper
import org.apache.hadoop.mapred.OutputCollector
import org.apache.hadoop.mapred.Reporter

class MaxTemperatureMapper : Mapper<LongWritable, Text, Text, IntWritable> {
    private val missingTempVal = 9999
    private val goodQualityRegex = "[01459]".toRegex()

    override fun map(
        key: LongWritable,
        value: Text,
        output: OutputCollector<Text, IntWritable>,
        reporter: Reporter
    ) {
        val line = value.toString()
        val year = line.substring(15, 19)
        val airTemperature: Int =
            if (line[87] == '+') line.substring(88, 92).toInt()
            else line.substring(87, 92).toInt()
        val quality = line.substring(92, 93)

        if (airTemperature != missingTempVal && quality.matches(goodQualityRegex)) {
            output.collect(Text(year), IntWritable(airTemperature))
        }
    }

    override fun configure(job: JobConf?) {
        TODO("Not yet implemented")
    }

    override fun close() {
        TODO("Not yet implemented")
    }
}